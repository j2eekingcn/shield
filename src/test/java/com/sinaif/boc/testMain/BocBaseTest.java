package com.sinaif.boc.testMain;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.sinaif.king.enums.common.FinanceCommonParamEnum;
import com.sinaif.king.shield.boc.api.BocRequestTask;


/**
 * @Description : 测试父类
 * @author : 陈惟鲜
 * @Date : 2018年5月8日 下午3:59:52
 *
 */
@RunWith(JUnit4.class)
public class BocBaseTest {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected BocRequestTask bocRequestTask;
	
	@Before
	public void testBefore(){
		System.out.println("=========@Before===========");
		
		bocRequestTask = new BocRequestTask();
	}
	
	/**
	  * @return
	  * @Description : 初始化请求参数
	  * @author : 陈惟鲜
	  * @Date : 2018年5月8日 下午5:01:12
	  */
	public BocRequestTask initRequestTask() {

		Map<String, String> productParamsMap = new HashMap<String, String>();
		productParamsMap.put(FinanceCommonParamEnum.PCODE_SERVERURL.getCode(), "http://test.boccfc.cn/paygate/service.xml");
		productParamsMap.put("serverUploadUrl","http://test.boccfc.cn/paygate/uploadImage.json");
//		productParamsMap.put("serverUrl","http://192.168.1.23:18889/service.xml");
//		productParamsMap.put("serverUploadUrl","http://192.168.1.23:18889/uploadImage.json");

		productParamsMap.put("certsPath","D:/boc_templete/key/");
		productParamsMap.put("productCode","0F1413");
		productParamsMap.put("keyPass","123456");
		productParamsMap.put("rcmdStoreName","新浪-虚拟网点");
		productParamsMap.put("merchantId","N03030100000000");
		productParamsMap.put("rcmdStoreCode","N03030100000011");
		productParamsMap.put("version","1.0");
		productParamsMap.put("productName","新易贷-新浪有借");
		productParamsMap.put("keyFileName","N03030100000000.p12");
		productParamsMap.put("merchantCode","N03030100000010");
		productParamsMap.put("netCode","N03030100000011");
		productParamsMap.put("operatorCode","001");
		productParamsMap.put("interest","3");
		productParamsMap.put("interest","3");
		productParamsMap.put("interest2","0.05");
		productParamsMap.put("loantime","3");
		productParamsMap.put("loantime","3");
		productParamsMap.put("times","3");
		productParamsMap.put("withdrawWaitday","30");
		
		
		BocRequestTask bocRequestTask = new BocRequestTask();
		bocRequestTask.setProductParamsMap(productParamsMap);
		logger.info("初始化资金方配置信息,capSideInfo==={}"+JSONObject.toJSONString(productParamsMap));
		return bocRequestTask;
	}
}
