package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduUserMessage;

/**
 * 消息提醒Service接口
 */
public interface IEduUserMessageService
{
    public EduUserMessage selectEduUserMessageByMessageId(Long messageId);

    public List<EduUserMessage> selectEduUserMessageList(EduUserMessage message);

    public int insertEduUserMessage(EduUserMessage message);

    public int updateEduUserMessage(EduUserMessage message);

    public int deleteEduUserMessageByMessageIds(Long[] messageIds);

    public int deleteEduUserMessageByMessageId(Long messageId);
}
