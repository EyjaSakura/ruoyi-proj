package com.ruoyi.system.service.education;

import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.education.EduAssignment;
import com.ruoyi.system.domain.education.EduAssignmentAttachment;
import com.ruoyi.system.domain.education.EduAssignmentSubmission;
import com.ruoyi.system.domain.education.EduCourse;
import com.ruoyi.system.domain.education.EduCourseChapter;
import com.ruoyi.system.domain.education.EduCourseEnrollment;
import com.ruoyi.system.domain.education.EduCourseNotice;
import com.ruoyi.system.domain.education.EduCourseResource;
import com.ruoyi.system.domain.education.EduCourseTeacher;
import com.ruoyi.system.domain.education.EduLearningProgress;
import com.ruoyi.system.domain.education.EduStudentProfile;
import com.ruoyi.system.domain.education.EduSubmissionAttachment;
import com.ruoyi.system.domain.education.EduTeacherProfile;
import com.ruoyi.system.domain.education.EduUserMessage;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 教学模块权限与数据范围控制
 */
@Service("eduAuth")
public class EducationPermissionService
{
    public static final String ACTION_QUERY = "query";
    public static final String ACTION_EXPORT = "export";
    public static final String ACTION_ADD = "add";
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_REMOVE = "remove";

    private static final String ROLE_MASTER = "master";
    private static final String ROLE_TEACHER = "teacher";
    private static final String ROLE_STUDENT = "student";

    public static final String PARAM_DENY_ALL = "scopeDenyAll";
    public static final String PARAM_DEPT_ID = "scopeDeptId";
    public static final String PARAM_TEACHER_USER_ID = "scopeTeacherUserId";
    public static final String PARAM_STUDENT_USER_ID = "scopeStudentUserId";
    public static final String PARAM_PUBLISHED_ONLY = "scopePublishedOnly";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IEduCourseService courseService;

    @Autowired
    private IEduCourseTeacherService courseTeacherService;

    @Autowired
    private IEduCourseEnrollmentService enrollmentService;

    @Autowired
    private IEduTeacherProfileService teacherProfileService;

    @Autowired
    private IEduStudentProfileService studentProfileService;

    @Autowired
    private IEduAssignmentService assignmentService;

    @Autowired
    private IEduAssignmentAttachmentService assignmentAttachmentService;

    @Autowired
    private IEduAssignmentSubmissionService submissionService;

    @Autowired
    private IEduSubmissionAttachmentService submissionAttachmentService;

    @Autowired
    private IEduCourseChapterService chapterService;

    @Autowired
    private IEduCourseResourceService resourceService;

    @Autowired
    private IEduCourseNoticeService noticeService;

    @Autowired
    private IEduLearningProgressService progressService;

    @Autowired
    private IEduUserMessageService messageService;

    public boolean isAdmin()
    {
        Long userId = SecurityUtils.getUserId();
        return SecurityUtils.isAdmin(userId) || SecurityUtils.hasRole("admin");
    }

    public boolean isMaster()
    {
        return SecurityUtils.hasRole(ROLE_MASTER);
    }

    public boolean isTeacher()
    {
        return SecurityUtils.hasRole(ROLE_TEACHER);
    }

    public boolean isStudent()
    {
        return SecurityUtils.hasRole(ROLE_STUDENT);
    }

    public Long getCurrentUserId()
    {
        return SecurityUtils.getUserId();
    }

    public Long getCurrentDeptId()
    {
        return SecurityUtils.getDeptId();
    }

    public void checkModuleAction(String module, String action)
    {
        if (!hasModuleAction(module, action))
        {
            throw new ServiceException("当前账号无权执行该操作");
        }
    }

    public boolean hasModuleAction(String module, String action)
    {
        if (isAdmin())
        {
            return true;
        }
        if (isMaster())
        {
            return !matchesAction(module, action, "statistics") || ACTION_QUERY.equals(action);
        }
        if (isTeacher())
        {
            return matchesTeacherAction(module, action);
        }
        if (isStudent())
        {
            return matchesStudentAction(module, action);
        }
        return false;
    }

