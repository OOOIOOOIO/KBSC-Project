package com.sh.webDomain;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginFormDTO {
	@NotBlank
	private String user_id;
	
	@NotBlank
	private String user_pw;
}
