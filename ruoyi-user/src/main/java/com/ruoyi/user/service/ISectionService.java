package com.ruoyi.user.service;

import com.ruoyi.user.domain.Section;

import java.util.List;

public interface ISectionService {

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
    public List<Section> selectSectionListByGameId(Integer gameId);

    /**
     * 根据版块名称模糊查询
     */
    public List<Section> selectSectionListByName(String sectionName);

    /**
     * 查询热门版块列表
     */
    public List<Section> selectHotSectionList(Integer limit);

    /**
     * 查询所有版块列表
     */
    public List<Section> selectAllSectionList();
}
