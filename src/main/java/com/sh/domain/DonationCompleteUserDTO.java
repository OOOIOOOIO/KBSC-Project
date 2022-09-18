package com.sh.domain;

import lombok.Data;

@Data
public class DonationCompleteUserDTO {
	private int cu_num;
	private String c_title;
	private String donation_date;
	private String donation_type;
	private String c_field;
	private String org_name;
	private String u_sys_id;
	private String c_sys_id;
	private int c_board_num;
}
