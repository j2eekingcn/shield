package com.sinaif.king.shield.remote;

import com.sinaif.king.common.RemoteResult;
import com.sinaif.king.model.charge.vo.TianbeiDataVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 分流模块服务远程调用接口类
 * @author : ZJL
 * @version : 1.0
 * Create Date : 2019年4月10日 下午6:51:02
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 *
 */
@FeignClient(name = "king-splitter-fuse-web", url = "${splitter_web.url}", fallback = HystrixFinanceFuseRemoteServiceCallback.class)
public interface SplitterFuseRemoteService {
 
	
	/**
	 * 熔断处理服务
	 */
	@RequestMapping(value = "/fuse")
	public RemoteResult<TianbeiDataVO> fuse(@RequestBody String jsonStr);

	 

}
