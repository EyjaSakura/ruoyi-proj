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
import com.ruoyi.system.domain.education.EduStudentProfile;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduStudentProfileService;

/**
 * 学生档案Controller
 */
@RestController
@RequestMapping("/education/student")
public class EduStudentProfileController extends BaseController
{
    @Autowired
    private IEduStudentProfileService studentService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduStudentProfile student)
    {
        educationPermission.checkModuleAction("student", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyStudentProfileScope(student);
        startPage();
        List<EduStudentProfile> list = studentService.selectEduStudentProfileList(student);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:student:list')")
    @Log(title = "学生档案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduStudentProfile student)
    {
        educationPermission.checkModuleAction("student", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyStudentProfileScope(student);
        List<EduStudentProfile> list = studentService.selectEduStudentProfileList(student);
        ExcelUtil<EduStudentProfile> util = new ExcelUtil<EduStudentProfile>(EduStudentProfile.class);
        util.exportExcel(response, list, "学生档案数据");
    }

    @PreAuthorize("@ss.hasPermi('education:student:list')")
    @GetMapping(value = "/{studentId}")
    public AjaxResult getInfo(@PathVariable Long studentId)
    {
        educationPermission.checkModuleAction("student", EducationPermissionService.ACTION_QUERY);
        EduStudentProfile student = studentService.selectEduStudentProfileByStudentId(studentId);
        educationPermission.checkStudentProfilePermission(student, EducationPermissionService.ACTION_QUERY);
        return success(student);
    }

    @PreAuthorize("@ss.hasPermi('education:student:list')")
    @Log(title = "学生档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduStudentProfile student)
    {
        educationPermission.checkModuleAction("student", EducationPermissionService.ACTION_ADD);
        educationPermission.checkDeptOwnership(student.getDeptId());
        student.setCreateBy(getUsername());
        return toAjax(studentService.insertEduStudentProfile(student));
    }

    @PreAuthorize("@ss.hasPermi('education:student:list')")
    @Log(title = "学生档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduStudentProfile student)
    {
        educationPermission.checkModuleAction("student", EducationPermissionService.ACTION_EDIT);
        EduStudentProfile source = studentService.selectEduStudentProfileByStudentId(student.getStudentId());
        educationPermission.checkStudentProfilePermission(source, EducationPermissionService.ACTION_EDIT);
        if (educationPermission.isStudent() && !educationPermission.isMaster())
        {
            student.setUserId(source.getUserId());
            student.setDeptId(source.getDeptId());
            student.setStudentNo(source.getStudentNo());
            student.setStatus(source.getStatus());
            student.setStudyStatus(source.getStudyStatus());
        }
        else
        {
            educationPermission.checkDeptOwnership(student.getDeptId());
        }
        student.setUpdateBy(getUsername());
        return toAjax(studentService.updateEduStudentProfile(student));
    }

    @PreAuthorize("@ss.hasPermi('education:student:list')")
    @Log(title = "学生档案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{studentIds}")
    public AjaxResult remove(@PathVariable Long[] studentIds)
    {
        educationPermission.checkModuleAction("student", EducationPermissionService.ACTION_REMOVE);
        for (Long studentId : studentIds)
        {
            EduStudentProfile student = studentService.selectEduStudentProfileByStudentId(studentId);
            educationPermission.checkStudentProfilePermission(student, EducationPermissionService.ACTION_REMOVE);
        }
        return toAjax(studentService.deleteEduStudentProfileByStudentIds(studentIds));
    }
}
