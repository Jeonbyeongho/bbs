package com.jbh.bbs.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

//myBatis 맵퍼 인터페이스
@Repository(value = "bbsMapper")  //context-mybatis.xml에 Repository 어노테이션
public interface BbsMapper {      //자동으로 스캔해 놓았고 여기서 @Repository어노테이션 정의.
	List<BbsVo> select(Map map);	
	BbsVo selectOne(int idx);
	void insert(BbsVo bbsVo);
	void update(BbsVo bbsVo);
	void delete(int idx);
	void updateReadCount(int idx);
}
