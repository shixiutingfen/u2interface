package cn.jiuling.recog.elasticsearch.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.jiuling.recog.elasticsearch.dao.TechLearnDao;
import cn.jiuling.recog.elasticsearch.util.ElasticsearchUtil;
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"classpath*:applicationContext-common.xml"})  
public class SynTecContentData {
	@Resource
	private TechLearnDao techLearnDao;
	@Test
	public void getContent(){
		List<Map<String,Object>> contentList = techLearnDao.getContent();
		List<String> rowkeys = new ArrayList<String>();
		List<Map<String, Object>> sources = new ArrayList<Map<String, Object>>();
		for(Map<String,Object>  map : contentList){
			
			Map<String, Object> source = new HashMap<String, Object>();
			buildMap(source, map);
			sources.add(source);
			rowkeys.add(String.valueOf((BigInteger) map.get("contentid")));
			//System.out.println(readText(new File("git//liaoxuefeng//"+map.get("content"))));
		}
		ElasticsearchUtil.init("sxt", 9300, "119.29.214.134");
		ElasticsearchUtil.indexResultDataBulk("tec_content", rowkeys, sources);
		ElasticsearchUtil.close();
	}
	public void buildMap(Map<String, Object> source, Map<String,Object> map) {
		BigInteger titletype =  (BigInteger) map.get("titletype");
		StringBuffer path = new StringBuffer("D://svn//weekworks//content//");
		if(1 == titletype.intValue()){//python
			path.append("python//liaoxuefeng//"+map.get("content"));
		}else if(2 == titletype.intValue()){//js
			path.append("javascript//liaoxuefeng//"+map.get("content"));
		}else if(3 == titletype.intValue()){//git
			path.append("git//liaoxuefeng//"+map.get("content"));
		}else{
			
		}
		String content = readText(new File(path.toString()));
		source.put("titleid", "title_"+map.get("titleid"));
		source.put("contentid", "content_"+map.get("contentid"));
		source.put("content", content);
		source.put("title", map.get("title"));
		source.put("titletype", map.get("titletype"));
		source.put("parentid", map.get("titleparentid"));
  	}
	public  static String readText(File file){
    	StringBuffer result = new StringBuffer();
	    try   
	    {       
	        if(file.isFile()&&file.exists())  
	        {       
	            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");       
	            BufferedReader reader=new BufferedReader(read);       
	            String line;       
	            while ((line = reader.readLine()) != null)   
	            {        
	            	result.append(line);    
	            }         
	            read.close();      
	        }     
	    } catch (Exception e)   
	    {         
	        e.printStackTrace();     
	    }     
	    return result.toString();   
    }
}
