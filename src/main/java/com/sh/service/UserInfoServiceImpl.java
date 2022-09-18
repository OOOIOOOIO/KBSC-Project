package com.sh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sh.domain.DonationCompleteUserDTO;
import com.sh.domain.VolunteerUserApplyDTO;
import com.sh.domain.VolunteerUserCompleteDTO;
import com.sh.mapper.UserInfoMapper;
import com.sh.webDomain.MyDonationInfoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService{

	private final UserInfoMapper mapper;

	@Override
	public List<VolunteerUserApplyDTO> getVolunteerApplyHistory(String u_sys_id) {
		
		return  mapper.getVolunteerApplyHistory(u_sys_id);
	}
	
	@Override
	public Integer getVolunteerApplyTimes(String u_sys_id) {
		List<VolunteerUserApplyDTO> applyInfo = mapper.getVolunteerApplyHistory(u_sys_id);
		
		return applyInfo.size();
	}

	@Override
	public List<VolunteerUserCompleteDTO> getVolunteerCompleteHistory(String u_sys_id) {
		
		return  mapper.getVolunteerCompleteHistory(u_sys_id);
	}
	
	@Override
	public Integer getVolunteerCompleteTimes(String u_sys_id) {
		List<VolunteerUserCompleteDTO> completeInfo = mapper.getVolunteerCompleteHistory(u_sys_id);
		
		return completeInfo.size();
	}

	@Override
	public List<MyDonationInfoDTO> getDonationCompleteHistory(String u_sys_id) {
		List<MyDonationInfoDTO> completeInfo = mapper.getDonationCompleteHistory(u_sys_id);
		
		for(MyDonationInfoDTO info : completeInfo) {
			int cu_num = info.getCu_num();
			
			if(info.getDonation_type().equals("cash")) {
				Integer cash = mapper.getDonationCash(cu_num, u_sys_id);
				info.setAmount_of_money(cash);
			}
			else {
				Integer point = mapper.getDonationPoint(cu_num, u_sys_id);
				info.setAmount_of_point(point);
			}
		}
		
		return completeInfo;
	}
	
	@Override
	public Integer getDonationCompleteTimes(String u_sys_id) {
		List<MyDonationInfoDTO> completeInfo = mapper.getDonationCompleteHistory(u_sys_id);
		
		return completeInfo.size();
	}

	@Override
	public Long getDonationAmountTotalCash(String u_sys_id) {
		Optional<Long> amountOfCash = Optional.ofNullable(mapper.getDonationAmountCash(u_sys_id));
		
		return amountOfCash.orElse(0L);
	}

	@Override
	public Integer getDonationAmountTotalPoint(String u_sys_id) {
		Optional<Integer> amountOfPoint = Optional.ofNullable(mapper.getDonationAmountPoint(u_sys_id));
		
		return amountOfPoint.orElse(0);
	}
	
	@Override
	public Integer getMyRestPoint(String u_sys_id) {
		
		Optional<Integer> vPoint = Optional.ofNullable(mapper.getMyVolunteerPoint(u_sys_id));
		Optional<Integer> dPoint = Optional.ofNullable(mapper.getMyDonationPoint(u_sys_id));
		
		int myPoint = vPoint.orElse(0) - dPoint.orElse(0);
		
		return myPoint;
	}


	


}
