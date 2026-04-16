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
import com.ruoyi.system.domain.education.EduAssignment;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduAssignmentService;

/**
 * 作业管理Controller
 */
@RestController
@RequestMapping("/education/assignment")
public class EduAssignmentController extends BaseController
{
    @Autowired
    private IEduAssignmentService assignmentService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:assignment:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduAssignment assignment)
    {
        educationPermission.checkModuleAction("assignment", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyAssignmentScope(assignment);
        startPage();
        List<EduAssignment> list = assignmentService.selectEduAssignmentList(assignment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:assignment:list')")
    @Log(title = "作业管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduAssignment assignment)
    {
        educationPermission.checkModuleAction("assignment", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyAssignmentScope(assignment);
        List<EduAssignment> list = assignmentService.selectEduAssignmentList(assignment);
        ExcelUtil<EduAssignment> util = new ExcelUtil<EduAssignment>(EduAssignment.class);
        util.exportExcel(response, list, "作业管理数据");
    }

    @PreAuthorize("@ss.hasPermi('education:assignment:list')")
    @GetMapping(value = "/{assignmentId}")
    public AjaxResult getInfo(@PathVariable Long assignmentId)
    {
        educationPermission.checkModuleAction("assignment", EducationPermissionService.ACTION_QUERY);
        EduAssignment assignment = assignmentService.selectEduAssignmentByAssignmentId(assignmentId);
        educationPermission.checkAssignmentPermission(assignment);
        return success(assignment);
    }

    @PreAuthorize("@ss.hasPermi('education:assignment:list')")
    @Log(title = "作业管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduAssignment assignment)
    {
        educationPermission.checkModuleAction("assignment", EducationPermissionService.ACTION_ADD);
        educationPermission.checkCourseOwnership(assignment.getCourseId(), true);
        assignment.setPublishUserId(educationPermission.getCurrentUserId());
        assignment.setCreateBy(getUsername());
        return toAjax(assignmentService.insertEduAssignment(assignment));
    }

    @PreAuthorize("@ss.hasPermi('education:assignment:list')")
    @Log(title = "作业管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduAssignment assignment)
    {
        educationPermission.checkModuleAction("assignment", EducationPermissionService.ACTION_EDIT);
        EduAssignment source = assignmentService.selectEduAssignmentByAssignmentId(assignment.getAssignmentId());
        educationPermission.checkAssignmentPermission(source);
        educationPermission.checkCourseOwnership(assignment.getCourseId(), true);
        if (!educationPermission.isAdmin())
        {
            assignment.setPublishUserId(source.getPublishUserId());
            assignment.setPublishTime(source.getPublishTime());
        }
        assignment.setUpdateBy(getUsername());
        return toAjax(assignmentService.updateEduAssignment(assignment));
    }

    @PreAuthorize("@ss.hasPermi('education:assignment:list')")
    @Log(title = "作业管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{assignmentIds}")
    public AjaxResult remove(@PathVariable Long[] assignmentIds)
    {
        educationPermission.checkModuleAction("assignment", EducationPermissionService.ACTION_REMOVE);
        for (Long assignmentId : assignmentIds)
        {
            EduAssignment assignment = assignmentService.selectEduAssignmentByAssignmentId(assignmentId);
            educationPermission.checkAssignmentPermission(assignment);
        }
        return toAjax(assignmentService.deleteEduAssignmentByAssignmentIds(assignmentIds));
    }
}
