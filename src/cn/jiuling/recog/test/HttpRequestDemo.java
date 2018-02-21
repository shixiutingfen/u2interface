package cn.jiuling.recog.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.loocme.sys.entities.HttpManagerParams;
import com.loocme.sys.util.FileUtil;

import cn.jiuling.recog.manager.util.JSONUtils;
import net.sf.json.JSONObject;

public class HttpRequestDemo
{
    private static Logger logger = Logger.getLogger(HttpRequestDemo.class);

    private static final String HTTP_PREFIX = "http://192.168.0.66:20280";

    public static void main(String[] args)
    {
//        System.out.println(addVideoObjextTask());
//        System.out.println(getVideoObjectTaskList());
//        System.out.println(pauseVideoObjectTask());
//        System.out.println(continueVideoObjectTask());
//        System.out.println(deleteVideoObjectTask());
        //System.out.println(extractFromPicture());
        addTrafficTask();
    }

    /**
     * 添加结构化任务
     */
    public static String addVideoObjextTask()
    {
        String url = "/rest/taskManage/addVideoObjextTask";
        JSONObject params = new JSONObject();
        
        // 添加离线任务
//        params.put("serialnumber", "9999999999");
//        params.put("type", "objext");
//        params.put("url", "file:///u2s/manager/compare_resource/tt.avi");
        
        // 添加实时任务
        params.put("serialnumber", "9999999999");
        params.put("type", "objext");
        params.put("url", "rtmp://192.168.0.31/live6");
        
        return postContent(url, params.toString());
    }
    
    
    /**
     * 添加结构化任务
     */
    public static String addTrafficTask()
    {
        String url = "/rest/taskManage/addTrafficTask";
        JSONObject params = new JSONObject();
        
        // 添加离线任务
		//        params.put("serialnumber", "9999999999");
		//        params.put("type", "objext");
		//        params.put("url", "file:///u2s/manager/compare_resource/tt.avi");
        
        // 添加实时任务
        params.put("serialnumber", "9999999999");
        params.put("type", "objext");
        
        String path = HttpRequestDemo.class.getClassLoader().getResource("vsd_smart_traffic_param.json").getPath();
		
		String defaultParam = JSONUtils.readJsonFile(path);
        
        params.put("param", defaultParam);
        
        return postContent(url, params.toString());
    }

    /**
     * 获取结构化任务状态
     */
    public static String getVideoObjectTaskList()
    {
        String url = "/rest/taskManage/getVideoObjectTaskList";
        JSONObject params = new JSONObject();
        
        params.put("serialnumber", "9999999999");
        params.put("type", "objext");
        
        return postContent(url, params.toString());
    }

    /**
     * 停止
     */
    public static String pauseVideoObjectTask()
    {
        String url = "/rest/taskManage/pauseVideoObjectTask";
        JSONObject params = new JSONObject();
        
        params.put("serialnumber", "9999999999");
        
        return postContent(url, params.toString());
    }

    /**
     * 继续
     */
    public static String continueVideoObjectTask()
    {
        String url = "/rest/taskManage/continueVideoObjectTask";
        JSONObject params = new JSONObject();
        
        params.put("serialnumber", "9999999999");
        
        return postContent(url, params.toString());
    }

    /**
     * 删除任务
     */
    public static String deleteVideoObjectTask()
    {
        String url = "/rest/taskManage/deleteVideoObjectTask";
        JSONObject params = new JSONObject();
        
        params.put("serialnumber", "9999999999");
        
        return postContent(url, params.toString());
    }

    /**
     * 获取特征值
     */
    public static String extractFromPicture()
    {
        String url = "/rest/feature/extractFromPicture";
        JSONObject params = new JSONObject();
        
        params.put("objtype", "1");
        params.put("picture", FileUtil.read(new File("D:/temp/picturebase64.txt")));
        
        return postContent(url, params.toString());
    }

    public static HttpClient _client;
    static
    {
        HttpManagerParams params = new HttpManagerParams();
        HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
        managerParams.setConnectionTimeout(params.getConnectionTimeout());
        managerParams.setSoTimeout(params.getSoTimeout());
        managerParams.setStaleCheckingEnabled(params.getStaleCheckEnabled());
        managerParams.setTcpNoDelay(params.getTcpNoDelay());
        managerParams.setDefaultMaxConnectionsPerHost(
                params.getDefaultMaxConnectionsPerHost());
        managerParams.setMaxTotalConnections(params.getMaxTotalConnections());
        managerParams.setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(0, false));

        HttpConnectionManager _connectionManager = new MultiThreadedHttpConnectionManager();
        _connectionManager.setParams(managerParams);

        _client = new HttpClient(_connectionManager);
    }

    public static String postContent(String url, String content)
    {
        logger.info("开始http调用地址:" + url);
        PostMethod method = new PostMethod(HTTP_PREFIX + url);
        method.addRequestHeader("Connection", "Keep-Alive");
        method.addRequestHeader("Content-Type", "application/json");
        method.getParams().setContentCharset("UTF-8");
        method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        method.getParams().setUriCharset("UTF-8");

        try
        {
            method.setRequestEntity(new StringRequestEntity(content,
                    "application/json", "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }

        try
        {
            int statusCode = _client.executeMethod(method);
            logger.info("返回http响应码:" + statusCode);
            if (statusCode != HttpStatus.SC_OK)
            {
                logger.error("请求失败，http status = " + statusCode);
                return null;
            }

            String respStr = method.getResponseBodyAsString();
            logger.info("响应信息：" + respStr);
            return respStr;
        }
        catch (SocketTimeoutException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            method.releaseConnection();
        }
    }
}