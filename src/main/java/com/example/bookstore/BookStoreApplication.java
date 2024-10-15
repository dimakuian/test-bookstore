package com.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BookStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}

}
