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
import com.ruoyi.system.domain.education.EduTeacherProfile;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduTeacherProfileService;

/**
 * 教师档案Controller
 */
@RestController
@RequestMapping("/education/teacher")
public class EduTeacherProfileController extends BaseController
{
    @Autowired
    private IEduTeacherProfileService teacherService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:teacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduTeacherProfile teacher)
    {
        educationPermission.checkModuleAction("teacher", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyTeacherProfileScope(teacher);
        startPage();
        List<EduTeacherProfile> list = teacherService.selectEduTeacherProfileList(teacher);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:teacher:list')")
    @Log(title = "教师档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduTeacherProfile teacher)
    {
        educationPermission.checkModuleAction("teacher", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyTeacherProfileScope(teacher);
        List<EduTeacherProfile> list = teacherService.selectEduTeacherProfileList(teacher);
        ExcelUtil<EduTeacherProfile> util = new ExcelUtil<EduTeacherProfile>(EduTeacherProfile.class);
        util.exportExcel(response, list, "教师档案数据");
    }

    @PreAuthorize("@ss.hasPermi('education:teacher:list')")
    @GetMapping(value = "/{teacherId}")
    public AjaxResult getInfo(@PathVariable Long teacherId)
    {
        educationPermission.checkModuleAction("teacher", EducationPermissionService.ACTION_QUERY);
        EduTeacherProfile teacher = teacherService.selectEduTeacherProfileByTeacherId(teacherId);
        educationPermission.checkTeacherProfilePermission(teacher, EducationPermissionService.ACTION_QUERY);
        return success(teacher);
    }

    @PreAuthorize("@ss.hasPermi('education:teacher:list')")
    @Log(title = "教师档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduTeacherProfile teacher)
    {
        educationPermission.checkModuleAction("teacher", EducationPermissionService.ACTION_ADD);
        educationPermission.checkDeptOwnership(teacher.getDeptId());
        teacher.setCreateBy(getUsername());
        return toAjax(teacherService.insertEduTeacherProfile(teacher));
    }

    @PreAuthorize("@ss.hasPermi('education:teacher:list')")
    @Log(title = "教师档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduTeacherProfile teacher)
    {
        educationPermission.checkModuleAction("teacher", EducationPermissionService.ACTION_EDIT);
        EduTeacherProfile source = teacherService.selectEduTeacherProfileByTeacherId(teacher.getTeacherId());
        educationPermission.checkTeacherProfilePermission(source, EducationPermissionService.ACTION_EDIT);
        if (educationPermission.isTeacher() && !educationPermission.isMaster())
        {
            teacher.setUserId(source.getUserId());
            teacher.setDeptId(source.getDeptId());
            teacher.setTeacherNo(source.getTeacherNo());
            teacher.setStatus(source.getStatus());
        }
        else
        {
            educationPermission.checkDeptOwnership(teacher.getDeptId());
        }
        teacher.setUpdateBy(getUsername());
        return toAjax(teacherService.updateEduTeacherProfile(teacher));
    }

    @PreAuthorize("@ss.hasPermi('education:teacher:list')")
    @Log(title = "教师档案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{teacherIds}")
    public AjaxResult remove(@PathVariable Long[] teacherIds)
    {
        educationPermission.checkModuleAction("teacher", EducationPermissionService.ACTION_REMOVE);
        for (Long teacherId : teacherIds)
        {
            EduTeacherProfile teacher = teacherService.selectEduTeacherProfileByTeacherId(teacherId);
            educationPermission.checkTeacherProfilePermission(teacher, EducationPermissionService.ACTION_REMOVE);
        }
        return toAjax(teacherService.deleteEduTeacherProfileByTeacherIds(teacherIds));
    }
}
