package com.zoey.learning.seatastorage.api;


import com.zoey.learning.commons.result.ApiResult;
import com.zoey.learning.seatastorage.service.TStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * T storage controller
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
@RestController
@Slf4j
public class TStorageController {

    //业务层
    @Resource
    private TStorageService tStorageService;


    /**
     * //远程减库存
     *
     * @param productId the product id
     * @param count     the count
     * @return the api result
     */
    @PostMapping(value = "/storage/decrease")
    public ApiResult decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count) {

        return tStorageService.decrease(productId, count);
    }

    /**
     * 主键查询库存
     *
     * @param id the id
     * @return the storage
     */
    @GetMapping("/storage/get/{id}")
    public ApiResult getStorage(@PathVariable("id") Long id) {
        return tStorageService.getStorage(id);
    }


}