package com.ruoyi.system.domain.education;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 提交附件对象 edu_submission_attachment
 */
public class EduSubmissionAttachment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 提交附件ID */
    @Excel(name = "提交附件ID")
    private Long submissionAttachmentId;

    /** 作业提交ID */
    @Excel(name = "作业提交ID")
    private Long submissionId;

    /** 文件原名称 */
    @Excel(name = "文件原名称")
    private String fileName;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String fileUrl;

    /** 文件后缀 */
    @Excel(name = "文件后缀")
    private String fileSuffix;

    /** 文件大小，单位字节 */
    @Excel(name = "文件大小，单位字节")
    private Long fileSize;

    /** 上传人用户ID */
    @Excel(name = "上传人用户ID")
    private Long uploadUserId;

    public void setSubmissionAttachmentId(Long submissionAttachmentId)
    {
        this.submissionAttachmentId = submissionAttachmentId;
    }

    public Long getSubmissionAttachmentId()
    {
        return submissionAttachmentId;
    }

    public void setSubmissionId(Long submissionId)
    {
        this.submissionId = submissionId;
    }

    public Long getSubmissionId()
    {
        return submissionId;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileSuffix(String fileSuffix)
    {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix()
    {
        return fileSuffix;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setUploadUserId(Long uploadUserId)
    {
        this.uploadUserId = uploadUserId;
    }

    public Long getUploadUserId()
    {
        return uploadUserId;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("submissionAttachmentId", getSubmissionAttachmentId())
            .append("submissionId", getSubmissionId())
            .append("fileName", getFileName())
            .append("fileUrl", getFileUrl())
            .append("fileSuffix", getFileSuffix())
            .append("fileSize", getFileSize())
            .append("uploadUserId", getUploadUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
