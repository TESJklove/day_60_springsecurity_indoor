package com.zoom.job;

import com.zoom.common.RedisConstant;
import com.zoom.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/13 17:05
 * 任务调度类
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 用于删除垃圾图片
     */
    public void clearImg() {
        //获取jedis对象
        Jedis jedis = jedisPool.getResource();
        Set<String> sdiffImg = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //删除垃圾图片
        for (String s : sdiffImg) {
            System.out.println("删除图片:"+s);
            QiniuUtils.deleteFileFromQiniu(s);
        }
        //归还连接
        jedis.close();
    }

}
