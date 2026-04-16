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
import com.ruoyi.system.domain.education.EduLearningProgress;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduLearningProgressService;

/**
 * 学习进度Controller
 */
@RestController
@RequestMapping("/education/progress")
public class EduLearningProgressController extends BaseController
{
    @Autowired
    private IEduLearningProgressService progressService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:progress:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduLearningProgress progress)
    {
        educationPermission.checkModuleAction("progress", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyProgressScope(progress);
        startPage();
        List<EduLearningProgress> list = progressService.selectEduLearningProgressList(progress);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:progress:list')")
    @Log(title = "学习进度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduLearningProgress progress)
    {
        educationPermission.checkModuleAction("progress", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyProgressScope(progress);
        List<EduLearningProgress> list = progressService.selectEduLearningProgressList(progress);
        ExcelUtil<EduLearningProgress> util = new ExcelUtil<EduLearningProgress>(EduLearningProgress.class);
        util.exportExcel(response, list, "学习进度数据");
    }

    @PreAuthorize("@ss.hasPermi('education:progress:list')")
    @GetMapping(value = "/{progressId}")
    public AjaxResult getInfo(@PathVariable Long progressId)
    {
        educationPermission.checkModuleAction("progress", EducationPermissionService.ACTION_QUERY);
        EduLearningProgress progress = progressService.selectEduLearningProgressByProgressId(progressId);
        educationPermission.checkProgressPermission(progress);
        return success(progress);
    }

    @PreAuthorize("@ss.hasPermi('education:progress:list')")
    @Log(title = "学习进度", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduLearningProgress progress)
    {
        educationPermission.checkModuleAction("progress", EducationPermissionService.ACTION_ADD);
        educationPermission.checkProgressPermission(progress);
        progress.setCreateBy(getUsername());
        return toAjax(progressService.insertEduLearningProgress(progress));
    }

    @PreAuthorize("@ss.hasPermi('education:progress:list')")
    @Log(title = "学习进度", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduLearningProgress progress)
    {
        educationPermission.checkModuleAction("progress", EducationPermissionService.ACTION_EDIT);
        EduLearningProgress source = progressService.selectEduLearningProgressByProgressId(progress.getProgressId());
        educationPermission.checkProgressPermission(source);
        educationPermission.checkProgressPermission(progress);
        progress.setUpdateBy(getUsername());
        return toAjax(progressService.updateEduLearningProgress(progress));
    }

    @PreAuthorize("@ss.hasPermi('education:progress:list')")
    @Log(title = "学习进度", businessType = BusinessType.DELETE)
    @DeleteMapping("/{progressIds}")
    public AjaxResult remove(@PathVariable Long[] progressIds)
    {
        educationPermission.checkModuleAction("progress", EducationPermissionService.ACTION_REMOVE);
        for (Long progressId : progressIds)
        {
            EduLearningProgress progress = progressService.selectEduLearningProgressByProgressId(progressId);
            educationPermission.checkProgressPermission(progress);
        }
        return toAjax(progressService.deleteEduLearningProgressByProgressIds(progressIds));
    }
}
