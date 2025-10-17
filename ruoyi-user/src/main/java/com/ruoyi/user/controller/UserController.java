package com.ruoyi.user.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.user.R.R;
import com.ruoyi.user.constant.UserConstant;
import com.ruoyi.user.domain.User;
import com.ruoyi.user.emun.ResultCodeEnum;
import com.ruoyi.user.service.IUserService;
import com.ruoyi.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * 用户控制器
 * @Author : SockLightDust
 */
@RestController
@RequestMapping("/user/profile")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<String> register(@Validated @RequestBody User user) {
        // 参数校验
        if (StringUtils.isEmpty(user.getUserName())) {
            return R.fail(ResultCodeEnum.USER_NAME_EMPTY);
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return R.fail(ResultCodeEnum.PASSWORD_EMPTY);
        }
        if (StringUtils.isEmpty(user.getNickName())) {
            return R.fail(ResultCodeEnum.NICK_NAME_EMPTY);
        }

        // 校验用户名唯一性
        if (UserConstant.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return R.fail(ResultCodeEnum.USER_NAME_EXISTS);
        }

        // 校验邮箱唯一性
        if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstant.NOT_UNIQUE.equals(userService.checkEmailUnique(user.getEmail()))) {
            return R.fail(ResultCodeEnum.EMAIL_EXISTS);
        }

        // 校验手机号唯一性
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstant.NOT_UNIQUE.equals(userService.checkPhoneUnique(user.getPhonenumber()))) {
            return R.fail(ResultCodeEnum.PHONE_EXISTS);
        }

        int result = userService.registerUser(user);
        return result > 0 ? R.ok(ResultCodeEnum.REGISTER_SUCCESS.getMessage())
                : R.fail(ResultCodeEnum.REGISTER_FAILED);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R<LoginResponse> login(@RequestBody User loginUser, HttpServletRequest request) {
        // 参数校验
        if (StringUtils.isEmpty(loginUser.getUserName())) {
            return R.fail(ResultCodeEnum.USER_NAME_EMPTY);
        }
        if (StringUtils.isEmpty(loginUser.getPassword())) {
            return R.fail(ResultCodeEnum.PASSWORD_EMPTY);
        }

        User user = userService.login(loginUser.getUserName(), loginUser.getPassword());
        if (user == null) {
            return R.fail(ResultCodeEnum.LOGIN_FAILED);
        }

        // 更新登录信息
        String loginIp = IpUtils.getIpAddr(request);
        userService.updateUserLoginInfo(user.getUserName(), loginIp);

        // 生成JWT token和刷新token
        String accessToken = jwtUtils.generateToken(user.getUserId(), user.getUserName());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUserId(), user.getUserName());

        System.out.println("=== 登录成功 ===");
        System.out.println("AccessToken: " + accessToken);
        System.out.println("RefreshToken: " + refreshToken);

        // 构建返回数据
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setTokenType("Bearer");
        loginResponse.setExpiresIn(jwtUtils.getExpiration());
        loginResponse.setUser(user);

        return R.ok(loginResponse);
    }

    /**
     * ⭐ 刷新token - 修复版本
     */
    @PostMapping("/refreshToken")
    public R<TokenResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println("=== 收到刷新Token请求 ===");

        // 验证Authorization头
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            System.err.println("Authorization头格式错误");
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }

        String refreshToken = authHeader.substring(7);
        System.out.println("RefreshToken: " + refreshToken.substring(0, Math.min(50, refreshToken.length())) + "...");

        try {
            // ⭐ 使用专门的RefreshToken验证方法
            if (!jwtUtils.validateRefreshToken(refreshToken)) {
                System.err.println("RefreshToken验证失败");
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            // 从RefreshToken中获取用户信息
            Long userId = jwtUtils.getUserIdFromToken(refreshToken);
            String userName = jwtUtils.getUserNameFromToken(refreshToken);

            System.out.println("用户ID: " + userId);
            System.out.println("用户名: " + userName);

            if (userId == null || StringUtils.isEmpty(userName)) {
                System.err.println("无法从RefreshToken中获取用户信息");
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            // 生成新token
            String newAccessToken = jwtUtils.generateToken(userId, userName);
            String newRefreshToken = jwtUtils.generateRefreshToken(userId, userName);

            System.out.println("=== Token刷新成功 ===");
            System.out.println("新AccessToken: " + newAccessToken.substring(0, Math.min(50, newAccessToken.length())) + "...");
            System.out.println("新RefreshToken: " + newRefreshToken.substring(0, Math.min(50, newRefreshToken.length())) + "...");

            // 将旧RefreshToken加入黑名单
            jwtUtils.invalidateToken(refreshToken);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setAccessToken(newAccessToken);
            tokenResponse.setRefreshToken(newRefreshToken);
            tokenResponse.setTokenType("Bearer");
            tokenResponse.setExpiresIn(jwtUtils.getExpiration());

            return R.ok(tokenResponse);
        } catch (Exception e) {
            System.err.println("刷新Token异常: " + e.getMessage());
            e.printStackTrace();
            return R.fail(ResultCodeEnum.TOKEN_INVALID);
        }
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (StringUtils.isNotEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // 将token加入黑名单
            jwtUtils.invalidateToken(token);
        }
        return R.ok("退出成功");
    }

    /**
     * 获取用户详细信息
     */
    @GetMapping("/{userId}")
    public R<User> getInfo(@PathVariable("userId") Long userId) {
        User user = userService.selectUserById(userId);
        return user != null ? R.ok(user) : R.fail(ResultCodeEnum.USER_NOT_FOUND);
    }

    /**
     * 修改用户资料
     */
    @PutMapping("/update")
    public R<String> updateProfile(@RequestBody User user) {
        if (user.getUserId() == null) {
            return R.fail(ResultCodeEnum.USER_ID_EMPTY);
        }

        // 校验邮箱唯一性（排除自己）
        if (StringUtils.isNotEmpty(user.getEmail())) {
            User existUser = userService.selectUserById(user.getUserId());
            if (existUser != null && !user.getEmail().equals(existUser.getEmail())
                    && UserConstant.NOT_UNIQUE.equals(userService.checkEmailUnique(user.getEmail()))) {
                return R.fail(ResultCodeEnum.EMAIL_EXISTS);
            }
        }

        // 校验手机号唯一性（排除自己）
        if (StringUtils.isNotEmpty(user.getPhonenumber())) {
            User existUser = userService.selectUserById(user.getUserId());
            if (existUser != null && !user.getPhonenumber().equals(existUser.getPhonenumber())
                    && UserConstant.NOT_UNIQUE.equals(userService.checkPhoneUnique(user.getPhonenumber()))) {
                return R.fail(ResultCodeEnum.PHONE_EXISTS);
            }
        }

        int result = userService.updateUserProfile(user);
        return result > 0 ? R.ok(ResultCodeEnum.UPDATE_SUCCESS.getMessage())
                : R.fail(ResultCodeEnum.UPDATE_FAILED);
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public R<String> updatePassword(@RequestBody UpdatePasswordRequest request) {
        if (request.getUserId() == null) {
            return R.fail(ResultCodeEnum.USER_ID_EMPTY);
        }
        if (StringUtils.isEmpty(request.getOldPassword())) {
            return R.fail(ResultCodeEnum.OLD_PASSWORD_EMPTY);
        }
        if (StringUtils.isEmpty(request.getNewPassword())) {
            return R.fail(ResultCodeEnum.NEW_PASSWORD_EMPTY);
        }

        int result = userService.updateUserPassword(request.getUserId(),
                request.getOldPassword(),
                request.getNewPassword());
        return result > 0 ? R.ok(ResultCodeEnum.PASSWORD_UPDATE_SUCCESS.getMessage())
                : R.fail(ResultCodeEnum.PASSWORD_UPDATE_FAILED);
    }

    /**
     * 注销账户
     */
    @PutMapping("/deactivate/{userId}")
    public R<String> deactivateAccount(@PathVariable("userId") Long userId) {
        if (userId == null) {
            return R.fail(ResultCodeEnum.USER_ID_EMPTY);
        }

        int result = userService.deactivateUser(userId);
        return result > 0 ? R.ok(ResultCodeEnum.ACCOUNT_DEACTIVATE_SUCCESS.getMessage())
                : R.fail(ResultCodeEnum.ACCOUNT_DEACTIVATE_FAILED);
    }

    /**
     * 校验用户名
     */
    @GetMapping("/checkUserNameUnique/{userName}")
    public R<Boolean> checkUserNameUnique(@PathVariable("userName") String userName) {
        boolean isUnique = UserConstant.UNIQUE.equals(userService.checkUserNameUnique(userName));
        return R.ok(isUnique);
    }

    /**
     * 校验邮箱
     */
    @GetMapping("/checkEmailUnique/{email}")
    public R<Boolean> checkEmailUnique(@PathVariable("email") String email) {
        boolean isUnique = UserConstant.UNIQUE.equals(userService.checkEmailUnique(email));
        return R.ok(isUnique);
    }

    /**
     * 校验手机号
     */
    @GetMapping("/checkPhoneUnique/{phonenumber}")
    public R<Boolean> checkPhoneUnique(@PathVariable("phonenumber") String phonenumber) {
        boolean isUnique = UserConstant.UNIQUE.equals(userService.checkPhoneUnique(phonenumber));
        return R.ok(isUnique);
    }

    /**
     * 登录响应对象
     */
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
        private User user;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public Long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    /**
     * Token响应对象
     */
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public Long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
        }
    }

    /**
     * 修改密码请求对象
     */
    public static class UpdatePasswordRequest {
        private Long userId;
        private String oldPassword;
        private String newPassword;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}