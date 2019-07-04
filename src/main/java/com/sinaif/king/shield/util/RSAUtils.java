package com.sinaif.king.shield.util;

import com.alibaba.fastjson.JSONObject;
import com.sinaif.king.finance.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAUtils {
	

	private static Logger logger = LoggerFactory.getLogger(RSAUtils.class);
	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
    public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
	/**
	 * 随机生成密钥对
	 */
	public static KeyPair genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
//		keyPairGen.initialize(2048, new SecureRandom("956098".getBytes()));//种子123456
		keyPairGen.initialize(2048, new SecureRandom("956091".getBytes()));//种子123456
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return keyPair;
	}

	/**
	 * 获取公钥
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return getPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}
	/**
	 * 获取公钥
	 * @param publicKeyStr
	 * @return
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}
	/**
	 * 获取私钥
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static RSAPrivateKey getPrivateKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return getPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}
	/**
	 * 获取私钥
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static RSAPrivateKey getPrivateKey(String privateKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decode(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}
	/**
	 * 公钥加密
	 * @param publicKey
	 * @param plainStr
	 * @return
	 */
	public static String encryptString(RSAPublicKey publicKey, String plainStr) {
		try {
			return new String(encrypt(publicKey, plainStr.getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 公钥加密
	 * @param publicKey
	 * @param plainTextData
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {

			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}
	/**
	 * 私钥解密
	 * @param privateKey
	 * @param cipherData
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}

	/*
	 * 私钥签名
	 */
    public static String sign(String content, String privateKey, String inputCharset)
    {
        try
        {
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf                 = KeyFactory.getInstance("RSA");
            PrivateKey priKey               = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update( content.getBytes(inputCharset) );
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
         
        return null;
    }
    
    
    /**
     * 公钥验签
     * @param content
     * @param sign
     * @param publicKey
     * @param inputCharset
     * @return
     */
     public static boolean verify(String content, String sign, String publicKey, String inputCharset)
     {
         try
         {
             KeyFactory keyFactory = KeyFactory.getInstance("RSA");
             byte[] encodedKey = Base64.decode(publicKey);
             PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
             java.security.Signature signature = java.security.Signature
             .getInstance(SIGN_ALGORITHMS);
             signature.initVerify(pubKey);
             signature.update( content.getBytes(inputCharset) );
             boolean bverify = signature.verify( Base64.decode(sign) );
             return bverify;
              
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
          
         return false;
     }
     
    /**
     * 私钥加密 
     * @param privateKey
     * @param plainTextData
     * @return
     * @throws Exception
     */
 	public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception {
 		if (privateKey == null) {
 			throw new Exception("加密私钥为空, 请设置");
 		}
 		Cipher cipher = null;
 		try {
 			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
 			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
 			byte[] output = cipher.doFinal(plainTextData);
 			return output;
 		} catch (NoSuchAlgorithmException e) {
 			throw new Exception("无此加密算法");
 		} catch (NoSuchPaddingException e) {
 			e.printStackTrace();
 			return null;
 		} catch (InvalidKeyException e) {
 			throw new Exception("加密私钥非法,请检查");
 		} catch (IllegalBlockSizeException e) {
 			throw new Exception("明文长度非法");
 		} catch (BadPaddingException e) {
 			throw new Exception("明文数据已损坏");
 		}
 	}
     
 	
 	/**
 	 * 私钥加密
 	 * @param privateKey
 	 * @param plainStr
 	 * @return
 	 */
	public static String encryptString(RSAPrivateKey privateKey, String plainStr) {
		try {
			return Base64.encode(encrypt(privateKey, plainStr.getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 私钥加密
	 * @param privateKey
	 * @param plainStr
	 * @return
	 */
	public static String encryptString(String privateKey, String plainStr) {
		try {
//			logger.info("privateKey==={},plainStr=={}",privateKey,plainStr);
			RSAPrivateKey privateKey2 = getPrivateKey(privateKey);
//			logger.info("privateKey2====="+privateKey2);
			byte[] encrypt = encrypt(privateKey2, plainStr.getBytes());
			String encode = Base64.encode(encrypt);
//			logger.info("encode====="+encode);
			return encode;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 公钥解密
	 * @param publickKey
	 * @param plainStr
	 * @return
	 */
	public static String decryptString(String publickKey, String plainStr) {
		try {
			return new String(decryptString(getPublicKey(publickKey), plainStr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
 	
	/**
	 * 公钥解密
	 * @param rsaPublicKey
	 * @param cipherData
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(RSAPublicKey rsaPublicKey, byte[] cipherData) throws Exception {
		if (rsaPublicKey == null) {
			throw new Exception("公钥密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}
	
	
	/**
	 * 公钥解密
	 * @param rsaPublicKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptString(RSAPublicKey rsaPublicKey, String data) throws Exception {
		return decrypt(rsaPublicKey, Base64.decode(data));
	}
	
	

    
    public static void main(String[] args) throws Exception {
    	KeyPair keyPair = genKeyPair();
    	String prikey = Base64.encode(keyPair.getPrivate().getEncoded());
    	String pubKey =  Base64.encode(keyPair.getPublic().getEncoded());
    	System.out.println("prikey="+prikey);
    	System.out.println("pubkey="+pubKey);
 //   	prikey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCH4Ecm74vvFO5mqvy2i15qKyVV8E4/fZaxzQy3IU88mWuxPaDMxREkEr32XnXyoEpPb6ovnmNOd00S24b0UlXwvBgHtIXRpUf2SDyDLieba5+KVcV991Y7SsM3+DhAiJqtRkh79sXy9UjeokR/xcuRHUYdqOdFBOLOtS6UUoBvEdJYIPAcdgcJrff3Zg/obb2qQEuNR8evXiesKS5e+00L2nt0jkMW3o1piJnlUfuizP2hPEyemmJdvawqdzrGZLz6KEVlH8sVzYYUQAXAQT8RY/qm/EOVmnjkR/vteMB0KASszN2dU1zEITauLTNqh9OLv3y7fWwWUjk3q32ccuNrAgMBAAECggEAPFSF4zDbliVaETX8jWTmYAeWRUk0xRYTUhD0g1+D2ilY2tK9Ug5yJO05cWnRvxASzqCLFSejelSYYTYd/t+MDx7FIK6z+DkF8cFzNN/KcETiCr84uEyY+iyg73oEKAs/eo5JYtlYXR8x4sBz37PIRj+UQVkcjtWziwqBixPyuN2vB0Plz8QGIhpRDz7v/yLk4E+Rc+0CVrmHUsr87yZXqJKQT8cPt98SpvMg00SDyKByYzc/AfyWbCKIG2gdaCYSfVRuE+AILCMG/7xEiTV9NFRvCzjPa7LvCHmF7f3BsbUwwMakIpstFPOqXdq/gBMVG9ID4SeEgr4ojHR0BT2MUQKBgQDStf8m8Eoc8vduaDpEcZyx9WXCg+FS/+oWfZAAyUCl6HaVw9uF6TtZnMmq0wNP4IoaVkU8GGGmSKSJKGmVEa5qdD0PuMJpA0K8HCEqcHwxrx2pTjauiYyvL5qdANArVfdZReX7guQpO3tA1heKMoWurx1J5UAeKgN2efnW0vpkAwKBgQClFJ+9XoM44yWI3jwV8Y1dtOcMKO91MG07In18W2GdUifzo+B7XUcxY5lzEQdXBQGcIeR9rqrjXa2sKZQp3EVEp5Z1kTbttunzbOWbzoHPFwqLPNmDBnSbdlU3O/Hgr1DJDM18RzhQ7N9KitdRhH06nDnBtSg8uLjChkKzjhCKeQKBgGyG9rs8ij+s2MpR/qjgB4AVT+tG99hDHXOmCnSNM7IAMIvky/2F6l62hZxpgMw0b40vIAva1vjJc419dhwkdjw4UHb3h5KE7mtq5Z4xP5ZrhpB8S2fMSty0XSM+lvmrFnIxsUnA2fluvIXhf1Bbw0T8xs4XuNWQeCYenxMJqZO3AoGABFw2F+deKet9+057Ycz+geIcVCrkoEDGMdCoZdd4DZK08W97p2aAUeKik/J4Mik0sK4IffVWGClKRAjZG/3wam7Ov441WDQDm+Vdby8WKKUgJytVR2utOsn2WD4iFmArDxRifQzYhPKstyhRpZqNQDYOJHqndGsgniqzqlBvSWkCgYBl6ehQykMJu0qXrjPe2Hk6HR5buSRXrEYK6K5DrsQRXVhhY8SSBGbu4OrgTSd2pIBzbUsMY+zvj/Fk2lHQXradlHyF2yrZ4+xMjI1hu96V7ry6NmJmmYVzEpmLL1eWUPv4HVK7k8SekQtd489gFqbkMgvqRWHTDIny4Degy1aw5A==";
    	prikey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQChHEP4d896wdubHz+m8TjoExgrNYMlKsW87+vMZ3smzwiIqGoIG5EQShZqydsgB2JOXn+9s5jVHPEJxHea2Nbn08VP8BAh4Hb4q6VpsKCXkfdstgsS8+Rh+nJzpTxElW+osUBOA+5i4N7i+ut/NXgsxmCxqlJrrYIJI24LFvQHQkAio0qBjp4K9otm6EAF50EZGteBUTWmZa/VuQxUbcix57PYAAZGQHGdEuxxyNuL/qogfNFt9rfpCgMR4xdRZvrDwRwAX6EVWob/Q/iD+GrU4efHv6tBWSh6Esv8Jt/ZnYL3fSR4P5+m7KUuy9gK+X5c5vZEdy4tNH3Y61rR3VvJAgMBAAECggEAfa8FP3KIA2X0IcFw8JVCJZmvwxWN55LEi65HL0CTDCV6rNFlVknbEvAZKNmr/gKEqEqEMMNIuQhI6avA+qWqkVPdm4zVqPfpF/kfo6HMxjFy6fXiEbj+M4kjfCAtMfu6DcmpNrNOZwiyGDRTPvvBcnyXtkH+5k2HIgXntPMFEBtWPdh3poQwJsHnqWpUifCFiQnR5LvBJQZH4ceAwsvGeWIUSCoWhppkh4NRvAaUDon/k4t0aEy1CPzSpS/uQN4Y/N27m8v2/jk8Z0u8Joe+Q03hpEMr7dfSs3bqkIgQX9ZP4d8AEMtzze8Z423PF1yRW9OlUNOX5CF+BT5TP4h6AQKBgQD1mvsJH/J9hIkTjdXYeRq9trsP4RPS/G/wYtBbektUAxk2K9wPm0byU2HQuqEib8VU4VFdkUuiw0+emQ+2BBNunMy1/+d5cn1huKyiR3kH0uaRzVBL1p3qHG+sqZKWE6e/EhoE9WvqQc3Ry9At9pNCzdD2jQwHUtGbzdMMoS/gaQKBgQCn7dJExXHppqyG9pMXOPjwp4b95tBEr8qPluJPFEvE5ypQt1rW43OeORk+FqGwXz2UHCJKIgXgjYnxvHDHxrM02eZTYyTj+jqeGQO8fics38/JxmAwmBu1144pczF4iAbuclYlB7EBY02iX4eS8L1Qng1zaU0VsIyDLCvH94Q0YQKBgQC0Trf3RfXvAgrkSR9yUc448tq32KSGI39GejS+w7Rjk/bBV0eySWu3YVGRPEIplubG3reuOonNjxd3tqTbGnjtnr2G670S4uN7h2ltpY0MGl/dMF6/nmrGQWQW3VLZTMq8slxZwZcdHnwshjVqWPhZdeHv7zKiecGaYWuMfRU56QKBgQCZEzTE86au8fv62vGiDZD+7fcjoy7eLdBbq5KHu1yGFKKCCWGI2LUf2bSk4ERrXaXoSO0I3pK06tB/xuKXeQ0KdEZ8ZLfQCN0+GFdLj0NuqGXk7Cvqn/1CeUdhiVvjHzwSR682+hfjx/2QsbwHueMYhbqFJcvapaCwQad3FK0ygQKBgQC2gK2KQ2GNzkpOSzQh82d1ofyLwNrFl+fFxYycsHWvxpP2yWj/NDqlzRVBzEQv18DKNvtF6PmtV065b0KOpvcpy6kcKjX1ct9HaxL/LcEMo4dUPD7+PGJcZjeisFw2Fn7fh6g3r3qsS0aFGvgdTirjH6IuuGXOPMBw8at6286xjw==";

    	JSONObject json = new JSONObject();
	
		json.put("Mobile", "13812345678");
		json.put("Identifier", "110685189407251014");
		json.put("Name", "张学友");
		json.put("Busid","0019");
		json.put("MerUserId","0019");
		json.put("BankCard","110685189407251014");
		//私钥给商户
		String encryptStr =  RSAUtils.encryptString(prikey, json.toJSONString());
		System.out.println("encryptStr="+encryptStr);
		JSONObject tempJson = new JSONObject();
		tempJson.put("merAccCode","sina-yfq-D896B4D789F548D5A50C12" );
		String sign = RSAUtils.sign(tempJson.toJSONString(), prikey, "UTF-8");
		System.out.println("sign="+sign);
//		//公钥给拉卡拉
//		boolean b =  RSAUtils.verify(json.toJSONString(), sign, pubKey, "UTF-8");
//		System.out.println("b="+b);
//		String decryptStr =  RSAUtils.decryptString(pubKey,encryptStr);
//		
//		System.out.println("decryptStr="+decryptStr);
//    	String plainStr ="srYVFpXGGp8/gdUC2CZBS9n2v1VBR+BgTMoiM78v63RvhjtIHCnYe73pDbxYTw0D80ey3YvZvlkDy/qRRSkrms8gAqYa31XkHayPzu4LRhkX3SgHuywtwSIVlLcH52zk0AlupWWG4BaMzEKk9U0MpqN3GF/SSM23XrDPxUNPDJ2L23meL85FOnTf5XHuxMurIlE8NWPWnHMxF6GpHOsQfDjYGUvWADT8Nwliu9dbzXCdAn0A1Ut2jLC7VyZXbLvtVVxtOd3bT1mlR8zpqHzUeXZFdTDCFgqveo9AH75Qg4aLFVUadxBku1HRSQMwx/kTWWxWIsLc3KF3QUJd3JoFug==";
//		String pub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6oWxHWNHA+2rfYUwsf519bLSVmNNY7Lb9j8jPZl1tyDfjG/RucSwYCZQqMhUcYFtgXSgl+aFp7+yn0THnoGKDJeNcSDipjgCsZZh0V6TnK7gm6VZ8rSltXjqpd3SaZzIsFGtTk696bAABv3BnRAhNAvTYJ10vdkkKT7Nt64+fP25qARbFaZ0TDW3tztUxcIzSBWh3vQ6RZMgWHSH0S6l34Mp0tNOVMKFmYTLSSXPVtc3mVYlBZe9Im9dz8BQbeqEnjDbcg5F9VtDjow/DpGwB9wOas8/SGnBch76blJwpOPw8KjQ9P8/WLTXKviGrBEMAB+z65/99+CzPIesNrgutQIDAQAB";
//		String sign = "KOikTkkVuvov5HrXmXRUzzX6x4zq5HF02OJqZhwcnRZqcSHtLxzr2x/uGRFoezgijgBlnp8PMySwSNqkXC12HSozX9RYJnTo4BQNkJRpx3JJgk37ojaBfSrJYP9SIprZ0Nn5dDEXhZh4iW/4WbtMv2IEfV++uPhorHxTezQojvQBSuMs3kydKBwmQNFWakIjIcdDFDtCJZbib61+J41zJexOjAaZ2IyMBWtpt30DggucEQCIbG+0pRXfShDVRagVcRM5xxx1duGqK5ZqNcGtNuhAa1Uen4V6eMd67PrSn5BJRUam+RUHLSUK/mXD7S3ooXsTr6KP4dTFO5lIMyCryg==";
//		String content = RSAUtils.decryptString(pub, plainStr);
//		System.out.println("content:"+content);
//		System.out.println(RSAUtils.verify(content, sign, pub, "UTF-8"));
	//	String trustKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoifibE0JsIWa2frUgTwCsngiMEgF6cdYFs0EOS/FmSehQ4//DUEr7iRXcPtZBogVi+ov4A1OzdQFb8URjTeW1y7uf4chjrhOsLxZy243q8GMCVN+QM2memZIrN6T8+20fYE7FQlbQLS+Csx5TO9nM15trIOcjDVqmsuQ28MYSviYLN/MUUbtNRAMVeVqSCuRP6vbMeVCfJDFC/uq69m9TEgcbJTKoTR5OPFfbB/YsAoRIfIQjb//3Llt/+01whXqOv5KKVJgzpG7dk4aYIgp5UnSFVkIUHA10IGib7/ikub2N2ZKzzdrlqNNRab/oLmWRKeIHa+w+3zI+hE1l4bcEQIDAQAB";
	//	String EncryptData = "KLfzgs5s37DauHOuGeS5pMfdqS/lUM3sHA3Upr/mE AF3ZEN0NKw1vortP2mmy1b9MFv4zw fx4mUa0SSSacx3xIpHz6hAN5euynpPCIVwFJVZOQtvu6S40NI1Edwp6 Q6L/1Rit27 ucl2jPlF/o0h/dUBjqsoTUGhZaM5vGCEumU0f6rVL1x0097WSQ6VSXsRnOj8hKp8tef D/OIs5czVrrxJVf0wT1umx6/0HkbOonksU2vZWOFoN6K39FWovdC/8rHUhiuBGF4qn1tLzZMalLfxMxsKG WWXLTxGz U8F9iE3MV0IWWLIQphO3uesI4ux0CpggCyvOHEIdajw==";
	//	System.out.println(RSAUtils.decryptString(trustKey, EncryptData.replace(" ", "+")));
//		String reqData = JSONObject.fromObject(reqMap).toString();
//		//加解密
//		String str =encryptString(prikey, reqData);
//    	System.out.println(str);
//    	System.out.println(decryptString(pubKey,str));
//    	//签名
//    	String sign = sign(reqData, prikey, "UTF-8");
//    	System.out.println(sign);
//    	System.out.println(verify(reqData, sign, pubKey, "UTF-8"));
    	
	}
}
