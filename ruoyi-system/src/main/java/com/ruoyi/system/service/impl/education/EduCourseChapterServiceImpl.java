package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduCourseChapter;
import com.ruoyi.system.mapper.education.EduCourseChapterMapper;
import com.ruoyi.system.service.education.IEduCourseChapterService;

/**
 * 课程章节Service业务层处理
 */
@Service
public class EduCourseChapterServiceImpl implements IEduCourseChapterService
{
    @Autowired
    private EduCourseChapterMapper chapterMapper;

    @Override
    public EduCourseChapter selectEduCourseChapterByChapterId(Long chapterId)
    {
        return chapterMapper.selectEduCourseChapterByChapterId(chapterId);
    }

    @Override
    public List<EduCourseChapter> selectEduCourseChapterList(EduCourseChapter chapter)
    {
        return chapterMapper.selectEduCourseChapterList(chapter);
    }

    @Override
    public int insertEduCourseChapter(EduCourseChapter chapter)
    {
        chapter.setCreateTime(DateUtils.getNowDate());
        return chapterMapper.insertEduCourseChapter(chapter);
    }

    @Override
    public int updateEduCourseChapter(EduCourseChapter chapter)
    {
        chapter.setUpdateTime(DateUtils.getNowDate());
        return chapterMapper.updateEduCourseChapter(chapter);
    }

    @Override
    public int deleteEduCourseChapterByChapterIds(Long[] chapterIds)
    {
        return chapterMapper.deleteEduCourseChapterByChapterIds(chapterIds);
    }

    @Override
    public int deleteEduCourseChapterByChapterId(Long chapterId)
    {
        return chapterMapper.deleteEduCourseChapterByChapterId(chapterId);
    }
}
