package com.ruoyi.user.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.user.R.R;
import com.ruoyi.user.emun.ResultCodeEnum;
import com.ruoyi.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

/**
 * 文件上传控制器
 *
 * @author SockLightDust
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/user/upload")
public class FileUploadController {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 验证token
     */
    private boolean validateToken(String authHeader) {
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        return jwtUtils.validateToken(token);
    }

    /**
     * 上传头像
     */
    @PostMapping("/save-avatar")
    public R<String> uploadAvatar(@RequestBody Map<String, String> data,
                                  @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 验证token
            if (!validateToken(authHeader)) {
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            String fileName = data.get("fileName");
            String base64Data = data.get("base64Data");

            if (fileName == null || base64Data == null) {
                return R.fail("文件名或内容为空");
            }

            // 验证文件名格式（安全检查）
            if (!fileName.matches("^avatar_\\d+_[a-zA-Z0-9]+\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                return R.fail("文件名格式不正确");
            }

            // 去掉前缀（data:image/png;base64,）
            String base64Image = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // 验证图片大小（2MB限制）
            if (imageBytes.length > 5 * 1024 * 1024) {
                return R.fail("头像大小不能超过5MB");
            }

            // 保存路径
            String path = "D:/IdeaStash/test/public/images/headPortrait/";
            File dir = new File(path);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    return R.fail("创建目录失败");
                }
            }

            // 保存文件
            Files.write(Paths.get(path + fileName), imageBytes);

            // 返回相对路径
            String relativePath = "images/headPortrait/" + fileName;
            return R.ok(relativePath);

        } catch (IllegalArgumentException e) {
            return R.fail("图片格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    /**
     * 上传帖子图片
     */
    @PostMapping("/save-post-image")
    public R<String> uploadPostImage(@RequestBody Map<String, String> data,
                                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 验证token
            if (!validateToken(authHeader)) {
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            String fileName = data.get("fileName");
            String base64Data = data.get("base64Data");

            if (fileName == null || base64Data == null) {
                return R.fail("文件名或内容为空");
            }

            // 验证文件名格式（安全检查）
            if (!fileName.matches("^post_\\d+_[a-zA-Z0-9]+\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                return R.fail("文件名格式不正确");
            }

            // 去掉前缀（data:image/png;base64,）
            String base64Image = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // 验证图片大小（5MB限制）
            if (imageBytes.length > 10 * 1024 * 1024) {
                return R.fail("图片大小不能超过5MB");
            }

            // 保存路径
            String path = "D:/IdeaStash/test/public/images/user/post/";
            File dir = new File(path);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    return R.fail("创建目录失败");
                }
            }

            // 保存文件
            Files.write(Paths.get(path + fileName), imageBytes);

            // 返回相对路径
            String relativePath = "images/user/post/" + fileName;
            return R.ok(relativePath);

        } catch (IllegalArgumentException e) {
            return R.fail("图片格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    /**
     * 通用图片上传（支持多种类型）
     */
    @PostMapping("/image")
    public R<String> uploadImage(@RequestBody Map<String, String> data,
                                 @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 验证token
            if (!validateToken(authHeader)) {
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            String fileName = data.get("fileName");
            String base64Data = data.get("base64Data");
            String type = data.get("type"); // 图片类型：avatar, post, other

            if (fileName == null || base64Data == null) {
                return R.fail("文件名或内容为空");
            }

            // 根据类型设置不同的验证规则和保存路径
            String pathPrefix;
            String fileNamePattern;
            long maxSize;

            switch (type == null ? "other" : type) {
                case "avatar":
                    pathPrefix = "images/headPortrait/";
                    fileNamePattern = "^avatar_\\d+_[a-zA-Z0-9]+\\.(jpg|jpeg|png|gif|bmp|webp)$";
                    maxSize = 2 * 1024 * 1024; // 2MB
                    break;
                case "post":
                    pathPrefix = "images/user/post/";
                    fileNamePattern = "^post_\\d+_[a-zA-Z0-9]+\\.(jpg|jpeg|png|gif|bmp|webp)$";
                    maxSize = 5 * 1024 * 1024; // 5MB
                    break;
                default:
                    pathPrefix = "images/other/";
                    fileNamePattern = "^[a-zA-Z0-9_-]+\\.(jpg|jpeg|png|gif|bmp|webp)$";
                    maxSize = 3 * 1024 * 1024; // 3MB
                    break;
            }

            // 验证文件名格式（安全检查）
            if (!fileName.matches(fileNamePattern)) {
                return R.fail("文件名格式不正确");
            }

            // 去掉前缀（data:image/png;base64,）
            String base64Image = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // 验证图片大小
            if (imageBytes.length > maxSize) {
                return R.fail("图片大小超过限制");
            }

            // 保存路径
            String fullPath = "D:/IdeaStash/test/public/" + pathPrefix;
            File dir = new File(fullPath);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    return R.fail("创建目录失败");
                }
            }

            // 保存文件
            Files.write(Paths.get(fullPath + fileName), imageBytes);

            // 返回相对路径
            String relativePath = pathPrefix + fileName;
            return R.ok(relativePath);

        } catch (IllegalArgumentException e) {
            return R.fail("图片格式错误");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除图片文件
     */
    @DeleteMapping("/image")
    public R<String> deleteImage(@RequestBody Map<String, String> data,
                                 @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 验证token
            if (!validateToken(authHeader)) {
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            String relativePath = data.get("path");
            if (StringUtils.isEmpty(relativePath)) {
                return R.fail("文件路径不能为空");
            }

            // 安全检查：只允许删除指定目录下的文件
            if (!relativePath.startsWith("images/")) {
                return R.fail("无效的文件路径");
            }

            String fullPath = "D:/IdeaStash/test/public/" + relativePath;
            File file = new File(fullPath);

            if (!file.exists()) {
                return R.fail("文件不存在");
            }

            if (!file.isFile()) {
                return R.fail("不是有效的文件");
            }

            // 删除文件
            boolean deleted = file.delete();
            return deleted ? R.ok("删除成功") : R.fail("删除失败");

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("删除失败：" + e.getMessage());
        }
    }
}