package com.sh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.webDomain.OrgSearchFormDTO;
import com.sh.webDomain.DonationSearchFormDTO;

@Mapper
public interface VolunteerOrgMapper {
	
	/*
	 * 봉사단체 정보
	 */
	VolunteerOrgDTO getVolunteerOrgInfo(String v_sys_id);
	
	/*
	 * 봉사 공고 등록
	 */
	int saveVolunteerInfo(VolunteerInfoDTO volunteerInfo);
	
	/*
	 * 봉사 공고 수정
	 */
	int updateVolunteerInfo(VolunteerInfoDTO volunteerInfo);
	
	/*
	 * 봉사 공고 삭제
	 */
	int deleteVolunteerInfo(int v_board_num);
	
	/*
	 * 봉사목록 가져오기
	 */
	List<VolunteerInfoDTO> getVolunteerBoardList();
	
	/*
	 * 봉사정보 읽기(select)
	 */
	VolunteerInfoDTO readVolunteerBoardInfo(int v_board_num);
	
	
	/*
	 * 검색
	 */
	List<VolunteerInfoDTO> searchBoardList(OrgSearchFormDTO searchInfo);
}
