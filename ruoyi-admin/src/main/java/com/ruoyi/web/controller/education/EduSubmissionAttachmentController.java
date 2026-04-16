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
import com.ruoyi.system.domain.education.EduSubmissionAttachment;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduSubmissionAttachmentService;

/**
 * 提交附件Controller
 */
@RestController
@RequestMapping("/education/submissionAttachment")
public class EduSubmissionAttachmentController extends BaseController
{
    @Autowired
    private IEduSubmissionAttachmentService submissionAttachmentService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:submissionAttachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduSubmissionAttachment submissionAttachment)
    {
        educationPermission.checkModuleAction("submissionAttachment", EducationPermissionService.ACTION_QUERY);
        educationPermission.applySubmissionAttachmentScope(submissionAttachment);
        startPage();
        List<EduSubmissionAttachment> list = submissionAttachmentService.selectEduSubmissionAttachmentList(submissionAttachment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:submissionAttachment:list')")
    @Log(title = "提交附件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduSubmissionAttachment submissionAttachment)
    {
        educationPermission.checkModuleAction("submissionAttachment", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applySubmissionAttachmentScope(submissionAttachment);
        List<EduSubmissionAttachment> list = submissionAttachmentService.selectEduSubmissionAttachmentList(submissionAttachment);
        ExcelUtil<EduSubmissionAttachment> util = new ExcelUtil<EduSubmissionAttachment>(EduSubmissionAttachment.class);
        util.exportExcel(response, list, "提交附件数据");
    }

    @PreAuthorize("@ss.hasPermi('education:submissionAttachment:list')")
    @GetMapping(value = "/{submissionAttachmentId}")
    public AjaxResult getInfo(@PathVariable Long submissionAttachmentId)
    {
        educationPermission.checkModuleAction("submissionAttachment", EducationPermissionService.ACTION_QUERY);
        EduSubmissionAttachment submissionAttachment = submissionAttachmentService.selectEduSubmissionAttachmentBySubmissionAttachmentId(submissionAttachmentId);
        educationPermission.checkSubmissionAttachmentPermission(submissionAttachment);
        return success(submissionAttachment);
    }

    @PreAuthorize("@ss.hasPermi('education:submissionAttachment:list')")
    @Log(title = "提交附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduSubmissionAttachment submissionAttachment)
    {
        educationPermission.checkModuleAction("submissionAttachment", EducationPermissionService.ACTION_ADD);
        if (educationPermission.isStudent() && !educationPermission.isMaster())
        {
            submissionAttachment.setUploadUserId(educationPermission.getCurrentUserId());
        }
        educationPermission.checkSubmissionAttachmentPermission(submissionAttachment);
        submissionAttachment.setCreateBy(getUsername());
        return toAjax(submissionAttachmentService.insertEduSubmissionAttachment(submissionAttachment));
    }

    @PreAuthorize("@ss.hasPermi('education:submissionAttachment:list')")
    @Log(title = "提交附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduSubmissionAttachment submissionAttachment)
    {
        educationPermission.checkModuleAction("submissionAttachment", EducationPermissionService.ACTION_EDIT);
        EduSubmissionAttachment source = submissionAttachmentService.selectEduSubmissionAttachmentBySubmissionAttachmentId(submissionAttachment.getSubmissionAttachmentId());
        educationPermission.checkSubmissionAttachmentPermission(source);
        if (educationPermission.isStudent() && !educationPermission.isMaster())
        {
            submissionAttachment.setSubmissionId(source.getSubmissionId());
            submissionAttachment.setUploadUserId(source.getUploadUserId());
        }
        educationPermission.checkSubmissionAttachmentPermission(submissionAttachment);
        submissionAttachment.setUpdateBy(getUsername());
        return toAjax(submissionAttachmentService.updateEduSubmissionAttachment(submissionAttachment));
    }

    @PreAuthorize("@ss.hasPermi('education:submissionAttachment:list')")
    @Log(title = "提交附件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{submissionAttachmentIds}")
    public AjaxResult remove(@PathVariable Long[] submissionAttachmentIds)
    {
        educationPermission.checkModuleAction("submissionAttachment", EducationPermissionService.ACTION_REMOVE);
        for (Long submissionAttachmentId : submissionAttachmentIds)
        {
            EduSubmissionAttachment submissionAttachment = submissionAttachmentService.selectEduSubmissionAttachmentBySubmissionAttachmentId(submissionAttachmentId);
            educationPermission.checkSubmissionAttachmentPermission(submissionAttachment);
        }
        return toAjax(submissionAttachmentService.deleteEduSubmissionAttachmentBySubmissionAttachmentIds(submissionAttachmentIds));
    }
}
