package com.ruoyi.system.domain.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 作业提交对象 edu_assignment_submission
 */
public class EduAssignmentSubmission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 作业提交ID */
    @Excel(name = "作业提交ID")
    private Long submissionId;

    /** 作业ID */
    @Excel(name = "作业ID")
    private Long assignmentId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 学生用户ID */
    @Excel(name = "学生用户ID")
    private Long studentUserId;

    /** 第几次提交 */
    @Excel(name = "第几次提交")
    private Integer submitRound;

    /** 提交说明 */
    @Excel(name = "提交说明")
    private String submitRemark;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提交时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 是否逾期（0否 1是） */
    @Excel(name = "是否逾期", dictType = "sys_yes_no")
    private String lateFlag;

    /** 是否当前最新提交（0否 1是） */
    @Excel(name = "是否当前最新提交", dictType = "sys_yes_no")
    private String isLatest;

    /** 批改状态（0未批改 1已批改） */
    @Excel(name = "批改状态", dictType = "edu_review_status")
    private String reviewStatus;

    /** 得分 */
    @Excel(name = "得分")
    private BigDecimal score;

    /** 教师评语 */
    @Excel(name = "教师评语")
    private String teacherComment;

    /** 批改教师用户ID */
    @Excel(name = "批改教师用户ID")
    private Long reviewUserId;

    /** 批改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "批改时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    public void setSubmissionId(Long submissionId)
    {
        this.submissionId = submissionId;
    }

    public Long getSubmissionId()
    {
        return submissionId;
    }

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

    public void setStudentUserId(Long studentUserId)
    {
        this.studentUserId = studentUserId;
    }

    public Long getStudentUserId()
    {
        return studentUserId;
    }

    public void setSubmitRound(Integer submitRound)
    {
        this.submitRound = submitRound;
    }

    public Integer getSubmitRound()
    {
        return submitRound;
    }

    public void setSubmitRemark(String submitRemark)
    {
        this.submitRemark = submitRemark;
    }

    public String getSubmitRemark()
    {
        return submitRemark;
    }

    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
    }

    public Date getSubmitTime()
    {
        return submitTime;
    }

    public void setLateFlag(String lateFlag)
    {
        this.lateFlag = lateFlag;
    }

    public String getLateFlag()
    {
        return lateFlag;
    }

    public void setIsLatest(String isLatest)
    {
        this.isLatest = isLatest;
    }

    public String getIsLatest()
    {
        return isLatest;
    }

    public void setReviewStatus(String reviewStatus)
    {
        this.reviewStatus = reviewStatus;
    }

    public String getReviewStatus()
    {
        return reviewStatus;
    }

    public void setScore(BigDecimal score)
    {
        this.score = score;
    }

    public BigDecimal getScore()
    {
        return score;
    }

    public void setTeacherComment(String teacherComment)
    {
        this.teacherComment = teacherComment;
    }

    public String getTeacherComment()
    {
        return teacherComment;
    }

    public void setReviewUserId(Long reviewUserId)
    {
        this.reviewUserId = reviewUserId;
    }

    public Long getReviewUserId()
    {
        return reviewUserId;
    }

    public void setReviewTime(Date reviewTime)
    {
        this.reviewTime = reviewTime;
    }

    public Date getReviewTime()
    {
        return reviewTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("submissionId", getSubmissionId())
            .append("assignmentId", getAssignmentId())
            .append("courseId", getCourseId())
            .append("studentUserId", getStudentUserId())
            .append("submitRound", getSubmitRound())
            .append("submitRemark", getSubmitRemark())
            .append("submitTime", getSubmitTime())
            .append("lateFlag", getLateFlag())
            .append("isLatest", getIsLatest())
            .append("reviewStatus", getReviewStatus())
            .append("score", getScore())
            .append("teacherComment", getTeacherComment())
            .append("reviewUserId", getReviewUserId())
            .append("reviewTime", getReviewTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
