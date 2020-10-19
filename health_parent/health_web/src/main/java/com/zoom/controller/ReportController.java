package com.zoom.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zoom.common.MessageConstant;
import com.zoom.entity.Result;
import com.zoom.service.MemberService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/18 16:50
 * 各种报告或者图片的打印
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    /**
     * 获取会员数与月数的关系数据，为关系图提供数据
     *
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            //获取当月前一年的每一个月份的用户人数与月份的分开的map集合
            Map map = memberService.getMonthWithMemberNumber();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 获取当日的运营数据统计
     *
     * @return
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            //根据日期获取大量的数据 新增会员数 总会员数 本周新增会员数 本月新增会员数 今日预约数
            // 今日到诊数 本周预约数 本周到诊数 本月预约数 本月到诊数 热门套餐(套餐名称 预约数量 占比 备注)
            // 这里暂时根据就获取当日时间
            Date today = new Date();
            Map map = memberService.getManyDataByDate();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 使用生成
     *
     * @return
     */
    @GetMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取，模板所在的路径
            String path = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            //获取操作xlsx工作簿的对象 (模板)
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            //获取数据
            Map map = memberService.getManyDataByDate();
            //获取jxls对象，将数据输出到模型中，这里不需要获取对象，而是改变了对象的值
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(xssfWorkbook,map);
            //将数据返回给请求发送方
            ServletOutputStream outputStream = response.getOutputStream();
            //设置文件类型,可用在tomcat的目录下的config目录下的web.xml搜索对应的格式查看例如 txt
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //设置文件下载后的名称，以及告诉系统这是文件下载content-Disposition
            response.setHeader("content-Disposition","attachment;filename=abc.xlsx");
            xssfWorkbook.write(outputStream);
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_FAIL);
        }
    }
}
