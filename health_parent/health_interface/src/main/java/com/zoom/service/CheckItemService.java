package com.zoom.service;

import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.pojo.CheckItem;

import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/8 20:48
 */
public interface CheckItemService {
    List<CheckItem> findAll() throws Exception;

    PageResult findByPage(QueryPageBean queryPageBean)throws Exception;
}
