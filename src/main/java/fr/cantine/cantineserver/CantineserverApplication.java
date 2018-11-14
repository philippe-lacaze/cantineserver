package fr.cantine.cantineserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * Configuration et boostrapping de l'application.
 */
@EnableMongoAuditing
@EnableReactiveMongoRepositories
@SpringBootApplication
public class CantineserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(CantineserverApplication.class, args);
	}
}
