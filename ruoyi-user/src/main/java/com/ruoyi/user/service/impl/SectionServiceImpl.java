package com.ruoyi.user.service.impl;

import com.ruoyi.user.domain.Section;
import com.ruoyi.user.mapper.SectionMapper;
import com.ruoyi.user.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/5/24下午 7:04
 * @Author : SockLightDust
 */
@Service
public class SectionServiceImpl implements ISectionService {

    @Autowired
    private SectionMapper sectionMapper;

    /**
     * 查询论坛版块信息
     */
    @Override
    public Section selectSectionById(Integer sectionId) {
        return sectionMapper.selectSectionById(sectionId);
    }

    /**
     * 查询论坛版块信息列表
     */
    @Override
    public List<Section> selectSectionList(Section section) {
        return sectionMapper.selectSectionList(section);
    }

    /**
     * 根据游戏ID查询版块列表
     */
    @Override
    public List<Section> selectSectionListByGameId(Integer gameId) {
        return sectionMapper.selectSectionListByGameId(gameId);
    }

    /**
     * 根据版块名称模糊查询
     */
    @Override
    public List<Section> selectSectionListByName(String sectionName) {
        return sectionMapper.selectSectionListByName(sectionName);
    }

    /**
     * 查询热门版块列表
     */
    @Override
    public List<Section> selectHotSectionList(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10; // 默认查询10条
        }
        return sectionMapper.selectHotSectionList(limit);
    }

    /**
     * 查询所有版块列表
     */
    @Override
    public List<Section> selectAllSectionList() {
        return sectionMapper.selectAllSectionList();
    }
}