package com.ruoyi.user.controller;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片访问控制器（无需认证）
 *
 * @author SockLightDust
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/user/public")
public class ImageAccessController {

    private static final String BASE_PATH = "C:/gameform/public/";

    /**
     * 访问头像图片
     */
    @GetMapping("/images/headPortrait/{fileName}")
    public void getAvatar(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        serveImage("images/headPortrait/" + fileName, response);
    }

    /**
     * 访问游戏图标
     */
    @GetMapping("/images/games/icon/{fileName}")
    public void getGameIcon(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        serveImage("images/games/icon/" + fileName, response);
    }

    /**
     * 访问游戏图片
     */
    @GetMapping("/images/games/images/{fileName}")
    public void getGameImage(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        serveImage("images/games/images/" + fileName, response);
    }

    /**
     * 访问帖子原图
     */
    @GetMapping("/images/user/post/{fileName}")
    public void getPostImage(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        serveImage("images/user/post/" + fileName, response);
    }

    /**
     * 访问帖子缩略图
     */
    @GetMapping("/images/user/thumbnail/{fileName}")
    public void getPostThumbnail(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        serveImage("images/user/thumbnail/" + fileName, response);
    }

    /**
     * 访问其他图片
     */
    @GetMapping("/images/other/{fileName}")
    public void getOtherImage(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        serveImage("images/other/" + fileName, response);
    }

    /**
     * 通用图片服务方法
     */
    private void serveImage(String relativePath, HttpServletResponse response) throws IOException {
        // 安全检查：防止路径遍历攻击
        if (relativePath.contains("..") || !relativePath.startsWith("images/")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String filePath = BASE_PATH + relativePath;
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 设置响应头
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        switch (extension) {
            case "jpg":
            case "jpeg":
                response.setContentType("image/jpeg");
                break;
            case "png":
                response.setContentType("image/png");
                break;
            case "gif":
                response.setContentType("image/gif");
                break;
            case "bmp":
                response.setContentType("image/bmp");
                break;
            case "webp":
                response.setContentType("image/webp");
                break;
            default:
                response.setContentType("application/octet-stream");
                break;
        }

        response.setContentLength((int) file.length());
        response.setHeader("Cache-Control", "max-age=3600"); // 缓存1小时

        // 输出文件
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}