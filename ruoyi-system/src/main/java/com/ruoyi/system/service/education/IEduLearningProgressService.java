package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduLearningProgress;

/**
 * 学习进度Service接口
 */
public interface IEduLearningProgressService
{
    public EduLearningProgress selectEduLearningProgressByProgressId(Long progressId);

    public List<EduLearningProgress> selectEduLearningProgressList(EduLearningProgress progress);

    public int insertEduLearningProgress(EduLearningProgress progress);

    public int updateEduLearningProgress(EduLearningProgress progress);

    public int deleteEduLearningProgressByProgressIds(Long[] progressIds);

    public int deleteEduLearningProgressByProgressId(Long progressId);
}
