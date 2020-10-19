package com.zoom.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zoom.common.MessageConstant;
import com.zoom.dao.MemberDao;
import com.zoom.dao.OrderDao;
import com.zoom.dao.SetmealDao;
import com.zoom.exception.OrderException;
import com.zoom.pojo.Member;
import com.zoom.pojo.Order;
import com.zoom.pojo.OrderSetting;
import com.zoom.pojo.Setmeal;
import com.zoom.service.OrderService;
import com.zoom.service.SetmealService;
import com.zoom.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/17 14:59
 * 体检预约业务层实现类,用户前台操作
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SetmealDao setmealDao;
    /**
     * 用于用户添加体检预约的方法
     *  进行多种情况的判断
     * @param map
     * @throws Exception
     */
    @Override
    public void orderSubmit(Map map) throws Exception {
        //将这些数据转换为对应的可用数据
        String idCard = map.get("idCard")+"";
        String name = map.get("name")+"";
        String orderDateString = map.get("orderDate") + "";
        int setmealId = Integer.parseInt(map.get("setmealId")+"");
        String sex = map.get("sex") + "";
        String telephone = map.get("telephone") + "";
        String code = map.get("validateCode") + "";

        //判断性别是否符合要求,根据套餐id获取套餐信息从而判断
        Setmeal setmeal = setmealDao.selectSetmealAndCheckGroupsBySetmealId(setmealId);
        if (setmeal==null){
            throw new OrderException("操作异常，该套餐不存在");
        }
        if (!setmeal.getSex().equals("0")&&!setmeal.getSex().equals(sex)){

            throw new OrderException("您输入的性别与套餐要求的性别不匹配，请自习查阅后再输入");
        }

        //根据日期查找，判断该日期是否可用预约
        Date orderDate = DateUtils.parseString2Date(orderDateString);
        if (orderDate.getTime()<new Date().getTime()){
            //防止恶意篡改前端数据
            throw new OrderException("操作异常，您不可以预约以前的日期");
        }
        OrderSetting orderSetting =  orderDao.getOderInformByOrderDate(orderDate);
        if (orderSetting==null){
            throw new OrderException("该日期暂时还没有可用预约，请更换预约日期");
        }
        //判断是否已经约满
        if(orderSetting.getNumber()<=orderSetting.getReservations()){
            //可预约数小于已预约数，说明了已经约满了
            throw new OrderException(MessageConstant.ORDER_FULL);
        }

        //判断是否是会员，是的话判断以前是否预约过，不是的话就先注册再直接预约(第一次登录不可能是以前预约过的)
        //先定义好一个member，不管是否注册都会给它赋值，后续会用到
        Member member = memberDao.getMemberByPhoneNumber(telephone);
        if (member==null){
            //新用户则要先注册
            member = new Member();
            member.setName(name);
            member.setSex(sex);
            member.setIdCard(idCard);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            //根据身份证获取生日
            //430381199702097819
            //6-10,10-12,12-14
            String birthday = idCard.substring(6,10)+"/"+idCard.substring(10,12)+"/"+idCard.substring(12,14);
            member.setBirthday(new Date(birthday));
            //创建一个新的用户
            memberDao.createNewMember(member);
        }else {
            //老用户则要判断是否预约了该套餐,根据用户id以及套餐id和时间
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setSetmealId(setmealId);
            order.setOrderDate(orderDate);
            Order orderInform = orderDao.getOrderInformBySetmealIdAndMemberIdAndOrderDate(order);
            if (orderInform!=null){
                //查询到了信息代表已预约，抛出异常
                throw new OrderException(MessageConstant.HAS_ORDERED);
            }
        }
        //执行到这一步说明没有预约套餐，则现在开始预约套餐
        Order orderCreate = new Order();
        orderCreate.setMemberId(member.getId());
        orderCreate.setOrderDate(orderDate);
        orderCreate.setOrderType(Order.ORDERTYPE_WEIXIN);
        orderCreate.setOrderStatus(Order.ORDERSTATUS_NO);
        orderCreate.setSetmealId(setmealId);
        orderDao.createOrder(orderCreate);
        //到这没异常的话代表预约成功
    }
}
