package cn.jiuling.recog.sendmsg.protocol.impl;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.exception.HttpConnectionException;
import com.loocme.sys.util.HttpUtil;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.recog.sendmsg.constants.MsgConst;
import cn.jiuling.recog.sendmsg.protocol.IObjextSendProtocol;

public class SendProtocolHttpNormal
        implements IObjextSendProtocol, java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String URL_PERSON = "";
    private String URL_BIKE = "";
    private String URL_CAR = "";

    @Override
    public void initParams(Var params)
    {
        URL_PERSON = params.getString("urlPerson");
        URL_BIKE = params.getString("urlBike");
        URL_CAR = params.getString("urlCar");
    }

    @Override
    public int send(int objType, Object msgObj)
    {
        if (MsgConst.OBJTYPE_PERSON == objType && StringUtil.isNull(URL_PERSON))
        {
            return 1;
        }
        else if (MsgConst.OBJTYPE_BIKE == objType
                && StringUtil.isNull(URL_BIKE))
        {
            return 1;
        }
        else if (MsgConst.OBJTYPE_CAR == objType && StringUtil.isNull(URL_CAR))
        {
            return 1;
        }

        try
        {
            HttpUtil.postContent(URL_PERSON, "UTF-8", 5, null,
                    "application/json", msgObj.toString());
        }
        catch (HttpConnectionException e)
        {
            e.printStackTrace();
            return 2;
        }
        return 0;
    }

    @Override
    public void close()
    {
    }

}
