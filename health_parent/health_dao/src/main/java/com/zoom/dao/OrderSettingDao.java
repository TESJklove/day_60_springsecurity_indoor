package com.zoom.dao;

import com.zoom.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/14 21:14
 * 预约持久层接口
 */
public interface OrderSettingDao {
    //首先判断是否已存在 根据日期判断
    int isExistByDate(Date orderDate);

    //存在则修改,根据日期
    void updateOrderSettingByDate(OrderSetting orderSetting);

    //存在则新增
    void addOrderSetting(OrderSetting orderSetting);

    /**
     * 通过前端传过来的年月获取该月份的所有预约信息
     * @param date
     * @return
     */
    List<OrderSetting> getOrderSettingByYearMonth(String date);
}
