package com.abhishek.identity.dto;


import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response with primary contact and linked data")
public class IdentifyResponse {

    @Schema(description = "Primary contact ID", example = "1")
    private Long primaryContactId;

    @Schema(description = "List of email addresses", example = "[\"a@example.com\"]")
    private List<String> emails;

    @Schema(description = "List of phone numbers", example = "[\"1111\", \"2222\"]")
    private List<String> phoneNumbers;

    @Schema(description = "IDs of secondary contacts", example = "[2, 3]")
    private List<Long> secondaryContactIds;

}
