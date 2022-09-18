package com.sh.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.sh.confing.SecurityJavaConfig;
import com.sh.domain.CharityOrgDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.mapper.JoinMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService {

	private final JoinMapper mapper;
	private final SecurityJavaConfig passwordEncoder;
	
	/*m
	 * 1. join인 로직이 똑같은데 이거 그냥 controller에서 request param으로 0 , 1, 2 나눠서 할까 흠..
	 * 
	 * ================================js 우편번호 왜 안됨?
	 */
	
	/*
	 * commonUser
	 */
	
	@Override
	public boolean cuJoin(UserDTO user) {

		// u_sys_id
		UUID userUuid = UUID.randomUUID();
		user.setU_sys_id(userUuid.toString());
		
		// pw 암호화
		String userPw = user.getUser_pw();
		String encryPw = passwordEncoder.pwBcryptEncoder().encode(userPw);
		user.setUser_pw(encryPw);
		
		return 1 == mapper.cuJoin(user);
	}

	@Override
	public boolean cuCheckId(String user_id) {
		int result = mapper.cuCheckId(user_id);
		return result > 0;
	}

	@Override
	public boolean cuCheckEmail(String user_email) {
		int result = mapper.cuCheckEmail(user_email);
		return result > 0;
	}

	@Override
	public boolean cuCheckPhone(String user_phone) {
		int result = mapper.cuCheckPhone(user_phone);
		return result > 0;
	}

	/*
	 * volunteer org
	 */
	
	@Override
	public boolean voJoin(VolunteerOrgDTO user) {
		
		// v_sys_id
		UUID userUuid = UUID.randomUUID();
		user.setV_sys_id(userUuid.toString());
		
		// pw 암호화
		String userPw = user.getUser_pw();
		String encryPw = passwordEncoder.pwBcryptEncoder().encode(userPw);
		user.setUser_pw(encryPw);
		
		// org_code
		UUID orgCode = UUID.randomUUID();
		user.setOrg_code(orgCode.toString());
		
		return 1 == mapper.voJoin(user);
	}

	@Override
	public boolean voCheckId(String user_id) {
		int result = mapper.voCheckId(user_id);
		return result > 0;
	}

	@Override
	public boolean voCheckEmail(String user_email) {
		int result = mapper.voCheckEmail(user_email);
		return result > 0;
	}

	@Override
	public boolean voCheckPhone(String user_phone) {
		int result = mapper.voCheckPhone(user_phone);
		return result > 0;
	}

	/*
	 * charity org
	 */
	@Override
	public boolean coJoin(CharityOrgDTO user) {
		
		// c_sys_id
		UUID userUuid = UUID.randomUUID();
		user.setC_sys_id(userUuid.toString());
		
		// pw 암호화
		String userPw = user.getUser_pw();
		String encryPw = passwordEncoder.pwBcryptEncoder().encode(userPw);
		user.setUser_pw(encryPw);
		
		// org_code
		UUID orgCode = UUID.randomUUID();
		user.setOrg_code(orgCode.toString());
		
		return 1 == mapper.coJoin(user);
	}

	@Override
	public boolean coCheckId(String user_id) {
		int result = mapper.coCheckId(user_id);
		return result > 0;
	}

	@Override
	public boolean coCheckEmail(String user_email) {
		int result = mapper.coCheckEmail(user_email);
		return result > 0;
	}

	@Override
	public boolean coCheckPhone(String user_phone) {
		int result = mapper.coCheckPhone(user_phone);
		return result > 0;
	}


}
