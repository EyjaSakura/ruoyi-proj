package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StudyLineMapper;
import com.ruoyi.system.domain.StudyLine;
import com.ruoyi.system.service.IStudyLineService;

/**
 * 学习路线管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
@Service
public class StudyLineServiceImpl implements IStudyLineService 
{
    @Autowired
    private StudyLineMapper studyLineMapper;

    /**
     * 查询学习路线管理
     * 
     * @param id 学习路线管理主键
     * @return 学习路线管理
     */
    @Override
    public StudyLine selectStudyLineById(Long id)
    {
        return studyLineMapper.selectStudyLineById(id);
    }

    /**
     * 查询学习路线管理列表
     * 
     * @param studyLine 学习路线管理
     * @return 学习路线管理
     */
    @Override
    public List<StudyLine> selectStudyLineList(StudyLine studyLine)
    {
        return studyLineMapper.selectStudyLineList(studyLine);
    }

    /**
     * 新增学习路线管理
     * 
     * @param studyLine 学习路线管理
     * @return 结果
     */
    @Override
    public int insertStudyLine(StudyLine studyLine)
    {
        return studyLineMapper.insertStudyLine(studyLine);
    }

    /**
     * 修改学习路线管理
     * 
     * @param studyLine 学习路线管理
     * @return 结果
     */
    @Override
    public int updateStudyLine(StudyLine studyLine)
    {
        return studyLineMapper.updateStudyLine(studyLine);
    }

    /**
     * 批量删除学习路线管理
     * 
     * @param ids 需要删除的学习路线管理主键
     * @return 结果
     */
    @Override
    public int deleteStudyLineByIds(Long[] ids)
    {
        return studyLineMapper.deleteStudyLineByIds(ids);
    }

    /**
     * 删除学习路线管理信息
     * 
     * @param id 学习路线管理主键
     * @return 结果
     */
    @Override
    public int deleteStudyLineById(Long id)
    {
        return studyLineMapper.deleteStudyLineById(id);
    }
}
