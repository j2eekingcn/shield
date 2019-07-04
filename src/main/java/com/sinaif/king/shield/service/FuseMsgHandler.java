package com.sinaif.king.shield.service;

import org.json.JSONObject;

import com.sinaif.king.common.ApiResult;

/**
 * 
 * FileName    : FuseHandler.java
 * Description :king-shield-service com.sinaif.king.shield.service.FuseHandler.java
 * @author : ZJL
 * @version : 1.0
 * Create Date : 2019年4月10日 下午7:19:54
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 *
 */
@FunctionalInterface
public interface FuseMsgHandler {

    ApiResult handler(JSONObject message);

}
