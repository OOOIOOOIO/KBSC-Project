package com.sh.service;

import java.util.List;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.DonationInfoDTO;
import com.sh.domain.DonationPaymentCashDTO;
import com.sh.domain.DonationPaymentPointDTO;
import com.sh.domain.VolunteerInfoDTO;
import com.sh.webDomain.DonationPaymentInfoDTO;
import com.sh.webDomain.VolunteerSearchFormDTO;
import com.sh.webDomain.DonationSearchFormDTO;

public interface DonationUserService {

	/*
	 * 기부 목록 불러오기(select)
	 */
	List<DonationInfoDTO> getDonationBoardList();
	
	/*
	 * 기부 정보 불러오기(select)
	 */
	DonationInfoDTO getDonationBoardInfo(int c_board_num);
	
	/*
	 * 기부 단체 정보 가져오기(select)
	 */
	CharityOrgDTO getCharityOrgInfo(int c_board_num);
	
	/*
	 * 유저 기부 완료
	 */
	boolean saveDonationCompleteInfoUser(int c_board_num, String u_sys_id, String donationtype);
	
	/*
	 * 단체 기부 완료
	 */
	boolean saveDonationCompleteInfoOrg(int c_board_num, String u_sys_id, String donationtype);
	
	/*
	 * 기부 결제 정보 저장(insert)
	 */
	boolean saveDonationPaymentCashUser(DonationPaymentCashDTO paymentInfo, int c_board_num, String u_sys_id);
	
	/*
	 * 기부 결제정보 point 저장(insert)
	 */
	boolean saveDonationPaymentPointUser(DonationPaymentPointDTO paymentInfo, int c_board_num, String u_sys_id);

	/*
	 * 기부 결제 정보 저장(insert)
	 */
	boolean saveDonationPaymentCashOrg(DonationPaymentCashDTO paymentInfo, int c_board_num, String u_sys_id);
	
	/*
	 * 기부 결제정보 point 저장(insert)
	 */
	boolean saveDonationPaymentPointOrg(DonationPaymentPointDTO paymentInfo, int c_board_num, String u_sys_id);
	
	/*
	 * 내 잔여 포인트 확인(select)
	 */
	Integer getMyRestPoint(String u_sys_id);
	
	/*
	 * 기부한 포인트가 잔여 포인트보다 많을 경우
	 */
	boolean checkAvailablePoint(Integer donationPoint, String u_sys_id);
	
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

	boolean checkCreditCard(DonationPaymentCashDTO paymentInfo);
	
	
	/*
	 * 방금 기부한 기부 정보 현금 
	 */
	DonationPaymentInfoDTO getRecentDonationCashInfo(String u_sys_id);
	
	/*
	 * 방금 기부한 기부 정보 포인트
	 */
	DonationPaymentInfoDTO getRecentDonationPointInfo(String u_sys_id);
	
	/*
	 * 검색
	 */
	List<DonationInfoDTO> searchBoardList(VolunteerSearchFormDTO searchInfo);
	
}
