package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseEnrollment;

/**
 * 选课管理Mapper接口
 */
public interface EduCourseEnrollmentMapper
{
    public EduCourseEnrollment selectEduCourseEnrollmentByEnrollmentId(Long enrollmentId);

    public List<EduCourseEnrollment> selectEduCourseEnrollmentList(EduCourseEnrollment enrollment);

    public int insertEduCourseEnrollment(EduCourseEnrollment enrollment);

    public int updateEduCourseEnrollment(EduCourseEnrollment enrollment);

    public int deleteEduCourseEnrollmentByEnrollmentId(Long enrollmentId);

    public int deleteEduCourseEnrollmentByEnrollmentIds(Long[] enrollmentIds);
}
