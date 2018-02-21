package cn.jiuling.recog.manager.servlet;

import java.io.IOException;
import java.util.List;
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
@WebServlet("/rest/taskManage/deleteVideoObjectTask")
public class RecogTaskDelete extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(RecogTaskDelete.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecogTaskDelete() {
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
		
		List<Map<String, Object>> list = Select.search("select * from vsd_task where serialnumber = ?", serialnumber);
		if (list == null || list.size() == 0) {
			super.returnMsg("-1", "删除失败，该任务不存在", null, request, response);
			return;
		}

		try {
			deleteVideoObjectTask(serialnumber);
			super.returnMsg("0", "Success", null, request, response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.returnMsg("-1", e.toString(), null, request, response);
		}

	}

	protected void deleteVideoObjectTask(String serialnumber) throws Exception {
		SqlExecutor excutor = SqlExecutor.getInstance();
		excutor.addSql("delete from vsd_task where serialnumber = ?", serialnumber);
		excutor.addSql("delete from vsd_task_relation where serialnumber = ?", serialnumber);
		excutor.addSql("delete from ctrl_unit_file where serialnumber = ?", serialnumber);// 离线任务需要删除ctrl_unit_file表数据
		excutor.commit();
	}

	public static void main(String[] args) {
		String ymd = "2018-01-03 12:42:55".toString().trim().split(" ")[0].replaceAll("-", "");
		System.out.println(ymd);
	}
}
