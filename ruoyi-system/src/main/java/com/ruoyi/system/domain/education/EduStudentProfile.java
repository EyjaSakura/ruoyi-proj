package com.ruoyi.system.domain.education;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 学生档案对象 edu_student_profile
 */
public class EduStudentProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学生档案ID */
    @Excel(name = "学生档案ID")
    private Long studentId;

    /** 用户ID，对应sys_user.user_id */
    @Excel(name = "用户ID，对应sys_user.user_id")
    private Long userId;

    /** 学号 */
    @Excel(name = "学号")
    private String studentNo;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 院系ID，对应sys_dept.dept_id */
    private Long deptId;

    /** 院系名称 */
    @Excel(name = "所属院系")
    private String deptName;

    /** 专业名称 */
    @Excel(name = "专业名称")
    private String majorName;

    /** 班级名称 */
    @Excel(name = "班级名称")
    private String className;

    /** 年级 */
    @Excel(name = "年级")
    private String gradeName;

    /** 入学年份 */
    @Excel(name = "入学年份")
    private String admissionYear;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 学籍状态（1在读 2休学 3毕业） */
    @Excel(name = "学籍状态", dictType = "edu_study_status")
    private String studyStatus;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", dictType = "sys_normal_disable")
    private String status;

    /** 删除标志（0存在 2删除） */
    @Excel(name = "删除标志")
    private String delFlag;

    public void setStudentId(Long studentId)
    {
        this.studentId = studentId;
    }

    public Long getStudentId()
    {
        return studentId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
    }

    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public String getStudentName()
    {
        return studentName;
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

    public void setMajorName(String majorName)
    {
        this.majorName = majorName;
    }

    public String getMajorName()
    {
        return majorName;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassName()
    {
        return className;
    }

    public void setGradeName(String gradeName)
    {
        this.gradeName = gradeName;
    }

    public String getGradeName()
    {
        return gradeName;
    }

    public void setAdmissionYear(String admissionYear)
    {
        this.admissionYear = admissionYear;
    }

    public String getAdmissionYear()
    {
        return admissionYear;
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

    public void setStudyStatus(String studyStatus)
    {
        this.studyStatus = studyStatus;
    }

    public String getStudyStatus()
    {
        return studyStatus;
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
            .append("studentId", getStudentId())
            .append("userId", getUserId())
            .append("studentNo", getStudentNo())
            .append("studentName", getStudentName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("majorName", getMajorName())
            .append("className", getClassName())
            .append("gradeName", getGradeName())
            .append("admissionYear", getAdmissionYear())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("studyStatus", getStudyStatus())
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
