package com.sinaif.king.shield.boc.api;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.FinderException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sinaif.king.common.Constants;
import com.sinaif.king.common.db.RedisCache;
import com.sinaif.king.common.task.TaskQueueManager;
import com.sinaif.king.common.utils.DateUtils;
import com.sinaif.king.common.utils.HNUtil;
import com.sinaif.king.common.utils.HttpClientUtils;
import com.sinaif.king.common.utils.StringUtils;
import com.sinaif.king.common.utils.XmlMapTool;
import com.sinaif.king.enums.common.FinanceCommonParamEnum;
import com.sinaif.king.enums.finance.message.LogReqResultBean;
import com.sinaif.king.exception.FinanceException;
import com.sinaif.king.finance.api.base.LogDbTask;
import com.sinaif.king.model.finance.api.boc.BocRequestBody;
import com.sinaif.king.model.finance.api.boc.BocRequestHeader;
import com.sinaif.king.model.finance.api.boc.BocRequestItem;
import com.sinaif.king.model.finance.api.boc.BocResponseItem;
import com.sinaif.king.model.finance.api.boc.BocResponseResult;
import com.sinaif.king.model.finance.api.boc.XmlHelper;
import com.sinaif.king.model.finance.enums.BocTransCode;
import com.sinaif.king.model.finance.response.boc.BaseResult;
import com.sinaif.king.model.finance.response.boc.FaceCompareResult;
import com.sinaif.king.model.finance.response.boc.LoanApplyResult;
import com.sinaif.king.service.datasync.DataSyncUserRefService;
import com.sinaif.king.service.message.LogRequestService;
import com.sinaif.king.service.product.finance.FinanceControlService;

/**
 * 
 * @Description : 中银的网络任务请求的任务
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @author : Tommy
 * @version : 1.0 Create Date : 2017年4月22日 下午2:35:32
 *
 */
@Service
@Scope("prototype")
public class BocRequestTask {

	private Logger logger = LoggerFactory.getLogger(BocRequestTask.class);

	private XmlMapper xmlMapper = new XmlMapper();
	/** 自动增长的请求ID */
	private AtomicInteger requestId = new AtomicInteger(0);

	private Map<String, String> productParamsMap;
	@Autowired
	private LogRequestService logRequestService;
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private FinanceControlService financeControlService;
	@Autowired
	private DataSyncUserRefService dataSyncUserRefService;

	/***
	 * 根据对象返回结果
	 * 
	 * @param body
	 * @return
	 */
	public void invoke(BocRequestBody body) throws FinanceException {
		invokeHttp(body);
	}

	/***
	 * 根据对象返回结果
	 * 
	 * @param body
	 * @param t
	 * @return
	 */
	public <T> T invoke(BocRequestBody body, Class<T> t) throws FinanceException {
		BocResponseResult result = invokeHttp(body);
		T xx = (T) result.getBody(t);
		if (xx instanceof BaseResult) {
			BaseResult resultx = (BaseResult) xx;
			resultx.setHeader(result.getHead());
		}
		return xx;
	}
	
	/**
	 * 通过json转成对象的方法
	 */
	public <T> T invokeJson(BocResponseResult item, Class<T> t) throws FinanceException {
		T xx = JSONObject.parseObject(JSONObject.toJSON(item.getBody()).toString(), t);
		return xx;
	}

	/**
	 * 根据返回的数据进行
	 * 
	 * @param body
	 * @return
	 */
	private BocResponseResult invokeHttp(BocRequestBody body) {
		BocRequestItem item = new BocRequestItem();
		item.setBody(body);

		try {
			// 进行网络请求
			BocResponseResult result = getHttpRequestData(item);

			// 异常错误拦截
//			String code = result.getHead().getReturnCode();
//			if (!"000000".equals(code)) {
//				String errorMsg = result.getHead().getErrorMsg();
//				throw new FinanceException(code, errorMsg);
//			}
			return result;
		} catch (FinanceException e) {
			logger.error("异常",e);
			throw e;
		} catch (Exception e) {
			logger.error("错误",e);
			throw new FinanceException("", e.getMessage());
		}
	}

