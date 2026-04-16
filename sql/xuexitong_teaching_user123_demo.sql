-- 学习通教学管理平台 student1 / teacher1 / master1 专用演示数据
-- 执行顺序建议：
-- 1. sql/ry_20240629.sql
-- 2. sql/xuexitong_teaching_schema.sql
-- 3. sql/xuexitong_teaching_demo_init.sql
-- 4. sql/xuexitong_teaching_user123_demo.sql
-- 说明：
-- 1. 本脚本不会创建账号，直接按 user_name 查找已存在的 student1 / teacher1 / master1
-- 2. 用于补充 5 组课程、选课、作业、公告、消息联动演示数据
-- 3. 适用环境：MySQL 5.7 / UTF-8（建议 utf8mb4）

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 一、student1 / teacher1 / master1 账号联动演示数据
-- ----------------------------
SET @demo_student1_user_id := (SELECT user_id FROM sys_user WHERE user_name = 'student1' LIMIT 1);
SET @demo_teacher1_user_id := (SELECT user_id FROM sys_user WHERE user_name = 'teacher1' LIMIT 1);
SET @demo_master1_user_id  := (SELECT user_id FROM sys_user WHERE user_name = 'master1' LIMIT 1);
SET @demo_dept_id          := COALESCE((SELECT dept_id FROM sys_user WHERE user_id = @demo_master1_user_id LIMIT 1), (SELECT dept_id FROM sys_user WHERE user_id = @demo_teacher1_user_id LIMIT 1), 210);
SET @demo_teacher_name     := COALESCE((SELECT NULLIF(nick_name, '') FROM sys_user WHERE user_id = @demo_teacher1_user_id LIMIT 1), '演示教师一');
SET @demo_student_name     := COALESCE((SELECT NULLIF(nick_name, '') FROM sys_user WHERE user_id = @demo_student1_user_id LIMIT 1), '演示学生一');
SET @demo_teacher_phone    := COALESCE((SELECT NULLIF(phonenumber, '') FROM sys_user WHERE user_id = @demo_teacher1_user_id LIMIT 1), '13900011111');
SET @demo_student_phone    := COALESCE((SELECT NULLIF(phonenumber, '') FROM sys_user WHERE user_id = @demo_student1_user_id LIMIT 1), '13900021111');
SET @demo_teacher_email    := COALESCE((SELECT NULLIF(email, '') FROM sys_user WHERE user_id = @demo_teacher1_user_id LIMIT 1), 'teacher1@example.edu.cn');
SET @demo_student_email    := COALESCE((SELECT NULLIF(email, '') FROM sys_user WHERE user_id = @demo_student1_user_id LIMIT 1), 'student1@example.edu.cn');

