package com.zoom.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zoom.common.MessageConstant;
import com.zoom.common.RedisMessageConstant;
import com.zoom.entity.Result;
import com.zoom.exception.OrderException;
import com.zoom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/17 14:34
 * 体检预约控制层，前台用户端
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

   @Reference
    private OrderService orderService;
    /**
     * 体检预约提交
     * @param map
     * @return
     */
    @RequestMapping("submit")
    public Result examinationOrder(@RequestBody Map map){

        try {
            //获取前端传过来的验证码
            String validateCode = map.get("validateCode")+"";
            //获取电话号码
            String telephone = map.get("telephone")+"";
            //先校验，校验之后再提交给业务层
            String rCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone);
            if (StringUtils.isEmpty(validateCode)||StringUtils.isEmpty(rCode)|| !validateCode.equals(rCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            //验证码相等，再调用业务层中的方法,进行判断
            orderService.orderSubmit(map);
            //没有异常抛出则说明预约成功
            return new Result(true, MessageConstant.ORDER_SUCCESS);

        } catch (OrderException e) {
            e.printStackTrace();
            //发生异常则将异常的信息打印出来，当然该异常时自定义抛出的
            return new Result(false, e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.SYSTEM_EXCEPTION);
        }
    }
}
