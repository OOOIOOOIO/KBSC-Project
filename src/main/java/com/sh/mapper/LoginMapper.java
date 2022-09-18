package com.sh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.webDomain.LoginFormDTO;

@Mapper
@Repository
public interface LoginMapper {

	/*
	 * common user
	 */
	String cuLogin(String user_id);
	
	UserDTO cuUserInfo(String user_id);
	
	int cuCheckId(String user_id);
	
	
	// findbyid
	
	/*
	 * volunteer org
	 */
	String voLogin(String user_id);
	
	VolunteerOrgDTO voUserInfo(String user_id);
	
	int voCheckId(String user_id);
	
	
	/*
	 * charity org
	 */
	String coLogin(String user_id);
	
	CharityOrgDTO coUserInfo(String user_id);
	
	int coCheckId(String user_id);
    
}
