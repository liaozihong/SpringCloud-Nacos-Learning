package com.zoey.learning.seataorder.service;


import com.zoey.learning.commons.result.ApiResult;
import com.zoey.learning.seataorder.service.impl.FeignTStorageServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "seata-storage-service", fallback = FeignTStorageServiceImpl.class)
public interface FeignTStorageService {


    //远程库存
    @PostMapping(value = "/storage/decrease")
    public ApiResult decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);


    //远程主键查询库存
    @GetMapping("/storage/get/{id}")
    public ApiResult getStorage(@PathVariable("id") Long id);

}
