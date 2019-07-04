package com.sinaif.king.shield.boc.api;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinaif.king.enums.finance.FinanceParamConfEnum;
import com.sinaif.king.enums.finance.product.ProductEnum;
import com.sinaif.king.finance.api.IFinanceCommonApiService;
import com.sinaif.king.finance.api.IFinanceInLoanApiService;
import com.sinaif.king.finance.api.IFinancePostLoanApiService;
import com.sinaif.king.finance.api.IFinancePreLoanApiService;
import com.sinaif.king.finance.api.IFinanceProcessor;
import com.sinaif.king.finance.service.IFinanceBillService;
import com.sinaif.king.finance.service.IFinanceBusinessService;
import com.sinaif.king.service.product.finance.FinanceParamConfService;
import com.sinaif.king.shield.boc.service.BocBillServiceImpl;
import com.sinaif.king.shield.boc.service.BocBusinessServiceImpl;

/**
 * @author : Roger
 * @version : 1.0
 * @Description :
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @Date : 2018/5/14 14:59
 */
public class BocFinanceProcessor implements IFinanceProcessor {

    @Autowired
    private BocFinancePreLoanApiServiceImpl preLoanApiService;

    @Autowired
    private BocFinanceInLoanApiServiceImpl inLoanApiService;

    @Autowired
    private BocFinancePostLoanApiServiceImpl postLoanApiService;

    @Autowired
    private BocFinanceCommonApiServiceImpl commonApiService;

    @Autowired
    private BocBusinessServiceImpl service;

    @Autowired
    private BocBillServiceImpl billService;

    @Autowired
    private FinanceParamConfService financeParamConfService;

    private ProductEnum productEnum;

    public BocFinanceProcessor(ProductEnum productEnum) {
        this.setProductType(productEnum);
    }

    /**
     * 根据产品ID初始化资方公共参数
     */
    @PostConstruct
    private void initProductParams() {
    	Map<String, String> productParamsMap = financeParamConfService.initApiProductParamsMap(FinanceParamConfEnum.PRODUCT_INFO.getCode(), productEnum.id, null);
//        productParamsMap.put("certsPath", "G:/usr/local/keys/boc/loan_test/");
//        productParamsMap.put("certsPath", "D:/boc_templete/key/");
//        productParamsMap.put("serverUrl", "http://chenweixian-pc:8092/service.xml");

        preLoanApiService.init(productParamsMap);
        inLoanApiService.init(productParamsMap);
        postLoanApiService.init(productParamsMap);
        commonApiService.init(productParamsMap);
    }

    @Override
    public IFinancePreLoanApiService getPreLoanApiService() {
        return preLoanApiService;
    }

    @Override
    public IFinanceInLoanApiService getInLoanApiService() {
        return inLoanApiService;
    }

    @Override
    public IFinancePostLoanApiService getPostLoanApiService() {
        return postLoanApiService;
    }

    @Override
    public IFinanceCommonApiService getCommonApiService() {
        return commonApiService;
    }

    @Override
    public IFinanceBusinessService getService() {
        return service;
    }

    @Override
    public IFinanceBillService getBillService() {
        return billService;
    }

    @Override
    public ProductEnum getProductType() {
        return productEnum;
    }

    @Override
    public void setProductType(ProductEnum productEnum) {
        this.productEnum = productEnum;
    }
}
