package com.abhishek.identity.controller;

import com.abhishek.identity.dto.IdentifyRequest;
import com.abhishek.identity.dto.IdentifyResponse;
import com.abhishek.identity.service.IdentifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/identify")
public class IdentifyController {

    private final IdentifyService identifyService;

    public IdentifyController(IdentifyService identifyService) {
        this.identifyService = identifyService;
    }

    @Operation(
        summary = "Identify contact based on email or phone",
        description = "Returns a primary contact and list of linked secondary contacts based on reconciliation logic",
        requestBody = @RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(implementation = IdentifyRequest.class)
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(schema = @Schema(implementation = IdentifyResponse.class))
            )
        }
    )
    @PostMapping
    public ResponseEntity<IdentifyResponse> identify(@org.springframework.web.bind.annotation.RequestBody IdentifyRequest request) {
        return ResponseEntity.ok(identifyService.identify(request));
    }
}