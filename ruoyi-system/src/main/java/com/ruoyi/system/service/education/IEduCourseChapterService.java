package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseChapter;

/**
 * 课程章节Service接口
 */
public interface IEduCourseChapterService
{
    public EduCourseChapter selectEduCourseChapterByChapterId(Long chapterId);

    public List<EduCourseChapter> selectEduCourseChapterList(EduCourseChapter chapter);

    public int insertEduCourseChapter(EduCourseChapter chapter);

    public int updateEduCourseChapter(EduCourseChapter chapter);

    public int deleteEduCourseChapterByChapterIds(Long[] chapterIds);

    public int deleteEduCourseChapterByChapterId(Long chapterId);
}
