package com.zoey.learning.seataorder.controller;


import com.zoey.learning.commons.result.ApiResult;
import com.zoey.learning.seataorder.entity.TOrder;
import com.zoey.learning.seataorder.service.FeignTAccountService;
import com.zoey.learning.seataorder.service.FeignTStorageService;
import com.zoey.learning.seataorder.service.TOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class TOrderController {
    @Resource
    private TOrderService tOrderService;

    @Resource
    private FeignTStorageService feignTStorageService;

    @Resource
    private FeignTAccountService feignTAccountService;

    //下单
    @GetMapping("/order/create")
    public ApiResult create(TOrder tOrder) {
        return this.tOrderService.insert(tOrder);
    }


    //远程主键查询库存
    @GetMapping("/order/storage/get/{id}")
    public ApiResult getStorage(@PathVariable("id") Long id) {
        return feignTStorageService.getStorage(id);
    }

    //主键查询账户
    @GetMapping("/order/account/get/{id}")
    public ApiResult getAccount(@PathVariable("id") Long id) {
        return feignTAccountService.getAccount(id);
    }


}