package com.zoom.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zoom.common.MessageConstant;
import com.zoom.entity.PageResult;
import com.zoom.entity.QueryPageBean;
import com.zoom.entity.Result;
import com.zoom.exception.ConnectionOverflowException;
import com.zoom.pojo.CheckGroup;
import com.zoom.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/9 20:01
 * 体检检查组控制层
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 分页查询检查项
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/pageSelect")
    public Result selectPageList(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = this.checkGroupService.selectPageList(queryPageBean);
            //未出现异常则说明查询成功，正常返回结果
            return new Result(true, MessageConstant.PAGE_SELECT_CHECKGROUP_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常说明查询失败返回对应的失败信息
            return new Result(false, MessageConstant.PAGE_SELECT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 新增检查组
     *
     * @return
     */
    @PostMapping("/create")
    public Result createCheckGroup(@RequestParam List<Integer> checkItems, @RequestBody CheckGroup checkGroup) {
        //获取到新增界面的检查组的信息，新增检查组到检查表
        //获取检查项的id和检查组的id集合，添加到检查组和检查项中间表中
        try {
            System.out.println("aaa");
            this.checkGroupService.createCheckGroup(checkItems, checkGroup);
            //如果没有异常则返回
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 修改检查组的信息
     * @param checkItems
     * @param checkGroup
     * @return
     */
    @PostMapping("/updateCheckGroup")
    public Result updateCheckGroup(@RequestParam List<Integer> checkItems, @RequestBody CheckGroup checkGroup){
        try {
            this.checkGroupService.updateCheckGroup(checkItems,checkGroup);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据检查组编号获取检查项的编号的集合
     * @param checkGroupId
     * @return
     */
    @GetMapping("/getItemIdByGroupId")
    public Result getAllItemIdByGroupId(Integer checkGroupId){
        try {
            List<Integer> itemsList =  this.checkGroupService.getItemIdByGroupId(checkGroupId);
            return new Result(true,MessageConstant.SELECT_CHECKITEMSID_BY_CHECKGROUP_SUCCESS,itemsList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SELECT_CHECKITEMSID_BY_CHECKGROUP_FAIL);
        }
    }


    /**
     * 根据检查组的id获取检查组的信息(其中包括检查项id集合)
     * @param checkGroupId
     * @return
     */
    @GetMapping("/getCheckGroupAllByGroupId")
    public Result getCheckGroupAllByGroupId(Integer checkGroupId){
        try {
            CheckGroup checkGroup =  this.checkGroupService.getCheckGroupAllByGroupId(checkGroupId);
            //没有发生异常，代表着查询成功
            return new Result(true,MessageConstant.PAGE_SELECT_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            //发生了异常，返回失败的标记给前端
            return new Result(false,MessageConstant.PAGE_SELECT_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据检查组的id删除检查组，需要判断该检查组下面是否有关联的检查项，以及是否有检查套餐关联了该检查组
     * @param checkGroupId
     * @return
     */
    @GetMapping("/deleteCheckGroup")
    public Result deleteCheckGroup(Integer checkGroupId){
        try {
            this.checkGroupService.deleteCheckGroup(checkGroupId);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (ConnectionOverflowException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

}
