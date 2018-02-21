package cn.jiuling.recog.sendmsg.protocol;

import com.loocme.sys.datastruct.Var;

public interface IObjextSendProtocol
{

    public void initParams(Var params);
    public int send(int objType, Object msgObj);
    public void close();
}
