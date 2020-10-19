package com.zoom.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zoom.common.MessageConstant;
import com.zoom.common.RedisConstant;
import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.entity.Result;
import com.zoom.pojo.CheckGroup;
import com.zoom.pojo.Setmeal;
import com.zoom.service.CheckGroupService;
import com.zoom.service.SetmealService;
import com.zoom.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


import java.util.List;

import java.util.UUID;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/11 20:02
 * 套餐相关的控制层
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private CheckGroupService checkGroupService;

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 图片上传
     *
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result fileUpload(MultipartFile imgFile) {
        try {
            //获取图片本来的名字
            String fileName = imgFile.getOriginalFilename();
            //去除后缀获取文件名
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            //通过uuid将其独特话，尽量避免图片名重复，发生覆盖
            String newFileName = (UUID.randomUUID() + suffix).replace("-", "");
            //调用工具类中的方法，将图片传递到七牛云上
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), newFileName);
            //走到这一步说明没有出现异常，则返回成功的信息以及文件名,用于回显以及后续的数据库文件的添加,
            //以及添加redis缓存，创建连接
            Jedis jedis = jedisPool.getResource();
            //添加数据
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);
            //归还连接
            jedis.close();
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            //走到这一步表示上传失败
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 获取所有检查组
     *
     * @return
     */
    @GetMapping("/getCheckGroup")
    public Result getCheckGroup() {
        try {
            //获取检查组
            List<CheckGroup> checkGroupList = this.checkGroupService.getCheckGroupList();
            return new Result(true, MessageConstant.PAGE_SELECT_CHECKGROUP_SUCCESS, checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常则返回异常信息
            return new Result(true, MessageConstant.PAGE_SELECT_CHECKGROUP_FAIL);
        }
    }

    @PostMapping("/createSetmeal")
    public Result createSetmeal(@RequestParam List<Integer> checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            //首先新增套餐
            this.setmealService.createSetmeal(checkgroupIds,setmeal);

            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询套餐
     * @param queryPageBean
     * @return
     */
    @PostMapping("/pageSelect")
    public Result selectPageList(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = this.setmealService.selectPageList(queryPageBean);
            //未出现异常则说明查询成功，正常返回结果
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常说明查询失败返回对应的失败信息
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 根据套餐编号获取套餐信息
     * @param setmealId
     * @return
     */
    @GetMapping("/setmealEidt")
    public Result selectSetmealAndCheckGroupsBySetmealId(Integer setmealId){
       try{
           //调用业务层的方法，复杂的业务在业务层内完成
          Setmeal setmeal =  this.setmealService.selectSetmealAndCheckGroupsBySetmealId(setmealId);
           //查询成功则返回对应的值以及旗帜
           return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
       }catch (Exception e){
           e.printStackTrace();
           return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
       }

    }

    /**
     * 编辑套餐，包括其下面的检查组
     * @param checkgroupIds
     * @param setmeal
     * @return
     */
    @PostMapping("/setmealEditSubmit")
    public Result setmealEditSubmit(@RequestParam List<Integer> checkgroupIds, @RequestBody Setmeal setmeal){
        try {
            //将这个功能放置到业务层，控制层就只做数据交互
            this.setmealService.setmealEditSubmit(checkgroupIds,setmeal);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 根据套餐id获取关联的检查组的id集合
     * @param setmealId
     * @return
     */
    @GetMapping("/getCheckgroupIds")
    public Result getCheckGroupIds(Integer setmealId){
        try {
            List<Integer> checkGroupIds = this.checkGroupService.getCheckGroupIds(setmealId);
            return new Result(true,MessageConstant.GET_CHECKGROUPIDS_BY_SETMEALID_SUCCESS,checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false,MessageConstant.GET_CHECKGROUPIDS_BY_SETMEALID_FAIL);
        }
    }


}
