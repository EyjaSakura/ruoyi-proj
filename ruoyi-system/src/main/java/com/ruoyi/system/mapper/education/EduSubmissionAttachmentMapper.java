package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduSubmissionAttachment;

/**
 * 提交附件Mapper接口
 */
public interface EduSubmissionAttachmentMapper
{
    public EduSubmissionAttachment selectEduSubmissionAttachmentBySubmissionAttachmentId(Long submissionAttachmentId);

    public List<EduSubmissionAttachment> selectEduSubmissionAttachmentList(EduSubmissionAttachment submissionAttachment);

    public int insertEduSubmissionAttachment(EduSubmissionAttachment submissionAttachment);

    public int updateEduSubmissionAttachment(EduSubmissionAttachment submissionAttachment);

    public int deleteEduSubmissionAttachmentBySubmissionAttachmentId(Long submissionAttachmentId);

    public int deleteEduSubmissionAttachmentBySubmissionAttachmentIds(Long[] submissionAttachmentIds);
}