	/**
	 * 创建请求的报文
	 * 
	 * @param item
	 * @param code
	 */
	protected void buidHead(BocRequestItem item, BocTransCode code, String id) {
		BocRequestHeader header = item.getBody().getHeader();
		header.setTransCode(item.getBody().getTransCode().val);
		header.setTransReqTime(DateUtils.getCurrentTime());
		header.setTransSeqNo(id);
		header.setMerchantId(productParamsMap.get(FinanceCommonParamEnum.PCODE_MERCHANTID.getCode()));
		header.setProductCode(productParamsMap.get(FinanceCommonParamEnum.PCODE_PRODUCTCODE.getCode()));
		header.setNetCode(productParamsMap.get(FinanceCommonParamEnum.PCODE_NETCODE.getCode()));
		header.setOperatorCode(productParamsMap.get(FinanceCommonParamEnum.PCODE_OPERATORCODE.getCode()));
		header.setRcmdStoreCode(productParamsMap.get(FinanceCommonParamEnum.PCODE_RCMDSTORECODE.getCode()));
		header.setRcmdStoreName(productParamsMap.get(FinanceCommonParamEnum.PCODE_RCMDSTORENAME.getCode()));
		header.setVersion(productParamsMap.get(FinanceCommonParamEnum.PCODE_VERSION.getCode()));
		
		if (BocTransCode.TRANSCODE_7251.val.equals(code.val)) {
			header.setVersion("1.1");
		} else {
			header.setVersion(productParamsMap.get(FinanceCommonParamEnum.PCODE_VERSION.getCode()));
		}
		

		if (StringUtils.isEmpty(header.getCustomerId())) {
			header.setCustomerId(HNUtil.getRandomString(20));
		}
		if (StringUtils.isEmpty(header.getTransType())) {
			header.setTransType("90");
		}
		item.setHeader(header);
	}

	/**
	 * 创建签名认证
	 * 
	 * @return
	 * @throws FinderException
	 */
	protected String buidSignature(BocRequestItem item) throws FinderException {
		String result = "";
		// 加载公钥、私钥
		PrivateKey priKey;
		try {
			logger.info("【签名地址】:" + productParamsMap.get(FinanceCommonParamEnum.PCODE_CERTSPATH.getCode()));
			priKey = XmlHelper.getPrivateKey(productParamsMap.get(FinanceCommonParamEnum.PCODE_CERTSPATH.getCode()) + productParamsMap.get(FinanceCommonParamEnum.PCODE_KEYFILENAME.getCode()), productParamsMap.get(FinanceCommonParamEnum.PCODE_KEYPASS.getCode()));
			result = new String(XmlHelper.signXml(item.toXml().getBytes("UTF-8"), priKey), "UTF-8");
		} catch (Exception e) {
			logger.error("异常",e);
			
			throw new FinderException("签名失败");
		}
		return result;
	}
	
	/**
	 * 创建签名认证
	 * 
	 * @return
	 * @throws FinderException
	 */
	public String buidSignatureBack(BocResponseItem item) throws FinderException {
		String result = "";
		// 加载公钥、私钥
		PrivateKey priKey;
		try {
			logger.info("【签名地址】:" + productParamsMap.get(FinanceCommonParamEnum.PCODE_CERTSPATH.getCode()));
			priKey = XmlHelper.getPrivateKey(productParamsMap.get(FinanceCommonParamEnum.PCODE_CERTSPATH.getCode()) + productParamsMap.get(FinanceCommonParamEnum.PCODE_KEYFILENAME.getCode()), productParamsMap.get(FinanceCommonParamEnum.PCODE_KEYPASS.getCode()));
			result = new String(XmlHelper.signXml(item.toXml().getBytes("UTF-8"), priKey), "UTF-8");
		} catch (Exception e) {
			
			logger.error("签名失败", e);
			throw new FinderException("签名失败");
		}
		return result;
	}
	

