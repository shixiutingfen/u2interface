package cn.jiuling.recog.manager.util;

import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.loocme.sys.constance.DateFormatConst;
import com.loocme.sys.util.DateUtil;
import com.loocme.sys.util.StringUtil;
import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiException;
import com.sun.jimi.core.JimiWriter;
import com.sun.jimi.core.options.JPGOptions;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringUtils {
	/**
	 * 获取随机数
	 * 
	 * @return
	 */
	public static String getRandom6Number(int size) {
		if (size <= 0)
			size = 6;// 默认6位
		String randString = "0123456789";// 随机产生的字符串
		Random random = new Random();// 随机种子
		String rst = "";// 返回值
		for (int i = 0; i < size; i++) {
			rst += randString.charAt(random.nextInt(6));
		}
		return rst;
	}

	public static byte[] fileToBytes(String filepath) {
		InputStream is = null;
		byte[] retBy = null;
		try {
			is = new FileInputStream(new File(filepath));
			retBy = IOUtils.toByteArray(is);
			IOUtils.closeQuietly(is);
		} catch (Exception e) {
			e.printStackTrace();
			retBy = null;
		}
		return retBy;
	}

	// 根据图片的路径获取base64
	public static String convertPic2base64(String picturePath) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(picturePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	// 根据图片的base64返回图片
	public static void convertBase642Pic(String base64Str, String path) {
		if (StringUtil.isNotNull(base64Str) && StringUtil.isNotNull(path)) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				// 解密
				byte[] b = decoder.decodeBuffer(base64Str);
				// 处理数据
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {
						b[i] += 256;
					}
				}
				OutputStream out = new FileOutputStream(path);
				out.write(b);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取图片格式函数
	 * 
	 * @param file
	 * @return
	 */
	public static String getExtension(byte[] input) {
		// 图片格式
		String format = "";
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(new ByteArrayInputStream(input));
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (iter.hasNext()) {

				format = iter.next().getFormatName();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return format;
	}

	/**
	 * 经测试 支持的格式 (png,gif) -->jpeg 不支持的格式(bmp,tif)
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] forJpg(InputStream input) {
		ByteArrayOutputStream ots = null;
		try {
			ots = new ByteArrayOutputStream();

			JPGOptions options = new JPGOptions();
			options.setQuality(100);

			ImageProducer image = Jimi.getImageProducer(input);
			JimiWriter writer = Jimi.createJimiWriter(Jimi.getEncoderTypes()[3], ots);

			writer.setSource(image);
			writer.setOptions(options);
			writer.putImage(ots);

			return ots.toByteArray();
		} catch (JimiException e) {
			e.printStackTrace();
		} finally {
			if (null != ots) {
				try {
					ots.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static String getLinuxServerCfg(String key) {
		String result = null;
		java.util.Properties props;
		try {
			props = PropertiesLoaderUtils.loadAllProperties("config-linux.properties");
			result = props.getProperty(key);// 根据name得到对应的value
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getYMD000_() {
		return DateUtil.getFormat(DateUtil.getDate(null), DateFormatConst.YMD_) + " 00:00:00";
	}

	public static String getYMDHMS_() {
		return DateUtil.getFormat(new Date(), DateFormatConst.YMDHMS_);
	}

}
