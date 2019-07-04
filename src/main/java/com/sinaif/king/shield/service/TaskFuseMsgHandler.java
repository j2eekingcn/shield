package com.sinaif.king.shield.service;

import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinaif.king.common.ApiResult;
import com.sinaif.king.common.utils.DateUtils;
import com.sinaif.king.common.utils.StringUtils;
import com.sinaif.king.shield.util.ShieldLog;

/**
 * FileName    : com.sinaif.service.handler.java
 * Description :
 *
 * @author : Leo
 * @version : 1.0
 * Create Date : 2018/7/17 11:02
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 **/
public abstract class TaskFuseMsgHandler extends AbstractFuseMsgHandler {

	@Override
	public ApiResult handler(JSONObject message) {
		return null;
	}

}
