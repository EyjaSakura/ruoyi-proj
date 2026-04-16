package com.ruoyi.system.mapper.education;

import java.util.List;
import com.ruoyi.system.domain.education.EduStudentProfile;

/**
 * 学生档案Mapper接口
 */
public interface EduStudentProfileMapper
{
    public EduStudentProfile selectEduStudentProfileByStudentId(Long studentId);

    public List<EduStudentProfile> selectEduStudentProfileList(EduStudentProfile student);

    public int insertEduStudentProfile(EduStudentProfile student);

    public int updateEduStudentProfile(EduStudentProfile student);

    public int deleteEduStudentProfileByStudentId(Long studentId);

    public int deleteEduStudentProfileByStudentIds(Long[] studentIds);
}
