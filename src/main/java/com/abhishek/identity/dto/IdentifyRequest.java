package com.abhishek.identity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request containing email and/or phone number to identify contact")
public class IdentifyRequest {

    @Schema(description = "Email address of the user", example = "a@example.com")
    private String email;

    @Schema(description = "Phone number of the user", example = "9999")
    private String phoneNumber;

}
