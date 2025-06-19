package com.ruoyi.user.service;

import com.ruoyi.user.domain.User;

/**
 * 用户信息Service接口
 *
 * @author SockLightDust
 * @date 2025-05-24
 */
public interface IUserService {

    /**
     * 查询用户信息
     *
     * @param userId 用户信息主键
     * @return 用户信息
     */
    public User selectUserById(Long userId);

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    public User selectUserByUserName(String userName);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 结果
     */
    public int registerUser(User user);

    /**
     * 用户登录验证
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息
     */
    public User login(String userName, String password);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserProfile(User user);

    /**
     * 修改用户密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果
     */
    public int updateUserPassword(Long userId, String oldPassword, String newPassword);

    /**
     * 注销用户（修改状态为停用）
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deactivateUser(Long userId);

    /**
     * 更新用户登录信息
     *
     * @param userName 用户名
     * @param loginIp 登录IP
     * @return 结果
     */
    public int updateUserLoginInfo(String userName, String loginIp);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public String checkUserNameUnique(String userName);

    /**
     * 校验邮箱是否唯一
     *
     * @param email 邮箱
     * @return 结果
     */
    public String checkEmailUnique(String email);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public String checkPhoneUnique(String phonenumber);
}