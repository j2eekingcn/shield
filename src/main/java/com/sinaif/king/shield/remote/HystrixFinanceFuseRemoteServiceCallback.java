package com.sinaif.king.shield.remote;

import com.sinaif.king.common.RemoteResult;
import org.springframework.stereotype.Component;

/**
 * finance远程调用熔断处理类
 * @author : ZJL
 * @version : 1.0
 * Create Date : 2019年4月10日 下午7:04:13
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 *
 */
@Component
public class HystrixFinanceFuseRemoteServiceCallback implements FinanceFuseRemoteService {

	@Override
	public RemoteResult<String> fuse(String jsonStr) {
		return new RemoteResult<>(404, "服务未找到");
	}

}
