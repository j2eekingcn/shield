package com.sinaif.king.shield.boc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sinaif.king.common.ErrorCode;
import com.sinaif.king.common.db.RedisCache;
import com.sinaif.king.common.utils.DateUtils;
import com.sinaif.king.common.utils.HNUtil;
import com.sinaif.king.common.utils.NumberUtils;
import com.sinaif.king.common.utils.RandomMacAddress;
import com.sinaif.king.common.utils.StringUtils;
import com.sinaif.king.constant.DataUtil;
import com.sinaif.king.dao.credit.CreditApplyBeanMapper;
import com.sinaif.king.enums.app.DeviceUploadTypeEnum;
import com.sinaif.king.enums.app.SystemParamTypeEnum;
import com.sinaif.king.enums.app.UserContactTypeEnum;
import com.sinaif.king.enums.common.CommonEnum;
import com.sinaif.king.enums.common.FinanceBusinessRouterEnum;
import com.sinaif.king.enums.common.KeyWordReplaceEnum;
import com.sinaif.king.enums.common.LoanProgressTypeEnum;
import com.sinaif.king.enums.common.LoanStatusEnum;
import com.sinaif.king.enums.datasync.BindTypeEnum;
import com.sinaif.king.enums.finance.ApplyStatusEnum;
import com.sinaif.king.enums.finance.audit.DataItemReturnEnum;
import com.sinaif.king.enums.finance.flow.FlowEnum;
import com.sinaif.king.enums.finance.product.ProductEnum;
import com.sinaif.king.exception.FinanceException;
import com.sinaif.king.finance.api.FinanceProcessorFactory;
import com.sinaif.king.finance.service.IFinanceBusinessService;
import com.sinaif.king.finance.service.base.FinanceBusinessService;
import com.sinaif.king.finance.service.helper.BocBusinessServiceHelper;
import com.sinaif.king.finance.service.helper.CommonServiceHelper;
import com.sinaif.king.model.app.boc.BocDataBean;
import com.sinaif.king.model.app.device.UserApplistBean;
import com.sinaif.king.model.datasync.data.DataSyncVO;
import com.sinaif.king.model.device.UploadDeviceMongoBean;
import com.sinaif.king.model.device.UserGpsMongoBean;
import com.sinaif.king.model.device.vo.DeviceInfoVo;
import com.sinaif.king.model.finance.access.ProductUserAccessBean;
import com.sinaif.king.model.finance.bill.BillDetailBean;
import com.sinaif.king.model.finance.bill.BillInfoBean;
import com.sinaif.king.model.finance.bill.BillProgressBean;
import com.sinaif.king.model.finance.credit.CreditApplyBean;
import com.sinaif.king.model.finance.credit.CreditInfoBean;
import com.sinaif.king.model.finance.data.DataInfoBean;
import com.sinaif.king.model.finance.data.vo.BasicInfoVO;
import com.sinaif.king.model.finance.data.vo.ContactData;
import com.sinaif.king.model.finance.data.vo.IDCardVO;
import com.sinaif.king.model.finance.data.vo.UserBankCardVO;
import com.sinaif.king.model.finance.data.vo.UserInfoVo;
import com.sinaif.king.model.finance.data.vo.UserMediaVO;
import com.sinaif.king.model.finance.product.cache.ProductCache;
import com.sinaif.king.model.finance.repay.RepayApplyBean;
import com.sinaif.king.model.finance.repay.RepayRecordBean;
import com.sinaif.king.model.finance.request.FinanceApiRequest;
import com.sinaif.king.model.finance.request.boc.ContactItem;
import com.sinaif.king.model.finance.request.boc.LoanApplyProgressReq;
import com.sinaif.king.model.finance.request.boc.LoanApplyReq;
import com.sinaif.king.model.finance.request.boc.PhotoItem;
import com.sinaif.king.model.finance.request.boc.UserInoAllInfoReq;
import com.sinaif.king.model.finance.response.boc.AmountQueryResult;
import com.sinaif.king.model.finance.response.boc.LoanApplyResult;
import com.sinaif.king.model.finance.response.boc.LoanApplySubimtResult;
import com.sinaif.king.model.finance.response.boc.LoanSendQueryResult;
import com.sinaif.king.model.finance.response.boc.RepaymentPlanResult;
import com.sinaif.king.model.finance.response.boc.UserAllInfoResult;
import com.sinaif.king.model.finance.system.SystemParamConfBean;
import com.sinaif.king.model.finance.vo.BillCommonVo;
import com.sinaif.king.model.finance.vo.BillCreateVo;
import com.sinaif.king.model.finance.withdraw.WithdrawApplyBean;
import com.sinaif.king.service.access.ProductUserAccessService;
import com.sinaif.king.service.apply.info.ApplyStatusTempService;
import com.sinaif.king.service.bill.BillDetailService;
import com.sinaif.king.service.credit.CreditApplyService;
import com.sinaif.king.service.credit.CreditFlowService;
import com.sinaif.king.service.credit.CreditInfoService;
import com.sinaif.king.service.data.DataSyncService;
import com.sinaif.king.service.data.UserResouceService;
import com.sinaif.king.service.flow.item.FlowService;
import com.sinaif.king.service.loan.LoanBizFinanceRefBeanService;
import com.sinaif.king.service.loan.LoanProgressBeanService;
import com.sinaif.king.service.product.ProductCreditConfService;
import com.sinaif.king.service.product.ProductInfoService;
import com.sinaif.king.service.product.ProductParamRelService;
import com.sinaif.king.service.repay.BillProgressBeanService;
import com.sinaif.king.service.repay.RepayApplyService;
import com.sinaif.king.service.repay.RepayRecordBeanService;
import com.sinaif.king.service.risk.UploadDeviceResultService;
import com.sinaif.king.service.system.ISystemParamConfService;
import com.sinaif.king.service.withdraw.WithdrawApplyService;

/**
 * @author : Roger
 * @version : 1.0
 * @Description : 中银业务处理实现
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @Date : 2018/5/14 15:05
 */
@Service
public class BocBusinessServiceImpl extends FinanceBusinessService implements IFinanceBusinessService {

	@Autowired
	private FinanceProcessorFactory financeProcessorFactory;
	@Autowired
	private CreditApplyService creditApplyService;
	@Autowired
	private CreditInfoService creditInfoService;
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private LoanBizFinanceRefBeanService loanBizFinanceRefBeanService;
	@Autowired
	private WithdrawApplyService withdrawApplyService;
	@Autowired
	private RepayApplyService repayApplyService;
	@Autowired
	private RepayRecordBeanService repayRecordBeanService;
	@Autowired
	private BocBusinessServiceHelper bocBusinessServiceHelper;
	@Autowired
	private CommonServiceHelper commonServiceHelper;
	@Autowired
	private UploadDeviceResultService uploadDeviceResultService;
	@Autowired
	private UserResouceService userResouceService;
	@Autowired
	private ProductParamRelService productParamRelService;
	@Autowired
	private ProductUserAccessService productUserAccessService;
	@Autowired
	private ProductCreditConfService productCreditConfService;
	@Autowired
	private CreditApplyBeanMapper creditApplyBeanMapper;

	@Autowired
	private CreditFlowService creditFlowService;
	
	@Autowired
	private LoanProgressBeanService loanProgressBeanService;

	@Autowired
	private FlowService<Void, Void, Void> flowService;
	
	@Autowired
	private BillProgressBeanService billProgressBeanService;
	
	@Autowired
	private BillDetailService billDetailService;

	@Autowired
	private ApplyStatusTempService applyStatusTempService;

	@Autowired
	private DataSyncService dataSyncService;

	@Autowired
	private ProductInfoService productInfoService;

	@Autowired
	private ISystemParamConfService systemParamConfService;

