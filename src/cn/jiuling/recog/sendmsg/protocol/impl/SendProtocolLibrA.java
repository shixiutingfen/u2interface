package cn.jiuling.recog.sendmsg.protocol.impl;

import java.beans.PropertyVetoException;
import java.util.Date;

import com.loocme.plugin.spring.comp.SqlExecutor;
import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.StringUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import cn.jiuling.recog.manager.constants.MyIpaddress;
import cn.jiuling.recog.manager.constants.TaskConstants;
import cn.jiuling.recog.manager.entity.VsdTask;
import cn.jiuling.recog.sendmsg.protocol.IObjextSendProtocol;
import cn.jiuling.recog.sendmsg.util.RandomUtils;

public class SendProtocolLibrA
        implements IObjextSendProtocol, java.io.Serializable
{

    private static final long serialVersionUID = 3359504093174035903L;

    private static ComboPooledDataSource dataSource = null;

    public void initParams(Var params)
    {
        try
        {
            dataSource = new ComboPooledDataSource();
            // 设置连接数，url，驱动，用户名，密码，初始化连接数，最大连接数
            dataSource.setJdbcUrl(params.getString("jdbcUrl"));
            dataSource.setDriverClass(params.getString("driverClass"));
            dataSource.setUser(params.getString("user"));
            dataSource.setPassword(params.getString("password"));
            dataSource.setInitialPoolSize(5);
            dataSource.setMinPoolSize(5);
            dataSource.setMaxPoolSize(30);
            dataSource.setMaxIdleTime(20);
            dataSource.setAcquireIncrement(5);
            dataSource.setIdleConnectionTestPeriod(60);
            dataSource.setAcquireRetryAttempts(0);
            dataSource.setBreakAfterAcquireFailure(false);
            dataSource.setTestConnectionOnCheckout(false);
            dataSource.setTestConnectionOnCheckin(true);
        }
        catch (PropertyVetoException e)
        {
            e.printStackTrace();
            dataSource = null;
        }
    }

    public int send(int objType, Object msgObj)
    {
        if (!(msgObj instanceof Var))
        {
            return 1;
        }
        
        if (null == dataSource)
        {
            return 3;
        }

        Var msgVar = (Var) msgObj;

        // 通过存储过程写入数据库
        // SqlExecutor executor = SqlExecutor.getInstance();
        long id = RandomUtils.getResultId();
        SqlExecutor executor = SqlExecutor.getInstance(dataSource);
        String createTime = msgVar.getString("createTime");
        String insertTime = msgVar.getString("createTime");
        StringBuffer sb = new StringBuffer();
        Long cameraId = 0l;
        Date entryTime = null;
        Integer frameRage = 0;
        long fromType = 0;
        VsdTask taskInfo = TaskConstants
                .getTaskInfo(msgVar.getString("serialNumber"));
        if (null != taskInfo.getTaskRelation())
        {
            fromType = taskInfo.getTaskRelation().getFromType();
            if (1 == fromType || 4 == fromType)
            {
                // 实时流
                cameraId = taskInfo.getTaskRelation().getCameraFileId();
            }
            else if (2 == fromType || 3 == fromType)
            {
                // 离线视频
                if (null != taskInfo.getTaskRelation().getOffUnitFile())
                {
                    cameraId = taskInfo.getTaskRelation().getOffUnitFile()
                            .getCameraId();
                    entryTime = taskInfo.getTaskRelation().getOffUnitFile()
                            .getEntryTime();
                    frameRage = taskInfo.getTaskRelation().getOffUnitFile()
                            .getFramerate();
                }
            }
        }
        // 离线
        if (null != entryTime)
        {
            createTime = getCreateTime(entryTime, frameRage,
                    msgVar.getInt("snapshot.frameIdx"));
        }

        String xywh = msgVar.getInt("snapshot.boundingBox.x") + ","
                + msgVar.getInt("snapshot.boundingBox.y") + ","
                + msgVar.getInt("snapshot.boundingBox.w") + ","
                + msgVar.getInt("snapshot.boundingBox.h");
        if (objType == 1 || objType == 4)
        {
            int bike_genre = 0;
            int wheels = msgVar.getInt("features.wheels");
            if (wheels == 1)
            {
                bike_genre = 3;
            }
            else if (wheels == 2)
            {
                bike_genre = 2;
            }
            else if (wheels == 3)
            {
                bike_genre = 5;
            }
            String costStyle = "0";
            String trousersStyle = "0";
            int upperClothing = msgVar.getInt("upperClothing");
            int lowerClothing = msgVar.getInt("lowerClothing");
            if (upperClothing == 262144)
            {
                costStyle = "1";
            }
            else if (upperClothing == 524288)
            {
                costStyle = "2";
            }
            else if (upperClothing == 33554432)
            {
                costStyle = "1";
            }
            else if (upperClothing == 67108864)
            {
                costStyle = "2";
            }
            if (lowerClothing == 1048576)
            {
                trousersStyle = "1";
            }
            else if (lowerClothing == 2097152)
            {
                trousersStyle = "2";
            }
            else if (lowerClothing == 4194304)
            {
                trousersStyle = "3";
            }

            sb.append(" insert into objext_result");
            sb.append(" (id,serialnumber,cameraid,imgurl,");
            sb.append(
                    " bigimgurl,objtype,maincolor_tag_1,maincolor_tag_2,upcolor_tag_1,");
            sb.append("upcolor_tag_2,lowcolor_tag_1,lowcolor_tag_2,");
            sb.append(
                    "sex,age,wheels,size,bag,handbag,glasses,umbrella,angle,");
            sb.append(
                    "tubeid,objid,startframeidx,endframeidx,startframepts,endframepts,");
            sb.append("frameidx,width,height,xywh,distance,");
            sb.append("bike_genre,seating_count,helmet,helmet_color_tag_1,");
            sb.append("bike_has_plate,createtime,");
            sb.append(
                    "inserttime,recog_id,slave_ip,coat_style,trousers_style)");
            sb.append(" values (");
            sb.append("?,?,?,?,?,?,?,?,?,?,");
            sb.append("?,?,?,?,?,?,?,?,?,?,");
            sb.append("?,?,?,?,?,?,?,?,?,?,");
            sb.append("?,?,?,?,?,?,?,?,?,?,");
            sb.append("?,?,?)");
            executor.addSql(sb.toString(), id, msgVar.getString("serialNumber"),
                    cameraId, msgVar.getString("imgURL"),
                    msgVar.getString("bigImgURL"), objType,
                    this.getColorByCode(
                            msgVar.getString("features.mainColor1.code")),
                    this.getColorByCode(
                            msgVar.getString("features.mainColor2.code")),
                    this.getColorByCode(
                            msgVar.getString("features.coatColor1.code")),
                    this.getColorByCode(
                            msgVar.getString("features.coatColor2.code")),
                    this.getColorByCode(
                            msgVar.getString("features.trousersColor1.code")),
                    this.getColorByCode(
                            msgVar.getString("features.trousersColor2.code")),
                    msgVar.getInt("features.sex"),
                    msgVar.getInt("features.age"),
                    msgVar.getInt("features.wheels"), 0,
                    msgVar.getInt("features.bag"),
                    msgVar.getInt("features.carryBag"),
                    msgVar.getInt("features.glasses"),
                    msgVar.getInt("features.umbrella"),
                    msgVar.getInt("features.angle"), 0, msgVar.getInt("objId"),
                    msgVar.getInt("startFrameIdx"),
                    msgVar.getInt("endFrameIdx"),
                    msgVar.getInt("startFramePts"),
                    msgVar.getInt("endFramePts"),
                    msgVar.getInt("snapshot.frameIdx"),
                    msgVar.getInt("snapshot.width"),
                    msgVar.getInt("snapshot.height"), xywh, 0, bike_genre, 0,
                    msgVar.getInt("features.helmet.have"),
                    this.getColorByCode(
                            msgVar.getString("features.helmet.color")),
                    msgVar.getInt("features.bikeHasPlate"), createTime,
                    insertTime, msgVar.getString("uuid"), MyIpaddress.Local_Ip,
                    costStyle, trousersStyle);
        }
        else
        {
            sb.append(" insert into vlpr_result ");
            sb.append(" (id,serialnumber,CarColor,smallimgurl,ImageURL,");
            sb.append(
                    "frame_index,InsertTime,CreateTime,vehicleKind,cameraId,objid,");
            sb.append(
                    "startframepts,startframeidx,recog_id,slave_ip,vehicleBrand,License)");
            sb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            executor.addSql(sb.toString(), id, msgVar.getString("serialNumber"),
                    msgVar.getString("features.colorName"),
                    msgVar.getString("imgURL"), msgVar.getString("bigImgURL"),
                    msgVar.getInt("startFrameIdx"), insertTime, createTime,
                    msgVar.getString("features.type"), cameraId,
                    msgVar.getInt("objId"), msgVar.getInt("startFramePts"),
                    msgVar.getInt("startFrameIdx"), msgVar.getString("uuid"),
                    MyIpaddress.Local_Ip,
                    msgVar.getString("features.brandName"),
                    msgVar.getString("features.plateLicence"));
        }

        int succCount = executor.commit();

        return succCount > 0 ? 0 : 2;
    }

    private Integer getColorByCode(String code)
    {
        if (StringUtil.isNull(code) || !StringUtil.isInteger(code)) return null;
        if ("-1".equals(code)) return null;
        return StringUtil.getInteger(code);
    }

    private String getCreateTime(Date entryTime, Integer frameRate,
            Integer frameidx)
    {
        Integer second = frameidx / frameRate;
        Long tmpTime = entryTime.getTime() / 1000 + second;
        Date createTime = DateUtil.getDateBySecond(tmpTime);
        String createTimeStr = DateUtil.getFormat(createTime,
                DateFormatConst.YMDHMS_);
        return createTimeStr;
    }

    @Override
    public void close()
    {
        dataSource.close();
        dataSource = null;
    }

}
