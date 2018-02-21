package cn.jiuling.recog.sendmsg.constants;

import com.loocme.sys.util.ReflectUtil;

import cn.jiuling.recog.sendmsg.converse.IObjextDataConverse;
import cn.jiuling.recog.sendmsg.converse.impl.DataConverseNormal;
import cn.jiuling.recog.sendmsg.feature.IObjextFeatureHandler;
import cn.jiuling.recog.sendmsg.feature.impl.FeatureHandlerNormal;
import cn.jiuling.recog.sendmsg.protocol.IObjextSendProtocol;
import cn.jiuling.recog.sendmsg.protocol.impl.SendProtocolKafkaNormal;

public class MsgConst
{
    
    public static final int OBJTYPE_PERSON = 1;
    public static final int OBJTYPE_BIKE = 4;
    public static final int OBJTYPE_CAR = 2;
    
    public static String FEATURE_KAFKA_IP = "";
    
    private static IObjextDataConverse dataConverseInstance = null;
    private static IObjextSendProtocol sendProtocolInstance = null;
    private static IObjextFeatureHandler featureHandlerInstance = null;

    public static IObjextDataConverse getDataConverse()
    {
        if (null == dataConverseInstance)
        {
            dataConverseInstance = new DataConverseNormal();
        }
        return dataConverseInstance;
    }
    
    public static boolean setDataConverse(String classpath)
    {
        Object tHandler = ReflectUtil.newInstance(classpath);
        
        if (null != tHandler && tHandler instanceof IObjextDataConverse)
        {
            dataConverseInstance = (IObjextDataConverse) tHandler;
            return true;
        }
        
        return false;
    }
    
    public static IObjextSendProtocol getSendProtocol()
    {
        if (null == sendProtocolInstance)
        {
            sendProtocolInstance = new SendProtocolKafkaNormal();
        }
        
        return sendProtocolInstance;
    }
    
    public static boolean setSendProtocol(String classpath)
    {
        Object tHandler = ReflectUtil.newInstance(classpath);
        
        if (null != tHandler && tHandler instanceof IObjextSendProtocol)
        {
            sendProtocolInstance = (IObjextSendProtocol) tHandler;
            return true;
        }
        
        return false;
    }
    
    public static IObjextFeatureHandler getFeatureHandler()
    {
        if (null == featureHandlerInstance)
        {
            featureHandlerInstance = new FeatureHandlerNormal();
        }
        return featureHandlerInstance;
    }
    
    public static boolean setFeatureHandler(String classpath)
    {
        Object tHandler = ReflectUtil.newInstance(classpath);
        
        if (null != tHandler && tHandler instanceof IObjextFeatureHandler)
        {
            featureHandlerInstance = (IObjextFeatureHandler) tHandler;
            return true;
        }
        
        return false;
    }
}
