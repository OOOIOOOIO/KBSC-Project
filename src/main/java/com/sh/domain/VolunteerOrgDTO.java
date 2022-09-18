package com.sh.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class VolunteerOrgDTO {
	private String v_sys_id;
	
	@NotBlank
	private String name_kr;
	
	@NotBlank
	private String name_en;
	
	@NotBlank
	private String user_id;
	
	@NotBlank
	private String user_pw;
	
	@NotBlank
	private String user_phone;
	
	@Email
	private String user_email;
	
	@NotBlank
	private String user_birth;
	
	@NotBlank
	private String zipcode;
	
	@NotBlank
	private String addr;
	
	@NotBlank
	private String addr_detail;
	
	@Nullable
	private String addr_etc;
	
	@NotBlank
	private String org_name;
	
	@NotBlank
	private String org_phone;
	
	@NotBlank
	private String business_num;
	
	
	private String org_code;
}
