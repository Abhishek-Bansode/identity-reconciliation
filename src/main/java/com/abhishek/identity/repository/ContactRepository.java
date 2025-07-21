package com.abhishek.identity.repository;

import java.util.List;

import com.abhishek.identity.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);
    List<Contact> findByLinkedId(Long linkedId);

}
