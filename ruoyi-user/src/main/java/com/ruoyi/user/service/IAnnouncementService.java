package com.ruoyi.user.service;

import com.ruoyi.user.domain.Announcement;

import java.util.List;

public interface IAnnouncementService {
    /**
     * 查询公告列表
     */
    List<Announcement> selectAnnouncementList(Announcement announcement);

    /**
     * 根据主键查询公告详情
     */
    Announcement selectAnnouncementByTitelAndContent(String titel, String content);

    /**
     * 查询所有公告列表
     */
    List<Announcement> selectAllAnnouncementList();

    /**
     * 根据关键词搜索公告
     */
    List<Announcement> searchAnnouncements(String keyword);

    /**
     * 根据发布者ID查询公告
     */
    List<Announcement> selectAnnouncementListByIssuerId(Integer issuerId);

    /**
     * 分页查询公告
     */
    List<Announcement> selectAnnouncementsByPage(int pageNum, int pageSize);

    /**
     * 统计公告总数
     */
    long countAnnouncements();
}