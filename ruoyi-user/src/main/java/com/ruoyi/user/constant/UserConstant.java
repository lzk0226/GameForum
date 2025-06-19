package com.ruoyi.user.constant;

/**
 * 用户常量信息
 *
 * @author SockLightDust
 */
public class UserConstant {

    /**
     * 校验返回结果码
     */
    public static final String UNIQUE = "0";
    public static final String NOT_UNIQUE = "1";

    /**
     * 用户类型
     */
    public static final String USER_TYPE_SYSTEM = "00";  // 系统用户
    public static final String USER_TYPE_NORMAL = "10";  // 普通用户

    /**
     * 用户状态
     */
    public static final String USER_STATUS_NORMAL = "0";  // 正常
    public static final String USER_STATUS_DISABLE = "1"; // 停用

    /**
     * 删除标志
     */
    public static final String DEL_FLAG_EXIST = "0";    // 存在
    public static final String DEL_FLAG_DELETE = "2";   // 删除

    /**
     * 用户性别
     */
    public static final String SEX_MALE = "0";     // 男
    public static final String SEX_FEMALE = "1";   // 女
    public static final String SEX_UNKNOWN = "2";  // 未知
}