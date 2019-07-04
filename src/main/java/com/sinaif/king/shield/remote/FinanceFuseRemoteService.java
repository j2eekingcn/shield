package com.sinaif.king.shield.remote;

import com.sinaif.king.common.RemoteResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * finance fuse熔断模块远程接口
 * @author : ZJL
 * @version : 1.0
 * Create Date : 2019年4月10日 下午7:02:25
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 *
 */
@FeignClient(name = "king-finance-fuse-web", url = "${finance_web.url}", fallback = HystrixFinanceFuseRemoteServiceCallback.class)
public interface FinanceFuseRemoteService {

	/**
	 * 提交天贝服务码
	 * @param jsonStr 对应ChargeReqVO，
	 *     terminalid，userid必传
	 *     mobile，idNo必传
	 *     param为天贝必传参数name、phone、idNo、servicePwd
	 * @return
	 */
	@RequestMapping(value = "/fuse", method = RequestMethod.POST)
	public RemoteResult<String> fuse(@RequestBody String jsonStr);

}
