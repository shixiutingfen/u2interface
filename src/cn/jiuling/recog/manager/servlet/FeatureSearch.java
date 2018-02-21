package cn.jiuling.recog.manager.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.loocme.plugin.spring.comp.Select;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.MapUtil;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.PostUtil;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.jni.u2s.U2sRecogNativeSingle;
import cn.jiuling.recog.manager.util.ImageBaseUtil;
import cn.jiuling.recog.manager.util.StringUtils;
import cn.jiuling.recog.sendmsg.constants.MsgConst;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class WsPictureRecog
 */
@WebServlet("/rest/feature/search")
public class FeatureSearch extends BaseServlet
{
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(FeatureSearch.class);

    private static int TRADE_OFF = 0;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeatureSearch()
    {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        this.doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        String tradeOff = request.getParameter("tradeOff");
        if (StringUtil.isNotNull(tradeOff))
        {
            TRADE_OFF = StringUtil.getInteger(tradeOff);
            super.returnMsg("0", "修改tradeOff值成功，新值：" + TRADE_OFF, null, request,
                    response);
            return;
        }

        String body = super.LoggerRequest(request);

        if (StringUtil.isNull(body))
        {
            super.returnMsg("-1", "请求参数不能为空", null, request, response);
            return;
        }

        Var reqVar = Var.fromJson(body);
        if (null == reqVar || reqVar.isNull())
        {
            super.returnMsg("-1", "请求参数不是json格式或为空", null, request, response);
            return;
        }

        int objtype = reqVar.getInt("objtype");
        if (0 == objtype)
        {
            super.returnMsg("-1", "请求参数objtype不能为空", null, request, response);
            return;
        }

        if (1 != objtype && 2 != objtype && 4 != objtype)
        {
            super.returnMsg("-1", "请求参数objtype只能是1,2,4中的一个", null, request,
                    response);
            return;
        }
        
        String startTime = reqVar.getString("starttime");
        String endTime = reqVar.getString("endtime");
        
        String featureStr = reqVar.getString("feature");
        if (StringUtil.isNull(featureStr))
        {
            String picture = reqVar.getString("picture");
            if (StringUtil.isNull(picture))
            {
                super.returnMsg("-1", "请求参数picture不能为空", null, request, response);
                return;
            }

            byte[] picBy = null;
            // 获取图片
            if (PatternUtil.isMatch(picture, "^(http|ftp).*$",
                    Pattern.CASE_INSENSITIVE))
            {
                URLConnection conn = null;

                try
                {
                    // 下载图片
                    URL url = new URL(picture);
                    conn = url.openConnection();
                    // 设置超时间为3秒
                    conn.setConnectTimeout(3 * 1000);
                    conn.setReadTimeout(10 * 1000);
                    // 防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty("User-Agent",
                            "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    InputStream inputStream = conn.getInputStream();
                    if (null != inputStream)
                    {
                        picBy = ImageBaseUtil.input2byte(inputStream);
                    }
                }
                catch (RuntimeException e)
                {
                    logger.error(e);
                    e.printStackTrace();
                    super.returnMsg("-1", "下载图片失败", null, request, response);
                    return;
                }
            }
            else
            {
                // base64解析
                picBy = Base64.decode(picture.getBytes());
            }

            if (null == picBy)
            {
                super.returnMsg("-1", "获取图片失败", null, request, response);
                return;
            }

            String suffix = StringUtils.getExtension(picBy);
            logger.info("=================根据base64获取图片的格式：" + suffix);
            if (StringUtil.isNull(suffix))
            {
                super.returnMsg("-1", "获取图片格式失败，请确认图片是否完整", null, request,
                        response);
                return;
            }

            if (PatternUtil.isNotMatch(suffix, "^(jpg|jpeg)$",
                    Pattern.CASE_INSENSITIVE))
            {
                picBy = StringUtils.forJpg(new ByteArrayInputStream(picBy));
                if (picBy == null)
                {
                    super.returnMsg("-1", "不支持的图片格式: " + suffix, null, request,
                            response);
                    return;
                }
            }

            if (picBy.length > 3000000)
            {
                logger.info("=============picture length: " + picBy.length);
                super.returnMsg("-1", "图片太大", null, request, response);
                return;
            }

            int autoFetch = reqVar.getInt("autoFetch");
            if (1 == autoFetch)
            {
                // 自动获取图片目标
                String objexts = U2sRecogNativeSingle
                        .ObjectDetectionOnImage(objtype, picBy);
                Var objextsVar = Var.fromJson(objexts);
                if (1 == objextsVar.getInt("objexts.size"))
                {
                    featureStr = objextsVar.getString("objexts[0].featureVector");
                }
            }

            if (StringUtil.isNull(featureStr))
            {
                // 提取图片特征
                byte[] featureBy = U2sRecogNativeSingle.GetFeature(objtype, picBy);
                if (null != featureBy)
                {
                    featureStr = new String(Base64.encode(featureBy));
                }
            }
            
            if (StringUtil.isNull(featureStr))
            {
                super.returnMsg("-1", "获取图片特征信息失败" + suffix, null, request,
                        response);
                return;
            }
        }

        Var searchParam = Var.newObject();
        searchParam.set("type", objtype);
        searchParam.set("query", featureStr);
        if (0 < TRADE_OFF && 100 >= TRADE_OFF)
        {
            searchParam.set("tradeoff", TRADE_OFF);
        }

        String cameraIds = reqVar.getString("cameraids");
        String serialnumbers = reqVar.getString("serialnumbers");

        StringBuffer realSelectSqlBuff = new StringBuffer();
        StringBuffer offSelectSqlBuff = new StringBuffer();

        // 查询实时任务列表
        realSelectSqlBuff
                .append("select v.serialnumber, null as entry_time, r.from_type ")
                .append("from vsd_task v ")
                .append("left outer join vsd_task_relation r on v.id = r.task_id ")
                .append("where r.from_type in (1,4) ");
        offSelectSqlBuff
                .append("select v.serialnumber, c.entry_time, r.from_type ")
                .append("from vsd_task v ")
                .append("left outer join vsd_task_relation r on v.id = r.task_id ")
                .append("left outer join ctrl_unit_file c on r.camera_file_id = c.id ")
                .append("where r.from_type in (2,3) ");
        // 获取搜索范围
        if (StringUtil.isNotNull(serialnumbers))
        {
            String[] serialnumberArr = serialnumbers.split(",");
            realSelectSqlBuff.append("and v.serialnumber in (");
            offSelectSqlBuff.append("and v.serialnumber in (");
            for (int i = 0; i < serialnumberArr.length; i++)
            {
                if (0 != i)
                {
                    realSelectSqlBuff.append(",");
                    offSelectSqlBuff.append(",");
                }
                realSelectSqlBuff.append("'").append(serialnumberArr[i])
                        .append("'");
                offSelectSqlBuff.append("'").append(serialnumberArr[i])
                        .append("'");
            }
            realSelectSqlBuff.append(") ");
            offSelectSqlBuff.append(") ");

            boolean containsTime = true;
            Date startTimeDt = null;
            Date endTimeDt = null;

            if (StringUtil.isNotNull(startTime))
            {
                offSelectSqlBuff.append("and c.entry_time >= '")
                        .append(startTime).append("'");
                startTimeDt = DateUtil.getDate(startTime);
            }
            else
            {
                containsTime = false;
            }
            if (StringUtil.isNotNull(endTime))
            {
                offSelectSqlBuff.append("and c.entry_time <= '").append(endTime)
                        .append("'");
                endTimeDt = DateUtil.getDate(endTime);
            }
            else
            {
                containsTime = false;
            }

            List<Map<String, Object>> taskList = Select
                    .search(realSelectSqlBuff.toString() + " union all "
                            + offSelectSqlBuff.toString());
            if (ListUtil.isNull(taskList))
            {
                JSONObject result = new JSONObject();
                result.put("results", new JSONArray());
                super.returnMsg("0", "分析任务列表为空", result, request, response);
                return;
            }

            Map<String, Object> taskMap = null;
            Date taskMaxEntryTime = null, taskMinEntryTime = null;
            for (int i = 0; i < taskList.size(); i++)
            {
                taskMap = taskList.get(i);
                int fromType = MapUtil.getInteger(taskMap, "from_type");
                if (!containsTime && (1 == fromType || 4 == fromType))
                {
                    super.returnMsg("-1", "有实时流任务，搜索的时间范围参数不能为空", null, request,
                            response);
                    return;
                }
                if (1 == fromType || 4 == fromType)
                {
                    searchParam.set("tasks[" + i + "].id",
                            MapUtil.getString(taskMap, "serialnumber"));
                }
                else
                {
                    Date entryTime = DateUtil
                            .getDateByObject(taskMap.get("entry_time"));
                    searchParam.set("tasks[" + i + "].id",
                            MapUtil.getString(taskMap, "serialnumber"));
                    searchParam.set("tasks[" + i + "].start_at", DateUtil
                            .getFormat(entryTime, DateFormatConst.YMDHMS_));
                    if (null == taskMaxEntryTime
                            || taskMaxEntryTime.getTime() < entryTime.getTime())
                        taskMaxEntryTime = entryTime;
                    if (null == taskMinEntryTime
                            || taskMinEntryTime.getTime() > entryTime.getTime())
                        taskMinEntryTime = entryTime;
                }
            }

            if (null == startTimeDt)
            {
                startTimeDt = DateUtil.getPrevHour(taskMinEntryTime, 12);
            }
            if (null == endTimeDt)
            {
                endTimeDt = DateUtil.getNextHour(taskMaxEntryTime, 12);
            }

            searchParam.set("from",
                    DateUtil.getFormat(startTimeDt, DateFormatConst.YMDHMS_));
            searchParam.set("to",
                    DateUtil.getFormat(endTimeDt, DateFormatConst.YMDHMS_));
        }
        else
        {
            if (StringUtil.isNull(startTime) || StringUtil.isNull(endTime))
            {
                super.returnMsg("-1", "时间范围参数不能为空", null, request, response);
                return;
            }

            Date startTimeDt = DateUtil.getDate(startTime);
            Date endTimeDt = DateUtil.getDate(endTime);

            searchParam.set("from",
                    DateUtil.getFormat(startTimeDt, DateFormatConst.YMDHMS_));
            searchParam.set("to",
                    DateUtil.getFormat(endTimeDt, DateFormatConst.YMDHMS_));

            if (StringUtil.isNotNull(cameraIds))
            {
                realSelectSqlBuff.append("and r.camera_file_id in (");
                offSelectSqlBuff.append("and c.camera_id in (");
                String[] cameraIdArr = cameraIds.split(",");
                for (int i = 0; i < cameraIdArr.length; i++)
                {
                    if (0 != i)
                    {
                        realSelectSqlBuff.append(",");
                        offSelectSqlBuff.append(",");
                    }
                    realSelectSqlBuff.append(cameraIdArr[i]);
                    offSelectSqlBuff.append(cameraIdArr[i]);
                }
                realSelectSqlBuff.append(") ");
                offSelectSqlBuff.append(") ");
            }

            // 添加搜索时间范围
            offSelectSqlBuff.append("and c.entry_time between '")
                    .append(DateUtil.getFormat(
                            DateUtil.getPrevHour(startTimeDt, 12),
                            DateFormatConst.YMDHMS_))
                    .append("'").append(" and '")
                    .append(DateUtil.getFormat(
                            DateUtil.getNextHour(endTimeDt, 12),
                            DateFormatConst.YMDHMS_))
                    .append("'");

            List<Map<String, Object>> taskList = Select
                    .search(realSelectSqlBuff.toString() + " union all "
                            + offSelectSqlBuff.toString());
            if (ListUtil.isNull(taskList))
            {
                JSONObject result = new JSONObject();
                result.put("results", new JSONArray());
                super.returnMsg("0", "分析任务列表为空", result, request, response);
                return;
            }

            Map<String, Object> taskMap = null;
            for (int i = 0; i < taskList.size(); i++)
            {
                taskMap = taskList.get(i);
                int fromType = MapUtil.getInteger(taskMap, "from_type");
                if (1 == fromType || 4 == fromType)
                {
                    searchParam.set("tasks[" + i + "].id",
                            MapUtil.getString(taskMap, "serialnumber"));
                }
                else
                {
                    Date entryTime = DateUtil
                            .getDateByObject(taskMap.get("entry_time"));
                    searchParam.set("tasks[" + i + "].id",
                            MapUtil.getString(taskMap, "serialnumber"));
                    searchParam.set("tasks[" + i + "].start_at", DateUtil
                            .getFormat(entryTime, DateFormatConst.YMDHMS_));
                }
            }
        }

        JSONObject result = new JSONObject();
        // 调用搜图接口
        try
        {
            logger.info("搜图请求参数：" + searchParam.toString());
            String stResultStr = PostUtil.requestContent(
                    "http://" + MsgConst.FEATURE_KAFKA_IP + ":39081/search",
                    "application/json", searchParam.toString());
            JSONObject stResultJson = JSONObject.fromObject(stResultStr);
            if (null == stResultJson || !stResultJson.containsKey("error_msg"))
            {
                super.returnMsg("-1", "搜图接口无响应", null, request, response);
                return;
            }
            String errMsg = stResultJson.getString("error_msg");
            if (!"ok".equalsIgnoreCase(errMsg))
            {
                super.returnMsg("-1", "搜图接口调用异常：" + errMsg, null, request,
                        response);
                return;
            }
            result.put("results", stResultJson.get("results"));
        }
        catch (HttpConnectionException e)
        {
            logger.error(e);
            e.printStackTrace();
            super.returnMsg("-1", "搜图接口调用失败：" + e.getMessage(), null, request,
                    response);
            return;
        }

        super.returnMsg("0", "Success", result, request, response);
    }
}
