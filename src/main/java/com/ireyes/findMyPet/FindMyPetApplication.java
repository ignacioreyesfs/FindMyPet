package com.ireyes.findMyPet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ireyes.findMyPet.service.storage.StorageService;

@SpringBootApplication
public class FindMyPetApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindMyPetApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
