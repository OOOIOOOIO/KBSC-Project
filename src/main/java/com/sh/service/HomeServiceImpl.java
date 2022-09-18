package com.sh.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sh.mapper.HomeMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService{

	private final HomeMapper mapper;

	@Override
	public Long getVolunteerPoint() {
		Optional<Long> volunteerPoint = Optional.ofNullable(mapper.getVolunteerPoint());
		
		return volunteerPoint.orElse(0L);
	}

	@Override
	public Long getVolunteerTimes() {
		Optional<Long> volunteerTimes = Optional.ofNullable(mapper.getVolunteerTimes());
		
		return volunteerTimes.orElse(0L);
	}

	@Override
	public Long getDonationTimes() {
		Optional<Long> donationTimes = Optional.ofNullable(mapper.getDonationTimes());
		
		return donationTimes.orElse(0L);
	}

	@Override
	public Long getTotalVolunteerHours() {
		Optional<Long> volunteerHours = Optional.ofNullable(mapper.getTotalVolunteerHours());
		
		return volunteerHours.orElse(0L);
	}
	
	@Override
	public Long getTotalDonationAmountByPoint() {
		Optional<Long> donationPoint = Optional.ofNullable(mapper.getTotalDonationAmountByPoint());
		
		return donationPoint.orElse(0L);
	}


	@Override
	public Long getTotalDonationAmountByCash() {
		Optional<Long> donationCash = Optional.ofNullable(mapper.getTotalDonationAmountByCash());
		
		return donationCash.orElse(0L);
	}
}
