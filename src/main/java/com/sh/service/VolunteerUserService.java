package com.sh.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.domain.VolunteerPointCertificateUserDTO;
import com.sh.domain.VolunteerUserApplyDTO;
import com.sh.domain.VolunteerUserCompleteDTO;
import com.sh.webDomain.VolunteerApplyFormDTO;
import com.sh.webDomain.DonationSearchFormDTO;

public interface VolunteerUserService {
	/*
	 * 봉사목록 불러오기(select)
	 */
	List<VolunteerInfoDTO> getVolunteerBoardList(/*키워드? 아님 숫자 0 이상?*/);
	
	/*
	 * 봉사정보 읽기(select)
	 */
	VolunteerInfoDTO readVolunteerBoardInfo(int v_board_num);
	
	/*
	 * 봉사단체 정보 가져오기(select, subquery) 
	 */
	VolunteerOrgDTO getVolunteerOrgInfo(int v_board_num);
	
	/*
	 * 봉사신청자 수 가져오기 
	 */
	List<Integer> getApplyVolunteerNum(int v_board_num, List<LocalDate> dateList, int r_num);
	
	/*
	 * 봉사신청 가능 날짜 가져오기
	 */
	List<LocalDate> availableDateList(String v_start_date, String v_end_date);
	
	
	/*
	 * 봉사 신청 가능 날짜와 신청 가능 인원 List to Map
	 */
	LinkedHashMap<LocalDate, Integer> ListToMap(List<LocalDate> dateList, List<Integer> avNum);
	
	/*
	 * 봉사신청 완료 테이블(insert) : 봉사단체가 수락 누르면.
	 */
	boolean saveApplyVolunteerInfo(VolunteerApplyFormDTO applyInfo, String u_sys_id);

	/*
	 * 봉사신청 완료 테이블(insert) : 봉사단체가 수락 누르면.
	 */
	boolean saveCompleteVolunteerInfo(VolunteerApplyFormDTO applyInfo, String u_sys_id);
	
	
	/*
	 * 봉사 포인트, 인증서 (insert)
	 */
	boolean saveCompletVolunteerPointCertificate(VolunteerApplyFormDTO applyInfo, String u_sys_id);
	
	/*
	 * 검색
	 */
	List<VolunteerInfoDTO> searchBoardList(DonationSearchFormDTO searchInfo);
	 
}
