package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduUserMessage;

/**
 * 消息提醒Mapper接口
 */
public interface EduUserMessageMapper
{
    public EduUserMessage selectEduUserMessageByMessageId(Long messageId);

    public List<EduUserMessage> selectEduUserMessageList(EduUserMessage message);

    public int insertEduUserMessage(EduUserMessage message);

    public int updateEduUserMessage(EduUserMessage message);

    public int deleteEduUserMessageByMessageId(Long messageId);

    public int deleteEduUserMessageByMessageIds(Long[] messageIds);
}
