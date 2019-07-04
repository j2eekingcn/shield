package com.sinaif.king.shield.boc.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sinaif.king.common.ErrorCode;
import com.sinaif.king.common.io.RemoteFile;
import com.sinaif.king.common.utils.HttpClientUtils;
import com.sinaif.king.exception.FinanceException;
import com.sinaif.king.finance.api.IFinancePreLoanApiService;
import com.sinaif.king.model.finance.request.FinanceApiRequest;
import com.sinaif.king.model.finance.request.boc.FileUploadReq;
import com.sinaif.king.model.finance.request.boc.LoanApplyProgressReq;
import com.sinaif.king.model.finance.request.boc.LoanApplyReq;
import com.sinaif.king.model.finance.request.boc.UserQualificationReq;

/**
 * @author : Roger
 * @version : 1.0
 * @Description : 中银贷前接口实现类
 * @Copyright : Sinaif Software Co.,Ltd. All Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 * @Date : 2018/5/14 14:55
 */
@Service
@Scope("prototype")
public class BocFinancePreLoanApiServiceImpl extends BocBaseApiService implements IFinancePreLoanApiService {

    @Override
    public <E, T> T loanValidation(FinanceApiRequest<E> apiRequest, Class<T> t) {
        UserQualificationReq req = (UserQualificationReq) apiRequest.getData();

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
    public <E, T> T uploadLoanFile(FinanceApiRequest<E> apiRequest, Class<T> t) {
        FileUploadReq req = (FileUploadReq) apiRequest.getData();

        T obj = null;
        try{
            RemoteFile file = new RemoteFile(req.getFileUrl());
            long time = System.currentTimeMillis();
            logger.info(String.format("用户id[%s]上传文件到中银服务器开始了[%s]，文件地址[%s]", req.getCustomerId(),time,req.getFileUrl()));
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("merchantId", productParamsMap.get("merchantId"));
            params.put("customerId",req.getCustomerId());
            params.put("fileType", req.getFileType());
            params.put("file", file.getFile());
            params.put("fileName", file.getName());
            String content = HttpClientUtils.sendPostFiles(productParamsMap.get("serverUploadUrl"), params);
            logger.info(String.format("用户id[%s]上传文件到中银服务器使用时间(毫秒)：[%s]，文件地址[%s]", req.getCustomerId(),(System.currentTimeMillis() - time),req.getFileUrl()));
            obj = (T) JSONObject.parseObject(content, t);
            file.free();
        }catch(FinanceException e){
            logger.error("error", e);
            throw e;
        }catch(RuntimeException e){
            logger.error("运行时异常:"+e.getMessage(), e);
            if(String.valueOf(ErrorCode.UPLOAD_FILE_NOT_FOUND).equals(e.getMessage())){
                throw new FinanceException(String.valueOf(ErrorCode.UPLOAD_FILE_NOT_FOUND), "上传文件异常");
            }
            throw new FinanceException("500", "上传文件异常");
        }catch(Exception e){
            logger.error("error", e);
            throw new FinanceException("500", "上传文件异常");
        }

        return obj;
    }

    @Override
    public <E, T> T registerLoanInfo(FinanceApiRequest<E> apiRequest, Class<T> t) {
        return null;
    }

    @Override
    public <E, T> T loanApply(FinanceApiRequest<E> apiRequest, Class<T> t) {
        LoanApplyReq req = (LoanApplyReq) apiRequest.getData();

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
    public <E, T> T queryApplyProgress(FinanceApiRequest<E> apiRequest, Class<T> t) {
        LoanApplyProgressReq req = (LoanApplyProgressReq) apiRequest.getData();

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
    public <E, T> T patchApply(FinanceApiRequest<E> apiRequest, Class<T> t) {
        return null;
    }
}
