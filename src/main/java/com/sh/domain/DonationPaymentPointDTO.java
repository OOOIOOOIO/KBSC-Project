package com.sh.domain;

import lombok.Data;

@Data
public class DonationPaymentPointDTO {

	private int payment_num;
	
	private Integer amount_of_point;
	
	private int c_board_num;
	 
	private String u_sys_id;

	private String c_sys_id;
	
	private int co_num;
	
	private int cu_num;
}
