package com.sh.service;

public interface HomeService {
	/*
	 * 총 봉사 포인트
	 */
	Long getVolunteerPoint();
	
	/*
	 * 총 봉사 횟수
	 */
	Long getVolunteerTimes();
	
	/*
	 * 총 기부 횟수
	 */
	Long getDonationTimes();
	
	/*
	 * 총 봉사 시간
	 */
	Long getTotalVolunteerHours();
	
	/*
	 * 봉사 포인트로 기부한 금액
	 */
	Long getTotalDonationAmountByPoint();
	
	
	/*
	 * 총 기부 금액
	 */
	Long getTotalDonationAmountByCash();
}
