package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseTeacher;

/**
 * 授课安排Service接口
 */
public interface IEduCourseTeacherService
{
    public EduCourseTeacher selectEduCourseTeacherByCourseTeacherId(Long courseTeacherId);

    public List<EduCourseTeacher> selectEduCourseTeacherList(EduCourseTeacher courseTeacher);

    public int insertEduCourseTeacher(EduCourseTeacher courseTeacher);

    public int updateEduCourseTeacher(EduCourseTeacher courseTeacher);

    public int deleteEduCourseTeacherByCourseTeacherIds(Long[] courseTeacherIds);

    public int deleteEduCourseTeacherByCourseTeacherId(Long courseTeacherId);
}
