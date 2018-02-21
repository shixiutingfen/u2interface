package cn.jiuling.recog.manager.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.loocme.security.encrypt.Base64;
import com.loocme.sys.datastruct.IVarForeachHandler;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.datastruct.WeekArray;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.jni.u2s.U2sRecogNativeSingle;
import cn.jiuling.recog.manager.util.ImageBaseUtil;
import cn.jiuling.recog.manager.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class WsPictureRecog
 */
@WebServlet("/rest/feature/structPicture")
public class PictureStruct extends BaseServlet
{
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(PictureStruct.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PictureStruct()
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
        String body = super.LoggerRequest(request);
        JSONObject jobj = null;

        if (StringUtil.isNull(body))
        {
            super.returnMsg("-1", "请求参数不能为空", null, request, response);
            return;
        }

        try
        {
            jobj = JSONObject.fromObject(body);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            super.returnMsg("-1", "请求参数不是json格式", null, request, response);
            return;
        }

        if (null == jobj)
        {
            super.returnMsg("-1", "请求参数不是json格式", null, request, response);
            return;
        }

        String objtype = jobj.getString("objtype");
        if (StringUtil.isNull(objtype))
        {
            super.returnMsg("-1", "请求参数objtype不能为空", null, request, response);
            return;
        }

        if (!("0".equals(objtype) || ("1".equals(objtype) || "2".equals(objtype)
                || "4".equals(objtype))))
        {
            super.returnMsg("-1", "请求参数objtype只能是1,2,4中的一个", null, request,
                    response);
            return;
        }

        String picture = jobj.getString("picture");
        if (StringUtil.isNull(picture))
        {
            super.returnMsg("-1", "请求参数picture不能为空", null, request, response);
            return;
        }

        try
        {
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
            
            String suffix = StringUtils.getExtension(picBy);
            logger.info("=================根据base64获取图片的格式：" + suffix);
            if (StringUtil.isNull(suffix))
            {
                super.returnMsg("-1", "不支持的图片格式", null, request, response);
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

            String objexts = U2sRecogNativeSingle
                    .ObjectDetectionOnImage(Integer.valueOf(objtype), picBy);
            Var objextsVar = Var.fromJson(objexts);
            WeekArray objextsArr = objextsVar.getArray("objexts");
            JSONArray results = new JSONArray();
            objextsArr.foreach(new IVarForeachHandler() {

                private static final long serialVersionUID = 1L;

                @Override
                public void execute(String index, Var objextVar)
                {
                	String mainType = objextVar.getString("mainType");
                	if(!objtype.equals("0") && !objtype.equals(mainType)){
                		return;
                	}
                    Var tmpVar = Var.newObject();
                    tmpVar.set("objType", objextVar.get("mainType"));
                    tmpVar.set("snapshot.boundingBox.x", objextVar.get("x"));
                    tmpVar.set("snapshot.boundingBox.y", objextVar.get("y"));
                    tmpVar.set("snapshot.boundingBox.w", objextVar.get("w"));
                    tmpVar.set("snapshot.boundingBox.h", objextVar.get("h"));
                    tmpVar.set("features.featureData",
                            objextVar.get("featureVector"));
                    results.add(JSONObject.fromObject(tmpVar.toString()));
                }
            });
            JSONObject result = new JSONObject();
            result.put("objexts", results);
            super.returnMsg("0", "Success", result, request, response);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            super.returnMsg("-1", e.toString(), null, request, response);
        }
    }
}
