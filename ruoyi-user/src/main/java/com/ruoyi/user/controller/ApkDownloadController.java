package com.ruoyi.user.controller;

import com.ruoyi.user.R.R;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * APK下载控制器
 *
 * @author SockLightDust
 * @date 2025-10-12
 */
@RestController
@RequestMapping("/user/download")
public class ApkDownloadController {

    private static final String APK_PATH = "C:/gameform/public/Apk/GameForm.apk";
    private static final String APK_FILENAME = "GameForm.apk";

    /**
     * 下载APK文件
     */
    @GetMapping("/apk")
    public ResponseEntity<Resource> downloadApk() {
        try {
            File apkFile = new File(APK_PATH);

            // 检查文件是否存在
            if (!apkFile.exists() || !apkFile.isFile()) {
                return ResponseEntity.notFound().build();
            }

            // 创建资源
            Resource resource = new FileSystemResource(apkFile);

            // 获取文件大小
            long fileSize = apkFile.length();

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + APK_FILENAME + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.android.package-archive");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileSize)
                    .contentType(MediaType.parseMediaType("application/vnd.android.package-archive"))
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 获取APK信息（文件大小、版本等）
     */
    @GetMapping("/apk/info")
    public R<ApkInfo> getApkInfo() {
        try {
            File apkFile = new File(APK_PATH);

            if (!apkFile.exists() || !apkFile.isFile()) {
                return R.fail("APK文件不存在");
            }

            ApkInfo info = new ApkInfo();
            info.setFileName(APK_FILENAME);
            info.setFileSize(apkFile.length());
            info.setFileSizeFormatted(formatFileSize(apkFile.length()));
            info.setLastModified(apkFile.lastModified());

            return R.ok(info);

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("获取APK信息失败：" + e.getMessage());
        }
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * APK信息实体类
     */
    public static class ApkInfo {
        private String fileName;
        private Long fileSize;
        private String fileSizeFormatted;
        private Long lastModified;

        // Getters and Setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Long getFileSize() {
            return fileSize;
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }

        public String getFileSizeFormatted() {
            return fileSizeFormatted;
        }

        public void setFileSizeFormatted(String fileSizeFormatted) {
            this.fileSizeFormatted = fileSizeFormatted;
        }

        public Long getLastModified() {
            return lastModified;
        }

        public void setLastModified(Long lastModified) {
            this.lastModified = lastModified;
        }
    }
}