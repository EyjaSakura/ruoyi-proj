package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.StudyLine;

/**
 * 学习路线管理Service接口
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
public interface IStudyLineService 
{
    /**
     * 查询学习路线管理
     * 
     * @param id 学习路线管理主键
     * @return 学习路线管理
     */
    public StudyLine selectStudyLineById(Long id);

    /**
     * 查询学习路线管理列表
     * 
     * @param studyLine 学习路线管理
     * @return 学习路线管理集合
     */
    public List<StudyLine> selectStudyLineList(StudyLine studyLine);

    /**
     * 新增学习路线管理
     * 
     * @param studyLine 学习路线管理
     * @return 结果
     */
    public int insertStudyLine(StudyLine studyLine);

    /**
     * 修改学习路线管理
     * 
     * @param studyLine 学习路线管理
     * @return 结果
     */
    public int updateStudyLine(StudyLine studyLine);

    /**
     * 批量删除学习路线管理
     * 
     * @param ids 需要删除的学习路线管理主键集合
     * @return 结果
     */
    public int deleteStudyLineByIds(Long[] ids);

    /**
     * 删除学习路线管理信息
     * 
     * @param id 学习路线管理主键
     * @return 结果
     */
    public int deleteStudyLineById(Long id);
}
