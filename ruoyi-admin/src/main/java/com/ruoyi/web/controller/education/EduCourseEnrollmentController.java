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
import com.ruoyi.system.domain.education.EduCourseEnrollment;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduCourseEnrollmentService;

/**
 * 选课管理Controller
 */
@RestController
@RequestMapping("/education/enrollment")
public class EduCourseEnrollmentController extends BaseController
{
    @Autowired
    private IEduCourseEnrollmentService enrollmentService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:enrollment:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduCourseEnrollment enrollment)
    {
        educationPermission.checkModuleAction("enrollment", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyEnrollmentScope(enrollment);
        startPage();
        List<EduCourseEnrollment> list = enrollmentService.selectEduCourseEnrollmentList(enrollment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:enrollment:list')")
    @Log(title = "选课管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduCourseEnrollment enrollment)
    {
        educationPermission.checkModuleAction("enrollment", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyEnrollmentScope(enrollment);
        List<EduCourseEnrollment> list = enrollmentService.selectEduCourseEnrollmentList(enrollment);
        ExcelUtil<EduCourseEnrollment> util = new ExcelUtil<EduCourseEnrollment>(EduCourseEnrollment.class);
        util.exportExcel(response, list, "选课管理数据");
    }

    @PreAuthorize("@ss.hasPermi('education:enrollment:list')")
    @GetMapping(value = "/{enrollmentId}")
    public AjaxResult getInfo(@PathVariable Long enrollmentId)
    {
        educationPermission.checkModuleAction("enrollment", EducationPermissionService.ACTION_QUERY);
        EduCourseEnrollment enrollment = enrollmentService.selectEduCourseEnrollmentByEnrollmentId(enrollmentId);
        educationPermission.checkEnrollmentPermission(enrollment);
        return success(enrollment);
    }

    @PreAuthorize("@ss.hasPermi('education:enrollment:list')")
    @Log(title = "选课管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduCourseEnrollment enrollment)
    {
        educationPermission.checkModuleAction("enrollment", EducationPermissionService.ACTION_ADD);
        if (educationPermission.isStudent() && !educationPermission.isMaster())
        {
            enrollment.setStudentUserId(educationPermission.getCurrentUserId());
        }
        else
        {
            educationPermission.checkUserDeptOwnership(enrollment.getStudentUserId());
        }
        educationPermission.checkCourseOwnership(enrollment.getCourseId(), false);
        enrollment.setCreateBy(getUsername());
        return toAjax(enrollmentService.insertEduCourseEnrollment(enrollment));
    }

    @PreAuthorize("@ss.hasPermi('education:enrollment:list')")
    @Log(title = "选课管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduCourseEnrollment enrollment)
    {
        educationPermission.checkModuleAction("enrollment", EducationPermissionService.ACTION_EDIT);
        EduCourseEnrollment source = enrollmentService.selectEduCourseEnrollmentByEnrollmentId(enrollment.getEnrollmentId());
        educationPermission.checkEnrollmentPermission(source);
        if (educationPermission.isStudent() && !educationPermission.isMaster())
        {
            enrollment.setStudentUserId(source.getStudentUserId());
        }
        else
        {
            educationPermission.checkUserDeptOwnership(enrollment.getStudentUserId());
        }
        educationPermission.checkCourseOwnership(enrollment.getCourseId(), false);
        enrollment.setUpdateBy(getUsername());
        return toAjax(enrollmentService.updateEduCourseEnrollment(enrollment));
    }

    @PreAuthorize("@ss.hasPermi('education:enrollment:list')")
    @Log(title = "选课管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{enrollmentIds}")
    public AjaxResult remove(@PathVariable Long[] enrollmentIds)
    {
        educationPermission.checkModuleAction("enrollment", EducationPermissionService.ACTION_REMOVE);
        for (Long enrollmentId : enrollmentIds)
        {
            EduCourseEnrollment enrollment = enrollmentService.selectEduCourseEnrollmentByEnrollmentId(enrollmentId);
            educationPermission.checkEnrollmentPermission(enrollment);
        }
        return toAjax(enrollmentService.deleteEduCourseEnrollmentByEnrollmentIds(enrollmentIds));
    }
}
