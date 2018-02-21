package cn.jiuling.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 公共视图控制器
 * 
 * @author YangXQ
 * @since 2016/1/11/11:00
 **/
@Controller
public class CommonController {
	/**
     * 基于权限标识的权限控制案例
     */
    @RequestMapping(value = "/test")
    @ResponseBody
    public Map<String,Object> test(){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("test", 111);
    	return result;
    }
}