	@Override
	public void loanApply(CreditApplyBean creditApplyBean) throws FinanceException {
		if (creditApplyBean == null) {
			logger.info("开户订单信息为空");
			return;
		}
		// 获取当前批次信息
		String orderid = creditApplyBean.getId();
		ProductEnum productEnum = ProductEnum.getById(creditApplyBean.getProductid());
		String userid = creditApplyBean.getUserid();
		String terminalid = creditApplyBean.getTerminalid();
		String msgInfo = String.format("，开户送件用户id【%s】,订单号【%s】,产品id【%s】", userid, orderid,creditApplyBean.getProductid());
		logger.info("送件开始了， " + msgInfo);
		try {
			/*********** zinc 新增 数据同步   begin**************************************/
			ProductCache cache = productInfoService.getById(creditApplyBean.getProductid());
			logger.info("产品获取：" + cache + msgInfo);
			Boolean isSend = false;
			DataSyncVO vo = new DataSyncVO();
			if(cache != null && cache.getIssync()) {
				logger.info("同步判断：" + cache.getIssync() + msgInfo);
				vo.setTerminalid(creditApplyBean.getTerminalid());
				vo.setUserid(creditApplyBean.getUserid());
				List<String> productids = Lists.newArrayList();
				productids.add(creditApplyBean.getProductid());
				vo.setProductids(productids);
				vo.setBindtype(BindTypeEnum.SEND_APPL_CHECK.getCode());
				logger.info("数据送件检查开始   vo={}", vo);
				isSend = dataSyncService.check(vo);
				logger.info("数据送件检查完成   vo={}", vo);			
			}
			if(isSend) {
				logger.info("已发送，不再送件，" + msgInfo);
				return ;
			}
			/************zinc 新增 数据同步   end *************************************/
			
			// 判断阶段步骤
			if (!FlowEnum.FIXED_CREDIT_SEND.getId().equals(creditApplyBean.getStagecode())
					&& !FlowEnum.COMM_CREDIT_SUBMIT.getId().equals(creditApplyBean.getStagecode())) {
				logger.info("开户订单信息阶段步骤【" + creditApplyBean.getStagecode() + "】不正确与【"
						+ FlowEnum.FIXED_CREDIT_SEND_RESULT.getId() + "或" + FlowEnum.COMM_CREDIT_SUBMIT.getId() + "】不匹配, "
						+ msgInfo);
				return;
			}
	
			// 获取身份证信息
			IDCardVO cardBean = userResouceService.queryIDCardVO(userid, terminalid, orderid);
			if (cardBean == null) {
				logger.error("身份证信息不存在, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "身份证信息不存在");
				return;
			}
			// 判断用户是否准入，如果已准入，则什么都不做，否则做准入的事
			ProductUserAccessBean productUserAccessBean = bocBusinessServiceHelper.doProductUserAccessBean(creditApplyBean,
					cardBean);
			if (productUserAccessBean == null || !CommonEnum.PRODUCT_USER_ACCESS_ACCESSSTATUS_PASS.getCode()
					.equals(productUserAccessBean.getAccessstatus().toString())) {
				logger.info("准入信息不通过，当前为【" + productUserAccessBean.getAccessstatus() + "】");
				// commonServiceHelper.updateCreditApplyBeanRemark(orderid,"准入信息不通过");
				flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(),null, null, "准入信息不通过");
				// creditFlowService.rejectFlow(productEnum.id,
				// FlowEnum.FIXED_CREDIT_SEND, ApplyStatusEnum.CREDITREJECT, vo,
				// "准入信息不通过");
				return;
			}
	
			// 查询通讯录
			BocDataBean bocDataBean = bocBusinessServiceHelper.runBookData(userid, DeviceUploadTypeEnum.PHONEBOOK.val);
			if (bocDataBean == null) {
				logger.error("通讯录不存在, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid,"通讯录不存在");
				// flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND.getId(), ApplyStatusEnum.CREDITBACK.getCode(), null, "通讯录不存在");
				return;
			}
			UserInfoVo userInfo = userResouceService.queryUserInfoVo(userid);
			if (userInfo == null) {
				logger.error("用户信息不存在, " + msgInfo);
				// commonServiceHelper.updateCreditApplyBeanRemark(orderid,"用户信息不存在");
				flowService.returnFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null, "用户信息不存在");
				loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITBACK,creditApplyBean.getUserid(), creditApplyBean.getProductid(),null, null,
						orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
				saveReturnRecord(creditApplyBean.getId(), creditApplyBean.getUserid(), creditApplyBean.getProductid(), creditApplyBean.getTerminalid(),  DataItemReturnEnum.FAMILY_ADDRESS, DataItemReturnEnum.COMPANY_ADDRESS, DataItemReturnEnum.COMPANY_INFO);
				return;
			}
			// 获取用户基本信息
			BasicInfoVO basicInfo = userResouceService.queryBasicInfoVo(userid, terminalid, orderid);
			List<DataInfoBean> dataInfoBeans = userResouceService.selectDataInfoList(creditApplyBean.getUserid(), orderid, creditApplyBean.getTerminalid());
			if (dataInfoBeans == null || dataInfoBeans.size() == 0) {
				logger.error("用户资料项信息不存在, " + msgInfo);
				// commonServiceHelper.updateCreditApplyBeanRemark(orderid,"用户资料项信息不存在");
				flowService.returnFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null, "用户资料项信息不存在");
				loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITBACK,creditApplyBean.getUserid(), creditApplyBean.getProductid(),null, null,
						orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
				saveReturnRecord(creditApplyBean.getId(), creditApplyBean.getUserid(), creditApplyBean.getProductid(), creditApplyBean.getTerminalid(),  DataItemReturnEnum.COMPANY_PHONE);
				return;
			}
			// 视频
			String vedioKey = DataUtil.getItemNo(CommonEnum.DATA_ITEM_CONF_VIDEO.getCode(), creditApplyBean.getProductid());
			DataInfoBean vedioBean = userResouceService.selectDataInfo(dataInfoBeans, vedioKey);
			if (vedioBean == null) {
				logger.error("小视频资料项不存在, " + msgInfo);
				// commonServiceHelper.updateCreditApplyBeanRemark(orderid,"小视频资料项不存在");
				flowService.returnFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null, "小视频资料项不存在");
				loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITBACK,creditApplyBean.getUserid(), creditApplyBean.getProductid(),null, null,
						orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
				saveReturnRecord(creditApplyBean.getId(), creditApplyBean.getUserid(), creditApplyBean.getProductid(), creditApplyBean.getTerminalid(), DataItemReturnEnum.VIDEO);
				return;
			}
			UserMediaVO media = userResouceService.queryUserMedia(vedioBean.getItemvalue());
			if (media == null) {
				logger.error("小视频信息不存在, " + msgInfo);
				// commonServiceHelper.updateCreditApplyBeanRemark(orderid,"小视频资料文件信息");
				flowService.returnFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null, "小视频资料文件信息");
				loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITBACK,creditApplyBean.getUserid(), creditApplyBean.getProductid(),null, null,
						orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
				saveReturnRecord(creditApplyBean.getId(), creditApplyBean.getUserid(), creditApplyBean.getProductid(), creditApplyBean.getTerminalid(),  DataItemReturnEnum.VIDEO);
				return;
			}
			// 获取银行卡信息：储蓄卡
			String bankType = DataUtil.getItemNo(CommonEnum.DATA_ITEM_CONF_BANKID_CARD.getCode(),
					creditApplyBean.getProductid());
			DataInfoBean cardDataInfoBean = userResouceService.selectDataInfo(dataInfoBeans, bankType);
			if (cardDataInfoBean == null) {
				logger.error("银行卡资料项不存在, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "银行卡资料项不存在");
				return;
			}
			UserBankCardVO bankInfo = userResouceService.queryUserBankCard(cardDataInfoBean.getItemvalue());
			if (bankInfo == null) {
				logger.error("银行卡信息不存在, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "银行卡信息不存在");
				return;
			}
			// 获取银行卡信息：信用卡
			String creditType = DataUtil.getItemNo(CommonEnum.DATA_ITEM_CONF_BANKID_CREDIT.getCode(),
					creditApplyBean.getProductid());
			DataInfoBean creditDataInfoBean = userResouceService.selectDataInfo(dataInfoBeans, creditType);
			UserBankCardVO creditInfo = null;
			if (creditDataInfoBean != null) {
				creditInfo = userResouceService.queryUserBankCard(creditDataInfoBean.getItemvalue());
				if (creditInfo == null) {
					logger.error("信用卡信息未找到不存在, 银行卡id："+creditDataInfoBean.getItemvalue()+ msgInfo);
				}
			}
			// 获取用户的所有联系人信息
			List<ContactData> contactList = userResouceService.queryContactInfoList(userid, terminalid, orderid);
			if (contactList == null) {
				logger.error("联系人信息不存在, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "联系人信息不存在");
				return;
			}
			ContactData familyContact = new ContactData();
			ContactData friendContact = new ContactData();
			ContactData workmateContact = new ContactData();
			for (ContactData contact : contactList) {  //父母子女兄弟姐妹配偶都作为家人类型
				if (UserContactTypeEnum.FAMILY.val == contact.getContacttype() ||
						UserContactTypeEnum.PARENTS.val == contact.getContacttype() || 
	            		UserContactTypeEnum.CHILD.val == contact.getContacttype() ||
	            		UserContactTypeEnum.BROTHERS.val == contact.getContacttype() ||
	            		UserContactTypeEnum.SISTERS.val == contact.getContacttype() ||
	            		UserContactTypeEnum.SPOUSE.val == contact.getContacttype()) {
					familyContact = contact;
				} else if (UserContactTypeEnum.FRIEND.val == contact.getContacttype()) {
					friendContact = contact;
				} else if (UserContactTypeEnum.WORKMATE.val == contact.getContacttype()) {
					workmateContact = contact;
				}
			}
			// 得到applicationId
			String applicationId = loanBizFinanceRefBeanService.getbyServicetypeValue(
					CommonEnum.LOAN_BIZ_FINANCE_REF_SERVICETYPE_ACCESS.getCode(), productUserAccessBean.getId(),
					CommonEnum.LOAN_BIZ_FINANCE_REF_FINANCEKEY_BOC_APPLICATIONID.getCode(), creditApplyBean.getTerminalid());
	
			long time = System.currentTimeMillis();
			// 设备信息
			DeviceInfoVo deviceInfo = uploadDeviceResultService.queryDeviceinfo(userid);
			if (deviceInfo == null) {
				logger.error("用户的设备信息未找到, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "用户的设备信息未找到");
				return;
			}

			logger.info("构建请求参数参数开始了, " + msgInfo);
			// ##1.进行中银贷款申请
			LoanApplyReq req = new LoanApplyReq();
			req.getHeader().setCustomerId(userid);
			req.getHeader().setTransType("10");

			logger.info("查询定位GPS信息开始了, " + msgInfo);
			List<UserGpsMongoBean> gpsMongoBeans = uploadDeviceResultService.queryGPS(userid, "1");
			logger.info(String.format("获取用户的最新一笔GPS数据结束了：%s，花费的时间：%s", userid, (System.currentTimeMillis() - time)));
			UserGpsMongoBean gpsMongoBean = new UserGpsMongoBean();
			if (gpsMongoBeans != null && gpsMongoBeans.size() > 0) {
				gpsMongoBean = gpsMongoBeans.get(0);
			}
			logger.info("查询所有GPS定位信息记录开始了, " + msgInfo);
			// 查询所有GPS定位信息记录
			int cityTrack = bocBusinessServiceHelper.runGpsCityTrack(userid);
			// 设备信息
			String longitude = gpsMongoBean.getLongitude();
			String latitude = gpsMongoBean.getLatitude();
			String ip = gpsMongoBean.getIp();
			if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)) {
				if (!StringUtils.isEmpty(ip)) {
					String gps = HNUtil.getGpsByIp(ip);
					String gpsx[] = gps == null ? null : gps.split(",");
					longitude = gpsx != null && gpsx.length == 2 ? gpsx[0] : "";
					latitude = gpsx != null && gpsx.length == 2 ? gpsx[1] : "";
				}
				if (StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)) {
					logger.error("定位信息不存在, " + msgInfo);
					commonServiceHelper.updateCreditApplyBeanRemark(orderid, "定位信息不存在");
					return;
				}
			}
			// ip的判断
			if (StringUtils.isEmpty(ip)) {
				logger.error("ip信息不存在不存在, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "ip信息不存在不存在");
				return;
			}
			// 对定位城市的判断--直接获取用户的家庭地址选择的城市
			String geoCity = gpsMongoBean.getCity();
			if (StringUtils.isEmpty(geoCity) || geoCity.length() >= 10) {
				geoCity = basicInfo.getAddrcityname();
			}
			if (StringUtils.isEmpty(geoCity)) {
				logger.error("定位城市不存在, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "定位城市不存在");
				return;
	
			}
			req.setBrand(deviceInfo.getBrand());
			req.setLongitude(longitude);
			req.setLatitude(latitude);
			// 超过10位截取
			if (!StringUtils.isEmpty(geoCity) && geoCity.length() > 10) {
				geoCity = geoCity.substring(0, 10);
			}
			req.setIp(ip);
			// 地理轨迹城市数目
			req.setSumOfDifferCity(cityTrack + "");
			// 获取定位城市
			req.setGeoCity(StringUtils.filterSpecialCharOfXml(geoCity));
			logger.info("身份信息填充开始了, " + msgInfo);
			// 身份证参数
			req.setIdNo(cardBean.getCardno().toUpperCase());
			// 身份证有效时间
			Date idstarttime = DateUtils.format(cardBean.getValidstartdate(), DateUtils.DATE_FORMAT_12);
			Date idendtime = DateUtils.format(cardBean.getValidenddate(), DateUtils.DATE_FORMAT_12);
			String idstart = DateUtils.dateToString(idstarttime, DateUtils.DATE_FORMAT_4);
			// 身份证有效开始时间判断
			if (StringUtils.isEmpty(idstart)) {
				logger.error("身份证有效开始时间为空, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "身份证有效开始时间为空");
				return;
			}
			req.setIdNoValidStartDate(idstart);
			// 身份证有效开始时间判断
			String idend = DateUtils.dateToString(idendtime, DateUtils.DATE_FORMAT_4);
			if (StringUtils.isEmpty(idend)) {
				logger.error("身份证有效结束时间为空, " + msgInfo);
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, "身份证有效结束时间为空");
				return;
			}
			req.setIdNoValidEndDate(idend);
	
			req.setCustomerName(StringUtils.filterSpecialCharOfXml(cardBean.getCardname()));
			req.setMobileNo(StringUtils.filterSpecialCharOfXml(userInfo.getUsername()));
			req.setApplicationId(applicationId);
			req.setRegisterDate(DateUtils.dateToString(userInfo.getCreatetime(), DateUtils.DATE_FORMAT_12));
	
			logger.info("设置保存家庭地址参数开始了, " + msgInfo);
			// 地址信息
			req.setHomeAddr1(StringUtils.filterSpecialCharOfXml(basicInfo.getAddrprovincename()));
			req.setHomeAddr2(StringUtils.filterSpecialCharOfXml(basicInfo.getAddrcityname()));
			req.setHomeAddr3(StringUtils.filterSpecialCharOfXml(basicInfo.getAddrareaname()));
			String fafulladdr = basicInfo.getAddr();
			// 地址不能操作25个字
			if (!StringUtils.isEmpty(fafulladdr) && fafulladdr.length() > 25) {
				fafulladdr = fafulladdr.substring(fafulladdr.length() - 25, fafulladdr.length());
			}
			req.setHomeAddr4(StringUtils.filterSpecialCharOfXml(fafulladdr));
			logger.info("设置保存公司地址参数开始了, " + msgInfo);
			String companyname = basicInfo.getCompanyname();
			// 单位名称不能操作15个字
			if (!StringUtils.isEmpty(companyname) && companyname.length() > 15) {
				companyname = companyname.substring(0, 15);
			}
			req.setJobUnit(StringUtils.filterSpecialCharOfXml(companyname));
			req.setUnitAddr1(StringUtils.filterSpecialCharOfXml(basicInfo.getCompanyprovincename()));
			req.setUnitAddr2(StringUtils.filterSpecialCharOfXml(basicInfo.getCompanycityname()));
			req.setUnitAddr3(StringUtils.filterSpecialCharOfXml(basicInfo.getCompanyareaname()));
			String fulladdr = basicInfo.getCompanyaddr();
			// 地址不能超过25个字
			if (!StringUtils.isEmpty(fulladdr) && fulladdr.length() > 25) {
				fulladdr = fulladdr.substring(fulladdr.length() - 25, fulladdr.length());
			}
			req.setUnitAddr4(StringUtils.filterSpecialCharOfXml(fulladdr));
			req.setUnitTelArea(basicInfo.getCompanyzone());
			req.setUnitTelNo(basicInfo.getComptelephone());
			req.setUnitTelExt(
					StringUtils.isEmpty(basicInfo.getCompextensionnum()) ? "0000" : basicInfo.getCompextensionnum());
			logger.info("设置银行卡参数开始了, " + msgInfo);
			// 银行卡信息
			req.setAuthSysId("0101");
			req.setDebitCardPhoneNo("");
			req.setDebitCardNo(bankInfo.getBanknumber());
			req.setOpenBank(bankInfo.getBankname());
			req.setOpenBankId(bankInfo.getBankcode());
			req.setProv(bankInfo.getOpenprovincename());
			req.setCity(bankInfo.getOpencityname());
			logger.info("设置联系人参数开始了, " + msgInfo);
			// 联系人信息
			List<ContactItem> contacts = new ArrayList<ContactItem>();
			ContactItem familyItem = new ContactItem();
			familyItem.setContactName(familyContact.getContactname());
			familyItem.setContactPhone(familyContact.getContactphone());
			familyItem.setContactRelation(String.valueOf(UserContactTypeEnum.FAMILY.val));
			contacts.add(familyItem);
	
			ContactItem friendItem = new ContactItem();
			friendItem.setContactName(friendContact.getContactname());
			friendItem.setContactPhone(friendContact.getContactphone());
			friendItem.setContactRelation(String.valueOf(friendContact.getContacttype()));
			contacts.add(friendItem);
	
			ContactItem workmateItem = new ContactItem();
			workmateItem.setContactName(workmateContact.getContactname());
			workmateItem.setContactPhone(workmateContact.getContactphone());
			workmateItem.setContactRelation(String.valueOf(workmateContact.getContacttype()));
			contacts.add(workmateItem);
	
			req.setContactList(contacts);

			logger.info("固定参数填充开始, " + msgInfo);
			// 这块参数固定
			req.setReturnDate("0");
			req.setPayMode("3");
			req.setLoanDeadline("0");
			req.setEasyPayType("A");
			req.setInstalType("A");
			req.setIntrestFeeType("1");
			if (ProductEnum.SIYJ.equals(productEnum)) {
				req.setIntrestType("D");
				req.setInstalFeeType("A");
				req.setPrePayType("B");
			} else if (ProductEnum.SIYH.equals(productEnum)) {
				req.setIntrestType("A");
				req.setInstalFeeType("E");
				req.setPrePayType("A");
			}
	
			logger.info("设置定位和城市信息参数开始了, " + msgInfo);
	
			req.setAddressBookNumber(String
					.valueOf(StringUtils.isEmpty(bocDataBean.getPhbooknumber()) ? "0" : bocDataBean.getPhbooknumber()));
			req.setAddressBookNumberCity(String.valueOf(
					StringUtils.isEmpty(bocDataBean.getPhbookcitytotal()) ? "0" : bocDataBean.getPhbookcitytotal()));
			req.setAddressBookNumberRow(
					String.valueOf(StringUtils.isEmpty(bocDataBean.getPhbooktotal()) ? "0" : bocDataBean.getPhbooktotal()));
	
			// 信用卡
			// req.setHasCreditCard(creditInfo == null ? "0" : "1");
			req.setCreditCardNo(creditInfo == null ? "" : creditInfo.getBanknumber());
	
			// 设备信息
			logger.info("设置设备信息参数开始了, " + msgInfo);
			req.setDeviceType(deviceInfo.getType() == 1 ? "Android" : "IOS");
			req.setModel("");
			req.setImei(deviceInfo.getImei());
			req.setImsi(deviceInfo.getImsi());
			req.setSerialNo(deviceInfo.getSerialnumber());
			req.setAndroidId(deviceInfo.getAndroidid());
			// mac为空的情况下，随机生成
			if (StringUtils.isEmpty(deviceInfo.getMac()) || deviceInfo.getType() == 2) {
				deviceInfo.setMac(RandomMacAddress.getMacAddrWithFormat(":"));
			}
			req.setMac(deviceInfo.getMac());
			req.setIdfa(deviceInfo.getIfda());
			req.setPlatform(deviceInfo.getPlatform());
			req.setCapacity(deviceInfo.getCapacity());
			req.setVersion(deviceInfo.getVersion());
			req.setTongdun("");
	
			// 申请时间包括时分秒例如：20170122123355
			req.setApplyDate(DateUtils.dateToString(new Date(), DateUtils.DATE_FORMAT_1));
			// 注册时有效通讯录条数
			req.setRegisteAddressBookCount(req.getAddressBookNumber());
			// 申请时有效通讯录条数
			req.setApplyAddressBookCount(req.getAddressBookNumber());
			// 文字影像已核查的标志位1:是 0:否
			req.setFormChecked("1");
			// 文字影像已核查人姓名
			req.setFormCheckPerson("");
	
			// 操作系统版本
			req.setOperationSystemVersion("");
	
			if (ProductEnum.SIYJ.equals(productEnum)) {
				logger.info("有借渠道产品信息, " + msgInfo);
				// 渠道号码
				req.setChannel_id("79");
				req.setChannel_name("推广渠道");
				// 新浪有借的产品代码：默认值
				req.setChannel("0F1413");
				req.setChannelType("");
			} else if (ProductEnum.SIYH.equals(productEnum)) {
				logger.info("有还渠道产品信息, " + msgInfo);
				// 进件类型：2APP，3微博
				req.setApplyType("2");
				// 新浪卡贷的产品代码：默认值
				req.setChannel("0F2213");
				req.setRepaymentAmount("0");
				req.setIncomeAmount("0");
				req.setExpenditureAmount("0");
				// 文字影像已核查人姓名
				req.setFormCheckPerson("001");
				// 获取通话记录及处理得到未接电话、呼入电话、凌晨通话总数
				bocBusinessServiceHelper.runCallRecord(req, userid);
				// 获取应用列表
				logger.info("获取用户的applist数据开始了：" + msgInfo);
				time = System.currentTimeMillis();
				List<UploadDeviceMongoBean> results = uploadDeviceResultService.queryUploadRecord(userid,
						DeviceUploadTypeEnum.APPLIST.val);
				logger.info(String.format("获取用户的applist数据结束了：花费的时间：%s, " + msgInfo, (System.currentTimeMillis() - time)));
				List<UserApplistBean> appList = new ArrayList<UserApplistBean>();
				if (results != null) {
					if (results != null && results.size() > 0) {
						UploadDeviceMongoBean appListBean = results.get(0);
						String jsonContent = appListBean.getContent();
						appList = JSONObject.parseArray(jsonContent, UserApplistBean.class);
						if (appList == null) {
							appList = new ArrayList<UserApplistBean>();
						}
					}
				}
				req.setInstalledAppNum(appList.size() + "");
				// 获取信息时间
				req.setGetInfoTime(DateUtils.dateToString(new Date(), DateUtils.DATE_FORMAT_1));
			}

			logger.info("家庭信息填充, " + msgInfo);
			// 如果地址为空，则取家庭地址
			String famadr = cardBean.getFamadr();
			// 身份证背景为空，则判断
			if (StringUtils.isEmpty(famadr)) {
				famadr = req.getHomeAddr1() + req.getHomeAddr2() + req.getHomeAddr3() + req.getHomeAddr4();
			}
			// 家庭地址 不能操作100个字
			if (!StringUtils.isEmpty(famadr) && famadr.length() > 100) {
				famadr = famadr.substring(0, 100);
			}
			// 户籍所在地址，身份证有有效期setOperationSystemVersion
			req.setIdAddress(famadr);
			req.setValidDate(cardBean.getValidenddate());
			req.setCardIssuer(bankInfo.getBankcode());
			req.setCardIssuerName(bankInfo.getBankname());
			/** 收入 -20171229 */
			String incomecode = productParamRelService.selectCodeByProductidAndSysCode(creditApplyBean.getProductid(),
					SystemParamTypeEnum.INCOME.val, basicInfo.getIncomecode(), creditApplyBean.getTerminalid());
			req.setIncomeRange(incomecode);// 收入代码

			// ##2.进行上传文件
			logger.info("上传身份证反面照开始了, " + msgInfo);
			time = System.currentTimeMillis();
			try {
				bocBusinessServiceHelper.uploadFile(cardBean.getOppimageurl(), userid, "70", productEnum);
			} catch (FinanceException e) {
				if (String.valueOf(ErrorCode.UPLOAD_FILE_NOT_FOUND).equals(e.getError())) {
					logger.info("身份证反面图片文件不存在, " + msgInfo);
					flowService.returnFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null, "身份证反面图片文件不存在");
					return;
				}
				logger.error("进行上传身份证反面照文件FinanceException异常", e);
				throw e;
			}

			logger.info(String.format("上传身份证反面照结束了,使用时间(毫秒)：%s, " + msgInfo, (System.currentTimeMillis() - time)));

			logger.info("上传多媒体文件开始了, " + msgInfo);
			time = System.currentTimeMillis();
			try {
				bocBusinessServiceHelper.uploadFile(media.getMediaurl(), userid, "72", productEnum);
			} catch (FinanceException e) {
				if (String.valueOf(ErrorCode.UPLOAD_FILE_NOT_FOUND).equals(e.getError())) {
					logger.info("身份证正面图片文件不存在, " + msgInfo);
					loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITBACK,creditApplyBean.getUserid(), creditApplyBean.getProductid(),null, null,
							orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
					flowService.returnFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null, "身份证正面图片文件不存在");
					return;
				}
				logger.error("进行人脸返单文件FinanceException异常", e);
				throw e;
			}
			logger.info(String.format("上传多媒体文件结束了,使用时间(毫秒)：%s, " + msgInfo, (System.currentTimeMillis() - time)));

			List<PhotoItem> items = new ArrayList<PhotoItem>();

			logger.info("设置图片地址信息参数开始了, " + msgInfo);
			PhotoItem phone3 = new PhotoItem();
			phone3.setPhotoType("72");
			phone3.setPhotoName(StringUtils.getUrlName(media.getMediaurl()));
			items.add(phone3);

			PhotoItem phone4 = new PhotoItem();
			phone4.setPhotoType("70");
			phone4.setPhotoName(StringUtils.getUrlName(cardBean.getOppimageurl()));
			items.add(phone4);

			req.setPhotoList(items);

			logger.info("开始送件开始了, " + msgInfo);

			FinanceApiRequest<LoanApplyReq> apiRequest = new FinanceApiRequest<LoanApplyReq>(
					creditApplyBean.getProductid(), req);
			LoanApplySubimtResult result = financeProcessorFactory.getProcesser(productEnum).getPreLoanApiService()
					.loanApply(apiRequest, LoanApplySubimtResult.class);
			logger.info("开始送件结束了, " + msgInfo);
			//保存进度
			loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.SENDFINANCE,creditApplyBean.getUserid(), creditApplyBean.getProductid(),null, null, creditApplyBean.getId() ,null,0, null,2,null, creditApplyBean.getTerminalid());
			// 送件成功之后，进行更新状态--不管成功失败，都进行更新到发送中银状态
			// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
			// FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(),
			// ApplyStatusEnum.AUDITINGAPPLY.getCode(),"");
			flowService.successFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND.getId(), null, null);
			

			/*********** zinc 新增 数据同步   begin**************************************/	
			if(cache.getIssync()) {
				try {
					vo.setBindtype(BindTypeEnum.SEND_APPLY.getCode());
					logger.info("数据同步开始   vo={}", vo);
					dataSyncService.trigger(vo);
					logger.info("数据同步完成   vo={}", vo);
				} catch (Exception e) {
					logger.error("数据同步异常 {}", e.getMessage());
				}				
			}
			/************zinc 新增 数据同步   end *************************************/
			
		} catch (FinanceException e) {
			e.printStackTrace();
			String errorCode = e.getError();
			logger.error("送件异常", e);
			logger.error(String.format("送件FinanceException异常[%s]，code[%s], " + msgInfo, e.getMessage(), errorCode));
			logger.info("查询用户状态2252, " + msgInfo);
			// 重复性检查不通过
			if ("309702".equals(errorCode)) {
				// 更新流程状态和贷款状态
				// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
				// FlowEnum.FIXED_CREDIT_RESULT.getId(), 0, "送件不成功被拒，重复性检查不通过");
				flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(),null, null, "送件不成功被拒，重复性检查不通过");
				return;
			} else if ("101020".equals(errorCode)) {// 预审不通过
				// 更新流程状态和贷款状态
				// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
				// FlowEnum.FIXED_CREDIT_RESULT.getId(), 0, "预审不通过");
				loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITREJECT,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
				flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(),null, null, "送件不成功被拒，预审不通过");
				return;
			} else if ("309703".equals(errorCode)) {
				// 产品不能共存
				// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
				// FlowEnum.FIXED_CREDIT_RESULT.getId(), 0, "产品不能共存");
				loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITREJECT,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
				flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(),null, null, "产品不能共存");
				return;
			}

		} catch (Exception e) {
			logger.error(String.format("送件Exception异常[%s], " + msgInfo, e));
			try {
				commonServiceHelper.updateCreditApplyBeanRemark(orderid, e == null ? "送件异常了" : e.getMessage());
			} catch (Exception e2) {
				logger.error("========更新贷款批次号表异常==========" + msgInfo, e);
			}
		}
	}

	@Override
	public void loanApplyProgress(CreditApplyBean creditApplyBean) throws FinanceException {
		if (creditApplyBean == null) {
			logger.info("开户订单信息为空");
			return;
		}
		String userid = creditApplyBean.getUserid();
		String orderid = creditApplyBean.getId();
		ProductEnum productEnum = ProductEnum.getById(creditApplyBean.getProductid());

		String msgInfo = String.format("用户id【%s】,订单号【%s】", userid, orderid);
		// 判断阶段步骤
		java.util.Set<String> flowSet = new HashSet<>();
		flowSet.add(FlowEnum.FIXED_CREDIT_SEND_RESULT.getId());
		flowSet.add(FlowEnum.PROD_ZY_QUESTIONS_SUBMIT.getId());
		if (!flowSet.contains(creditApplyBean.getStagecode())) {
			logger.info("开户订单信息阶段步骤【" + creditApplyBean.getStagecode() + "】不正确与【"
					+ FlowEnum.FIXED_CREDIT_SEND_RESULT.getId() + "】【" + FlowEnum.PROD_ZY_QUESTIONS_SUBMIT.getId()
					+ "】不匹配, " + msgInfo);
			return;
		}
		try {

			// TRANSCODE_2252("2252"), // 贷款申请进度查询
			LoanApplyProgressReq loanApplyProgressReq = new LoanApplyProgressReq();
			loanApplyProgressReq.getHeader().setCustomerId(userid);

			logger.info("送件结果查询开始了, " + msgInfo);
			FinanceApiRequest<LoanApplyProgressReq> apiRequest = new FinanceApiRequest<LoanApplyProgressReq>(
					creditApplyBean.getProductid(), loanApplyProgressReq);
			LoanApplyResult result = financeProcessorFactory.getProcesser(productEnum).getPreLoanApiService()
					.queryApplyProgress(apiRequest, LoanApplyResult.class);

			if (result != null) {
				logger.info("中银接口查询返回,callBack.result=" + JSONObject.toJSONString(result) + msgInfo);
				String status = result.getResultStatus();
				logger.info("中银接口查询返回,callBack.status=" + status + msgInfo);
				logger.info("中银接口查询返回,callBack.userid=" + result.getHeader().getCustomerId() + msgInfo);

				if ("101".equals(status)) {
					// 101 审批处理中 更新信息订单为审批
					logger.info("订单【" + orderid + "】审批处理中，不处理, " + msgInfo);
					// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
					// creditApplyBean.getStagecode(),
					// ApplyStatusEnum.AUDITINGAPPLY.getCode(), "送件结果审批处理中");
				} else if ("102".equals(status)) {
					// 102.审批通过 更新信息订单为通过 ，增加授信信息
					BigDecimal totalAmt = new BigDecimal(result.getTotalAmt());
					// 保存授信信息
					logger.info("执行登记借款人信息, " + msgInfo);
					// 写入授信表信息t_credit_info 保存用户的授信额度和期数
					CreditInfoBean overWriteBean = new CreditInfoBean();
					overWriteBean.setStartdate(DateUtils.string2DateSuper(result.getLoanApproveDate()));
					overWriteBean.setEnddate(DateUtils.string2DateSuper(result.getAmountValidDate()));
					/** 最大提现额度 */
					overWriteBean.setMaxwithdrawamount(totalAmt);
					/** 最小提现额度 */
					overWriteBean.setMinwithdrawamount(totalAmt);
					/** 备注 */
					overWriteBean.setRemark("中银审核通过，获得授信额度");
					commonServiceHelper.saveCreditInfo(creditApplyBean, totalAmt, overWriteBean);
					// 保存中银返回的LoanAcctNo信息
					commonServiceHelper.saveLoanBizFinanceRefBean(userid, creditApplyBean.getProductid(),
							CommonEnum.LOAN_BIZ_FINANCE_REF_SERVICETYPE_CREDIT.getCode(), creditApplyBean.getId(),
							CommonEnum.LOAN_BIZ_FINANCE_REF_FINANCEKEY_BOC_LOANACCTNO.getCode(), result.getLoanAcctNo(),
							new Date(), CommonEnum.LOAN_BIZ_FINANCE_REF_DATAFROM_FINANCE.getCode(), creditApplyBean.getTerminalid());

					// 添加主流程进度：用户初始授信成功
					logger.info("用户初始授信成功,添加进度, " + msgInfo);
					// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
					// FlowEnum.FIXED_WITHDRAW_APPLY.getId(),
					// ApplyStatusEnum.WITHDRAWWAIT.getCode(), "");
					//保存进度
					 Map<String ,Object> key =new HashMap<String, Object>();
		             key.put(KeyWordReplaceEnum.CREDITLINE.desc,totalAmt);
					loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITSUCCESS,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,key, creditApplyBean.getTerminalid());
					flowService.successFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), ApplyStatusEnum.CREDITSUCCESS.getCode(), null);
					// 增加关联授信产品订单信息
//					commonServiceHelper.saveCreditRelationProduct(creditApplyBean);

				} else if ("103".equals(status) || "104".equals(status)) {
					// 判断是否状态改变
					LoanSendQueryResult loanSendQueryResult = bocBusinessServiceHelper
							.getLoanSendQueryResult(creditApplyBean);
					logger.info(
							String.format("二次送件的操作接口返回结果,loanSendQueryResult=[%s], " + msgInfo, loanSendQueryResult));
					if (null != loanSendQueryResult) {
						// 判断是否可以二次送件
						boolean isAgainLoan = "1".equals(loanSendQueryResult.getReapplyFlag()) ? true : false;
						logger.info(String.format("是否可以二次送件:isAgainLoan=[%s], " + msgInfo, isAgainLoan));
						if (isAgainLoan) {
							// 二次送件流程 更改为待送件
							int addDay = 0;
							if (!StringUtils.isEmpty(loanSendQueryResult.getActualReapplyDays())) {
								addDay = Integer.parseInt(loanSendQueryResult.getActualReapplyDays());
							}
							// 保存中银返回的信息
							commonServiceHelper.saveLoanBizFinanceRefBean(userid, creditApplyBean.getProductid(),
									CommonEnum.LOAN_BIZ_FINANCE_REF_SERVICETYPE_CREDIT.getCode(),
									creditApplyBean.getId(),
									CommonEnum.LOAN_BIZ_FINANCE_REF_FINANCEKEY_BOC_ACTUALREAPPLYDAYS.getCode(),
									loanSendQueryResult.getActualReapplyDays(), new Date(),
									CommonEnum.LOAN_BIZ_FINANCE_REF_DATAFROM_FINANCE.getCode(), creditApplyBean.getTerminalid());
							// 更新等待期
							Date waitdate = DateUtils.addDate(new Date(), addDay,Integer.parseInt(CommonEnum.CREDIT_INFO_PERIODUNIT_DAY.getCode()));
							CreditApplyBean updateCreditApplyBean = new CreditApplyBean();
							updateCreditApplyBean.setId(orderid);
							updateCreditApplyBean.setWaitdate(waitdate);
							updateCreditApplyBean.setUpdatetime(new Date());
							logger.info("更新等待期，更新参数：" + JSON.toJSONString(updateCreditApplyBean)  + msgInfo);
							creditApplyBeanMapper.updateByPrimaryKeySelective(updateCreditApplyBean);
							
							if (addDay == 0) {// 0天立即扭转
								// 更改阶段为等待补件、状态为过等待期
								// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
								// FlowEnum.COMM_CREDIT_RETURN.getId(),
								// ApplyStatusEnum.REJECTEDREAPPLY.getCode(),"可以立即申请");
								//保存进度
								loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.REJECTEDREAPPLY,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
								flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_RESULT.getId(), null, null, "可以立即申请");
							} else {
								// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
								// FlowEnum.FIXED_CREDIT_RESULT.getId(),
								// ApplyStatusEnum.WAITINGPERIOD.getCode(),
								// "等待到可送件日期");
								//保存进度
								 Map<String ,Object> key =new HashMap<String, Object>();
					             key.put(KeyWordReplaceEnum.CREDITDAY.desc,addDay);
								loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.WAITINGPERIOD,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,key, creditApplyBean.getTerminalid());
								flowService.errorReturnFlow(orderid, null, FlowEnum.FIXED_CREDIT_RESULT.getId(), null, null, "等待到可送件日期");
							}
						} else {// 直接拒绝
							// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
							// FlowEnum.FIXED_CREDIT_RESULT.getId(),
							// ApplyStatusEnum.CREDITREJECT.getCode(), "审批拒绝 ");
							//添加进度
							loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITREJECT,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
							flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(),null, null, "审批拒绝 ");
						}
					} else if ("ERR214".equals(result.getResultCode())) {// 返回为不可申请则走拒绝流程
						// saveApplyFail(userId, batchId, result);
						// 添加判断是否需要更新导流数据
						// agreeLoanService.updateAgreeResult(batchId,
						// AgreeLoanResultxEnum.FAIL, null,"审批拒绝");
						// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
						// FlowEnum.FIXED_CREDIT_RESULT.getId(),
						// ApplyStatusEnum.CREDITREJECT.getCode(), "审批拒绝 ");
						//添加进度
						loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITREJECT,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
						flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null, "审批拒绝 ");
					}
				}else if ("109".equals(status)) {// 审批拒绝
					//添加进度
					loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.CREDITREJECT,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
					flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null,"审批拒绝 ");
				}else if ("121".equals(status)) {
					// 判断是否状态改变
					// checkProgressSend(userid, batchId);
					// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
					// FlowEnum.PROD_ZY_QUESTIONS.getId(),
					// ApplyStatusEnum.QUESTIONDISTRIBUTED.getCode(), "下发问卷");
					//添加进度
					loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.QUESTIONDISTRIBUTED,userid, creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
					flowService.rejectFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND_RESULT.getId(), null, null,
							"下发问卷");
					// 121.电核反欺诈 TODO
