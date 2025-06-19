package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizSectionMapper;
import com.ruoyi.forum.domain.BizSection;
import com.ruoyi.forum.service.IBizSectionService;

/**
 * 论坛版块Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizSectionServiceImpl implements IBizSectionService 
{
    @Autowired
    private BizSectionMapper bizSectionMapper;

    /**
     * 查询论坛版块
     * 
     * @param sectionId 论坛版块主键
     * @return 论坛版块
     */
    @Override
    public BizSection selectBizSectionBySectionId(Long sectionId)
    {
        return bizSectionMapper.selectBizSectionBySectionId(sectionId);
    }

    /**
     * 查询论坛版块列表
     * 
     * @param bizSection 论坛版块
     * @return 论坛版块
     */
    @Override
    public List<BizSection> selectBizSectionList(BizSection bizSection)
    {
        return bizSectionMapper.selectBizSectionList(bizSection);
    }

    /**
     * 新增论坛版块
     * 
     * @param bizSection 论坛版块
     * @return 结果
     */
    @Override
    public int insertBizSection(BizSection bizSection)
    {
        bizSection.setCreateTime(DateUtils.getNowDate());
        return bizSectionMapper.insertBizSection(bizSection);
    }

    /**
     * 修改论坛版块
     * 
     * @param bizSection 论坛版块
     * @return 结果
     */
    @Override
    public int updateBizSection(BizSection bizSection)
    {
        bizSection.setUpdateTime(DateUtils.getNowDate());
        return bizSectionMapper.updateBizSection(bizSection);
    }

    /**
     * 批量删除论坛版块
     * 
     * @param sectionIds 需要删除的论坛版块主键
     * @return 结果
     */
    @Override
    public int deleteBizSectionBySectionIds(Long[] sectionIds)
    {
        return bizSectionMapper.deleteBizSectionBySectionIds(sectionIds);
    }

    /**
     * 删除论坛版块信息
     * 
     * @param sectionId 论坛版块主键
     * @return 结果
     */
    @Override
    public int deleteBizSectionBySectionId(Long sectionId)
    {
        return bizSectionMapper.deleteBizSectionBySectionId(sectionId);
    }
}
