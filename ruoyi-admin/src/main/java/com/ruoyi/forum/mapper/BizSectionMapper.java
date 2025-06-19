package com.ruoyi.forum.mapper;

import java.util.List;
import com.ruoyi.forum.domain.BizSection;

/**
 * 论坛版块Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-17
 */
public interface BizSectionMapper 
{
    /**
     * 查询论坛版块
     * 
     * @param sectionId 论坛版块主键
     * @return 论坛版块
     */
    public BizSection selectBizSectionBySectionId(Long sectionId);

    /**
     * 查询论坛版块列表
     * 
     * @param bizSection 论坛版块
     * @return 论坛版块集合
     */
    public List<BizSection> selectBizSectionList(BizSection bizSection);

    /**
     * 新增论坛版块
     * 
     * @param bizSection 论坛版块
     * @return 结果
     */
    public int insertBizSection(BizSection bizSection);

    /**
     * 修改论坛版块
     * 
     * @param bizSection 论坛版块
     * @return 结果
     */
    public int updateBizSection(BizSection bizSection);

    /**
     * 删除论坛版块
     * 
     * @param sectionId 论坛版块主键
     * @return 结果
     */
    public int deleteBizSectionBySectionId(Long sectionId);

    /**
     * 批量删除论坛版块
     * 
     * @param sectionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizSectionBySectionIds(Long[] sectionIds);
}
