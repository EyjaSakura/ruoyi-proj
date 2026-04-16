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
import com.ruoyi.system.domain.education.EduTerm;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduTermService;

/**
 * 学期管理Controller
 */
@RestController
@RequestMapping("/education/term")
public class EduTermController extends BaseController
{
    @Autowired
    private IEduTermService termService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:term:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduTerm term)
    {
        educationPermission.checkModuleAction("term", EducationPermissionService.ACTION_QUERY);
        startPage();
        List<EduTerm> list = termService.selectEduTermList(term);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:term:list')")
    @Log(title = "学期管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduTerm term)
    {
        educationPermission.checkModuleAction("term", EducationPermissionService.ACTION_EXPORT);
        List<EduTerm> list = termService.selectEduTermList(term);
        ExcelUtil<EduTerm> util = new ExcelUtil<EduTerm>(EduTerm.class);
        util.exportExcel(response, list, "学期管理数据");
    }

    @PreAuthorize("@ss.hasPermi('education:term:list')")
    @GetMapping(value = "/{termId}")
    public AjaxResult getInfo(@PathVariable Long termId)
    {
        educationPermission.checkModuleAction("term", EducationPermissionService.ACTION_QUERY);
        return success(termService.selectEduTermByTermId(termId));
    }

    @PreAuthorize("@ss.hasPermi('education:term:list')")
    @Log(title = "学期管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduTerm term)
    {
        educationPermission.checkModuleAction("term", EducationPermissionService.ACTION_ADD);
        term.setCreateBy(getUsername());
        return toAjax(termService.insertEduTerm(term));
    }

    @PreAuthorize("@ss.hasPermi('education:term:list')")
    @Log(title = "学期管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduTerm term)
    {
        educationPermission.checkModuleAction("term", EducationPermissionService.ACTION_EDIT);
        term.setUpdateBy(getUsername());
        return toAjax(termService.updateEduTerm(term));
    }

    @PreAuthorize("@ss.hasPermi('education:term:list')")
    @Log(title = "学期管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{termIds}")
    public AjaxResult remove(@PathVariable Long[] termIds)
    {
        educationPermission.checkModuleAction("term", EducationPermissionService.ACTION_REMOVE);
        return toAjax(termService.deleteEduTermByTermIds(termIds));
    }
}
