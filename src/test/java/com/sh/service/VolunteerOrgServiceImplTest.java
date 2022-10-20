package com.sh.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.webDomain.LoginFormDTO;
import com.sh.webDomain.SessionConst;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class VolunteerOrgServiceImplTest {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private VolunteerOrgService service;
	
	
	@Test
	public void loginTest() {
		
		LoginFormDTO userInfo = new LoginFormDTO();
		userInfo.setUser_id("dnfls101");
		userInfo.setUser_pw("sh20057950!");
		
		boolean result = loginService.voLogin(userInfo);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void updateBoardTest() {
		
	}
}
