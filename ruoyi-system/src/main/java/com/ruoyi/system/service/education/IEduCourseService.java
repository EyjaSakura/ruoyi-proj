package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourse;

/**
 * 课程管理Service接口
 */
public interface IEduCourseService
{
    public EduCourse selectEduCourseByCourseId(Long courseId);

    public List<EduCourse> selectEduCourseList(EduCourse course);

    public int insertEduCourse(EduCourse course);

    public int insertEduCourse(EduCourse course, Long ownerTeacherUserId);

    public int updateEduCourse(EduCourse course);

    public int deleteEduCourseByCourseIds(Long[] courseIds);

    public int deleteEduCourseByCourseId(Long courseId);

    /**
     * 校验 (term_id, course_code, class_no) 唯一性
     * @param course          当前课程对象（courseId为null时表示新增）
     * @return true=通过，false=已存在
     */
    boolean checkClassNoUnique(EduCourse course);

    /**
     * 模糊搜索课程（用于下拉框实时搜索）
     * @param courseName 课程名称关键字
     * @param deptId 限定院系（传null则按权限过滤）
     * @return 课程列表
     */
    List<EduCourse> searchCoursesForSelect(String courseName, Long deptId);
}
