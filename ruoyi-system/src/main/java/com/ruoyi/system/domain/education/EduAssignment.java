package com.ruoyi.system.domain.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 作业管理对象 edu_assignment
 */
public class EduAssignment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 作业ID */
    @Excel(name = "作业ID")
    private Long assignmentId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 作业标题 */
    @Excel(name = "作业标题")
    private String title;

    /** 作业类型（1普通作业 2实验作业 3课程设计） */
    @Excel(name = "作业类型", dictType = "edu_assignment_type")
    private String assignmentType;

    /** 作业要求 */
    @Excel(name = "作业要求")
    private String content;

    /** 总分 */
    @Excel(name = "总分")
    private BigDecimal totalScore;

    /** 最大提交次数，0表示不限 */
    @Excel(name = "最大提交次数，0表示不限")
    private Integer submitLimit;

    /** 是否允许重交（0否 1是） */
    @Excel(name = "是否允许重交", dictType = "sys_yes_no")
    private String allowResubmit;

    /** 截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "截止时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deadlineTime;

    /** 发布状态（0草稿 1已发布 2已关闭） */
    @Excel(name = "发布状态", dictType = "edu_assignment_publish_status")
    private String publishStatus;

    /** 发布人用户ID */
    @Excel(name = "发布人用户ID")
    private Long publishUserId;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    public void setAssignmentId(Long assignmentId)
    {
        this.assignmentId = assignmentId;
    }

    public Long getAssignmentId()
    {
        return assignmentId;
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

    public void setAssignmentType(String assignmentType)
    {
        this.assignmentType = assignmentType;
    }

    public String getAssignmentType()
    {
        return assignmentType;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setTotalScore(BigDecimal totalScore)
    {
        this.totalScore = totalScore;
    }

    public BigDecimal getTotalScore()
    {
        return totalScore;
    }

    public void setSubmitLimit(Integer submitLimit)
    {
        this.submitLimit = submitLimit;
    }

    public Integer getSubmitLimit()
    {
        return submitLimit;
    }

    public void setAllowResubmit(String allowResubmit)
    {
        this.allowResubmit = allowResubmit;
    }

    public String getAllowResubmit()
    {
        return allowResubmit;
    }

    public void setDeadlineTime(Date deadlineTime)
    {
        this.deadlineTime = deadlineTime;
    }

    public Date getDeadlineTime()
    {
        return deadlineTime;
    }

    public void setPublishStatus(String publishStatus)
    {
        this.publishStatus = publishStatus;
    }

    public String getPublishStatus()
    {
        return publishStatus;
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

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("assignmentId", getAssignmentId())
            .append("courseId", getCourseId())
            .append("title", getTitle())
            .append("assignmentType", getAssignmentType())
            .append("content", getContent())
            .append("totalScore", getTotalScore())
            .append("submitLimit", getSubmitLimit())
            .append("allowResubmit", getAllowResubmit())
            .append("deadlineTime", getDeadlineTime())
            .append("publishStatus", getPublishStatus())
            .append("publishUserId", getPublishUserId())
            .append("publishTime", getPublishTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
