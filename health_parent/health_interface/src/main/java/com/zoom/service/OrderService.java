package com.zoom.service;

import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/17 14:58
 * 体检预约业务接口
 */
public interface OrderService {

    /**
     * 用于用户添加体检预约的方法
     * 进行多种情况的判断
     * @param map
     */
    void orderSubmit(Map map) throws Exception;
}
