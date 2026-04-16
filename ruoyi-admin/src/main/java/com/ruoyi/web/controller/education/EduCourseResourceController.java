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
import com.ruoyi.system.domain.education.EduCourseResource;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduCourseResourceService;

/**
 * 学习资源Controller
 */
@RestController
@RequestMapping("/education/resource")
public class EduCourseResourceController extends BaseController
{
    @Autowired
    private IEduCourseResourceService resourceService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:resource:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduCourseResource resource)
    {
        educationPermission.checkModuleAction("resource", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyCourseResourceScope(resource);
        startPage();
        List<EduCourseResource> list = resourceService.selectEduCourseResourceList(resource);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:resource:list')")
    @Log(title = "学习资源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduCourseResource resource)
    {
        educationPermission.checkModuleAction("resource", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyCourseResourceScope(resource);
        List<EduCourseResource> list = resourceService.selectEduCourseResourceList(resource);
        ExcelUtil<EduCourseResource> util = new ExcelUtil<EduCourseResource>(EduCourseResource.class);
        util.exportExcel(response, list, "学习资源数据");
    }

    @PreAuthorize("@ss.hasPermi('education:resource:list')")
    @GetMapping(value = "/{resourceId}")
    public AjaxResult getInfo(@PathVariable Long resourceId)
    {
        educationPermission.checkModuleAction("resource", EducationPermissionService.ACTION_QUERY);
        EduCourseResource resource = resourceService.selectEduCourseResourceByResourceId(resourceId);
        educationPermission.checkCourseResourcePermission(resource);
        return success(resource);
    }

    @PreAuthorize("@ss.hasPermi('education:resource:list')")
    @Log(title = "学习资源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduCourseResource resource)
    {
        educationPermission.checkModuleAction("resource", EducationPermissionService.ACTION_ADD);
        educationPermission.checkCourseOwnership(resource.getCourseId(), true);
        resource.setUploadUserId(educationPermission.getCurrentUserId());
        resource.setCreateBy(getUsername());
        return toAjax(resourceService.insertEduCourseResource(resource));
    }

    @PreAuthorize("@ss.hasPermi('education:resource:list')")
    @Log(title = "学习资源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduCourseResource resource)
    {
        educationPermission.checkModuleAction("resource", EducationPermissionService.ACTION_EDIT);
        EduCourseResource source = resourceService.selectEduCourseResourceByResourceId(resource.getResourceId());
        educationPermission.checkCourseResourcePermission(source);
        educationPermission.checkCourseOwnership(resource.getCourseId(), true);
        resource.setUpdateBy(getUsername());
        return toAjax(resourceService.updateEduCourseResource(resource));
    }

    @PreAuthorize("@ss.hasPermi('education:resource:list')")
    @Log(title = "学习资源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{resourceIds}")
    public AjaxResult remove(@PathVariable Long[] resourceIds)
    {
        educationPermission.checkModuleAction("resource", EducationPermissionService.ACTION_REMOVE);
        for (Long resourceId : resourceIds)
        {
            EduCourseResource resource = resourceService.selectEduCourseResourceByResourceId(resourceId);
            educationPermission.checkCourseResourcePermission(resource);
        }
        return toAjax(resourceService.deleteEduCourseResourceByResourceIds(resourceIds));
    }
}
