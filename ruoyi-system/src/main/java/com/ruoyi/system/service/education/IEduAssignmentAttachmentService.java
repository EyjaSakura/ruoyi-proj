package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduAssignmentAttachment;

/**
 * 作业附件Service接口
 */
public interface IEduAssignmentAttachmentService
{
    public EduAssignmentAttachment selectEduAssignmentAttachmentByAssignmentAttachmentId(Long assignmentAttachmentId);

    public List<EduAssignmentAttachment> selectEduAssignmentAttachmentList(EduAssignmentAttachment assignmentAttachment);

    public int insertEduAssignmentAttachment(EduAssignmentAttachment assignmentAttachment);

    public int updateEduAssignmentAttachment(EduAssignmentAttachment assignmentAttachment);

    public int deleteEduAssignmentAttachmentByAssignmentAttachmentIds(Long[] assignmentAttachmentIds);

    public int deleteEduAssignmentAttachmentByAssignmentAttachmentId(Long assignmentAttachmentId);
}
