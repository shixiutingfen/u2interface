package cn.jiuling.recog.sendmsg.feature.impl;

import com.loocme.sys.datastruct.Var;

import cn.jiuling.recog.sendmsg.feature.IObjextFeatureHandler;

public class FeatureHandlerNormal implements IObjextFeatureHandler, java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    @Override
    public Var getFeature(Var msgVar)
    {
        return null;
    }

    @Override
    public int sendFeature(Var featureVar)
    {
        return 0;
    }

    @Override
    public void initParams(Var params)
    {
    }

    @Override
    public void close()
    {
    }
}
