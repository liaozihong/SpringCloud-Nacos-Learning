package com.zoey.learning.seataorder.dao;


import com.zoey.learning.seataorder.entity.TOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TOrderDao {

    TOrder selectById(Long id);

    List<TOrder> selectAllByLimit(@Param("startIndex") int startIndex, @Param("limit") int limit);


    List<TOrder> selectAll(TOrder tOrder);


    List<TOrder> selectByMapParam(Map<String, Object> mapParam);


    int selectCountByMapParam(Map<String, Object> mapParam);


    int insert(TOrder tOrder);

    int update(TOrder tOrder);

    int deleteById(Long id);

}