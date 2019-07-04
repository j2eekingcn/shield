package com.sinaif.king.shield.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public abstract class AbstractFuseMsgHandler implements FuseMsgHandler {

	/**
	 * 判断同步数据是否需要操作
	 */
	protected boolean issyncflag(Integer issync, String createtime) {
		//数据互通的数据不操作
		if (issync == 1) {
			return true;
		}
		//创建时间为空不同步
		if (StringUtils.isBlank(createtime)) {
			return true;
		}
		//同步开始时间，小于该时间不同步
		Date issynctime = DateUtils.string2Date("");
		Date createtimeDate = DateUtils.string2Date(createtime);
		if (DateUtils.compareDate(issynctime, createtimeDate) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 判断创建时间
	 */
	protected boolean createtimeflag(String createtime) {
		//创建时间为空不同步
		if (StringUtils.isBlank(createtime)) {
			return true;
		}
		//同步开始时间，小于该时间不同步
		Date issynctime = DateUtils.string2Date("");
		Date createtimeDate = DateUtils.string2Date(createtime);
		if (DateUtils.compareDate(issynctime, createtimeDate) == 1) {
			return true;
		}
		return false;
	}

}
