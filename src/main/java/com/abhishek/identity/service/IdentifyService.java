package com.abhishek.identity.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import com.abhishek.identity.dto.IdentifyRequest;
import com.abhishek.identity.dto.IdentifyResponse;
import com.abhishek.identity.entity.Contact;
import com.abhishek.identity.entity.Contact.LinkPrecedence;
import com.abhishek.identity.repository.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class IdentifyService {

    private final ContactRepository contactRepository;

    public IdentifyService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public IdentifyResponse identify(IdentifyRequest request) {
        List<Contact> matchedContacts = findMatchingContacts(request.getEmail(), request.getPhoneNumber());

        if (matchedContacts.isEmpty()) {
            Contact newPrimary = createNewPrimaryContact(request);
            return buildResponse(newPrimary, List.of());
        }

        // Group all related contacts (direct + indirect)
        List<Contact> allRelated = getTransitiveClosureOfContacts(matchedContacts);

        Contact primary = getOldestPrimaryContact(allRelated);

        // Check if any other primary needs to be demoted
        List<Contact> otherPrimaries = allRelated.stream()
                .filter(c -> !c.getId().equals(primary.getId()) && c.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .toList();

        for (Contact other : otherPrimaries) {
            other.setLinkPrecedence(LinkPrecedence.SECONDARY);
            other.setLinkedId(primary.getId());
            other.setUpdatedAt(LocalDateTime.now());
            contactRepository.save(other);

            // Also re-link their secondaries
            List<Contact> theirSecondaries = contactRepository.findByLinkedId(other.getId());
            for (Contact sec : theirSecondaries) {
                sec.setLinkedId(primary.getId());
                sec.setUpdatedAt(LocalDateTime.now());
                contactRepository.save(sec);
            }
        }

        // If request info is new, add it as a new secondary
        Contact newSecondary = createOrUpdateIfNotExists(request, allRelated, primary);
        if (newSecondary != null) {
            allRelated.add(newSecondary);
        }

        return buildResponse(primary, allRelated);
    }

    // ------------------ Helper Methods ------------------

    private Contact createOrUpdateIfNotExists(IdentifyRequest request, List<Contact> existing, Contact primary) {
        boolean emailExists = request.getEmail() != null &&
                existing.stream().anyMatch(c -> request.getEmail().equalsIgnoreCase(c.getEmail()));
        boolean phoneExists = request.getPhoneNumber() != null &&
                existing.stream().anyMatch(c -> request.getPhoneNumber().equalsIgnoreCase(c.getPhoneNumber()));

        if (emailExists && phoneExists) {
            return null;
        }

        Contact newSecondary = Contact.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .linkPrecedence(LinkPrecedence.SECONDARY)
                .linkedId(primary.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return contactRepository.save(newSecondary);
    }


    private Contact getOldestPrimaryContact(List<Contact> contacts) {
        return contacts.stream()
                .filter(c -> c.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .min(Comparator.comparing(Contact::getCreatedAt))
                .orElseThrow(() -> new IllegalStateException("No primary contact found"));
    }

    private List<Contact> getTransitiveClosureOfContacts(List<Contact> initialMatches) {
        Set<Long> visited = new HashSet<>();
        List<Contact> closure = new ArrayList<>();

        Queue<Contact> queue = new LinkedList<>(initialMatches);

        while (!queue.isEmpty()) {
            Contact current = queue.poll();
            if (visited.contains(current.getId())) {
                continue;
            }

            visited.add(current.getId());
            closure.add(current);

            // Get linked contacts
            if (current.getLinkPrecedence() == LinkPrecedence.PRIMARY) {
                queue.addAll(contactRepository.findByLinkedId(current.getId()));
            } else if (current.getLinkedId() != null) {
                contactRepository.findById(current.getLinkedId()).ifPresent(queue::add);
            }
        }

        return closure;
    }

    private static IdentifyResponse buildResponse(Contact primary, List<Contact> allContacts) {
        if (allContacts == null || allContacts.isEmpty()) {
            allContacts = List.of(primary);
        }

        Set<String> emails = allContacts.stream()
                .map(Contact::getEmail)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> phones = allContacts.stream()
                .map(Contact::getPhoneNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Long> secondaryIds = allContacts.stream()
                .filter(c -> !c.getId().equals(primary.getId()))
                .map(Contact::getId)
                .collect(Collectors.toList());

        return IdentifyResponse.builder()
                .primaryContactId(primary.getId())
                .emails(new ArrayList<>(emails))
                .phoneNumbers(new ArrayList<>(phones))
                .secondaryContactIds(secondaryIds)
                .build();
    }

    private Contact createNewPrimaryContact(IdentifyRequest request) {
        Contact contact = Contact.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .linkPrecedence(LinkPrecedence.PRIMARY)
                .linkedId(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return contactRepository.save(contact);
    }

    private List<Contact> findMatchingContacts(String email, String phone) {
        return contactRepository.findByEmailOrPhoneNumber(email, phone);
    }

}
