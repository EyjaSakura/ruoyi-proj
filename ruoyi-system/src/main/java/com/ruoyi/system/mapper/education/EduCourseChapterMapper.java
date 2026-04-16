package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduCourseChapter;

/**
 * 课程章节Mapper接口
 */
public interface EduCourseChapterMapper
{
    public EduCourseChapter selectEduCourseChapterByChapterId(Long chapterId);

    public List<EduCourseChapter> selectEduCourseChapterList(EduCourseChapter chapter);

    public int insertEduCourseChapter(EduCourseChapter chapter);

    public int updateEduCourseChapter(EduCourseChapter chapter);

    public int deleteEduCourseChapterByChapterId(Long chapterId);

    public int deleteEduCourseChapterByChapterIds(Long[] chapterIds);
}
