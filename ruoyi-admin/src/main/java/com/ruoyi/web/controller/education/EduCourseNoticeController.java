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
import com.ruoyi.system.domain.education.EduCourseNotice;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduCourseNoticeService;

/**
 * 课程公告Controller
 */
@RestController
@RequestMapping("/education/notice")
public class EduCourseNoticeController extends BaseController
{
    @Autowired
    private IEduCourseNoticeService noticeService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduCourseNotice notice)
    {
        educationPermission.checkModuleAction("notice", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyNoticeScope(notice);
        startPage();
        List<EduCourseNotice> list = noticeService.selectEduCourseNoticeList(notice);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:notice:list')")
    @Log(title = "课程公告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduCourseNotice notice)
    {
        educationPermission.checkModuleAction("notice", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyNoticeScope(notice);
        List<EduCourseNotice> list = noticeService.selectEduCourseNoticeList(notice);
        ExcelUtil<EduCourseNotice> util = new ExcelUtil<EduCourseNotice>(EduCourseNotice.class);
        util.exportExcel(response, list, "课程公告数据");
    }

    @PreAuthorize("@ss.hasPermi('education:notice:list')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        educationPermission.checkModuleAction("notice", EducationPermissionService.ACTION_QUERY);
        EduCourseNotice notice = noticeService.selectEduCourseNoticeByNoticeId(noticeId);
        educationPermission.checkNoticePermission(notice);
        return success(notice);
    }

    @PreAuthorize("@ss.hasPermi('education:notice:list')")
    @Log(title = "课程公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduCourseNotice notice)
    {
        educationPermission.checkModuleAction("notice", EducationPermissionService.ACTION_ADD);
        educationPermission.checkCourseOwnership(notice.getCourseId(), true);
        notice.setPublishUserId(educationPermission.getCurrentUserId());
        notice.setCreateBy(getUsername());
        return toAjax(noticeService.insertEduCourseNotice(notice));
    }

    @PreAuthorize("@ss.hasPermi('education:notice:list')")
    @Log(title = "课程公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduCourseNotice notice)
    {
        educationPermission.checkModuleAction("notice", EducationPermissionService.ACTION_EDIT);
        EduCourseNotice source = noticeService.selectEduCourseNoticeByNoticeId(notice.getNoticeId());
        educationPermission.checkNoticePermission(source);
        educationPermission.checkCourseOwnership(notice.getCourseId(), true);
        if (!educationPermission.isAdmin())
        {
            notice.setPublishUserId(source.getPublishUserId());
            notice.setPublishTime(source.getPublishTime());
        }
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateEduCourseNotice(notice));
    }

    @PreAuthorize("@ss.hasPermi('education:notice:list')")
    @Log(title = "课程公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        educationPermission.checkModuleAction("notice", EducationPermissionService.ACTION_REMOVE);
        for (Long noticeId : noticeIds)
        {
            EduCourseNotice notice = noticeService.selectEduCourseNoticeByNoticeId(noticeId);
            educationPermission.checkNoticePermission(notice);
        }
        return toAjax(noticeService.deleteEduCourseNoticeByNoticeIds(noticeIds));
    }
}
