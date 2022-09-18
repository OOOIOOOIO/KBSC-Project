package com.sh.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sh.confing.SecurityJavaConfig;
import com.sh.mapper.LoginMapper;


@SpringBootTest
//@WebMvcTest
@MapperScan("com.sh.mapper")
public class LoginServiceImplTest {

	@Autowired
	private SecurityJavaConfig passwordEncoder;
	@Autowired
	private LoginMapper mapper;
	
	
	@Test
	void loginTest() {
		String user_id = "test";
		String rowPw = "test";
		String encryptPW = mapper.coLogin(user_id);
		
		boolean result = passwordEncoder.pwBcryptEncoder().matches(rowPw, encryptPW);
		
		assertTrue(result);
	}
}
