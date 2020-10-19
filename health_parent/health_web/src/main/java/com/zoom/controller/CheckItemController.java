package com.zoom.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zoom.common.MessageConstant;
import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.entity.Result;
import com.zoom.pojo.CheckItem;
import com.zoom.service.CheckItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/8 20:59
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    @GetMapping("/selectAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItemList = this.checkItemService.findAll();
            return new Result(true,MessageConstant.PAGE_SELECT_CHECKGROUP_SUCCESS,checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.PAGE_SELECT_CHECKGROUP_FAIL);
    }
    @RequestMapping("/pageSelect")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult =  this.checkItemService.findByPage(queryPageBean);
            //如果没有发生异常则说明查询成功
            return new Result(true, MessageConstant.PAGE_SELECT_CHECKITEM_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PAGE_SELECT_CHECKITEM_FAIL);
        }
    }
}
