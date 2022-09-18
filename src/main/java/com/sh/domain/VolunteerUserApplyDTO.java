package com.sh.domain;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class VolunteerUserApplyDTO {
	
	private int vu_apply_num; // auto increment
	
	private String v_title;
	
	private String apply_date; // default current time
	
	private String v_date;

	private String v_field;

	private String v_place;

	private String v_time;

	private String org_name;
	
	private int v_board_num;

	private String v_sys_id;
	
	private String u_sys_id;
}
