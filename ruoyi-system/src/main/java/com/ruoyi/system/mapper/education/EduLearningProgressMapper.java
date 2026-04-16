package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduLearningProgress;

/**
 * 学习进度Mapper接口
 */
public interface EduLearningProgressMapper
{
    public EduLearningProgress selectEduLearningProgressByProgressId(Long progressId);

    public List<EduLearningProgress> selectEduLearningProgressList(EduLearningProgress progress);

    public int insertEduLearningProgress(EduLearningProgress progress);

    public int updateEduLearningProgress(EduLearningProgress progress);

    public int deleteEduLearningProgressByProgressId(Long progressId);

    public int deleteEduLearningProgressByProgressIds(Long[] progressIds);
}
