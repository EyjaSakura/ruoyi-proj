package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学习路线管理对象 study_line
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
public class StudyLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学习路线编号 */
    private Long id;

    /** 课程类型 */
    @Excel(name = "课程类型")
    private String courseType;

    /** 课程阶段 */
    @Excel(name = "课程阶段")
    private String courseStage;

    /** 课程详细内容 */
    @Excel(name = "课程详细内容")
    private String courseStageInfo;

    /** 课程阶段排序 */
    @Excel(name = "课程阶段排序")
    private String courseStageOrder;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCourseType(String courseType) 
    {
        this.courseType = courseType;
    }

    public String getCourseType() 
    {
        return courseType;
    }
    public void setCourseStage(String courseStage) 
    {
        this.courseStage = courseStage;
    }

    public String getCourseStage() 
    {
        return courseStage;
    }
    public void setCourseStageInfo(String courseStageInfo) 
    {
        this.courseStageInfo = courseStageInfo;
    }

    public String getCourseStageInfo() 
    {
        return courseStageInfo;
    }
    public void setCourseStageOrder(String courseStageOrder) 
    {
        this.courseStageOrder = courseStageOrder;
    }

    public String getCourseStageOrder() 
    {
        return courseStageOrder;
    }
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }
    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("courseType", getCourseType())
            .append("courseStage", getCourseStage())
            .append("courseStageInfo", getCourseStageInfo())
            .append("courseStageOrder", getCourseStageOrder())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
