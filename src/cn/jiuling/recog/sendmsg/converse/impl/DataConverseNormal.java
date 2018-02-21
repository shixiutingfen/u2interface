package cn.jiuling.recog.sendmsg.converse.impl;

import com.loocme.sys.datastruct.Var;

import cn.jiuling.recog.sendmsg.converse.IObjextDataConverse;

public class DataConverseNormal implements IObjextDataConverse, java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    @Override
    public Object getPerson(Var person)
    {
        return person;
    }

    @Override
    public Object getBick(Var bike)
    {
        return bike;
    }

    @Override
    public Object getCar(Var car)
    {
        return car;
    }

}
