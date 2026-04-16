package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseTeacher;

/**
 * 授课安排Mapper接口
 */
public interface EduCourseTeacherMapper
{
    public EduCourseTeacher selectEduCourseTeacherByCourseTeacherId(Long courseTeacherId);

    public List<EduCourseTeacher> selectEduCourseTeacherList(EduCourseTeacher courseTeacher);

    public int insertEduCourseTeacher(EduCourseTeacher courseTeacher);

    public int updateEduCourseTeacher(EduCourseTeacher courseTeacher);

    public int deleteEduCourseTeacherByCourseTeacherId(Long courseTeacherId);

    public int deleteEduCourseTeacherByCourseTeacherIds(Long[] courseTeacherIds);
}
