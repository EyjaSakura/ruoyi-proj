package com.ruoyi.system.mapper.education;

import java.util.List;
import java.util.Map;

/**
 * 教学统计Mapper接口
 */
public interface EducationStatisticsMapper
{
    public Map<String, Object> selectSummary();

    public List<Map<String, Object>> selectCourseStats();

    public List<Map<String, Object>> selectTeacherWorkload();

    public List<Map<String, Object>> selectDeadlineAssignments();
}
