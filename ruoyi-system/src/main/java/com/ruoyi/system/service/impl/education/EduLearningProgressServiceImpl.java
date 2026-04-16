package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduLearningProgress;
import com.ruoyi.system.mapper.education.EduLearningProgressMapper;
import com.ruoyi.system.service.education.IEduLearningProgressService;

/**
 * 学习进度Service业务层处理
 */
@Service
public class EduLearningProgressServiceImpl implements IEduLearningProgressService
{
    @Autowired
    private EduLearningProgressMapper progressMapper;

    @Override
    public EduLearningProgress selectEduLearningProgressByProgressId(Long progressId)
    {
        return progressMapper.selectEduLearningProgressByProgressId(progressId);
    }

    @Override
    public List<EduLearningProgress> selectEduLearningProgressList(EduLearningProgress progress)
    {
        return progressMapper.selectEduLearningProgressList(progress);
    }

    @Override
    public int insertEduLearningProgress(EduLearningProgress progress)
    {
        progress.setCreateTime(DateUtils.getNowDate());
        return progressMapper.insertEduLearningProgress(progress);
    }

    @Override
    public int updateEduLearningProgress(EduLearningProgress progress)
    {
        progress.setUpdateTime(DateUtils.getNowDate());
        return progressMapper.updateEduLearningProgress(progress);
    }

    @Override
    public int deleteEduLearningProgressByProgressIds(Long[] progressIds)
    {
        return progressMapper.deleteEduLearningProgressByProgressIds(progressIds);
    }

    @Override
    public int deleteEduLearningProgressByProgressId(Long progressId)
    {
        return progressMapper.deleteEduLearningProgressByProgressId(progressId);
    }
}
