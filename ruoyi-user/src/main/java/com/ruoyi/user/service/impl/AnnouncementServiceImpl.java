package com.ruoyi.user.service.impl;

import com.ruoyi.user.domain.Announcement;
import com.ruoyi.user.mapper.AnnouncementMapper;
import com.ruoyi.user.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/28上午 9:52
 * @Author : SockLightDust
 */
@Service
public class AnnouncementServiceImpl implements IAnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public List<Announcement> selectAnnouncementList(Announcement announcement) {
        return announcementMapper.selectAnnouncementList(announcement);
    }

    @Override
    public Announcement selectAnnouncementByTitelAndContent(String titel, String content) {
        if (titel == null || titel.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("标题和内容不能为空");
        }
        Announcement announcement = new Announcement();
        announcement.setTitel(titel);
        announcement.setContent(content);
        return announcementMapper.selectAnnouncementByTitelAndContent(announcement);
    }

    @Override
    public List<Announcement> selectAllAnnouncementList() {
        return announcementMapper.selectAllAnnouncementList();
    }

    @Override
    public List<Announcement> searchAnnouncements(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return selectAllAnnouncementList();
        }
        return announcementMapper.selectAnnouncementListByKeyword(keyword.trim());
    }

    @Override
    public List<Announcement> selectAnnouncementListByIssuerId(Integer issuerId) {
        if (issuerId == null) {
            throw new IllegalArgumentException("发布者ID不能为空");
        }
        return announcementMapper.selectAnnouncementListByIssuerId(issuerId);
    }

    @Override
    public List<Announcement> selectAnnouncementsByPage(int pageNum, int pageSize) {
        if (pageNum < 1) pageNum = 1;
        if (pageSize < 1) pageSize = 10;

        Announcement announcement = new Announcement();
        // 这里可以添加分页参数的处理逻辑
        return announcementMapper.selectAnnouncementsByPage(announcement);
    }

    @Override
    public long countAnnouncements() {
        return announcementMapper.countAnnouncements();
    }
}