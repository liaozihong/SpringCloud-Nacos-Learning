package com.zoey.learning.seatastorage.dao;

import com.zoey.learning.seatastorage.entity.TStorage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * T storage dao
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
public interface TStorageDao {

    /**
     * Select by id t storage.
     *
     * @param id the id
     * @return the t storage
     */
    TStorage selectById(Long id);

    /**
     * Select all by limit list.
     *
     * @param startIndex the start index
     * @param limit      the limit
     * @return the list
     */
    List<TStorage> selectAllByLimit(@Param("startIndex") int startIndex, @Param("limit") int limit);


    /**
     * Select all list.
     *
     * @param tStorage the t storage
     * @return the list
     */
    List<TStorage> selectAll(TStorage tStorage);


    /**
     * Select by map param list.
     *
     * @param mapParam the map param
     * @return the list
     */
    List<TStorage> selectByMapParam(Map<String, Object> mapParam);


    /**
     * Select count by map param int.
     *
     * @param mapParam the map param
     * @return the int
     */
    int selectCountByMapParam(Map<String, Object> mapParam);


    /**
     * Insert int.
     *
     * @param tStorage the t storage
     * @return the int
     */
    int insert(TStorage tStorage);

    /**
     * Update int.
     *
     * @param tStorage the t storage
     * @return the int
     */
    int update(TStorage tStorage);

    /**
     * Delete by id int.
     *
     * @param id the id
     * @return the int
     */
    int deleteById(Long id);

}