package com.ruoyi.system.domain.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息提醒对象 edu_user_message
 */
public class EduUserMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    @Excel(name = "消息ID")
    private Long messageId;

    /** 接收用户ID */
    @Excel(name = "接收用户ID")
    private Long userId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 作业ID */
    @Excel(name = "作业ID")
    private Long assignmentId;

    /** 公告ID */
    @Excel(name = "公告ID")
    private Long noticeId;

    /** 业务类型（assignment_deadline/assignment_graded/new_assignment/new_course/course_notice） */
    @Excel(name = "业务类型")
    private String bizType;

    /** 消息标题 */
    @Excel(name = "消息标题")
    private String messageTitle;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String messageContent;

    /** 发送人用户ID */
    @Excel(name = "发送人用户ID")
    private Long senderUserId;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /** 已读状态（0未读 1已读） */
    @Excel(name = "已读状态", dictType = "edu_message_read_status")
    private String readStatus;

    /** 已读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "已读时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    /** 页面跳转路径 */
    @Excel(name = "页面跳转路径")
    private String linkPath;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }

    public Long getMessageId()
    {
        return messageId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setAssignmentId(Long assignmentId)
    {
        this.assignmentId = assignmentId;
    }

    public Long getAssignmentId()
    {
        return assignmentId;
    }

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }

    public Long getNoticeId()
    {
        return noticeId;
    }

    public void setBizType(String bizType)
    {
        this.bizType = bizType;
    }

    public String getBizType()
    {
        return bizType;
    }

    public void setMessageTitle(String messageTitle)
    {
        this.messageTitle = messageTitle;
    }

    public String getMessageTitle()
    {
        return messageTitle;
    }

    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }

    public String getMessageContent()
    {
        return messageContent;
    }

    public void setSenderUserId(Long senderUserId)
    {
        this.senderUserId = senderUserId;
    }

    public Long getSenderUserId()
    {
        return senderUserId;
    }

    public void setSendTime(Date sendTime)
    {
        this.sendTime = sendTime;
    }

    public Date getSendTime()
    {
        return sendTime;
    }

    public void setReadStatus(String readStatus)
    {
        this.readStatus = readStatus;
    }

    public String getReadStatus()
    {
        return readStatus;
    }

    public void setReadTime(Date readTime)
    {
        this.readTime = readTime;
    }

    public Date getReadTime()
    {
        return readTime;
    }

    public void setLinkPath(String linkPath)
    {
        this.linkPath = linkPath;
    }

    public String getLinkPath()
    {
        return linkPath;
    }

    public void setExpireTime(Date expireTime)
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime()
    {
        return expireTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("messageId", getMessageId())
            .append("userId", getUserId())
            .append("courseId", getCourseId())
            .append("assignmentId", getAssignmentId())
            .append("noticeId", getNoticeId())
            .append("bizType", getBizType())
            .append("messageTitle", getMessageTitle())
            .append("messageContent", getMessageContent())
            .append("senderUserId", getSenderUserId())
            .append("sendTime", getSendTime())
            .append("readStatus", getReadStatus())
            .append("readTime", getReadTime())
            .append("linkPath", getLinkPath())
            .append("expireTime", getExpireTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
