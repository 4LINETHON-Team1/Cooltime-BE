package com.likelion.fourthlinethon.team1.cooltime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CooltimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CooltimeApplication.class, args);
	}

}
