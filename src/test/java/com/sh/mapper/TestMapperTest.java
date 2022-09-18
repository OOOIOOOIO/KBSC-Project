package com.sh.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sh.confing.SecurityJavaConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TestMapperTest {
	

	@Autowired
	private TestMapper mapper;
	
	@Autowired
	private SecurityJavaConfig passwordEncoder;
	
	/*
	 * pw 암호화 테스트
	 */
	@Test
	void testEncryptPw() {
		Map<String, String> user = new HashMap<>();
		
		String userPw = "tespw";
		
		String encryPw = passwordEncoder.pwBcryptEncoder().encode(userPw);
		user.put("user_id", "testID");
		user.put("user_pw", encryPw);
		
		int result = mapper.testJoin(user);
		
		assertThat(result).isEqualTo(1);
	}
	
	/*
	 * pw 복호화 테스트
	 */
//	@Test
	void testDcryptPw() {
		Map<String, String> user = new HashMap<>();
		
		String userId = "testID";
		String userPw = "tespw";
		
		String encryPw = passwordEncoder.pwBcryptEncoder().encode(userPw);
		String encryPw2 = passwordEncoder.pwBcryptEncoder().encode(userPw);
		
		// 문자열 테스트
		// 같은 문자열이더라도 만들 때마다 다른 해쉬코드 줌. 당연한건가;
//		assertThat(encryPw).isEqualTo(encryPw2);

		
		// db에서 가져와서 테스트
		String dbPw = mapper.testGetPw(userId);
		
		boolean result = passwordEncoder.pwBcryptEncoder().matches(userPw, dbPw);
		assertTrue(result);
		
	}
}

