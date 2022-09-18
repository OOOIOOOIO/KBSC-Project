package com.sh.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.domain.VolunteerUserApplyDTO;

/*
 * Slf4j 사용하기
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
public class VolunteerUserMapperTest {
    Logger log = (Logger) LoggerFactory.getLogger(VolunteerUserMapperTest.class);
	
	@Autowired
	private VolunteerUserMapper mapper;
	
//	@Test
	void getBoardInfoTest() {
		
		VolunteerInfoDTO info = mapper.readVolunteerBoardInfo(1);
		log.info("===========" + info.toString());
	}
	
//	@Test
	void getVolunterOrgInfoTest() {
		
		VolunteerOrgDTO voInfo = mapper.getVolunteerOrgInfo(1);
		log.info(voInfo.toString());
		
	}
	
	@Test
	void getVolunteerBoardApplyInfoTest() {
		VolunteerUserApplyDTO applyInfo = mapper.getVolunteerBoardApplyInfoUser(2);
		
		log.info(applyInfo.toString());
	}
	
}
