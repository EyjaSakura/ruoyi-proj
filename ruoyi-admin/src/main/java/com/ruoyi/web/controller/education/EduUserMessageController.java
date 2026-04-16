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
import com.ruoyi.system.domain.education.EduUserMessage;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduUserMessageService;

/**
 * 消息提醒Controller
 */
@RestController
@RequestMapping("/education/message")
public class EduUserMessageController extends BaseController
{
    @Autowired
    private IEduUserMessageService messageService;

    @Autowired
    private EducationPermissionService educationPermission;

    @PreAuthorize("@ss.hasPermi('education:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(EduUserMessage message)
    {
        educationPermission.checkModuleAction("message", EducationPermissionService.ACTION_QUERY);
        educationPermission.applyMessageScope(message);
        startPage();
        List<EduUserMessage> list = messageService.selectEduUserMessageList(message);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('education:message:list')")
    @Log(title = "消息提醒", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EduUserMessage message)
    {
        educationPermission.checkModuleAction("message", EducationPermissionService.ACTION_EXPORT);
        educationPermission.applyMessageScope(message);
        List<EduUserMessage> list = messageService.selectEduUserMessageList(message);
        ExcelUtil<EduUserMessage> util = new ExcelUtil<EduUserMessage>(EduUserMessage.class);
        util.exportExcel(response, list, "消息提醒数据");
    }

    @PreAuthorize("@ss.hasPermi('education:message:list')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable Long messageId)
    {
        educationPermission.checkModuleAction("message", EducationPermissionService.ACTION_QUERY);
        EduUserMessage message = messageService.selectEduUserMessageByMessageId(messageId);
        educationPermission.checkMessagePermission(message);
        return success(message);
    }

    @PreAuthorize("@ss.hasPermi('education:message:list')")
    @Log(title = "消息提醒", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EduUserMessage message)
    {
        educationPermission.checkModuleAction("message", EducationPermissionService.ACTION_ADD);
        message.setSenderUserId(educationPermission.getCurrentUserId());
        educationPermission.checkMessagePermission(message);
        message.setCreateBy(getUsername());
        return toAjax(messageService.insertEduUserMessage(message));
    }

    @PreAuthorize("@ss.hasPermi('education:message:list')")
    @Log(title = "消息提醒", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EduUserMessage message)
    {
        educationPermission.checkModuleAction("message", EducationPermissionService.ACTION_EDIT);
        EduUserMessage source = messageService.selectEduUserMessageByMessageId(message.getMessageId());
        educationPermission.checkMessagePermission(source);
        if (educationPermission.isStudent() && !educationPermission.isMaster())
        {
            message.setUserId(source.getUserId());
            message.setCourseId(source.getCourseId());
            message.setAssignmentId(source.getAssignmentId());
            message.setNoticeId(source.getNoticeId());
            message.setBizType(source.getBizType());
            message.setMessageTitle(source.getMessageTitle());
            message.setMessageContent(source.getMessageContent());
            message.setSenderUserId(source.getSenderUserId());
            message.setSendTime(source.getSendTime());
            message.setLinkPath(source.getLinkPath());
            message.setExpireTime(source.getExpireTime());
        }
        else if (!educationPermission.isAdmin())
        {
            message.setSenderUserId(source.getSenderUserId());
            message.setSendTime(source.getSendTime());
        }
        educationPermission.checkMessagePermission(message);
        message.setUpdateBy(getUsername());
        return toAjax(messageService.updateEduUserMessage(message));
    }

    @PreAuthorize("@ss.hasPermi('education:message:list')")
    @Log(title = "消息提醒", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        educationPermission.checkModuleAction("message", EducationPermissionService.ACTION_REMOVE);
        for (Long messageId : messageIds)
        {
            EduUserMessage message = messageService.selectEduUserMessageByMessageId(messageId);
            educationPermission.checkMessagePermission(message);
        }
        return toAjax(messageService.deleteEduUserMessageByMessageIds(messageIds));
    }
}
