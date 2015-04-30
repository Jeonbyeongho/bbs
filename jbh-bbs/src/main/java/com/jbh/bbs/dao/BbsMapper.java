package com.jbh.bbs.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

//myBatis ���� �������̽�
@Repository(value = "bbsMapper")  //context-mybatis.xml�� Repository ������̼�
public interface BbsMapper {      //�ڵ����� ��ĵ�� ���Ұ� ���⼭ @Repository������̼� ����.
	List<BbsVo> select(Map map);	
	BbsVo selectOne(int idx);
	void insert(BbsVo bbsVo);
	void update(BbsVo bbsVo);
	void delete(int idx);
	void updateReadCount(int idx);
}
