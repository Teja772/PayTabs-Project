package com.bank.system2_corebank.controller;

import com.bank.system2_corebank.dto.TransactionRequest;
import com.bank.system2_corebank.entity.Card;
import com.bank.system2_corebank.entity.Transaction;
import com.bank.system2_corebank.repository.CardRepository;
import com.bank.system2_corebank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/process")
public class BankController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // ---------------------------------------------------------
    // HELPER: This converts a plain PIN (1234) into a secure Hash
    // ---------------------------------------------------------
    private String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing PIN", e);
        }
    }

    // ---------------------------------------------------------
    // MAIN: Process the Transaction
    // ---------------------------------------------------------
    @PostMapping
    public String processTransaction(@RequestBody TransactionRequest request) {
        // 1. Prepare the log
        Transaction log = new Transaction();
        log.setCardNumber(request.getCardNumber());
        log.setAmount(request.getAmount());
        log.setType(request.getType());
        log.setTimestamp(LocalDateTime.now());

        // 2. Find the card in database
        Card card = cardRepository.findById(request.getCardNumber()).orElse(null);
        
        if (card == null) {
            log.setStatus("FAILED");
            log.setReason("Invalid Card Number");
            transactionRepository.save(log);
            return "FAILURE: Invalid Card Number";
        }

        // 3. Verify PIN (Compare the Hash!)
        String inputHash = hashPin(request.getPin());
        if (!card.getPinHash().equals(inputHash)) {
            log.setStatus("FAILED");
            log.setReason("Invalid PIN");
            transactionRepository.save(log);
            return "FAILURE: Invalid PIN";
        }

        // 4. Handle Withdraw or Topup
        if ("withdraw".equalsIgnoreCase(request.getType())) {
            if (card.getBalance() < request.getAmount()) {
                log.setStatus("FAILED");
                log.setReason("Insufficient Balance");
                transactionRepository.save(log);
                return "FAILURE: Insufficient Balance";
            }
            card.setBalance(card.getBalance() - request.getAmount());
        
        } else if ("topup".equalsIgnoreCase(request.getType())) {
            card.setBalance(card.getBalance() + request.getAmount());
        }

        // 5. Save changes
        cardRepository.save(card); // Update money
        
        log.setStatus("SUCCESS");
        log.setReason("Transaction Completed");
        transactionRepository.save(log); // Save history

        return "SUCCESS: New Balance is " + card.getBalance();
    }

    // ---------------------------------------------------------
    // ADMIN: Get all transactions
    // ---------------------------------------------------------
    @GetMapping("/logs")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}