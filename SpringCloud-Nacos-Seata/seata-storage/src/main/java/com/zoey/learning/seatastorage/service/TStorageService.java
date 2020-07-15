package com.zoey.learning.seatastorage.service;


import com.zoey.learning.commons.result.ApiResult;

/**
 * T storage service
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
public interface TStorageService {

    /**
     * 减库存
     *
     * @param productId the product id
     * @param count     the count
     * @return the api result
     */
    ApiResult decrease(Long productId, Integer count);


    /**
     * 查询库存
     *
     * @param id the id
     * @return the storage
     */
    ApiResult getStorage(Long id);

}