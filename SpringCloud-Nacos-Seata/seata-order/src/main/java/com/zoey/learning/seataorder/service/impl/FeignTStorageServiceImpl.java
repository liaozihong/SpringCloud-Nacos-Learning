package com.zoey.learning.seataorder.service.impl;


import com.zoey.learning.commons.result.ApiResult;
import com.zoey.learning.seataorder.service.FeignTStorageService;
import org.springframework.stereotype.Component;

/**
 * Feign t storage service
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
@Component  //服务降级
public class FeignTStorageServiceImpl implements FeignTStorageService {

    @Override
    public ApiResult decrease(Long productId, Integer count) {
        return new ApiResult(543, "--------->减库存服务降级...");
    }

    @Override
    public ApiResult getStorage(Long id) {
        return new ApiResult(543, "--------->查询库存服务降级...");
    }
}
