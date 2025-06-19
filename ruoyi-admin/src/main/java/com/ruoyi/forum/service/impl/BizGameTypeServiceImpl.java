package com.ruoyi.forum.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.forum.mapper.BizGameTypeMapper;
import com.ruoyi.forum.domain.BizGameType;
import com.ruoyi.forum.service.IBizGameTypeService;

/**
 * 游戏类型Service业务层处理
 *
 * @author ruoyi
 * @date 2025-05-17
 */
@Service
public class BizGameTypeServiceImpl implements IBizGameTypeService
{
    @Autowired
    private BizGameTypeMapper bizGameTypeMapper;

    /**
     * 查询游戏类型
     *
     * @param typeId 游戏类型主键
     * @return 游戏类型
     */
    @Override
    public BizGameType selectBizGameTypeByTypeId(Long typeId)
    {
        return bizGameTypeMapper.selectBizGameTypeByTypeId(typeId);
    }

    /**
     * 查询游戏类型列表
     *
     * @param bizGameType 游戏类型
     * @return 游戏类型
     */
    @Override
    public List<BizGameType> selectBizGameTypeList(BizGameType bizGameType)
    {
        return bizGameTypeMapper.selectBizGameTypeList(bizGameType);
    }

    /**
     * 新增游戏类型
     *
     * @param bizGameType 游戏类型
     * @return 结果
     */
    @Override
    public int insertBizGameType(BizGameType bizGameType)
    {
        bizGameType.setCreateTime(DateUtils.getNowDate());
        return bizGameTypeMapper.insertBizGameType(bizGameType);
    }

    /**
     * 修改游戏类型
     *
     * @param bizGameType 游戏类型
     * @return 结果
     */
    @Override
    public int updateBizGameType(BizGameType bizGameType)
    {
        bizGameType.setUpdateTime(DateUtils.getNowDate());
        return bizGameTypeMapper.updateBizGameType(bizGameType);
    }

    /**
     * 批量删除游戏类型
     *
     * @param typeIds 需要删除的游戏类型主键
     * @return 结果
     */
    @Override
    public int deleteBizGameTypeByTypeIds(Long[] typeIds)
    {
        return bizGameTypeMapper.deleteBizGameTypeByTypeIds(typeIds);
    }

    /**
     * 删除游戏类型信息
     *
     * @param typeId 游戏类型主键
     * @return 结果
     */
    @Override
    public int deleteBizGameTypeByTypeId(Long typeId)
    {
        return bizGameTypeMapper.deleteBizGameTypeByTypeId(typeId);
    }
}
