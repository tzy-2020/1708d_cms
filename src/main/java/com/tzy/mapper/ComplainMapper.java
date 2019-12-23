package com.tzy.mapper;

import java.util.List;

import com.tzy.domain.Complain;
import com.tzy.vo.ComplainVO;

public interface ComplainMapper {
	
	int insert(Complain complain);
	
	//查询举报
	List<Complain> selects(ComplainVO complainVO);

}
