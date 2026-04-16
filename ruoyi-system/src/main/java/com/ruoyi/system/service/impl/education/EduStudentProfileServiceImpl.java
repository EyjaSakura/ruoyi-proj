package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduStudentProfile;
import com.ruoyi.system.mapper.education.EduStudentProfileMapper;
import com.ruoyi.system.service.education.IEduStudentProfileService;

/**
 * 学生档案Service业务层处理
 */
@Service
public class EduStudentProfileServiceImpl implements IEduStudentProfileService
{
    @Autowired
    private EduStudentProfileMapper studentMapper;

    @Override
    public EduStudentProfile selectEduStudentProfileByStudentId(Long studentId)
    {
        return studentMapper.selectEduStudentProfileByStudentId(studentId);
    }

    @Override
    public List<EduStudentProfile> selectEduStudentProfileList(EduStudentProfile student)
    {
        return studentMapper.selectEduStudentProfileList(student);
    }

    @Override
    public int insertEduStudentProfile(EduStudentProfile student)
    {
        student.setCreateTime(DateUtils.getNowDate());
        return studentMapper.insertEduStudentProfile(student);
    }

    @Override
    public int updateEduStudentProfile(EduStudentProfile student)
    {
        student.setUpdateTime(DateUtils.getNowDate());
        return studentMapper.updateEduStudentProfile(student);
    }

    @Override
    public int deleteEduStudentProfileByStudentIds(Long[] studentIds)
    {
        return studentMapper.deleteEduStudentProfileByStudentIds(studentIds);
    }

    @Override
    public int deleteEduStudentProfileByStudentId(Long studentId)
    {
        return studentMapper.deleteEduStudentProfileByStudentId(studentId);
    }
}
