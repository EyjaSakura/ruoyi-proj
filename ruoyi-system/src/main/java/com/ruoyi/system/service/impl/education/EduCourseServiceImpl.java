package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.ruoyi.common.exception.ServiceException;
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
        System.out.println(">>> insertEduCourse 被调用了，termId=" + course.getTermId());
        // 课序号默认值补充
        if (!StringUtils.hasText(course.getClassNo()))
        {
            course.setClassNo("01");
        }
        course.setCreateTime(DateUtils.getNowDate());
        try
        {
            courseMapper.insertEduCourse(course);
        }
        catch (DataIntegrityViolationException e)
        {
            // 课程库（termId=9999）课程号重复，或普通学期课序号重复
            String msg = "9999".equals(String.valueOf(course.getTermId()))
                    ? "新增课程失败，课程库中课程号【" + course.getCourseCode() + "】已存在"
                    : "新增课堂失败，该学期下课程号【" + course.getCourseCode() + "】课序号【" + course.getClassNo() + "】已存在";
            throw new ServiceException(msg);
        }
        if (ownerTeacherUserId != null && course.getCourseId() != null)
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
        return course.getCourseId();
    }

    @Override
    public int updateEduCourse(EduCourse course)
    {
        course.setUpdateTime(DateUtils.getNowDate());
        try
        {
            return courseMapper.updateEduCourse(course);
        }
        catch (DataIntegrityViolationException e)
        {
            String msg = "9999".equals(String.valueOf(course.getTermId()))
                    ? "修改课程失败，课程库中课程号【" + course.getCourseCode() + "】已存在"
                    : "修改课堂失败，该学期下课程号【" + course.getCourseCode() + "】课序号【" + course.getClassNo() + "】已存在";
            throw new ServiceException(msg);
        }
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
    public boolean checkTermCourseCodeUnique(EduCourse course)
    {
        if (course.getTermId() == null || !StringUtils.hasText(course.getCourseCode()))
        {
            return true;
        }
        int count = courseMapper.countByTermAndCourseCode(
                course.getTermId(),
                course.getCourseCode(),
                course.getCourseId());
        return count == 0;
    }

    @Override
    public List<EduCourse> searchCoursesForSelect(String courseName, Long deptId)
    {
        return courseMapper.selectCourseForSearch(courseName, deptId);
    }
}
