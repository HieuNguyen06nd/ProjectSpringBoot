package com.hieunguyen.lakeSide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hieunguyen.lakeSide")
public class LakeSideApplication {

	public static void main(String[] args) {
		SpringApplication.run(LakeSideApplication.class, args);
	}

}
