package com.sh.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.sh.confing.SecurityJavaConfig;
import com.sh.domain.CharityOrgDTO;
import com.sh.domain.DonationCompleteOrgDTO;
import com.sh.domain.DonationCompleteUserDTO;
import com.sh.domain.DonationInfoDTO;
import com.sh.domain.DonationPaymentCashDTO;
import com.sh.domain.DonationPaymentPointDTO;
import com.sh.domain.VolunteerInfoDTO;
import com.sh.mapper.DonationUserMapper;
import com.sh.webDomain.DonationCompleteInfoDTO;
import com.sh.webDomain.DonationPaymentInfoDTO;
import com.sh.webDomain.VolunteerSearchFormDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonationUserServiceImpl implements DonationUserService{

	private final DonationUserMapper mapper;
	private final SecurityJavaConfig passwordEncoder;

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
	public DonationInfoDTO getDonationBoardInfo(int c_board_num) {

		return mapper.getDonationBoardInfo(c_board_num);
	}

	@Override
	public CharityOrgDTO getCharityOrgInfo(int c_board_num) {
		
		return mapper.getCharityOrgInfo(c_board_num);
	}


	
	@Override
	@Transactional
	public boolean saveDonationPaymentCash(DonationPaymentCashDTO paymentInfo, int c_board_num, String u_sys_id, String donationType) {
		
//		==================== 기부 완료 유저 
		// 보드 정보
		DonationCompleteUserDTO user = mapper.getSomeBoardInfoUser(c_board_num);
		user.setU_sys_id(u_sys_id);
		user.setDonation_type(donationType);
		
		int userInfoResult = mapper.saveDonationCompleteInfoUser(user);
		
//		==================== 기부 완료 단체
		
		DonationCompleteOrgDTO org = mapper.getSomeBoardInfoOrg(c_board_num);
		org.setU_sys_id(u_sys_id);
		org.setDonation_type(donationType);
		
		int orgInfoResult = mapper.saveDonationCompleteInfoOrg(org);
		
		if(userInfoResult != orgInfoResult) {
			return false;
		}
		
		
//		====================== 결제정보 유저
		
		DonationCompleteInfoDTO completeInfo = mapper.getCompleteInfo(u_sys_id, c_board_num);
		
		int cu_num = completeInfo.getCo_num();
		int co_num = completeInfo.getCu_num();
		String c_sys_id = completeInfo.getC_sys_id();
		
		paymentInfo.setC_board_num(c_board_num);
		paymentInfo.setU_sys_id(u_sys_id);
		paymentInfo.setCu_num(cu_num);
		paymentInfo.setCo_num(co_num);
		paymentInfo.setC_sys_id(c_sys_id);
		
		int userResult = mapper.saveDonationPaymentCashUser(paymentInfo);

		// =================== 결제 정보 단체
		
		int orgResult = mapper.saveDonationPaymentCashOrg(paymentInfo);
		
		
		
		if(userResult != orgResult) {
			return false;
		}
		
		return true;
	}
	
	
	
	@Override
	@Transactional
	public boolean saveDonationPaymentPoint(DonationPaymentPointDTO paymentInfo, int c_board_num, String u_sys_id, String donationType) {
		
//		==================== 기부완료 유저
		// 보드 정보
		DonationCompleteUserDTO user = mapper.getSomeBoardInfoUser(c_board_num);
		user.setU_sys_id(u_sys_id);
		user.setDonation_type(donationType);
		
		int userInfoResult = mapper.saveDonationCompleteInfoUser(user);
//		==================== 기부완료 단체
		
		DonationCompleteOrgDTO org = mapper.getSomeBoardInfoOrg(c_board_num);
		org.setU_sys_id(u_sys_id);
		org.setDonation_type(donationType);
		
		int orgInfoResult = mapper.saveDonationCompleteInfoOrg(org);
		
		if(userInfoResult != orgInfoResult) {
			return false;
		}
		
		
//		================== 결제정보 유저
		
		DonationCompleteInfoDTO completeInfo = mapper.getCompleteInfo(u_sys_id, c_board_num);
		
		int cu_num = completeInfo.getCo_num();
		int co_num = completeInfo.getCu_num();
		String c_sys_id = completeInfo.getC_sys_id();
		
		paymentInfo.setC_board_num(c_board_num);
		paymentInfo.setU_sys_id(u_sys_id);
		paymentInfo.setCu_num(cu_num);
		paymentInfo.setCo_num(co_num);
		paymentInfo.setC_sys_id(c_sys_id);
		
		
		int userResult = mapper.saveDonationPaymentPointUser(paymentInfo);
		
//		================== 결제정보 단체
		
		int orgResult = mapper.saveDonationPaymentPointOrg(paymentInfo);
		
		if(userResult != orgResult) {
			return false;
		}
		
		return true;
	}

	@Override
	public Integer getMyRestPoint(String u_sys_id) {
		
		Optional<Integer> vPoint = Optional.ofNullable(mapper.getMyVolunteerPoint(u_sys_id));
		Optional<Integer> dPoint = Optional.ofNullable(mapper.getMyDonationPoint(u_sys_id));
		
		int myPoint = vPoint.orElse(0) - dPoint.orElse(0);
		
		return myPoint;
	}

	@Override
	public boolean checkAvailablePoint(Integer donationPoint, String u_sys_id) {
		
		donationPoint = Optional.ofNullable(donationPoint).orElse(0);
		
		Optional<Integer> vPoint = Optional.ofNullable(mapper.getMyVolunteerPoint(u_sys_id));
		Optional<Integer> dPoint = Optional.ofNullable(mapper.getMyDonationPoint(u_sys_id));
		
		int restPoint = vPoint.orElse(0) - dPoint.orElse(0);
		
		
		if(donationPoint > restPoint || donationPoint == 0) {
			return false;
		}
		
		return true;
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
	public boolean checkCreditCard(DonationPaymentCashDTO paymentInfo) {
		// 여기서 유효성 체크하장
		Integer amount_of_money = paymentInfo.getAmount_of_money();
		String credit_card_number = paymentInfo.getCredit_card_number();
		String credit_card_company = paymentInfo.getCredit_card_company();
		String credit_card_valid_date = paymentInfo.getCredit_card_valid_date();
		String credit_card_owner_birth = paymentInfo.getCredit_card_owner_birth();
		
		log.info(paymentInfo.toString());
		boolean result = true;
		
		if(amount_of_money == null) {
			result = false;
		}
		else if(ObjectUtils.isEmpty(credit_card_number) || !Pattern.matches("(\\d{16})", credit_card_number)) {
			result = false;
		}
		else if(ObjectUtils.isEmpty(credit_card_company)) {
			result = false;
		}
		else if(ObjectUtils.isEmpty(credit_card_valid_date) || !Pattern.matches("(0[1-9]|1[012])/(2[0-9]|3[0-9])", credit_card_valid_date)) {
			result = false;
		}
		else if(ObjectUtils.isEmpty(credit_card_owner_birth) || !Pattern.matches("^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$", credit_card_owner_birth)) {
			result = false;
		}
		
		if(result) {
			String encryCardNumber = passwordEncoder.pwBcryptEncoder().encode(credit_card_number);
			paymentInfo.setCredit_card_number(encryCardNumber);
			
			String encryOwnerBirth = passwordEncoder.pwBcryptEncoder().encode(credit_card_owner_birth);
			paymentInfo.setCredit_card_owner_birth(encryOwnerBirth);
		}
		
		return result;
	}

	@Override
	public DonationPaymentInfoDTO getRecentDonationCashInfo(String u_sys_id) {
		DonationPaymentInfoDTO paymentInfo = mapper.getRecentDonationInfo(u_sys_id);
		
		int cu_num = paymentInfo.getCu_num();
		Optional<Long> cash = Optional.ofNullable(mapper.getRecentPaymentCash(cu_num));
		
		paymentInfo.setAmount_of_money(cash.orElse(0L));
		
		
		return paymentInfo;
		
	}
	
	@Override
	public DonationPaymentInfoDTO getRecentDonationPointInfo(String u_sys_id) {
		DonationPaymentInfoDTO paymentInfo = mapper.getRecentDonationInfo(u_sys_id);
		
		int cu_num = paymentInfo.getCu_num();
		Optional<Integer> point = Optional.ofNullable(mapper.getRecentPaymentPoint(cu_num));
		
		paymentInfo.setAmount_of_point(point.orElse(0));
		
		
		return paymentInfo;
		
	}

	@Override
	public List<DonationInfoDTO> searchBoardList(VolunteerSearchFormDTO searchInfo) {
		
		List<DonationInfoDTO> searchList = mapper.searchBoardList(searchInfo);
		return searchList;
	}

	
	
	
}
