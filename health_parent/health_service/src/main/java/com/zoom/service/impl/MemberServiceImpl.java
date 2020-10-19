package com.zoom.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zoom.dao.MemberDao;
import com.zoom.dao.OrderDao;
import com.zoom.service.MemberService;
import com.zoom.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/18 16:57
 * 用户业务层实体类
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 获取当月前一年的每一个月份的用户人数与月份的分开的map集合
     *
     * @return
     */
    @Override
    public Map getMonthWithMemberNumber() throws Exception {
        //首先获取月份
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, -12);
        List<String> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            months.add(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1));
            calendar.add(Calendar.MONTH, 1);
        }
        List<Integer> number = new ArrayList<>();
        for (String month : months) {
            int count = memberDao.getMemberNumberByMonth(month + "-31");
            number.add(count);
        }
        Map map = new HashMap();
        map.put("months", months);
        map.put("memberCount", number);

        return map;
    }
//
//    /**
//     * 根据日期获取大量的数据 新增会员数 总会员数 本周新增会员数 本月新增会员数 今日预约数
//     * 今日到诊数 本周预约数 本周到诊数 本月预约数 本月到诊数 热门套餐(套餐名称 预约数量 占比 备注)
//     *
//     * @param today
//     * @return
//     */
//    @Override
//    public Map getManyDataByDate(Date today) throws Exception {
//        //获取将要返回的map集合
//        Map map = new HashMap();
//        //将日期转换为字符串格式
//        String reportDateString = DateUtils.parseDate2String(today);
//        map.put("reportDate", reportDateString);
//        //新增会员数
//        Integer todayNewMember = memberDao.findMemberCountByDate(reportDateString);
//        map.put("todayNewMember", todayNewMember);
//
//        //总会员数
//        Integer totalMember = memberDao.findMemberTotalCount();
//        map.put("totalMember", totalMember);
//
//        //本周新增会员数 计算出本周第一天的日期，转换为字符串，再计算这一天之后的新增会员数
//        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek(today);
//        String firstDayOfWeekString = DateUtils.parseDate2String(today);
//        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(firstDayOfWeekString);
//        map.put("thisWeekNewMember", thisWeekNewMember);
//
//        //本月新增会员数
//        Calendar calendar = Calendar.getInstance();
//        //获取指定时间的当月第一天
//        calendar.setTime(today);
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        String thisMonthNewMemberString = DateUtils.parseDate2String(calendar.getTime());
//        int thisMonthNewMember = memberDao.findMemberCountAfterDate(thisMonthNewMemberString);
//        map.put("thisMonthNewMember",thisMonthNewMember);
//
//        //获取今日预约数
//        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDateString);
//        map.put("todayOrderNumber",todayOrderNumber);
//        //获取今日到诊数
//   //     orderDao
//
//   //     todayVisitsNumber thisWeekOrderNumber thisWeekVisitsNumber thisMonthOrderNumber   thisMonthVisitsNumber
//        return null;
//    }


    /**
     * 根据日期获取大量的数据 新增会员数 总会员数 本周新增会员数 本月新增会员数 今日预约数
     * 今日到诊数 本周预约数 本周到诊数 本月预约数 本月到诊数 热门套餐(套餐名称 预约数量 占比 备注)
     *
     *
     * @return
     */
    @Override
    public Map getManyDataByDate() throws Exception {
        //1.定义Map返回结果
        Map rsMap = new HashMap();
        //报表日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());

        // 获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 获取本周最后一天的日期
        String thisWeekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        // 获得本月第一天的日期
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        // 获取本月最后一天的日期
        String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

        //2.查询会员数据
        // 今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);

        // 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        // 本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        // 本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
        //3.查询预约数据
        // 今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);

        // 本周预约数
        Map<String,Object> weekMap = new HashMap<String,Object>();
        weekMap.put("begin",thisWeekMonday);
        weekMap.put("end",thisWeekSunday);
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(weekMap);

        // 本月预约数
        Map<String,Object> monthMap = new HashMap<String,Object>();
        monthMap.put("begin",firstDay4ThisMonth);
        monthMap.put("end",lastDay4ThisMonth);
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(monthMap);

        // 今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

        // 本周到诊数
        Map<String,Object> weekMap2 = new HashMap<String,Object>();
        weekMap2.put("begin",thisWeekMonday);
        weekMap2.put("end",thisWeekSunday);
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(weekMap2);

        // 本月到诊数
        Map<String,Object> monthMap2 = new HashMap<String,Object>();
        monthMap2.put("begin",firstDay4ThisMonth);
        monthMap2.put("end",lastDay4ThisMonth);
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(monthMap2);

        //4.查询热门套餐数据
        List<Map> hotSetmeal = orderDao.findHotSetmeal();


        //5.返回结果
        rsMap.put("reportDate",today);
        rsMap.put("todayNewMember",todayNewMember); //今日新增会员数
        rsMap.put("totalMember",totalMember);
        rsMap.put("thisWeekNewMember",thisWeekNewMember);
        rsMap.put("thisMonthNewMember",thisMonthNewMember);
        rsMap.put("todayOrderNumber",todayOrderNumber);
        rsMap.put("todayVisitsNumber",todayVisitsNumber);
        rsMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        rsMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        rsMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        rsMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        rsMap.put("hotSetmeal",hotSetmeal);
        return rsMap;
    }


}
