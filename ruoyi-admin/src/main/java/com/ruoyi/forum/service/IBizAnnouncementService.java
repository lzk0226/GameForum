package com.ruoyi.forum.service;

import com.ruoyi.forum.domain.BizAnnouncement;

import java.util.List;

/**
 * 公告栏Service接口
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public interface IBizAnnouncementService 
{
    /**
     * 查询公告栏
     * 
     * @param titel 公告栏主键
     * @return 公告栏
     */
    public BizAnnouncement selectBizAnnouncementByTitel(String titel);

    /**
     * 查询公告栏列表
     * 
     * @param bizAnnouncement 公告栏
     * @return 公告栏集合
     */
    public List<BizAnnouncement> selectBizAnnouncementList(BizAnnouncement bizAnnouncement);

    /**
     * 新增公告栏
     * 
     * @param bizAnnouncement 公告栏
     * @return 结果
     */
    public int insertBizAnnouncement(BizAnnouncement bizAnnouncement);

    /**
     * 修改公告栏
     * 
     * @param bizAnnouncement 公告栏
     * @return 结果
     */
    public int updateBizAnnouncement(BizAnnouncement bizAnnouncement);

    /**
     * 批量删除公告栏
     * 
     * @param titels 需要删除的公告栏主键集合
     * @return 结果
     */
    public int deleteBizAnnouncementByTitels(String[] titels);

    /**
     * 删除公告栏信息
     * 
     * @param titel 公告栏主键
     * @return 结果
     */
    public int deleteBizAnnouncementByTitel(String titel);
}
