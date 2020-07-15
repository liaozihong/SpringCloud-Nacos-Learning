package com.zoey.learning.seataorder.service;


import com.zoey.learning.commons.result.ApiResult;
import com.zoey.learning.seataorder.entity.TOrder;

/**
 * T order service
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
public interface TOrderService {


    /**
     * Insert api result.
     *
     * @param tOrder the t order
     * @return the api result
     */
    ApiResult insert(TOrder tOrder);


}