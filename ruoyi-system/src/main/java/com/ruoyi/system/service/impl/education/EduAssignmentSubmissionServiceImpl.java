package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduAssignmentSubmission;
import com.ruoyi.system.mapper.education.EduAssignmentSubmissionMapper;
import com.ruoyi.system.service.education.IEduAssignmentSubmissionService;

/**
 * 作业提交Service业务层处理
 */
@Service
public class EduAssignmentSubmissionServiceImpl implements IEduAssignmentSubmissionService
{
    @Autowired
    private EduAssignmentSubmissionMapper submissionMapper;

    @Override
    public EduAssignmentSubmission selectEduAssignmentSubmissionBySubmissionId(Long submissionId)
    {
        return submissionMapper.selectEduAssignmentSubmissionBySubmissionId(submissionId);
    }

    @Override
    public List<EduAssignmentSubmission> selectEduAssignmentSubmissionList(EduAssignmentSubmission submission)
    {
        return submissionMapper.selectEduAssignmentSubmissionList(submission);
    }

    @Override
    public int insertEduAssignmentSubmission(EduAssignmentSubmission submission)
    {
        submission.setCreateTime(DateUtils.getNowDate());
        return submissionMapper.insertEduAssignmentSubmission(submission);
    }

    @Override
    public int updateEduAssignmentSubmission(EduAssignmentSubmission submission)
    {
        submission.setUpdateTime(DateUtils.getNowDate());
        return submissionMapper.updateEduAssignmentSubmission(submission);
    }

    @Override
    public int deleteEduAssignmentSubmissionBySubmissionIds(Long[] submissionIds)
    {
        return submissionMapper.deleteEduAssignmentSubmissionBySubmissionIds(submissionIds);
    }

    @Override
    public int deleteEduAssignmentSubmissionBySubmissionId(Long submissionId)
    {
        return submissionMapper.deleteEduAssignmentSubmissionBySubmissionId(submissionId);
    }
}
