package com.ruoyi.system.mapper.education;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.education.EduCourse;

/**
 * 课程管理Mapper接口
 */
public interface EduCourseMapper
{
    public EduCourse selectEduCourseByCourseId(Long courseId);

    public List<EduCourse> selectEduCourseList(EduCourse course);

    public int insertEduCourse(EduCourse course);

    public int updateEduCourse(EduCourse course);

    public int deleteEduCourseByCourseId(Long courseId);

    public int deleteEduCourseByCourseIds(Long[] courseIds);

    /**
     * 唯一性校验：同一学期下 courseCode+classNo 组合是否已存在
     * @param termId         学期ID
     * @param courseCode     课程号
     * @param classNo        课序号
     * @param excludeCourseId 编辑时排除自身（新增传null）
     */
    int countByUniqueKey(@Param("termId") Long termId,
                         @Param("courseCode") String courseCode,
                         @Param("classNo") String classNo,
                         @Param("excludeCourseId") Long excludeCourseId);

    /**
     * 模糊搜索课程列表（返回courseId和courseName）
     * @param courseName 课程名称关键字
     * @param deptId 限定院系（传null则不过滤）
     * @return 课程列表 [{courseId, courseName, deptName}]
     */
    List<EduCourse> selectCourseForSearch(@Param("courseName") String courseName, @Param("deptId") Long deptId);
}
