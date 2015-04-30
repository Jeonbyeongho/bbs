package com.jbh.bbs.dao;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

//VO와 Mapper를 연결해주는 DAO(DataAccessObject)생성
//스프링은 DAO를 Service라는 어노테이션을 이용한다.
@Service(value = "bbsDao")
public class BbsDao {
	//@Resource특정 Bean의 기능 수행을 위해 다른 Bean을 참조해야 하는 경우 사용
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
