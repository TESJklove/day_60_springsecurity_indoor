package com.zoom.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zoom.common.RedisConstant;
import com.zoom.dao.SetmealDao;
import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.pojo.Setmeal;
import com.zoom.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/11 22:05
 * 套餐相关的业务层实现类
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String path;


    /**
     * 新增套餐,并且获取新增的数据的id
     *
     * @param checkgroupIds
     * @param setmeal
     * @throws Exception
     */
    @Transactional
    @Override
    public void createSetmeal(List<Integer> checkgroupIds, Setmeal setmeal) throws Exception {
        //新增套餐
        this.setmealDao.createSetmeal(setmeal);
        //新增完成之后给redis新增数据
        Jedis jedis = jedisPool.getResource();
        //将数据插入到redis的set集合中
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        //归还连接
        jedis.close();
        //获取套餐的id用于后面循环增加套餐检查组的关联关系
        Integer setmealId = setmeal.getId();
        //调用增加关联关系的方法
        addSetmealWithCheckGroupConnection(checkgroupIds, setmealId);

    }

    /**
     * 分页查询套餐
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult selectPageList(QueryPageBean queryPageBean) throws Exception {
        //使用分页助手传递进当前页数和每页记录数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //调用dao接口使得返回值为Page<T>
        Page<Setmeal> page = this.setmealDao.selectPageList(queryPageBean.getQueryString());
        //将值传递个PageResult并返回给上一级
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据套餐编号获取套餐信息
     *
     * @param setmealId
     * @return
     */
    @Override
    public Setmeal selectSetmealAndCheckGroupsBySetmealId(Integer setmealId) throws Exception {
        //获取套餐的数据
        return this.setmealDao.selectSetmealAndCheckGroupsBySetmealId(setmealId);
    }

    /**
     * 编辑套餐，包括其下面的检查组
     *
     * @param checkgroupIds
     * @param setmeal
     * @throws Exception
     */
    @Transactional
    @Override
    public void setmealEditSubmit(List<Integer> checkgroupIds, Setmeal setmeal) throws Exception {
        //第一步，获取到setneal的id将原先的关联检查项都清空
        this.setmealDao.deleteSetmealWithGroupConnectionBySetmealId(setmeal.getId());
        //第二部，修改套餐
        this.setmealDao.updateSetmeal(setmeal);
        //编辑完成之后给redis新增数据
        Jedis jedis = jedisPool.getResource();
        //将数据插入到redis的set集合中
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        //归还连接
        jedis.close();
        //第三步， 重新增加套餐与检查组的关联字段
        addSetmealWithCheckGroupConnection(checkgroupIds, setmeal.getId());
        //重新生成页面
        //1、生成套餐列表的页面
        List<Setmeal> allSetmeal = getAllSetmeal();
        if (allSetmeal != null && allSetmeal.size() > 0) {
            produceOrderList(allSetmeal);
            for (Setmeal setmealByFor : allSetmeal) {
                //2、生成套餐详情的页面
                produceOrderParticular(setmealByFor);
            }
        }
    }

    /**
     * 获取所有的套餐信息
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<Setmeal> getAllSetmeal() throws Exception {
        return setmealDao.getAllSetmeal();
    }

    /**
     * 用于遍历新增套餐与检查组的关联字段
     *
     * @param checkgroupIds
     * @param setmealId
     */
    public void addSetmealWithCheckGroupConnection(List<Integer> checkgroupIds, Integer setmealId) throws Exception {
        Map<String, Integer> connectionMap = new HashMap<>();
        for (Integer checkgroupId : checkgroupIds) {
            connectionMap.put("checkgroupId", checkgroupId);
            connectionMap.put("setmealId", setmealId);
            addSetmealWithCheckGroupConnection(connectionMap);
        }

    }

    /**
     * 增加套餐与检查组的对应关系,传一个map只需增加一条数据
     *
     * @param connectionMap
     */
    @Override
    public void addSetmealWithCheckGroupConnection(Map<String, Integer> connectionMap) throws Exception {
        this.setmealDao.addSetmealAndCheckGroupConnection(connectionMap);
    }

    /**
     * 生成套餐列表的页面
     */
    private void produceOrderList(List<Setmeal> allSetmeal) throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("setmealList", allSetmeal);
        freeMarkerConfigurer("mobile_setmeal.ftl", "m_setmeal", valueMap);
    }

    /**
     * 生成套餐详情的页面
     */
    private void produceOrderParticular(Setmeal setmealNoCheckGroups) throws Exception {
        Setmeal setmeal = selectSetmealAndCheckGroupsBySetmealId(setmealNoCheckGroups.getId());
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(setmeal)) {
            System.out.println(setmeal);
            map.put("setmeal", setmeal);
            freeMarkerConfigurer("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId(), map);
        }
    }

    /**
     * 根据输入的数据生成前端页面
     *
     * @param templateAddress 模板的地址,一般都在ftl下面
     * @param htmlName        页面的名称，只需要输入页面的名称即可，路径都注入了，后缀也添加上了
     * @param valueMap        页面需要的值
     * @throws IOException
     * @throws TemplateException
     */
    public void freeMarkerConfigurer(String templateAddress, String htmlName, Map<String, Object> valueMap) throws Exception {
        System.out.println(path);
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //获取模板
        Template template = configuration.getTemplate(templateAddress);
        //获取流,并设置地址
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path + htmlName + ".html"))));

        template.process(valueMap, br);
    }
}
