package cn.jiuling.recog.manager.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import net.sf.json.JSONObject;

public class JSONUtils {
	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, Object> getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator<?> keyIter = jsonObject.keys();
		String key;
		Object value;
		Map<String, Object> valueMap = new LinkedHashMap<String, Object>();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}

	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, String> getMapStr4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator<?> keyIter = jsonObject.keys();
		String key;
		String value;
		Map<String, String> valueMap = new LinkedHashMap<String, String>();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = (String) jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}

	/**
	 * Map转String
	 * 
	 * @param paramMap
	 * @return
	 */
	public static String getJSonString4Map(Map<String, Object> paramMap) {

		if (null != paramMap) {
			JSONObject json = JSONObject.fromObject(paramMap);
			if (null != json) {
				return json.toString();
			} else
				return null;
		}

		return null;
	}

	/**
	 * 读取整个json文件
	 * 
	 * @param filePath
	 * @return 字符串
	 */
	public static String readJsonFile(String filePath) {
		File file = new File(filePath);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		// System.out.println(buffer.toString());
		return buffer.toString();
	}
}
