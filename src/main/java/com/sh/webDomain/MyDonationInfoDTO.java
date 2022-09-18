package com.sh.webDomain;

import lombok.Data;

@Data
public class MyDonationInfoDTO {
	private int cu_num;
	private String c_title;
	private String donation_date;
	private String donation_type;
	private String c_field;
	private String org_name;
	private String u_sys_id;
	private String c_sys_id;
	private int c_board_num;
	private int amount_of_money;
	private int amount_of_point;
}
