package com.ruoyi.system.service.impl.education;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.education.EduAssignment;
import com.ruoyi.system.domain.education.EduAssignmentSubmission;
import com.ruoyi.system.domain.education.EduCourse;
import com.ruoyi.system.domain.education.EduCourseEnrollment;
import com.ruoyi.system.domain.education.EduCourseTeacher;
import com.ruoyi.system.domain.education.EduStudentProfile;
import com.ruoyi.system.domain.education.EduTeacherProfile;
import com.ruoyi.system.domain.education.EduTerm;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduAssignmentService;
import com.ruoyi.system.service.education.IEduAssignmentSubmissionService;
import com.ruoyi.system.service.education.IEduCourseEnrollmentService;
import com.ruoyi.system.service.education.IEduCourseService;
import com.ruoyi.system.service.education.IEduCourseTeacherService;
import com.ruoyi.system.service.education.IEduStudentProfileService;
import com.ruoyi.system.service.education.IEduTeacherProfileService;
import com.ruoyi.system.service.education.IEduTermService;
import com.ruoyi.system.service.education.IEducationStatisticsService;

/**
 * 教学统计Service业务层处理
 */
@Service
public class EducationStatisticsServiceImpl implements IEducationStatisticsService
{
    @Autowired
    private EducationPermissionService educationPermission;

    @Autowired
    private IEduCourseService courseService;

    @Autowired
    private IEduTeacherProfileService teacherProfileService;

    @Autowired
    private IEduStudentProfileService studentProfileService;

    @Autowired
    private IEduCourseEnrollmentService enrollmentService;

    @Autowired
    private IEduAssignmentService assignmentService;

    @Autowired
    private IEduAssignmentSubmissionService submissionService;

    @Autowired
    private IEduCourseTeacherService courseTeacherService;

    @Autowired
    private IEduTermService termService;

    @Autowired
    private ISysUserService userService;

    @Override
    public Map<String, Object> getOverview()
    {
        EduCourse courseQuery = new EduCourse();
        educationPermission.applyCourseScope(courseQuery, false);
        EduTeacherProfile teacherQuery = new EduTeacherProfile();
        educationPermission.applyTeacherProfileScope(teacherQuery);
        EduStudentProfile studentQuery = new EduStudentProfile();
        educationPermission.applyStudentProfileScope(studentQuery);
        EduCourseEnrollment enrollmentQuery = new EduCourseEnrollment();
        educationPermission.applyEnrollmentScope(enrollmentQuery);
        EduAssignment assignmentQuery = new EduAssignment();
        educationPermission.applyAssignmentScope(assignmentQuery);
        EduAssignmentSubmission submissionQuery = new EduAssignmentSubmission();
        educationPermission.applySubmissionScope(submissionQuery);
        EduCourseTeacher courseTeacherQuery = new EduCourseTeacher();
        educationPermission.applyCourseTeacherScope(courseTeacherQuery);

        List<EduCourse> courses = courseService.selectEduCourseList(courseQuery);
        List<EduTeacherProfile> teachers = teacherProfileService.selectEduTeacherProfileList(teacherQuery);
        List<EduStudentProfile> students = studentProfileService.selectEduStudentProfileList(studentQuery);
        List<EduCourseEnrollment> enrollments = enrollmentService.selectEduCourseEnrollmentList(enrollmentQuery);
        List<EduAssignment> assignments = assignmentService.selectEduAssignmentList(assignmentQuery);
        List<EduAssignmentSubmission> submissions = submissionService.selectEduAssignmentSubmissionList(submissionQuery);
        List<EduCourseTeacher> courseTeachers = courseTeacherService.selectEduCourseTeacherList(courseTeacherQuery);
        Map<Long, String> termNameMap = buildTermNameMap();

        Map<String, Object> result = new HashMap<>();
        result.put("summary", buildSummary(courses, teachers, students, enrollments, assignments, submissions));
        result.put("courseStats", buildCourseStats(courses, enrollments, termNameMap));
        result.put("teacherWorkload", buildTeacherWorkload(courseTeachers, assignments, teachers));
        result.put("deadlineAssignments", buildDeadlineAssignments(assignments, submissions, courses));
        return result;
    }

    private Map<Long, String> buildTermNameMap()
    {
        Map<Long, String> termNameMap = new LinkedHashMap<Long, String>();
        for (EduTerm term : termService.selectEduTermList(new EduTerm()))
        {
            termNameMap.put(term.getTermId(), term.getTermName());
        }
        return termNameMap;
    }

    private Map<String, Object> buildSummary(List<EduCourse> courses, List<EduTeacherProfile> teachers,
        List<EduStudentProfile> students, List<EduCourseEnrollment> enrollments, List<EduAssignment> assignments,
        List<EduAssignmentSubmission> submissions)
    {
        Map<String, Object> summary = new HashMap<String, Object>();
        long enrollmentCount = enrollments.stream().filter(item -> "1".equals(item.getEnrollStatus()) || "3".equals(item.getEnrollStatus())).count();
        long assignmentCount = assignments.stream().filter(item -> "1".equals(item.getPublishStatus())).count();
        long reviewedCount = submissions.stream().filter(item -> "1".equals(item.getReviewStatus())).count();
        double reviewRate = submissions.isEmpty() ? 0D : scale((double) reviewedCount * 100D / submissions.size());
        double avgProgress = enrollments.isEmpty() ? 0D : scale(enrollments.stream()
            .filter(item -> item.getProgressPercent() != null)
            .mapToDouble(item -> item.getProgressPercent().doubleValue())
            .average().orElse(0D));

        summary.put("courseCount", courses.size());
        summary.put("teacherCount", teachers.size());
        summary.put("studentCount", students.size());
        summary.put("enrollmentCount", enrollmentCount);
        summary.put("assignmentCount", assignmentCount);
        summary.put("reviewedCount", reviewedCount);
        summary.put("reviewRate", reviewRate);
        summary.put("avgProgress", avgProgress);
        return summary;
    }

