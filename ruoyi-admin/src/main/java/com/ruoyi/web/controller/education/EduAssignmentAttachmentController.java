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
import com.ruoyi.system.domain.education.EduAssignmentAttachment;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduAssignmentAttachmentService;

/**
 * 作业附件Controller
 */
@RestController
@RequestMapping("/education/assignmentAttachment")
public class EduAssignmentAttachmentController extends BaseController
{
    @Autowired
    private IEduAssignmentAttachmentService assignmentAttachmentService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:assignmentAttachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduAssignmentAttachment assignmentAttachment)
    {
        educationPermission.checkModuleAction("assignmentAttachment", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyAssignmentAttachmentScope(assignmentAttachment);
        startPage();
        List<EduAssignmentAttachment> list = assignmentAttachmentService.selectEduAssignmentAttachmentList(assignmentAttachment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:assignmentAttachment:list')")
    @Log(title = "作业附件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduAssignmentAttachment assignmentAttachment)
    {
        educationPermission.checkModuleAction("assignmentAttachment", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyAssignmentAttachmentScope(assignmentAttachment);
        List<EduAssignmentAttachment> list = assignmentAttachmentService.selectEduAssignmentAttachmentList(assignmentAttachment);
        ExcelUtil<EduAssignmentAttachment> util = new ExcelUtil<EduAssignmentAttachment>(EduAssignmentAttachment.class);
        util.exportExcel(response, list, "作业附件数据");
    }

    @PreAuthorize("@ss.hasPermi('education:assignmentAttachment:list')")
    @GetMapping(value = "/{assignmentAttachmentId}")
    public AjaxResult getInfo(@PathVariable Long assignmentAttachmentId)
    {
        educationPermission.checkModuleAction("assignmentAttachment", EducationPermissionService.ACTION_QUERY);
        EduAssignmentAttachment assignmentAttachment = assignmentAttachmentService.selectEduAssignmentAttachmentByAssignmentAttachmentId(assignmentAttachmentId);
        educationPermission.checkAssignmentAttachmentPermission(assignmentAttachment);
        return success(assignmentAttachment);
    }

    @PreAuthorize("@ss.hasPermi('education:assignmentAttachment:list')")
    @Log(title = "作业附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduAssignmentAttachment assignmentAttachment)
    {
        educationPermission.checkModuleAction("assignmentAttachment", EducationPermissionService.ACTION_ADD);
        educationPermission.checkAssignmentAttachmentPermission(assignmentAttachment);
        assignmentAttachment.setUploadUserId(educationPermission.getCurrentUserId());
        assignmentAttachment.setCreateBy(getUsername());
        return toAjax(assignmentAttachmentService.insertEduAssignmentAttachment(assignmentAttachment));
    }

    @PreAuthorize("@ss.hasPermi('education:assignmentAttachment:list')")
    @Log(title = "作业附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduAssignmentAttachment assignmentAttachment)
    {
        educationPermission.checkModuleAction("assignmentAttachment", EducationPermissionService.ACTION_EDIT);
        EduAssignmentAttachment source = assignmentAttachmentService.selectEduAssignmentAttachmentByAssignmentAttachmentId(assignmentAttachment.getAssignmentAttachmentId());
        educationPermission.checkAssignmentAttachmentPermission(source);
        educationPermission.checkAssignmentAttachmentPermission(assignmentAttachment);
        assignmentAttachment.setUpdateBy(getUsername());
        return toAjax(assignmentAttachmentService.updateEduAssignmentAttachment(assignmentAttachment));
    }

    @PreAuthorize("@ss.hasPermi('education:assignmentAttachment:list')")
    @Log(title = "作业附件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{assignmentAttachmentIds}")
    public AjaxResult remove(@PathVariable Long[] assignmentAttachmentIds)
    {
        educationPermission.checkModuleAction("assignmentAttachment", EducationPermissionService.ACTION_REMOVE);
        for (Long assignmentAttachmentId : assignmentAttachmentIds)
        {
            EduAssignmentAttachment assignmentAttachment = assignmentAttachmentService.selectEduAssignmentAttachmentByAssignmentAttachmentId(assignmentAttachmentId);
            educationPermission.checkAssignmentAttachmentPermission(assignmentAttachment);
        }
        return toAjax(assignmentAttachmentService.deleteEduAssignmentAttachmentByAssignmentAttachmentIds(assignmentAttachmentIds));
    }
}
