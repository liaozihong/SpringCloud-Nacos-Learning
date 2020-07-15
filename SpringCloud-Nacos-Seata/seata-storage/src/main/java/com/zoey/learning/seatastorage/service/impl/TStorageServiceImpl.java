package com.zoey.learning.seatastorage.service.impl;


import com.zoey.learning.commons.result.ApiResult;
import com.zoey.learning.seatastorage.dao.TStorageDao;
import com.zoey.learning.seatastorage.entity.TStorage;
import com.zoey.learning.seatastorage.service.TStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * T storage service
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
@Service("tStorageService")
@Slf4j
public class TStorageServiceImpl implements TStorageService {
    @Resource
    private TStorageDao tStorageDao;

    @Override
    public ApiResult decrease(Long productId, Integer count) {
        //由于dao是自动生成的 没有根据productId 查询商品的方法 且productId 又不是主键。
        //所有使用我下面方式查询
        Map<String, Object> param = new HashMap<>();
        param.put("productId", productId);

        TStorage tStorage = tStorageDao.selectByMapParam(param).get(0);

        if (tStorage == null) {
            return new ApiResult(400, "该商品库存不存在!");
        }
        //修改
        tStorage.setTotal(tStorage.getTotal() - count); //减总数
        tStorage.setUsed(tStorage.getUsed() + count); //加使用库存
        tStorage.setResidue(tStorage.getResidue() - count); //余量

        int count1 = tStorageDao.update(tStorage);
        if (count1 > 0) {
            return new ApiResult(200, "减库存操作成功！", tStorage);
        }
        return new ApiResult(400, "减库存操作失败！");
    }


    @Override
    public ApiResult getStorage(Long id) {

        TStorage tStorage = tStorageDao.selectById(id);
        if (tStorage != null) {
            return new ApiResult(200, "查询库存success!", tStorage);
        }
        return new ApiResult(400, "该商品库存不存在!");
    }
}