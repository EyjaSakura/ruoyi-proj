package com.ruoyi.system.service.impl.education;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.education.EduCourse;
import com.ruoyi.system.mapper.education.EduCourseMapper;
import com.ruoyi.system.service.education.EducationPermissionService;

/**
 * 课程库管理Service业务层处理
 */
@Service
public class EduCourseLibServiceImpl implements com.ruoyi.system.service.education.IEduCourseLibService
{
    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private EducationPermissionService educationPermission;

    /**
     * 查询课程库
     * 
     * @param courseId 课程ID
     * @return 课程
     */
    @Override
    public EduCourse selectCourseLibByCourseId(Long courseId)
    {
        EduCourse course = courseMapper.selectEduCourseByCourseId(courseId);
        // 检查课程是否属于课程库且在用户权限范围内
        if (course != null && course.getTermId() != null && course.getTermId() == 9999L)
        {
            // 如果当前用户是学院管理员，检查学院是否匹配
            if (educationPermission.isMaster())
            {
                Long currentDeptId = educationPermission.getCurrentDeptId();
                if (!currentDeptId.equals(course.getDeptId()))
                {
                    return null; // 无权访问其他学院的课程
                }
            }
            // 系统管理员可以访问所有课程库
        }
        return course;
    }

    /**
     * 查询课程库列表
     * 
     * @param eduCourse 课程
     * @return 课程集合
     */
    @Override
    public List<EduCourse> selectCourseLibList(EduCourse eduCourse)
    {
        // 设置学期为9999（课程库学期）
        eduCourse.setTermId(9999L);
        // 按学院管理员的学院进行数据范围过滤
        educationPermission.applyCourseScope(eduCourse, false);
        return courseMapper.selectEduCourseList(eduCourse);
    }

    /**
     * 新增课程库
     * 
     * @param eduCourse 课程
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCourseLib(EduCourse eduCourse)
    {
        // 设置默认院系为当前用户的学院
        if (eduCourse.getDeptId() == null)
        {
            eduCourse.setDeptId(SecurityUtils.getDeptId());
        }
        // 课序号默认值
        if (!StringUtils.hasText(eduCourse.getClassNo()))
        {
            eduCourse.setClassNo("00");
        }
        // 总课时默认值
        if (eduCourse.getTotalHours() == null)
        {
            eduCourse.setTotalHours(0);
        }
        // 容量上限默认值（0表示不限）
        if (eduCourse.getCapacity() == null)
        {
            eduCourse.setCapacity(0);
        }
        // 设置创建信息
        eduCourse.setCreateBy(SecurityUtils.getUsername());
        try
        {
            return courseMapper.insertEduCourse(eduCourse);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ServiceException("新增课程失败，课程库中课程号【" + eduCourse.getCourseCode() + "】已存在");
        }
    }

    /**
     * 修改课程库
     *
     * @param eduCourse 课程
     * @return 结果
     */
    @Override
    public int updateCourseLib(EduCourse eduCourse)
    {
        eduCourse.setUpdateBy(SecurityUtils.getUsername());
        try
        {
            return courseMapper.updateEduCourse(eduCourse);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ServiceException("修改课程失败，课程库中课程号【" + eduCourse.getCourseCode() + "】已存在");
        }
    }

    /**
     * 批量删除课程库
     * 
     * @param courseIds 需要删除的课程ID
     * @return 结果
     */
    @Override
    public int deleteCourseLibByCourseIds(Long[] courseIds)
    {
        return courseMapper.deleteEduCourseByCourseIds(courseIds);
    }

    /**
     * 导入课程库数据
     * 
     * @param courseList 课程列表
     * @param updateSupport 是否更新已存在数据
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importCourseLib(List<EduCourse> courseList, boolean updateSupport)
    {
        if (courseList == null || courseList.isEmpty())
        {
            return AjaxResult.error("导入课程数据不能为空");
        }
        
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        
        for (EduCourse course : courseList)
        {
            try
            {
                // 设置默认院系为当前用户的学院
                if (course.getDeptId() == null)
                {
                    course.setDeptId(SecurityUtils.getDeptId());
                }
                
                // 检查课程号是否已存在
                EduCourse existCourse = new EduCourse();
                existCourse.setCourseCode(course.getCourseCode());
                existCourse.setTermId(9999L); // 课程库学期
                List<EduCourse> existList = courseMapper.selectEduCourseList(existCourse);
                
                if (existList != null && !existList.isEmpty())
                {
                    if (updateSupport)
                    {
                        // 更新已存在的课程
                        course.setCourseId(existList.get(0).getCourseId());
                        course.setUpdateBy(SecurityUtils.getUsername());
                        courseMapper.updateEduCourse(course);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、课程 " + course.getCourseCode() + " 更新成功");
                    }
                    else
                    {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、课程 " + course.getCourseCode() + " 已存在");
                    }
                }
                else
                {
                    // 新增课程
                    course.setCreateBy(SecurityUtils.getUsername());
                    courseMapper.insertEduCourse(course);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、课程 " + course.getCourseCode() + " 导入成功");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、课程 " + course.getCourseCode() + " 导入失败";
                failureMsg.append(msg + ", 错误信息: " + e.getMessage());
            }
        }
        
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，共 " + failureNum + " 条数据导入失败，错误如下：");
            return AjaxResult.error(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
            return AjaxResult.success(successMsg.toString());
        }
    }
}
