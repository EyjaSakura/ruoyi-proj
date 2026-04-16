package com.ruoyi.system.domain.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 选课管理对象 edu_course_enrollment
 */
public class EduCourseEnrollment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 选课记录ID */
    @Excel(name = "选课记录ID")
    private Long enrollmentId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 学期ID */
    @Excel(name = "学期ID")
    private Long termId;

    /** 学生用户ID，对应sys_user.user_id */
    @Excel(name = "学生用户ID，对应sys_user.user_id")
    private Long studentUserId;

    /** 来源类型（1学生自选 2管理员分配） */
    @Excel(name = "来源类型", dictType = "edu_enroll_source_type")
    private String sourceType;

    /** 选课时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "选课时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date enrollTime;

    /** 退课时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "退课时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dropTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "完成时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /** 选课状态（1已选 2已退课 3已完成） */
    @Excel(name = "选课状态", dictType = "edu_enroll_status")
    private String enrollStatus;

    /** 课程完成进度 */
    @Excel(name = "课程完成进度")
    private BigDecimal progressPercent;

    /** 应完成任务数 */
    @Excel(name = "应完成任务数")
    private Integer requiredTaskCount;

    /** 已完成任务数 */
    @Excel(name = "已完成任务数")
    private Integer finishedTaskCount;

    /** 最近学习时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最近学习时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastStudyTime;

    public void setEnrollmentId(Long enrollmentId)
    {
        this.enrollmentId = enrollmentId;
    }

    public Long getEnrollmentId()
    {
        return enrollmentId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setTermId(Long termId)
    {
        this.termId = termId;
    }

    public Long getTermId()
    {
        return termId;
    }

    public void setStudentUserId(Long studentUserId)
    {
        this.studentUserId = studentUserId;
    }

    public Long getStudentUserId()
    {
        return studentUserId;
    }

    public void setSourceType(String sourceType)
    {
        this.sourceType = sourceType;
    }

    public String getSourceType()
    {
        return sourceType;
    }

    public void setEnrollTime(Date enrollTime)
    {
        this.enrollTime = enrollTime;
    }

    public Date getEnrollTime()
    {
        return enrollTime;
    }

    public void setDropTime(Date dropTime)
    {
        this.dropTime = dropTime;
    }

    public Date getDropTime()
    {
        return dropTime;
    }

    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }

    public Date getFinishTime()
    {
        return finishTime;
    }

    public void setEnrollStatus(String enrollStatus)
    {
        this.enrollStatus = enrollStatus;
    }

    public String getEnrollStatus()
    {
        return enrollStatus;
    }

    public void setProgressPercent(BigDecimal progressPercent)
    {
        this.progressPercent = progressPercent;
    }

    public BigDecimal getProgressPercent()
    {
        return progressPercent;
    }

    public void setRequiredTaskCount(Integer requiredTaskCount)
    {
        this.requiredTaskCount = requiredTaskCount;
    }

    public Integer getRequiredTaskCount()
    {
        return requiredTaskCount;
    }

    public void setFinishedTaskCount(Integer finishedTaskCount)
    {
        this.finishedTaskCount = finishedTaskCount;
    }

    public Integer getFinishedTaskCount()
    {
        return finishedTaskCount;
    }

    public void setLastStudyTime(Date lastStudyTime)
    {
        this.lastStudyTime = lastStudyTime;
    }

    public Date getLastStudyTime()
    {
        return lastStudyTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("enrollmentId", getEnrollmentId())
            .append("courseId", getCourseId())
            .append("termId", getTermId())
            .append("studentUserId", getStudentUserId())
            .append("sourceType", getSourceType())
            .append("enrollTime", getEnrollTime())
            .append("dropTime", getDropTime())
            .append("finishTime", getFinishTime())
            .append("enrollStatus", getEnrollStatus())
            .append("progressPercent", getProgressPercent())
            .append("requiredTaskCount", getRequiredTaskCount())
            .append("finishedTaskCount", getFinishedTaskCount())
            .append("lastStudyTime", getLastStudyTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
