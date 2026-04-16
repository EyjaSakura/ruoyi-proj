package com.ruoyi.web.controller.education;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEducationStatisticsService;

/**
 * 教学统计Controller
 */
@RestController
@RequestMapping("/education/statistics")
public class EducationStatisticsController extends BaseController
{
    @Autowired
    private IEducationStatisticsService statisticsService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:statistics:list')")
    @GetMapping("/overview")
    public AjaxResult overview()
    {
        educationPermission.checkModuleAction("statistics", EducationPermissionService.ACTION_QUERY);
        return success(statisticsService.getOverview());
    }
}
