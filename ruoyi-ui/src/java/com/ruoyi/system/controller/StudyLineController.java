package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.StudyLine;
import com.ruoyi.system.service.IStudyLineService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学习路线管理Controller
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
@RestController
@RequestMapping("/system/studyline")
public class StudyLineController extends BaseController
{
    @Autowired
    private IStudyLineService studyLineService;

    /**
     * 查询学习路线管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:studyline:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudyLine studyLine)
    {
        startPage();
        List<StudyLine> list = studyLineService.selectStudyLineList(studyLine);
        return getDataTable(list);
    }

    /**
     * 导出学习路线管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:studyline:export')")
    @Log(title = "学习路线管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StudyLine studyLine)
    {
        List<StudyLine> list = studyLineService.selectStudyLineList(studyLine);
        ExcelUtil<StudyLine> util = new ExcelUtil<StudyLine>(StudyLine.class);
        util.exportExcel(response, list, "学习路线管理数据");
    }

    /**
     * 获取学习路线管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:studyline:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(studyLineService.selectStudyLineById(id));
    }

    /**
     * 新增学习路线管理
     */
    @PreAuthorize("@ss.hasPermi('system:studyline:add')")
    @Log(title = "学习路线管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StudyLine studyLine)
    {
        return toAjax(studyLineService.insertStudyLine(studyLine));
    }

    /**
     * 修改学习路线管理
     */
    @PreAuthorize("@ss.hasPermi('system:studyline:edit')")
    @Log(title = "学习路线管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StudyLine studyLine)
    {
        return toAjax(studyLineService.updateStudyLine(studyLine));
    }

    /**
     * 删除学习路线管理
     */
    @PreAuthorize("@ss.hasPermi('system:studyline:remove')")
    @Log(title = "学习路线管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(studyLineService.deleteStudyLineByIds(ids));
    }
}
