package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduAssignment;

/**
 * 作业管理Service接口
 */
public interface IEduAssignmentService
{
    public EduAssignment selectEduAssignmentByAssignmentId(Long assignmentId);

    public List<EduAssignment> selectEduAssignmentList(EduAssignment assignment);

    public int insertEduAssignment(EduAssignment assignment);

    public int updateEduAssignment(EduAssignment assignment);

    public int deleteEduAssignmentByAssignmentIds(Long[] assignmentIds);

    public int deleteEduAssignmentByAssignmentId(Long assignmentId);
}