	/**
	 * 请求网络数据请求
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws FinderException 
	 */
	protected BocResponseResult getHttpRequestData(BocRequestItem item) throws UnsupportedEncodingException, FinderException {
		// 根据编码判断
		String productid = productParamsMap.get(FinanceCommonParamEnum.COMMON_KING_PRODUCTID.getCode());
		// 终端ID
		String terminalid = productParamsMap.get(FinanceCommonParamEnum.COMMON_KING_TERMINALID.getCode());
		// 请求用户ID
		String reqUserId = item.getBody().getHeader().getCustomerId();
		// 查询用户ID
		String userid = dataSyncUserRefService.getApiUserid(terminalid, reqUserId, productid);
		// 重新设置为原用户ID
		item.getBody().getHeader().setCustomerId(userid);
		
		BocResponseResult result = null;
		String content = "";
		boolean isOK = false;

		LogReqResultBean request = new LogReqResultBean();
		request.setRequesttime(new Date());
		int requestIndex = 0;
		
		String requestIdx = DateUtils.dateToString(new Date(), DateUtils.DATE_FORMAT_1)+HNUtil.getRandomString(4);
		request.setId(requestIdx);
		requestIndex = requestId.incrementAndGet();
		request.setRequeststatus(1);
		// 创建参数
		buidHead(item, item.getBody().getTransCode(),requestIdx);
		String requstXml = buidSignature(item);
		request.setBusinessid(reqUserId);
		try {
			request.setBusinesstype(item.getBody().getTransCode().val);
		} catch (Exception e) {
			logger.error("异常",e);
		}
		request.setRequestbody(requstXml);
		// 用户ID
		String redisKey = Constants.FINANCE_CALL_INFO_KEY(userid);
		// F1_1002
		Object redisValObject = redisCache.get(redisKey);
		// 进行网络请求
		String serverUrl = financeControlService.getServerUrl(productid, userid, productParamsMap);
		request.setRequestdesc(serverUrl);
		logger.info(serverUrl);
		logger.info("【"+requestIndex+"】 请求的数据 交易码：" + item.getBody().getTransCode() + " \n" + requstXml);
		content = HttpClientUtils.sendPost(serverUrl, requstXml.getBytes("UTF-8"));
		logger.info("【"+requestIndex+"】响应的数据：\n" + content);
		result = XmlMapTool.readValue(content, Constants.BOC_XML_ROOT,BocResponseResult.class);//xmlMapper.readValue(content, BocResponseResult.class);
		request.setResponsetime(new Date());
		isOK = true;
		request.setResponsebody(content);
		try{
			if(result!=null){
				request.setRemark(result.getHead().getErrorMsg());
				request.setResponseid(result.getHead().getReturnCode());
				//设置remark字段
				setBocReqResultRamark(item.getBody().getTransCode(), request, result);
			}
			request.setStatus(isOK ? 1 : 0);
			request.setResponsestatus(isOK ? 1 : 0);
			request.setTerminalid(terminalid);
			request.setProductid(productid);
			// 处理信息
			this.handleTemp(redisValObject, request);
			logger.info("【"+requestIndex+"】响应的数据保存开始：\n" );
			TaskQueueManager.getIntance().addTask(new LogDbTask(request, logRequestService));
			logger.info("【"+requestIndex+"】响应的数据保结束：\n" );
		}catch(Exception e){
			logger.error("异常",e);
		}
		return result;
	}
	
	/**
	 * @Description : 临时处理，有借有还调用借口的时候处理
	 * @author : 陈惟鲜 danger
	 * @Date : 2018年8月16日 下午3:46:20
	 * @param request
	 */
	private void handleTemp(Object redisValObject, LogReqResultBean request){
		if (redisValObject == null){
			return ;
		}
		String[] valueArray = redisValObject.toString().split("_");
		if (valueArray == null ||  valueArray.length < 2){
			return ;
		}
		String logSource = valueArray[0];// 产品ID
		String logProductid = valueArray[1];// 终端ID
		request.setResponsestatus(Integer.parseInt(productParamsMap.get(FinanceCommonParamEnum.COMMON_KING_TERMINALID.getCode())));
		request.setTerminalid(logProductid);
		request.setProductid(logSource);
	}

	/**
	 * 设置成功之后的remark字段
	 * @param transCode
	 * @param request
	 * @param result
	 */
	private void setBocReqResultRamark(BocTransCode transCode,LogReqResultBean request,BocResponseResult result) {
		//是否成功，成功才进行存值
		if("000000".equals(result.getHead().getReturnCode())){
			String remark = "";
			//贷款记录数据
			if(BocTransCode.TRANSCODE_2252.equals(transCode)){
				//result = result
				LoanApplyResult loanApplyResult = result.getBody(LoanApplyResult.class);
				remark = loanApplyResult.getResultStatus();
				request.setRemark(remark);
			}else if(BocTransCode.TRANSCODE_1015.equals(transCode)){
				//人脸比对结果
				FaceCompareResult faceCompareResult = result.getBody(FaceCompareResult.class);
				remark = faceCompareResult.getResultStatus();
				request.setRemark(remark);
			}
		}

	}

	public Map<String, String> getProductParamsMap() {
		return productParamsMap;
	}

	public void setProductParamsMap(Map<String, String> productParamsMap) {
		this.productParamsMap = productParamsMap;
	}
}
