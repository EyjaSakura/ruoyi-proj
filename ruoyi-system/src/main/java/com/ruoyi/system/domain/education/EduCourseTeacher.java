package com.ruoyi.system.domain.education;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 授课安排对象 edu_course_teacher
 */
public class EduCourseTeacher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 授课关联ID */
    @Excel(name = "授课关联ID")
    private Long courseTeacherId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 教师用户ID，对应sys_user.user_id */
    @Excel(name = "教师用户ID，对应sys_user.user_id")
    private Long teacherUserId;

    /** 教师角色（1主讲教师 2助教） */
    @Excel(name = "教师角色", dictType = "edu_teacher_role")
    private String teacherRole;

    /** 是否负责人（0否 1是） */
    @Excel(name = "是否负责人", dictType = "sys_yes_no")
    private String isOwner;

    /** 排序号 */
    @Excel(name = "排序号")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", dictType = "sys_normal_disable")
    private String status;

    public void setCourseTeacherId(Long courseTeacherId)
    {
        this.courseTeacherId = courseTeacherId;
    }

    public Long getCourseTeacherId()
    {
        return courseTeacherId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setTeacherUserId(Long teacherUserId)
    {
        this.teacherUserId = teacherUserId;
    }

    public Long getTeacherUserId()
    {
        return teacherUserId;
    }

    public void setTeacherRole(String teacherRole)
    {
        this.teacherRole = teacherRole;
    }

    public String getTeacherRole()
    {
        return teacherRole;
    }

    public void setIsOwner(String isOwner)
    {
        this.isOwner = isOwner;
    }

    public String getIsOwner()
    {
        return isOwner;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
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
            .append("courseTeacherId", getCourseTeacherId())
            .append("courseId", getCourseId())
            .append("teacherUserId", getTeacherUserId())
            .append("teacherRole", getTeacherRole())
            .append("isOwner", getIsOwner())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
