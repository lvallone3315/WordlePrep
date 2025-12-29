package org.fdu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for Springboot web interface
 * SpringBootApplication annotation triggers: auto-configuration, component scanning and
 *   marking this class as the configuration source
 */
@SpringBootApplication
public class WordleWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordleWebApplication.class, args);
    }
}