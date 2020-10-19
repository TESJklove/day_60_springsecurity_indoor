package com.zoom.dao;

import com.github.pagehelper.Page;
import com.zoom.pojo.CheckItem;

import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/8 20:51
 *
 */
public interface CheckItemDao {
    List<CheckItem> findAll() throws Exception;

    Page<CheckItem> findAllByPage(String queryString);

    /**
     * 根据 检查组的id获取到关联的检查项的数据
     * @return List<Setmeal>
     */
    List<CheckItem> getSetmealListByGroupId(Integer checkGroupId);
}
