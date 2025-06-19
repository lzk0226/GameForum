package com.ruoyi.user.controller;

import com.ruoyi.user.R.R;
import com.ruoyi.user.domain.Announcement;
import com.ruoyi.user.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/28上午 9:53
 * @Author : SockLightDust
 */
@RestController
@RequestMapping("/user/announcements")
@CrossOrigin(origins = "*")
public class AnnouncementController {

    @Autowired
    private IAnnouncementService announcementService;

    /**
     * 获取公告列表
     */

    @GetMapping("/list")
    public R<List<Announcement>> getAnnouncementList(Announcement announcement) {
        try {
            List<Announcement> announcements = announcementService.selectAnnouncementList(announcement);
            return R.ok("获取公告列表成功", announcements);
        } catch (Exception e) {
            return R.fail("获取公告列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有公告列表
     */
    @GetMapping
    public R<List<Announcement>> getAllAnnouncements() {
        try {
            List<Announcement> announcements = announcementService.selectAllAnnouncementList();
            return R.ok("获取所有公告成功", announcements);
        } catch (Exception e) {
            return R.fail("获取所有公告失败：" + e.getMessage());
        }
    }

    /**
     * 根据标题和内容获取公告详情
     */
    @GetMapping("/detail")
    public R<Announcement> getAnnouncementDetail(
            @RequestParam String titel,
            @RequestParam String content) {
        try {
            Announcement announcement = announcementService.selectAnnouncementByTitelAndContent(titel, content);
            if (announcement != null) {
                return R.ok("获取公告详情成功", announcement);
            } else {
                return R.fail("未找到该公告");
            }
        } catch (Exception e) {
            return R.fail("获取公告详情失败：" + e.getMessage());
        }
    }

    /**
     * 搜索公告
     */
    @GetMapping("/search")
    public R<List<Announcement>> searchAnnouncements(@RequestParam String keyword) {
        try {
            List<Announcement> announcements = announcementService.searchAnnouncements(keyword);
            return R.ok("搜索公告成功", announcements);
        } catch (Exception e) {
            return R.fail("搜索公告失败：" + e.getMessage());
        }
    }

    /**
     * 根据发布者ID获取公告列表
     */
    @GetMapping("/issuer/{issuerId}")
    public R<List<Announcement>> getAnnouncementsByIssuerId(@PathVariable Integer issuerId) {
        try {
            List<Announcement> announcements = announcementService.selectAnnouncementListByIssuerId(issuerId);
            return R.ok("获取发布者公告列表成功", announcements);
        } catch (Exception e) {
            return R.fail("获取发布者公告列表失败：" + e.getMessage());
        }
    }

    /**
     * 分页获取公告列表
     */
    @GetMapping("/page")
    public R<Map<String, Object>> getAnnouncementsByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            List<Announcement> announcements = announcementService.selectAnnouncementsByPage(pageNum, pageSize);
            long totalCount = announcementService.countAnnouncements();
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            Map<String, Object> result = new HashMap<>();
            result.put("list", announcements);
            result.put("pagination", createPaginationMap(pageNum, pageSize, totalCount, totalPages));

            return R.ok("获取分页公告列表成功", result);
        } catch (Exception e) {
            return R.fail("获取分页公告列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取公告统计信息
     */
    @GetMapping("/count")
    public R<Map<String, Object>> getAnnouncementCount() {
        try {
            long totalCount = announcementService.countAnnouncements();
            Map<String, Object> data = new HashMap<>();
            data.put("totalCount", totalCount);
            return R.ok("获取公告统计成功", data);
        } catch (Exception e) {
            return R.fail("获取公告统计失败：" + e.getMessage());
        }
    }

    /**
     * 创建分页信息Map
     */
    private Map<String, Object> createPaginationMap(int currentPage, int pageSize, long totalCount, int totalPages) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("currentPage", currentPage);
        pagination.put("pageSize", pageSize);
        pagination.put("totalCount", totalCount);
        pagination.put("totalPages", totalPages);
        return pagination;
    }
}