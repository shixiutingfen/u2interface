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
import cn.jiuling.recog.manager.util.JSONUtils;
import cn.jiuling.recog.manager.util.StringUtils;
import net.sf.json.JSONObject;


@WebServlet("/rest/taskManage/addTrafficTask")
public class RecogTrafficTaskAdd extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(RecogTrafficTaskAdd.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecogTrafficTaskAdd() {
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
		if (StringUtil.isNull(serialnumber)) {
			super.returnMsg("-1", "请求参数serialnumber不能为空", null, request, response);
			return;
		}
		if (serialnumber.length() > 64) {
			super.returnMsg("-1", "请求参数serialnumber长度不能超过64位", null, request, response);
			return;
		}

		if (PatternUtil.isNotMatch(serialnumber, "^[0-9a-zA-Z]{1,65}$")) {
			super.returnMsg("-1", "请求参数serialnumber只能是英文、数字", null, request, response);
			return;
		}

		List<Map<String, Object>> list = Select.search("select * from vsd_task where serialnumber = ?", serialnumber);
		if (list != null && list.size() > 0) {
			super.returnMsg("-1", "任务序列号已存在", null, request, response);
			return;
		}

		String type = "";
		if (jobj.containsKey("type")) {
			type = jobj.getString("type");
		}
		if (StringUtil.isNull(type)) {
			super.returnMsg("-1", "请求参数type不能为空", null, request, response);
			return;
		}
		if (!("objext".equals(type))) {
			super.returnMsg("-1", "请求参数type非法", null, request, response);
			return;
		}
		
		String param = "";
		if (jobj.containsKey("param")) {
			param = jobj.getString("param");
		}
		if (StringUtil.isNull(param)) {
			super.returnMsg("-1", "请求参数param不能为空", null, request, response);
			return;
		}
		
		Map<String, Object> paramMap = JSONUtils.getMap4Json(param);
		String url = "";
		if(paramMap.containsKey("url")) {
			url = paramMap.get("url").toString();
		}
		if(StringUtil.isNull(url)) {
			super.returnMsg("-1", "请求参数param url不能为空", null, request, response);
		}
		logger.info("========= param : " + param);
		try {
			Map<String, Object> map = JSONUtils.getMap4Json(body);
			// 数据入库
			insertBussinessData(map,url, request, response);

			JSONObject result = new JSONObject();
			result.put("serialnumber", serialnumber);
			super.returnMsg("0", "Success", result, request, response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.returnMsg("-1", e.toString(), null, request, response);
		}

	}

	protected void insertBussinessData(Map<String, Object> map, String url , HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long taskId = getTaskId();
		StringBuffer vsdTaskSql = new StringBuffer();
		vsdTaskSql.append(" insert into vsd_task (");
		vsdTaskSql.append(" 	id,serialnumber,type,busiType,progress,param,isvalid,status,createtime");
		vsdTaskSql.append(" 	) values( ");
		vsdTaskSql.append(taskId).append(",");
		vsdTaskSql.append("'" + map.get("serialnumber")).append("',");
		vsdTaskSql.append("'" + map.get("type")).append("',");
		vsdTaskSql.append(1).append(",");
		vsdTaskSql.append(0).append(",");
		vsdTaskSql.append("'" + map.get("param").toString()).append("',");
		vsdTaskSql.append(1).append(",");
		vsdTaskSql.append(0).append(",");
		vsdTaskSql.append("'" + StringUtils.getYMDHMS_()).append("'");
		vsdTaskSql.append("	)");

		StringBuffer ctrlUnitSql = new StringBuffer();

		StringBuffer vsdTaskRelationSql = new StringBuffer();
		vsdTaskRelationSql.append(
				" insert into vsd_task_relation (task_id,serialnumber,camera_file_id,from_type,createtime,createuser");
		vsdTaskRelationSql.append(" 	) values( ");
		vsdTaskRelationSql.append(taskId).append(",");
		vsdTaskRelationSql.append("'" + map.get("serialnumber")).append("',");

		//String url = map.get("url").toString();
		if (url.startsWith("vas") || url.startsWith("rtsp") || url.startsWith("rtmp")) {
			vsdTaskRelationSql.append(1l).append(",");
			vsdTaskRelationSql.append(4l).append(",");// 实时任务类型
		} else {
			Map<String, Object> cameraMap = Select.searchOne("select * from camera where name = '默认监控点'");
			if (cameraMap != null) {
				Long cameraFileId = getCameraFileId();

				ctrlUnitSql.append(
						" insert into ctrl_unit_file (id,file_type,file_suffix,file_name,file_nameafterupload,");
				ctrlUnitSql.append(" file_pathafterupload,camera_id,create_uerid,framerate,");
				ctrlUnitSql.append(" entry_time,create_time,transcoding_progress,transcode_status,serialnumber");
				ctrlUnitSql.append(" 	) values( ");
				ctrlUnitSql.append("'" + cameraFileId).append("',");
				ctrlUnitSql.append('4').append(",");

				String filename = url.substring(url.lastIndexOf("/") + 1, url.length());
				ctrlUnitSql.append("'." + filename.split("\\.")[1]).append("',");
				ctrlUnitSql.append("'" + filename).append("',");
				ctrlUnitSql.append("'" + filename).append("',");
				ctrlUnitSql.append("'" + url).append("',");
				ctrlUnitSql.append(cameraMap.get("id")).append(",");
				ctrlUnitSql.append(1l).append(",");
				ctrlUnitSql.append(25).append(",");

				ctrlUnitSql.append("'" + StringUtils.getYMDHMS_()).append("',");
				ctrlUnitSql.append("'" + StringUtils.getYMDHMS_()).append("',");
				ctrlUnitSql.append("'100'").append(",");
				ctrlUnitSql.append(1).append(",");
				ctrlUnitSql.append("'" + map.get("serialnumber")).append("'");
				ctrlUnitSql.append(")");

				vsdTaskRelationSql.append(cameraFileId).append(",");
				vsdTaskRelationSql.append(3l).append(",");// 离线任务类型
			} else {
				super.returnMsg("-1", "默认监控点不存在！", null, request, response);
				return;
			}
		}

		vsdTaskRelationSql.append("'" + StringUtils.getYMDHMS_()).append("',");
		vsdTaskRelationSql.append(1l);
		vsdTaskRelationSql.append(")");

		logger.info("=======vsdTaskSql:" + vsdTaskSql.toString());
		logger.info("=======ctrlUnitSql:" + ctrlUnitSql.toString());
		logger.info("=======vsdTaskRelationSql:" + vsdTaskRelationSql.toString());

		SqlExecutor executor = SqlExecutor.getInstance();
		executor.addSql(vsdTaskSql.toString());
		if (StringUtil.isNotNull(ctrlUnitSql.toString())) {
			executor.addSql(ctrlUnitSql.toString());
		}
		executor.addSql(vsdTaskRelationSql.toString());
		executor.commit();

	}

	protected Long getCameraFileId() {
		Long id = 0l;
		List<Map<String, Object>> ctrlUnitFileList = null;
		do {
			id = System.currentTimeMillis();
			ctrlUnitFileList = Select.search("select * from ctrl_unit_file where serialnumber = ?", id);
		} while (ctrlUnitFileList != null && ctrlUnitFileList.size() > 0);
		return id;
	}

	protected Long getTaskId() {
		Long serialnumberL = 0l;
		List<Map<String, Object>> taskList = null;
		do {
			String taskIdStr = "1" + cn.jiuling.recog.manager.util.StringUtils.getRandom6Number(9);
			serialnumberL = Long.valueOf(taskIdStr);
			taskList = Select.search("select * from vsd_task where serialnumber = ?", serialnumberL);

		} while (taskList != null && taskList.size() > 0);
		return serialnumberL;
	}
	
	protected void handleTaskParam(Map<String, Object> map,HttpServletRequest request, HttpServletResponse response) throws IOException {
		String paramJson = String.valueOf(map.get("param"));
		Map<String, Object> paramMap = JSONUtils.getMap4Json(paramJson);
		if(null == paramMap.get("url")) {
			super.returnMsg("-1", "请求参数param url不能为空", null, request, response);
		}
		
	}

	public static void main(String[] args) {
		String entryTime = StringUtils.getYMD000_();
		System.out.println(entryTime);
	}

}
