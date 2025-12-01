package com.bank.system1_gateway.controller;

import com.bank.system1_gateway.dto.TransactionRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // This allows React to talk to us
public class GatewayController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/transaction")
    public String handleTransaction(@RequestBody TransactionRequest request) {
        
        System.out.println("Received request for card: " + request.getCardNumber());

        // 1. Validation: Check if card starts with '4'
        if (request.getCardNumber() == null || !request.getCardNumber().startsWith("4")) {
            System.out.println("DECLINED: Invalid Card Range");
            return "DECLINED: Card range not supported (Must start with 4)";
        }

        // 2. If valid, send to System 2 (The Vault)
        String coreBankUrl = "http://localhost:8082/process";
        
        try {
            // This forwards the message to System 2 and waits for the answer
            return restTemplate.postForObject(coreBankUrl, request, String.class);
        } catch (Exception e) {
            return "ERROR: Could not connect to Core Bank (System 2)";
        }
    }
}