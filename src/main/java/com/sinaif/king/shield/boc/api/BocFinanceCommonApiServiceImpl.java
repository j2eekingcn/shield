package com.sinaif.king.shield.boc.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sinaif.king.exception.FinanceException;
import com.sinaif.king.finance.api.IFinanceCommonApiService;
import com.sinaif.king.model.finance.api.boc.BocRequestBody;
import com.sinaif.king.model.finance.enums.BocTransCode;
import com.sinaif.king.model.finance.request.FinanceApiRequest;
import com.sinaif.king.model.finance.request.boc.PhoneCodeReq;
import com.sinaif.king.model.finance.request.boc.PhoneResetReq;

/**
 * @author : Roger
 * @version : 1.0
 * @Description : 中银公共接口实现类
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @Date : 2018/5/14 14:55
 */
@Service
@Scope("prototype")
public class BocFinanceCommonApiServiceImpl extends BocBaseApiService implements IFinanceCommonApiService {

    @Override
    public <E, T> T modifyUserPhone(FinanceApiRequest<E> apiRequest, Class<T> t) {
        PhoneResetReq req = (PhoneResetReq) apiRequest.getData();
        T obj = null;
        try {
            logger.info("【BOC】req===={}", JSONObject.toJSONString(req));
            obj = capReq.invoke(req, t);
            logger.info("【BOC】result===={}", JSONObject.toJSONString(obj));
        } catch (FinanceException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return obj;
    }

    @Override
    public <E, T> T bindBankCardMsgSend(FinanceApiRequest<E> apiRequest, Class<T> t) {
    	BocRequestBody req = (BocRequestBody) apiRequest.getData();
        T obj = null;
        try {
            logger.info("【BOC】req===={}", JSONObject.toJSONString(req));
            obj = capReq.invoke(req, t);
            logger.info("【BOC】result===={}", JSONObject.toJSONString(obj));
        } catch (FinanceException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return obj;
    }
    
    @Override
    public <E, T> T bindBankCard(FinanceApiRequest<E> apiRequest, Class<T> t) {
        BocRequestBody req = (BocRequestBody) apiRequest.getData();
        T obj = null;
        try {
            logger.info("【BOC】req===={}", JSONObject.toJSONString(req));
            obj = capReq.invoke(req, t);
            logger.info("【BOC】result===={}", JSONObject.toJSONString(obj));
        } catch (FinanceException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return obj;
    }

    @Override
    public <E, T> T sendSms(FinanceApiRequest<E> apiRequest, Class<T> t) {
    	PhoneCodeReq req = (PhoneCodeReq)apiRequest.getData();
    	req.setTransCode(BocTransCode.TRANSCODE_1011);
    	T obj = capReq.invoke(req, t);
        return obj;
    }

    @Override
    public <E, T, M> T commonRequest(FinanceApiRequest<E> apiRequest, Class<T> t, M transCode) {
        BocTransCode bocTransCode = (BocTransCode) transCode;
        BocRequestBody req = (BocRequestBody) apiRequest.getData();

        T obj = null;
        try {
            logger.info("【BOC】req===={}", JSONObject.toJSONString(req));

            if (BocTransCode.TRANSCODE_1015.equals(bocTransCode) // 提交人脸比对
                    || BocTransCode.TRANSCODE_6003.equals(bocTransCode)  // 获取题库
                    || BocTransCode.TRANSCODE_6004.equals(bocTransCode) // 提交问卷
                    || BocTransCode.TRANSCODE_6005.equals(bocTransCode) // 申请转人工电核
                    || BocTransCode.TRANSCODE_1014.equals(bocTransCode) // 卡bin查询
                    || BocTransCode.TRANSCODE_1011.equals(bocTransCode) // 短信动态密码获取
                    || BocTransCode.TRANSCODE_1012.equals(bocTransCode) // 获取原手机号动态密码
                    || BocTransCode.TRANSCODE_5012.equals(bocTransCode) // 验证原手机动态密码
                    || BocTransCode.TRANSCODE_1013.equals(bocTransCode) // 获取新手机号动态密码
                    || BocTransCode.TRANSCODE_9006.equals(bocTransCode) // 商户主动拒绝
                    || BocTransCode.TRANSCODE_1025.equals(bocTransCode) // 重复进件前置查询
                    || BocTransCode.TRANSCODE_1019.equals(bocTransCode) // 二次动用身份证有效期修改
                    || BocTransCode.TRANSCODE_1130.equals(bocTransCode) // 银行卡支付签约短信
                    || BocTransCode.TRANSCODE_1131.equals(bocTransCode) // 银行卡支付签约
                    ) { 
                obj = capReq.invoke(req, t);
            }
            logger.info("【BOC】result===={}", JSONObject.toJSONString(obj));
        } catch (FinanceException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return obj;
    }

}
