package com.zoey.learning.account.controller;

import com.zoey.learning.account.service.TAccountService;
import com.zoey.learning.commons.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * T account controller
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
@RestController
@Slf4j
public class TAccountController {
    /**
     * 服务对象
     */
    @Resource
    private TAccountService tAccountService;


    /**
     * 远程减库存
     *
     * @param userId the user id
     * @param money  the money
     * @return the api result
     */
    @PostMapping(value = "/account/decrease")
    public ApiResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") Double money) {
        return tAccountService.decrease(userId, money);
    }


    /**
     * 主键查询账户
     *
     * @param id the id
     * @return the account
     */
    @GetMapping("/account/get/{id}")
    public ApiResult getAccount(@PathVariable("id") Long id) {
        return tAccountService.getAccount(id);
    }


}