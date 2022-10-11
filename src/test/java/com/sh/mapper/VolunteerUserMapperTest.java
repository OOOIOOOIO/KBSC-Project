package com.sh.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	@Test
	void applyTest() {
		
		List<Integer> applyNum = new ArrayList<>();
		String v_start_date = "2022-09-02";
		String v_end_date = "2022-09-10";
		int v_board_num = 1;
		
		LocalDate start = LocalDate.parse(v_start_date);
		LocalDate end = LocalDate.parse(v_end_date);
		
		List<LocalDate> dateList =  start.datesUntil(end).collect(Collectors.toList());
		
		
		
		for(int i = 0; i < dateList.size(); i++) {
			
			int num = mapper.getApplyVolunteerNum(v_board_num, dateList.get(i));
			
			applyNum.add(num);
			
		}
		
		for(LocalDate date : dateList) {
			log.info("=====봉사 신청 날짜====== "+date.toString());
		}
		
		applyNum.forEach(num -> log.info("======해당 날짜에 지원한 자원자 수======" + num));
		
	}
	
}
