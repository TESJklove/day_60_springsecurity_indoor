package com.zoom.dao;

import com.github.pagehelper.Page;
import com.zoom.pojo.Member;

import java.util.List;

/**
 * @author lkl
 * @version 1.0
 * @date 2020/10/17 15:50
 * 用户相关操作的持久层接口
 */
public interface MemberDao {
    /**
     * 根据电话号码获取用户信息
     *
     * @param telephone
     * @return
     */
    Member getMemberByPhoneNumber(String telephone);

    /**
     * 创建一个新的用户
     *
     * @param member
     */
    void createNewMember(Member member);

    /**
     * 根据月份查询该月人数 传进来的只有年月因此使用模糊查询
     *
     * @param month
     * @return
     */
    int getMemberNumberByMonth(String month);

    public List<Member> findAll();

    public Page<Member> selectByCondition(String queryString);

    public void add(Member member);

    public void deleteById(Integer id);

    public Member findById(Integer id);

    public Member findByTelephone(String telephone);

    public void edit(Member member);

    //<根据日期统计会员数，统计指定日期之前的会员数
    public Integer findMemberCountBeforeDate(String date);

    //获取指定日期的会员数量
    public Integer findMemberCountByDate(String date);
    //<!--根据日期统计会员数，统计指定日期之后的会员数-->
    public Integer findMemberCountAfterDate(String date);
    //获取总会员数
    public Integer findMemberTotalCount();
}
