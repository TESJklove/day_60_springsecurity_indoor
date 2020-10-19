package com.zoom.ftl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/15 20:24
 */
public class ModelTransaction {

    @Test
    public void ftlTOHtmlByInDoor() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("C:\\studyTool\\IdeaProject\\idea_java_stage_three\\zoom_health\\health_parent\\health_mobile\\src\\main\\webapp\\ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("index.ftl");
        FileWriter fileWriter = new FileWriter("C:\\studyTool\\IdeaProject\\idea_java_stage_three\\zoom_health\\health_parent\\health_mobile\\src\\main\\webapp\\pages/zoom.html");
        Map map=new HashMap();
        map.put("name", "张三");
        map.put("message", "欢迎来到传智播客！");
        template.process(map,fileWriter);
    }

    @Test
    public void ftlTOHtmlByList() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("C:\\studyTool\\IdeaProject\\idea_java_stage_three\\zoom_health\\health_parent\\health_mobile\\src\\main\\webapp\\ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("index.ftl");
        FileWriter fileWriter = new FileWriter("C:\\studyTool\\IdeaProject\\idea_java_stage_three\\zoom_health\\health_parent\\health_mobile\\src\\main\\webapp\\pages/goods.html");

        List goodsList = new ArrayList<>();
        Map map1=new HashMap();
        map1.put("id","1");
        map1.put("name","杜蕾斯牌避孕套");
        map1.put("price","139");
        goodsList.add(map1);

        Map map2=new HashMap();
        map2.put("id","2");
        map2.put("name","杜雪斯牌避孕套");
        map2.put("price","39");
        goodsList.add(map2);

        Map map3=new HashMap();
        map3.put("id","3");
        map3.put("name","社蕾斯牌避孕套");
        map3.put("price","13.9");
        goodsList.add(map3);

        System.out.println(goodsList);
        Map map = new HashMap();
        map.put("goodsList",goodsList);
        template.process(map,fileWriter);
    }
}
