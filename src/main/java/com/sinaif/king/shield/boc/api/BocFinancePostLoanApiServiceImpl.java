package com.sinaif.king.shield.boc.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sinaif.king.exception.FinanceException;
import com.sinaif.king.finance.api.IFinancePostLoanApiService;
import com.sinaif.king.model.finance.request.FinanceApiRequest;
import com.sinaif.king.model.finance.request.boc.AmountQueryReq;
import com.sinaif.king.model.finance.request.boc.AmountReturnApplyReq;
import com.sinaif.king.model.finance.request.boc.RepaymentPlanReq;
import com.sinaif.king.model.finance.request.boc.UserInoAllInfoReq;

/**
 * @author : Roger
 * @version : 1.0
 * @Description : 中银贷后接口实现类
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @Date : 2018/5/14 14:55
 */
@Service
@Scope("prototype")
public class BocFinancePostLoanApiServiceImpl extends BocBaseApiService implements IFinancePostLoanApiService {

    @Override
    public <E, T> T getLoanBill(FinanceApiRequest<E> apiRequest, Class<T> t) {
        UserInoAllInfoReq req = (UserInoAllInfoReq) apiRequest.getData();

        T obj = null;
        try {
            logger.info("【BOC】req===={}", JSONObject.toJSONString(req));
            obj = capReq.invoke(req, t);
//            logger.info("【BOC】result===={}", JSONObject.toJSONString(obj));
        } catch (FinanceException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return obj;
    }

    @Override
    public <E, T> T loanPlanQuery(FinanceApiRequest<E> apiRequest, Class<T> t) {
        RepaymentPlanReq req = (RepaymentPlanReq) apiRequest.getData();
        req.setCreLineCode(productParamsMap.get("productcode"));
        req.setMerchantCode(productParamsMap.get("merchantId"));

        T obj = null;
        try {
            logger.info("【BOC】req===={}", JSONObject.toJSONString(req));
            obj = capReq.invoke(req, t);
//            logger.info("【BOC】result===={}", JSONObject.toJSONString(obj));
        } catch (FinanceException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

        return obj;
    }

    @Override
    public <E, T> T repaymentApply(FinanceApiRequest<E> apiRequest, Class<T> t) {
        AmountReturnApplyReq req = (AmountReturnApplyReq) apiRequest.getData();
        req.getHeader().setTransType("40");

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
    public <E, T> T repaymentApplyResultQuery(FinanceApiRequest<E> apiRequest, Class<T> t) {
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

    @Override
    public <E, T> T repaymentHistoryQuery(FinanceApiRequest<E> apiRequest, Class<T> t) {
        return null;
    }

    @Override
    public <E, T> T billDownload(FinanceApiRequest<E> apiRequest, Class<T> t) {
        return null;
    }

    @Override
    public <E, T> T repaymentApplyConfirm(FinanceApiRequest<E> apiRequest, Class<T> t) {
        return null;
    }
}
