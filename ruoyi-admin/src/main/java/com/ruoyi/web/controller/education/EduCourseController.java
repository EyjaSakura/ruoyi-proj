package com.ruoyi.web.controller.education;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.education.EduCourse;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduCourseService;

/**
 * 课程管理Controller
 */
@RestController
@RequestMapping("/education/course")
public class EduCourseController extends BaseController
{
    @Autowired
    private IEduCourseService courseService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduCourse course)
    {
        educationPermission.checkModuleAction("course", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyCourseScope(course, true);
        startPage();
        List<EduCourse> list = courseService.selectEduCourseList(course);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:course:list')")
    @Log(title = "课程管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduCourse course)
    {
        educationPermission.checkModuleAction("course", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyCourseScope(course, true);
        List<EduCourse> list = courseService.selectEduCourseList(course);
        ExcelUtil<EduCourse> util = new ExcelUtil<EduCourse>(EduCourse.class);
        util.exportExcel(response, list, "课程管理数据");
    }

    @PreAuthorize("@ss.hasPermi('education:course:list')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable Long courseId)
    {
        educationPermission.checkModuleAction("course", EducationPermissionService.ACTION_QUERY);
        EduCourse course = courseService.selectEduCourseByCourseId(courseId);
        educationPermission.checkCoursePermission(course, true);
        return success(course);
    }

    @PreAuthorize("@ss.hasPermi('education:course:list')")
    @Log(title = "课程管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduCourse course)
    {
        educationPermission.checkModuleAction("course", EducationPermissionService.ACTION_ADD);
        educationPermission.checkDeptOwnership(course.getDeptId());
        if (!courseService.checkClassNoUnique(course))
        {
            return error("新增课程失败，该学期下课程号【" + course.getCourseCode() + "】课序号【" + course.getClassNo() + "】已存在");
        }
        course.setCreateBy(getUsername());
        Long ownerTeacherUserId = educationPermission.isTeacher() ? educationPermission.getCurrentUserId() : null;
        return toAjax(courseService.insertEduCourse(course, ownerTeacherUserId));
    }

    @PreAuthorize("@ss.hasPermi('education:course:list')")
    @Log(title = "课程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduCourse course)
    {
        educationPermission.checkModuleAction("course", EducationPermissionService.ACTION_EDIT);
        EduCourse source = courseService.selectEduCourseByCourseId(course.getCourseId());
        educationPermission.checkCoursePermission(source, false);
        educationPermission.checkDeptOwnership(course.getDeptId());
        if (!courseService.checkClassNoUnique(course))
        {
            return error("修改课程失败，该学期下课程号【" + course.getCourseCode() + "】课序号【" + course.getClassNo() + "】已存在");
        }
        course.setUpdateBy(getUsername());
        return toAjax(courseService.updateEduCourse(course));
    }

    @PreAuthorize("@ss.hasPermi('education:course:list')")
    @Log(title = "课程管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds)
    {
        educationPermission.checkModuleAction("course", EducationPermissionService.ACTION_REMOVE);
        for (Long courseId : courseIds)
        {
            EduCourse course = courseService.selectEduCourseByCourseId(courseId);
            educationPermission.checkCoursePermission(course, false);
        }
        return toAjax(courseService.deleteEduCourseByCourseIds(courseIds));
    }

    /**
     * 模糊搜索课程（用于下拉框实时搜索）
     */
    @GetMapping("/search")
    public AjaxResult searchCourses(@RequestParam(required = false) String keyword)
    {
        educationPermission.checkModuleAction("course", EducationPermissionService.ACTION_QUERY);
        // 根据权限设置查询范围：系统管理员可查所有，其他角色只能查本学院
        Long deptId = educationPermission.isAdmin() ? null : educationPermission.getCurrentDeptId();
        List<EduCourse> list = courseService.searchCoursesForSelect(keyword, deptId);
        return success(list);
    }
}
