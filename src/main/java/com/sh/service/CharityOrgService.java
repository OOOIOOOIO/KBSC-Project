package com.sh.service;

import java.util.List;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.DonationInfoDTO;
import com.sh.domain.VolunteerInfoDTO;
import com.sh.webDomain.OrgSearchFormDTO;
import com.sh.webDomain.VolunteerSearchFormDTO;

public interface CharityOrgService {
	/*
	 * 기부단체 정보
	 */
	CharityOrgDTO getCharityOrgInfo(String c_sys_id);
	
	/*
	 * 기부 공고 등록
	 */
	boolean saveDonationInfo(DonationInfoDTO donationInfo, String c_sys_id);
	
	/*
	 * 기부 공고 수정
	 */
	boolean updateDonationInfo(DonationInfoDTO donationInfo, String c_sys_id);
	
	/*
	 * 기부 공고 삭제
	 */
	boolean deleteDonationInfo(int c_board_num);
	/*
	 * 기부목록 가져오기
	 */
	List<DonationInfoDTO> getDonationBoardList();
	
	/*
	 * 기부정보 읽기(select)
	 */
	DonationInfoDTO readDonationBoardInfo(int c_board_num);
	
	/*
	 * 해당 공고 현재 기부 인원 수 
	 */
	Long getDonationTimes(int c_board_num);
	
	/*
	 * 해당 공고 현재 기부 금액 현금
	 */
	Long getDonationAmountCash(int c_board_num);
	
	/*
	 * 해당 공고 현재 기부 금액 포인트
	 */
	Long getDonationAmountPoint(int c_board_num);
	
	
	/*
	 * 검색
	 */
	List<DonationInfoDTO> searchBoardList(OrgSearchFormDTO searchInfo);	
	
	
}
