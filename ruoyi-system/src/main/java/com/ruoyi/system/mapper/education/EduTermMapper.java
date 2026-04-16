package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduTerm;

/**
 * 学期管理Mapper接口
 */
public interface EduTermMapper
{
    public EduTerm selectEduTermByTermId(Long termId);

    public List<EduTerm> selectEduTermList(EduTerm term);

    public int insertEduTerm(EduTerm term);

    public int updateEduTerm(EduTerm term);

    public int deleteEduTermByTermId(Long termId);

    public int deleteEduTermByTermIds(Long[] termIds);

    /**
     * 校验学期编码唯一性（排除自身）
     *
     * @param term 学期信息
     * @return 结果
     */
    public int checkTermCodeUnique(com.ruoyi.system.domain.education.EduTerm term);
}
