package com.sinaif.boc.testMain;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.sinaif.king.exception.FinanceException;
import com.sinaif.king.model.finance.request.boc.UserQualificationReq;
import com.sinaif.king.model.finance.request.caiyy.AccountBindReq;
import com.sinaif.king.model.finance.request.caiyy.BaseRequest;
import com.sinaif.king.model.finance.response.boc.UserQualificationResult;


public class BocApiTest extends BocBaseTest{

//	身份证ID
	private String idNo = "411081199004235955";
//	客户姓名
	private String customerName = "王正伟";
//	手机号
	private String mobileNo = "15013550001";
	
	String startSign = "\n\n\n\n\n\n\n============startSign============";
	String endSign = "\n\n\n\n\n\n\n============endSign============";

	/**
	 * TODO 所有接口，待定
	 */
	@Test
	public void account_bind(){
		System.out.println(startSign);
		UserQualificationReq req = new UserQualificationReq();

//		身份证ID
		req.setIdNo(idNo);
//		客户姓名
		req.setCustomerName(customerName);
//		手机号
		req.setMobileNo(mobileNo);

        logger.info("【BOC】req===={}", JSONObject.toJSONString(req));
        UserQualificationResult obj = bocRequestTask.invoke(req, UserQualificationResult.class);
        logger.info("【BOC】result===={}", JSONObject.toJSONString(obj));

		
		System.out.println(endSign);
	}
}
