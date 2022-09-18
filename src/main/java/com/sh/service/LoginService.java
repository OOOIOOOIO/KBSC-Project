package com.sh.service;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.webDomain.LoginFormDTO;

public interface LoginService {
	
	/*
	 * common user
	 */
	boolean cuLogin(LoginFormDTO user);
	
	UserDTO cuUserInfo(String user_id);
	
	boolean cuCheckId(String user_id);
	
	
	// findbyid
	
	/*
	 * volunteer org
	 */
	boolean voLogin(LoginFormDTO user);
	
	VolunteerOrgDTO voUserInfo(String user_id);
	
	boolean voCheckId(String user_id);
	
	
	/*
	 * charity org
	 */
	boolean coLogin(LoginFormDTO user);
	
	CharityOrgDTO coUserInfo(String user_id);
	
	boolean coCheckId(String user_id);
}
