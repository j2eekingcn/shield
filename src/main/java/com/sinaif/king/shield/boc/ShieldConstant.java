package com.sinaif.king.shield.boc;

import java.util.HashMap;
import java.util.Map;

/**
 * 商户隔离
 * FileName    : ShieldConstant.java
 * @author : ZJL
 * @version : 1.0
 * Create Date : 2019年4月10日 上午11:08:25
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 *
 */
public class ShieldConstant {

	/**
	 * 事件配置
	 */
	public static class MessageConfig {

		/**
		 * ab测试，0关，1开发双数
		 */
		public static final Integer AB_TEST_0 = 0;
		public static final Integer AB_TEST_1 = 1;

		/**
		 * 运营系统消息区分
		 */
		public static Map<String, String> EVENT_SOURCE = new HashMap<String, String>() {
			{
//				// 运营事件
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY001.name(), "E-KY001");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY002.name(), "E-KY002");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY003.name(), "E-KY003");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY004.name(), "E-KY004");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY005.name(), "E-KY005");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY006.name(), "E-KY006");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY007.name(), "E-KY007");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY008.name(), "E-KY008");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY009.name(), "E-KY009");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY010.name(), "E-KY010");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY011.name(), "E-KY011");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY012.name(), "E-KY012");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY013.name(), "E-KY013");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY014.name(), "E-KY014");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY015.name(), "E-KY015");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY016.name(), "E-KY016");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY017.name(), "E-KY017");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY018.name(), "E-KY018");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY019.name(), "E-KY019");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY020.name(), "E-KY020");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY021.name(), "E-KY021");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY022.name(), "E-KY022");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY023.name(), "E-KY023");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY024.name(), "E-KY024");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY025.name(), "E-KY025");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KY026.name(), "E-KY026");
//
//				// 系统事件
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX001.name(), "E-KX001");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX002.name(), "E-KX002");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX003.name(), "E-KX003");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX004.name(), "E-KX004");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX005.name(), "E-KX005");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX006.name(), "E-KX006");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX007.name(), "E-KX007");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX008.name(), "E-KX008");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX010.name(), "E-KX010");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX0091.name(), "E-KX0091");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX0092.name(), "E-KX0092");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX011.name(), "E-KX011");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX012.name(), "E-KX012");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX013.name(), "E-KX013");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX014.name(), "E-KX014");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX015.name(), "E-KX015");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX016.name(), "E-KX016");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX017.name(), "E-KX017");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX018.name(), "E-KX018");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX019.name(), "E-KX019");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX020.name(), "E-KX020");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX021.name(), "E-KX021");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX022.name(), "E-KX022");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX023.name(), "E-KX023");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX024.name(), "E-KX024");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX025.name(), "E-KX025");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX026.name(), "E-KX026");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX027.name(), "E-KX027");
//				put(HMessageEnum.MessageBusinessType.USER_EVENT_KX028.name(), "E-KX028");
			}
		};
	}

	/**
	 * 消息物料表
	 */
	public static class MessageDetail {
		/**
		 * 消息类型：来自代码集
		 * 1  手机短信
		 * 2  手机推送
		 * 3  APP站内消息
		 * 4  自动电话
		 * 5  微信公众号消息
		 */
		public static final String MESSAGE_TYPE_1 = "1";
		public static final String MESSAGE_TYPE_2 = "2";
		public static final String MESSAGE_TYPE_3 = "3";
		public static final String MESSAGE_TYPE_4 = "4";
		public static final String MESSAGE_TYPE_5 = "5";
	}

	/**
	 * 消息待发送记录
	 */
	public static class MessageRecord {

		/**
		 * 当前发送状态：-1已过期/0待定/1待发送/2已发送/3发送失败/4阈值取消/5优先级取消/6互斥取消/7系统取消/8人工取消/9数据异常/10发送超时
		 */
		public static final Integer SEND_STATUS_NEGATIVE_1 = -1;
		public static final Integer SEND_STATUS_0 = 0;
		public static final Integer SEND_STATUS_1 = 1;
		public static final Integer SEND_STATUS_2 = 2;
		public static final Integer SEND_STATUS_3 = 3;
		public static final Integer SEND_STATUS_4 = 4;
		public static final Integer SEND_STATUS_5 = 5;
		public static final Integer SEND_STATUS_6 = 6;
		public static final Integer SEND_STATUS_7 = 7;
		public static final Integer SEND_STATUS_8 = 8;
		public static final Integer SEND_STATUS_9 = 9;
		public static final Integer SEND_STATUS_10 = 10;

		/**
		 * 类型：0运营消息，1系统消息
		 */
		public static final Integer TYPE_0 = 0;
		public static final Integer TYPE_1 = 1;

		/**
		 * 运营系统消息区分
		 */
		public static Map<String, String> SOURCE_MAP = new HashMap<String, String>() {
			{
				put(String.valueOf(TYPE_0), "opt-event");
				put(String.valueOf(TYPE_1), "sys-event");
			}
		};

	}

	/**
	 * 消息物料表
	 */
	public static class Event {
		public static final String USER_EVENT_KX014 = "USER_EVENT_KX014";
	}

	/**
	 * 字典配置
	 */
	public static class Dict {

		/**
		 * 运营运营常量
		 */
		public static final String TYPE_MESSAGE_EVENT_CONSTANT = "MESSAGE_EVENT_CONSTANT";

		/**
		 * 系统事件常量
		 */
		public static final String SYSTEM_EVENT_CONSTANT = "SYSTEM_EVENT_CONSTANT";

		/**
		 * 短信
		 * 手机推送
		 * APP站内消息
		 * 自动电话
		 * 微信公众号消息
		 * 最大发送次数
		 */
		public static final String KEY_EVENT_DAY_MAX = "EVENT_DAY_MAX_";

		/**
		 * PART1 ~ PART6
		 */
		public static final String KEY_EVENT_STEP_MAX = "EVENT_STEP_MAX_";

		/**
		 * 事件封装开关 0关,1开启
		 */
		public static final String EVENT_SWITCH = "EVENT_SWITCH";
		/**
		 * 运营事件封装开关 0关,1开启
		 */
		public static final String EVENT_OPT_SWITCH = "EVENT_OPT_SWITCH";
		/**
		 * 系统事件封装开关 0关,1开启
		 */
		public static final String EVENT_SYS_SWITCH = "EVENT_SYS_SWITCH";
		/**
		 * 系统事件
		 * 短信
		 * 手机推送
		 */
		public static final String SYSTEM_EVENT_DAY_MAX = "SYSTEM_EVENT_DAY_MAX_";
	}

	/**
	 * 发送时间
	 */
	public static class MessageTimes {

		/**
		 * 时间计算方向：0正向/1倒计时
		 */
		public static final Integer TIME_DIRECTION_0 = 0;
		public static final Integer TIME_DIRECTION_1 = 1;

		/**
		 * 触发日期：空或0是即时/1是第一天/2是第二天，以此类推
		 */
		public static final Integer TRIGGER_DAY_0 = 0;

		/**
		 * 触发时间：格式mm:ss，空是即时
		 */
		public static final String TRIGGER_TIME = "00:00";
	}

	/**
	 * 事件关系
	 */
	public static class MessageEventsRel {

		/**
		 * 类型：0优先级事件 1互斥事件
		 */
		public static final Integer TYPE_0 = 0;
		public static final Integer TYPE_1 = 1;

	}

	/**
	 * 表名称常量
	 */
	public static class Table {

		/**
		 * sinaif_test 表名称常量
		 */
		public static final String T_USER_ACCOUNT = "t_user_account";
		public static final String T_DATA_INFO_RETURN_MAIN = "t_data_info_return_main";

		/**
		 * sinaif_king
		 */
		public static final String T_APP_USER_STATUS = "t_app_user_status";
		public static final String T_CREDIT_INFO = "t_credit_info";
		public static final String T_WITHDRAW_APPLY = "t_withdraw_apply";
		public static final String T_CREDIT_APPLY = "t_credit_apply";
		public static final String T_BILL_DETAIL = "t_bill_detail";
		public static final String T_SPLITER_MATCH_RESULT = "t_spliter_match_result";
		public static final String T_LOAN_PROGRESS = "t_loan_progress";
		public static final String T_BILL_OVERDUE = "t_bill_overdue";
		public static final String T_SPLITER_MATCH_TAG = "t_spliter_match_tag";
	}

	/**
	 * 库表字段数据
	 */
	public static final String DATABASE = "database"; //库
	public static final String TABLE = "table"; //表
	public static final String TYPE = "type"; //类型，insert，update
	public static final String DATA = "data"; //数据
	public static final String DBDATA = "dbData"; //数据

	/**
	 * 类型
	 */
	public static final String UPDATE = "update"; //更新
	public static final String INSERT = "insert"; //删除

	/**
	 * 字段名常量
	 */
	public static final String STATUS = "status";
	public static final String TERMINALID = "terminalid";
	public static final String FINANCEID = "financeid";
	public static final String USERID = "userid";
	public static final String ID = "id";
	public static final String PRODUCTID = "productid";
	public static final String BILLID = "billid";
	public static final String ORDERID = "orderid";
	public static final String APPLYSTATUS = "applystatus";
	public static final String OLD = "old";
	public static final String STAGECODE = "stagecode";
	public static final String LEVEL = "level";
	public static final String LOGINSTATUS = "loginstatus";
	public static final String ACCOUNTID = "accountid";
	public static final String USERNAME = "username";
	public static final String OPERATIONTYPE = "operationtype";
	public static final String CLIENTID = "clientid";
	public static final String ENABLED = "enabled";
	public static final String BILLSTATUS = "billstatus";
	public static final String FINISHAPPLY = "finishApply";
	public static final String RESULTID = "resultid";
	public static final String UPDATETIME = "updatetime"; // 更新时间
	public static final String CREATETIME = "createtime"; // 创建时间
	public static final String ISSYNC = "issync"; // 是否同步：0非同步1同步

	/**
	 * code
	 */
	public static final String CODE100000010007 = "100000010007";
	public static final String CODE100000010002 = "100000010002";
	public static final String CODE100000010003 = "100000010003";
	public static final String CODE100000010004 = "100000010004";
	public static final String CODE100000010005 = "100000010005";
	public static final String CODE100000010006 = "100000010006";
	public static final String CODE100000010011 = "100000010011";
	public static final String CODE1000101 = "1000101";
	public static final String CODE1000102 = "1000102";

	/**
	 * kafka的topic
	 */
	public static final String OPERATE = "operate";
	public static final String SYSTEM = "system";

	/**
	 * 通配符
	 */
	public static class KeyWord {

		/**
		 * 客户姓名-王小明
		 */
		public static final String NICKNAME = "nickname";
		/**
		 * 申请时间-12月28日
		 */
		public static final String APPLYTIME = "applytime";
		/**
		 * #prodname#	产品名称	你我贷大王贷
		 */
		public static final String PRODNAME = "prodname";
		/**
		 * #period #	期数	5
		 */
		public static final String PERIOD = "period";
		/**
		 * #unpayamount#	未还金额	2000.05
		 */
		public static final String UNPAYAMOUNT = "unpayamount";
		/**
		 * #repaymentamount#	还款金额	2000.05
		 */
		public static final String REPAYMENTAMOUNT = "repaymentamount";
		/**
		 * #repaytime#	还款日期	12月28日
		 */
		public static final String REPAYTIME = "repaytime";
		/**
		 * #withdrawamount#	提现金额	20000
		 */
		public static final String WITHDRAWAMOUNT = "withdrawamount";
		/**
		 * #withdrawtime#	提现时间	12月28日
		 */
		public static final String WITHDRAWTIME = "withdrawtime";
		/**
		 * #creditline#	授信额度	10000
		 */
		public static final String CREDITLINE = "creditline";
		/**
		 * #maxcredit#	最高授信额度	20000
		 */
		public static final String MAXCREDIT = "maxcredit";
		/**
		 * #bankno#	客户银行卡号	4232只显示后四位
		 */
		public static final String BANKNO = "bankno";
		/**
		 * #bankname#	客户所属银行	工商银行	显示银行简称
		 */
		public static final String BANKNAME = "bankname";
		/**
		 * #pubinformation#	返单具体资料项	身份信息/活体影像信息/联系人信息/基本信息	展示需要修改项，以/作为分隔符
		 */
		public static final String PUBINFORMATION = "pubinformation";
		/**
		 * #allhighestcredit#	商户最高额度
		 */
		public static final String allhighestcredit = "allhighestcredit";
	}

	/**
	 * 缓存key
	 */
	public static class CacheKey {

		/**
		 * 运营事件，幂等性
		 */
		public static final String EVENT_CACHE_IDEMPOTENCY_OPT = "EVENT_CACHE_IDEMPOTENCY_OPT";
		/**
		 * 系统事件，幂等性
		 */
		public static final String EVENT_CACHE_IDEMPOTENCY_SYS = "EVENT_CACHE_IDEMPOTENCY_SYS";
		/**
		 * 封装事件，幂等性
		 */
		public static final String EVENT_CACHE_IDEMPOTENCY_MAXWELL = "EVENT_CACHE_IDEMPOTENCY_MAXWELL";

		/**
		 * 二次验证，幂等性
		 */
		public static final String EVENT_CACHE_IDEMPOTENCY_SECOND = "EVENT_CACHE_IDEMPOTENCY_AFTERLOAN";

		/**
		 * SYSTEM_EVENT_KY025，幂等性
		 */
		public static final String SYSTEM_EVENT_KY025 = "SYSTEM_EVENT_KY025";

		/**
		 * 运营事件，幂等性时效期
		 */
		public static final int EVENT_CACHE_IDEMPOTENCY_OPT_TIME = 60 * 1;
		/**
		 * 系统事件，幂等性时效期
		 */
		public static final int EVENT_CACHE_IDEMPOTENCY_SYS_TIME = 60 * 1;
		/**
		 * 封装事件，幂等性时效期
		 */
		public static final int EVENT_CACHE_IDEMPOTENCY_MAXWELL_TIME = 60 * 1;
		/**
		 * 二次验证，幂等性时效期
		 */
		public static final int EVENT_CACHE_IDEMPOTENCY_SECOND_TIME = 60 * 1;

	}

	/**
	 * push 状态
	 */
	public static class PushStatus {

		/**
		 * 首页刷新
		 */
		public static final String PUSH_STATUS_REFRESHHOME = "REFRESHHOME";

		/**
		 * 首页刷新
		 */
		public static final String PUSH_STATUS_PUSH_MASS = "PUSH_MASS";

		/**
		 * 类型：0贷前，1贷中，2贷后，3系统，4我的
		 */
		public static final int TYPE_1 = 1;
		/**
		 * 类型：0贷前，1贷中，2贷后，3系统，4我的
		 */
		public static final int TYPE_2 = 2;
		/**
		 * 类型：0贷前，1贷中，2贷后，3系统，4我的
		 */
		public static final int TYPE_3 = 3;
		/**
		 * 类型：0贷前，1贷中，2贷后，3系统，4我的
		 */
		public static final int TYPE_4 = 4;
		/**
		 * 类型：0贷前，1贷中，2贷后，3系统，4我的
		 */
		public static final int TYPE_5 = 5;

	}

}
