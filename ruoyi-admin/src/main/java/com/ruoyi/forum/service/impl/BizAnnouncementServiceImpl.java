package com.ruoyi.forum.service.impl;

import com.ruoyi.forum.domain.BizAnnouncement;
import com.ruoyi.forum.mapper.BizAnnouncementMapper;
import com.ruoyi.forum.service.IBizAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告栏Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-28
 */
@Service
public class BizAnnouncementServiceImpl implements IBizAnnouncementService 
{
    @Autowired
    private BizAnnouncementMapper bizAnnouncementMapper;

    /**
     * 查询公告栏
     * 
     * @param titel 公告栏主键
     * @return 公告栏
     */
    @Override
    public BizAnnouncement selectBizAnnouncementByTitel(String titel)
    {
        return bizAnnouncementMapper.selectBizAnnouncementByTitel(titel);
    }

    /**
     * 查询公告栏列表
     * 
     * @param bizAnnouncement 公告栏
     * @return 公告栏
     */
    @Override
    public List<BizAnnouncement> selectBizAnnouncementList(BizAnnouncement bizAnnouncement)
    {
        return bizAnnouncementMapper.selectBizAnnouncementList(bizAnnouncement);
    }

    /**
     * 新增公告栏
     * 
     * @param bizAnnouncement 公告栏
     * @return 结果
     */
    @Override
    public int insertBizAnnouncement(BizAnnouncement bizAnnouncement)
    {
        return bizAnnouncementMapper.insertBizAnnouncement(bizAnnouncement);
    }

    /**
     * 修改公告栏
     * 
     * @param bizAnnouncement 公告栏
     * @return 结果
     */
    @Override
    public int updateBizAnnouncement(BizAnnouncement bizAnnouncement)
    {
        return bizAnnouncementMapper.updateBizAnnouncement(bizAnnouncement);
    }

    /**
     * 批量删除公告栏
     * 
     * @param titels 需要删除的公告栏主键
     * @return 结果
     */
    @Override
    public int deleteBizAnnouncementByTitels(String[] titels)
    {
        return bizAnnouncementMapper.deleteBizAnnouncementByTitels(titels);
    }

    /**
     * 删除公告栏信息
     * 
     * @param titel 公告栏主键
     * @return 结果
     */
    @Override
    public int deleteBizAnnouncementByTitel(String titel)
    {
        return bizAnnouncementMapper.deleteBizAnnouncementByTitel(titel);
    }
}
