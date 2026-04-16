package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduTeacherProfile;

/**
 * 教师档案Service接口
 */
public interface IEduTeacherProfileService
{
    public EduTeacherProfile selectEduTeacherProfileByTeacherId(Long teacherId);

    public List<EduTeacherProfile> selectEduTeacherProfileList(EduTeacherProfile teacher);

    public int insertEduTeacherProfile(EduTeacherProfile teacher);

    public int updateEduTeacherProfile(EduTeacherProfile teacher);

    public int deleteEduTeacherProfileByTeacherIds(Long[] teacherIds);

    public int deleteEduTeacherProfileByTeacherId(Long teacherId);
}
