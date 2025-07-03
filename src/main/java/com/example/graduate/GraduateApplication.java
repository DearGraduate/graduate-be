package com.example.graduate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GraduateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraduateApplication.class, args);
	}

}
