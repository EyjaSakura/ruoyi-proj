package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduCourseResource;
import com.ruoyi.system.mapper.education.EduCourseResourceMapper;
import com.ruoyi.system.service.education.IEduCourseResourceService;

/**
 * 学习资源Service业务层处理
 */
@Service
public class EduCourseResourceServiceImpl implements IEduCourseResourceService
{
    @Autowired
    private EduCourseResourceMapper resourceMapper;

    @Override
    public EduCourseResource selectEduCourseResourceByResourceId(Long resourceId)
    {
        return resourceMapper.selectEduCourseResourceByResourceId(resourceId);
    }

    @Override
    public List<EduCourseResource> selectEduCourseResourceList(EduCourseResource resource)
    {
        return resourceMapper.selectEduCourseResourceList(resource);
    }

    @Override
    public int insertEduCourseResource(EduCourseResource resource)
    {
        resource.setCreateTime(DateUtils.getNowDate());
        return resourceMapper.insertEduCourseResource(resource);
    }

    @Override
    public int updateEduCourseResource(EduCourseResource resource)
    {
        resource.setUpdateTime(DateUtils.getNowDate());
        return resourceMapper.updateEduCourseResource(resource);
    }

    @Override
    public int deleteEduCourseResourceByResourceIds(Long[] resourceIds)
    {
        return resourceMapper.deleteEduCourseResourceByResourceIds(resourceIds);
    }

    @Override
    public int deleteEduCourseResourceByResourceId(Long resourceId)
    {
        return resourceMapper.deleteEduCourseResourceByResourceId(resourceId);
    }
}
