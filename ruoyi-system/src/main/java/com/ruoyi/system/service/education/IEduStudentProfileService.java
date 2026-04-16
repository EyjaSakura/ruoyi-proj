package com.ruoyi.system.service.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduStudentProfile;

/**
 * 学生档案Service接口
 */
public interface IEduStudentProfileService
{
    public EduStudentProfile selectEduStudentProfileByStudentId(Long studentId);

    public List<EduStudentProfile> selectEduStudentProfileList(EduStudentProfile student);

    public int insertEduStudentProfile(EduStudentProfile student);

    public int updateEduStudentProfile(EduStudentProfile student);

    public int deleteEduStudentProfileByStudentIds(Long[] studentIds);

    public int deleteEduStudentProfileByStudentId(Long studentId);
}
