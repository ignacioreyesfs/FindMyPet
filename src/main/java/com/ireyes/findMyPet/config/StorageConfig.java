package com.ireyes.findMyPet.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ireyes.findMyPet.service.storage.StorageService;

@Configuration
public class StorageConfig {
	@Bean
	CommandLineRunner initStorage(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
}
