package com.sinaif.king.shield.remote;

import com.sinaif.king.common.RemoteResult;
import com.sinaif.king.model.charge.vo.TianbeiDataVO;
import org.springframework.stereotype.Component;


/**
 * 分流熔断远程调用熔断处理类
 * @author : ZJL
 * @version : 1.0
 * Create Date : 2019年4月10日 下午6:50:26
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 *
 */
@Component
public class HystrixSplitterFuseRemoteServiceCallback implements SplitterFuseRemoteService {

	@Override
	public RemoteResult<TianbeiDataVO> fuse(String jsonStr) {
		return new RemoteResult<>(404, "服务未找到");
	}

}
