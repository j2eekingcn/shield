package com.sinaif.king.shield.util;

import com.alibaba.fastjson.JSON;
import com.sinaif.king.finance.util.RequestParam;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

public class SignUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SignUtil.class);

	public static final String HASH_MD5 = "MD5";
	// 签名私钥
	public static final String SALT = "UyF8qExaLdi6QQwYIYjqBGXbUPIOYy";

	/**
	 * 签名
	 * 
	 * @param headerMap
	 * @param bodyMap
	 * @return
	 */
	public static String sign(TreeMap<String, String> headerMap, TreeMap<String, Object> bodyMap,String key) {

		if (headerMap == null) {
			return null;
		}

		RequestParam request = new RequestParam();
		// 报文头
		request.setHeader(headerMap);
		// 报文体
		request.setBody(bodyMap);
		String json = JSON.toJSONString(request);
		logger.info("签名json串====="+json);
		try {

			MessageDigest digest = MessageDigest.getInstance(HASH_MD5);
			String saltkey = SALT;
			if(StringUtils.isNotEmpty(key)){
				saltkey = key;
			}
			logger.info("saltkey===="+saltkey);
			if (saltkey != null) {
				digest.reset();
				digest.update(saltkey.getBytes());
			}
			byte[] encode = digest.digest(json.getBytes());
			String hex = Hex.encodeHexString(encode).toUpperCase();
			// 最后添加签名信息
			headerMap.put("sign", hex);
			logger.info("sign ： " + hex);
			json = JSON.toJSONString(request);
			logger.info("请求报文 ： "+json);
			return hex;

		} catch (NoSuchAlgorithmException ex) {
			String msg = "No native  MessageDigest instance available on the current JVM.";
			ex.printStackTrace();
			logger.error("",ex);
		}
		return "";
	}

	// 对json参数进行转码加密
	public static String encryption(String jsonStr) {
		byte[] input = jsonStr.getBytes();
		// Base64编码
//		Base64 encoder = new Base64();
//		String encoderStr = encoder.byteArrayToBase64(input);
		BASE64Encoder encoder = new BASE64Encoder();
		String encoderStr = encoder.encode(input);
		logger.info("encoderStr:" + encoderStr);

		return encoderStr;
	}

	public static void main(String[] args) {
		TreeMap<String, String> header = new TreeMap<>();
		header.put("appId", "h5609e636cb3f461b4cc2d88e5f50ae904");
		header.put("deviceType", "IOS");
		header.put("partnerId", "00000000005");
		header.put("channelCode", "SINA");
//		header.put("reuestSourceIp", "V1.0");
		header.put("signMethod", "MD5");
		
		header.put("timeStamp", String.valueOf(System.currentTimeMillis()));
		header.put("format", "json");
		TreeMap<String, Object> body = new TreeMap<>();
		body.put("mobile", "18576621134");
		body.put("memberId", "152179199571451758");
		body.put("fingerPrint",
				"{'deviceType':'H5','ip':'0:0:0:0:0:0:0:1','mobile':'18576621134','udid':'51d6f3a4-83fd-47f6-aa92-f13817982e63'}");
		// 进行签名
		String jsonStr = SignUtil.sign(header, body,null);
		logger.info("paramJson:" + jsonStr);

	}
}
