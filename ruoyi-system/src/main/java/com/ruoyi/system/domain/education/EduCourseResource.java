package com.ruoyi.system.domain.education;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 学习资源对象 edu_course_resource
 */
public class EduCourseResource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资源ID */
    @Excel(name = "资源ID")
    private Long resourceId;

    /** 课程ID */
    @Excel(name = "课程ID")
    private Long courseId;

    /** 章节ID，可为空 */
    @Excel(name = "章节ID，可为空")
    private Long chapterId;

    /** 资源标题 */
    @Excel(name = "资源标题")
    private String resourceTitle;

    /** 资源类型（1课件 2文档资料 3图片 4视频 5压缩包 6其他） */
    @Excel(name = "资源类型", dictType = "edu_resource_type")
    private String resourceType;

    /** 文件原名称 */
    @Excel(name = "文件原名称")
    private String fileName;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String fileUrl;

    /** 文件后缀 */
    @Excel(name = "文件后缀")
    private String fileSuffix;

    /** 文件大小，单位字节 */
    @Excel(name = "文件大小，单位字节")
    private Long fileSize;

    /** 下载次数 */
    @Excel(name = "下载次数")
    private Integer downloadCount;

    /** 上传人用户ID */
    @Excel(name = "上传人用户ID")
    private Long uploadUserId;

    /** 排序号 */
    @Excel(name = "排序号")
    private Integer orderNum;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", dictType = "sys_normal_disable")
    private String status;

    /** 删除标志（0存在 2删除） */
    @Excel(name = "删除标志")
    private String delFlag;

    public void setResourceId(Long resourceId)
    {
        this.resourceId = resourceId;
    }

    public Long getResourceId()
    {
        return resourceId;
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

    public void setResourceTitle(String resourceTitle)
    {
        this.resourceTitle = resourceTitle;
    }

    public String getResourceTitle()
    {
        return resourceTitle;
    }

    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }

    public String getResourceType()
    {
        return resourceType;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileSuffix(String fileSuffix)
    {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix()
    {
        return fileSuffix;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setDownloadCount(Integer downloadCount)
    {
        this.downloadCount = downloadCount;
    }

    public Integer getDownloadCount()
    {
        return downloadCount;
    }

    public void setUploadUserId(Long uploadUserId)
    {
        this.uploadUserId = uploadUserId;
    }

    public Long getUploadUserId()
    {
        return uploadUserId;
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
            .append("resourceId", getResourceId())
            .append("courseId", getCourseId())
            .append("chapterId", getChapterId())
            .append("resourceTitle", getResourceTitle())
            .append("resourceType", getResourceType())
            .append("fileName", getFileName())
            .append("fileUrl", getFileUrl())
            .append("fileSuffix", getFileSuffix())
            .append("fileSize", getFileSize())
            .append("downloadCount", getDownloadCount())
            .append("uploadUserId", getUploadUserId())
            .append("orderNum", getOrderNum())
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
