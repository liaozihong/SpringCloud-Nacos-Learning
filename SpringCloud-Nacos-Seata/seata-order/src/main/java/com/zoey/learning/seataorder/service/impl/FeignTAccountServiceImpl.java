package com.zoey.learning.seataorder.service.impl;

import com.zoey.learning.commons.result.ApiResult;
import com.zoey.learning.seataorder.service.FeignTAccountService;
import org.springframework.stereotype.Component;

/**
 * Feign t account service
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
@Component
public class FeignTAccountServiceImpl implements FeignTAccountService {

    @Override
    public ApiResult decrease(Long userId, Double money) {
        return new ApiResult(543, "------>扣款服务降级");
    }

    @Override
    public ApiResult getAccount(Long id) {
        return new ApiResult(543, "------>查询账户服务降级");
    }


}
