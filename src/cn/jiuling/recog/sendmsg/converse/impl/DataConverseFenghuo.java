package cn.jiuling.recog.sendmsg.converse.impl;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.recog.sendmsg.converse.IObjextDataConverse;

public class DataConverseFenghuo implements IObjextDataConverse, java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    @Override
    public Object getPerson(Var person)
    {
        Var resultVar = Var.newObject();
        resultVar.set("objtype", person.getString("objType"));
        resultVar.set("serialnumber", person.getString("serialNumber"));
        resultVar.set("imgurl", person.getString("imgURL"));
        resultVar.set("bigimgurl", person.getString("bigImgURL"));
        resultVar.set("featuredataurl", "a");
        resultVar.set("tubeid", "");//
        resultVar.set("objid", person.getString("objId"));
        resultVar.set("startframeidx", person.getString("startFrameIdx"));
        resultVar.set("endframeidx", person.getString("endFrameIdx"));
        resultVar.set("startframepts", person.getString("startFramePts"));
        resultVar.set("endframepts", person.getString("endFramePts"));
        
        resultVar.set("snapshot.frameIdx", person.getString("snapshot.frameIdx"));
        resultVar.set("snapshot.width", person.getString("snapshot.width"));
        resultVar.set("snapshot.height", person.getString("snapshot.height"));
        
        resultVar.set("snapshot.boundingbox.x", person.getString("snapshot.boundingBox.x"));
        resultVar.set("snapshot.boundingbox.y", person.getString("snapshot.boundingBox.y"));
        resultVar.set("snapshot.boundingbox.w", person.getString("snapshot.boundingBox.w"));
        resultVar.set("snapshot.boundingbox.h", person.getString("snapshot.boundingBox.h"));
        
        resultVar.set("features.maincolor_tag_1", "");
        resultVar.set("features.maincolor_tag_2", "");
        resultVar.set("features.maincolor_tag_3", "");
        
        resultVar.set("features.upcolor_tag_1", person.getString("features.coatColor1.value"));
        resultVar.set("features.upcolor_tag_2", person.getString("features.coatColor2.value"));
        resultVar.set("features.upcolor_tag_3", "");
        
        resultVar.set("features.lowcolor_tag_1", person.getString("features.trousersColor1.value"));
        resultVar.set("features.lowcolor_tag_2", person.getString("features.trousersColor2.value"));
        resultVar.set("features.lowcolor_tag_3", "");
        
        resultVar.set("features.sex", person.getString("features.sex"));
        resultVar.set("features.age", person.getString("features.age"));
        resultVar.set("features.wheels", "");
        resultVar.set("features.size", "0");
        resultVar.set("features.bag", person.getString("features.bag"));
        resultVar.set("features.glasses", person.getString("features.glasses"));
        resultVar.set("features.umbrella", person.getString("features.umbrella"));
        resultVar.set("features.angle", person.getString("features.angle"));
        resultVar.set("features.bike_color", "");
        resultVar.set("features.bike_genre", "");
        resultVar.set("features.seating_count", "");
        resultVar.set("features.helmet", "");
        return resultVar.toString();
    }

    @Override
    public Object getBick(Var bike)
    {
    	Var resultVar = Var.newObject();
    	resultVar.set("objtype", bike.getString("objType"));
        resultVar.set("serialnumber", bike.getString("serialNumber"));
        resultVar.set("imgurl", bike.getString("imgURL"));
        resultVar.set("bigimgurl", bike.getString("bigImgURL"));
        resultVar.set("featuredataurl", "a");
        resultVar.set("tubeid", "");//
        resultVar.set("objid", bike.getString("objId"));
        resultVar.set("startframeidx", bike.getString("startFrameIdx"));
        resultVar.set("endframeidx", bike.getString("endFrameIdx"));
        resultVar.set("startframepts", bike.getString("startFramePts"));
        resultVar.set("endframepts", bike.getString("endFramePts"));
        
        resultVar.set("snapshot.frameIdx", bike.getString("snapshot.frameIdx"));
        resultVar.set("snapshot.width", bike.getString("snapshot.width"));
        resultVar.set("snapshot.height", bike.getString("snapshot.height"));
        
        resultVar.set("snapshot.boundingbox.x", bike.getString("snapshot.boundingBox.x"));
        resultVar.set("snapshot.boundingbox.y", bike.getString("snapshot.boundingBox.y"));
        resultVar.set("snapshot.boundingbox.w", bike.getString("snapshot.boundingBox.w"));
        resultVar.set("snapshot.boundingbox.h", bike.getString("snapshot.boundingBox.h"));
        
        resultVar.set("features.maincolor_tag_1", bike.getString("features.mainColor1.value"));
        resultVar.set("features.maincolor_tag_2", bike.getString("features.mainColor2.value"));
        resultVar.set("features.maincolor_tag_3", "");
        
        resultVar.set("features.upcolor_tag_1", bike.getString("features.coatColor1.value"));
        resultVar.set("features.upcolor_tag_2", bike.getString("features.coatColor2.value"));
        resultVar.set("features.upcolor_tag_3", "");
        
        resultVar.set("features.lowcolor_tag_1", bike.getString("features.trousersColor1.value"));
        resultVar.set("features.lowcolor_tag_2", bike.getString("features.trousersColor2.value"));
        resultVar.set("features.lowcolor_tag_3", "");
        
        resultVar.set("features.sex", bike.getString("features.sex"));
        resultVar.set("features.age", bike.getString("features.age"));
        resultVar.set("features.wheels", bike.getString("features.wheels"));
        resultVar.set("features.size", "0");
        resultVar.set("features.bag", bike.getString("features.bag"));
        resultVar.set("features.glasses", bike.getString("features.glasses"));
        resultVar.set("features.umbrella", bike.getString("features.umbrella"));
        resultVar.set("features.angle", bike.getString("features.angle"));
        resultVar.set("features.bike_color", bike.getString("features.mainColor1.value"));//
        resultVar.set("features.bike_genre", bike.getString("features.bikeGenre"));
        resultVar.set("features.seating_count", "");//
        String helmet = "0";
        if (bike.getBoolean("features.helmet.have"))
        {
        	helmet = "1";
        }
        resultVar.set("features.helmet", helmet);//
        return resultVar.toString();
    }

    @Override
    public Object getCar(Var car)
    {
    	Var resultVar = Var.newObject();
    	resultVar.set("objtype", car.getString("objType"));
    	resultVar.set("serialnumber", car.getString("serialNumber"));
    	resultVar.set("License", car.getString("features.plateLicence"));
    	resultVar.set("LicenseAttribution", "");//车辆归属地
    	resultVar.set("PlateColor", car.getString("features.plateColorName"));
    	resultVar.set("PlateType", car.getString("features.plateClassCode"));//车牌类型
    	resultVar.set("Confidence", "");//
    	resultVar.set("Bright", "");
    	resultVar.set("Direction", "");//方向
    	resultVar.set("left", "");//车牌坐标
    	resultVar.set("top", "");
    	resultVar.set("right", "");
    	resultVar.set("bottom", "");
    	resultVar.set("CarColor", car.getString("features.colorName"));
    	resultVar.set("CarLogo", car.getString("features.brandName"));
    	resultVar.set("smallimgurl", car.getString("imgURL"));
    	resultVar.set("bigimgurl", car.getString("bigImgURL"));
    	resultVar.set("frame_index", car.getString("snapshot.frameIdx"));
    	resultVar.set("speed", "");
    	resultVar.set("vehicleKind", car.getString("features.type"));
    	String vehicleBrand = "", vehicleSeries = "", vehicleStyle = "";
    	String brandName = car.getString("features.brandName");
    	if (StringUtil.isNotNull(brandName))
    	{
    		String[] brands = brandName.split("-");
    		if (brands.length >= 1)
    		{
    			vehicleBrand = brands[0];
    		}
    		if (brands.length >= 2)
    		{
    			vehicleSeries = brands[1];
    		}
    		if (brands.length >= 3)
    		{
    			vehicleStyle = brands[2];
    			for (int i = 3; i < brands.length; i++) {
    				vehicleStyle += "-" + brands[i];
				}
    		}
    	}
    	resultVar.set("vehicleBrand", vehicleBrand);
    	resultVar.set("vehicleSeries", vehicleSeries);
    	resultVar.set("vehicleStyle", vehicleStyle);
    	resultVar.set("tag", car.getString("features.tagNum"));
    	resultVar.set("paper", car.getString("features.paper"));
    	resultVar.set("sun", car.getString("features.sun"));
    	resultVar.set("drop", car.getString("features.drop"));
    	resultVar.set("call", car.getString("features.hasCall"));
    	resultVar.set("crash", car.getString("features.hasCrash"));
    	resultVar.set("danger", car.getString("features.hasDanger"));
    	resultVar.set("mainBelt", car.getString("features.safetyBelt.mainDriver"));
    	resultVar.set("secondBelt", car.getString("features.safetyBelt.coDriver"));
    	resultVar.set("vehicleLeft", car.getString("snapshot.boundingBox.x"));//
    	resultVar.set("vehicleTop", car.getString("snapshot.boundingBox.y"));//
    	resultVar.set("vehicleRight", car.getString("snapshot.boundingBox.w"));//
    	resultVar.set("vehicleBootom", car.getString("snapshot.boundingBox.h"));//
    	resultVar.set("vehicleConfidence", "");
    	resultVar.set("featuredataurl", "a");
        return resultVar.toString();
    }

    
    public static void main(String[] args) {
    	Var resultVar = Var.newObject();
    	resultVar.set("nihao", null);
    	resultVar.set("he-1", "");
    	resultVar.set("he-2", "xxx");
    	System.out.println(resultVar.toString());
	}
}
