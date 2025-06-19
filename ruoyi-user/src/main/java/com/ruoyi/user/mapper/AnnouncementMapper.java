package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.Announcement;

import java.util.List;

public interface AnnouncementMapper {

    /**
     * 查询公告列表
     */
    List<Announcement> selectAnnouncementList(Announcement announcement);

    /**
     * 根据主键查询公告详情
     */
    Announcement selectAnnouncementByTitelAndContent(Announcement announcement);

    /**
     * 查询所有公告列表
     */
    List<Announcement> selectAllAnnouncementList();

    /**
     * 根据关键词搜索公告
     */
    List<Announcement> selectAnnouncementListByKeyword(String keyword);

    /**
     * 根据发布者ID查询公告
     */
    List<Announcement> selectAnnouncementListByIssuerId(Integer issuerId);

    /**
     * 统计公告总数
     */
    long countAnnouncements();

    /**
     * 分页查询公告
     */
    List<Announcement> selectAnnouncementsByPage(Announcement announcement);
}