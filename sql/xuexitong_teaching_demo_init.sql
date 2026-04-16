-- 学习通教学管理平台演示数据初始化脚本
-- 执行顺序建议：
-- 1. sql/ry_20240629.sql
-- 2. sql/xuexitong_teaching_schema.sql
-- 3. sql/xuexitong_teaching_demo_init.sql
-- 4. sql/xuexitong_teaching_user123_demo.sql（可选，依赖已存在的 student1 / teacher1 / master1 账号）
-- 适用环境：MySQL 5.7 / UTF-8（建议 utf8mb4）

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 一、角色兜底初始化
-- ----------------------------
INSERT INTO sys_role
(
  role_name,
  role_key,
  role_sort,
  data_scope,
  menu_check_strictly,
  dept_check_strictly,
  status,
  del_flag,
  create_by,
  create_time,
  remark
)
SELECT
  '学生',
  'student',
  10,
  '5',
  1,
  1,
  '0',
  '0',
  'admin',
  NOW(),
  '学习通教学管理平台-学生角色'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'student');

INSERT INTO sys_role
(
  role_name,
  role_key,
  role_sort,
  data_scope,
  menu_check_strictly,
  dept_check_strictly,
  status,
  del_flag,
  create_by,
  create_time,
  remark
)
SELECT
  '教师',
  'teacher',
  20,
  '5',
  1,
  1,
  '0',
  '0',
  'admin',
  NOW(),
  '学习通教学管理平台-教师角色'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'teacher');

INSERT INTO sys_role
(
  role_name,
  role_key,
  role_sort,
  data_scope,
  menu_check_strictly,
  dept_check_strictly,
  status,
  del_flag,
  create_by,
  create_time,
  remark
)
SELECT
  '院系管理员',
  'master',
  30,
  '4',
  1,
  1,
  '0',
  '0',
  'admin',
  NOW(),
  '学习通教学管理平台-院系管理员角色'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'master');

