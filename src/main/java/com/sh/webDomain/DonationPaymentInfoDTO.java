package com.sh.webDomain;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class DonationPaymentInfoDTO {
	private int cu_num;
	
	private String org_name;
	private String donation_type;
	private String donation_date;
	private String name_kr;
	
	@Nullable
	private Integer amount_of_point;
	
	@Nullable
	private Long amount_of_money;
}
