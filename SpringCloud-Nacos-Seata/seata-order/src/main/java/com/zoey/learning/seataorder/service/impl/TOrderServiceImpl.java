package com.zoey.learning.seataorder.service.impl;

import com.zoey.learning.commons.result.ApiResult;
import com.zoey.learning.seataorder.dao.TOrderDao;
import com.zoey.learning.seataorder.entity.TOrder;
import com.zoey.learning.seataorder.service.FeignTAccountService;
import com.zoey.learning.seataorder.service.FeignTStorageService;
import com.zoey.learning.seataorder.service.TOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * T order service
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
@Service("tOrderService")
@Slf4j
public class TOrderServiceImpl implements TOrderService {
    @Resource
    private TOrderDao tOrderDao;


    /**
     * feign 库存远程调用接口
     */
    @Resource
    private FeignTStorageService feignTStorageService;

    /**
     * feign 账户远程调用接口
     */
    @Resource
    private FeignTAccountService feignTAccountService;


    @Override
    @GlobalTransactional(name = "zoey-create-order", rollbackFor = {Throwable.class, Exception.class})
    public ApiResult insert(TOrder tOrder) {

        log.info("--------【start】--->创建新订单！");
        tOrder.setStatus(0); //状态改为新创建 //0 创建 1 完成
        int count = this.tOrderDao.insert(tOrder);
        log.info("--------【end】--->创建新订单！");


        log.info("--------【start】--->远程调用【减库存】！");
        ApiResult decreaseStorage = feignTStorageService.decrease(tOrder.getProductId(), tOrder.getCount());
        log.info("--------【end】---->远程调用【减库存】！");

        log.info("--------【start】--->远程调用【扣余额】！");
        ApiResult decreaseAccount = feignTAccountService.decrease(tOrder.getUserId(), tOrder.getMoney());
        log.info("--------【end】---->远程调用【扣余额】！");

        //扣款成功后修改状态
        if (decreaseAccount.getCode() == 200) {
            log.info("--------【start】--->修改订单状态！");
            TOrder tOrder1 = new TOrder();
            tOrder1.setId(tOrder.getId());
            tOrder1.setStatus(1); //状态改为完成  //0 创建 1 完成
            int count1 = this.tOrderDao.update(tOrder);
            log.info("--------【end】--->修改订单状态！");
            tOrder.setStatus(tOrder1.getStatus());
        }
        if (count > 0 && decreaseStorage.getCode() == 200 && decreaseAccount.getCode() == 200) {
            return new ApiResult(200, "下单成功！", tOrder);
        }
        return new ApiResult(500, "下单失败！");
    }

}