package com.sh.domain;

import lombok.Data;

@Data
public class CharityReviewBoardDTO {
	private int board_num;
	private String title;
	private String name_kr;
	private String r_date;
	private String contents;
	private String u_sys_id;
}
