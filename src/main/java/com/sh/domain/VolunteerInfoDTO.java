package com.sh.domain;

import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class VolunteerInfoDTO {
	
	private int v_board_num; // auto increment
	
	@NotBlank
	private String v_title;
	
	@NotBlank
	private String v_contents;
	
	@NotBlank
	private String v_start_date;
	
	@NotBlank
	private String v_end_date;
	
	@NotBlank
	private String v_field;
	
	@NotBlank
	private String v_place;
	
	@NotBlank
	private String v_time;
	
	@NotBlank
	private String v_type;
	
	@NotBlank
	private String v_target;
	
	@NotBlank
	private String r_start_date;
	
	@NotBlank
	private String r_end_date;
	
	@NotBlank
	private String r_num;
	
	@NotBlank
	private String org_name;
	
	@NotBlank
	private String act_type;
	
	private String v_sys_id;
}   
