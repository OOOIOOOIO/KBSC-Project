package com.sh.javaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LocalDateTest {
	public static void main(String[] args) {
		LocalDate start = LocalDate.parse("2022-09-03");
		LocalDate end = LocalDate.parse("2022-09-05").plusDays(1);
		
		List<LocalDate> list =  start.datesUntil(end).collect(Collectors.toList());
		
		for(LocalDate a : list) {
			System.out.println(a.toString());			
		}
		
		LocalDateTime appl = LocalDateTime.parse("2022-08-18 20:29:55");
		System.out.println(appl.toString());
		
	}
}
