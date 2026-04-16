package com.ruoyi.web.controller.education;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.education.EduAssignment;
import com.ruoyi.system.domain.education.EduAssignmentSubmission;
import com.ruoyi.system.domain.education.EduCourse;
import com.ruoyi.system.domain.education.EduCourseChapter;
import com.ruoyi.system.domain.education.EduCourseNotice;
import com.ruoyi.system.domain.education.EduStudentProfile;
import com.ruoyi.system.domain.education.EduTeacherProfile;
import com.ruoyi.system.domain.education.EduTerm;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.education.IEduAssignmentService;
import com.ruoyi.system.service.education.IEduAssignmentSubmissionService;
import com.ruoyi.system.service.education.IEduCourseChapterService;
import com.ruoyi.system.service.education.IEduCourseNoticeService;
import com.ruoyi.system.service.education.IEduCourseService;
import com.ruoyi.system.service.education.IEduStudentProfileService;
import com.ruoyi.system.service.education.IEduTeacherProfileService;
import com.ruoyi.system.service.education.EducationPermissionService;
import com.ruoyi.system.service.education.IEduTermService;

/**
 * 教学管理通用下拉数据
 */
@RestController
@RequestMapping("/education/common")
public class EducationCommonController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IEduTermService termService;

    @Autowired
    private IEduCourseService courseService;

    @Autowired
    private IEduTeacherProfileService teacherService;

    @Autowired
    private IEduStudentProfileService studentService;

    @Autowired
    private IEduAssignmentService assignmentService;

    @Autowired
    private IEduCourseChapterService chapterService;

    @Autowired
    private IEduCourseNoticeService noticeService;

    @Autowired
    private IEduAssignmentSubmissionService submissionService;

    @Autowired
    private EducationPermissionService educationPermission;

    /**
     * 获取启用中的院系树
     */
    @PreAuthorize("@ss.hasAnyPermi('education:teacher:list,education:student:list')")
    @GetMapping("/deptTree")
    public AjaxResult deptTree()
    {
        return success(buildDeptOptions());
    }

    /**
     * 获取教学模块通用选项数据
     */
    @PreAuthorize("@ss.hasAnyPermi('education:teacher:list,education:student:list,education:course:list,education:assignment:list,education:chapter:list,education:courseTeacher:list,education:enrollment:list,education:resource:list,education:notice:list,education:progress:list,education:submission:list,education:assignmentAttachment:list,education:submissionAttachment:list,education:message:list')")
    @GetMapping("/options")
    public AjaxResult options()
    {
        AjaxResult ajax = success();

        ajax.put("deptOptions", buildDeptOptions());

        EduCourse courseQuery = new EduCourse();
        educationPermission.applyCourseScope(courseQuery, educationPermission.isStudent());
        EduTeacherProfile teacherQuery = new EduTeacherProfile();
        educationPermission.applyTeacherProfileScope(teacherQuery);
        EduStudentProfile studentQuery = new EduStudentProfile();
        educationPermission.applyStudentProfileScope(studentQuery);
        EduAssignment assignmentQuery = new EduAssignment();
        educationPermission.applyAssignmentScope(assignmentQuery);
        EduCourseChapter chapterQuery = new EduCourseChapter();
        educationPermission.applyCourseChapterScope(chapterQuery);
        EduCourseNotice noticeQuery = new EduCourseNotice();
        educationPermission.applyNoticeScope(noticeQuery);
        EduAssignmentSubmission submissionQuery = new EduAssignmentSubmission();
        educationPermission.applySubmissionScope(submissionQuery);

        List<EduTerm> terms = termService.selectEduTermList(new EduTerm());
        List<EduCourse> courses = courseService.selectEduCourseList(courseQuery);
        List<EduTeacherProfile> teachers = teacherService.selectEduTeacherProfileList(teacherQuery);
        List<EduStudentProfile> students = studentService.selectEduStudentProfileList(studentQuery);
        List<EduAssignment> assignments = assignmentService.selectEduAssignmentList(assignmentQuery);
        List<EduCourseChapter> chapters = chapterService.selectEduCourseChapterList(chapterQuery);
        List<EduCourseNotice> notices = noticeService.selectEduCourseNoticeList(noticeQuery);
        List<EduAssignmentSubmission> submissions = submissionService.selectEduAssignmentSubmissionList(submissionQuery);

        List<SysUser> users = buildScopedUsers(teachers, students);

        Map<Long, String> courseLabelMap = new LinkedHashMap<Long, String>();
        for (EduCourse course : courses)
        {
            courseLabelMap.put(course.getCourseId(), buildCourseLabel(course));
        }

        Map<Long, String> studentLabelMap = new LinkedHashMap<Long, String>();
        for (EduStudentProfile student : students)
        {
            studentLabelMap.put(student.getUserId(), buildNameWithCode(student.getStudentName(), student.getStudentNo()));
        }

        Map<Long, String> assignmentLabelMap = new LinkedHashMap<Long, String>();
        for (EduAssignment assignment : assignments)
        {
            assignmentLabelMap.put(assignment.getAssignmentId(),
                buildAssociationLabel(assignment.getTitle(), courseLabelMap.get(assignment.getCourseId())));
        }

        ajax.put("termOptions", toTermOptions(terms));
        ajax.put("courseOptions", toOptions(courses, EduCourse::getCourseId, this::buildCourseLabel));
        ajax.put("teacherUserOptions", toOptions(teachers, EduTeacherProfile::getUserId,
            item -> buildNameWithCode(item.getTeacherName(), item.getTeacherNo())));
        ajax.put("studentUserOptions", toOptions(students, EduStudentProfile::getUserId,
            item -> buildNameWithCode(item.getStudentName(), item.getStudentNo())));
        ajax.put("userOptions", toOptions(users, SysUser::getUserId, this::buildUserLabel));
        ajax.put("assignmentOptions", toOptions(assignments, EduAssignment::getAssignmentId,
            item -> buildAssociationLabel(item.getTitle(), courseLabelMap.get(item.getCourseId()))));
        ajax.put("chapterOptions", toOptions(chapters, EduCourseChapter::getChapterId,
            item -> buildAssociationLabel(item.getChapterTitle(), courseLabelMap.get(item.getCourseId()))));
        ajax.put("noticeOptions", toOptions(notices, EduCourseNotice::getNoticeId,
            item -> buildAssociationLabel(item.getTitle(), courseLabelMap.get(item.getCourseId()))));
        ajax.put("submissionOptions", toOptions(submissions, EduAssignmentSubmission::getSubmissionId,
            item -> buildSubmissionLabel(studentLabelMap.get(item.getStudentUserId()),
                assignmentLabelMap.get(item.getAssignmentId()), item.getSubmitRound())));
        return ajax;
    }

    private List<SysUser> buildScopedUsers(List<EduTeacherProfile> teachers, List<EduStudentProfile> students)
    {
        Map<Long, SysUser> userMap = new LinkedHashMap<Long, SysUser>();
        if (educationPermission.isAdmin())
        {
            SysUser userQuery = new SysUser();
            userQuery.setStatus("0");
            List<SysUser> users = userService.selectUserList(userQuery);
            for (SysUser user : users)
            {
                userMap.put(user.getUserId(), user);
            }
            return new ArrayList<SysUser>(userMap.values());
        }

        if (educationPermission.isMaster())
        {
            SysUser userQuery = new SysUser();
            userQuery.setStatus("0");
            userQuery.setDeptId(educationPermission.getCurrentDeptId());
            List<SysUser> users = userService.selectUserList(userQuery);
            for (SysUser user : users)
            {
                userMap.put(user.getUserId(), user);
            }
            return new ArrayList<SysUser>(userMap.values());
        }

        SysUser currentUser = userService.selectUserById(educationPermission.getCurrentUserId());
        if (currentUser != null)
        {
            userMap.put(currentUser.getUserId(), currentUser);
        }
        for (EduTeacherProfile teacher : teachers)
        {
            SysUser user = userService.selectUserById(teacher.getUserId());
            if (user != null)
            {
                userMap.put(user.getUserId(), user);
            }
        }
        for (EduStudentProfile student : students)
        {
            SysUser user = userService.selectUserById(student.getUserId());
            if (user != null)
            {
                userMap.put(user.getUserId(), user);
            }
        }
        return new ArrayList<SysUser>(userMap.values());
    }

    private <T> List<Map<String, Object>> toOptions(List<T> source, Function<T, Long> valueFunc, Function<T, String> labelFunc)
    {
        List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();
        for (T item : source)
        {
            Long value = valueFunc.apply(item);
            String label = labelFunc.apply(item);
            if (value == null || StringUtils.isEmpty(label))
            {
                continue;
            }
            Map<String, Object> option = new LinkedHashMap<String, Object>();
            option.put("value", value);
            option.put("label", label);
            options.add(option);
        }
        return options;
    }

    /**
     * 学期选项：额外附加 startDate，用于前端自动填充选课时间
     */
    private List<Map<String, Object>> toTermOptions(List<EduTerm> terms)
    {
        List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();
        for (EduTerm term : terms)
        {
            if (term.getTermId() == null || StringUtils.isEmpty(term.getTermName()))
            {
                continue;
            }
            Map<String, Object> option = new LinkedHashMap<String, Object>();
            option.put("value", term.getTermId());
            option.put("label", term.getTermName());
            if (term.getStartDate() != null)
            {
                option.put("startDate", term.getStartDate());
            }
            options.add(option);
        }
        return options;
    }

    private String buildCourseLabel(EduCourse course)
    {
        return buildNameWithCode(course.getCourseName(), course.getCourseCode());
    }

    private String buildUserLabel(SysUser user)
    {
        String mainName = StringUtils.isNotEmpty(user.getNickName()) ? user.getNickName() : user.getUserName();
        if (StringUtils.isEmpty(mainName))
        {
            return null;
        }
        if (StringUtils.isEmpty(user.getUserName()) || StringUtils.equals(mainName, user.getUserName()))
        {
            return mainName;
        }
        return mainName + "（" + user.getUserName() + "）";
    }

    private String buildNameWithCode(String name, String code)
    {
        if (StringUtils.isEmpty(name))
        {
            return null;
        }
        if (StringUtils.isEmpty(code))
        {
            return name;
        }
        return name + "（" + code + "）";
    }

    private String buildAssociationLabel(String mainLabel, String subLabel)
    {
        if (StringUtils.isEmpty(mainLabel))
        {
            return null;
        }
        if (StringUtils.isEmpty(subLabel))
        {
            return mainLabel;
        }
        return mainLabel + " / " + subLabel;
    }

    private String buildSubmissionLabel(String studentLabel, String assignmentLabel, Integer submitRound)
    {
        String label = buildAssociationLabel(studentLabel, assignmentLabel);
        if (StringUtils.isEmpty(label))
        {
            return null;
        }
        if (submitRound == null)
        {
            return label;
        }
        return label + " / 第" + submitRound + "次";
    }

    private List<TreeSelect> buildDeptOptions()
    {
        // 系统管理员：返回完整院系树（可跨学院查询）
        if (educationPermission.isAdmin())
        {
            SysDept dept = new SysDept();
            dept.setStatus("0");
            return deptService.selectDeptTreeList(dept);
        }

        // 教师或学生：只返回自己所属院系
        if (educationPermission.isTeacher() || educationPermission.isStudent())
        {
            List<TreeSelect> deptOptions = new ArrayList<TreeSelect>();
            Long currentDeptId = educationPermission.getCurrentDeptId();
            if (currentDeptId == null)
            {
                return deptOptions;
            }

            SysDept currentDept = deptService.selectDeptById(currentDeptId);
            if (currentDept == null || !StringUtils.equals("0", currentDept.getStatus()) || StringUtils.equals("2", currentDept.getDelFlag()))
            {
                return deptOptions;
            }

            deptOptions.add(new TreeSelect(currentDept));
            return deptOptions;
        }

        // 学院管理员：返回完整院系树
        SysDept dept = new SysDept();
        dept.setStatus("0");
        return deptService.selectDeptTreeList(dept);
    }

    /**
     * 模糊搜索教师（用于下拉框实时搜索）
     */
    @GetMapping("/searchTeacher")
    public AjaxResult searchTeacher(@RequestParam(required = false) String keyword)
    {
        List<EduTeacherProfile> teachers = teacherService.selectEduTeacherProfileList(new EduTeacherProfile());
        // 过滤：按姓名或工号模糊搜索
        if (StringUtils.isNotEmpty(keyword))
        {
            String kw = keyword.toLowerCase();
            teachers = teachers.stream()
                .filter(t -> (t.getTeacherName() != null && t.getTeacherName().toLowerCase().contains(kw))
                          || (t.getTeacherNo() != null && t.getTeacherNo().toLowerCase().contains(kw)))
                .collect(java.util.stream.Collectors.toList());
        }
        // 按权限过滤：只有系统管理员可以查所有学院，其他角色只能查本学院的教师
        if (!educationPermission.isAdmin())
        {
            Long currentDeptId = educationPermission.getCurrentDeptId();
            final Long deptId = currentDeptId;
            teachers = teachers.stream()
                .filter(t -> deptId != null && deptId.equals(t.getDeptId()))
                .collect(java.util.stream.Collectors.toList());
        }
        // 限制返回数量
        if (teachers.size() > 20)
        {
            teachers = teachers.subList(0, 20);
        }
        // 构建选项列表
        List<Map<String, Object>> options = new ArrayList<>();
        for (EduTeacherProfile teacher : teachers)
        {
            Map<String, Object> option = new LinkedHashMap<>();
            option.put("value", teacher.getUserId());
            option.put("label", buildNameWithCode(teacher.getTeacherName(), teacher.getTeacherNo()));
            options.add(option);
        }
        return success(options);
    }
}
