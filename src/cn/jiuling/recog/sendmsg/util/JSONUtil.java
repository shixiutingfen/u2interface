package cn.jiuling.recog.sendmsg.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * json操作帮助类
 * 提供：json文件操作，json与String互转，json转java，json转map，json转list
 * @author Administrator
 *
 */
public class JSONUtil {
    
    protected static Logger logger = Logger.getLogger(JSONUtil.class.getName());
    /**
     * 读取整个json文件
     * @param filePath 
     * @return 字符串
     */
    public static String readJsonFile(String filePath)
    {
        File file = new File(filePath);
        Scanner scanner = null;
        StringBuilder buffer = new StringBuilder();
        try {
            scanner = new Scanner(file, "utf-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        
        System.out.println(buffer.toString());
        return buffer.toString();
    }
    
    /**
     * 读取json文件中某个属性
     * @param filePath
     * @param attrName
     * @return  attrValue
     */
    public static String readJsonFile(String filePath,String attrName)
    {
        return null;
    }
    
	/**
	 * 从一个JSON 对象字符格式中得到一个java对象
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return java对象
	 */
	
	public static Object getObject4JsonString(String jsonString, Class<?> pojoCalss) {
		Object pojo;
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}
	

	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String,Object> getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator<?> keyIter = jsonObject.keys();
		String key;
		Object value;
		Map<String,Object> valueMap = new HashMap<String,Object>();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}
	
	
	/**
	 * 从json数组中得到相应java数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();

	}

	/** */
	/**
	 * 从json对象集合表达式中得到一个java对象列表
	 * 
	 * @param jsonString
	 * @param pojoClass
	 * @return
	 */
	public static List<Object> getList4Json(String jsonString, Class<?> pojoClass) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);

		JSONObject jsonObject;
		Object pojoValue;

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);

		}
		return list;

	}

	/** */
	/**
	 * 从json数组中解析出java字符串数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String[] getStringArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);

		}

		return stringArray;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
	   /* String fullFileName = "D:/vsd_task_param.json";
	    String param = readJsonFile(fullFileName);
	    
	    JSONObject jsonObject = JSONObject.fromObject(param);
	   String url =  (String)jsonObject.get("url");
	   System.out.println(url);
	   jsonObject.put("url", "yxq");
	   System.out.println(jsonObject.toString());*/
		
		Map map = new HashMap();
		map.put("test", 111);
		JSONObject json = JSONObject.fromObject(map);
		System.out.println(json.toString());
	}
	
}
