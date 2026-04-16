package com.ruoyi.system.domain.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 学习进度对象 edu_learning_progress
 */
public class EduLearningProgress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学习进度ID */
    @Excel(name = "学习进度ID")
    private Long progressId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 章节ID */
    @Excel(name = "章节ID")
    private Long chapterId;

    /** 学生用户ID */
    @Excel(name = "学生用户ID")
    private Long studentUserId;

    /** 累计学习时长（分钟） */
    @Excel(name = "累计学习时长")
    private Integer learnMinutes;

    /** 章节完成进度 */
    @Excel(name = "章节完成进度")
    private BigDecimal progressPercent;

    /** 是否完成（0否 1是） */
    @Excel(name = "是否完成", dictType = "edu_progress_completed_flag")
    private String completedFlag;

    /** 首次学习时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "首次学习时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date firstStudyTime;

    /** 最近学习时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最近学习时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastStudyTime;

    public void setProgressId(Long progressId)
    {
        this.progressId = progressId;
    }

    public Long getProgressId()
    {
        return progressId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setChapterId(Long chapterId)
    {
        this.chapterId = chapterId;
    }

    public Long getChapterId()
    {
        return chapterId;
    }

    public void setStudentUserId(Long studentUserId)
    {
        this.studentUserId = studentUserId;
    }

    public Long getStudentUserId()
    {
        return studentUserId;
    }

    public void setLearnMinutes(Integer learnMinutes)
    {
        this.learnMinutes = learnMinutes;
    }

    public Integer getLearnMinutes()
    {
        return learnMinutes;
    }

    public void setProgressPercent(BigDecimal progressPercent)
    {
        this.progressPercent = progressPercent;
    }

    public BigDecimal getProgressPercent()
    {
        return progressPercent;
    }

    public void setCompletedFlag(String completedFlag)
    {
        this.completedFlag = completedFlag;
    }

    public String getCompletedFlag()
    {
        return completedFlag;
    }

    public void setFirstStudyTime(Date firstStudyTime)
    {
        this.firstStudyTime = firstStudyTime;
    }

    public Date getFirstStudyTime()
    {
        return firstStudyTime;
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
            .append("progressId", getProgressId())
            .append("courseId", getCourseId())
            .append("chapterId", getChapterId())
            .append("studentUserId", getStudentUserId())
            .append("learnMinutes", getLearnMinutes())
            .append("progressPercent", getProgressPercent())
            .append("completedFlag", getCompletedFlag())
            .append("firstStudyTime", getFirstStudyTime())
            .append("lastStudyTime", getLastStudyTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
