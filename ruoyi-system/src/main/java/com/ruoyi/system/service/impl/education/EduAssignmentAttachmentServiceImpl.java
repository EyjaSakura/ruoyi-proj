package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduAssignmentAttachment;
import com.ruoyi.system.mapper.education.EduAssignmentAttachmentMapper;
import com.ruoyi.system.service.education.IEduAssignmentAttachmentService;

/**
 * 作业附件Service业务层处理
 */
@Service
public class EduAssignmentAttachmentServiceImpl implements IEduAssignmentAttachmentService
{
    @Autowired
    private EduAssignmentAttachmentMapper assignmentAttachmentMapper;

    @Override
    public EduAssignmentAttachment selectEduAssignmentAttachmentByAssignmentAttachmentId(Long assignmentAttachmentId)
    {
        return assignmentAttachmentMapper.selectEduAssignmentAttachmentByAssignmentAttachmentId(assignmentAttachmentId);
    }

    @Override
    public List<EduAssignmentAttachment> selectEduAssignmentAttachmentList(EduAssignmentAttachment assignmentAttachment)
    {
        return assignmentAttachmentMapper.selectEduAssignmentAttachmentList(assignmentAttachment);
    }

    @Override
    public int insertEduAssignmentAttachment(EduAssignmentAttachment assignmentAttachment)
    {
        assignmentAttachment.setCreateTime(DateUtils.getNowDate());
        return assignmentAttachmentMapper.insertEduAssignmentAttachment(assignmentAttachment);
    }

    @Override
    public int updateEduAssignmentAttachment(EduAssignmentAttachment assignmentAttachment)
    {
        assignmentAttachment.setUpdateTime(DateUtils.getNowDate());
        return assignmentAttachmentMapper.updateEduAssignmentAttachment(assignmentAttachment);
    }

    @Override
    public int deleteEduAssignmentAttachmentByAssignmentAttachmentIds(Long[] assignmentAttachmentIds)
    {
        return assignmentAttachmentMapper.deleteEduAssignmentAttachmentByAssignmentAttachmentIds(assignmentAttachmentIds);
    }

    @Override
    public int deleteEduAssignmentAttachmentByAssignmentAttachmentId(Long assignmentAttachmentId)
    {
        return assignmentAttachmentMapper.deleteEduAssignmentAttachmentByAssignmentAttachmentId(assignmentAttachmentId);
    }
}
