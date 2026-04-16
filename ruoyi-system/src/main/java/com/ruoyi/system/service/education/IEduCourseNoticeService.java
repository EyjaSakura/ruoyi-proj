package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseNotice;

/**
 * 课程公告Service接口
 */
public interface IEduCourseNoticeService
{
    public EduCourseNotice selectEduCourseNoticeByNoticeId(Long noticeId);

    public List<EduCourseNotice> selectEduCourseNoticeList(EduCourseNotice notice);

    public int insertEduCourseNotice(EduCourseNotice notice);

    public int updateEduCourseNotice(EduCourseNotice notice);

    public int deleteEduCourseNoticeByNoticeIds(Long[] noticeIds);

    public int deleteEduCourseNoticeByNoticeId(Long noticeId);
}
