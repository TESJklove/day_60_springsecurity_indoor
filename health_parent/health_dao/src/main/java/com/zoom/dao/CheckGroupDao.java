package com.zoom.dao;

import com.github.pagehelper.Page;
import com.zoom.entity.QueryPageBean;
import com.zoom.pojo.CheckGroup;
import com.zoom.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/9 20:10
 * 体检检查组持久层接口
 */
public interface CheckGroupDao {
    Page<CheckGroup> selectPageList(String queryString);

    /**
     * 新增检查项
     * @param checkGroup
     */
    void createCheckGroup(CheckGroup checkGroup);

    /**
     * 新增一条检查项和检查组的中间关联数据
     * @param checkItemAndGroupMap
     */
    void createGroupAndItemConnection(Map<String, Integer> checkItemAndGroupMap);

    /**
     * 根据检查组的id查询关联的检查项id的集合
     * @param checkGroupId
     * @return
     */
    List<Integer> getItemIdByGroupId(Integer checkGroupId);

    CheckGroup getCheckGroupAllByGroupId(Integer checkGroupId);

    /**
     * 删除该检查组所有相关联的检查项的信息 根据检查组的id
     * @param id
     */
    void removeCheckItemWithGroupConnectionByGroupId(Integer id);

    /**
     * 修改检查组的信息
     * @param checkGroup
     */
    void updateCheckGroup(CheckGroup checkGroup);

    /**
     * 查看是否有关联的检查项
     * @param checkGroupId
     * @return
     */
    int getItemCountByGroupId(Integer checkGroupId);

    /**
     * 查看是否有关联的套餐
     * @param checkGroupId
     * @return
     */
    int getSetMealCountByGroupId(Integer checkGroupId);

    /**
     * 根据删除检查组id删除检查组
     * @param checkGroupId
     */
    void deleteCheckGroupById(Integer checkGroupId);

    /**
     * 查询所有的检查组
     * @return
     */
    List<CheckGroup> getCheckGroupList();

    /**
     * 根据套餐id获取关联的检查组的id集合
     * @param setmealId
     * @return
     */
    List<Integer> getIdBySetmealId(Integer setmealId);

    /**
     * 根据套餐id获取关联的检查组的集合
     * @param setmealId
     * @return
     */
    List<Setmeal> getIdBySetmeal(Integer setmealId);
}