INSERT IGNORE INTO edu_teacher_profile
(teacher_id, user_id, teacher_no, teacher_name, dept_id, job_title, phone, email, office_address, research_direction, intro, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(51011, @demo_teacher1_user_id, 'TEA-DEMO-001', @demo_teacher_name, @demo_dept_id, '讲师', @demo_teacher_phone, @demo_teacher_email, '教学楼A-305', '软件工程实践与项目开发', 'teacher1 账号演示教师档案，用于课程、资源、作业和公告演示。', '0', '0', 'master1', NOW(), '', NULL, 'teacher1 演示档案');

INSERT IGNORE INTO edu_student_profile
(student_id, user_id, student_no, student_name, dept_id, major_name, class_name, grade_name, admission_year, phone, email, study_status, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(52011, @demo_student1_user_id, 'STU-DEMO-001', @demo_student_name, @demo_dept_id, '软件工程', '软件工程2301班', '2023级', '2023', @demo_student_phone, @demo_student_email, '1', '0', '0', 'master1', NOW(), '', NULL, 'student1 演示档案');

INSERT IGNORE INTO edu_term
(term_id, term_code, term_name, school_year, semester_no, start_date, end_date, select_start_time, select_end_time, status, create_by, create_time, update_by, update_time, remark)
VALUES
(53011, 'DEMO-2026-1', '2026账号演示学期', '2025-2026', '2', '2026-02-24', '2026-07-05', '2026-02-20 08:00:00', '2026-03-10 23:59:59', '1', 'master1', NOW(), '', NULL, 'student1/teacher1/master1 联动演示学期');

INSERT IGNORE INTO edu_course
(course_id, term_id, dept_id, course_code, course_name, course_cover, course_type, credit, total_hours, capacity, selected_count, select_start_time, select_end_time, intro, complete_rule, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(54101, 53011, @demo_dept_id, 'DEMO-C001', 'Spring Boot应用开发实训', '/profile/course/demo_springboot.jpg', '2', 3.0, 48, 60, 1, '2026-02-20 08:00:00', '2026-03-10 23:59:59', '围绕接口开发、权限控制和项目部署开展课程演示。', '完成章节学习、资源阅读并提交课程作业。', '1', '0', 'master1', NOW(), '', NULL, 'teacher1/student1 演示课程'),
(54102, 53011, @demo_dept_id, 'DEMO-C002', '数据结构算法案例课', '/profile/course/demo_algorithm.jpg', '1', 3.5, 56, 60, 1, '2026-02-20 08:00:00', '2026-03-10 23:59:59', '结合栈、队列、树和排序案例讲解算法思想。', '完成案例练习并按时提交作业。', '1', '0', 'master1', NOW(), '', NULL, 'teacher1/student1 演示课程'),
(54103, 53011, @demo_dept_id, 'DEMO-C003', 'Vue后台管理系统开发', '/profile/course/demo_vue.jpg', '2', 2.5, 40, 50, 1, '2026-02-20 08:00:00', '2026-03-10 23:59:59', '讲解若依风格页面开发、表格表单和权限显隐。', '完成页面改造练习并通过界面自测。', '1', '0', 'master1', NOW(), '', NULL, 'teacher1/student1 演示课程'),
(54104, 53011, @demo_dept_id, 'DEMO-C004', '软件测试用例设计', '/profile/course/demo_test.jpg', '1', 2.0, 32, 45, 1, '2026-02-20 08:00:00', '2026-03-10 23:59:59', '聚焦功能测试、边界测试和用例评审。', '提交测试用例文档并完成课堂展示。', '1', '0', 'master1', NOW(), '', NULL, 'teacher1/student1 演示课程'),
(54105, 53011, @demo_dept_id, 'DEMO-C005', '毕业设计过程管理', '/profile/course/demo_graduation.jpg', '1', 1.5, 24, 80, 1, '2026-02-20 08:00:00', '2026-03-10 23:59:59', '用于演示周报提交、阶段检查和过程性评价。', '按周提交周报并查看老师评语。', '1', '0', 'master1', NOW(), '', NULL, 'teacher1/student1 演示课程');

INSERT IGNORE INTO edu_course_teacher
(course_teacher_id, course_id, teacher_user_id, teacher_role, is_owner, order_num, status, create_by, create_time, update_by, update_time, remark)
VALUES
(55101, 54101, @demo_teacher1_user_id, '1', '1', 1, '0', 'master1', NOW(), '', NULL, 'teacher1 主讲课程'),
(55102, 54102, @demo_teacher1_user_id, '1', '1', 1, '0', 'master1', NOW(), '', NULL, 'teacher1 主讲课程'),
(55103, 54103, @demo_teacher1_user_id, '1', '1', 1, '0', 'master1', NOW(), '', NULL, 'teacher1 主讲课程'),
(55104, 54104, @demo_teacher1_user_id, '1', '1', 1, '0', 'master1', NOW(), '', NULL, 'teacher1 主讲课程'),
(55105, 54105, @demo_teacher1_user_id, '1', '1', 1, '0', 'master1', NOW(), '', NULL, 'teacher1 主讲课程');

INSERT IGNORE INTO edu_course_chapter
(chapter_id, course_id, parent_id, ancestors, chapter_title, chapter_type, chapter_desc, order_num, duration_minutes, status, create_by, create_time, update_by, update_time, remark)
VALUES
(56101, 54101, 0, '0', '第一章 项目脚手架搭建', '1', '完成 Spring Boot 项目初始化与运行环境配置。', 1, 90, '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示章节'),
(56102, 54102, 0, '0', '第一章 线性表与栈队列', '1', '通过案例理解顺序表、链表、栈和队列的应用。', 1, 100, '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示章节'),
(56103, 54103, 0, '0', '第一章 Vue后台模板初始化', '1', '学习列表页、搜索区和弹窗表单的实现方式。', 1, 85, '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示章节'),
(56104, 54104, 0, '0', '第一章 测试计划与用例设计', '1', '掌握测试点梳理、等价类和边界值设计方法。', 1, 80, '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示章节'),
(56105, 54105, 0, '0', '第一章 开题与任务书准备', '1', '明确毕业设计目标、阶段任务和周报要求。', 1, 70, '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示章节');

INSERT IGNORE INTO edu_course_resource
(resource_id, course_id, chapter_id, resource_title, resource_type, file_name, file_url, file_suffix, file_size, download_count, upload_user_id, order_num, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(57101, 54101, 56101, 'Spring Boot实训课件', '1', 'SpringBoot实训课件.pptx', '/profile/resource/demo_springboot.pptx', 'pptx', 2097152, 6, @demo_teacher1_user_id, 1, '0', '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示资源'),
(57102, 54102, 56102, '数据结构案例讲义', '2', '数据结构案例讲义.pdf', '/profile/resource/demo_algorithm.pdf', 'pdf', 1048576, 4, @demo_teacher1_user_id, 1, '0', '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示资源'),
(57103, 54103, 56103, 'Vue页面开发示例', '2', 'Vue页面开发示例.docx', '/profile/resource/demo_vue.docx', 'docx', 786432, 7, @demo_teacher1_user_id, 1, '0', '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示资源'),
(57104, 54104, 56104, '测试用例模板', '2', '测试用例模板.xlsx', '/profile/resource/demo_test.xlsx', 'xlsx', 524288, 3, @demo_teacher1_user_id, 1, '0', '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示资源'),
(57105, 54105, 56105, '毕业设计周报模板', '2', '毕业设计周报模板.docx', '/profile/resource/demo_report.docx', 'docx', 458752, 5, @demo_teacher1_user_id, 1, '0', '0', 'teacher1', NOW(), '', NULL, 'teacher1 演示资源');

INSERT IGNORE INTO edu_course_enrollment
(enrollment_id, course_id, term_id, student_user_id, source_type, enroll_time, drop_time, finish_time, enroll_status, progress_percent, required_task_count, finished_task_count, last_study_time, create_by, create_time, update_by, update_time, remark)
VALUES
(58101, 54101, 53011, @demo_student1_user_id, '1', '2026-02-25 09:10:00', NULL, NULL, '1', 36.00, 5, 2, '2026-04-01 20:30:00', 'student1', NOW(), '', NULL, 'student1 演示选课'),
(58102, 54102, 53011, @demo_student1_user_id, '1', '2026-02-25 10:20:00', NULL, NULL, '1', 68.00, 6, 4, '2026-04-08 19:00:00', 'student1', NOW(), '', NULL, 'student1 演示选课'),
(58103, 54103, 53011, @demo_student1_user_id, '2', '2026-02-26 08:40:00', NULL, '2026-03-28 18:00:00', '3', 100.00, 5, 5, '2026-03-28 18:00:00', 'master1', NOW(), '', NULL, 'student1 演示选课'),
(58104, 54104, 53011, @demo_student1_user_id, '1', '2026-02-26 14:30:00', NULL, NULL, '1', 82.00, 4, 3, '2026-04-09 21:10:00', 'student1', NOW(), '', NULL, 'student1 演示选课'),
(58105, 54105, 53011, @demo_student1_user_id, '2', '2026-02-27 09:00:00', NULL, '2026-04-06 17:30:00', '3', 100.00, 4, 4, '2026-04-06 17:30:00', 'master1', NOW(), '', NULL, 'student1 演示选课');

INSERT IGNORE INTO edu_learning_progress
(progress_id, course_id, chapter_id, student_user_id, learn_minutes, progress_percent, completed_flag, first_study_time, last_study_time, create_by, create_time, update_by, update_time, remark)
VALUES
(59101, 54101, 56101, @demo_student1_user_id, 48, 36.00, '0', '2026-03-05 19:00:00', '2026-04-01 20:30:00', 'student1', NOW(), '', NULL, 'student1 演示学习进度'),
(59102, 54102, 56102, @demo_student1_user_id, 96, 68.00, '0', '2026-03-03 18:20:00', '2026-04-08 19:00:00', 'student1', NOW(), '', NULL, 'student1 演示学习进度'),
(59103, 54103, 56103, @demo_student1_user_id, 120, 100.00, '1', '2026-03-01 20:00:00', '2026-03-28 18:00:00', 'student1', NOW(), '', NULL, 'student1 演示学习进度'),
(59104, 54104, 56104, @demo_student1_user_id, 88, 82.00, '0', '2026-03-10 19:10:00', '2026-04-09 21:10:00', 'student1', NOW(), '', NULL, 'student1 演示学习进度'),
(59105, 54105, 56105, @demo_student1_user_id, 75, 100.00, '1', '2026-03-06 21:00:00', '2026-04-06 17:30:00', 'student1', NOW(), '', NULL, 'student1 演示学习进度');

INSERT IGNORE INTO edu_assignment
(assignment_id, course_id, title, assignment_type, content, total_score, submit_limit, allow_resubmit, deadline_time, publish_status, publish_user_id, publish_time, create_by, create_time, update_by, update_time, remark)
VALUES
(60101, 54101, 'Spring Boot接口开发作业', '2', '完成用户查询接口、统一返回结构和异常处理。', 100.00, 2, '1', '2026-03-18 23:59:59', '1', @demo_teacher1_user_id, '2026-03-08 09:00:00', 'teacher1', NOW(), '', NULL, 'teacher1 演示作业'),
(60102, 54102, '栈与队列综合练习', '1', '使用栈和队列实现表达式求值与调度模拟。', 100.00, 1, '0', '2026-03-22 23:59:59', '1', @demo_teacher1_user_id, '2026-03-10 10:00:00', 'teacher1', NOW(), '', NULL, 'teacher1 演示作业'),
(60103, 54103, 'Vue页面权限改造作业', '1', '为列表页补充按钮显隐和角色操作控制。', 100.00, 2, '1', '2026-03-20 23:59:59', '1', @demo_teacher1_user_id, '2026-03-09 14:30:00', 'teacher1', NOW(), '', NULL, 'teacher1 演示作业'),
(60104, 54104, '测试用例编写练习', '2', '围绕登录、分页、搜索场景编写功能测试用例。', 100.00, 2, '1', '2026-03-25 23:59:59', '1', @demo_teacher1_user_id, '2026-03-12 08:40:00', 'teacher1', NOW(), '', NULL, 'teacher1 演示作业'),
(60105, 54105, '毕业设计周报第一期', '3', '提交本周任务进度、问题总结和下周计划。', 100.00, 3, '1', '2026-04-05 23:59:59', '1', @demo_teacher1_user_id, '2026-03-25 16:20:00', 'teacher1', NOW(), '', NULL, 'teacher1 演示作业');

INSERT IGNORE INTO edu_assignment_attachment
(assignment_attachment_id, assignment_id, file_name, file_url, file_suffix, file_size, order_num, upload_user_id, create_by, create_time, update_by, update_time, remark)
VALUES
(61101, 60101, '接口开发说明文档.pdf', '/profile/assignment/demo_api.pdf', 'pdf', 420000, 1, @demo_teacher1_user_id, 'teacher1', NOW(), '', NULL, 'teacher1 演示作业附件'),
(61102, 60102, '算法练习题目.docx', '/profile/assignment/demo_algorithm.docx', 'docx', 320000, 1, @demo_teacher1_user_id, 'teacher1', NOW(), '', NULL, 'teacher1 演示作业附件'),
(61103, 60103, '页面权限改造要求.docx', '/profile/assignment/demo_vue.docx', 'docx', 288000, 1, @demo_teacher1_user_id, 'teacher1', NOW(), '', NULL, 'teacher1 演示作业附件'),
(61104, 60104, '测试用例模板.xlsx', '/profile/assignment/demo_test.xlsx', 'xlsx', 256000, 1, @demo_teacher1_user_id, 'teacher1', NOW(), '', NULL, 'teacher1 演示作业附件'),
(61105, 60105, '周报撰写模板.docx', '/profile/assignment/demo_report.docx', 'docx', 230000, 1, @demo_teacher1_user_id, 'teacher1', NOW(), '', NULL, 'teacher1 演示作业附件');

INSERT IGNORE INTO edu_assignment_submission
(submission_id, assignment_id, course_id, student_user_id, submit_round, submit_remark, submit_time, late_flag, is_latest, review_status, score, teacher_comment, review_user_id, review_time, create_by, create_time, update_by, update_time, remark)
VALUES
(62101, 60101, 54101, @demo_student1_user_id, 1, '已完成接口开发并补充接口测试截图。', '2026-03-17 21:10:00', '0', '1', '1', 88.00, '接口设计清晰，异常处理比较完整。', @demo_teacher1_user_id, '2026-03-18 10:00:00', 'student1', NOW(), '', NULL, 'student1 演示作业提交'),
(62102, 60102, 54102, @demo_student1_user_id, 1, '已提交栈与队列综合练习代码。', '2026-03-21 19:30:00', '0', '1', '0', NULL, '', NULL, NULL, 'student1', NOW(), '', NULL, 'student1 演示作业提交'),
(62103, 60103, 54103, @demo_student1_user_id, 1, '页面权限显隐已处理，并附操作录屏说明。', '2026-03-19 22:05:00', '0', '1', '1', 93.00, '角色显隐逻辑完整，页面体验较好。', @demo_teacher1_user_id, '2026-03-20 09:15:00', 'student1', NOW(), '', NULL, 'student1 演示作业提交'),
(62104, 60104, 54104, @demo_student1_user_id, 2, '根据第一次反馈补充了边界场景和异常场景。', '2026-03-24 20:40:00', '0', '1', '1', 90.00, '补充后的测试点更完整，边界覆盖较充分。', @demo_teacher1_user_id, '2026-03-25 11:20:00', 'student1', NOW(), '', NULL, 'student1 演示作业提交'),
(62105, 60105, 54105, @demo_student1_user_id, 1, '已提交毕业设计周报第一期，请老师查看。', '2026-04-04 18:10:00', '0', '1', '1', 96.00, '周报内容详实，阶段目标清晰。', @demo_teacher1_user_id, '2026-04-05 09:40:00', 'student1', NOW(), '', NULL, 'student1 演示作业提交');

INSERT IGNORE INTO edu_submission_attachment
(submission_attachment_id, submission_id, file_name, file_url, file_suffix, file_size, upload_user_id, create_by, create_time, update_by, update_time, remark)
VALUES
(63101, 62101, '接口开发作业.zip', '/profile/submission/demo_api.zip', 'zip', 860000, @demo_student1_user_id, 'student1', NOW(), '', NULL, 'student1 演示提交附件'),
(63102, 62102, '栈与队列练习.zip', '/profile/submission/demo_algorithm.zip', 'zip', 620000, @demo_student1_user_id, 'student1', NOW(), '', NULL, 'student1 演示提交附件'),
(63103, 62103, '页面权限改造说明.docx', '/profile/submission/demo_vue.docx', 'docx', 350000, @demo_student1_user_id, 'student1', NOW(), '', NULL, 'student1 演示提交附件'),
(63104, 62104, '测试用例文档.xlsx', '/profile/submission/demo_test.xlsx', 'xlsx', 280000, @demo_student1_user_id, 'student1', NOW(), '', NULL, 'student1 演示提交附件'),
(63105, 62105, '毕业设计周报第一期.docx', '/profile/submission/demo_report.docx', 'docx', 240000, @demo_student1_user_id, 'student1', NOW(), '', NULL, 'student1 演示提交附件');

INSERT IGNORE INTO edu_course_notice
(notice_id, course_id, title, content, notice_type, top_flag, publish_user_id, publish_time, status, create_by, create_time, update_by, update_time, remark)
VALUES
(64101, 54101, 'Spring Boot实训开课提醒', '请本周内完成项目环境搭建，并在下次课堂前确认项目可以正常运行。', '2', '1', @demo_teacher1_user_id, '2026-03-01 09:00:00', '1', 'teacher1', NOW(), '', NULL, 'teacher1 演示公告'),
(64102, 54102, '算法练习提交说明', '本次作业只接收 zip 压缩包，请将源码与运行说明一并提交。', '1', '0', @demo_teacher1_user_id, '2026-03-10 11:00:00', '1', 'teacher1', NOW(), '', NULL, 'teacher1 演示公告'),
(64103, 54103, 'Vue课程资料已更新', '新增了按钮权限显隐示例代码，建议结合作业要求一起查看。', '2', '0', @demo_teacher1_user_id, '2026-03-13 15:20:00', '1', 'teacher1', NOW(), '', NULL, 'teacher1 演示公告'),
(64104, 54104, '测试用例课堂展示安排', '请按名单顺序进行 5 分钟展示，重点说明边界场景设计思路。', '1', '0', @demo_teacher1_user_id, '2026-03-18 08:30:00', '1', 'teacher1', NOW(), '', NULL, 'teacher1 演示公告'),
(64105, 54105, '毕业设计周报提交通知', '每周日晚前提交周报，老师会在次日中午前完成批注。', '1', '0', @demo_teacher1_user_id, '2026-03-25 17:00:00', '1', 'teacher1', NOW(), '', NULL, 'teacher1 演示公告');

INSERT IGNORE INTO edu_user_message
(message_id, user_id, course_id, assignment_id, notice_id, biz_type, message_title, message_content, sender_user_id, send_time, read_status, read_time, link_path, expire_time, create_by, create_time, update_by, update_time, remark)
VALUES
(65101, @demo_student1_user_id, 54101, NULL, NULL, 'new_course', '你已加入 Spring Boot应用开发实训', '院系管理员已为你分配演示课程“Spring Boot应用开发实训”，可立即进入课程中心查看。', @demo_master1_user_id, '2026-02-27 09:00:00', '1', '2026-02-27 09:10:00', '/education/course/detail/54101', '2026-06-30 23:59:59', 'master1', NOW(), '', NULL, 'student1 演示消息'),
(65102, @demo_student1_user_id, 54101, 60101, NULL, 'new_assignment', 'Spring Boot接口开发作业已发布', 'teacher1 已发布新作业，请在截止时间前完成接口开发与文档整理。', @demo_teacher1_user_id, '2026-03-08 09:05:00', '1', '2026-03-08 10:00:00', '/education/assignment/detail/60101', '2026-03-31 23:59:59', 'teacher1', NOW(), '', NULL, 'student1 演示消息'),
(65103, @demo_student1_user_id, 54102, 60102, NULL, 'assignment_deadline', '栈与队列综合练习即将截止', '你的“栈与队列综合练习”将在今晚 23:59 截止，请尽快确认提交内容。', @demo_teacher1_user_id, '2026-03-22 18:00:00', '0', NULL, '/education/assignment/detail/60102', '2026-03-22 23:59:59', 'teacher1', NOW(), '', NULL, 'student1 演示消息'),
(65104, @demo_student1_user_id, 54104, NULL, 64104, 'course_notice', '测试用例课堂展示安排已发布', 'teacher1 发布了新的课程公告，请查看课堂展示顺序和时间安排。', @demo_teacher1_user_id, '2026-03-18 08:35:00', '0', NULL, '/education/notice/detail/64104', '2026-04-10 23:59:59', 'teacher1', NOW(), '', NULL, 'student1 演示消息'),
(65105, @demo_student1_user_id, 54103, 60103, NULL, 'assignment_graded', 'Vue页面权限改造作业已批改', '你的作业已完成批改，当前得分 93 分，请及时查看老师评语。', @demo_teacher1_user_id, '2026-03-20 09:20:00', '1', '2026-03-20 09:50:00', '/education/submission/detail/62103', '2026-04-20 23:59:59', 'teacher1', NOW(), '', NULL, 'student1 演示消息');

SET FOREIGN_KEY_CHECKS = 1;
