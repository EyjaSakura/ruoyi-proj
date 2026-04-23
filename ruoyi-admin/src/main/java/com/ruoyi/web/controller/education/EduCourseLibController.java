package com.ruoyi.web.controller.education;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.education.EduCourse;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduCourseLibService;

/**
 * 课程库管理Controller
 */
@RestController
@RequestMapping("/education/courseLib")
public class EduCourseLibController extends BaseController
{
    @Autowired
    private IEduCourseLibService courseLibService;

    @Autowired
    private EducationPermissionService educationPermission;

    /**
     * 查询课程库列表
     */
    @PreAuthorize("@ss.hasAnyPermi('education:courseLib:list,education:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduCourse eduCourse)
    {
        startPage();
        // 固定查询 termId = 9999 的课程库数据
        eduCourse.setTermId(9999L);
        List<EduCourse> list = courseLibService.selectCourseLibList(eduCourse);
        return getDataTable(list);
    }

    /**
     * 导出课程库列表
     */
    @PreAuthorize("@ss.hasAnyPermi('education:courseLib:export,education:course:export')")
    @Log(title = "课程库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduCourse eduCourse)
    {
        // 固定查询 termId = 9999 的课程库数据
        eduCourse.setTermId(9999L);
        List<EduCourse> list = courseLibService.selectCourseLibList(eduCourse);
        ExcelUtil<EduCourse> util = new ExcelUtil<EduCourse>(EduCourse.class);
        util.exportExcel(response, list, "课程库数据");
    }

    /**
     * 获取课程库详细信息
     */
    @PreAuthorize("@ss.hasAnyPermi('education:courseLib:query,education:course:query')")
    @GetMapping(value = "/{courseId}")
    public AjaxResult getInfo(@PathVariable("courseId") Long courseId)
    {
        return success(courseLibService.selectCourseLibByCourseId(courseId));
    }

    /**
     * 新增课程库
     */
    @PreAuthorize("@ss.hasAnyPermi('education:courseLib:add,education:course:add')")
    @Log(title = "课程库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduCourse eduCourse)
    {
        // 固定设置 termId = 9999
        eduCourse.setTermId(9999L);
        // 学院管理员（deptId 由前端隐藏，null 时自动取当前用户所属学院）
        if (eduCourse.getDeptId() == null && !educationPermission.isAdmin())
        {
            eduCourse.setDeptId(educationPermission.getCurrentDeptId());
        }
        return toAjax(courseLibService.insertCourseLib(eduCourse));
    }

    /**
     * 修改课程库
     */
    @PreAuthorize("@ss.hasAnyPermi('education:courseLib:edit,education:course:edit')")
    @Log(title = "课程库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduCourse eduCourse)
    {
        return toAjax(courseLibService.updateCourseLib(eduCourse));
    }

    /**
     * 删除课程库
     */
    @PreAuthorize("@ss.hasAnyPermi('education:courseLib:remove,education:course:remove')")
    @Log(title = "课程库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{courseIds}")
    public AjaxResult remove(@PathVariable Long[] courseIds)
    {
        return toAjax(courseLibService.deleteCourseLibByCourseIds(courseIds));
    }

    /**
     * 下载导入模板
     */
    @PreAuthorize("@ss.hasAnyPermi('education:courseLib:import,education:course:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<EduCourse> util = new ExcelUtil<EduCourse>(EduCourse.class);
        util.importTemplateExcel(response, "课程库导入模板");
    }

    /**
     * 导入课程库数据
     */
    @PreAuthorize("@ss.hasAnyPermi('education:courseLib:import,education:course:import')")
    @Log(title = "课程库", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<EduCourse> util = new ExcelUtil<EduCourse>(EduCourse.class);
        List<EduCourse> courseList = util.importExcel(file.getInputStream());
        
        // 设置默认值
        for (EduCourse course : courseList) {
            // 设置为课程库学期
            course.setTermId(9999L);
            // 课序号默认值
            if (course.getClassNo() == null || course.getClassNo().isEmpty()) {
                course.setClassNo("00");
            }
            // 总课时默认值
            if (course.getTotalHours() == null) {
                course.setTotalHours(0);
            }
            // 容量上限默认值（0表示不限）
            if (course.getCapacity() == null) {
                course.setCapacity(0);
            }
        }
        
        return courseLibService.importCourseLib(courseList, updateSupport);
    }
}
