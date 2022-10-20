package com.sh.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.sh.domain.UserDTO;

@Mapper
public interface TestMapper {

	int testJoin(Map<String, String> user);
	
	String testGetPw(String user_id);
}
