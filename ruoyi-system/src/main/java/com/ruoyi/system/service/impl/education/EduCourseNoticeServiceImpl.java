package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduCourseNotice;
import com.ruoyi.system.mapper.education.EduCourseNoticeMapper;
import com.ruoyi.system.service.education.IEduCourseNoticeService;

/**
 * 课程公告Service业务层处理
 */
@Service
public class EduCourseNoticeServiceImpl implements IEduCourseNoticeService
{
    @Autowired
    private EduCourseNoticeMapper noticeMapper;

    @Override
    public EduCourseNotice selectEduCourseNoticeByNoticeId(Long noticeId)
    {
        return noticeMapper.selectEduCourseNoticeByNoticeId(noticeId);
    }

    @Override
    public List<EduCourseNotice> selectEduCourseNoticeList(EduCourseNotice notice)
    {
        return noticeMapper.selectEduCourseNoticeList(notice);
    }

    @Override
    public int insertEduCourseNotice(EduCourseNotice notice)
    {
        notice.setCreateTime(DateUtils.getNowDate());
        return noticeMapper.insertEduCourseNotice(notice);
    }

    @Override
    public int updateEduCourseNotice(EduCourseNotice notice)
    {
        notice.setUpdateTime(DateUtils.getNowDate());
        return noticeMapper.updateEduCourseNotice(notice);
    }

    @Override
    public int deleteEduCourseNoticeByNoticeIds(Long[] noticeIds)
    {
        return noticeMapper.deleteEduCourseNoticeByNoticeIds(noticeIds);
    }

    @Override
    public int deleteEduCourseNoticeByNoticeId(Long noticeId)
    {
        return noticeMapper.deleteEduCourseNoticeByNoticeId(noticeId);
    }
}
