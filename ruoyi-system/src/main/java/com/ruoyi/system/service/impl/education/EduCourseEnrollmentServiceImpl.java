package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduCourseEnrollment;
import com.ruoyi.system.mapper.education.EduCourseEnrollmentMapper;
import com.ruoyi.system.service.education.IEduCourseEnrollmentService;

/**
 * 选课管理Service业务层处理
 */
@Service
public class EduCourseEnrollmentServiceImpl implements IEduCourseEnrollmentService
{
    @Autowired
    private EduCourseEnrollmentMapper enrollmentMapper;

    @Override
    public EduCourseEnrollment selectEduCourseEnrollmentByEnrollmentId(Long enrollmentId)
    {
        return enrollmentMapper.selectEduCourseEnrollmentByEnrollmentId(enrollmentId);
    }

    @Override
    public List<EduCourseEnrollment> selectEduCourseEnrollmentList(EduCourseEnrollment enrollment)
    {
        return enrollmentMapper.selectEduCourseEnrollmentList(enrollment);
    }

    @Override
    public int insertEduCourseEnrollment(EduCourseEnrollment enrollment)
    {
        enrollment.setCreateTime(DateUtils.getNowDate());
        return enrollmentMapper.insertEduCourseEnrollment(enrollment);
    }

    @Override
    public int updateEduCourseEnrollment(EduCourseEnrollment enrollment)
    {
        enrollment.setUpdateTime(DateUtils.getNowDate());
        return enrollmentMapper.updateEduCourseEnrollment(enrollment);
    }

    @Override
    public int deleteEduCourseEnrollmentByEnrollmentIds(Long[] enrollmentIds)
    {
        return enrollmentMapper.deleteEduCourseEnrollmentByEnrollmentIds(enrollmentIds);
    }

    @Override
    public int deleteEduCourseEnrollmentByEnrollmentId(Long enrollmentId)
    {
        return enrollmentMapper.deleteEduCourseEnrollmentByEnrollmentId(enrollmentId);
    }
}
