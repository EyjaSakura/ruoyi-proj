package com.ruoyi.system.service.impl.education;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.education.EduTeacherProfile;
import com.ruoyi.system.mapper.education.EduTeacherProfileMapper;
import com.ruoyi.system.service.education.IEduTeacherProfileService;

/**
 * 教师档案Service业务层处理
 */
@Service
public class EduTeacherProfileServiceImpl implements IEduTeacherProfileService
{
    @Autowired
    private EduTeacherProfileMapper teacherMapper;

    @Override
    public EduTeacherProfile selectEduTeacherProfileByTeacherId(Long teacherId)
    {
        return teacherMapper.selectEduTeacherProfileByTeacherId(teacherId);
    }

    @Override
    public List<EduTeacherProfile> selectEduTeacherProfileList(EduTeacherProfile teacher)
    {
        return teacherMapper.selectEduTeacherProfileList(teacher);
    }

    @Override
    public int insertEduTeacherProfile(EduTeacherProfile teacher)
    {
        teacher.setCreateTime(DateUtils.getNowDate());
        return teacherMapper.insertEduTeacherProfile(teacher);
    }

    @Override
    public int updateEduTeacherProfile(EduTeacherProfile teacher)
    {
        teacher.setUpdateTime(DateUtils.getNowDate());
        return teacherMapper.updateEduTeacherProfile(teacher);
    }

    @Override
    public int deleteEduTeacherProfileByTeacherIds(Long[] teacherIds)
    {
        return teacherMapper.deleteEduTeacherProfileByTeacherIds(teacherIds);
    }

    @Override
    public int deleteEduTeacherProfileByTeacherId(Long teacherId)
    {
        return teacherMapper.deleteEduTeacherProfileByTeacherId(teacherId);
    }
}
