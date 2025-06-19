package com.ruoyi.forum.mapper;

import com.ruoyi.forum.domain.BizAnnouncement;

import java.util.List;

/**
 * 公告栏Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
public interface BizAnnouncementMapper 
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
     * 删除公告栏
     * 
     * @param titel 公告栏主键
     * @return 结果
     */
    public int deleteBizAnnouncementByTitel(String titel);

    /**
     * 批量删除公告栏
     * 
     * @param titels 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizAnnouncementByTitels(String[] titels);
}
