package com.server.home;

import javax.crypto.SecretKey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@SpringBootApplication
public class HomeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(HomeApplication.class, args);
		TrialBean tb = context.getBean(TrialBean.class);
		System.out.println(tb.getPass());
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tb.getPass()));
		System.out.println(key);
	}

}
