package com.ruoyi.system.domain.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 课程公告对象 edu_course_notice
 */
public class EduCourseNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    @Excel(name = "公告ID")
    private Long noticeId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 公告标题 */
    @Excel(name = "公告标题")
    private String title;

    /** 公告内容 */
    @Excel(name = "公告内容")
    private String content;

    /** 公告类型（1课程公告 2课程提醒） */
    @Excel(name = "公告类型", dictType = "edu_notice_type")
    private String noticeType;

    /** 是否置顶（0否 1是） */
    @Excel(name = "是否置顶", dictType = "sys_yes_no")
    private String topFlag;

    /** 发布人用户ID */
    @Excel(name = "发布人用户ID")
    private Long publishUserId;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 状态（0草稿 1已发布 2已撤回） */
    @Excel(name = "状态", dictType = "edu_notice_status")
    private String status;

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }

    public Long getNoticeId()
    {
        return noticeId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setNoticeType(String noticeType)
    {
        this.noticeType = noticeType;
    }

    public String getNoticeType()
    {
        return noticeType;
    }

    public void setTopFlag(String topFlag)
    {
        this.topFlag = topFlag;
    }

    public String getTopFlag()
    {
        return topFlag;
    }

    public void setPublishUserId(Long publishUserId)
    {
        this.publishUserId = publishUserId;
    }

    public Long getPublishUserId()
    {
        return publishUserId;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("courseId", getCourseId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("noticeType", getNoticeType())
            .append("topFlag", getTopFlag())
            .append("publishUserId", getPublishUserId())
            .append("publishTime", getPublishTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
