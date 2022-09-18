package com.sh.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sh.mapper.VolunteerUserMapper;
import com.sh.mapper.VolunteerUserMapperTest;

@SpringBootTest
@MapperScan("com.sh.mapper")
public class VolunteerUserServiceTest {

	@Autowired
	VolunteerUserMapper mapper;
	
	Logger log = (Logger) LoggerFactory.getLogger(VolunteerUserMapperTest.class);
	
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
