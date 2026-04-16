package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduAssignmentSubmission;

/**
 * 作业提交Service接口
 */
public interface IEduAssignmentSubmissionService
{
    public EduAssignmentSubmission selectEduAssignmentSubmissionBySubmissionId(Long submissionId);

    public List<EduAssignmentSubmission> selectEduAssignmentSubmissionList(EduAssignmentSubmission submission);

    public int insertEduAssignmentSubmission(EduAssignmentSubmission submission);

    public int updateEduAssignmentSubmission(EduAssignmentSubmission submission);

    public int deleteEduAssignmentSubmissionBySubmissionIds(Long[] submissionIds);

    public int deleteEduAssignmentSubmissionBySubmissionId(Long submissionId);
}
