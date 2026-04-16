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
import com.ruoyi.system.domain.education.EduCourseChapter;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduCourseChapterService;

/**
 * 课程章节Controller
 */
@RestController
@RequestMapping("/education/chapter")
public class EduCourseChapterController extends BaseController
{
    @Autowired
    private IEduCourseChapterService chapterService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:chapter:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduCourseChapter chapter)
    {
        educationPermission.checkModuleAction("chapter", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyCourseChapterScope(chapter);
        startPage();
        List<EduCourseChapter> list = chapterService.selectEduCourseChapterList(chapter);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:chapter:list')")
    @Log(title = "课程章节", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduCourseChapter chapter)
    {
        educationPermission.checkModuleAction("chapter", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyCourseChapterScope(chapter);
        List<EduCourseChapter> list = chapterService.selectEduCourseChapterList(chapter);
        ExcelUtil<EduCourseChapter> util = new ExcelUtil<EduCourseChapter>(EduCourseChapter.class);
        util.exportExcel(response, list, "课程章节数据");
    }

    @PreAuthorize("@ss.hasPermi('education:chapter:list')")
    @GetMapping(value = "/{chapterId}")
    public AjaxResult getInfo(@PathVariable Long chapterId)
    {
        educationPermission.checkModuleAction("chapter", EducationPermissionService.ACTION_QUERY);
        EduCourseChapter chapter = chapterService.selectEduCourseChapterByChapterId(chapterId);
        educationPermission.checkCourseChapterPermission(chapter);
        return success(chapter);
    }

    @PreAuthorize("@ss.hasPermi('education:chapter:list')")
    @Log(title = "课程章节", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduCourseChapter chapter)
    {
        educationPermission.checkModuleAction("chapter", EducationPermissionService.ACTION_ADD);
        educationPermission.checkCourseOwnership(chapter.getCourseId(), true);
        chapter.setCreateBy(getUsername());
        return toAjax(chapterService.insertEduCourseChapter(chapter));
    }

    @PreAuthorize("@ss.hasPermi('education:chapter:list')")
    @Log(title = "课程章节", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduCourseChapter chapter)
    {
        educationPermission.checkModuleAction("chapter", EducationPermissionService.ACTION_EDIT);
        EduCourseChapter source = chapterService.selectEduCourseChapterByChapterId(chapter.getChapterId());
        educationPermission.checkCourseChapterPermission(source);
        educationPermission.checkCourseOwnership(chapter.getCourseId(), true);
        chapter.setUpdateBy(getUsername());
        return toAjax(chapterService.updateEduCourseChapter(chapter));
    }

    @PreAuthorize("@ss.hasPermi('education:chapter:list')")
    @Log(title = "课程章节", businessType = BusinessType.DELETE)
    @DeleteMapping("/{chapterIds}")
    public AjaxResult remove(@PathVariable Long[] chapterIds)
    {
        educationPermission.checkModuleAction("chapter", EducationPermissionService.ACTION_REMOVE);
        for (Long chapterId : chapterIds)
        {
            EduCourseChapter chapter = chapterService.selectEduCourseChapterByChapterId(chapterId);
            educationPermission.checkCourseChapterPermission(chapter);
        }
        return toAjax(chapterService.deleteEduCourseChapterByChapterIds(chapterIds));
    }
}
