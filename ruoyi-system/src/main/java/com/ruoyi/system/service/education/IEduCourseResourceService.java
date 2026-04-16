package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseResource;

/**
 * 学习资源Service接口
 */
public interface IEduCourseResourceService
{
    public EduCourseResource selectEduCourseResourceByResourceId(Long resourceId);

    public List<EduCourseResource> selectEduCourseResourceList(EduCourseResource resource);

    public int insertEduCourseResource(EduCourseResource resource);

    public int updateEduCourseResource(EduCourseResource resource);

    public int deleteEduCourseResourceByResourceIds(Long[] resourceIds);

    public int deleteEduCourseResourceByResourceId(Long resourceId);
}
