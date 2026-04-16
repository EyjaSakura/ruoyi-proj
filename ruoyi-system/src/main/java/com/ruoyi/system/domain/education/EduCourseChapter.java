package com.ruoyi.system.domain.education;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 课程章节对象 edu_course_chapter
 */
public class EduCourseChapter extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 章节ID */
    @Excel(name = "章节ID")
    private Long chapterId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 父章节ID */
    @Excel(name = "父章节ID")
    private Long parentId;

    /** 祖级列表 */
    @Excel(name = "祖级列表")
    private String ancestors;

    /** 章节标题 */
    @Excel(name = "章节标题")
    private String chapterTitle;

    /** 章节类型（1章 2节） */
    @Excel(name = "章节类型")
    private String chapterType;

    /** 章节说明 */
    @Excel(name = "章节说明")
    private String chapterDesc;

    /** 排序号 */
    @Excel(name = "排序号")
    private Integer orderNum;

    /** 建议学习时长（分钟） */
    @Excel(name = "建议学习时长")
    private Integer durationMinutes;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", dictType = "sys_normal_disable")
    private String status;

    public void setChapterId(Long chapterId)
    {
        this.chapterId = chapterId;
    }

    public Long getChapterId()
    {
        return chapterId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setChapterTitle(String chapterTitle)
    {
        this.chapterTitle = chapterTitle;
    }

    public String getChapterTitle()
    {
        return chapterTitle;
    }

    public void setChapterType(String chapterType)
    {
        this.chapterType = chapterType;
    }

    public String getChapterType()
    {
        return chapterType;
    }

    public void setChapterDesc(String chapterDesc)
    {
        this.chapterDesc = chapterDesc;
    }

    public String getChapterDesc()
    {
        return chapterDesc;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setDurationMinutes(Integer durationMinutes)
    {
        this.durationMinutes = durationMinutes;
    }

    public Integer getDurationMinutes()
    {
        return durationMinutes;
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
            .append("chapterId", getChapterId())
            .append("courseId", getCourseId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("chapterTitle", getChapterTitle())
            .append("chapterType", getChapterType())
            .append("chapterDesc", getChapterDesc())
            .append("orderNum", getOrderNum())
            .append("durationMinutes", getDurationMinutes())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