//					 syncQuestions(userid, orderid);
				} else if ("55".equals(status)) {
					// 55.等待提交资料(改当前的任务为) TODO
					// loanApply(batchId);
					// 待送件
					// loanRecordService.updateAllApplyStatus(userid, batchId,
					// ApplyLoanStatusEnum.PENDING);
					// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
					// FlowEnum.FIXED_CREDIT_SEND.getCode(), 0, "电核反欺诈，需要问卷");
					logger.info("返回状态55，暂不处理, " + msgInfo);
				}
			}
			logger.info("送件结果查询结束, " + msgInfo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序异常" + msgInfo, e);
			throw new FinanceException(ErrorCode.SYSTEM_ERROR + "", e.getMessage());
		}
	}

	@Override
	public void sendWithdrawApply(WithdrawApplyBean withdrawApplyBean) throws FinanceException {
		
	}

	@Override
	public void loanMidRisk(CreditApplyBean creditApplyBean) throws FinanceException {

	}

	@Override
	public void withdrawApplyResultQuery(WithdrawApplyBean withdrawApplyBean) throws FinanceException {

		if (withdrawApplyBean == null) {
			logger.info("提现申请信息为空");
			return;
		}
		String userid = withdrawApplyBean.getUserid();
		String productid = withdrawApplyBean.getProductid();
		String orderid = withdrawApplyBean.getOrderid();
		String withdrawid = withdrawApplyBean.getId();
		String msgInfo = String.format("用户id【%s】,订单号【%s】,提现单号【%s】", userid, orderid, withdrawid);
		// 添加流程数据
		ApplyStatusEnum applyStatusEnum = null;

		String merchantOrderId = loanBizFinanceRefBeanService.getbyServicetypeValue(
				CommonEnum.LOAN_BIZ_FINANCE_REF_SERVICETYPE_WITHDRAW.getCode(), withdrawid,
				CommonEnum.LOAN_BIZ_FINANCE_REF_FINANCEKEY_BOC_MERCHANTORDERID.getCode(), withdrawApplyBean.getTerminalid());
		// 账单已存在，已提现过了
		BillCommonVo oldBillCommonVo = selectBillCommonVo(orderid, CommonEnum.BILL_SOURCE_MASTER.getCode(), withdrawApplyBean.getTerminalid());
		if (oldBillCommonVo != null) {
			logger.info("订单已提现过了, " + msgInfo);
			withdrawApplyBean.setStatus(Integer.parseInt(CommonEnum.WITHDRAW_APPLY_STATUS_FAIL.getCode()));
			withdrawApplyBean.setStagecode(FlowEnum.COMM_LENDED.getId());
			withdrawApplyBean
					.setFailurereason("订单已提现过了，已经存在账单，不能再提现，账单ID【" + oldBillCommonVo.getBillInfoBean().getId() + "】");
			applyStatusEnum = ApplyStatusEnum.WITHDRAWFAIL;
			withdrawApplyBean.setUpdatetime(new Date());// 最后更新时间
			withdrawApplyService.updateByPrimaryKeySelective(withdrawApplyBean);
			return;
		}

		logger.info("提现请求开始了, " + msgInfo);
		AmountQueryResult result = bocBusinessServiceHelper.getAmountQueryResult(userid, productid, orderid,
				merchantOrderId, withdrawApplyBean.getApplyamount(), withdrawApplyBean.getCreatetime(), withdrawApplyBean.getTerminalid());
		logger.info("提现请求结束了, " + msgInfo);
		if (result == null) {
			logger.info("result为空, " + msgInfo);
			return;
		}
		logger.info(String.format("同步提现请求返回了,订单状态[0成功，1失败，2处理中]，实际返回[%s]" + msgInfo,
				result != null ? result.getTransStus() : ""));
		
		// 判断是否被冻结
 		UserAllInfoResult checkUserAllInfoResult = bocBusinessServiceHelper.getUserAllInfoResultByParam(
 				withdrawApplyBean.getUserid(), withdrawApplyBean.getOrderid(), withdrawApplyBean.getProductid(),
 				withdrawApplyBean.getTerminalid());

 		if (checkUserAllInfoResult == null) {
 			logger.info("获取账户信息汇总为空, " + msgInfo);
 			return;
 		}
 		// payFrozenStus 0 正常 1 月结中尚未完结 2 还款中尚未完结 3 电商还款冻结中
 		if (!"0".equals(checkUserAllInfoResult.getPayFrozenStus())) {
 			logger.info(
 					"中银账单同步,账户信息payFrozenStus处于非正常状态(" + checkUserAllInfoResult.getPayFrozenStus() + "), " + msgInfo);
 			return;
 		} else {
 			logger.info(
 					"中银账单同步,账户信息payFrozenStus处于正常状态(" + checkUserAllInfoResult.getPayFrozenStus() + "), " + msgInfo);
 		}
		
		
		// 提现成功
        String billId = null;
		if ("0".equals(result.getTransStus())) {// 提现成功
			// 更新提现状态：提现成功
			withdrawApplyBean.setStatus(Integer.parseInt(CommonEnum.WITHDRAW_APPLY_STATUS_SUCCESS.getCode()));
			withdrawApplyBean.setStagecode(FlowEnum.BASE_REPAY.getId());
			withdrawApplyBean.setLendtime(DateUtils.string2DateSuper(result.getOriPayTime()));
			withdrawApplyBean.setIscomplete(Integer.parseInt(CommonEnum.YES.getCode()));
			applyStatusEnum = ApplyStatusEnum.WITHDRAWSUCCESS;
			// 生成待还款账单， 构建账单所需对象
			BillCreateVo billCreateVo = commonServiceHelper.initBillCreateVo(withdrawApplyBean);
			// //获取账单信息（还款计划明细）
			RepaymentPlanResult repaymentPlanResult = bocBusinessServiceHelper.getBillResponse(withdrawApplyBean);
			if (repaymentPlanResult != null && repaymentPlanResult.getDetails() != null
					&& repaymentPlanResult.getDetails().size() > 0) {
				logger.info("提现成功");
				logger.info("【中银提现进度同步】:创建账单  ," + msgInfo);
				/************** 创建主张单工单账单 *******************/
				BillCommonVo mainBillCommonVo = bocBusinessServiceHelper.initBillCommonVo(billCreateVo,
						repaymentPlanResult, CommonEnum.BILL_SOURCE_MASTER.getCode(),
						CommonEnum.BILL_OPERATETYPE_QUERY_WITHDRAW.getCode());
				
				logger.info("开始生成账单," + msgInfo);
				super.insertBillCommonVo(mainBillCommonVo, withdrawApplyBean.getTerminalid());
				logger.info("结束生成账单," + msgInfo);
				// //更新提现状态和流程状态
				logger.info("【提现进度同步】：状态：提现成功," + msgInfo);
				// withdrawApplyBean.setPayGateOrderId(mainWithdrawBean.getFinancevalue());
				applyStatusEnum = ApplyStatusEnum.WITHDRAWSUCCESS;
				//保存进度
				loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.WITHDRAWSUCCESS,withdrawApplyBean.getUserid(), withdrawApplyBean.getProductid(),mainBillCommonVo.getBillInfoBean().getId(), null, withdrawApplyBean.getOrderid() ,null,1, null,1,null, withdrawApplyBean.getTerminalid());
				// 更新授信信息
				commonServiceHelper.updateCreditInfoForWithdraw(withdrawApplyBean,
						mainBillCommonVo.getBillInfoBean().getLoanamount());
                billId = mainBillCommonVo.getBillInfoBean().getId();
			} else {
				logger.info("提现中，获取账单明细结果为空..终止执行," + msgInfo);
				applyStatusEnum = ApplyStatusEnum.WITHDRAWAUDIT;
				return;
			}
		}
		// 提现失败
		else if ("1".equals(result.getTransStus())) {
			logger.info("提现失败，资方返回结果失败：1" + result.getErrorMsg() + msgInfo);
			// 更新提现状态：提现失败
			withdrawApplyBean.setStatus(Integer.parseInt(CommonEnum.WITHDRAW_APPLY_STATUS_FAIL.getCode()));
			withdrawApplyBean.setStagecode(FlowEnum.COMM_LENDED.getId());
			withdrawApplyBean.setFailurereason(result.getErrorMsg() + "\t" + result.getReturnCode());
			applyStatusEnum = ApplyStatusEnum.WITHDRAWFAIL;
			//保存进度
			loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.WITHDREWFAIL,withdrawApplyBean.getUserid(), withdrawApplyBean.getProductid(),null, null, withdrawApplyBean.getOrderid() ,null,1, null,1,null, withdrawApplyBean.getTerminalid());
		}
		// 提现失败
		else if ("9".equals(result.getTransStus())) {
			logger.info("提现失败，资方返回结果失败：9" + result.getErrorMsg() + msgInfo);
			// 更新提现状态：提现失败
			withdrawApplyBean.setStatus(Integer.parseInt(CommonEnum.WITHDRAW_APPLY_STATUS_FAIL.getCode()));
			withdrawApplyBean.setStagecode(FlowEnum.COMM_LENDED.getId());
			withdrawApplyBean.setFailurereason("订单不存在，" + result.getErrorMsg() + "\t" + result.getReturnCode());
			applyStatusEnum = ApplyStatusEnum.WITHDRAWREJECT;
			//保存进度
			loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.WITHDRAWREJECT,withdrawApplyBean.getUserid(), withdrawApplyBean.getProductid(),null, null, withdrawApplyBean.getOrderid() ,null,1, null,1,null, withdrawApplyBean.getTerminalid());
			
		}
		Date resulttime = new Date();
		if (StringUtils.isEmpty(result.getOriPayTime())) {
			resulttime = DateUtils.string2DateSuper(result.getOriPayTime());
		}
		withdrawApplyBean.setIscomplete(Integer.parseInt(CommonEnum.YES.getCode()));
		withdrawApplyBean.setResulttime(resulttime);
		withdrawApplyBean.setUpdatetime(new Date());// 最后更新时间
		withdrawApplyService.updateByPrimaryKeySelective(withdrawApplyBean);
		// 添加流程数据
		if (applyStatusEnum != null) {
			// * 客户或商户销货人员所操作的用款(消费)、撤销、退货、还款交易其实际的处理结果返回；
			// － 0：表示已送至核心且处理成功，核心系统已完全记录相关之信息
			// －
			// 1：表示无法在核心系统中找到任何相关的交易信息记录；可能交易信息根本未送达核心或者已送至核心处理，惟可能处理失败系统已进行回复处理了(只有log有流水记录)
			// －
			// 9：表示电商平台接收到“合作商户信贷平台”送入的电文信息，还未传送至核心系统前先行匹配电文中之“原交易订单号码”是否存在时，无法匹配成功，移入9，直接返回“合作商户信贷平台”。
			if (applyStatusEnum.getCode() == ApplyStatusEnum.WITHDRAWSUCCESS.getCode()) {// 成功
				flowService.successFlow(orderid, billId, FlowEnum.COMM_LENDED.getId(),
						applyStatusEnum.getCode(), null);
			} else if (applyStatusEnum.getCode() == ApplyStatusEnum.WITHDRAWFAIL.getCode()) {
				flowService.rejectFlow(orderid, null, FlowEnum.COMM_LENDED.getId(),
						applyStatusEnum.getCode(), null, null);
			} else {
				flowService.rejectFlow(orderid, null, FlowEnum.COMMON_WITHDRAW_APPLY_SUBMIT.getId(), null, null, null);
			}
		}
	}

	@Override
	public void repaymentApplyResultQuery(RepayApplyBean repayApplyBean) throws FinanceException {
		if (repayApplyBean == null) {
			logger.info("还款申请信息为空");
			return;
		}
		String userid = repayApplyBean.getUserid();
		String productid = repayApplyBean.getProductid();
		ProductEnum productEnum = ProductEnum.getById(productid);
		String orderid = repayApplyBean.getOrderid();
		String repayid = repayApplyBean.getId();
		String terminalid = repayApplyBean.getTerminalid();
		String msgInfo = String.format("用户id【%s】,订单号【%s】,还款单号【%s】,还款主账单号【%s】", userid, orderid, repayid,
				repayApplyBean.getBillid());
		logger.info("还款结果查询 开始了，" + msgInfo);
		String loanAcctNo = bocBusinessServiceHelper.getLoanAcctNo(orderid, repayApplyBean.getTerminalid());
		String merchantOrderId = loanBizFinanceRefBeanService.getbyServicetypeValue(
				CommonEnum.LOAN_BIZ_FINANCE_REF_SERVICETYPE_REPAY.getCode(), repayid,
				CommonEnum.LOAN_BIZ_FINANCE_REF_FINANCEKEY_BOC_MERCHANTORDERID.getCode(), terminalid);

		AmountQueryResult result = bocBusinessServiceHelper.getAmountQueryResult(userid, productid, orderid,
				merchantOrderId, repayApplyBean.getRepayamount(), repayApplyBean.getApplytime(), repayApplyBean.getTerminalid());
		logger.info(String.format("同步还款请求返回了,订单状态[0成功，1失败，2处理中]，实际返回[%s], " + msgInfo, result != null ? result.getTransStus() : ""));
		// "0：成功
		if ("0".equals(result.getTransStus())) {
			// 执行：中银接口查询，（账户信息汇总查询）7251
			UserInoAllInfoReq userInoAllInfoReq = new UserInoAllInfoReq();
			userInoAllInfoReq.setLoanAcctNo(loanAcctNo);
			userInoAllInfoReq.getHeader().setCustomerId(userid);
			FinanceApiRequest<UserInoAllInfoReq> userInoAllInfoReqFinanceApiRequest = new FinanceApiRequest<UserInoAllInfoReq>(
					productid, userInoAllInfoReq);
			UserAllInfoResult queryResult = financeProcessorFactory.getProcesser(productEnum).getPostLoanApiService()
					.getLoanBill(userInoAllInfoReqFinanceApiRequest, UserAllInfoResult.class);
			if (queryResult == null || queryResult.getAcctStus() == null) {
				logger.info("获取账户信息汇总为空, " + msgInfo);
				return;
			}
			// 当天要还的金额
			BigDecimal nowrepayamount = BigDecimal.ZERO;

			if (!StringUtils.isEmpty(queryResult.getTotalLoanAmt())) {
				nowrepayamount = NumberUtils.fen2Yuan(queryResult.getTotalLoanAmt());
			}
			logger.info("待还款金额：" + nowrepayamount.toString());
			// /***======单体还款======*/
			// //添加账单同步逻辑
			logger.info("查询汇总账单，获取代还总和【" + nowrepayamount + "】");
			if (nowrepayamount.doubleValue() == 0) {
				logger.info("查询汇总账单，获取代还总和为0, " + msgInfo);
			}
			if (StringUtils.isEmpty(repayApplyBean.getBillid())) {
				logger.info("获取总账单ID为空, " + msgInfo);
				return;
			}
			BillInfoBean billInfoBean = billVoService.getBillInfoBeanById(repayApplyBean.getBillid());
			if (billInfoBean == null) {
				logger.info("获取主账单信息为空, " + msgInfo);
				return;
			}
			Date resulttime = new Date();
			if (StringUtils.isEmpty(result.getOriPayTime())) {
				resulttime = DateUtils.string2DateSuper(result.getOriPayTime());
			}
			repayApplyBean.setResulttime(resulttime);
			repayApplyBean.setIscomplete(Integer.parseInt(CommonEnum.YES.getCode()));
			repayApplyBean.setApplystatus(Integer.parseInt(CommonEnum.REPAY_APPLY_APPLYSTATUS_PAYSUCCESS.getCode()));
			repayApplyBean.setUpdatetime(new Date());// 最后更新时间
			repayApplyService.updateByPrimaryKeySelective(repayApplyBean);
			
			BillCommonVo httpBillCommonVo = bocBusinessServiceHelper.initHttpBillCommonVo(billInfoBean,CommonEnum.BILL_OPERATETYPE_QUERY_REPAY.getCode());
			/** 还款类型:1账单还款，2一次性结清，3部分提前还款，4逾期还款，5逾期一次性结清*/
			if (CommonEnum.REPAY_APPLY_REPAYTYPE_ONECEPAY.getCode().equals(repayApplyBean.getRepaytype().toString())){
				flowService.successFlow(billInfoBean.getOrderid(), billInfoBean.getId(), FlowEnum.BASE_REPAY.getId(), ApplyStatusEnum.REPAYALLSUCCESS.getCode(), null);
			}else{
				flowService.successFlow(billInfoBean.getOrderid(), billInfoBean.getId(), FlowEnum.BASE_REPAY.getId(), ApplyStatusEnum.REPAYSUCCESS.getCode(), null);
			}
			
			httpBillCommonVo.setRepayApplyId(repayid);
			logger.info("还款更新整单开始, " + msgInfo);
			super.updateBillCommonVo(httpBillCommonVo, repayApplyBean.getTerminalid());
			logger.info("还款更新整单结束, " + msgInfo);
			
		} else if ("1".equals(result.getTransStus())) {
			String newStatus = CommonEnum.REPAY_APPLY_APPLYSTATUS_PAYFAIL.getCode();
			int resultUpdate = repayApplyService.updateRepayApplyBeanStatus(repayid,
					repayApplyBean.getApplystatus().toString(), newStatus, result.getErrorMsg());
			if (resultUpdate > 0) {
				// 还款费用项记录。
				RepayRecordBean repayRecordBean = commonServiceHelper.initRepayRecordBean(repayApplyBean,
						Integer.parseInt(CommonEnum.BILL_PROGRESS_PROGRESS_REPAYMENT.getCode()),
						Integer.parseInt(CommonEnum.REPAY_RECORD_RESULT_2.getCode()), repayApplyBean.getRepayamount(),
						"还款失败");
				repayRecordBeanService.save(repayRecordBean);

				/** 还款类型:1账单还款，2一次性结清，3部分提前还款，4逾期还款，5逾期一次性结清*/
				if (CommonEnum.REPAY_APPLY_REPAYTYPE_ONECEPAY.getCode().equals(repayApplyBean.getRepaytype().toString())
						||CommonEnum.REPAY_APPLY_REPAYTYPE_OVERONECEPAY.getCode().equals(repayApplyBean.getRepaytype().toString())){
					//一次性结清或逾期结清
					String content = loanProgressBeanService.getStatusDesc(LoanStatusEnum.REPAYALLFAILED, userid, orderid, null, null, null, repayApplyBean.getTerminalid());
					List<BillDetailBean> billDetails = billDetailService.getByBillIdNStatus(repayApplyBean.getBillid(), 1);
					for(BillDetailBean billDetail : billDetails) {
			    		insertBillProgressBean(userid, productid,billDetail.getId(), 2,1,content , 1, "一次性结清失败", terminalid);
					}
					billDetails = billDetailService.getByBillIdNStatus(repayApplyBean.getBillid(), 3);
					for(BillDetailBean billDetail : billDetails) {
			    		insertBillProgressBean(userid, productid,billDetail.getId(), 2,1,content , 1, "一次性结清失败", terminalid);
					}
	    			loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.REPAYALLFAILED,userid, productid,repayApplyBean.getBillid(), null, orderid ,"还款失败",1, null,1,null, repayApplyBean.getTerminalid());
					flowService.rejectFlow(orderid, repayApplyBean.getBillid(), FlowEnum.BASE_REPAY.getId(), ApplyStatusEnum.REPAYALLFAILED.getCode(), null,"一次性还款失败");
				}else{
					if(CommonEnum.REPAY_APPLY_REPAYTYPE_REPAY.getCode().equals(repayApplyBean.getRepaytype().toString())) {
						//还款失败
						String content = loanProgressBeanService.getStatusDesc(LoanStatusEnum.REPAYFAILED, userid, orderid, repayApplyBean.getBilldetailid(), null, null, repayApplyBean.getTerminalid());
			    		insertBillProgressBean(userid, productid,repayApplyBean.getBilldetailid(), 2,1,content , 1, "还款失败", terminalid);
			    		loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.REPAYFAILED,userid, productid,repayApplyBean.getBillid(),  repayApplyBean.getBilldetailid(), orderid ,"还款失败",1, null,1,null, repayApplyBean.getTerminalid());    		
			    	}
					else if(CommonEnum.REPAY_APPLY_REPAYTYPE_OVERPAY.getCode().equals(repayApplyBean.getRepaytype().toString())
							&& !StringUtils.isEmpty(repayApplyBean.getBilldetailid())) {
						//逾期单期手动还款失败
						String content = loanProgressBeanService.getStatusDesc(LoanStatusEnum.REPAYFAILED, userid, orderid, repayApplyBean.getBilldetailid(), null, null, repayApplyBean.getTerminalid());
			    		insertBillProgressBean(userid, productid,repayApplyBean.getBilldetailid(), 2,1,content , 1, "逾期单期手动还款失败", terminalid);
	    	    		loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.OVERDUEONEFAILED,userid, productid,repayApplyBean.getBillid(),  repayApplyBean.getBilldetailid(), orderid ,"还款失败",1, null,1,null, repayApplyBean.getTerminalid());			    	
	    	    	}
					else if(CommonEnum.REPAY_APPLY_REPAYTYPE_OVERPAY.getCode().equals(repayApplyBean.getRepaytype().toString())
							&& !StringUtils.isEmpty(repayApplyBean.getBillid())) {
						//逾期多期还款失败
						String content = loanProgressBeanService.getStatusDesc(LoanStatusEnum.REPAYFAILED, userid, orderid, null, null, null, repayApplyBean.getTerminalid());
						List<BillDetailBean> billDetails = billDetailService.getByBillIdNStatus(repayApplyBean.getBillid(), 3);
						for(BillDetailBean billDetail : billDetails) {
				    		insertBillProgressBean(userid, productid,billDetail.getId(), 2,1,content , 1, "逾期手动还款失败", terminalid);
						}
		    			loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.OVERDUEFAILED,userid, productid,repayApplyBean.getBillid(), null, orderid ,"还款失败",1, null,1,null, repayApplyBean.getTerminalid());
	    	    	}
					flowService.rejectFlow(orderid, repayApplyBean.getBillid(), FlowEnum.BASE_REPAY.getId(), ApplyStatusEnum.REPAYFAILED.getCode(), null,"还款失败");
				}
			} else {
				logger.info("更新还款记录失败.影响条数为0, " + msgInfo);
			}
		}
		logger.info("还款结果查询 结束了，" + msgInfo);

	}

	@Override
	public void billSync(BillInfoBean billInfoBean) throws FinanceException {
		String message = "中银账单同步";
		String msgInfo = String.format("用户id【%s】,订单号【%s】,主账单编号【%s】", billInfoBean.getUserid(),
				billInfoBean.getOrderid(), billInfoBean.getId());
		// 账单同步开始时间
		String billSyncStartTime = null;
		// 账单同步结束时间
		String billSyncEndTime = null;
		SystemParamConfBean startTimeBean = systemParamConfService.queryByServiceTypeAndCodeWithCache("zy_bill_sync_start_time","20190225","billSyncStartTime", billInfoBean.getTerminalid());
		if (null != startTimeBean) {
			billSyncStartTime = startTimeBean.getPvalue();
		}
		SystemParamConfBean endTimeBean = systemParamConfService.queryByServiceTypeAndCodeWithCache("zy_bill_sync_end_time","20190225","billSyncEndTime", billInfoBean.getTerminalid());
		if (null != endTimeBean) {
			billSyncEndTime = endTimeBean.getPvalue();
		}

		Date date = new Date();
		if (StringUtils.isNotEmpty(billSyncStartTime)) {
			Date date1 = DateUtils.string2DateByHHmmss(billSyncStartTime, date);
			if (date.compareTo(date1) < 0) {
				logger.info("{} {}：不在同步时间段内，任务结束", message, msgInfo);
				return;
			}
		}
		if (StringUtils.isNotEmpty(billSyncEndTime)) {
			Date date2 = DateUtils.string2DateByHHmmss(billSyncEndTime, date);
			if (date.compareTo(date2) > 0) {
				logger.info("{} {}：不在同步时间段内，任务结束", message, msgInfo);
				return;
			}
		}
		// 判断是否被冻结
 		UserAllInfoResult checkUserAllInfoResult = bocBusinessServiceHelper.getUserAllInfoResultByParam(
 				billInfoBean.getUserid(), billInfoBean.getOrderid(), billInfoBean.getProductid(),
 				billInfoBean.getTerminalid());

 		if (checkUserAllInfoResult == null) {
 			logger.info("获取账户信息汇总为空, " + msgInfo);
 			return;
 		}
 		// payFrozenStus 0 正常 1 月结中尚未完结 2 还款中尚未完结 3 电商还款冻结中
 		if (!"0".equals(checkUserAllInfoResult.getPayFrozenStus())) {
 			logger.info(
 					"中银账单同步,账户信息payFrozenStus处于非正常状态(" + checkUserAllInfoResult.getPayFrozenStus() + "), " + msgInfo);
 			return;
 		} else {
 			logger.info(
 					"中银账单同步,账户信息payFrozenStus处于正常状态(" + checkUserAllInfoResult.getPayFrozenStus() + "), " + msgInfo);
 		}
		

		logger.info(message + "开始, " + msgInfo);

		BillCommonVo httpBillCommonVo = bocBusinessServiceHelper.initHttpBillCommonVo(billInfoBean,
				CommonEnum.BILL_OPERATETYPE_SYN_BILL.getCode());
		if (httpBillCommonVo == null) {
			logger.info("构建同步账单信息为空, " + msgInfo);
			return;
		}
		
		super.updateBillCommonVo(httpBillCommonVo, billInfoBean.getTerminalid());
		logger.info(message + "结束, " + msgInfo);
	}

	@Override
	public void loanApplyAgain(CreditApplyBean creditApplyBean) throws FinanceException {
		String orderid = creditApplyBean.getId();
		String message = "二次送件状态扭转";
		String msgInfo = String.format("用户id【%s】,订单号【%s】", creditApplyBean.getUserid(), orderid);
		logger.info(message + " start, " + msgInfo);
		// 1、判断阶段是结果阶段、状态是等待期
		if (!FlowEnum.FIXED_CREDIT_RESULT.getId().equals(creditApplyBean.getStagecode())) {
			logger.info("阶段不正确，必须为【" + FlowEnum.FIXED_CREDIT_RESULT.getId() + "】，当前为【" + creditApplyBean.getStagecode()
					+ "】，订单ID【" + orderid + "】");
			return;
		}
		if (ApplyStatusEnum.WAITINGPERIOD.getCode() != creditApplyBean.getApplystatus().intValue()) {
			logger.info("状态不不正确，必须为【" + ApplyStatusEnum.WAITINGPERIOD.getCode() + "】，当前为【"
					+ creditApplyBean.getStagecode() + "】，订单ID【" + orderid + "】");
			return;
		}
		// 2、更改阶段为等待补件、状态为过等待期
		// commonServiceHelper.updateCreditApplyBeanRemark(orderid,
		// FlowEnum.COMM_CREDIT_RETURN.getId(),
		// ApplyStatusEnum.REAPPLYWAITED.getCode(),"过了等待期，状态扭转待补件");
		//保存进度
		loanProgressBeanService.saveLoanProgress(LoanProgressTypeEnum.REAPPLYWAITED,creditApplyBean.getUserid(), creditApplyBean.getProductid(),null, null, orderid ,null,0, null,1,null, creditApplyBean.getTerminalid());
		applyStatusTempService.updateStatusDisabled(creditApplyBean.getUserid(), creditApplyBean.getId(), null);
		flowService.returnFlow(orderid, null, FlowEnum.FIXED_CREDIT_RESULT.getId(), null, null, "过了等待期，状态扭转待补件");

		logger.info(message + " end, " + msgInfo);
	}

	@Override
	public void idCardValidate(CreditApplyBean creditApplyBean) {
		//
//		String orderid = creditApplyBean.getId(),userid = creditApplyBean.getUserid(),productid = creditApplyBean.getProductid();
//		String terminalid = creditApplyBean.getTerminalid();
//		ProductEnum productEnum = ProductEnum.getById(productid);
//		String msgInfo = String.format("用户id【%s】,订单号【%s】", creditApplyBean.getUserid(), orderid);
//		// 获取身份证信息
//		IDCardVO cardBean = userResouceService.queryIDCardVO(userid, terminalid, orderid);
//		// ##2.进行上传文件
//		logger.info("上传身份证反面照开始了, " + msgInfo);
//		long time = System.currentTimeMillis();
//		try {
//			bocBusinessServiceHelper.uploadFile(cardBean.getOppimageurl(), userid, "70", productEnum);
//		} catch (FinanceException e) {
//			if (String.valueOf(ErrorCode.UPLOAD_FILE_NOT_FOUND).equals(e.getError())) {
//				logger.info("身份证反面图片文件不存在, " + msgInfo);
//				return;
//			}
//			logger.error("进行上传身份证反面照文件FinanceException异常", e);
//			throw e;
//		}
//		logger.info(String.format("上传身份证反面照结束了,使用时间(毫秒)：%s, " + msgInfo, (System.currentTimeMillis() - time)));
//		List<PhotoItem> items = new ArrayList<PhotoItem>();
//		logger.info("设置图片地址信息参数开始了, " + msgInfo);
//
//		PhotoItem phone = new PhotoItem();
//		phone.setPhotoType("70");
//		phone.setPhotoName(StringUtils.getUrlName(cardBean.getOppimageurl()));
//		items.add(phone);
//		IdCardValidDateReq req = new IdCardValidDateReq();
//		// 身份证有效时间
//		Date idstarttime = DateUtils.format(cardBean.getValidstartdate(), DateUtils.DATE_FORMAT_12);
//		Date idendtime = DateUtils.format(cardBean.getValidenddate(), DateUtils.DATE_FORMAT_12);
//		String idstart = DateUtils.dateToString(idstarttime, DateUtils.DATE_FORMAT_4);
//		// 身份证有效开始时间判断
//		if (StringUtils.isEmpty(idstart)) {
//			logger.error("身份证有效开始时间为空, " + msgInfo);
//			commonServiceHelper.updateCreditApplyBeanRemark(orderid, "身份证有效开始时间为空");
//			flowService.rejectFlow(orderid, null, FlowEnum.FIXED_WITHDRAW_APPLY.getId(),
//					ApplyStatusEnum.CREDITBACK.getCode(), null, "身份证反面图片文件不存在");
//			return;
//		}
//		// 身份证有效开始时间判断
//		String idend = DateUtils.dateToString(idendtime, DateUtils.DATE_FORMAT_4);
//		if (StringUtils.isEmpty(idend)) {
//			logger.error("身份证有效结束时间为空, " + msgInfo);
//			commonServiceHelper.updateCreditApplyBeanRemark(orderid, "身份证有效结束时间为空");
//			flowService.rejectFlow(orderid, null, FlowEnum.FIXED_WITHDRAW_APPLY.getId(),
//					ApplyStatusEnum.WITHDRAWWAIT.getCode(), null, "身份证反面图片文件不存在");
//			return;
//		}
//		req.setIdNoStartDate(idstart);
//		req.setIdNoDeadlineDate(idend);
//		req.setPhotoList(items);
//
//		logger.info("开始处理身份证过期修改了, " + msgInfo);
//		FinanceApiRequest<IdCardValidDateReq> apiRequest = new FinanceApiRequest<IdCardValidDateReq>(
//				creditApplyBean.getProductid(), req);
//		financeProcessorFactory.getProcesser(productEnum).getCommonApiService()
//				.commonRequest(apiRequest, CommonResult.class,BocTransCode.TRANSCODE_1019);
//		logger.info("处理身份证过期修改结束了, " + msgInfo);
//		//TODO 改成提交身份证审核中状态
//		flowService.successFlow(orderid, null, FlowEnum.FIXED_CREDIT_SEND.getId(), null, null);
	}

	@Override
	public <R, T> R taskRouter(T obj, FinanceBusinessRouterEnum financeBusinessRouterEnum, Class<R> r) {
		return null;
	}

	 private void insertBillProgressBean(String userid, String productid, String serviceid, Integer billtype, Integer progress, String content, Integer status, String remark, String terminalid){
		 	Date curDate = new Date();	
		 	BillProgressBean bean = new BillProgressBean();
			/** 主键ID*/
			bean.setId(HNUtil.getId());
			bean.setTerminalid(terminalid);
			/** 用户ID，引用自t_user_account表的id字段*/
			bean.setUserid(userid);
			/** 产品ID，引用自t_product_info表的id字段*/
			bean.setProductid(productid);
			/** 业务ID，账单类型为1对应t_bill_info表的id字段；账单类型为2对应t_bill_detail表的id字段*/
			bean.setServiceid(serviceid);
			/** 账单类型：1主账单，2分期账单*/
			bean.setBilltype(billtype);
			/** 进度类型：1手动还款，2批扣，3对账*/
			bean.setProgress(progress);
			/** 产生的进度变化文案*/
			bean.setContent(content);
			/** 展示类型：1产品展示，2非产品展示*/
			bean.setStatus(status);
			/** 备注*/
			bean.setRemark(remark);
			/** 创建时间*/
			bean.setCreatetime(curDate);
			/**更新时间*/
			bean.setUpdatetime(curDate);
			
			billProgressBeanService.insertBillProgressBean(bean);
		}
}
