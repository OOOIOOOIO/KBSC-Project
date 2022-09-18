package com.sh.domain;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DonationPaymentCashDTO {

	private int payment_num;
	
	@NotBlank
	private Integer amount_of_money;
	
	@NotBlank
	private String credit_card_number;
	
	private String payment_type;
	
	@NotBlank
	private String credit_card_company;
	
	@NotBlank
	private String credit_card_valid_date;
	
	@NotBlank
	private String credit_card_owner_name;
	
	@NotBlank
	private String credit_card_owner_birth;
	
	private int c_board_num;
	 
	private String u_sys_id;

	private String c_sys_id;
	
	private int co_num;
	
	private int cu_num;
	
}
