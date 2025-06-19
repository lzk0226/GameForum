package com.ruoyi.user.service.impl;


import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.user.domain.User;
import com.ruoyi.user.mapper.UserMapper;
import com.ruoyi.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/24下午 7:52
 * @Author : SockLightDust
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户信息
     *
     * @param userId 用户信息主键
     * @return 用户信息
     */
    @Override
    public User selectUserById(Long userId) {
        User user = userMapper.selectUserById(userId);
        if (user != null) {
            // 清空密码，不返回给前端
            user.setPassword(null);
        }
        return user;
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    @Override
    public User selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int registerUser(User user) {
        // 设置用户类型为普通用户
        user.setUserType("10");
        // 设置账号状态为正常
        user.setStatus("0");
        // 设置删除标志为正常
        user.setDelFlag("0");
        // 加密密码
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        // 设置创建时间
        user.setCreateTime(DateUtils.getNowDate());
        return userMapper.insertUser(user);
    }

    /**
     * 用户登录验证
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息
     */
    @Override
    public User login(String userName, String password) {
        User user = userMapper.selectUserByUserName(userName);
        if (user == null) {
            return null;
        }

        // 验证用户状态
        if (!"0".equals(user.getStatus())) {
            return null; // 账号已停用
        }

        // 验证用户类型，只有普通用户可以登录
        if (!"10".equals(user.getUserType())) {
            return null; // 用户类型不正确
        }

        // 验证密码
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            return null; // 密码错误
        }

        // 清空密码，不返回给前端
        user.setPassword(null);
        return user;
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(User user) {
        user.setUpdateTime(DateUtils.getNowDate());
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果
     */
    @Override
    public int updateUserPassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectUserById(userId);
        if (user == null) {
            return 0;
        }

        // 验证旧密码
        if (!SecurityUtils.matchesPassword(oldPassword, user.getPassword())) {
            return 0; // 旧密码错误
        }

        // 设置新密码
        user.setPassword(SecurityUtils.encryptPassword(newPassword));
        user.setUpdateTime(DateUtils.getNowDate());
        return userMapper.updateUser(user);
    }

    /**
     * 注销用户（修改状态为停用）
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deactivateUser(Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setStatus("1"); // 设置为停用状态
        return userMapper.updateUserStatus(user);
    }

    /**
     * 更新用户登录信息
     *
     * @param userName 用户名
     * @param loginIp 登录IP
     * @return 结果
     */
    @Override
    public int updateUserLoginInfo(String userName, String loginIp) {
        User user = new User();
        user.setUserName(userName);
        user.setLoginIp(loginIp);
        return userMapper.updateUserLoginInfo(user);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return String.valueOf(UserConstants.NOT_UNIQUE);
        }
        return String.valueOf(UserConstants.UNIQUE);
    }

    /**
     * 校验邮箱是否唯一
     *
     * @param email 邮箱
     * @return 结果
     */
    @Override
    public String checkEmailUnique(String email) {
        int count = userMapper.checkEmailUnique(email);
        if (count > 0) {
            return String.valueOf(UserConstants.NOT_UNIQUE);
        }
        return String.valueOf(UserConstants.UNIQUE);
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    @Override
    public String checkPhoneUnique(String phonenumber) {
        int count = userMapper.checkPhoneUnique(phonenumber);
        if (count > 0) {
            return String.valueOf(UserConstants.NOT_UNIQUE);
        }
        return String.valueOf(UserConstants.UNIQUE);
    }
}