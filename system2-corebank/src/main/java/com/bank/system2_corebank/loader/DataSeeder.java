package com.bank.system2_corebank.loader;
import com.bank.system2_corebank.entity.Card;
import com.bank.system2_corebank.repository.CardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CardRepository cardRepository;

    public DataSeeder(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create a default card for testing
        if (cardRepository.count() == 0) {
            Card card = new Card();
            card.setCardNumber("4123456789012345");
            card.setBalance(1000.00);
            card.setCustomerName("Test User");
            
            // We must HASH the PIN "1234" before saving
            // (SHA-256 Hash of "1234" is hardcoded here for simplicity)
            // You can also use the same hashPin function from the controller
            card.setPinHash("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4");

            cardRepository.save(card);
            System.out.println("--------------------------------------------");
            System.out.println("✅ TEST DATA LOADED: Card 4123456789012345 | PIN 1234");
            System.out.println("--------------------------------------------");
        }
    }
}