-- ----------------------------
-- 二、依赖演示数据：院系与用户
-- ----------------------------
INSERT IGNORE INTO sys_dept
(dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(210, 100, '0,100', '信息工程学院', 10, '张院长', '13800001001', 'xxgc@example.edu.cn', '0', '0', 'admin', NOW(), '', NULL, '学习通演示院系'),
(211, 100, '0,100', '经济管理学院', 11, '李院长', '13800001002', 'jjgl@example.edu.cn', '0', '0', 'admin', NOW(), '', NULL, '学习通演示院系'),
(212, 100, '0,100', '外国语学院',   12, '王院长', '13800001003', 'wgy@example.edu.cn',  '0', '0', 'admin', NOW(), '', NULL, '学习通演示院系');

INSERT IGNORE INTO sys_user
(user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark)
VALUES
(21001, 210, 'teacher_zhangwei',   '张伟',       '00', 'zhangwei@example.edu.cn', '13900010001', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '教师演示账号'),
(21002, 210, 'teacher_liling',     '李玲',       '00', 'liling@example.edu.cn',   '13900010002', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '教师演示账号'),
(21003, 211, 'teacher_wanghao',    '王浩',       '00', 'wanghao@example.edu.cn',  '13900010003', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '教师演示账号'),
(21004, 211, 'teacher_chenyu',     '陈瑜',       '00', 'chenyu@example.edu.cn',   '13900010004', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '教师演示账号'),
(21005, 212, 'teacher_zhaomin',    '赵敏',       '00', 'zhaomin@example.edu.cn',  '13900010005', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '教师演示账号'),
(22001, 210, 'student_liming',     '李明',       '00', 'liming@example.edu.cn',   '13900020001', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '学生演示账号'),
(22002, 210, 'student_wangfang',   '王芳',       '00', 'wangfang@example.edu.cn', '13900020002', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '学生演示账号'),
(22003, 211, 'student_zhouxin',    '周欣',       '00', 'zhouxin@example.edu.cn',  '13900020003', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '学生演示账号'),
(22004, 211, 'student_sunyu',      '孙宇',       '00', 'sunyu@example.edu.cn',    '13900020004', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '学生演示账号'),
(22005, 212, 'student_hejing',     '何静',       '00', 'hejing@example.edu.cn',   '13900020005', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '学生演示账号'),
(23001, 210, 'master_xinxi',       '信息院管理员', '00', 'masterxx@example.edu.cn', '13900030001', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '院系管理员演示账号'),
(23002, 211, 'master_jingguan',    '经管院管理员', '00', 'masterjg@example.edu.cn', '13900030002', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '院系管理员演示账号');

INSERT IGNORE INTO sys_user_role (user_id, role_id)
SELECT t.user_id, r.role_id
FROM
(
  SELECT 21001 AS user_id UNION ALL
  SELECT 21002 UNION ALL
  SELECT 21003 UNION ALL
  SELECT 21004 UNION ALL
  SELECT 21005
) t
JOIN sys_role r ON r.role_key = 'teacher';

INSERT IGNORE INTO sys_user_role (user_id, role_id)
SELECT t.user_id, r.role_id
FROM
(
  SELECT 22001 AS user_id UNION ALL
  SELECT 22002 UNION ALL
  SELECT 22003 UNION ALL
  SELECT 22004 UNION ALL
  SELECT 22005
) t
JOIN sys_role r ON r.role_key = 'student';

INSERT IGNORE INTO sys_user_role (user_id, role_id)
SELECT t.user_id, r.role_id
FROM
(
  SELECT 23001 AS user_id UNION ALL
  SELECT 23002
) t
JOIN sys_role r ON r.role_key = 'master';

-- ----------------------------
-- 三、业务表示例数据（每张表5条）
-- ----------------------------
INSERT IGNORE INTO edu_teacher_profile
(teacher_id, user_id, teacher_no, teacher_name, dept_id, job_title, phone, email, office_address, research_direction, intro, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(31001, 21001, 'T2026001', '张伟', 210, '副教授', '13900010001', 'zhangwei@example.edu.cn', '信息楼A301', '数据库系统与数据建模', '长期承担数据库原理、数据建模相关课程教学。', '0', '0', 'admin', NOW(), '', NULL, '教师档案示例'),
(31002, 21002, 'T2026002', '李玲', 210, '讲师',   '13900010002', 'liling@example.edu.cn',   '信息楼B205', 'Java开发与Web应用',   '主要负责Java程序设计与Web开发教学。',     '0', '0', 'admin', NOW(), '', NULL, '教师档案示例'),
(31003, 21003, 'T2026003', '王浩', 211, '教授',   '13900010003', 'wanghao@example.edu.cn',  '经管楼C402', '组织管理与企业运营',   '多年从事管理学基础课程教学与案例研究。', '0', '0', 'admin', NOW(), '', NULL, '教师档案示例'),
(31004, 21004, 'T2026004', '陈瑜', 211, '讲师',   '13900010004', 'chenyu@example.edu.cn',   '经管楼C318', '市场营销与品牌传播',   '负责市场营销实务与营销策划方向教学。', '0', '0', 'admin', NOW(), '', NULL, '教师档案示例'),
(31005, 21005, 'T2026005', '赵敏', 212, '副教授', '13900010005', 'zhaomin@example.edu.cn',  '外语楼D206', '英语教学与跨文化交流', '承担综合英语、跨文化沟通课程教学任务。', '0', '0', 'admin', NOW(), '', NULL, '教师档案示例');

INSERT IGNORE INTO edu_student_profile
(student_id, user_id, student_no, student_name, dept_id, major_name, class_name, grade_name, admission_year, phone, email, study_status, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(32001, 22001, '20230001', '李明', 210, '软件工程',         '软件工程2301班', '2023级', '2023', '13900020001', 'liming@example.edu.cn',   '1', '0', '0', 'admin', NOW(), '', NULL, '学生档案示例'),
(32002, 22002, '20230002', '王芳', 210, '计算机科学与技术', '计科2302班',     '2023级', '2023', '13900020002', 'wangfang@example.edu.cn', '1', '0', '0', 'admin', NOW(), '', NULL, '学生档案示例'),
(32003, 22003, '20230003', '周欣', 211, '工商管理',         '工商2301班',     '2023级', '2023', '13900020003', 'zhouxin@example.edu.cn',  '1', '0', '0', 'admin', NOW(), '', NULL, '学生档案示例'),
(32004, 22004, '20230004', '孙宇', 211, '市场营销',         '营销2301班',     '2023级', '2023', '13900020004', 'sunyu@example.edu.cn',    '1', '0', '0', 'admin', NOW(), '', NULL, '学生档案示例'),
(32005, 22005, '20230005', '何静', 212, '英语',             '英语2301班',     '2023级', '2023', '13900020005', 'hejing@example.edu.cn',   '1', '0', '0', 'admin', NOW(), '', NULL, '学生档案示例');

INSERT IGNORE INTO edu_term
(term_id, term_code, term_name, school_year, semester_no, start_date, end_date, select_start_time, select_end_time, status, create_by, create_time, update_by, update_time, remark)
VALUES
(33001, '2024-2025-1', '2024-2025学年第一学期', '2024-2025', '1', '2024-09-02', '2025-01-10', '2024-08-20 08:00:00', '2024-09-10 23:59:59', '2', 'admin', NOW(), '', NULL, '学期示例数据'),
(33002, '2024-2025-2', '2024-2025学年第二学期', '2024-2025', '2', '2025-02-24', '2025-06-30', '2025-02-10 08:00:00', '2025-03-02 23:59:59', '2', 'admin', NOW(), '', NULL, '学期示例数据'),
(33003, '2025-2026-1', '2025-2026学年第一学期', '2025-2026', '1', '2025-09-01', '2026-01-15', '2025-08-20 08:00:00', '2025-09-12 23:59:59', '1', 'admin', NOW(), '', NULL, '学期示例数据'),
(33004, '2025-2026-2', '2025-2026学年第二学期', '2025-2026', '2', '2026-02-23', '2026-07-05', '2026-02-10 08:00:00', '2026-02-28 23:59:59', '0', 'admin', NOW(), '', NULL, '学期示例数据'),
(33005, '2026-2027-1', '2026-2027学年第一学期', '2026-2027', '1', '2026-09-07', '2027-01-15', '2026-08-20 08:00:00', '2026-09-10 23:59:59', '0', 'admin', NOW(), '', NULL, '学期示例数据');

INSERT IGNORE INTO edu_course
(course_id, term_id, dept_id, course_code, course_name, course_cover, course_type, credit, total_hours, capacity, selected_count, select_start_time, select_end_time, intro, complete_rule, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(34001, 33003, 210, 'CS3001', '数据库原理',     '/profile/course/db.jpg',      '1', 3.0, 48, 80, 1, '2025-08-20 08:00:00', '2025-09-12 23:59:59', '介绍关系数据库、SQL语言与数据库设计方法。', '完成全部章节学习并按时提交课程作业。', '1', '0', 'admin', NOW(), '', NULL, '课程示例数据'),
(34002, 33003, 210, 'CS3002', 'Java Web开发',  '/profile/course/javaweb.jpg', '2', 2.5, 40, 60, 1, '2025-08-20 08:00:00', '2025-09-12 23:59:59', '讲解Java Web开发基础、Servlet与MVC实践。', '完成章节学习并提交至少1次课程作业。', '1', '0', 'admin', NOW(), '', NULL, '课程示例数据'),
(34003, 33003, 211, 'MG3001', '管理学基础',     '/profile/course/manage.jpg',  '1', 2.0, 32, 90, 1, '2025-08-20 08:00:00', '2025-09-12 23:59:59', '围绕计划、组织、领导、控制等管理职能展开教学。', '完成课程学习、参与讨论并提交作业。', '1', '0', 'admin', NOW(), '', NULL, '课程示例数据'),
(34004, 33003, 211, 'MK3001', '市场营销实务',   '/profile/course/market.jpg',  '2', 2.0, 32, 50, 0, '2025-08-20 08:00:00', '2025-09-12 23:59:59', '结合案例分析营销策略、用户研究与品牌传播。', '完成课程章节学习并完成案例分析作业。', '1', '0', 'admin', NOW(), '', NULL, '课程示例数据'),
(34005, 33003, 212, 'EN3001', '综合英语提升',   '/profile/course/english.jpg', '1', 3.0, 48, 70, 1, '2025-08-20 08:00:00', '2025-09-12 23:59:59', '提升英语阅读、写作与跨文化表达能力。', '完成学习资源阅读与单元作业提交。', '1', '0', 'admin', NOW(), '', NULL, '课程示例数据');

INSERT IGNORE INTO edu_course_teacher
(course_teacher_id, course_id, teacher_user_id, teacher_role, is_owner, order_num, status, create_by, create_time, update_by, update_time, remark)
VALUES
(35001, 34001, 21001, '1', '1', 1, '0', 'admin', NOW(), '', NULL, '授课安排示例'),
(35002, 34002, 21002, '1', '1', 1, '0', 'admin', NOW(), '', NULL, '授课安排示例'),
(35003, 34003, 21003, '1', '1', 1, '0', 'admin', NOW(), '', NULL, '授课安排示例'),
(35004, 34004, 21004, '1', '1', 1, '0', 'admin', NOW(), '', NULL, '授课安排示例'),
(35005, 34005, 21005, '1', '1', 1, '0', 'admin', NOW(), '', NULL, '授课安排示例');

INSERT IGNORE INTO edu_course_chapter
(chapter_id, course_id, parent_id, ancestors, chapter_title, chapter_type, chapter_desc, order_num, duration_minutes, status, create_by, create_time, update_by, update_time, remark)
VALUES
(36001, 34001, 0, '0', '第一章 数据库基础概念', '1', '理解数据、数据库与数据库管理系统基本概念。', 1, 90, '0', 'admin', NOW(), '', NULL, '章节示例数据'),
(36002, 34002, 0, '0', '第一章 Java Web环境搭建', '1', '完成开发环境准备与第一个Web应用部署。', 1, 80, '0', 'admin', NOW(), '', NULL, '章节示例数据'),
(36003, 34003, 0, '0', '第一章 管理学导论',     '1', '学习管理活动的基本内涵与发展脉络。',     1, 70, '0', 'admin', NOW(), '', NULL, '章节示例数据'),
(36004, 34004, 0, '0', '第一章 市场与用户分析', '1', '掌握市场细分、目标客户与用户画像分析方法。', 1, 75, '0', 'admin', NOW(), '', NULL, '章节示例数据'),
(36005, 34005, 0, '0', '第一章 学术英语阅读',   '1', '训练学术文本阅读与重点信息提取能力。',     1, 85, '0', 'admin', NOW(), '', NULL, '章节示例数据');

INSERT IGNORE INTO edu_course_resource
(resource_id, course_id, chapter_id, resource_title, resource_type, file_name, file_url, file_suffix, file_size, download_count, upload_user_id, order_num, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES
(37001, 34001, 36001, '数据库原理第一章课件', '1', '数据库原理-第一章.pptx',   '/profile/resource/db_ch1.pptx',      'pptx', 2097152, 12, 21001, 1, '0', '0', 'admin', NOW(), '', NULL, '资源示例数据'),
(37002, 34002, 36002, 'Java Web开发环境说明', '2', 'JavaWeb开发环境说明.pdf', '/profile/resource/javaweb_env.pdf',  'pdf',  1048576, 8,  21002, 1, '0', '0', 'admin', NOW(), '', NULL, '资源示例数据'),
(37003, 34003, 36003, '管理学基础导学资料',   '2', '管理学基础导学.docx',     '/profile/resource/manage_intro.docx','docx', 524288,  5,  21003, 1, '0', '0', 'admin', NOW(), '', NULL, '资源示例数据'),
(37004, 34004, 36004, '营销案例分析图片',     '3', '营销案例图示.png',         '/profile/resource/market_case.png',  'png',  786432,  6,  21004, 1, '0', '0', 'admin', NOW(), '', NULL, '资源示例数据'),
(37005, 34005, 36005, '综合英语阅读材料',     '2', '综合英语阅读材料.pdf',     '/profile/resource/english_read.pdf', 'pdf',  1572864, 9,  21005, 1, '0', '0', 'admin', NOW(), '', NULL, '资源示例数据');

INSERT IGNORE INTO edu_course_enrollment
(enrollment_id, course_id, term_id, student_user_id, source_type, enroll_time, drop_time, finish_time, enroll_status, progress_percent, required_task_count, finished_task_count, last_study_time, create_by, create_time, update_by, update_time, remark)
VALUES
(38001, 34001, 33003, 22001, '1', '2025-09-02 09:10:00', NULL,                   NULL,                   '1', 60.00, 5, 3, '2026-04-05 20:10:00', 'admin', NOW(), '', NULL, '选课示例数据'),
(38002, 34002, 33003, 22002, '1', '2025-09-03 10:00:00', NULL,                   '2025-12-28 18:00:00', '3', 100.00, 4, 4, '2025-12-28 18:00:00', 'admin', NOW(), '', NULL, '选课示例数据'),
(38003, 34003, 33003, 22003, '1', '2025-09-02 14:20:00', NULL,                   NULL,                   '1', 45.00, 5, 2, '2026-04-07 16:30:00', 'admin', NOW(), '', NULL, '选课示例数据'),
(38004, 34004, 33003, 22004, '1', '2025-09-04 08:30:00', '2025-09-15 11:00:00', NULL,                   '2', 10.00, 4, 0, '2025-09-14 21:00:00', 'admin', NOW(), '', NULL, '选课示例数据'),
(38005, 34005, 33003, 22005, '1', '2025-09-05 13:15:00', NULL,                   NULL,                   '1', 80.00, 6, 5, '2026-04-08 19:40:00', 'admin', NOW(), '', NULL, '选课示例数据');

INSERT IGNORE INTO edu_learning_progress
(progress_id, course_id, chapter_id, student_user_id, learn_minutes, progress_percent, completed_flag, first_study_time, last_study_time, create_by, create_time, update_by, update_time, remark)
VALUES
(39001, 34001, 36001, 22001, 60,  60.00, '0', '2026-03-20 19:00:00', '2026-04-05 20:10:00', 'admin', NOW(), '', NULL, '学习进度示例'),
(39002, 34002, 36002, 22002, 95, 100.00, '1', '2025-09-10 18:30:00', '2025-12-28 18:00:00', 'admin', NOW(), '', NULL, '学习进度示例'),
(39003, 34003, 36003, 22003, 45,  45.00, '0', '2026-03-30 20:00:00', '2026-04-07 16:30:00', 'admin', NOW(), '', NULL, '学习进度示例'),
(39004, 34004, 36004, 22004, 20,  15.00, '0', '2025-09-10 09:00:00', '2025-09-14 21:00:00', 'admin', NOW(), '', NULL, '学习进度示例'),
(39005, 34005, 36005, 22005, 88,  80.00, '0', '2026-03-18 21:10:00', '2026-04-08 19:40:00', 'admin', NOW(), '', NULL, '学习进度示例');

INSERT IGNORE INTO edu_assignment
(assignment_id, course_id, title, assignment_type, content, total_score, submit_limit, allow_resubmit, deadline_time, publish_status, publish_user_id, publish_time, create_by, create_time, update_by, update_time, remark)
VALUES
(40001, 34001, '数据库建模练习',   '1', '根据给定业务场景绘制ER图，并完成关系模式设计。', 100.00, 3, '1', '2025-10-15 23:59:59', '1', 21001, '2025-10-01 10:00:00', 'admin', NOW(), '', NULL, '作业示例数据'),
(40002, 34002, 'Servlet综合实验',  '2', '完成一个基于Servlet的课程作业提交页面。',         100.00, 2, '1', '2025-10-20 23:59:59', '1', 21002, '2025-10-03 09:30:00', 'admin', NOW(), '', NULL, '作业示例数据'),
(40003, 34003, '管理案例分析报告', '1', '选取一个企业案例，分析其计划与组织流程。',         100.00, 1, '0', '2025-10-18 23:59:59', '1', 21003, '2025-10-02 15:20:00', 'admin', NOW(), '', NULL, '作业示例数据'),
(40004, 34004, '营销方案设计',     '3', '围绕校园文创产品撰写市场营销方案。',               100.00, 2, '1', '2025-10-25 23:59:59', '1', 21004, '2025-10-05 11:00:00', 'admin', NOW(), '', NULL, '作业示例数据'),
(40005, 34005, '英语阅读摘要写作', '1', '阅读指定英文材料，提交中英文摘要各一份。',         100.00, 2, '1', '2025-10-22 23:59:59', '1', 21005, '2025-10-04 16:00:00', 'admin', NOW(), '', NULL, '作业示例数据');

INSERT IGNORE INTO edu_assignment_attachment
(assignment_attachment_id, assignment_id, file_name, file_url, file_suffix, file_size, order_num, upload_user_id, create_by, create_time, update_by, update_time, remark)
VALUES
(41001, 40001, '数据库建模练习说明.pdf',   '/profile/assignment/db_model_notice.pdf',  'pdf',  512000, 1, 21001, 'admin', NOW(), '', NULL, '作业附件示例'),
(41002, 40002, 'Servlet综合实验要求.docx', '/profile/assignment/servlet_task.docx',    'docx', 256000, 1, 21002, 'admin', NOW(), '', NULL, '作业附件示例'),
(41003, 40003, '管理案例分析模板.docx',   '/profile/assignment/manage_case.docx',     'docx', 288000, 1, 21003, 'admin', NOW(), '', NULL, '作业附件示例'),
(41004, 40004, '营销方案评分标准.pdf',     '/profile/assignment/market_rule.pdf',      'pdf',  420000, 1, 21004, 'admin', NOW(), '', NULL, '作业附件示例'),
(41005, 40005, '英语阅读材料.zip',        '/profile/assignment/english_reading.zip', 'zip',  980000, 1, 21005, 'admin', NOW(), '', NULL, '作业附件示例');

INSERT IGNORE INTO edu_assignment_submission
(submission_id, assignment_id, course_id, student_user_id, submit_round, submit_remark, submit_time, late_flag, is_latest, review_status, score, teacher_comment, review_user_id, review_time, create_by, create_time, update_by, update_time, remark)
VALUES
(42001, 40001, 34001, 22001, 1, '第一次提交，已完成基础ER图设计。',   '2025-10-12 20:10:00', '0', '0', '1', 78.00, '结构基本完整，但范式说明不够充分。', 21001, '2025-10-13 10:00:00', 'admin', NOW(), '', NULL, '作业提交示例'),
(42002, 40001, 34001, 22001, 2, '根据评语补充范式说明后重新提交。', '2025-10-14 21:30:00', '0', '1', '1', 88.00, '重交后内容更完整，建模思路清晰。',     21001, '2025-10-15 09:20:00', 'admin', NOW(), '', NULL, '作业提交示例'),
(42003, 40002, 34002, 22002, 1, '已完成Servlet实验并附运行截图。', '2025-10-18 19:40:00', '0', '1', '1', 92.00, '功能实现完整，代码结构较规范。',       21002, '2025-10-19 14:30:00', 'admin', NOW(), '', NULL, '作业提交示例'),
(42004, 40003, 34003, 22003, 1, '案例分析报告已上传，请老师批阅。', '2025-10-19 00:30:00', '1', '1', '0', NULL,  '',                                     NULL,   NULL,                  'admin', NOW(), '', NULL, '作业提交示例'),
(42005, 40005, 34005, 22005, 1, '已提交中英文摘要与阅读心得。',     '2025-10-20 17:05:00', '0', '1', '1', 95.00, '摘要准确，语言表达流畅。',             21005, '2025-10-21 10:10:00', 'admin', NOW(), '', NULL, '作业提交示例');

INSERT IGNORE INTO edu_submission_attachment
(submission_attachment_id, submission_id, file_name, file_url, file_suffix, file_size, upload_user_id, create_by, create_time, update_by, update_time, remark)
VALUES
(43001, 42001, '数据库建模练习-初稿.docx',   '/profile/submission/db_model_v1.docx',    'docx', 350000, 22001, 'admin', NOW(), '', NULL, '提交附件示例'),
(43002, 42002, '数据库建模练习-终稿.docx',   '/profile/submission/db_model_v2.docx',    'docx', 420000, 22001, 'admin', NOW(), '', NULL, '提交附件示例'),
(43003, 42003, 'Servlet综合实验.zip',       '/profile/submission/servlet_lab.zip',     'zip',  860000, 22002, 'admin', NOW(), '', NULL, '提交附件示例'),
(43004, 42004, '管理案例分析报告.docx',     '/profile/submission/manage_case.docx',    'docx', 290000, 22003, 'admin', NOW(), '', NULL, '提交附件示例'),
(43005, 42005, '英语阅读摘要.pdf',          '/profile/submission/english_summary.pdf', 'pdf',  310000, 22005, 'admin', NOW(), '', NULL, '提交附件示例');

INSERT IGNORE INTO edu_course_notice
(notice_id, course_id, title, content, notice_type, top_flag, publish_user_id, publish_time, status, create_by, create_time, update_by, update_time, remark)
VALUES
(44001, 34001, '数据库原理第一次作业已发布', '请同学们于截止时间前完成数据库建模练习，重交仅限未关闭前。', '1', '1', 21001, '2025-10-01 10:05:00', '1', 'admin', NOW(), '', NULL, '公告示例数据'),
(44002, 34002, 'Java Web实验课安排提醒',   '本周实验课请提前完成环境配置，课堂将检查运行效果。',         '2', '0', 21002, '2025-10-06 08:30:00', '1', 'admin', NOW(), '', NULL, '公告示例数据'),
(44003, 34003, '管理学基础讨论课通知',     '下周课程将围绕企业案例开展课堂讨论，请提前阅读资料。',       '1', '0', 21003, '2025-10-07 09:00:00', '1', 'admin', NOW(), '', NULL, '公告示例数据'),
(44004, 34004, '营销方案课堂展示说明',     '请已选课同学准备5分钟方案展示，按名单顺序依次汇报。',       '1', '0', 21004, '2025-10-08 14:10:00', '1', 'admin', NOW(), '', NULL, '公告示例数据'),
(44005, 34005, '英语阅读补充资料上传完成', '新增两篇拓展阅读材料，建议本周内完成阅读并记录关键词。',   '2', '0', 21005, '2025-10-09 18:20:00', '1', 'admin', NOW(), '', NULL, '公告示例数据');

INSERT IGNORE INTO edu_user_message
(message_id, user_id, course_id, assignment_id, notice_id, biz_type, message_title, message_content, sender_user_id, send_time, read_status, read_time, link_path, expire_time, create_by, create_time, update_by, update_time, remark)
VALUES
(45001, 22001, 34001, 40001, NULL,  'assignment_graded',   '数据库原理作业已批改',     '你的“数据库建模练习”已完成批改，请及时查看分数与评语。', 21001, '2025-10-15 09:25:00', '1', '2025-10-15 12:00:00', '/education/submission/detail/42002', '2025-10-31 23:59:59', 'admin', NOW(), '', NULL, '消息示例数据'),
(45002, 22002, 34002, 40002, NULL,  'assignment_deadline', 'Java Web作业即将截止',     '“Servlet综合实验”将于今晚23:59截止，请及时确认提交内容。', 21002, '2025-10-20 18:00:00', '0', NULL,                  '/education/assignment/detail/40002',  '2025-10-20 23:59:59', 'admin', NOW(), '', NULL, '消息示例数据'),
(45003, 22003, 34003, 40003, NULL,  'new_assignment',      '管理学基础发布新作业',     '课程已发布“管理案例分析报告”，请按要求完成并提交。',       21003, '2025-10-02 15:30:00', '0', NULL,                  '/education/assignment/detail/40003',  '2025-10-31 23:59:59', 'admin', NOW(), '', NULL, '消息示例数据'),
(45004, 22004, 34004, NULL,  44004, 'course_notice',       '市场营销实务发布课程公告', '老师发布了新的课程公告，请查看课堂展示相关安排。',         21004, '2025-10-08 14:15:00', '1', '2025-10-08 16:00:00', '/education/notice/detail/44004',      '2025-10-31 23:59:59', 'admin', NOW(), '', NULL, '消息示例数据'),
(45005, 22005, 34005, NULL,  NULL,  'new_course',          '综合英语提升课程已发布',   '你所在院系新增“综合英语提升”课程，可进入课程中心查看详情。', 21005, '2025-09-01 09:00:00', '0', NULL,                  '/education/course/detail/34005',      '2025-12-31 23:59:59', 'admin', NOW(), '', NULL, '消息示例数据');

-- ----------------------------
-- 三点一、student1 / teacher1 / master1 账号联动演示数据
-- 已拆分到独立脚本：sql/xuexitong_teaching_user123_demo.sql
-- 如需给已存在的 student1 / teacher1 / master1 账号单独导入演示数据，请执行该脚本

-- ----------------------------
-- 四、教学业务字典初始化
-- ----------------------------
INSERT IGNORE INTO sys_dict_type
(dict_id, dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES
(26001, '教学-学期状态',     'edu_term_status',               '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26002, '教学-课程类型',     'edu_course_type',               '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26003, '教学-课程状态',     'edu_course_status',             '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26004, '教学-教师角色',     'edu_teacher_role',              '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26005, '教学-资源类型',     'edu_resource_type',             '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26006, '教学-选课来源',     'edu_enroll_source_type',        '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26007, '教学-选课状态',     'edu_enroll_status',             '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26008, '教学-学籍状态',     'edu_study_status',              '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26009, '教学-学习完成状态', 'edu_progress_completed_flag',   '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26010, '教学-作业类型',     'edu_assignment_type',           '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26011, '教学-作业发布状态', 'edu_assignment_publish_status', '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26012, '教学-批改状态',     'edu_review_status',             '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26013, '教学-公告类型',     'edu_notice_type',               '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26014, '教学-公告状态',     'edu_notice_status',             '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典'),
(26015, '教学-消息已读状态', 'edu_message_read_status',       '0', 'admin', NOW(), '', NULL, '学习通教学管理平台字典');

INSERT IGNORE INTO sys_dict_data
(dict_code, dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES
(26101, 1, '未开始',   '0', 'edu_term_status',               '', 'info',    'N', '0', 'admin', NOW(), '', NULL, '学期状态'),
(26102, 2, '进行中',   '1', 'edu_term_status',               '', 'warning', 'Y', '0', 'admin', NOW(), '', NULL, '学期状态'),
(26103, 3, '已结束',   '2', 'edu_term_status',               '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '学期状态'),
(26104, 1, '必修',     '1', 'edu_course_type',               '', 'danger',  'N', '0', 'admin', NOW(), '', NULL, '课程类型'),
(26105, 2, '选修',     '2', 'edu_course_type',               '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '课程类型'),
(26106, 1, '草稿',     '0', 'edu_course_status',             '', 'info',    'N', '0', 'admin', NOW(), '', NULL, '课程状态'),
(26107, 2, '已发布',   '1', 'edu_course_status',             '', 'success', 'Y', '0', 'admin', NOW(), '', NULL, '课程状态'),
(26108, 3, '已下架',   '2', 'edu_course_status',             '', 'danger',  'N', '0', 'admin', NOW(), '', NULL, '课程状态'),
(26109, 1, '主讲教师', '1', 'edu_teacher_role',              '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '教师角色'),
(26110, 2, '助教',     '2', 'edu_teacher_role',              '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '教师角色'),
(26111, 1, '课件',     '1', 'edu_resource_type',             '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '资源类型'),
(26112, 2, '文档资料', '2', 'edu_resource_type',             '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '资源类型'),
(26113, 3, '图片',     '3', 'edu_resource_type',             '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '资源类型'),
(26114, 4, '视频',     '4', 'edu_resource_type',             '', 'danger',  'N', '0', 'admin', NOW(), '', NULL, '资源类型'),
(26115, 5, '压缩包',   '5', 'edu_resource_type',             '', 'info',    'N', '0', 'admin', NOW(), '', NULL, '资源类型'),
(26116, 6, '其他',     '6', 'edu_resource_type',             '', 'info',    'N', '0', 'admin', NOW(), '', NULL, '资源类型'),
(26117, 1, '学生自选', '1', 'edu_enroll_source_type',        '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '选课来源'),
(26118, 2, '管理员分配', '2', 'edu_enroll_source_type',      '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '选课来源'),
(26119, 1, '已选课',   '1', 'edu_enroll_status',             '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '选课状态'),
(26120, 2, '已退课',   '2', 'edu_enroll_status',             '', 'danger',  'N', '0', 'admin', NOW(), '', NULL, '选课状态'),
(26121, 3, '已完成',   '3', 'edu_enroll_status',             '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '选课状态'),
(26122, 1, '在读',     '1', 'edu_study_status',              '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '学籍状态'),
(26123, 2, '休学',     '2', 'edu_study_status',              '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '学籍状态'),
(26124, 3, '毕业',     '3', 'edu_study_status',              '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '学籍状态'),
(26125, 1, '未完成',   '0', 'edu_progress_completed_flag',   '', 'info',    'Y', '0', 'admin', NOW(), '', NULL, '学习完成状态'),
(26126, 2, '已完成',   '1', 'edu_progress_completed_flag',   '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '学习完成状态'),
(26127, 1, '普通作业', '1', 'edu_assignment_type',           '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '作业类型'),
(26128, 2, '实验作业', '2', 'edu_assignment_type',           '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '作业类型'),
(26129, 3, '课程设计', '3', 'edu_assignment_type',           '', 'danger',  'N', '0', 'admin', NOW(), '', NULL, '作业类型'),
(26130, 1, '草稿',     '0', 'edu_assignment_publish_status', '', 'info',    'N', '0', 'admin', NOW(), '', NULL, '作业发布状态'),
(26131, 2, '已发布',   '1', 'edu_assignment_publish_status', '', 'success', 'Y', '0', 'admin', NOW(), '', NULL, '作业发布状态'),
(26132, 3, '已关闭',   '2', 'edu_assignment_publish_status', '', 'danger',  'N', '0', 'admin', NOW(), '', NULL, '作业发布状态'),
(26133, 1, '未批改',   '0', 'edu_review_status',             '', 'warning', 'Y', '0', 'admin', NOW(), '', NULL, '批改状态'),
(26134, 2, '已批改',   '1', 'edu_review_status',             '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '批改状态'),
(26135, 1, '课程公告', '1', 'edu_notice_type',               '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '公告类型'),
(26136, 2, '课程提醒', '2', 'edu_notice_type',               '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '公告类型'),
(26137, 1, '草稿',     '0', 'edu_notice_status',             '', 'info',    'N', '0', 'admin', NOW(), '', NULL, '公告状态'),
(26138, 2, '已发布',   '1', 'edu_notice_status',             '', 'success', 'Y', '0', 'admin', NOW(), '', NULL, '公告状态'),
(26139, 3, '已撤回',   '2', 'edu_notice_status',             '', 'danger',  'N', '0', 'admin', NOW(), '', NULL, '公告状态'),
(26140, 1, '未读',     '0', 'edu_message_read_status',       '', 'warning', 'Y', '0', 'admin', NOW(), '', NULL, '消息已读状态'),
(26141, 2, '已读',     '1', 'edu_message_read_status',       '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '消息已读状态');

-- ----------------------------
-- 五、教学管理菜单初始化
-- 说明：
-- 1. 不能再使用固定 menu_id + INSERT IGNORE，否则二次开发项目里一旦 menu_id 已被占用，会被静默跳过
-- 2. 改为按 path 动态补齐菜单，重复执行也安全
-- ----------------------------
DROP TEMPORARY TABLE IF EXISTS tmp_edu_menu_seed;
CREATE TEMPORARY TABLE tmp_edu_menu_seed
(
  order_num   int(11)      NOT NULL,
  menu_name   varchar(100) NOT NULL,
  path        varchar(100) NOT NULL,
  component   varchar(255) DEFAULT NULL,
  perms       varchar(100) DEFAULT '',
  icon        varchar(100) DEFAULT '',
  remark      varchar(255) DEFAULT NULL
) ENGINE=Memory DEFAULT CHARSET=utf8mb4;

INSERT INTO tmp_edu_menu_seed
(order_num, menu_name, path, component, perms, icon, remark)
VALUES
(1,  '学期管理', 'term',          'education/term/index',          'education:term:list',          'date',          '学期管理菜单'),
(2,  '课程管理', 'course',        'education/course/index',        'education:course:list',        'skill',         '课程管理菜单'),
(3,  '教师档案', 'teacher',       'education/teacher/index',       'education:teacher:list',       'peoples',       '教师档案菜单'),
(4,  '学生档案', 'student',       'education/student/index',       'education:student:list',       'user',          '学生档案菜单'),
(5,  '选课管理', 'enrollment',    'education/enrollment/index',    'education:enrollment:list',    'edit',          '选课管理菜单'),
(6,  '学习资源', 'resource',      'education/resource/index',      'education:resource:list',      'documentation', '学习资源菜单'),
(7,  '作业管理', 'assignment',    'education/assignment/index',    'education:assignment:list',    'form',          '作业管理菜单'),
(8,  '作业提交', 'submission',    'education/submission/index',    'education:submission:list',    'build',         '作业提交菜单'),
(9,  '课程公告', 'notice',        'education/notice/index',        'education:notice:list',        'message',       '课程公告菜单'),
(10, '学习进度', 'progress',      'education/progress/index',      'education:progress:list',      'tree',          '学习进度菜单'),
(11, '消息提醒', 'message',       'education/message/index',       'education:message:list',       'message',       '消息提醒菜单'),
(12, '数据统计', 'statistics',    'education/statistics/index',    'education:statistics:list',    'chart',         '数据统计菜单'),
(13, '授课安排', 'courseTeacher', 'education/courseTeacher/index', 'education:courseTeacher:list', 'post',          '授课安排菜单'),
(14, '课程章节', 'chapter',       'education/chapter/index',       'education:chapter:list',       'tree-table',    '课程章节菜单');

SET @education_root_id := (SELECT menu_id FROM sys_menu WHERE path = 'education' AND menu_type = 'M' LIMIT 1);
SET @next_menu_id := (SELECT IFNULL(MAX(menu_id), 0) + 1 FROM sys_menu);

INSERT INTO sys_menu
(menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT
  @next_menu_id,
  '教学管理',
  0,
  10,
  'education',
  NULL,
  '',
  '',
  1,
  0,
  'M',
  '0',
  '0',
  '',
  'system',
  'admin',
  NOW(),
  '',
  NULL,
  '学习通教学管理目录'
FROM dual
WHERE @education_root_id IS NULL;

SET @education_root_id := IFNULL(@education_root_id, @next_menu_id);

UPDATE sys_menu
SET
  menu_name = '教学管理',
  parent_id = 0,
  order_num = 10,
  path = 'education',
  component = NULL,
  `query` = '',
  route_name = '',
  is_frame = 1,
  is_cache = 0,
  menu_type = 'M',
  visible = '0',
  status = '0',
  perms = '',
  icon = 'system',
  update_by = 'admin',
  update_time = NOW(),
  remark = '学习通教学管理目录'
WHERE menu_id = @education_root_id;

SET @next_menu_id := (SELECT IFNULL(MAX(menu_id), 0) FROM sys_menu);

INSERT INTO sys_menu
(menu_id, menu_name, parent_id, order_num, path, component, `query`, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT
  @next_menu_id := @next_menu_id + 1,
  t.menu_name,
  @education_root_id,
  t.order_num,
  t.path,
  t.component,
  '',
  '',
  1,
  0,
  'C',
  '0',
  '0',
  t.perms,
  t.icon,
  'admin',
  NOW(),
  '',
  NULL,
  t.remark
FROM tmp_edu_menu_seed t
WHERE NOT EXISTS (
  SELECT 1
  FROM sys_menu m
  WHERE m.parent_id = @education_root_id
    AND m.path = t.path
    AND m.menu_type = 'C'
);

UPDATE sys_menu m
JOIN tmp_edu_menu_seed t
  ON m.parent_id = @education_root_id
 AND m.path = t.path
 AND m.menu_type = 'C'
SET
  m.menu_name = t.menu_name,
  m.order_num = t.order_num,
  m.component = t.component,
  m.`query` = '',
  m.route_name = '',
  m.is_frame = 1,
  m.is_cache = 0,
  m.visible = '0',
  m.status = '0',
  m.perms = t.perms,
  m.icon = t.icon,
  m.update_by = 'admin',
  m.update_time = NOW(),
  m.remark = t.remark;

DROP TEMPORARY TABLE IF EXISTS tmp_edu_menu_seed;

-- ----------------------------
-- 六、角色与教学菜单关联
-- 说明：
-- 1. 补充 admin 角色，便于直接用管理员账号查看菜单
-- 2. 不再依赖固定 menu_id，统一按根菜单 + 子菜单 path 关联
-- ----------------------------
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
JOIN sys_menu m
  ON m.menu_id = @education_root_id
  OR m.parent_id = @education_root_id
WHERE r.role_key IN ('admin', 'master');

INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
JOIN sys_menu m
  ON m.menu_id = @education_root_id
  OR (m.parent_id = @education_root_id AND m.path IN ('course', 'resource', 'assignment', 'submission', 'notice', 'progress', 'message', 'statistics', 'courseTeacher', 'chapter'))
WHERE r.role_key = 'teacher';

INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r
JOIN sys_menu m
  ON m.menu_id = @education_root_id
  OR (m.parent_id = @education_root_id AND m.path IN ('course', 'enrollment', 'resource', 'assignment', 'submission', 'notice', 'progress', 'message'))
WHERE r.role_key = 'student';

SET FOREIGN_KEY_CHECKS = 1;
