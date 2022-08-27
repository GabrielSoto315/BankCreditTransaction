package com.Bank.BankCreditTransaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class BankCreditTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankCreditTransactionApplication.class, args);
	}

}
