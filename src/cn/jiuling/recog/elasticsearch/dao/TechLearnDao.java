package cn.jiuling.recog.elasticsearch.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TechLearnDao {
	@Resource  
	private JdbcTemplate jdbcTemplate;
	
	public List<Map<String,Object>> getContent(){
		StringBuffer sql = new StringBuffer("select ");
		sql.append("t.title AS title, ");
		sql.append("t.id AS titleid, ");
		sql.append("t.parentid AS titleparentid, ");
		sql.append("t.title_type AS titletype, ");
		sql.append("c.content AS content, ");
		sql.append("c.id AS contentid ");
		sql.append("FROM ");
		sql.append("tec_title t ");
		sql.append("LEFT JOIN tec_content c ON t.id = c.titleid ");
		System.out.println(sql.toString());
		List<Map<String ,Object>> result = this.jdbcTemplate.queryForList(sql.toString()); 
		return result;
	}
}
