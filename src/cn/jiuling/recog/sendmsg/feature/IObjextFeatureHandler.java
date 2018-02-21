package cn.jiuling.recog.sendmsg.feature;

import com.loocme.sys.datastruct.Var;

public interface IObjextFeatureHandler
{

    public void initParams(Var params);
    
    public Var getFeature(Var msgVar);
    
    public int sendFeature(Var feature);
    
    public void close();
}
