package com.ruoyi.system.domain.education;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 教师档案对象 edu_teacher_profile
 */
public class EduTeacherProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 教师档案ID */
    @Excel(name = "教师档案ID")
    private Long teacherId;

    /** 用户ID，对应sys_user.user_id */
    @Excel(name = "用户ID，对应sys_user.user_id")
    private Long userId;

    /** 教师工号 */
    @Excel(name = "教师工号")
    private String teacherNo;

    /** 教师姓名 */
    @Excel(name = "教师姓名")
    private String teacherName;

    /** 院系ID，对应sys_dept.dept_id */
    private Long deptId;

    /** 院系名称 */
    @Excel(name = "所属院系")
    private String deptName;

    /** 职称 */
    @Excel(name = "职称")
    private String jobTitle;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 办公地点 */
    @Excel(name = "办公地点")
    private String officeAddress;

    /** 研究方向 */
    @Excel(name = "研究方向")
    private String researchDirection;

    /** 教师简介 */
    @Excel(name = "教师简介")
    private String intro;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", dictType = "sys_normal_disable")
    private String status;

    /** 删除标志（0存在 2删除） */
    @Excel(name = "删除标志")
    private String delFlag;

    public void setTeacherId(Long teacherId)
    {
        this.teacherId = teacherId;
    }

    public Long getTeacherId()
    {
        return teacherId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setTeacherNo(String teacherNo)
    {
        this.teacherNo = teacherNo;
    }

    public String getTeacherNo()
    {
        return teacherNo;
    }

    public void setTeacherName(String teacherName)
    {
        this.teacherName = teacherName;
    }

    public String getTeacherName()
    {
        return teacherName;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle()
    {
        return jobTitle;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setOfficeAddress(String officeAddress)
    {
        this.officeAddress = officeAddress;
    }

    public String getOfficeAddress()
    {
        return officeAddress;
    }

    public void setResearchDirection(String researchDirection)
    {
        this.researchDirection = researchDirection;
    }

    public String getResearchDirection()
    {
        return researchDirection;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    public String getIntro()
    {
        return intro;
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
            .append("teacherId", getTeacherId())
            .append("userId", getUserId())
            .append("teacherNo", getTeacherNo())
            .append("teacherName", getTeacherName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("jobTitle", getJobTitle())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("officeAddress", getOfficeAddress())
            .append("researchDirection", getResearchDirection())
            .append("intro", getIntro())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
