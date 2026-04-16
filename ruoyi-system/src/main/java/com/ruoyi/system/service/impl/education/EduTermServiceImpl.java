package com.ruoyi.system.service.impl.education;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.education.EduTerm;
import com.ruoyi.system.mapper.education.EduTermMapper;
import com.ruoyi.system.service.education.IEduTermService;

/**
 * 学期管理Service业务层处理
 */
@Service
public class EduTermServiceImpl implements IEduTermService
{
    @Autowired
    private EduTermMapper termMapper;

    @Override
    public EduTerm selectEduTermByTermId(Long termId)
    {
        return termMapper.selectEduTermByTermId(termId);
    }

    @Override
    public List<EduTerm> selectEduTermList(EduTerm term)
    {
        return termMapper.selectEduTermList(term);
    }

    /** 允许的学期序号合法值（仅第一、第二学期） */
    private static final List<String> VALID_SEMESTER_NO = Arrays.asList("1", "2");

    @Override
    public int insertEduTerm(EduTerm term)
    {
        // 业务校验：学期序号只允许 1/2
        validateSemesterNo(term.getSemesterNo());
        validateTermCode(term);
        term.setCreateTime(DateUtils.getNowDate());
        return termMapper.insertEduTerm(term);
    }

    @Override
    public int updateEduTerm(EduTerm term)
    {
        // 业务校验：学期序号只允许 1/2
        if (StringUtils.isNotEmpty(term.getSemesterNo())) {
            validateSemesterNo(term.getSemesterNo());
        }
        // 业务校验：学期编码唯一性
        validateTermCode(term);
        term.setUpdateTime(DateUtils.getNowDate());
        return termMapper.updateEduTerm(term);
    }

    /**
     * 校验学期序号合法性（仅拦截已知业务约束，不做通用兜底）
     */
    private void validateSemesterNo(String semesterNo) {
        if (StringUtils.isEmpty(semesterNo)) {
            throw new ServiceException("学期序号不能为空");
        }
        if (!VALID_SEMESTER_NO.contains(semesterNo)) {
            throw new ServiceException("学期序号非法，只允许填写 1（第一学期）或 2（第二学期）");
        }
    }

    /**
     * 校验学期编码唯一性（仅拦截已知业务约束，不做通用兜底）
     */
    private void validateTermCode(EduTerm term) {
        if (StringUtils.isEmpty(term.getTermCode())) {
            throw new ServiceException("学期编码不能为空");
        }
        if (termMapper.checkTermCodeUnique(term) > 0) {
            throw new ServiceException("学期编码'" + term.getTermCode() + "'已存在，请勿重复添加");
        }
    }

    @Override
    public int deleteEduTermByTermIds(Long[] termIds)
    {
        return termMapper.deleteEduTermByTermIds(termIds);
    }

    @Override
    public int deleteEduTermByTermId(Long termId)
    {
        return termMapper.deleteEduTermByTermId(termId);
    }
}
