package cn.jiuling.recog.manager.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;

import cn.jiuling.jni.u2s.U2sRecogNativeSingle;
import cn.jiuling.recog.manager.util.StringUtils;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class WsPictureRecog
 */
@WebServlet("/rest/feature/extractFromPicture")
public class PictureFeatureGet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(PictureFeatureGet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PictureFeatureGet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String body = super.LoggerRequest(request);
		JSONObject jobj = null;
		
		if (StringUtil.isNull(body)) {
			super.returnMsg("-1", "请求参数不能为空", null, request, response);
			return;
		}

		try {
			jobj = JSONObject.fromObject(body);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.returnMsg("-1", "请求参数不是json格式", null, request, response);
			return;
		}
		
		if (null == jobj) {
			super.returnMsg("-1", "请求参数不是json格式", null, request, response);
			return;
		}

		String objtype = jobj.getString("objtype");
		if (StringUtil.isNull(objtype)) {
			super.returnMsg("-1", "请求参数objtype不能为空", null, request, response);
			return;
		}

		if (!("1".equals(objtype) || "2".equals(objtype) || "4".equals(objtype))) {
			super.returnMsg("-1", "请求参数objtype只能是1,2,4中的一个", null, request, response);
			return;
		}

		String picture = jobj.getString("picture");
		if (StringUtil.isNull(picture)) {
			super.returnMsg("-1", "请求参数picture不能为空", null, request, response);
			return;
		}

		try {
			JSONObject result = new JSONObject();
			byte[] picBy = Base64.decode(picture.getBytes());
			String suffix = StringUtils.getExtension(picBy);
			logger.info("=================根据base64获取图片的格式：" + suffix);
			if (StringUtil.isNull(suffix)) {
				super.returnMsg("-1", "不支持的图片格式", null, request, response);
				return;
			}

			if (PatternUtil.isNotMatch(suffix, "^(jpg|jpeg)$", Pattern.CASE_INSENSITIVE)) {
				picBy = StringUtils.forJpg(new ByteArrayInputStream(picBy));
				if(picBy == null){
					super.returnMsg("-1", "不支持的图片格式: "+suffix, null, request, response);
					return;
				}
			}

			if (picBy.length > 1000000) {
				logger.info("=============picture length: " + picBy.length);
				super.returnMsg("-1", "图片太大，只能提取单个目标特征", null, request, response);
				return;
			}

			result.put("feature", PictureFeatureGetFuc(Integer.valueOf(objtype), picBy));
			super.returnMsg("0", "Success", result, request, response);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			super.returnMsg("-1", e.toString(), null, request, response);
		}

	}

	/**
	 * 传入base64编码的图片String, 返回经过base64编码后特征字符串
	 * 
	 * @param objtype
	 *            类型
	 * @param picture
	 *            图片base64编码
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	protected String PictureFeatureGetFuc(int objtype, byte[] picBy) {
		byte[] picFeature = U2sRecogNativeSingle.GetFeature(objtype, picBy);
		logger.info("----   get feature ok.   " + new String(Base64.encode(picFeature)));
		return new String(Base64.encode(picFeature));
	}

	/**
	 * 比较原始图片特征信息和特征文件
	 * 
	 * @param objtype
	 *            类型
	 * @param picturePath
	 *            图片地址
	 * @param binPath
	 *            bin文件地址
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	/*
	 * protected String PictureFeatureGetFuc1(int objtype, String picturePath,
	 * String binPath, HttpServletRequest request, HttpServletResponse response)
	 * throws InstantiationException, IllegalAccessException { // String
	 * picturePath = "/home/jnitest/java/1.jpg"; // String binPath =
	 * "/home/jnitest/java/1.bin"; logger.info(
	 * "=====================picturePath===============================" +
	 * picturePath); byte[] picBy = StringUtils.fileToBytes(picturePath);
	 * logger.info(
	 * "=====================picBy.length===============================" +
	 * picBy.length); byte[] picFeature =
	 * U2sRecogNativeSingle.GetFeature(objtype, picBy); logger.info(
	 * "=================##====picFeature.length====##========================="
	 * + picFeature.length); logger.info(
	 * "=================##====picFeatureStr====##=========================" +
	 * new String(Base64.encode(picFeature)));
	 * 
	 * logger.info("=====================binPath==============================="
	 * + binPath); byte[] binBy = StringUtils.fileToBytes(binPath); logger.info(
	 * "=====================binBy.length===============================" +
	 * binBy.length); logger.info(
	 * "=================##====binByStr====##=========================" + new
	 * String(Base64.encode(binBy)));
	 * 
	 * logger.info(
	 * "=====================Arrays.equals(picFeature,binBy)==============================="
	 * + Arrays.equals(picFeature, binBy)); return new
	 * String(Base64.encode(picFeature)); }
	 */
	/**
	 * 提取原始图片特征信息
	 * 
	 * @param objtype
	 *            类型
	 * @param picturePath
	 *            图片地址
	 * @return
	 * @throws Exception
	 */
	/*
	 * protected String PictureFeatureGetFuc2(int objtype, String picturePath,
	 * HttpServletRequest request, HttpServletResponse response) throws
	 * Exception { logger.info(
	 * "----------------------[PictureFeatureGetFuc2]----------------"); String
	 * picture = StringUtils.convertPic2base64(picturePath); return
	 * PictureFeatureGetFuc(objtype, picture,request,response); }
	 */

	public static void main(String[] args) {

	}
}
