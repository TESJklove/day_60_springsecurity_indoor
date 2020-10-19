package com.zoom.service;

import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/11 22:05
 * 套餐的业务层接口
 */
public interface SetmealService {
    /**
     *新增套餐
     *
     * @param checkgroupIds
     * @param setmeal
     * @throws Exception
     */
    void createSetmeal(List<Integer> checkgroupIds, Setmeal setmeal)throws Exception;

    /**
     * 增加套餐与检查组的对应关系
     * @param connectionMap
     */
    void addSetmealWithCheckGroupConnection(Map<String, Integer> connectionMap)throws Exception;

    /**
     * 分页查询套餐
     * @param queryPageBean
     * @return
     */
    PageResult selectPageList(QueryPageBean queryPageBean)throws Exception;

    /**
     * 根据套餐编号获取套餐信息
     * @param setmealId
     * @return
     */
    Setmeal selectSetmealAndCheckGroupsBySetmealId(Integer setmealId)throws Exception;

    /**
     * 编辑套餐，包括其下面的检查组
     * @param checkgroupIds
     * @param setmeal
     */
    void setmealEditSubmit(List<Integer> checkgroupIds, Setmeal setmeal)throws Exception;

    /**
     * 获取所有的套餐信息
     * @return
     */
    List<Setmeal> getAllSetmeal()throws Exception;
}
