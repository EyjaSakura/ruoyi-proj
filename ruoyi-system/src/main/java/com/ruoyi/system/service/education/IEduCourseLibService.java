package com.ruoyi.system.service.education;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.education.EduCourse;

/**
 * 课程库管理Service接口
 */
public interface IEduCourseLibService
{
    /**
     * 查询课程库
     * 
     * @param courseId 课程ID
     * @return 课程
     */
    public EduCourse selectCourseLibByCourseId(Long courseId);

    /**
     * 查询课程库列表
     * 
     * @param eduCourse 课程
     * @return 课程集合
     */
    public List<EduCourse> selectCourseLibList(EduCourse eduCourse);

    /**
     * 新增课程库
     * 
     * @param eduCourse 课程
     * @return 结果
     */
    public int insertCourseLib(EduCourse eduCourse);

    /**
     * 修改课程库
     * 
     * @param eduCourse 课程
     * @return 结果
     */
    public int updateCourseLib(EduCourse eduCourse);

    /**
     * 批量删除课程库
     * 
     * @param courseIds 需要删除的课程ID
     * @return 结果
     */
    public int deleteCourseLibByCourseIds(Long[] courseIds);

    /**
     * 导入课程库数据
     * 
     * @param courseList 课程列表
     * @param updateSupport 是否更新已存在数据
     * @return 结果
     */
    public AjaxResult importCourseLib(List<EduCourse> courseList, boolean updateSupport);
}
