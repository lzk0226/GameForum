package com.ruoyi.user.mapper;

import com.ruoyi.user.domain.Section;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SectionMapper {

    /**
     * 查询论坛版块信息
     */
    public Section selectSectionById(Integer sectionId);

    /**
     * 查询论坛版块信息列表
     */
    public List<Section> selectSectionList(Section section);

    /**
     * 根据游戏ID查询版块列表
     */
    public List<Section> selectSectionListByGameId(@Param("gameId") Integer gameId);

    /**
     * 根据版块名称模糊查询
     */
    public List<Section> selectSectionListByName(@Param("sectionName") String sectionName);

    /**
     * 查询热门版块列表 (按显示顺序)
     */
    public List<Section> selectHotSectionList(@Param("limit") Integer limit);

    /**
     * 查询所有版块列表 (按显示顺序)
     */
    public List<Section> selectAllSectionList();
}