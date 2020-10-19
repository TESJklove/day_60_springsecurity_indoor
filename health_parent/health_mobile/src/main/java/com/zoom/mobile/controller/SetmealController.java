package com.zoom.mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zoom.common.MessageConstant;
import com.zoom.entity.Result;
import com.zoom.pojo.Setmeal;
import com.zoom.service.SetmealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/15 17:18
 * 前台预约套餐查询界面
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    /**
     * 获取所有的套餐信息
     *
     * @return
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal() {
        try {
            List<Setmeal> setmealList = setmealService.getAllSetmeal();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 根据id获取套餐，及其检查组，及其检查项
     *
     * @param setmealId
     * @return
     */
    @GetMapping("/findById")
    public Result findSetmealAndGroupAndItemsById(Integer setmealId) {
        try {
            Setmeal setmeal = setmealService.selectSetmealAndCheckGroupsBySetmealId(setmealId);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
