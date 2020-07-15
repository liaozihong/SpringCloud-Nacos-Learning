package com.zoey.learning.account.service.impl;

import com.zoey.learning.account.dao.TAccountDao;
import com.zoey.learning.account.entity.TAccount;
import com.zoey.learning.account.service.TAccountService;
import com.zoey.learning.commons.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Service("tAccountService")
@Slf4j
public class TAccountServiceImpl implements TAccountService {
    @Resource
    private TAccountDao tAccountDao;


    @Override
    public ApiResult decrease(Long userId, Double money) {

//        int a=10/0;
//        try {
//
//            Thread.sleep(200000);  //睡眠20秒
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //由于dao是自动生成的 没有根据userId 查询账户的的方法 且userId又不是主键。
        //所有使用我下面方式查询
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);

        TAccount tAccount = tAccountDao.selectByMapParam(param).get(0);

        if (tAccount == null) {
            return new ApiResult(400, "该用户不存在!");
        }
        //修改
        tAccount.setTotal(tAccount.getTotal() - money); //总金额
        tAccount.setUsed(tAccount.getUsed() + money); // 已经消费金额
        tAccount.setResidue(tAccount.getResidue() - money); //剩余金额

        int count1 = tAccountDao.update(tAccount);
        if (count1 > 0) {
            return new ApiResult(200, "扣款成功！", tAccount);
        }
        return new ApiResult(400, "扣款操作失败！");
    }


    //主键查询账户
    @Override
    public ApiResult getAccount(Long id) {
        TAccount tAccount = tAccountDao.selectById(id);
        if (tAccount != null) {
            return new ApiResult(200, "账户查询成功！", tAccount);
        }
        return new ApiResult(400, "账户查询失败！");
    }
}