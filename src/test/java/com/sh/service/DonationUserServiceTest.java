package com.sh.service;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ObjectUtils;

@SpringBootApplication
public class DonationUserServiceTest {
	
	@Test
	void checkCreditCard() {
		Integer amount_of_money = 1234;
		String credit_card_number = "123456789012";
		String credit_card_company = "kb";
		String credit_card_valid_date = "11/22";
		String credit_card_owner_name = "김성호";
		String credit_card_owner_birth = "19980111";
		
		
		if(amount_of_money == 0) {
			System.out.println("1");
		}
		if(ObjectUtils.isEmpty(credit_card_number) || !Pattern.matches("(\\d{11,12})$", credit_card_number)) {
			System.out.println("2");
		}
		if(ObjectUtils.isEmpty(credit_card_company)) {
			System.out.println("3");
		}
		if(ObjectUtils.isEmpty(credit_card_valid_date) || !Pattern.matches("(0[1-9]|1[012])/(2[0-9]|3[0-9])", credit_card_valid_date)) {
			System.out.println("4");
		}
		if(ObjectUtils.isEmpty(credit_card_owner_name)) {
			System.out.println("5");
		}
		if(ObjectUtils.isEmpty(credit_card_owner_birth) || !Pattern.matches("[0-9]{8}$", credit_card_owner_birth)) {
			System.out.println("6");
		}
	}
	
	
}
