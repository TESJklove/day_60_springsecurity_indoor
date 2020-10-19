package com.zoom.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zoom.common.MessageConstant;
import com.zoom.entity.Result;
import com.zoom.pojo.OrderSetting;
import com.zoom.service.OrderSettingService;
import com.zoom.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/14 13:00
 * 预约后台管理控制层
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * xls与xlsx文档上传
     *
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result XSLFileUpload(MultipartFile excelFile) {
        try {
            List<String[]> xlsFile = POIUtils.readExcel(excelFile);
            //将结果交给业务层处理,已有的就修改没有的就新增
            orderSettingService.addOrUpdateOrderSetting(xlsFile);
            return new Result(true, MessageConstant.FILE_UPLOAD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.FILE_UPLOAD_FAIL);
        }
    }

    /**
     * 通过前端传过来的年月获取该月份的所有预约信息
     *
     * @param date
     * @return
     */
    @GetMapping("/getOrderSettingByYearMonth")
    public Result getOrderSettingByYearMonth(String date) {
        try {
            //根据年月查询该月份的预约数据
            List<Map> mapList = orderSettingService.getOrderSettingByYearMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    @PostMapping("/updateNumberByOrderDate")
    public Result updateNumberByOrderDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.updateBySingle(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ORDERSETTING_FAIL);
        }

    }
}
