package com.sh.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sh.confing.SecurityJavaConfig;
import com.sh.domain.CharityOrgDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.mapper.LoginMapper;
import com.sh.webDomain.LoginFormDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	private final LoginMapper mapper;
	private final SecurityJavaConfig passwordEncoder;
	/*
	 * 1. login인 로직이 똑같은데 이거 그냥 controller에서 request param으로 0 , 1, 2 나눠서 할까 흠.. 
	 * 
	 */
	
	/*
	 * common user
	 */
	@Override
	public boolean cuLogin(LoginFormDTO user) {
		String user_id = user.getUser_id();
		String rowPw = user.getUser_pw();
		String encryptPw = mapper.cuLogin(user_id);
		
		boolean result = passwordEncoder.pwBcryptEncoder().matches(rowPw, encryptPw);
		
		return result;
	}
	
	@Override
	public UserDTO cuUserInfo(String user_id) {
		
		UserDTO user =  mapper.cuUserInfo(user_id);
		
		return user;
	}
	
	
	@Override
	public boolean cuCheckId(String user_id) {
		
		
		return 1 == mapper.cuCheckId(user_id);
	}
	
	/*
	 * volunteer org
	 */
	@Override
	public boolean voLogin(LoginFormDTO user) {
		String user_id = user.getUser_id();
		String rowPw = user.getUser_pw();
		String encryptPW = mapper.voLogin(user_id);

		boolean result = passwordEncoder.pwBcryptEncoder().matches(rowPw, encryptPW);
		
		return result;
	}
	
	
	@Override
	public VolunteerOrgDTO voUserInfo(String user_id) {
		VolunteerOrgDTO voUser = mapper.voUserInfo(user_id);
		
		return voUser;
	}
	
	@Override
	public boolean voCheckId(String user_id) {
		
		return 1== mapper.voCheckId(user_id);
	}
	
	/*
	 * charity org
	 */
	@Override
	public boolean coLogin(LoginFormDTO user) {
		String user_id = user.getUser_id();
		String rowPw = user.getUser_pw();
		String encryptPW = mapper.coLogin(user_id);
		
		boolean result = passwordEncoder.pwBcryptEncoder().matches(rowPw, encryptPW);
		
		return result;
	}
	
	
	@Override
	public CharityOrgDTO coUserInfo(String user_id) {
		CharityOrgDTO coUser = mapper.coUserInfo(user_id);
		
		return coUser;
	}
	
	
	@Override
	public boolean coCheckId(String user_id) {

		return 1 == mapper.coCheckId(user_id);
	}
	


}
