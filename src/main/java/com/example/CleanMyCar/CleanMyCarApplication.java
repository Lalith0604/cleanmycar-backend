package com.example.CleanMyCar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
public class CleanMyCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanMyCarApplication.class, args);
	}

}
