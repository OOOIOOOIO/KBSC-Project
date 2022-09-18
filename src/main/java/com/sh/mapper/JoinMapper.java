package com.sh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerOrgDTO;

@Mapper
@Repository
public interface JoinMapper {

	/*
	 * common user
	 */
	
	int cuJoin(UserDTO user);
	
	int cuCheckId(String user_id);
	
	int cuCheckEmail(String user_email);
	
	int cuCheckPhone(String user_phone);
	
	// findbyid
	
	/*
	 * volunteer org
	 */
	
	int voJoin(VolunteerOrgDTO user);
	
	int voCheckId(String user_id);
	
	int voCheckEmail(String user_email);
	
	int voCheckPhone(String user_phone);
	
	/*
	 * charity org
	 */
	
	int coJoin(CharityOrgDTO user);
	
	int coCheckId(String user_id);
    
	int coCheckEmail(String user_email);
	
	int coCheckPhone(String user_phone);
}
