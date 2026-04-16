package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduAssignment;
import com.ruoyi.system.mapper.education.EduAssignmentMapper;
import com.ruoyi.system.service.education.IEduAssignmentService;

/**
 * 作业管理Service业务层处理
 */
@Service
public class EduAssignmentServiceImpl implements IEduAssignmentService
{
    @Autowired
    private EduAssignmentMapper assignmentMapper;

    @Override
    public EduAssignment selectEduAssignmentByAssignmentId(Long assignmentId)
    {
        return assignmentMapper.selectEduAssignmentByAssignmentId(assignmentId);
    }

    @Override
    public List<EduAssignment> selectEduAssignmentList(EduAssignment assignment)
    {
        return assignmentMapper.selectEduAssignmentList(assignment);
    }

    @Override
    public int insertEduAssignment(EduAssignment assignment)
    {
        assignment.setCreateTime(DateUtils.getNowDate());
        return assignmentMapper.insertEduAssignment(assignment);
    }

    @Override
    public int updateEduAssignment(EduAssignment assignment)
    {
        assignment.setUpdateTime(DateUtils.getNowDate());
        return assignmentMapper.updateEduAssignment(assignment);
    }

    @Override
    public int deleteEduAssignmentByAssignmentIds(Long[] assignmentIds)
    {
        return assignmentMapper.deleteEduAssignmentByAssignmentIds(assignmentIds);
    }

    @Override
    public int deleteEduAssignmentByAssignmentId(Long assignmentId)
    {
        return assignmentMapper.deleteEduAssignmentByAssignmentId(assignmentId);
    }
}
