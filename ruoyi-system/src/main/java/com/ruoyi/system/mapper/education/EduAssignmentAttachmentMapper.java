package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduAssignmentAttachment;

/**
 * 作业附件Mapper接口
 */
public interface EduAssignmentAttachmentMapper
{
    public EduAssignmentAttachment selectEduAssignmentAttachmentByAssignmentAttachmentId(Long assignmentAttachmentId);

    public List<EduAssignmentAttachment> selectEduAssignmentAttachmentList(EduAssignmentAttachment assignmentAttachment);

    public int insertEduAssignmentAttachment(EduAssignmentAttachment assignmentAttachment);

    public int updateEduAssignmentAttachment(EduAssignmentAttachment assignmentAttachment);

    public int deleteEduAssignmentAttachmentByAssignmentAttachmentId(Long assignmentAttachmentId);

    public int deleteEduAssignmentAttachmentByAssignmentAttachmentIds(Long[] assignmentAttachmentIds);
}
