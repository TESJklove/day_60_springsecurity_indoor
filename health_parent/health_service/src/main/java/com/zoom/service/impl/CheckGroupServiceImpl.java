package com.zoom.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zoom.common.MessageConstant;
import com.zoom.dao.CheckGroupDao;
import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.exception.ConnectionOverflowException;
import com.zoom.pojo.CheckGroup;
import com.zoom.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/9 20:08
 * 体检检查组业务层实现类
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 分页查询检查组数据
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult selectPageList(QueryPageBean queryPageBean) {
        //设置分页小助手
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //将数据怼到分页小助手中
        Page<CheckGroup> checkGroups = this.checkGroupDao.selectPageList(queryPageBean.getQueryString());
        //返回结果
        return new PageResult(checkGroups.getTotal(), checkGroups.getResult());
    }

    /**
     * 新增检查组
     *
     * @param checkItems
     * @param checkGroup
     * @throws Exception
     */
    @Transactional
    @Override
    public void createCheckGroup(List<Integer> checkItems, CheckGroup checkGroup) throws Exception {
        //首先新增检查项,要求是能获取新增的查询组的id
        this.checkGroupDao.createCheckGroup(checkGroup);
        //获取检查项的id与选中的检查组的中的id共同创建
        createCheckItemAndGroup(checkItems, checkGroup.getId());

    }

    /**
     * 根据检查组查询关联的检查项的id
     *
     * @param checkGroupId
     * @return
     * @throws Exception
     */
    @Override
    public List<Integer> getItemIdByGroupId(Integer checkGroupId) throws Exception {
        List<Integer> itemList = this.checkGroupDao.getItemIdByGroupId(checkGroupId);
        return itemList;
    }

    @Override
    public CheckGroup getCheckGroupAllByGroupId(Integer checkGroupId) throws Exception {
        CheckGroup checkGroup = this.checkGroupDao.getCheckGroupAllByGroupId(checkGroupId);
        return checkGroup;
    }

    /**
     * 修改检查组的信息
     *
     * @param checkItems
     * @param checkGroup
     */
    @Transactional
    @Override
    public void updateCheckGroup(List<Integer> checkItems, CheckGroup checkGroup) throws Exception {
        //删除该检查组所有相关联的检查项的信息 根据检查组的id
        this.checkGroupDao.removeCheckItemWithGroupConnectionByGroupId(checkGroup.getId());
        //修改检查组的信息
        this.checkGroupDao.updateCheckGroup(checkGroup);
        //重新创建与检查组相关联的检查项的信息
        createCheckItemAndGroup(checkItems, checkGroup.getId());
    }

    /**
     * 根据检查组id删除检查组
     *
     * @param checkGroupId
     * @throws Exception
     */
    @Transactional
    @Override
    public void deleteCheckGroup(Integer checkGroupId) throws Exception {
        //查看是否有关联的检查项
        int countByItem = this.checkGroupDao.getItemCountByGroupId(checkGroupId);
        if (countByItem > 0) {
            throw new ConnectionOverflowException(MessageConstant.DELETE_CHECKITEM_FAIL_BY_HAVE_CHILD_ITEM);
        }
        //查看是否有关联的套餐
        int countBy = this.checkGroupDao.getSetMealCountByGroupId(checkGroupId);
        if (countBy > 0) {
            throw new ConnectionOverflowException(MessageConstant.DELETE_CHECKITEM_FAIL_BY_HAVE_PARENT_SETMEAL);
        }
        //如果都没有则能运行到这一步，删除这条数据
        this.checkGroupDao.deleteCheckGroupById(checkGroupId);
    }

    /**
     * 查询所有的检查组
     *
     * @return
     */
    @Override
    public List<CheckGroup> getCheckGroupList() throws Exception {
        return this.checkGroupDao.getCheckGroupList();
    }

    /**
     * 根据套餐id获取关联的检查组的id
     * @param setmealId
     * @return
     * @throws Exception
     */
    @Override
    public List<Integer> getCheckGroupIds(Integer setmealId) throws Exception {
        List<Integer> itemIdByGroupId = this.checkGroupDao.getIdBySetmealId(setmealId);
        return itemIdByGroupId;
    }

    /**
     * 根据检查组的id和检查项的id集合创建关联表
     *
     * @param checkItems
     * @param checkGroupId
     */
    private void createCheckItemAndGroup(List<Integer> checkItems, Integer checkGroupId) throws Exception {
        Map<String, Integer> checkItemAndGroupMap = new HashMap();
        for (Integer checkItem : checkItems) {
            //设置map集合的内容
            checkItemAndGroupMap.put("checkGroupId", checkGroupId);
            checkItemAndGroupMap.put("checkItemId", checkItem);
            //将map集合的数据添加进检查项和检查组中间表
            this.checkGroupDao.createGroupAndItemConnection(checkItemAndGroupMap);
        }
    }
}