    public void applyTeacherProfileScope(EduTeacherProfile query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            query.setDeptId(getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            query.setUserId(getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    public void applyStudentProfileScope(EduStudentProfile query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            query.setDeptId(getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            query.setUserId(getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    public void applyCourseScope(EduCourse query, boolean studentBrowse)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            query.setDeptId(getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            if (studentBrowse)
            {
                putParam(query, PARAM_PUBLISHED_ONLY, Boolean.TRUE);
            }
            else
            {
                putParam(query, PARAM_STUDENT_USER_ID, getCurrentUserId());
            }
            return;
        }
        denyAll(query);
    }

    public void applyCourseTeacherScope(EduCourseTeacher query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            putParam(query, PARAM_DEPT_ID, getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            query.setTeacherUserId(getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    public void applyCourseChapterScope(EduCourseChapter query)
    {
        applyCourseOwnedScope(query);
    }

    public void applyCourseResourceScope(EduCourseResource query)
    {
        applyCourseOwnedScope(query);
    }

    public void applyEnrollmentScope(EduCourseEnrollment query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            putParam(query, PARAM_DEPT_ID, getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            query.setStudentUserId(getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    public void applyProgressScope(EduLearningProgress query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            putParam(query, PARAM_DEPT_ID, getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            query.setStudentUserId(getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    public void applyAssignmentScope(EduAssignment query)
    {
        applyCourseOwnedScope(query);
    }

    public void applyAssignmentAttachmentScope(EduAssignmentAttachment query)
    {
        applyAssignmentOwnedScope(query);
    }

    public void applySubmissionScope(EduAssignmentSubmission query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            putParam(query, PARAM_DEPT_ID, getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            query.setStudentUserId(getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    public void applySubmissionAttachmentScope(EduSubmissionAttachment query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            putParam(query, PARAM_DEPT_ID, getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            query.setUploadUserId(getCurrentUserId());
            putParam(query, PARAM_STUDENT_USER_ID, getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    public void applyNoticeScope(EduCourseNotice query)
    {
        applyCourseOwnedScope(query);
    }

    public void applyMessageScope(EduUserMessage query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            putParam(query, PARAM_DEPT_ID, getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            query.setUserId(getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    public void checkTeacherProfilePermission(EduTeacherProfile teacher, String action)
    {
        requireNotNull(teacher);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            requireDeptMatch(teacher.getDeptId());
            return;
        }
        if (isTeacher() && Arrays.asList(ACTION_QUERY, ACTION_EDIT).contains(action))
        {
            requireSelfUser(teacher.getUserId());
            return;
        }
        throw new ServiceException("无权操作该教师档案");
    }

    public void checkStudentProfilePermission(EduStudentProfile student, String action)
    {
        requireNotNull(student);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            requireDeptMatch(student.getDeptId());
            return;
        }
        if (isTeacher() && Arrays.asList(ACTION_QUERY, ACTION_EXPORT).contains(action))
        {
            requireTeacherStudent(student.getUserId());
            return;
        }
        if (isStudent() && Arrays.asList(ACTION_QUERY, ACTION_EDIT).contains(action))
        {
            requireSelfUser(student.getUserId());
            return;
        }
        throw new ServiceException("无权操作该学生档案");
    }

    public void checkCoursePermission(EduCourse course, boolean studentBrowse)
    {
        requireNotNull(course);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            requireDeptMatch(course.getDeptId());
            return;
        }
        if (isTeacher())
        {
            requireTeacherCourse(course.getCourseId());
            return;
        }
        if (isStudent())
        {
            if (studentBrowse)
            {
                if (!StringUtils.equals(course.getStatus(), "1"))
                {
                    throw new ServiceException("只能查看已发布课程");
                }
                return;
            }
            requireStudentCourse(course.getCourseId(), getCurrentUserId());
            return;
        }
        throw new ServiceException("无权查看该课程");
    }

    public void checkCourseTeacherPermission(EduCourseTeacher courseTeacher)
    {
        requireNotNull(courseTeacher);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            requireCourseDeptMatch(courseTeacher.getCourseId());
            return;
        }
        if (isTeacher())
        {
            requireTeacherCourse(courseTeacher.getCourseId());
            return;
        }
        throw new ServiceException("无权查看该授课安排");
    }

    public void checkCourseChapterPermission(EduCourseChapter chapter)
    {
        requireNotNull(chapter);
        checkCourseOwnedPermission(chapter.getCourseId(), true);
    }

    public void checkCourseResourcePermission(EduCourseResource resource)
    {
        requireNotNull(resource);
        checkCourseOwnedPermission(resource.getCourseId(), true);
    }

    public void checkEnrollmentPermission(EduCourseEnrollment enrollment)
    {
        requireNotNull(enrollment);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            requireCourseDeptMatch(enrollment.getCourseId());
            return;
        }
        if (isTeacher())
        {
            requireTeacherCourse(enrollment.getCourseId());
            return;
        }
        if (isStudent())
        {
            requireSelfUser(enrollment.getStudentUserId());
            return;
        }
        throw new ServiceException("无权操作该选课记录");
    }

    public void checkProgressPermission(EduLearningProgress progress)
    {
        requireNotNull(progress);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            requireCourseDeptMatch(progress.getCourseId());
            return;
        }
        if (isTeacher())
        {
            requireTeacherCourse(progress.getCourseId());
            return;
        }
        if (isStudent())
        {
            requireSelfUser(progress.getStudentUserId());
            return;
        }
        throw new ServiceException("无权查看该学习进度");
    }

    public void checkAssignmentPermission(EduAssignment assignment)
    {
        requireNotNull(assignment);
        checkCourseOwnedPermission(assignment.getCourseId(), true);
    }

    public void checkAssignmentAttachmentPermission(EduAssignmentAttachment attachment)
    {
        requireNotNull(attachment);
        EduAssignment assignment = assignmentService.selectEduAssignmentByAssignmentId(attachment.getAssignmentId());
        checkAssignmentPermission(assignment);
    }

    public void checkSubmissionPermission(EduAssignmentSubmission submission)
    {
        requireNotNull(submission);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            requireCourseDeptMatch(submission.getCourseId());
            return;
        }
        if (isTeacher())
        {
            requireTeacherCourse(submission.getCourseId());
            return;
        }
        if (isStudent())
        {
            requireSelfUser(submission.getStudentUserId());
            return;
        }
        throw new ServiceException("无权操作该作业提交");
    }

    public void checkSubmissionAttachmentPermission(EduSubmissionAttachment attachment)
    {
        requireNotNull(attachment);
        EduAssignmentSubmission submission = submissionService.selectEduAssignmentSubmissionBySubmissionId(attachment.getSubmissionId());
        checkSubmissionPermission(submission);
    }

    public void checkNoticePermission(EduCourseNotice notice)
    {
        requireNotNull(notice);
        checkCourseOwnedPermission(notice.getCourseId(), true);
    }

    public void checkMessagePermission(EduUserMessage message)
    {
        requireNotNull(message);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            if (message.getCourseId() != null)
            {
                requireCourseDeptMatch(message.getCourseId());
            }
            else
            {
                requireUserDeptMatch(message.getUserId());
            }
            return;
        }
        if (isTeacher())
        {
            if (equalsCurrentUser(message.getSenderUserId()) || equalsCurrentUser(message.getUserId()))
            {
                return;
            }
            if (message.getCourseId() != null && isTeacherCourse(message.getCourseId()))
            {
                return;
            }
            if (message.getUserId() != null && isTeacherStudent(message.getUserId()))
            {
                return;
            }
            throw new ServiceException("无权查看该消息记录");
        }
        if (isStudent())
        {
            requireSelfUser(message.getUserId());
            return;
        }
        throw new ServiceException("无权查看该消息记录");
    }

    public EduTeacherProfile findTeacherProfileByUserId(Long userId)
    {
        if (userId == null)
        {
            return null;
        }
        EduTeacherProfile query = new EduTeacherProfile();
        query.setUserId(userId);
        List<EduTeacherProfile> list = teacherProfileService.selectEduTeacherProfileList(query);
        return list.isEmpty() ? null : list.get(0);
    }

    public EduStudentProfile findStudentProfileByUserId(Long userId)
    {
        if (userId == null)
        {
            return null;
        }
        EduStudentProfile query = new EduStudentProfile();
        query.setUserId(userId);
        List<EduStudentProfile> list = studentProfileService.selectEduStudentProfileList(query);
        return list.isEmpty() ? null : list.get(0);
    }

    private void applyCourseOwnedScope(BaseEntity query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            putParam(query, PARAM_DEPT_ID, getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            putParam(query, PARAM_STUDENT_USER_ID, getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    private void applyAssignmentOwnedScope(BaseEntity query)
    {
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            putParam(query, PARAM_DEPT_ID, getCurrentDeptId());
            return;
        }
        if (isTeacher())
        {
            putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
            return;
        }
        if (isStudent())
        {
            putParam(query, PARAM_STUDENT_USER_ID, getCurrentUserId());
            return;
        }
        denyAll(query);
    }

    private boolean matchesTeacherAction(String module, String action)
    {
        if (matchesAction(module, action, "statistics"))
        {
            return ACTION_QUERY.equals(action);
        }
        if (matchesAction(module, action, "teacher"))
        {
            return Arrays.asList(ACTION_QUERY, ACTION_EDIT).contains(action);
        }
        if (matchesAction(module, action, "course"))
        {
            return Arrays.asList(ACTION_QUERY, ACTION_EXPORT, ACTION_ADD).contains(action);
        }
        if (matchesAction(module, action, "student", "courseTeacher", "enrollment", "progress", "submission", "submissionAttachment", "term"))
        {
            return Arrays.asList(ACTION_QUERY, ACTION_EXPORT).contains(action) || ("submission".equals(module) && ACTION_EDIT.equals(action));
        }
        if (matchesAction(module, action, "chapter", "resource", "assignment", "assignmentAttachment", "notice", "message"))
        {
            return true;
        }
        return false;
    }

    private boolean matchesStudentAction(String module, String action)
    {
        if (matchesAction(module, action, "student"))
        {
            return Arrays.asList(ACTION_QUERY, ACTION_EDIT).contains(action);
        }
        if (matchesAction(module, action, "course"))
        {
            return ACTION_QUERY.equals(action);
        }
        if (matchesAction(module, action, "enrollment", "submission", "submissionAttachment"))
        {
            return Arrays.asList(ACTION_QUERY, ACTION_ADD, ACTION_EDIT, ACTION_REMOVE).contains(action);
        }
        if (matchesAction(module, action, "message"))
        {
            return Arrays.asList(ACTION_QUERY, ACTION_EDIT).contains(action);
        }
        if (matchesAction(module, action, "resource", "assignment", "assignmentAttachment", "notice", "progress"))
        {
            return ACTION_QUERY.equals(action);
        }
        return false;
    }

    private boolean matchesAction(String module, String action, String... modules)
    {
        return Arrays.asList(modules).contains(module)
            && Arrays.asList(ACTION_QUERY, ACTION_EXPORT, ACTION_ADD, ACTION_EDIT, ACTION_REMOVE).contains(action);
    }

    public void checkDeptOwnership(Long deptId)
    {
        if (!isAdmin())
        {
            requireDeptMatch(deptId);
        }
    }

    public void checkSelfOwnership(Long userId)
    {
        requireSelfUser(userId);
    }

    public void checkTeacherCourseOwnership(Long courseId)
    {
        if (!isAdmin())
        {
            requireTeacherCourse(courseId);
        }
    }

    public void checkTeacherStudentOwnership(Long studentUserId)
    {
        if (!isAdmin())
        {
            requireTeacherStudent(studentUserId);
        }
    }

    public void checkCourseOwnership(Long courseId, boolean studentRequireEnrollment)
    {
        checkCourseOwnedPermission(courseId, studentRequireEnrollment);
    }

    public void checkUserDeptOwnership(Long userId)
    {
        if (!isAdmin())
        {
            requireUserDeptMatch(userId);
        }
    }

    private void checkCourseOwnedPermission(Long courseId, boolean studentRequireEnrollment)
    {
        EduCourse course = courseService.selectEduCourseByCourseId(courseId);
        requireNotNull(course);
        if (isAdmin())
        {
            return;
        }
        if (isMaster())
        {
            requireDeptMatch(course.getDeptId());
            return;
        }
        if (isTeacher())
        {
            requireTeacherCourse(courseId);
            return;
        }
        if (isStudent())
        {
            if (studentRequireEnrollment)
            {
                requireStudentCourse(courseId, getCurrentUserId());
            }
            else if (!StringUtils.equals(course.getStatus(), "1"))
            {
                throw new ServiceException("只能查看已发布课程");
            }
            return;
        }
        throw new ServiceException("无权操作该课程关联数据");
    }

    private void requireTeacherCourse(Long courseId)
    {
        if (!isTeacherCourse(courseId))
        {
            throw new ServiceException("无权操作非本人授课课程数据");
        }
    }

    private boolean isTeacherCourse(Long courseId)
    {
        EduCourseTeacher query = new EduCourseTeacher();
        query.setCourseId(courseId);
        query.setTeacherUserId(getCurrentUserId());
        List<EduCourseTeacher> list = courseTeacherService.selectEduCourseTeacherList(query);
        return !list.isEmpty();
    }

    private void requireTeacherStudent(Long studentUserId)
    {
        if (!isTeacherStudent(studentUserId))
        {
            throw new ServiceException("无权查看非本人学生数据");
        }
    }

    private boolean isTeacherStudent(Long studentUserId)
    {
        EduCourseEnrollment query = new EduCourseEnrollment();
        query.setStudentUserId(studentUserId);
        putParam(query, PARAM_TEACHER_USER_ID, getCurrentUserId());
        List<EduCourseEnrollment> list = enrollmentService.selectEduCourseEnrollmentList(query);
        return !list.isEmpty();
    }

    private void requireStudentCourse(Long courseId, Long studentUserId)
    {
        EduCourseEnrollment query = new EduCourseEnrollment();
        query.setCourseId(courseId);
        query.setStudentUserId(studentUserId);
        List<EduCourseEnrollment> list = enrollmentService.selectEduCourseEnrollmentList(query);
        if (list.isEmpty())
        {
            throw new ServiceException("无权操作非本人课程数据");
        }
    }

    private void requireCourseDeptMatch(Long courseId)
    {
        EduCourse course = courseService.selectEduCourseByCourseId(courseId);
        requireNotNull(course);
        requireDeptMatch(course.getDeptId());
    }

    private void requireUserDeptMatch(Long userId)
    {
        SysUser user = userService.selectUserById(userId);
        if (user == null || !equalsLong(user.getDeptId(), getCurrentDeptId()))
        {
            throw new ServiceException("无权查看非本院系数据");
        }
    }

    private void requireDeptMatch(Long deptId)
    {
        if (!equalsLong(deptId, getCurrentDeptId()))
        {
            throw new ServiceException("无权操作非本院系数据");
        }
    }

    private void requireSelfUser(Long userId)
    {
        if (!equalsCurrentUser(userId))
        {
            throw new ServiceException("只能操作自己的数据");
        }
    }

    private boolean equalsCurrentUser(Long userId)
    {
        return equalsLong(userId, getCurrentUserId());
    }

    private boolean equalsLong(Long source, Long target)
    {
        return source != null && target != null && source.longValue() == target.longValue();
    }

    private void requireNotNull(Object entity)
    {
        if (entity == null)
        {
            throw new ServiceException("数据不存在或已被删除");
        }
    }

    private void denyAll(BaseEntity entity)
    {
        entity.getParams().put(PARAM_DENY_ALL, Boolean.TRUE);
    }

    private void putParam(BaseEntity entity, String key, Object value)
    {
        if (value != null)
        {
            entity.getParams().put(key, value);
        }
    }
}
