package com.api.landtracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class LandTrackerApplication {

	private final ObjectMapper objectMapper = new ObjectMapper();
	public static void main(String[] args) {
		SpringApplication.run(LandTrackerApplication.class, args);
	}
	@PostConstruct
	public void setUp() {
		objectMapper.registerModule(new JavaTimeModule());


	}
}
