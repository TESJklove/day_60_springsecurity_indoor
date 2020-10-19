package com.zoom.service;

import java.util.Date;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/18 16:57
 * 用户业务接口
 */
public interface MemberService {


    /**
     * 获取当月前一年的每一个月份的用户人数与月份的分开的map集合
     * @return
     */
    Map getMonthWithMemberNumber()throws Exception;

    /**
     * 根据日期获取大量的数据 新增会员数 总会员数 本周新增会员数 本月新增会员数 今日预约数
     * 今日到诊数 本周预约数 本周到诊数 本月预约数 本月到诊数 热门套餐(套餐名称 预约数量 占比 备注)
     * @return
     */
    Map getManyDataByDate() throws Exception;
}
