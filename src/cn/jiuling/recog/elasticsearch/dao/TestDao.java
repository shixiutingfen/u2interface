package cn.jiuling.recog.elasticsearch.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class TestDao {
	@Resource  
	private JdbcTemplate jdbcTemplate;  
    
  /** 
   * 查询 
   * Edited by zhangpl 
   * @return List<Map<String,Object>> 
   */  
  public List<Map<String,Object>> getUser(){  
      List<Map<String ,Object>> userList = this.jdbcTemplate.queryForList("select *from sys_user");  
      return userList;  
  } 
}
