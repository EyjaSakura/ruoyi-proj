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
import com.ruoyi.system.domain.education.EduAssignmentSubmission;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduAssignmentSubmissionService;

/**
 * 作业提交Controller
 */
@RestController
@RequestMapping("/education/submission")
public class EduAssignmentSubmissionController extends BaseController
{
    @Autowired
    private IEduAssignmentSubmissionService submissionService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:submission:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduAssignmentSubmission submission)
    {
        educationPermission.checkModuleAction("submission", EducationPermissionService.ACTION_QUERY);
        educationPermission.applySubmissionScope(submission);
        startPage();
        List<EduAssignmentSubmission> list = submissionService.selectEduAssignmentSubmissionList(submission);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:submission:list')")
    @Log(title = "作业提交", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduAssignmentSubmission submission)
    {
        educationPermission.checkModuleAction("submission", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applySubmissionScope(submission);
        List<EduAssignmentSubmission> list = submissionService.selectEduAssignmentSubmissionList(submission);
        ExcelUtil<EduAssignmentSubmission> util = new ExcelUtil<EduAssignmentSubmission>(EduAssignmentSubmission.class);
        util.exportExcel(response, list, "作业提交数据");
    }

    @PreAuthorize("@ss.hasPermi('education:submission:list')")
    @GetMapping(value = "/{submissionId}")
    public AjaxResult getInfo(@PathVariable Long submissionId)
    {
        educationPermission.checkModuleAction("submission", EducationPermissionService.ACTION_QUERY);
        EduAssignmentSubmission submission = submissionService.selectEduAssignmentSubmissionBySubmissionId(submissionId);
        educationPermission.checkSubmissionPermission(submission);
        return success(submission);
    }

    @PreAuthorize("@ss.hasPermi('education:submission:list')")
    @Log(title = "作业提交", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduAssignmentSubmission submission)
    {
        educationPermission.checkModuleAction("submission", EducationPermissionService.ACTION_ADD);
        if (educationPermission.isStudent() && !educationPermission.isMaster())
        {
            submission.setStudentUserId(educationPermission.getCurrentUserId());
            submission.setReviewStatus("0");
            submission.setReviewUserId(null);
            submission.setReviewTime(null);
        }
        educationPermission.checkSubmissionPermission(submission);
        submission.setCreateBy(getUsername());
        return toAjax(submissionService.insertEduAssignmentSubmission(submission));
    }

    @PreAuthorize("@ss.hasPermi('education:submission:list')")
    @Log(title = "作业提交", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduAssignmentSubmission submission)
    {
        educationPermission.checkModuleAction("submission", EducationPermissionService.ACTION_EDIT);
        EduAssignmentSubmission source = submissionService.selectEduAssignmentSubmissionBySubmissionId(submission.getSubmissionId());
        educationPermission.checkSubmissionPermission(source);
        if (educationPermission.isTeacher() && !educationPermission.isMaster())
        {
            submission.setAssignmentId(source.getAssignmentId());
            submission.setCourseId(source.getCourseId());
            submission.setStudentUserId(source.getStudentUserId());
            submission.setSubmitRound(source.getSubmitRound());
            submission.setSubmitRemark(source.getSubmitRemark());
            submission.setSubmitTime(source.getSubmitTime());
            submission.setLateFlag(source.getLateFlag());
            submission.setIsLatest(source.getIsLatest());
            submission.setReviewUserId(educationPermission.getCurrentUserId());
        }
        if (educationPermission.isStudent() && !educationPermission.isMaster())
        {
            submission.setAssignmentId(source.getAssignmentId());
            submission.setCourseId(source.getCourseId());
            submission.setStudentUserId(source.getStudentUserId());
            submission.setReviewStatus(source.getReviewStatus());
            submission.setScore(source.getScore());
            submission.setTeacherComment(source.getTeacherComment());
            submission.setReviewUserId(source.getReviewUserId());
            submission.setReviewTime(source.getReviewTime());
        }
        educationPermission.checkSubmissionPermission(submission);
        submission.setUpdateBy(getUsername());
        return toAjax(submissionService.updateEduAssignmentSubmission(submission));
    }

    @PreAuthorize("@ss.hasPermi('education:submission:list')")
    @Log(title = "作业提交", businessType = BusinessType.DELETE)
    @DeleteMapping("/{submissionIds}")
    public AjaxResult remove(@PathVariable Long[] submissionIds)
    {
        educationPermission.checkModuleAction("submission", EducationPermissionService.ACTION_REMOVE);
        for (Long submissionId : submissionIds)
        {
            EduAssignmentSubmission submission = submissionService.selectEduAssignmentSubmissionBySubmissionId(submissionId);
            educationPermission.checkSubmissionPermission(submission);
        }
        return toAjax(submissionService.deleteEduAssignmentSubmissionBySubmissionIds(submissionIds));
    }
}
