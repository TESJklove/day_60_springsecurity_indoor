package com.zoom.dao;

import com.github.pagehelper.Page;
import com.zoom.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/12 8:21
 * 套餐管理的持久层接口
 */
public interface SetmealDao {


    void addSetmealAndCheckGroupConnection(Map<String, Integer> connectionMap);

    /**
     * 新增套餐，并且获取新增的id
     * @param setmeal
     */
    void createSetmeal(Setmeal setmeal);

    /**
     * 分页查询套餐数据
     * @param queryString
     * @return
     */
    Page<Setmeal> selectPageList(String queryString);

    /**
     * 根据套餐编号获取套餐信息
     * @param setmealId
     * @return
     */
    Setmeal selectSetmealAndCheckGroupsBySetmealId(Integer setmealId);

    /**
     * 获取到setneal的id将原先的关联检查项都清空
     * @param setmealId
     */
    void deleteSetmealWithGroupConnectionBySetmealId(Integer setmealId);

    /**
     * 修改套餐主体数据
     * @param setmeal
     */
    void updateSetmeal(Setmeal setmeal);

    /**
     * 查询所有套餐信息
     * @return
     */
    List<Setmeal> getAllSetmeal();


}
