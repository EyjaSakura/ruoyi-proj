package com.ruoyi.system.domain.education;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 课程管理对象 edu_course
 */
public class EduCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 学期ID */
    @Excel(name = "学期ID")
    private Long termId;

    /** 开课院系ID */
    @Excel(name = "开课院系ID")
    private Long deptId;

    /** 课程号 */
    @Excel(name = "课程号")
    private String courseCode;

    /** 课序号（同一课程号在同一学期可开多个班，如01 02 03） */
    @Excel(name = "课序号")
    private String classNo;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 课程封面 */
    @Excel(name = "课程封面")
    private String courseCover;

    /** 课程类型（1必修 2选修） */
    @Excel(name = "课程类型", dictType = "edu_course_type")
    private String courseType;

    /** 学分 */
    @Excel(name = "学分")
    private BigDecimal credit;

    /** 总课时 */
    @Excel(name = "总课时")
    private Integer totalHours;

    /** 容量上限，0表示不限 */
    @Excel(name = "容量上限，0表示不限")
    private Integer capacity;

    /** 当前已选人数 */
    @Excel(name = "当前已选人数")
    private Integer selectedCount;

    /** 选课开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "选课开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date selectStartTime;

    /** 选课结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "选课结束时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date selectEndTime;

    /** 课程简介 */
    @Excel(name = "课程简介")
    private String intro;

    /** 课程完成规则说明 */
    @Excel(name = "课程完成规则说明")
    private String completeRule;

    /** 状态（0草稿 1已发布 2已下架） */
    @Excel(name = "状态", dictType = "edu_course_status")
    private String status;

    /** 删除标志（0存在 2删除） */
    @Excel(name = "删除标志")
    private String delFlag;

    /** 授课教师ID（从 edu_course_teacher JOIN sys_user 查出） */
    private Long teacherUserId;

    /** 授课教师姓名（从 edu_course_teacher JOIN sys_user 查出） */
    private String teacherUserName;

    /** 授课教师工号（从 edu_course_teacher JOIN sys_user 查出） */
    private String teacherLoginName;

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

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setCourseCode(String courseCode)
    {
        this.courseCode = courseCode;
    }

    public String getCourseCode()
    {
        return courseCode;
    }

    public void setClassNo(String classNo)
    {
        this.classNo = classNo;
    }

    public String getClassNo()
    {
        return classNo;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseCover(String courseCover)
    {
        this.courseCover = courseCover;
    }

    public String getCourseCover()
    {
        return courseCover;
    }

    public void setCourseType(String courseType)
    {
        this.courseType = courseType;
    }

    public String getCourseType()
    {
        return courseType;
    }

    public void setCredit(BigDecimal credit)
    {
        this.credit = credit;
    }

    public BigDecimal getCredit()
    {
        return credit;
    }

    public void setTotalHours(Integer totalHours)
    {
        this.totalHours = totalHours;
    }

    public Integer getTotalHours()
    {
        return totalHours;
    }

    public void setCapacity(Integer capacity)
    {
        this.capacity = capacity;
    }

    public Integer getCapacity()
    {
        return capacity;
    }

    public void setSelectedCount(Integer selectedCount)
    {
        this.selectedCount = selectedCount;
    }

    public Integer getSelectedCount()
    {
        return selectedCount;
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

    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    public String getIntro()
    {
        return intro;
    }

    public void setCompleteRule(String completeRule)
    {
        this.completeRule = completeRule;
    }

    public String getCompleteRule()
    {
        return completeRule;
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

    public Long getTeacherUserId()
    {
        return teacherUserId;
    }

    public void setTeacherUserId(Long teacherUserId)
    {
        this.teacherUserId = teacherUserId;
    }

    public String getTeacherUserName()
    {
        return teacherUserName;
    }

    public void setTeacherUserName(String teacherUserName)
    {
        this.teacherUserName = teacherUserName;
    }

    public String getTeacherLoginName()
    {
        return teacherLoginName;
    }

    public void setTeacherLoginName(String teacherLoginName)
    {
        this.teacherLoginName = teacherLoginName;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("courseId", getCourseId())
            .append("termId", getTermId())
            .append("deptId", getDeptId())
            .append("courseCode", getCourseCode())
            .append("classNo", getClassNo())
            .append("courseName", getCourseName())
            .append("courseCover", getCourseCover())
            .append("courseType", getCourseType())
            .append("credit", getCredit())
            .append("totalHours", getTotalHours())
            .append("capacity", getCapacity())
            .append("selectedCount", getSelectedCount())
            .append("selectStartTime", getSelectStartTime())
            .append("selectEndTime", getSelectEndTime())
            .append("intro", getIntro())
            .append("completeRule", getCompleteRule())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("teacherUserId", getTeacherUserId())
            .append("teacherUserName", getTeacherUserName())
            .append("teacherLoginName", getTeacherLoginName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
