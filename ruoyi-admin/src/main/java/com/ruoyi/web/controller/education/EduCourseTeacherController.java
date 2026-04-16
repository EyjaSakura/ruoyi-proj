package com.ruoyi.web.controller.education;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.education.EduCourseTeacher;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduCourseTeacherService;

/**
 * 授课安排Controller
 */
@RestController
@RequestMapping("/education/courseTeacher")
public class EduCourseTeacherController extends BaseController
{
    @Autowired
    private IEduCourseTeacherService courseTeacherService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:courseTeacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduCourseTeacher courseTeacher)
    {
        educationPermission.checkModuleAction("courseTeacher", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyCourseTeacherScope(courseTeacher);
        startPage();
        List<EduCourseTeacher> list = courseTeacherService.selectEduCourseTeacherList(courseTeacher);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:courseTeacher:list')")
    @Log(title = "授课安排", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduCourseTeacher courseTeacher)
    {
        educationPermission.checkModuleAction("courseTeacher", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyCourseTeacherScope(courseTeacher);
        List<EduCourseTeacher> list = courseTeacherService.selectEduCourseTeacherList(courseTeacher);
        ExcelUtil<EduCourseTeacher> util = new ExcelUtil<EduCourseTeacher>(EduCourseTeacher.class);
        util.exportExcel(response, list, "授课安排数据");
    }

    @PreAuthorize("@ss.hasPermi('education:courseTeacher:list')")
    @GetMapping(value = "/{courseTeacherId}")
    public AjaxResult getInfo(@PathVariable Long courseTeacherId)
    {
        educationPermission.checkModuleAction("courseTeacher", EducationPermissionService.ACTION_QUERY);
        EduCourseTeacher courseTeacher = courseTeacherService.selectEduCourseTeacherByCourseTeacherId(courseTeacherId);
        educationPermission.checkCourseTeacherPermission(courseTeacher);
        return success(courseTeacher);
    }

    @PreAuthorize("@ss.hasPermi('education:courseTeacher:list')")
    @Log(title = "授课安排", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduCourseTeacher courseTeacher)
    {
        educationPermission.checkModuleAction("courseTeacher", EducationPermissionService.ACTION_ADD);
        educationPermission.checkCourseOwnership(courseTeacher.getCourseId(), false);
        educationPermission.checkUserDeptOwnership(courseTeacher.getTeacherUserId());
        courseTeacher.setCreateBy(getUsername());
        return toAjax(courseTeacherService.insertEduCourseTeacher(courseTeacher));
    }

    @PreAuthorize("@ss.hasPermi('education:courseTeacher:list')")
    @Log(title = "授课安排", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduCourseTeacher courseTeacher)
    {
        educationPermission.checkModuleAction("courseTeacher", EducationPermissionService.ACTION_EDIT);
        EduCourseTeacher source = courseTeacherService.selectEduCourseTeacherByCourseTeacherId(courseTeacher.getCourseTeacherId());
        educationPermission.checkCourseTeacherPermission(source);
        educationPermission.checkCourseOwnership(courseTeacher.getCourseId(), false);
        educationPermission.checkUserDeptOwnership(courseTeacher.getTeacherUserId());
        courseTeacher.setUpdateBy(getUsername());
        return toAjax(courseTeacherService.updateEduCourseTeacher(courseTeacher));
    }

    @PreAuthorize("@ss.hasPermi('education:courseTeacher:list')")
    @Log(title = "授课安排", businessType = BusinessType.DELETE)
    @DeleteMapping("/{courseTeacherIds}")
    public AjaxResult remove(@PathVariable Long[] courseTeacherIds)
    {
        educationPermission.checkModuleAction("courseTeacher", EducationPermissionService.ACTION_REMOVE);
        for (Long courseTeacherId : courseTeacherIds)
        {
            EduCourseTeacher courseTeacher = courseTeacherService.selectEduCourseTeacherByCourseTeacherId(courseTeacherId);
            educationPermission.checkCourseTeacherPermission(courseTeacher);
        }
        return toAjax(courseTeacherService.deleteEduCourseTeacherByCourseTeacherIds(courseTeacherIds));
    }
}
