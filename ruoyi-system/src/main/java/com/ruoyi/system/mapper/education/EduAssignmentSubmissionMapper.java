package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduAssignmentSubmission;

/**
 * 作业提交Mapper接口
 */
public interface EduAssignmentSubmissionMapper
{
    public EduAssignmentSubmission selectEduAssignmentSubmissionBySubmissionId(Long submissionId);

    public List<EduAssignmentSubmission> selectEduAssignmentSubmissionList(EduAssignmentSubmission submission);

    public int insertEduAssignmentSubmission(EduAssignmentSubmission submission);

    public int updateEduAssignmentSubmission(EduAssignmentSubmission submission);

    public int deleteEduAssignmentSubmissionBySubmissionId(Long submissionId);

    public int deleteEduAssignmentSubmissionBySubmissionIds(Long[] submissionIds);
}
