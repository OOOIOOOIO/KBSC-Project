package com.sh.domain;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DonationInfoDTO {
	private int c_board_num; // auto increment

	@NotBlank
	private String c_title;

	@NotBlank
	private String c_contents;

	@NotBlank
	private String c_field;

	@NotBlank
	private String org_name;

	private String c_sys_id;
	
	
	
	private Long donationTimes;
	
	private Long donationCash;
	
	private Long donationPoint;
	
}
