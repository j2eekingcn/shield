/**
 */
package com.sinaif.king.shield.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : ZJL
 * @version : 1.0
 * Create Date : 2019年4月10日 下午7:20:57
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 **/
public class ShieldLog {

	public static Logger logger = LoggerFactory.getLogger(ShieldLog.class);

	public static boolean isInfoEnabled = logger.isInfoEnabled();

}
