package com.zoom.service;

import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.pojo.CheckGroup;

import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/9 20:05
 * 体检检查组业务层接口
 */
public interface CheckGroupService {
    /**
     * 分页查询检查组的信息
     *
     * @param queryPageBean
     * @return
     */
    PageResult selectPageList(QueryPageBean queryPageBean);

    /**
     * 新增检查组，以及新增关联的检查项的对应关系
     *
     * @param checkItems
     * @param checkGroup
     * @throws Exception
     */
    void createCheckGroup(List<Integer> checkItems, CheckGroup checkGroup) throws Exception;

    /**
     * 根据检查组的id查询关联的检查项的id
     *
     * @param checkGroupId
     * @return
     */
    List<Integer> getItemIdByGroupId(Integer checkGroupId) throws Exception;

    /**
     * 根据检查组的id获取检查组的信息(包括懒加载)
     *
     * @param checkGroupId
     * @return
     */
    CheckGroup getCheckGroupAllByGroupId(Integer checkGroupId) throws Exception;

    /**
     * 修改检查组的信息
     *
     * @param checkItems
     * @param checkGroup
     */
    void updateCheckGroup(List<Integer> checkItems, CheckGroup checkGroup) throws Exception;

    /**
     * 根据检查组的id删除检查组
     *
     * @param checkGroupId
     */
    void deleteCheckGroup(Integer checkGroupId) throws Exception;

    /**
     * 查询所有的检查组
     *
     * @return
     */
    List<CheckGroup> getCheckGroupList() throws Exception;

    /**
     *根据套餐id获取关联的检查组的id
     * @param setmealId
     * @return
     */
    List<Integer> getCheckGroupIds(Integer setmealId)throws Exception;
}
