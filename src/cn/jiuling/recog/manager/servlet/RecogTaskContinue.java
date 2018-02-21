package cn.jiuling.recog.manager.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.loocme.plugin.spring.comp.Select;
import com.loocme.plugin.spring.comp.SqlExecutor;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class WsPictureRecog
 */
@WebServlet("/rest/taskManage/continueVideoObjectTask")
public class RecogTaskContinue extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(RecogTaskContinue.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecogTaskContinue() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String body = super.LoggerRequest(request);
		JSONObject jobj = null;
		if (StringUtil.isNull(body)) {
			super.returnMsg("-1", "请求参数不能为空", null, request, response);
			return;
		}

		try {
			jobj = JSONObject.fromObject(body);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.returnMsg("-1", "请求参数不是json格式", null, request, response);
			return;
		}
		
		if (null == jobj) {
			super.returnMsg("-1", "请求参数不是json格式", null, request, response);
			return;
		}
		
		String serialnumber = "";
		if(jobj.containsKey("serialnumber")){
			serialnumber = jobj.getString("serialnumber");
		}
		
		if (StringUtil.isNull(serialnumber)) {
			super.returnMsg("-1", "请求参数serialnumber不能为空", null, request, response);
			return;
		}

		if (serialnumber.length() > 64) {
			super.returnMsg("-1", "请求参数serialnumber长度不能超过64位", null, request, response);
			return;
		}

		if (PatternUtil.isNotMatch(serialnumber, "^[0-9a-zA-Z]{1,65}$"))
		{
			super.returnMsg("-1", "请求参数serialnumber只能是英文、数字", null, request, response);
			return;
		}

		try {
			String taskType = getTaskType(serialnumber);
			if ("3".equals(taskType)) {
				super.returnMsg("-1", "请求的任务类型为离线任务，操作非法", null, request, response);
				return;
			} else if (StringUtil.isNull(taskType)) {
				super.returnMsg("-1", "请求的任务不存在", null, request, response);
				return;
			}
			continueVideoObjectTask(serialnumber);
			super.returnMsg("0", "Success", null, request, response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.returnMsg("-1", e.toString(), null, request, response);
		}

	}

	private String getTaskType(String serialnumber) {
		Map<String, Object> mp = Select.searchOne("select from_type from vsd_task_relation where serialnumber = ?",
				serialnumber);
		if (mp != null && mp.get("from_type") != null) {
			return mp.get("from_type").toString();
		}
		return "";
	}

	protected void continueVideoObjectTask(String serialnumber) {
		SqlExecutor excutor = SqlExecutor.getInstance();
		excutor.addSql("update vsd_task set isvalid = 1, status = 0 where serialnumber = ?", serialnumber);
		excutor.commit();
	}
}
