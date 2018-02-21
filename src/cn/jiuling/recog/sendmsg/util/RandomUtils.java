package cn.jiuling.recog.sendmsg.util;

import java.util.Random;
import java.util.UUID;

/**
 * 随机数生成帮助类
 * @author Administrator
 *
 */
public  class RandomUtils {

    /**
     * 获取当前时间做随机串
     * @return
     */
    public static Long getCurrentTime()
    {
        Long id = System.currentTimeMillis();
        return id;
    }
    
	/***
	 * 随机产生32位16进制字符串
	 * @return
	 */
	public static String getRandom32PK(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	} 
	
	
	public static long getResultId(){
		long id =  Long.parseLong(System.currentTimeMillis()+get8RandomValiteCode(4));
		return id;
	}
	/**




	 * @return
	 */
	public static String getRandomValiteCode(int size){
		if(size <= 0) size = 6;//默认6位 
		String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";//随机产生的字符串
		Random random = new Random();//随机种子
		String rst = "";//返回值
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(36));
		}
		return rst;
	}
	
	/**
	 * 获取随机的验证码
	 * @return
	 */
	public static String getRandom6ValiteCode(int size){
		if(size <= 0) size = 6;//默认6位 
		String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";//随机产生的字符串
		Random random = new Random();//随机种子
		String rst = "";//返回值
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(36));
		}
		return rst;
	}
	
	/**
	 * 获取随机数
	 * @return
	 */
	public static String getRandom6Number(int size){
		if(size <= 0) size = 6;//默认6位 
		String randString = "0123456789";//随机产生的字符串
		Random random = new Random();//随机种子
		String rst = "";//返回值
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(6));
		}
		return rst;
	}
	
	/**
	 * 获取8位随机字符串 
	 * @return
	 */
	public static String get8RandomValiteCode(int size){
		if(size <= 0) size = 8;//默认8位 
		String randString = "123456789";//随机产生的字符串
		Random random = new Random();//随机种子
		String rst = "";//返回值
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(9));
		}
		return rst;
	}
	
	
	public static void main(String[] args) {
		long aa = Long.parseLong(System.currentTimeMillis()+RandomUtils.get8RandomValiteCode(4));
		System.out.println(aa);
		/* for (int i=0;i<100;i++)
		 {
			 System.out.println(get24TimeRandom());
		 }*/
		//System.out.println("随机验证码6位:"+getRandomValiteCode(6));
		//System.out.println("随机"+RandomUtils.getRandom32PK().length()+"位："+RandomUtils.getRandom32PK());
		//System.out.println("随机"+RandomUtils.getRandom32BeginTimePK().length()+"位以时间打头："+RandomUtils.getRandom32BeginTimePK());
		//System.out.println("随机"+RandomUtils.getRandom32EndTimePK().length()+"位以时间结尾："+RandomUtils.getRandom32EndTimePK());
		//System.out.println(get8RandomValiteCode(16));
	}
}

