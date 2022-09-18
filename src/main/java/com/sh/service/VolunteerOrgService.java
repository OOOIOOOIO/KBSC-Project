package com.sh.service;

import java.util.List;

import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.webDomain.OrgSearchFormDTO;
import com.sh.webDomain.DonationSearchFormDTO;

public interface VolunteerOrgService {
	
	/*
	 * 봉사단체 정보
	 */
	VolunteerOrgDTO getVolunteerOrgInfo(String v_sys_id);
	
	/*
	 * 봉사 공고 등록
	 */
	boolean saveVolunteerInfo(String v_sys_id, VolunteerInfoDTO volunteerInfo);
	
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
