package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduAssignment;

/**
 * 作业管理Mapper接口
 */
public interface EduAssignmentMapper
{
    public EduAssignment selectEduAssignmentByAssignmentId(Long assignmentId);

    public List<EduAssignment> selectEduAssignmentList(EduAssignment assignment);

    public int insertEduAssignment(EduAssignment assignment);

    public int updateEduAssignment(EduAssignment assignment);

    public int deleteEduAssignmentByAssignmentId(Long assignmentId);

    public int deleteEduAssignmentByAssignmentIds(Long[] assignmentIds);
}
