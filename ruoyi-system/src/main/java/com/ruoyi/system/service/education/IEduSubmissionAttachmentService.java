package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduSubmissionAttachment;

/**
 * 提交附件Service接口
 */
public interface IEduSubmissionAttachmentService
{
    public EduSubmissionAttachment selectEduSubmissionAttachmentBySubmissionAttachmentId(Long submissionAttachmentId);

    public List<EduSubmissionAttachment> selectEduSubmissionAttachmentList(EduSubmissionAttachment submissionAttachment);

    public int insertEduSubmissionAttachment(EduSubmissionAttachment submissionAttachment);

    public int updateEduSubmissionAttachment(EduSubmissionAttachment submissionAttachment);

    public int deleteEduSubmissionAttachmentBySubmissionAttachmentIds(Long[] submissionAttachmentIds);

    public int deleteEduSubmissionAttachmentBySubmissionAttachmentId(Long submissionAttachmentId);
}
