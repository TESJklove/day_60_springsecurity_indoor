package com.zoom.service;

import com.zoom.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/14 20:41
 * 预约功能业务层接口
 */
public interface OrderSettingService {
    /**
     * 新增或者修改后台的预约信息(未完善)
     * @param xlsFile
     * @throws Exception
     */
    void addOrUpdateOrderSetting(List<String[]> xlsFile)throws Exception;

    /**
     * 通过前端传过来的年月获取该月份的所有预约信息
     * @param date
     * @return
     */
    List<Map> getOrderSettingByYearMonth(String date);

    /**
     * 单个预约的修改
     * @param orderSetting
     */
    void updateBySingle(OrderSetting orderSetting);

}
