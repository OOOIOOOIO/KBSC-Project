package com.sh.service;

import java.util.List;

import com.sh.domain.DonationCompleteUserDTO;
import com.sh.domain.VolunteerUserApplyDTO;
import com.sh.webDomain.MyDonationInfoDTO;
import com.sh.domain.VolunteerUserCompleteDTO;

public interface UserInfoService {
	
	/*
	 * 봉사 신청 건수
	 */
	Integer getVolunteerApplyTimes(String u_sys_id);
	
	/*
	 * 봉사 완료 건수
	 */
	Integer getVolunteerCompleteTimes(String u_sys_id);
	
	/*
	 * 봉사 신청 내역
	 */
	List<VolunteerUserApplyDTO> getVolunteerApplyHistory(String u_sys_id);
	
	/*
	 * 봉사 완료 내역
	 */
	List<VolunteerUserCompleteDTO> getVolunteerCompleteHistory(String u_sys_id);
	
	/*
	 * 기부 완료 내역
	 */
	List<MyDonationInfoDTO> getDonationCompleteHistory(String u_sys_id);	
	/*
	 * 기부 완료 건수
	 */
	Integer getDonationCompleteTimes(String u_sys_id);	
	
	/*
	 * 기부 총 현금 액수
	 */
	Long getDonationAmountTotalCash(String u_sys_id);

	/*
	 * 기부 총 포인트
	 */
	Integer getDonationAmountTotalPoint(String u_sys_id );
	
	/*
	 * 내 잔여 포인트 확인(select)
	 */
	Integer getMyRestPoint(String u_sys_id);
	
	
}
