package com.zoom.mobile.controller;

import com.aliyuncs.exceptions.ClientException;
import com.zoom.common.MessageConstant;
import com.zoom.common.RedisMessageConstant;
import com.zoom.entity.Result;
import com.zoom.utils.SMSUtils;
import com.zoom.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/17 10:36
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取手机号，发送验证码
     *
     * @param telephone
     * @return
     */
    @GetMapping("/sendMessageCode")
    public Result sendMailCode(String telephone) {
        try {
            //发送验证码,类型为预约类型
            sendMessageCode(RedisMessageConstant.SENDTYPE_ORDER,telephone);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 用于发送验证码的方法,第一个参数为RedisMessageConstant中的常量，方法会加盐作为redis的键，无需手动加盐
     * @param type
     * @param telephone
     * @throws Exception
     */
    public void sendMessageCode(String type,String telephone) throws Exception {
        //获取redis对象
        Jedis redis = jedisPool.getResource();
        //随机生成验证码
        String code = ValidateCodeUtils.generateValidateCode(6)+"";
        //调用发送短信的方法传入验证码以及手机号以及验证码类型
        SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
        //将验证码保存到redis中，键为验证类型加上下划线再加上手机号码
        redis.setex(type+"_"+telephone,5*60,code);
        //归还连接
        redis.close();
    }

}