    private List<Map<String, Object>> buildCourseStats(List<EduCourse> courses, List<EduCourseEnrollment> enrollments, Map<Long, String> termNameMap)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<Long, List<EduCourseEnrollment>> enrollmentGroup = new LinkedHashMap<Long, List<EduCourseEnrollment>>();
        for (EduCourseEnrollment enrollment : enrollments)
        {
            enrollmentGroup.computeIfAbsent(enrollment.getCourseId(), key -> new ArrayList<EduCourseEnrollment>()).add(enrollment);
        }
        courses.stream()
            .sorted(Comparator.comparing(EduCourse::getSelectedCount, Comparator.nullsLast(Integer::compareTo)).reversed())
            .limit(8)
            .forEach(course -> {
                List<EduCourseEnrollment> courseEnrollments = enrollmentGroup.getOrDefault(course.getCourseId(), new ArrayList<EduCourseEnrollment>());
                long finishedCount = courseEnrollments.stream().filter(item -> "3".equals(item.getEnrollStatus())).count();
                double avgProgress = scale(courseEnrollments.stream()
                    .filter(item -> item.getProgressPercent() != null)
                    .mapToDouble(item -> item.getProgressPercent().doubleValue())
                    .average().orElse(0D));
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("courseId", course.getCourseId());
                row.put("courseName", course.getCourseName());
                row.put("termName", termNameMap.get(course.getTermId()));
                row.put("selectedCount", course.getSelectedCount());
                row.put("avgProgress", avgProgress);
                row.put("finishedCount", finishedCount);
                result.add(row);
            });
        return result;
    }

    private List<Map<String, Object>> buildTeacherWorkload(List<EduCourseTeacher> courseTeachers, List<EduAssignment> assignments,
        List<EduTeacherProfile> teachers)
    {
        Map<Long, String> teacherNameMap = new LinkedHashMap<Long, String>();
        for (EduTeacherProfile teacher : teachers)
        {
            teacherNameMap.put(teacher.getUserId(), teacher.getTeacherName());
        }
        Map<Long, List<EduCourseTeacher>> teacherCourseMap = new LinkedHashMap<Long, List<EduCourseTeacher>>();
        for (EduCourseTeacher courseTeacher : courseTeachers)
        {
            teacherCourseMap.computeIfAbsent(courseTeacher.getTeacherUserId(), key -> new ArrayList<EduCourseTeacher>()).add(courseTeacher);
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        teacherCourseMap.entrySet().stream()
            .sorted((left, right) -> Integer.compare(right.getValue().size(), left.getValue().size()))
            .limit(8)
            .forEach(entry -> {
                Long teacherUserId = entry.getKey();
                List<EduCourseTeacher> teacherCourses = entry.getValue();
                long assignmentCount = assignments.stream()
                    .filter(item -> teacherCourses.stream().anyMatch(courseTeacher -> courseTeacher.getCourseId().equals(item.getCourseId())))
                    .count();
                long publishedAssignmentCount = assignments.stream()
                    .filter(item -> teacherCourses.stream().anyMatch(courseTeacher -> courseTeacher.getCourseId().equals(item.getCourseId())))
                    .filter(item -> "1".equals(item.getPublishStatus()))
                    .count();
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("teacherUserId", teacherUserId);
                row.put("teacherName", teacherNameMap.getOrDefault(teacherUserId, buildUserNickName(teacherUserId)));
                row.put("courseCount", teacherCourses.size());
                row.put("assignmentCount", assignmentCount);
                row.put("publishedAssignmentCount", publishedAssignmentCount);
                result.add(row);
            });
        return result;
    }

    private List<Map<String, Object>> buildDeadlineAssignments(List<EduAssignment> assignments, List<EduAssignmentSubmission> submissions,
        List<EduCourse> courses)
    {
        Map<Long, String> courseNameMap = new LinkedHashMap<Long, String>();
        for (EduCourse course : courses)
        {
            courseNameMap.put(course.getCourseId(), course.getCourseName());
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        assignments.stream()
            .filter(item -> "1".equals(item.getPublishStatus()))
            .sorted(Comparator.comparing(EduAssignment::getDeadlineTime, Comparator.nullsLast(java.util.Date::compareTo)))
            .limit(8)
            .forEach(assignment -> {
                long pendingReviewCount = submissions.stream()
                    .filter(item -> assignment.getAssignmentId().equals(item.getAssignmentId()))
                    .filter(item -> "1".equals(item.getIsLatest()))
                    .filter(item -> "0".equals(item.getReviewStatus()))
                    .count();
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("assignmentId", assignment.getAssignmentId());
                row.put("courseName", courseNameMap.get(assignment.getCourseId()));
                row.put("title", assignment.getTitle());
                row.put("deadlineTime", assignment.getDeadlineTime());
                row.put("pendingReviewCount", pendingReviewCount);
                result.add(row);
            });
        return result;
    }

    private String buildUserNickName(Long userId)
    {
        if (userId == null)
        {
            return "";
        }
        return userService.selectUserById(userId) != null ? userService.selectUserById(userId).getNickName() : "";
    }

    private double scale(double value)
    {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
