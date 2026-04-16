package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseNotice;

/**
 * 课程公告Mapper接口
 */
public interface EduCourseNoticeMapper
{
    public EduCourseNotice selectEduCourseNoticeByNoticeId(Long noticeId);

    public List<EduCourseNotice> selectEduCourseNoticeList(EduCourseNotice notice);

    public int insertEduCourseNotice(EduCourseNotice notice);

    public int updateEduCourseNotice(EduCourseNotice notice);

    public int deleteEduCourseNoticeByNoticeId(Long noticeId);

    public int deleteEduCourseNoticeByNoticeIds(Long[] noticeIds);
}
