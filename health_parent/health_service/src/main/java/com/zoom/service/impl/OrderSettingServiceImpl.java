package com.zoom.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zoom.dao.OrderSettingDao;
import com.zoom.pojo.OrderSetting;
import com.zoom.pojo.Setmeal;
import com.zoom.service.OrderSettingService;
import com.zoom.service.SetmealService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


import java.util.*;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/14 20:42
 * 预约功能实业务层实现类，后天管理
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 新增或者修改后台的预约信息(未完善)
     *
     * @param xlsFile
     * @throws Exception
     */
    @Transactional
    @Override
    public void addOrUpdateOrderSetting(List<String[]> xlsFile) throws Exception {
        //获取数据，逐条解析到OrderSetting对象中，然后单个的判断再新增或者修改

        //List<String[]>中的一个list代表一行，String[]中的一个数据代表了该行中的一列数据,因此解析就变得简单了起来
        for (String[] strings : xlsFile) {
            //创建一个预约设置实体类接收解析出来的数据，进行封装
            OrderSetting orderSetting = new OrderSetting();
            orderSetting.setOrderDate(new Date(strings[0]));
            orderSetting.setNumber(Integer.parseInt(strings[1]));
            //抽取方法用于后面的单独设置
            singleOrderSettingAddOrUpdate(orderSetting);
        }

    }



    /**
     * 通过前端传过来的年月获取该月份的所有预约信息
     *
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByYearMonth(String date) {
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByYearMonth(date);
        List<Map> orderSettingMapList = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map orderSettingMap = new HashMap();
            //获取日
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number", orderSetting.getNumber());
            orderSettingMap.put("reservations", orderSetting.getReservations());
            orderSettingMapList.add(orderSettingMap);
        }
        return orderSettingMapList;
    }

    /**
     * 单个预约的修改
     *
     * @param orderSetting
     */
    @Override
    public void updateBySingle(OrderSetting orderSetting) {
        singleOrderSettingAddOrUpdate(orderSetting);
    }


    /**
     * 用于单条数据的新增或者修改
     *
     * @param orderSetting
     */
    private void singleOrderSettingAddOrUpdate(OrderSetting orderSetting) {
        //首先判断是否已存在 根据日期判断
        int count = orderSettingDao.isExistByDate(orderSetting.getOrderDate());
        if (count > 0) {
            //存在则修改,根据日期
            orderSettingDao.updateOrderSettingByDate(orderSetting);
        } else {
            //不存在则新增
            orderSettingDao.addOrderSetting(orderSetting);
        }
    }
}
