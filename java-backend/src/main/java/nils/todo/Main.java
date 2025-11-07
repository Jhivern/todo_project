package nils.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class Main {
    /**
     * Starts the spring application
     * @param args Arguments
     */
    public static void main(String[] args) {
        // Initialize server here
        SpringApplication.run(Main.class, args);

    }
}