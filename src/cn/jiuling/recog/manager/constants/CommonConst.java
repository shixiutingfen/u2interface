package cn.jiuling.recog.manager.constants;

import java.util.Properties;

import com.loocme.sys.util.PropUtil;

public class CommonConst {
	
	// 是否启用jni
	public static String NATIVE_JNI_OPEN = "off"; 

	private static Properties props = null;

	static {
		loadConfig();
	}

	private static void loadConfig() {
		props = PropUtil.getPropFile("/parameter.properties");
		NATIVE_JNI_OPEN = PropUtil.getString(props, "native.jni.open");
	}
	
}
