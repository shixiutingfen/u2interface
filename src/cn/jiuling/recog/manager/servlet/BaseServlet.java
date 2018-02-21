package cn.jiuling.recog.manager.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.StringUtil;

import net.sf.json.JSONObject;

public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(BaseServlet.class);

	public String LoggerRequest(HttpServletRequest request) {
	    String body = this.getPostJson(request);
		String[] uriArray = request.getRequestURI().split("/");
		String method = uriArray[uriArray.length - 1];
		logger.info(String.format(" [%s] request param json : %s.", method, body));
		return body;
	}
	
	public void returnMsg(String retCode, String retDesc, JSONObject result, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (null == result) {
			result = new JSONObject();
		}
		result.put("ret", retCode);
		result.put("desc", retDesc);

		// 打印response日志
		String[] uriArray = request.getRequestURI().split("/");
		String method = uriArray[uriArray.length - 1];
		logger.info(" [" + method + "] response param json :" + result.toString());

		// 解决js调用跨域问题
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(result.toString());
		out.flush();
		out.close();
	}
	
	public String getPostJson(HttpServletRequest request) {
		StringBuffer bodyBuff = new StringBuffer();
		try {
			List<String> lines = IOUtils.readLines(request.getInputStream());
			if (ListUtil.isNotNull(lines)) {
				for (int i = 0; i < lines.size(); i++) {
					bodyBuff.append(lines.get(i));
				}
			}
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		String body = bodyBuff.toString();
		if (StringUtil.isNull(body))
		{
		    body = request.getQueryString();
		}
		return body;
	}
}
