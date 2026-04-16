package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduSubmissionAttachment;
import com.ruoyi.system.mapper.education.EduSubmissionAttachmentMapper;
import com.ruoyi.system.service.education.IEduSubmissionAttachmentService;

/**
 * 提交附件Service业务层处理
 */
@Service
public class EduSubmissionAttachmentServiceImpl implements IEduSubmissionAttachmentService
{
    @Autowired
    private EduSubmissionAttachmentMapper submissionAttachmentMapper;

    @Override
    public EduSubmissionAttachment selectEduSubmissionAttachmentBySubmissionAttachmentId(Long submissionAttachmentId)
    {
        return submissionAttachmentMapper.selectEduSubmissionAttachmentBySubmissionAttachmentId(submissionAttachmentId);
    }

    @Override
    public List<EduSubmissionAttachment> selectEduSubmissionAttachmentList(EduSubmissionAttachment submissionAttachment)
    {
        return submissionAttachmentMapper.selectEduSubmissionAttachmentList(submissionAttachment);
    }

    @Override
    public int insertEduSubmissionAttachment(EduSubmissionAttachment submissionAttachment)
    {
        submissionAttachment.setCreateTime(DateUtils.getNowDate());
        return submissionAttachmentMapper.insertEduSubmissionAttachment(submissionAttachment);
    }

    @Override
    public int updateEduSubmissionAttachment(EduSubmissionAttachment submissionAttachment)
    {
        submissionAttachment.setUpdateTime(DateUtils.getNowDate());
        return submissionAttachmentMapper.updateEduSubmissionAttachment(submissionAttachment);
    }

    @Override
    public int deleteEduSubmissionAttachmentBySubmissionAttachmentIds(Long[] submissionAttachmentIds)
    {
        return submissionAttachmentMapper.deleteEduSubmissionAttachmentBySubmissionAttachmentIds(submissionAttachmentIds);
    }

    @Override
    public int deleteEduSubmissionAttachmentBySubmissionAttachmentId(Long submissionAttachmentId)
    {
        return submissionAttachmentMapper.deleteEduSubmissionAttachmentBySubmissionAttachmentId(submissionAttachmentId);
    }
}
