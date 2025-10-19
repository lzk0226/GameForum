package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

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
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    public User selectUserByEmail(String email);

    /**
     * 根据手机号查询用户信息
     *
     * @param phonenumber 手机号
     * @return 用户信息
     */
    public User selectUserByPhone(String phonenumber);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(User user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(User user);

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserStatus(User user);

    /**
     * 更新用户登录信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserLoginInfo(User user);

    /**
     * 检查用户名是否唯一
     *
     * @param userName 用户名
     * @return 结果
     */
    public int checkUserNameUnique(String userName);

    /**
     * 检查邮箱是否唯一
     *
     * @param email 邮箱
     * @return 结果
     */
    public int checkEmailUnique(String email);

    /**
     * 检查手机号是否唯一
     *
     * @param phonenumber 手机号
     * @return 结果
     */
    public int checkPhoneUnique(String phonenumber);
}