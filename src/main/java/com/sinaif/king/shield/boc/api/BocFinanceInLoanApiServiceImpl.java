package com.sinaif.king.shield.boc.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sinaif.king.exception.FinanceException;
import com.sinaif.king.finance.api.IFinanceInLoanApiService;
import com.sinaif.king.model.finance.api.boc.BocRequestBody;
import com.sinaif.king.model.finance.request.FinanceApiRequest;
import com.sinaif.king.model.finance.request.boc.AmountQueryReq;

/**
 * @author : Roger
 * @version : 1.0
 * @Description : 中银贷中接口实现类
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @Date : 2018/5/14 14:55
 */
@Service
@Scope("prototype")
public class BocFinanceInLoanApiServiceImpl extends BocBaseApiService implements IFinanceInLoanApiService {

    @Override
    public <E, T> T withdrawApply(FinanceApiRequest<E> apiRequest, Class<T> t) {
        BocRequestBody req = (BocRequestBody) apiRequest.getData();
        req.getHeader().setTransType("20");

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
    public <E, T> T withdrawApplyConfirm(FinanceApiRequest<E> apiRequest, Class<T> t) {
    	return null;
    }

    @Override
    public <E, T> T withdrawApplyResultQuery(FinanceApiRequest<E> apiRequest, Class<T> t) {
        AmountQueryReq req = (AmountQueryReq) apiRequest.getData();

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
    
}
