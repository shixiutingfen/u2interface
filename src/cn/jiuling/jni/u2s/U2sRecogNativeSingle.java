package cn.jiuling.jni.u2s;

public class U2sRecogNativeSingle
{
    private static U2sRecogNative recog = null;

    public static void init()
    {
        recog = new U2sRecogNative();
        recog.Init("{}");
    }

    public static void release()
    {
        if (null == recog)
        {
            recog.Release("{}");
        }
    }

    public static byte[] GetFeature(int objType, byte[] picture)
    {
        if (null == recog)
        {
            return null;
        }
        return recog.GetFeature(objType, picture);
    }
    
    public static String ObjectDetectionOnImage(int objType, byte[] picture)
    {
        if (null == recog)
        {
            return null;
        }
        return recog.ObjectDetectionOnImage(objType, picture);
    }
}
