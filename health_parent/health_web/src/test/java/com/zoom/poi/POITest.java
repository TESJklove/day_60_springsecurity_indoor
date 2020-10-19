package com.zoom.poi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zoom.pojo.CheckGroup;
import com.zoom.service.CheckGroupService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.io.FileOutputStream;
import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/14 13:04
 * 文件解析以及修改测试
 */
//@ContextConfiguration(locations = "classpath:springmvc.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
public class POITest {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 对文件进行解析
     */
  //  @Test
    public void TestByJX() throws Exception {
        //获取工作簿的路径
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("E:\\黑马\\上课资料\\101期\\提前下发资料\\传智健康\\第4天\\资料\\create.xlsx");
        //获取工作簿的第一行数据
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(1);
        for (Row row : sheetAt) {
            for (Cell cell : row) {
                System.out.print(cell + "\t" + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 对文件进行解析
     */
   // @Test
    public void TestByJX2() throws Exception {
        //获取工作簿的路径
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("E:\\黑马\\上课资料\\101期\\提前下发资料\\传智健康\\第4天\\资料\\create.xlsx");
        //获取工作簿的第一行数据
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(1);
        int lastRowNum = sheetAt.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheetAt.getRow(i);
            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                System.out.print(row.getCell(j)+"\t"+"\t");
            }
            System.out.println();
        }
    }

 //   @Test
    public void getObjectToXSL() throws Exception {
        List<CheckGroup> checkGroupList = checkGroupService.getCheckGroupList();
        //先创建对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建sheet分单
        XSSFSheet sheet = workbook.createSheet("检查组");
        //创建行,再到行上创建列
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("编码");
        row.createCell(2).setCellValue("名称");
        row.createCell(3).setCellValue("助记");
        row.createCell(4).setCellValue("使用性别编码");
        row.createCell(5).setCellValue("介绍");
        row.createCell(6).setCellValue("注意事项");
        int i = 0;
        //循环添加数据
        for (CheckGroup checkGroup : checkGroupList) {
            i++;
            XSSFRow dataRow = sheet.createRow(i);
            dataRow.createCell(0).setCellValue(checkGroup.getId());
            dataRow.createCell(1).setCellValue(checkGroup.getCode());
            dataRow.createCell(2).setCellValue(checkGroup.getName());
            dataRow.createCell(3).setCellValue(checkGroup.getHelpCode());
            dataRow.createCell(4).setCellValue(checkGroup.getSex());
            dataRow.createCell(5).setCellValue(checkGroup.getRemark());
            dataRow.createCell(6).setCellValue(checkGroup.getAttention());
        }
        //创建一个字节输出流选定文件存放的位置
        FileOutputStream out = new FileOutputStream("D:\\荒蛮之地\\演讲/医院后台导出工作簿.xsl");
        //使用poi的对象输出数据
        workbook.write(out);
        //关流
        out.close();
        workbook.close();
    }

}
