package com.server.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HomeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(HomeApplication.class, args);
	}

}
