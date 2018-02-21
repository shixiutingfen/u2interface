package cn.jiuling.recog.sendmsg.entity;

import java.util.Date;

import com.loocme.sys.annotation.database.Proceduce;
import com.loocme.sys.annotation.database.SqlParam;

@Proceduce("call sp_insert_objext_result_jmanager(?,?,?,?,?,?,?,?,?,?,"
		+ "?,?,?,?,?,?,?,?,?,?,"
		+ "?,?,?,?,?,?,?,?,?,?,"
		+ "?,?,?,?,?,?,?,?,?,?,"
		+ "?,?)")
public class SaveObjextCallable implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 1)
    private String serialNumber;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 2)
    private String imgUrl;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 3)
    private String bigImgUrl;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 4)
    private Integer objType;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 5)
    private Integer mainColorTag1;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 6)
    private Integer mainColorTag2;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 7)
    private Integer upColorTag1;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 8)
    private Integer upColorTag2;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 9)
    private Integer lowColorTag1;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 10)
    private Integer lowColorTag2;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 11)
    private Integer bikeHasPlate;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 12)
    private Integer helmet;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 13)
    private Integer helmetColorTag1;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 14)
    private Integer sex;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 15)
    private Integer age;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 16)
    private Integer wheels;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 17)
    private Integer size;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 18)
    private Integer bag;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 19)
    private Integer glasses;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 20)
    private Integer umbrella;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 21)
    private Integer angle;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 22)
    private Integer handbag;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 23)
    private Integer tubeId;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 24)
    private Integer objId;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 25)
    private Integer startFrameIdx;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 26)
    private Integer endFrameIdx;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 27)
    private Integer startFramePts;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 28)
    private Integer endFramePts;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 29)
    private Integer frameIdx;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 30)
    private Integer width;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 31)
    private Integer height;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 32)
    private Integer x;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 33)
    private Integer y;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 34)
    private Integer w;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 35)
    private Integer h;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 36)
    private Float distance; 
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 37)
    private Date createTime;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 38)
    private Integer coatStyle;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 39)
    private Integer trousersStyle;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 40)
    private String recogId;
    @SqlParam(Type = SqlParam.TYPE_IN, Index = 41)
    private Long cameraId;
    @SqlParam(Type = SqlParam.TYPE_OUT, Index = 42)
    private Long id;
    public String getSerialNumber()
    {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }
    public String getImgUrl()
    {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }
    public String getBigImgUrl()
    {
        return bigImgUrl;
    }
    public void setBigImgUrl(String bigImgUrl)
    {
        this.bigImgUrl = bigImgUrl;
    }
    public Integer getObjType()
    {
        return objType;
    }
    public void setObjType(Integer objType)
    {
        this.objType = objType;
    }
    public Integer getMainColorTag1()
    {
        return mainColorTag1;
    }
    public void setMainColorTag1(Integer mainColorTag1)
    {
        this.mainColorTag1 = mainColorTag1;
    }
    public Integer getMainColorTag2()
    {
        return mainColorTag2;
    }
    public void setMainColorTag2(Integer mainColorTag2)
    {
        this.mainColorTag2 = mainColorTag2;
    }
    public Integer getUpColorTag1()
    {
        return upColorTag1;
    }
    public void setUpColorTag1(Integer upColorTag1)
    {
        this.upColorTag1 = upColorTag1;
    }
    public Integer getUpColorTag2()
    {
        return upColorTag2;
    }
    public void setUpColorTag2(Integer upColorTag2)
    {
        this.upColorTag2 = upColorTag2;
    }
    public Integer getLowColorTag1()
    {
        return lowColorTag1;
    }
    public void setLowColorTag1(Integer lowColorTag1)
    {
        this.lowColorTag1 = lowColorTag1;
    }
    public Integer getLowColorTag2()
    {
        return lowColorTag2;
    }
    public void setLowColorTag2(Integer lowColorTag2)
    {
        this.lowColorTag2 = lowColorTag2;
    }
    public Integer getBikeHasPlate()
    {
        return bikeHasPlate;
    }
    public void setBikeHasPlate(Integer bikeHasPlate)
    {
        this.bikeHasPlate = bikeHasPlate;
    }
    public Integer getHelmet()
    {
        return helmet;
    }
    public void setHelmet(Integer helmet)
    {
        this.helmet = helmet;
    }
    public Integer getHelmetColorTag1()
    {
        return helmetColorTag1;
    }
    public void setHelmetColorTag1(Integer helmetColorTag1)
    {
        this.helmetColorTag1 = helmetColorTag1;
    }
    public Integer getSex()
    {
        return sex;
    }
    public void setSex(Integer sex)
    {
        this.sex = sex;
    }
    public Integer getAge()
    {
        return age;
    }
    public void setAge(Integer age)
    {
        this.age = age;
    }
    public Integer getWheels()
    {
        return wheels;
    }
    public void setWheels(Integer wheels)
    {
        this.wheels = wheels;
    }
    public Integer getSize()
    {
        return size;
    }
    public void setSize(Integer size)
    {
        this.size = size;
    }
    public Integer getBag()
    {
        return bag;
    }
    public void setBag(Integer bag)
    {
        this.bag = bag;
    }
    public Integer getGlasses()
    {
        return glasses;
    }
    public void setGlasses(Integer glasses)
    {
        this.glasses = glasses;
    }
    public Integer getUmbrella()
    {
        return umbrella;
    }
    public void setUmbrella(Integer umbrella)
    {
        this.umbrella = umbrella;
    }
    public Integer getAngle()
    {
        return angle;
    }
    public void setAngle(Integer angle)
    {
        this.angle = angle;
    }
    public Integer getHandbag()
    {
        return handbag;
    }
    public void setHandbag(Integer handbag)
    {
        this.handbag = handbag;
    }
    public Integer getTubeId()
    {
        return tubeId;
    }
    public void setTubeId(Integer tubeId)
    {
        this.tubeId = tubeId;
    }
    public Integer getObjId()
    {
        return objId;
    }
    public void setObjId(Integer objId)
    {
        this.objId = objId;
    }
    public Integer getStartFrameIdx()
    {
        return startFrameIdx;
    }
    public void setStartFrameIdx(Integer startFrameIdx)
    {
        this.startFrameIdx = startFrameIdx;
    }
    public Integer getEndFrameIdx()
    {
        return endFrameIdx;
    }
    public void setEndFrameIdx(Integer endFrameIdx)
    {
        this.endFrameIdx = endFrameIdx;
    }
    public Integer getStartFramePts()
    {
        return startFramePts;
    }
    public void setStartFramePts(Integer startFramePts)
    {
        this.startFramePts = startFramePts;
    }
    public Integer getEndFramePts()
    {
        return endFramePts;
    }
    public void setEndFramePts(Integer endFramePts)
    {
        this.endFramePts = endFramePts;
    }
    public Integer getFrameIdx()
    {
        return frameIdx;
    }
    public void setFrameIdx(Integer frameIdx)
    {
        this.frameIdx = frameIdx;
    }
    public Integer getWidth()
    {
        return width;
    }
    public void setWidth(Integer width)
    {
        this.width = width;
    }
    public Integer getHeight()
    {
        return height;
    }
    public void setHeight(Integer height)
    {
        this.height = height;
    }
    public Integer getX()
    {
        return x;
    }
    public void setX(Integer x)
    {
        this.x = x;
    }
    public Integer getY()
    {
        return y;
    }
    public void setY(Integer y)
    {
        this.y = y;
    }
    public Integer getW()
    {
        return w;
    }
    public void setW(Integer w)
    {
        this.w = w;
    }
    public Integer getH()
    {
        return h;
    }
    public void setH(Integer h)
    {
        this.h = h;
    }
    public Date getCreateTime()
    {
        return createTime;
    }
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    public Integer getCoatStyle()
    {
        return coatStyle;
    }
    public void setCoatStyle(Integer coatStyle)
    {
        this.coatStyle = coatStyle;
    }
    public Integer getTrousersStyle()
    {
        return trousersStyle;
    }
    public void setTrousersStyle(Integer trousersStyle)
    {
        this.trousersStyle = trousersStyle;
    }
    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }
    
	public String getRecogId() {
		return recogId;
	}
	public void setRecogId(String recogId) {
		this.recogId = recogId;
	}
	public Long getCameraId() {
		return cameraId;
	}
	public void setCameraId(Long cameraId) {
		this.cameraId = cameraId;
	}
	public Float getDistance() {
		return distance;
	}
	public void setDistance(Float distance) {
		this.distance = distance;
	}
    
}
