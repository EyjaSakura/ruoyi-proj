package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduTerm;

/**
 * 学期管理Service接口
 */
public interface IEduTermService
{
    public EduTerm selectEduTermByTermId(Long termId);

    public List<EduTerm> selectEduTermList(EduTerm term);

    public int insertEduTerm(EduTerm term);

    public int updateEduTerm(EduTerm term);

    public int deleteEduTermByTermIds(Long[] termIds);

    public int deleteEduTermByTermId(Long termId);
}
