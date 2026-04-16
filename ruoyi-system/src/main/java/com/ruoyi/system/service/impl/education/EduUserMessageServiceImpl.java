package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduUserMessage;
import com.ruoyi.system.mapper.education.EduUserMessageMapper;
import com.ruoyi.system.service.education.IEduUserMessageService;

/**
 * 消息提醒Service业务层处理
 */
@Service
public class EduUserMessageServiceImpl implements IEduUserMessageService
{
    @Autowired
    private EduUserMessageMapper messageMapper;

    @Override
    public EduUserMessage selectEduUserMessageByMessageId(Long messageId)
    {
        return messageMapper.selectEduUserMessageByMessageId(messageId);
    }

    @Override
    public List<EduUserMessage> selectEduUserMessageList(EduUserMessage message)
    {
        return messageMapper.selectEduUserMessageList(message);
    }

    @Override
    public int insertEduUserMessage(EduUserMessage message)
    {
        message.setCreateTime(DateUtils.getNowDate());
        return messageMapper.insertEduUserMessage(message);
    }

    @Override
    public int updateEduUserMessage(EduUserMessage message)
    {
        message.setUpdateTime(DateUtils.getNowDate());
        return messageMapper.updateEduUserMessage(message);
    }

    @Override
    public int deleteEduUserMessageByMessageIds(Long[] messageIds)
    {
        return messageMapper.deleteEduUserMessageByMessageIds(messageIds);
    }

    @Override
    public int deleteEduUserMessageByMessageId(Long messageId)
    {
        return messageMapper.deleteEduUserMessageByMessageId(messageId);
    }
}
