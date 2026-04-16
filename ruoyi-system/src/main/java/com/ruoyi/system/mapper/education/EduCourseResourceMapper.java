package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseResource;

/**
 * 学习资源Mapper接口
 */
public interface EduCourseResourceMapper
{
    public EduCourseResource selectEduCourseResourceByResourceId(Long resourceId);

    public List<EduCourseResource> selectEduCourseResourceList(EduCourseResource resource);

    public int insertEduCourseResource(EduCourseResource resource);

    public int updateEduCourseResource(EduCourseResource resource);

    public int deleteEduCourseResourceByResourceId(Long resourceId);

    public int deleteEduCourseResourceByResourceIds(Long[] resourceIds);
}
