package com.sh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.DonationCompleteUserDTO;
import com.sh.domain.DonationCompleteOrgDTO;
import com.sh.domain.DonationInfoDTO;
import com.sh.domain.DonationPaymentCashDTO;
import com.sh.domain.DonationPaymentPointDTO;
import com.sh.webDomain.DonationCompleteInfoDTO;
import com.sh.webDomain.DonationPaymentInfoDTO;
import com.sh.webDomain.VolunteerSearchFormDTO;

@Mapper
@Repository
public interface DonationUserMapper {
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
	 * 단체 기부 완료 테이블에 필요한 board 정보(select)
	 */
	DonationCompleteUserDTO getSomeBoardInfoUser(int c_board_num);
	
	/*
	 * 단체 기부 완료 테이블에 필요한 board 정보(select)
	 */
	DonationCompleteOrgDTO getSomeBoardInfoOrg(int c_board_num);
	
	/*
	 * 유저 기부 완료
	 */
	int saveDonationCompleteInfoUser(DonationCompleteUserDTO completeUser);
	
	/*
	 * 단체 기부 완료
	 */
	int saveDonationCompleteInfoOrg(DonationCompleteOrgDTO completeOrg);
	
	/*
	 * 기부 결제정보 저장에 필요한 co_num, cu_num 
	 */
	DonationCompleteInfoDTO getCompleteInfo(@Param("u_sys_id") String u_sys_id, @Param("c_board_num") int c_board_num);
	
	/*
	 * 유저 기부 결제정보 cash 저장(insert)
	 */
	int saveDonationPaymentCashUser(DonationPaymentCashDTO paymentInfo);
	
	/*
	 * 유저 기부 결제정보 point 저장(insert)
	 */
	int saveDonationPaymentPointUser(DonationPaymentPointDTO paymentInfo);
	
	/*
	 * 단체 기부 결제정보 cash 저장(insert)
	 */
	int saveDonationPaymentCashOrg(DonationPaymentCashDTO paymentInfo);
	
	/*
	 * 단체 기부 결제정보 point 저장(insert)
	 */
	int saveDonationPaymentPointOrg(DonationPaymentPointDTO paymentInfo);
	
	/*
	 * 내 잔여 포인트 확인(select)
	 */
	Integer getMyVolunteerPoint(String u_sys_id);
	
	/*
	 * 내 사용 포인트 확인(select)
	 */
	Integer getMyDonationPoint(String u_sys_id);
	
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
	 * 방금 기부한 기부 정보 
	 */
	DonationPaymentInfoDTO getRecentDonationInfo(String u_sys_id);
	
	/*
	 * 방금 기부한 금액
	 */
	Long getRecentPaymentCash(int cu_num);
	
	/*
	 * 방금 기부한 포인트액
	 */
	Integer getRecentPaymentPoint(int cu_num);
	
	/*
	 * 검색
	 */
	List<DonationInfoDTO> searchBoardList(VolunteerSearchFormDTO searchInfo);
	
	

}
