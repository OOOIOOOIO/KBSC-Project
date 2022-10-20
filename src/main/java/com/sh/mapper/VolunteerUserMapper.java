package com.sh.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgApplyDTO;
import com.sh.domain.VolunteerOrgCompleteDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.domain.VolunteerPointCertificateOrgDTO;
import com.sh.domain.VolunteerPointCertificateUserDTO;
import com.sh.domain.VolunteerUserApplyDTO;
import com.sh.webDomain.DonationSearchFormDTO;
import com.sh.domain.VolunteerUserCompleteDTO;

@Mapper
public interface VolunteerUserMapper {
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
	 * 봉사신청자 수 가져오기(select)
	 */
	int getApplyVolunteerNum(@Param("v_board_num") int v_board_num, @Param("v_date") LocalDate v_date);
	
	
	/*
	 * 봉사 신청 유저 테이블(insert)
	 */
	int saveApplyedVolunteerInfoUser(VolunteerUserApplyDTO applyInfo);
	
	
	/*
	 * 봉사 신청 봉사단체 (insert)
	 */
	int saveApplyedVolunteerInfoOrg(VolunteerOrgApplyDTO applyInfo);
	
	/*
	 * 봉사완료 유저 (insert)
	 */
	int saveCompleteVolunteerInfoUser(VolunteerUserCompleteDTO completeInfo);
	
	/*
	 * 봉사완료 단체 (insert)
	 */
	int saveCompleteVolunteerInfoOrg(VolunteerOrgCompleteDTO completeInfo);
	
	/*
	 * 봉사신청에 필요한 정보 가져오기(select)
	 */
	VolunteerUserApplyDTO getVolunteerBoardApplyInfoUser(int v_board_num);
	/*
	 * 봉사신청에 필요한 정보 가져오기(select)
	 */
	VolunteerOrgApplyDTO getVolunteerBoardApplyInfoOrg(int v_board_num);

	/*
	 * 유저 봉사완료에 필요한 정보 가져오기(select)
	 */
	List<VolunteerUserCompleteDTO> getVolunteerBoardCompleteInfoUser(@Param("v_board_num") int v_board_num, @Param("u_sys_id") String u_sys_id);
	
	/*
	 * 단체 봉사완료에 필요한 정보 가져오기(select)
	 */
	List<VolunteerOrgCompleteDTO> getVolunteerBoardCompleteInfoOrg(@Param("v_board_num") int v_board_num, @Param("u_sys_id") String u_sys_id);
	
	/*
	 * 유저 봉사 포인트, 인증서 (insert)
	 */
	int saveCompleteVolunteerPointCertificateUser(VolunteerPointCertificateUserDTO pcInfo);

	/*
	 * 단체 봉사 포인트, 인증서 (insert)
	 */
	int saveCompleteVolunteerPointCertificateOrg(VolunteerPointCertificateOrgDTO pcInfo);
	
	 /*
	  * 봉사 포인트완료에 필요한 정보 가져오기(select)
	  */

	List<VolunteerPointCertificateUserDTO> getVolunteerCompleteUserInfo(@Param("v_board_num") int v_board_num, @Param("u_sys_id") String u_sys_id);
	/*
	 * 봉사 포인트완료에 필요한 정보 가져오기(select)
	 */
	List<VolunteerPointCertificateOrgDTO> getVolunteerCompleteOrgInfo(@Param("v_board_num") int v_board_num, @Param("u_sys_id") String u_sys_id);
	
	/*
	 * 봉사 공고에 있는 봉사 시간 가져오기
	 */
	int getVolunteerTime(int v_board_num);
	
	/*
	 * 검색
	 */
	List<VolunteerInfoDTO> searchBoardList(DonationSearchFormDTO searchInfo);
	
}
