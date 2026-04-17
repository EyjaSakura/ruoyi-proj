package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public int insertEduCourseChapter(EduCourseChapter chapter)
    {
        chapter.setCreateTime(DateUtils.getNowDate());
        // 自动计算 ancestors 和 chapterType
        calcAncestorsAndType(chapter);
        return chapterMapper.insertEduCourseChapter(chapter);
    }

    @Override
    @Transactional
    public int updateEduCourseChapter(EduCourseChapter chapter)
    {
        chapter.setUpdateTime(DateUtils.getNowDate());
        // 自动计算 ancestors 和 chapterType（允许外部覆盖）
        calcAncestorsAndType(chapter);
        return chapterMapper.updateEduCourseChapter(chapter);
    }

    /**
     * 自动计算 ancestors（祖级路径）和 chapterType（章节类型）
     * ancestors：父节点的 ancestors + 父节点ID，"0,parentId" 或 "0,pId,gId,parentId"
     * chapterType：父节点为顶级(0)时自动为"1"（章），父节点非顶级时自动为"2"（节）
     */
    private void calcAncestorsAndType(EduCourseChapter chapter)
    {
        Long parentId = chapter.getParentId();
        if (parentId == null)
        {
            parentId = 0L;
            chapter.setParentId(parentId);
        }
        if (parentId == 0)
        {
            // 顶级章节
            chapter.setAncestors("0");
            chapter.setChapterType("1");
        }
        else
        {
            // 查询父节点
            EduCourseChapter parent = chapterMapper.selectEduCourseChapterByChapterId(parentId);
            if (parent != null)
            {
                String parentAncestors = (parent.getAncestors() == null || parent.getAncestors().isEmpty()) ? "0" : parent.getAncestors();
                chapter.setAncestors(parentAncestors + "," + parentId);
                // 父节点是顶级 → 当前是"节"；父节点非顶级 → 当前也是"节"（最多两层）
                chapter.setChapterType(parent.getParentId() == null || parent.getParentId() == 0 ? "2" : "2");
            }
            else
            {
                // 父节点不存在，安全兜底
                chapter.setAncestors("0," + parentId);
                chapter.setChapterType("1");
            }
        }
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
