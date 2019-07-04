package com.sinaif.king.shield.boc.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinaif.king.common.db.RedisCache;
import com.sinaif.king.finance.api.base.AbstractFinanceApiService;
import com.sinaif.king.model.finance.request.FinanceApiRequest;

/**
 * @author : Roger
 * @version : 1.0
 * @Description : 中银资金方接口实现类的父类
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @Date : 2018/5/14 14:32
 */
public class BocBaseApiService extends AbstractFinanceApiService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected BocRequestTask capReq;

    @Autowired
    protected RedisCache cache;

    protected Map<String, String> productParamsMap;

    public void init(Map<String, String> productParamsMap) {
        capReq.setProductParamsMap(productParamsMap);
        this.productParamsMap = productParamsMap;
    }

    @Override
    public <E, T> T access(FinanceApiRequest<E> apiRequest, Class<T> t) {
        return null;
    }

    public Map<String, String> getProductParamsMap() {
        return productParamsMap;
    }

    public void setProductParamsMap(Map<String, String> productParamsMap) {
        this.productParamsMap = productParamsMap;
    }
}
