package cn.jiuling.recog.manager.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.loocme.plugin.spring.Config;
import com.loocme.plugin.spring.dao.SpringJdbcDao;

public class MyStartingListener implements ServletContextListener
{

    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
      /*  IObjextSendProtocol sendProtocol = MsgConst.getSendProtocol();
        if (null != sendProtocol)
        {
            sendProtocol.close();
        }
        if (("on").equals(CommonConst.NATIVE_JNI_OPEN))
        {
            U2sRecogNativeSingle.release();
        }*/
    }

    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        // 启动loocme orm framework
        WebApplicationContext applicationCxt = WebApplicationContextUtils
                .getWebApplicationContext(event.getServletContext());
        new Config(applicationCxt).create("dataSource");

        SpringJdbcDao.debug = false;

        /*if (("on").equals(CommonConst.NATIVE_JNI_OPEN))
        {
            U2sRecogNativeSingle.init();
        }*/

       /* MyIpaddress.reloadLocalIp();
        TaskConstants.reloadTaskStatus();

        // 启动定时任务
        LScheConfig.start(false, false);*/

        // 读取推送的配置文件
        //Var pushVar = Var.fromJson(FileUtil.read(new File("/u2s/manager/config/datapush.json")));
        
       /* String path = MyStartingListener.class.getClassLoader().getResource("libra/datapush.json").getPath();
        String datapush = JSONUtil.readJsonFile(path);
        Var pushVar = Var.fromJson(datapush);
        if (pushVar != null && pushVar.getBoolean("switch"))
        {
            String converseClasspath = pushVar.getString("converse.classpath");
            if (StringUtil.isNotNull(converseClasspath))
            {
                MsgConst.setDataConverse(converseClasspath);
            }
            MsgConst.setSendProtocol(pushVar.getString("push.classpath"));
            IObjextSendProtocol sendProtocol = MsgConst.getSendProtocol();
            if (null != sendProtocol)
            {
                sendProtocol.initParams(pushVar.get("push"));
            }
            MsgConst.setFeatureHandler(pushVar.getString("feature.classpath"));
            IObjextFeatureHandler featureHandler = MsgConst.getFeatureHandler();
            if (null != featureHandler)
            {
                featureHandler.initParams(pushVar.get("feature"));
            }

            // 开始接收kafka消息
            ExecutorService KAFKA_SERVICE = Executors.newSingleThreadExecutor();
            KAFKA_SERVICE.execute(new ObjextKafkaConsumer());
        }*/
    }
}
