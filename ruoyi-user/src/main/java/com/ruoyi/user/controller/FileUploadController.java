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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String BASE_PATH = "C:/gameform/public/";
    private static final Pattern BASE64_IMAGE_PATTERN = Pattern.compile(
            "<img[^>]+src=\"data:image/(\\w+);base64,([^\"]+)\"[^>]*>",
            Pattern.CASE_INSENSITIVE
    );

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
     * 从token获取用户ID
     */
    private Long getUserIdFromToken(String authHeader) {
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        return jwtUtils.getUserIdFromToken(token);
    }

    /**
     * 处理富文本内容，提取并保存base64图片
     * 返回处理后的HTML内容和第一张图片路径
     */
    public Map<String, Object> processRichTextContent(String htmlContent, String authHeader) {
        Map<String, Object> result = new HashMap<>();

        if (StringUtils.isEmpty(htmlContent)) {
            result.put("content", htmlContent);
            result.put("firstImagePath", null);
            return result;
        }

        Long userId = getUserIdFromToken(authHeader);
        if (userId == null) {
            result.put("content", htmlContent);
            result.put("firstImagePath", null);
            return result;
        }

        Matcher matcher = BASE64_IMAGE_PATTERN.matcher(htmlContent);
        StringBuffer processedContent = new StringBuffer();
        String firstImagePath = null;
        int imageIndex = 0;

        while (matcher.find()) {
            String imageType = matcher.group(1); // jpg, png, etc.
            String base64Data = matcher.group(2);

            try {
                // 生成唯一文件名
                String timestamp = String.valueOf(System.currentTimeMillis());
                String randomStr = UUID.randomUUID().toString().substring(0, 8);
                String fileName = String.format("post_%d_%s_%d.%s",
                        userId, randomStr, imageIndex, imageType);

                // 保存图片
                String relativePath = saveBase64Image(base64Data, fileName, "post");

                if (relativePath != null) {
                    // 记录第一张图片路径
                    if (imageIndex == 0) {
                        firstImagePath = relativePath;
                    }

                    // 替换为相对路径
                    String replacement = String.format(
                            "<img src=\"%s\" alt=\"post-image-%d\" />",
                            relativePath, imageIndex
                    );
                    matcher.appendReplacement(processedContent,
                            Matcher.quoteReplacement(replacement));
                    imageIndex++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 保留原始base64（降级处理）
                matcher.appendReplacement(processedContent,
                        Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(processedContent);

        result.put("content", processedContent.toString());
        result.put("firstImagePath", firstImagePath);
        return result;
    }

    /**
     * 保存base64图片
     */
    private String saveBase64Image(String base64Data, String fileName, String type) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // 验证大小
            if (imageBytes.length > 10 * 1024 * 1024) {
                return null;
            }

            String pathPrefix = "images/user/post/";
            String fullPath = BASE_PATH + pathPrefix;
            File dir = new File(fullPath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            Files.write(Paths.get(fullPath + fileName), imageBytes);
            return pathPrefix + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将图片路径转换为base64（用于前端显示）
     */
    public String convertImagePathsToBase64(String htmlContent) {
        if (StringUtils.isEmpty(htmlContent)) {
            return htmlContent;
        }

        Pattern imgPattern = Pattern.compile(
                "<img[^>]+src=\"(images/[^\"]+)\"[^>]*>",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = imgPattern.matcher(htmlContent);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String relativePath = matcher.group(1);
            String fullPath = BASE_PATH + relativePath;

            try {
                File imageFile = new File(fullPath);
                if (imageFile.exists() && imageFile.isFile()) {
                    byte[] imageBytes = Files.readAllBytes(Paths.get(fullPath));
                    String base64Data = Base64.getEncoder().encodeToString(imageBytes);

                    // 获取文件扩展名
                    String extension = relativePath.substring(relativePath.lastIndexOf(".") + 1);
                    String mimeType = getMimeType(extension);

                    String replacement = String.format(
                            "<img src=\"data:%s;base64,%s\" />",
                            mimeType, base64Data
                    );
                    matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
                } else {
                    // 文件不存在，保留原路径
                    matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 出错时保留原路径
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 根据文件扩展名获取MIME类型
     */
    private String getMimeType(String extension) {
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            default:
                return "image/jpeg";
        }
    }

    /**
     * 上传头像
     */
    @PostMapping("/save-avatar")
    public R<String> uploadAvatar(@RequestBody Map<String, String> data,
                                  @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (!validateToken(authHeader)) {
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            String fileName = data.get("fileName");
            String base64Data = data.get("base64Data");

            if (fileName == null || base64Data == null) {
                return R.fail("文件名或内容为空");
            }

            if (!fileName.matches("^avatar_\\d+_[a-zA-Z0-9]+\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                return R.fail("文件名格式不正确");
            }

            String base64Image = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            if (imageBytes.length > 5 * 1024 * 1024) {
                return R.fail("头像大小不能超过5MB");
            }

            String path = BASE_PATH + "images/headPortrait/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Files.write(Paths.get(path + fileName), imageBytes);
            String relativePath = "images/headPortrait/" + fileName;
            return R.ok(relativePath);

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    /**
     * 上传帖子图片（单张）
     */
    @PostMapping("/save-post-image")
    public R<String> uploadPostImage(@RequestBody Map<String, String> data,
                                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (!validateToken(authHeader)) {
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            String fileName = data.get("fileName");
            String base64Data = data.get("base64Data");

            if (fileName == null || base64Data == null) {
                return R.fail("文件名或内容为空");
            }

            if (!fileName.matches("^post_\\d+_[a-zA-Z0-9]+\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                return R.fail("文件名格式不正确");
            }

            String base64Image = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            if (imageBytes.length > 10 * 1024 * 1024) {
                return R.fail("图片大小不能超过10MB");
            }

            String path = BASE_PATH + "images/user/post/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Files.write(Paths.get(path + fileName), imageBytes);
            String relativePath = "images/user/post/" + fileName;
            return R.ok(relativePath);

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
            if (!validateToken(authHeader)) {
                return R.fail(ResultCodeEnum.TOKEN_INVALID);
            }

            String relativePath = data.get("path");
            if (StringUtils.isEmpty(relativePath)) {
                return R.fail("文件路径不能为空");
            }

            if (!relativePath.startsWith("images/")) {
                return R.fail("无效的文件路径");
            }

            String fullPath = BASE_PATH + relativePath;
            File file = new File(fullPath);

            if (!file.exists()) {
                return R.fail("文件不存在");
            }

            if (!file.isFile()) {
                return R.fail("不是有效的文件");
            }

            boolean deleted = file.delete();
            return deleted ? R.ok("删除成功") : R.fail("删除失败");

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("删除失败：" + e.getMessage());
        }
    }
}