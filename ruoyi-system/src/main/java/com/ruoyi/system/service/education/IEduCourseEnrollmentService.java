package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseEnrollment;

/**
 * 选课管理Service接口
 */
public interface IEduCourseEnrollmentService
{
    public EduCourseEnrollment selectEduCourseEnrollmentByEnrollmentId(Long enrollmentId);

    public List<EduCourseEnrollment> selectEduCourseEnrollmentList(EduCourseEnrollment enrollment);

    public int insertEduCourseEnrollment(EduCourseEnrollment enrollment);

    public int updateEduCourseEnrollment(EduCourseEnrollment enrollment);

    public int deleteEduCourseEnrollmentByEnrollmentIds(Long[] enrollmentIds);

    public int deleteEduCourseEnrollmentByEnrollmentId(Long enrollmentId);
}
