package com.zoom.dao;

import com.zoom.pojo.Order;
import com.zoom.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/17 15:21
 * 体检预约用户端持久层接口
 */
public interface OrderDao {
    /**
     * 根据预约日期获取当天的预约信息
     *
     * @return
     */
    OrderSetting getOderInformByOrderDate(Date orderDate);


    /**
     * 根据用户id以及套餐id和时间查询预约详情信息
     *
     * @param order
     * @return
     */
    Order getOrderInformBySetmealIdAndMemberIdAndOrderDate(Order order);

    /**
     * 新增套餐预约信息
     *
     * @param orderCreate
     */
    void createOrder(Order orderCreate);

    public void add(Order order);

    public List<Order> findByCondition(Order order);

    public Map findById4Detail(Integer id);

    public Integer findOrderCountByDate(String date);

    public Integer findOrderCountAfterDate(String date);

    public Integer findVisitsCountByDate(String date);

    public Integer findVisitsCountAfterDate(String date);

    public List<Map> findHotSetmeal();

    Integer findOrderCountBetweenDate(Map<String, Object> map);

    Integer findVisitsCountAfterDate(Map<String, Object> map);
}
