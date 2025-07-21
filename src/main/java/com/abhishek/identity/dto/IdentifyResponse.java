package com.abhishek.identity.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentifyResponse {
    private Long primaryContactId;
    private List<String> emails;
    private List<String> phoneNumbers;
    private List<Long> secondaryContactIds;
}
