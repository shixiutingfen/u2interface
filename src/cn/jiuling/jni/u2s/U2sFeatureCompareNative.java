package cn.jiuling.jni.u2s;

public class U2sFeatureCompareNative
{
	static
    {
        System.loadLibrary("CompareFeatures");
        System.loadLibrary("CompareFeature_jni");
    }
	
    public native float CompareFeature(byte[] feature1, byte[] feature2);
}
