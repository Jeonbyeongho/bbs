package com.jbh.bbs.dao;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

//VO�� Mapper�� �������ִ� DAO(DataAccessObject)����
//�������� DAO�� Service��� ������̼��� �̿��Ѵ�.
@Service(value = "bbsDao")
public class BbsDao {
	//@ResourceƯ�� Bean�� ��� ������ ���� �ٸ� Bean�� �����ؾ� �ϴ� ��� ���
	@Resource(name = "bbsMapper")
    private BbsMapper bbsMapper;

    public List<BbsVo> getSelect(Map map) {
        return this.bbsMapper.select(map);
    }

    public BbsVo getSelectOne(int idx) {
        return this.bbsMapper.selectOne(idx);
    }
    
    public void insert(BbsVo bbsVo) throws Exception{
    	this.bbsMapper.insert(bbsVo);
    }

    public void update(BbsVo bbsVo) {
         this.bbsMapper.update(bbsVo);
    }

    public void delete(int idx) {
         this.bbsMapper.delete(idx);
    }
    
    public void updateReadCount(int idx) {
    	   this.bbsMapper.updateReadCount(idx);
    }
}
