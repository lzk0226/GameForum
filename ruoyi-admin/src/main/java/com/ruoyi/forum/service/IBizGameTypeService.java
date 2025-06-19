package com.ruoyi.forum.service;

import com.ruoyi.forum.domain.BizGameType;

import java.util.List;

/**
 * 游戏类型Service接口
 *
 * @author ruoyi
 * @date 2025-05-17
 */
public interface IBizGameTypeService
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
     * 批量删除游戏类型
     *
     * @param typeIds 需要删除的游戏类型主键集合
     * @return 结果
     */
    public int deleteBizGameTypeByTypeIds(Long[] typeIds);

    /**
     * 删除游戏类型信息
     *
     * @param typeId 游戏类型主键
     * @return 结果
     */
    public int deleteBizGameTypeByTypeId(Long typeId);
}
