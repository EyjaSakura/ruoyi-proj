package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduCourseTeacher;
import com.ruoyi.system.mapper.education.EduCourseTeacherMapper;
import com.ruoyi.system.service.education.IEduCourseTeacherService;

/**
 * 授课安排Service业务层处理
 */
@Service
public class EduCourseTeacherServiceImpl implements IEduCourseTeacherService
{
    @Autowired
    private EduCourseTeacherMapper courseTeacherMapper;

    @Override
    public EduCourseTeacher selectEduCourseTeacherByCourseTeacherId(Long courseTeacherId)
    {
        return courseTeacherMapper.selectEduCourseTeacherByCourseTeacherId(courseTeacherId);
    }

    @Override
    public List<EduCourseTeacher> selectEduCourseTeacherList(EduCourseTeacher courseTeacher)
    {
        return courseTeacherMapper.selectEduCourseTeacherList(courseTeacher);
    }

    @Override
    public int insertEduCourseTeacher(EduCourseTeacher courseTeacher)
    {
        courseTeacher.setCreateTime(DateUtils.getNowDate());
        return courseTeacherMapper.insertEduCourseTeacher(courseTeacher);
    }

    @Override
    public int updateEduCourseTeacher(EduCourseTeacher courseTeacher)
    {
        courseTeacher.setUpdateTime(DateUtils.getNowDate());
        return courseTeacherMapper.updateEduCourseTeacher(courseTeacher);
    }

    @Override
    public int deleteEduCourseTeacherByCourseTeacherIds(Long[] courseTeacherIds)
    {
        return courseTeacherMapper.deleteEduCourseTeacherByCourseTeacherIds(courseTeacherIds);
    }

    @Override
    public int deleteEduCourseTeacherByCourseTeacherId(Long courseTeacherId)
    {
        return courseTeacherMapper.deleteEduCourseTeacherByCourseTeacherId(courseTeacherId);
    }
}
