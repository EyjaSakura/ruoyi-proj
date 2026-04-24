package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduCourse;
import com.ruoyi.system.domain.education.EduCourseTeacher;
import com.ruoyi.system.mapper.education.EduCourseMapper;
import com.ruoyi.system.service.education.IEduCourseService;
import com.ruoyi.system.service.education.IEduCourseTeacherService;

/**
 * 课程管理Service业务层处理
 */
@Service
public class EduCourseServiceImpl implements IEduCourseService
{
    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private IEduCourseTeacherService courseTeacherService;

    @Override
    public EduCourse selectEduCourseByCourseId(Long courseId)
    {
        return courseMapper.selectEduCourseByCourseId(courseId);
    }

    @Override
    public List<EduCourse> selectEduCourseList(EduCourse course)
    {
        return courseMapper.selectEduCourseList(course);
    }

    @Override
    public Long insertEduCourse(EduCourse course)
    {
        return insertEduCourse(course, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertEduCourse(EduCourse course, Long ownerTeacherUserId)
    {
        // 课序号默认值补充
        if (!StringUtils.hasText(course.getClassNo()))
        {
            course.setClassNo("01");
        }
        course.setCreateTime(DateUtils.getNowDate());
        int rows = courseMapper.insertEduCourse(course);
        if (rows > 0 && ownerTeacherUserId != null && course.getCourseId() != null)
        {
            EduCourseTeacher courseTeacher = new EduCourseTeacher();
            courseTeacher.setCourseId(course.getCourseId());
            courseTeacher.setTeacherUserId(ownerTeacherUserId);
            courseTeacher.setTeacherRole("1");
            courseTeacher.setIsOwner("1");
            courseTeacher.setOrderNum(1);
            courseTeacher.setStatus("0");
            courseTeacher.setCreateBy(course.getCreateBy());
            courseTeacher.setRemark("教师新增课程时自动建立主讲教师关联");
            courseTeacherService.insertEduCourseTeacher(courseTeacher);
        }
        // 返回新课堂的 courseId（前端需要用它来建授课关联）
        return rows > 0 ? course.getCourseId() : null;
    }

    @Override
    public int updateEduCourse(EduCourse course)
    {
        course.setUpdateTime(DateUtils.getNowDate());
        return courseMapper.updateEduCourse(course);
    }

    @Override
    public int deleteEduCourseByCourseIds(Long[] courseIds)
    {
        return courseMapper.deleteEduCourseByCourseIds(courseIds);
    }

    @Override
    public int deleteEduCourseByCourseId(Long courseId)
    {
        return courseMapper.deleteEduCourseByCourseId(courseId);
    }

    @Override
    public boolean checkClassNoUnique(EduCourse course)
    {
        if (course.getTermId() == null || !StringUtils.hasText(course.getCourseCode())
                || !StringUtils.hasText(course.getClassNo()))
        {
            return true;
        }
        int count = courseMapper.countByUniqueKey(
                course.getTermId(),
                course.getCourseCode(),
                course.getClassNo(),
                course.getCourseId());
        return count == 0;
    }

    @Override
    public List<EduCourse> searchCoursesForSelect(String courseName, Long deptId)
    {
        return courseMapper.selectCourseForSearch(courseName, deptId);
    }
}
