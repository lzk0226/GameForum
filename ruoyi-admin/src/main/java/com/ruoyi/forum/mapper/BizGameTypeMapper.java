package com.ruoyi.forum.mapper;

import com.ruoyi.forum.domain.BizGameType;

import java.util.List;

/**
 * 游戏类型Mapper接口
 *
 * @author ruoyi
 * @date 2025-05-17
 */
public interface BizGameTypeMapper
{
    /**
     * 查询游戏类型
     *
     * @param typeId 游戏类型主键
     * @return 游戏类型
     */
    public BizGameType selectBizGameTypeByTypeId(Long typeId);

    /**
     * 查询游戏类型列表
     *
     * @param bizGameType 游戏类型
     * @return 游戏类型集合
     */
    public List<BizGameType> selectBizGameTypeList(BizGameType bizGameType);

    /**
     * 新增游戏类型
     *
     * @param bizGameType 游戏类型
     * @return 结果
     */
    public int insertBizGameType(BizGameType bizGameType);

    /**
     * 修改游戏类型
     *
     * @param bizGameType 游戏类型
     * @return 结果
     */
    public int updateBizGameType(BizGameType bizGameType);

    /**
     * 删除游戏类型
     *
     * @param typeId 游戏类型主键
     * @return 结果
     */
    public int deleteBizGameTypeByTypeId(Long typeId);

    /**
     * 批量删除游戏类型
     *
     * @param typeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizGameTypeByTypeIds(Long[] typeIds);
}
