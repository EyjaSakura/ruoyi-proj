package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduTeacherProfile;

/**
 * 教师档案Mapper接口
 */
public interface EduTeacherProfileMapper
{
    public EduTeacherProfile selectEduTeacherProfileByTeacherId(Long teacherId);

    public List<EduTeacherProfile> selectEduTeacherProfileList(EduTeacherProfile teacher);

    public int insertEduTeacherProfile(EduTeacherProfile teacher);

    public int updateEduTeacherProfile(EduTeacherProfile teacher);

    public int deleteEduTeacherProfileByTeacherId(Long teacherId);

    public int deleteEduTeacherProfileByTeacherIds(Long[] teacherIds);
}
