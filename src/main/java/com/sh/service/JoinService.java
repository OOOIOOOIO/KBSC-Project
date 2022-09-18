package com.sh.service;

import org.springframework.validation.BindingResult;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerOrgDTO;

public interface JoinService {
	/*
	 * common user
	 */
	
	boolean cuJoin(UserDTO user);
	
	boolean cuCheckId(String user_id);
	
	boolean cuCheckEmail(String user_email);
	
	boolean cuCheckPhone(String user_phone);
	
	
	// findbyid
	
	/*
	 * volunteer org
	 */
	
	boolean voJoin(VolunteerOrgDTO user);
	
	boolean voCheckId(String user_id);
	
	boolean voCheckEmail(String user_email);
	
	boolean voCheckPhone(String user_phone);
	
	/*
	 * charity org
	 */
	
	boolean coJoin(CharityOrgDTO user);
	
	boolean coCheckId(String user_id);
    
	boolean coCheckEmail(String user_email);
	
	boolean coCheckPhone(String user_phone);
}
