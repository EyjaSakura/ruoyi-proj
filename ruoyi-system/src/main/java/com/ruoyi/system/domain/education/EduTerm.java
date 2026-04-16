package com.ruoyi.system.domain.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 学期管理对象 edu_term
 */
public class EduTerm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学期ID */
    @Excel(name = "学期ID")
    private Long termId;

    /** 学期编码 */
    @Excel(name = "学期编码")
    private String termCode;

    /** 学期名称 */
    @Excel(name = "学期名称")
    private String termName;

    /** 学年，例如2025-2026 */
    @Excel(name = "学年，例如2025-2026")
    private String schoolYear;

    /** 学期序号（1第一学期 2第二学期 3第三学期） */
    @Excel(name = "学期序号")
    private String semesterNo;

    /** 开学日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开学日期", dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 选课开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "选课开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date selectStartTime;

    /** 选课结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "选课结束时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date selectEndTime;

    /** 状态（0未开始 1进行中 2已结束） */
    @Excel(name = "状态", dictType = "edu_term_status")
    private String status;

    /** 删除标志（0存在，2删除） */
    private String delFlag;

    public void setTermId(Long termId)
    {
        this.termId = termId;
    }

    public Long getTermId()
    {
        return termId;
    }

    public void setTermCode(String termCode)
    {
        this.termCode = termCode;
    }

    public String getTermCode()
    {
        return termCode;
    }

    public void setTermName(String termName)
    {
        this.termName = termName;
    }

    public String getTermName()
    {
        return termName;
    }

    public void setSchoolYear(String schoolYear)
    {
        this.schoolYear = schoolYear;
    }

    public String getSchoolYear()
    {
        return schoolYear;
    }

    public void setSemesterNo(String semesterNo)
    {
        this.semesterNo = semesterNo;
    }

    public String getSemesterNo()
    {
        return semesterNo;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setSelectStartTime(Date selectStartTime)
    {
        this.selectStartTime = selectStartTime;
    }

    public Date getSelectStartTime()
    {
        return selectStartTime;
    }

    public void setSelectEndTime(Date selectEndTime)
    {
        this.selectEndTime = selectEndTime;
    }

    public Date getSelectEndTime()
    {
        return selectEndTime;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("termId", getTermId())
            .append("termCode", getTermCode())
            .append("termName", getTermName())
            .append("schoolYear", getSchoolYear())
            .append("semesterNo", getSemesterNo())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("selectStartTime", getSelectStartTime())
            .append("selectEndTime", getSelectEndTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
