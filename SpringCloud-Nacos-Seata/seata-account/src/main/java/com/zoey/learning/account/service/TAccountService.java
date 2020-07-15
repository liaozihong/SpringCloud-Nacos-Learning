package com.zoey.learning.account.service;


import com.zoey.learning.commons.result.ApiResult;

/**
 * T account service
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
public interface TAccountService {


    /**
     * 减余额
     *
     * @param userId the user id
     * @param money  the money
     * @return the api result
     */
    ApiResult decrease(Long userId, Double money);

    /**
     * 主键查询账户
     *
     * @param id the id
     * @return the account
     */
    ApiResult getAccount(Long id);
}