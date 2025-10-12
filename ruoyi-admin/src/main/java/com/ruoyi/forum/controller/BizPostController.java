package com.ruoyi.forum.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.forum.domain.BizPost;
import com.ruoyi.forum.service.IBizPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 论坛帖子Controller
 *
 * @author ruoyi
 * @date 2025-05-17
 */
@RestController
@RequestMapping("/forum/post")
public class BizPostController extends BaseController
{
    @Autowired
    private IBizPostService bizPostService;

    private static final String BASE_PATH = "C:/gameform/public/";
    private static final Pattern BASE64_IMAGE_PATTERN = Pattern.compile(
            "<img[^>]+src=\"data:image/(\\w+);base64,([^\"]+)\"[^>]*>",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * 查询论坛帖子列表
     */
    @PreAuthorize("@ss.hasPermi('forum:post:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizPost bizPost)
    {
        startPage();
        List<BizPost> list = bizPostService.selectBizPostList(bizPost);
        return getDataTable(list);
    }

    /**
     * 导出论坛帖子列表
     */
    @PreAuthorize("@ss.hasPermi('forum:post:export')")
    @Log(title = "论坛帖子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizPost bizPost)
    {
        List<BizPost> list = bizPostService.selectBizPostList(bizPost);
        ExcelUtil<BizPost> util = new ExcelUtil<BizPost>(BizPost.class);
        util.exportExcel(response, list, "论坛帖子数据");
    }

    /**
     * 获取论坛帖子详细信息
     */
    @PreAuthorize("@ss.hasPermi('forum:post:query')")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable("postId") Long postId)
    {
        BizPost post = bizPostService.selectBizPostByPostId(postId);

        // 将帖子内容中的图片路径转换为base64（用于编辑器显示）
        if (post != null && StringUtils.isNotEmpty(post.getPostContent())) {
            String processedContent = convertImagePathsToBase64(post.getPostContent());
            post.setPostContent(processedContent);
        }

        return success(post);
    }

    /**
     * 新增论坛帖子
     */
    @PreAuthorize("@ss.hasPermi('forum:post:add')")
    @Log(title = "论坛帖子", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizPost bizPost)
    {
        // 验证必要字段
        if (StringUtils.isEmpty(bizPost.getPostTitle())) {
            return error("帖子标题不能为空");
        }
        if (StringUtils.isEmpty(bizPost.getPostContent())) {
            return error("帖子内容不能为空");
        }

        // 验证标题长度
        if (bizPost.getPostTitle().length() > 100) {
            return error("帖子标题长度不能超过100个字符");
        }

        // 处理富文本内容：提取base64图片并保存
        Map<String, Object> processResult = processRichTextContent(bizPost.getPostContent(), bizPost.getUserId());
        String processedContent = (String) processResult.get("content");

        // 验证纯文本内容长度（不计入base64和HTML标签）
        if (!validateContentLength(processedContent, 100000L)) {
            return error("帖子内容过长，纯文本内容不能超过100000个字符");
        }

        // 设置处理后的内容
        bizPost.setPostContent(processedContent);

        return toAjax(bizPostService.insertBizPost(bizPost));
    }

    /**
     * 修改论坛帖子
     */
    @PreAuthorize("@ss.hasPermi('forum:post:edit')")
    @Log(title = "论坛帖子", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizPost bizPost)
    {
        // 验证必要字段
        if (bizPost.getPostId() == null) {
            return error("帖子ID不能为空");
        }

        // 验证帖子是否存在
        BizPost existPost = bizPostService.selectBizPostByPostId(bizPost.getPostId());
        if (existPost == null) {
            return error("帖子不存在");
        }

        // 如果更新了标题，验证标题
        if (StringUtils.isNotEmpty(bizPost.getPostTitle())) {
            if (bizPost.getPostTitle().trim().isEmpty()) {
                return error("帖子标题不能为空");
            }
            if (bizPost.getPostTitle().length() > 100) {
                return error("帖子标题长度不能超过100个字符");
            }
        }

        // 如果更新了内容，处理富文本
        if (StringUtils.isNotEmpty(bizPost.getPostContent())) {
            if (bizPost.getPostContent().trim().isEmpty()) {
                return error("帖子内容不能为空");
            }

            Map<String, Object> processResult = processRichTextContent(
                    bizPost.getPostContent(),
                    existPost.getUserId()
            );
            String processedContent = (String) processResult.get("content");

            // 验证纯文本内容长度
            if (!validateContentLength(processedContent, 100000L)) {
                return error("帖子内容过长，纯文本内容不能超过100000个字符");
            }

            bizPost.setPostContent(processedContent);
        }

        return toAjax(bizPostService.updateBizPost(bizPost));
    }

    /**
     * 删除论坛帖子
     */
    @PreAuthorize("@ss.hasPermi('forum:post:remove')")
    @Log(title = "论坛帖子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{postIds}")
    public AjaxResult remove(@PathVariable Long[] postIds)
    {
        return toAjax(bizPostService.deleteBizPostByPostIds(postIds));
    }

    // ========== 富文本处理方法 ==========

    /**
     * 处理富文本内容，提取并保存base64图片
     * 返回处理后的HTML内容
     */
    private Map<String, Object> processRichTextContent(String htmlContent, Long userId) {
        Map<String, Object> result = new HashMap<>();

        if (StringUtils.isEmpty(htmlContent)) {
            result.put("content", htmlContent);
            return result;
        }

        if (userId == null) {
            userId = 0L; // 默认用户ID
        }

        Matcher matcher = BASE64_IMAGE_PATTERN.matcher(htmlContent);
        StringBuffer processedContent = new StringBuffer();
        int imageIndex = 0;

        while (matcher.find()) {
            String imageType = matcher.group(1); // jpg, png, etc.
            String base64Data = matcher.group(2);

            try {
                // 生成唯一文件名
                String randomStr = UUID.randomUUID().toString().substring(0, 8);
                String fileName = String.format("admin_post_%d_%s_%d.%s",
                        userId, randomStr, imageIndex, imageType);

                // 保存图片
                String relativePath = saveBase64Image(base64Data, fileName);

                if (relativePath != null) {
                    // 替换为相对路径
                    String replacement = String.format(
                            "<img src=\"%s\" alt=\"post-image-%d\" />",
                            relativePath, imageIndex
                    );
                    matcher.appendReplacement(processedContent,
                            Matcher.quoteReplacement(replacement));
                    imageIndex++;
                } else {
                    // 保存失败，保留原始base64（降级处理）
                    matcher.appendReplacement(processedContent,
                            Matcher.quoteReplacement(matcher.group(0)));
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 出错时保留原始base64
                matcher.appendReplacement(processedContent,
                        Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(processedContent);

        result.put("content", processedContent.toString());
        return result;
    }

    /**
     * 保存base64图片
     */
    private String saveBase64Image(String base64Data, String fileName) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // 验证大小（最大10MB）
            if (imageBytes.length > 10 * 1024 * 1024) {
                return null;
            }

            String pathPrefix = "images/admin/post/";
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
    private String convertImagePathsToBase64(String htmlContent) {
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
     * 计算纯文本内容的长度（不包括base64图片和HTML标签）
     */
    private Long calculatePureTextLength(String htmlContent) {
        if (StringUtils.isEmpty(htmlContent)) {
            return 0L;
        }

        // 移除base64图片
        String withoutBase64 = htmlContent.replaceAll(
                "<img[^>]+src=\"data:image/\\w+;base64,[^\"]+\"[^>]*>",
                ""
        );

        // 移除所有HTML标签
        String plainText = withoutBase64.replaceAll("<[^>]+>", "");

        // 解码HTML实体
        plainText = plainText.replace("&nbsp;", " ")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'");

        return (long) plainText.length();
    }

    /**
     * 验证纯文本内容长度
     */
    private boolean validateContentLength(String htmlContent, long maxLength) {
        Long pureTextLength = calculatePureTextLength(htmlContent);
        return pureTextLength <= maxLength;
    }
}