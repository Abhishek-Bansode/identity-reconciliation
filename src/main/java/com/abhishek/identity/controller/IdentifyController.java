package com.abhishek.identity.controller;

import com.abhishek.identity.dto.IdentifyRequest;
import com.abhishek.identity.dto.IdentifyResponse;
import com.abhishek.identity.service.IdentifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/identify")
public class IdentifyController {

    private final IdentifyService identifyService;

    public IdentifyController(IdentifyService identifyService) {
        this.identifyService = identifyService;
    }

    @PostMapping
    public ResponseEntity<IdentifyResponse> identify(@RequestBody IdentifyRequest request) {
        return ResponseEntity.ok(identifyService.identify(request));
    }
}