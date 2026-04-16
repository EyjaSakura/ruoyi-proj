package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.StudyLine;

/**
 * 学习路线管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
public interface StudyLineMapper 
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
     * 删除学习路线管理
     * 
     * @param id 学习路线管理主键
     * @return 结果
     */
    public int deleteStudyLineById(Long id);

    /**
     * 批量删除学习路线管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudyLineByIds(Long[] ids);
}
