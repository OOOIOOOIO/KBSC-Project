package com.sh.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sh.domain.DonationCompleteUserDTO;
import com.sh.domain.VolunteerUserApplyDTO;
import com.sh.webDomain.MyDonationInfoDTO;
import com.sh.domain.VolunteerUserCompleteDTO;

@Mapper
public interface UserInfoMapper {

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
	 * 기부 현금
	 */
	Long getDonationAmountCash(String u_sys_id);
	
	/*
	 * 기부 포인트
	 */
	Integer getDonationAmountPoint(String u_sys_id);
	
	/*
	 * 내 잔여 포인트 확인(select)
	 */
	Integer getMyVolunteerPoint(String u_sys_id);
	
	/*
	 * 내 사용 포인트 확인(select)
	 */
	Integer getMyDonationPoint(String u_sys_id);
	
	/*
	 * 기부한 현금 확인
	 */
	Integer getDonationCash(@Param("cu_num") int cu_num, @Param("u_sys_id") String u_sys_id);
	
	/*
	 * 기부한 포인트 확인
	 */
	Integer getDonationPoint(@Param("cu_num") int cu_num, @Param("u_sys_id") String u_sys_id);
	
}
