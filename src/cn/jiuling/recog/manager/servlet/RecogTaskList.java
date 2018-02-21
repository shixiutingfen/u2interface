package cn.jiuling.recog.manager.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.loocme.plugin.spring.comp.Page;
import com.loocme.plugin.spring.comp.Select;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.recog.manager.constants.TaskConstants;
import cn.jiuling.recog.manager.util.JSONUtils;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class WsPictureRecog
 */
@WebServlet("/rest/taskManage/getVideoObjectTaskList")
public class RecogTaskList extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(RecogTaskList.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecogTaskList() {
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
		if (jobj.containsKey("serialnumber")) {
			serialnumber = jobj.getString("serialnumber");
		}
		if (StringUtil.isNotNull(serialnumber)) {
			if (serialnumber.length() > 64) {
				super.returnMsg("-1", "请求参数serialnumber长度不能超过64位", null, request, response);
				return;
			}
			if (PatternUtil.isNotMatch(serialnumber, "^[0-9a-zA-Z]{1,65}$")) {
				super.returnMsg("-1", "请求参数serialnumber只能是英文、数字", null, request, response);
				return;
			}
		}

		try {
			Map<String, String> map = JSONUtils.getMapStr4Json(body);
			JSONObject result = new JSONObject();
			getVideoObjectTaskList(map, result);
			super.returnMsg("0", "Success", result, request, response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.returnMsg("-1", e.toString(), null, request, response);
		}
	}

	protected void getVideoObjectTaskList(Map<String, String> map, JSONObject result) throws IOException {
		String serialnumber = map.get("serialnumber");

		int pageNo = StringUtil.getInteger(map.get("pageNo"));
		if (pageNo <= 0) {
			pageNo = 1;
		}

		int pageSize = StringUtil.getInteger(map.get("pageSize"));
		if (pageSize <= 0) {
			pageSize = TaskConstants.TASK_PAGE_PAGESIZE;
		}

		Page<Map<String, Object>> filterPage = new Page<Map<String, Object>>(pageSize, pageNo);
		StringBuffer sql = new StringBuffer();

		if (StringUtil.isNotNull(serialnumber)) {
			sql.append("select * from vsd_task where serialnumber = '" + serialnumber + "'");
		} else {
			sql = new StringBuffer("select * from vsd_task where 1=1 ");
			if (StringUtil.isNotNull(map.get("startTime"))) {
				sql.append(" and createtime >= '").append(map.get("startTime")).append("'");
			}
			if (StringUtil.isNotNull(map.get("endTime"))) {
				sql.append(" and createtime <= '").append(map.get("endTime")).append("'");
			}
			if (StringUtil.isNotNull(map.get("type"))) {
				sql.append(" and type = '").append(map.get("type")).append("'");
			}
			if (StringUtil.isNotNull(map.get("status"))) {
				sql.append(" and status = '").append(map.get("status")).append("'");
			}
		}
		Select.searchPage(sql.toString(), filterPage);
		List<Map<String, Object>> taskList = filterPage.getRecords();
		int totalcount = filterPage.getRecordCount();

		List<Map<String, Object>> tasks = convertData4VsdTask(taskList);
		result.put("totalcount", totalcount);

		if (null == tasks || tasks.size() == 0) {
			result.put("tasks", new ArrayList<>());
		} else {
			result.put("tasks", tasks);
		}
	}

	protected List<Map<String, Object>> convertData4VsdTask(List<Map<String, Object>> taskList) {
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
		if (taskList.size() != 0) {
			for (Map<String, Object> vsdTaskMap : taskList) {
				Map<String, Object> mp = new LinkedHashMap<String, Object>();
				mp.put("serialnumber", vsdTaskMap.get("serialnumber"));
				mp.put("type", vsdTaskMap.get("type"));
				mp.put("status", vsdTaskMap.get("status"));
				mp.put("progress", vsdTaskMap.get("progress"));
				Object createTimeT = vsdTaskMap.get("createtime");
				Date createTime = null == createTimeT?new Date():new Date(((Timestamp) createTimeT).getTime());
				mp.put("createtime",
						DateUtil.getFormat(createTime, "yyyy-MM-dd HH:mm:ss"));
				mp.put("slaveip", vsdTaskMap.get("slaveip"));
				reList.add(mp);
			}
		}
		return reList;
	}
}
