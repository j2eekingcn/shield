package com.sinaif.king.shield.boc.service;

import org.springframework.stereotype.Service;

import com.sinaif.king.exception.FinanceException;
import com.sinaif.king.finance.service.IFinanceBillService;

/**
 * @author : Roger
 * @version : 1.0
 * @Description : 中银对账功能实现
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @Date : 2018/5/14 15:03
 */
@Service
public class BocBillServiceImpl implements IFinanceBillService {

    @Override
    public void billSync() throws FinanceException {

    }
}
