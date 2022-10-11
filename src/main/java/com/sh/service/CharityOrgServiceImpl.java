package com.sh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.DonationInfoDTO;
import com.sh.mapper.CharityOrgMapper;
import com.sh.webDomain.OrgSearchFormDTO;
import com.sh.webDomain.VolunteerSearchFormDTO;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CharityOrgServiceImpl implements CharityOrgService {

	private final CharityOrgMapper mapper;
	
	@Override
	public CharityOrgDTO getCharityOrgInfo(String c_sys_id) {
		return mapper.getCharityOrgInfo(c_sys_id);
	}

	@Override
	public boolean saveDonationInfo(DonationInfoDTO donationInfo, String c_sys_id) {
		donationInfo.setC_sys_id(c_sys_id);
		
		return 1 == mapper.saveDonationInfo(donationInfo);
	}

	
//	@Override
//	public List<DonationInfoDTO> getDonationBoardList() {
//		return mapper.getDonationBoardList();
//	}

	@Override
	public List<DonationInfoDTO> getDonationBoardList() {
		List<DonationInfoDTO> list = mapper.getDonationBoardList();
		
		
		for(DonationInfoDTO info : list) {
			int c_board_num = info.getC_board_num();
			Optional<Long> donationTimes = Optional.ofNullable(mapper.getDonationTimes(c_board_num));
			Long t = donationTimes.orElse(0L);
			
			Optional<Long> donationCash = Optional.ofNullable(mapper.getDonationAmountCash(c_board_num));
			Long c = donationCash.orElse(0L);
			
			Optional<Long> donationPoint = Optional.ofNullable(mapper.getDonationAmountPoint(c_board_num));
			Long p = donationPoint.orElse(0L);
			info.setDonationTimes(t);
			info.setDonationCash(c);
			info.setDonationPoint(p);
		}
		
		return list;
		
	}
	
	
	@Override
	public DonationInfoDTO readDonationBoardInfo(int c_board_num) {
		
		return mapper.readDonationBoardInfo(c_board_num);
	}

	@Override
	public Long getDonationTimes(int c_board_num) {
		Optional<Long> donationTimes = Optional.ofNullable(mapper.getDonationTimes(c_board_num));
		
		return donationTimes.orElse(0L);
	}

	@Override
	public Long getDonationAmountCash(int c_board_num) {
		Optional<Long> donationAmount = Optional.ofNullable(mapper.getDonationAmountCash(c_board_num));
		
		return donationAmount.orElse(0L);
	}
	
	@Override
	public Long getDonationAmountPoint(int c_board_num) {
		Optional<Long> donationAmount = Optional.ofNullable(mapper.getDonationAmountPoint(c_board_num));
		
		return donationAmount.orElse(0L);
	}
	
	
	@Override
	public List<DonationInfoDTO> searchBoardList(OrgSearchFormDTO searchInfo) {
		
		List<DonationInfoDTO> searchList = mapper.searchBoardList(searchInfo);
		return searchList;
	}

	@Override
	public boolean updateDonationInfo(DonationInfoDTO donationInfo, String c_sys_id) {
		donationInfo.setC_sys_id(c_sys_id);
		
		int result = mapper.updateDonationInfo(donationInfo);
		
		return result == 1;
	}

	@Override
	public boolean deleteDonationInfo(int c_board_num) {
		
		int result = mapper.deleteDonationInfo(c_board_num);
		return result == 1;
	}	



	
}
