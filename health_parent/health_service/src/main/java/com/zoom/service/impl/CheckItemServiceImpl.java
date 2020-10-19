package com.zoom.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zoom.dao.CheckItemDao;
import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.pojo.CheckItem;
import com.zoom.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/8 20:48
 */
@Service
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll()throws Exception {
        return this.checkItemDao.findAll();
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) throws Exception {
        //业务层要查询两个数据，但是有了分页小助手之后就只简化了很多了
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> page = this.checkItemDao.findAllByPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }
}
