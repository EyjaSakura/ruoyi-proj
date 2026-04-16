-- 学习通教学管理平台业务表设计
-- 适用环境：MySQL 5.7 / UTF-8（建议 utf8mb4）
-- 说明：
-- 1. 用户表沿用若依 sys_user，通过 sys_role.role_key 区分角色：
--    学生：student
--    教师：teacher
--    院系管理员：master
-- 2. 院系信息沿用若依 sys_dept，业务表中的 dept_id 逻辑关联 sys_dept.dept_id
-- 3. 本脚本仅包含教学业务表与角色初始化 SQL，不修改若依基础表结构

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 角色初始化（沿用若依 sys_role）
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
-- 1. 教师档案表
-- ----------------------------
DROP TABLE IF EXISTS edu_teacher_profile;
CREATE TABLE edu_teacher_profile (
  teacher_id          bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '教师档案ID',
  user_id             bigint(20)    NOT NULL COMMENT '用户ID，对应sys_user.user_id',
  teacher_no          varchar(32)   NOT NULL COMMENT '教师工号',
  teacher_name        varchar(30)   NOT NULL COMMENT '教师姓名',
  dept_id             bigint(20)    NOT NULL COMMENT '院系ID，对应sys_dept.dept_id',
  job_title           varchar(50)   DEFAULT '' COMMENT '职称',
  phone               varchar(11)   DEFAULT '' COMMENT '联系电话',
  email               varchar(50)   DEFAULT '' COMMENT '邮箱',
  office_address      varchar(100)  DEFAULT '' COMMENT '办公地点',
  research_direction  varchar(255)  DEFAULT '' COMMENT '研究方向',
  intro               varchar(500)  DEFAULT '' COMMENT '教师简介',
  status              char(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  del_flag            char(1)       DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (teacher_id),
  UNIQUE KEY uk_teacher_user_id (user_id),
  UNIQUE KEY uk_teacher_no (teacher_no),
  KEY idx_teacher_dept_id (dept_id),
  KEY idx_teacher_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师档案表';

-- ----------------------------
-- 2. 学生档案表
-- ----------------------------
DROP TABLE IF EXISTS edu_student_profile;
CREATE TABLE edu_student_profile (
  student_id          bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '学生档案ID',
  user_id             bigint(20)    NOT NULL COMMENT '用户ID，对应sys_user.user_id',
  student_no          varchar(32)   NOT NULL COMMENT '学号',
  student_name        varchar(30)   NOT NULL COMMENT '学生姓名',
  dept_id             bigint(20)    NOT NULL COMMENT '院系ID，对应sys_dept.dept_id',
  major_name          varchar(100)  DEFAULT '' COMMENT '专业名称',
  class_name          varchar(100)  DEFAULT '' COMMENT '班级名称',
  grade_name          varchar(30)   DEFAULT '' COMMENT '年级',
  admission_year      char(4)       DEFAULT '' COMMENT '入学年份',
  phone               varchar(11)   DEFAULT '' COMMENT '联系电话',
  email               varchar(50)   DEFAULT '' COMMENT '邮箱',
  study_status        char(1)       DEFAULT '1' COMMENT '学籍状态（1在读 2休学 3毕业）',
  status              char(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  del_flag            char(1)       DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (student_id),
  UNIQUE KEY uk_student_user_id (user_id),
  UNIQUE KEY uk_student_no (student_no),
  KEY idx_student_dept_id (dept_id),
  KEY idx_student_status (status),
  KEY idx_student_study_status (study_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生档案表';

-- ----------------------------
-- 3. 学期表
-- ----------------------------
DROP TABLE IF EXISTS edu_term;
CREATE TABLE edu_term (
  term_id             bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '学期ID',
  term_code           varchar(30)   NOT NULL COMMENT '学期编码',
  term_name           varchar(50)   NOT NULL COMMENT '学期名称',
  school_year         varchar(20)   NOT NULL COMMENT '学年，例如2025-2026',
  semester_no         char(1)       NOT NULL COMMENT '学期序号（1第一学期 2第二学期 3第三学期）',
  start_date          date          DEFAULT NULL COMMENT '开学日期',
  end_date            date          DEFAULT NULL COMMENT '结束日期',
  select_start_time   datetime      DEFAULT NULL COMMENT '选课开始时间',
  select_end_time     datetime      DEFAULT NULL COMMENT '选课结束时间',
  status              char(1)       DEFAULT '0' COMMENT '状态（0未开始 1进行中 2已结束）',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (term_id),
  UNIQUE KEY uk_term_code (term_code),
  KEY idx_term_status (status),
  KEY idx_term_school_year (school_year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学期表';

-- ----------------------------
-- 4. 课程表
-- ----------------------------
DROP TABLE IF EXISTS edu_course;
CREATE TABLE edu_course (
  course_id           bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  term_id             bigint(20)    NOT NULL COMMENT '学期ID',
  dept_id             bigint(20)    NOT NULL COMMENT '开课院系ID',
  course_code         varchar(32)   NOT NULL COMMENT '课程号',
  class_no            varchar(4)    NOT NULL DEFAULT '01' COMMENT '课序号（如01 02 03，同一课程号同一学期可开多个班）',
  course_name         varchar(100)  NOT NULL COMMENT '课程名称',
  course_cover        varchar(255)  DEFAULT '' COMMENT '课程封面',
  course_type         char(1)       DEFAULT '2' COMMENT '课程类型（1必修 2选修）',
  credit              decimal(4,1)  DEFAULT 0.0 COMMENT '学分',
  total_hours         int(11)       DEFAULT 0 COMMENT '总课时',
  capacity            int(11)       DEFAULT 0 COMMENT '容量上限，0表示不限',
  selected_count      int(11)       DEFAULT 0 COMMENT '当前已选人数',
  select_start_time   datetime      DEFAULT NULL COMMENT '选课开始时间',
  select_end_time     datetime      DEFAULT NULL COMMENT '选课结束时间',
  intro               text COMMENT '课程简介',
  complete_rule       varchar(500)  DEFAULT '' COMMENT '课程完成规则说明',
  status              char(1)       DEFAULT '0' COMMENT '状态（0草稿 1已发布 2已下架）',
  del_flag            char(1)       DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (course_id),
  UNIQUE KEY uk_course_term_code_class (term_id, course_code, class_no),
  KEY idx_course_dept_id (dept_id),
  KEY idx_course_term_id (term_id),
  KEY idx_course_status (status),
  KEY idx_course_name (course_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- ----------------------------
-- 5. 课程授课教师关联表
-- ----------------------------
DROP TABLE IF EXISTS edu_course_teacher;
CREATE TABLE edu_course_teacher (
  course_teacher_id   bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '授课关联ID',
  course_id           bigint(20)    NOT NULL COMMENT '课程ID',
  teacher_user_id     bigint(20)    NOT NULL COMMENT '教师用户ID，对应sys_user.user_id',
  teacher_role        char(1)       DEFAULT '1' COMMENT '教师角色（1主讲教师 2助教）',
  is_owner            char(1)       DEFAULT '1' COMMENT '是否负责人（0否 1是）',
  order_num           int(11)       DEFAULT 1 COMMENT '排序号',
  status              char(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (course_teacher_id),
  UNIQUE KEY uk_course_teacher (course_id, teacher_user_id),
  KEY idx_course_teacher_course_id (course_id),
  KEY idx_course_teacher_user_id (teacher_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程授课教师关联表';

-- ----------------------------
-- 6. 课程章节表
-- ----------------------------
DROP TABLE IF EXISTS edu_course_chapter;
CREATE TABLE edu_course_chapter (
  chapter_id          bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '章节ID',
  course_id           bigint(20)    NOT NULL COMMENT '课程ID',
  parent_id           bigint(20)    DEFAULT 0 COMMENT '父章节ID',
  ancestors           varchar(200)  DEFAULT '' COMMENT '祖级列表',
  chapter_title       varchar(100)  NOT NULL COMMENT '章节标题',
  chapter_type        char(1)       DEFAULT '1' COMMENT '章节类型（1章 2节）',
  chapter_desc        varchar(500)  DEFAULT '' COMMENT '章节说明',
  order_num           int(11)       DEFAULT 1 COMMENT '排序号',
  duration_minutes    int(11)       DEFAULT 0 COMMENT '建议学习时长（分钟）',
  status              char(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (chapter_id),
  KEY idx_chapter_course_id (course_id),
  KEY idx_chapter_parent_id (parent_id),
  KEY idx_chapter_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程章节表';

-- ----------------------------
-- 7. 课程资源表
-- ----------------------------
DROP TABLE IF EXISTS edu_course_resource;
CREATE TABLE edu_course_resource (
  resource_id         bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  course_id           bigint(20)    NOT NULL COMMENT '课程ID',
  chapter_id          bigint(20)    DEFAULT NULL COMMENT '章节ID，可为空',
  resource_title      varchar(100)  NOT NULL COMMENT '资源标题',
  resource_type       char(1)       DEFAULT '1' COMMENT '资源类型（1课件 2文档资料 3图片 4视频 5压缩包 6其他）',
  file_name           varchar(255)  NOT NULL COMMENT '文件原名称',
  file_url            varchar(255)  NOT NULL COMMENT '文件地址',
  file_suffix         varchar(20)   DEFAULT '' COMMENT '文件后缀',
  file_size           bigint(20)    DEFAULT 0 COMMENT '文件大小，单位字节',
  download_count      int(11)       DEFAULT 0 COMMENT '下载次数',
  upload_user_id      bigint(20)    NOT NULL COMMENT '上传人用户ID',
  order_num           int(11)       DEFAULT 1 COMMENT '排序号',
  status              char(1)       DEFAULT '0' COMMENT '状态（0正常 1停用）',
  del_flag            char(1)       DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (resource_id),
  KEY idx_resource_course_id (course_id),
  KEY idx_resource_chapter_id (chapter_id),
  KEY idx_resource_type (resource_type),
  KEY idx_resource_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程资源表';

-- ----------------------------
-- 8. 学生选课表
-- ----------------------------
DROP TABLE IF EXISTS edu_course_enrollment;
CREATE TABLE edu_course_enrollment (
  enrollment_id       bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '选课记录ID',
  course_id           bigint(20)    NOT NULL COMMENT '课程ID',
  term_id             bigint(20)    NOT NULL COMMENT '学期ID',
  student_user_id     bigint(20)    NOT NULL COMMENT '学生用户ID，对应sys_user.user_id',
  source_type         char(1)       DEFAULT '1' COMMENT '来源类型（1学生自选 2管理员分配）',
  enroll_time         datetime      DEFAULT NULL COMMENT '选课时间',
  drop_time           datetime      DEFAULT NULL COMMENT '退课时间',
  finish_time         datetime      DEFAULT NULL COMMENT '完成时间',
  enroll_status       char(1)       DEFAULT '1' COMMENT '选课状态（1已选 2已退课 3已完成）',
  progress_percent    decimal(5,2)  DEFAULT 0.00 COMMENT '课程完成进度',
  required_task_count int(11)       DEFAULT 0 COMMENT '应完成任务数',
  finished_task_count int(11)       DEFAULT 0 COMMENT '已完成任务数',
  last_study_time     datetime      DEFAULT NULL COMMENT '最近学习时间',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (enrollment_id),
  UNIQUE KEY uk_enrollment_course_student (course_id, student_user_id),
  KEY idx_enrollment_term_id (term_id),
  KEY idx_enrollment_student_user_id (student_user_id),
  KEY idx_enrollment_status (enroll_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生选课表';

-- ----------------------------
-- 9. 学习进度表
-- ----------------------------
DROP TABLE IF EXISTS edu_learning_progress;
CREATE TABLE edu_learning_progress (
  progress_id         bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '学习进度ID',
  course_id           bigint(20)    NOT NULL COMMENT '课程ID',
  chapter_id          bigint(20)    NOT NULL COMMENT '章节ID',
  student_user_id     bigint(20)    NOT NULL COMMENT '学生用户ID',
  learn_minutes       int(11)       DEFAULT 0 COMMENT '累计学习时长（分钟）',
  progress_percent    decimal(5,2)  DEFAULT 0.00 COMMENT '章节完成进度',
  completed_flag      char(1)       DEFAULT '0' COMMENT '是否完成（0否 1是）',
  first_study_time    datetime      DEFAULT NULL COMMENT '首次学习时间',
  last_study_time     datetime      DEFAULT NULL COMMENT '最近学习时间',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (progress_id),
  UNIQUE KEY uk_progress_course_chapter_student (course_id, chapter_id, student_user_id),
  KEY idx_progress_student_user_id (student_user_id),
  KEY idx_progress_completed_flag (completed_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习进度表';

-- ----------------------------
-- 10. 作业表
-- ----------------------------
DROP TABLE IF EXISTS edu_assignment;
CREATE TABLE edu_assignment (
  assignment_id       bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '作业ID',
  course_id           bigint(20)    NOT NULL COMMENT '课程ID',
  title               varchar(100)  NOT NULL COMMENT '作业标题',
  assignment_type     char(1)       DEFAULT '1' COMMENT '作业类型（1普通作业 2实验作业 3课程设计）',
  content             text COMMENT '作业要求',
  total_score         decimal(6,2)  DEFAULT 100.00 COMMENT '总分',
  submit_limit        int(11)       DEFAULT 0 COMMENT '最大提交次数，0表示不限',
  allow_resubmit      char(1)       DEFAULT '1' COMMENT '是否允许重交（0否 1是）',
  deadline_time       datetime      DEFAULT NULL COMMENT '截止时间',
  publish_status      char(1)       DEFAULT '0' COMMENT '发布状态（0草稿 1已发布 2已关闭）',
  publish_user_id     bigint(20)    NOT NULL COMMENT '发布人用户ID',
  publish_time        datetime      DEFAULT NULL COMMENT '发布时间',
  create_by           varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time         datetime      DEFAULT NULL COMMENT '创建时间',
  update_by           varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time         datetime      DEFAULT NULL COMMENT '更新时间',
  remark              varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (assignment_id),
  KEY idx_assignment_course_id (course_id),
  KEY idx_assignment_deadline_time (deadline_time),
  KEY idx_assignment_publish_status (publish_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业表';

-- ----------------------------
-- 11. 作业附件表
-- ----------------------------
DROP TABLE IF EXISTS edu_assignment_attachment;
CREATE TABLE edu_assignment_attachment (
  assignment_attachment_id bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '作业附件ID',
  assignment_id            bigint(20)   NOT NULL COMMENT '作业ID',
  file_name                varchar(255) NOT NULL COMMENT '文件原名称',
  file_url                 varchar(255) NOT NULL COMMENT '文件地址',
  file_suffix              varchar(20)  DEFAULT '' COMMENT '文件后缀',
  file_size                bigint(20)   DEFAULT 0 COMMENT '文件大小，单位字节',
  order_num                int(11)      DEFAULT 1 COMMENT '排序号',
  upload_user_id           bigint(20)   NOT NULL COMMENT '上传人用户ID',
  create_by                varchar(64)  DEFAULT '' COMMENT '创建者',
  create_time              datetime     DEFAULT NULL COMMENT '创建时间',
  update_by                varchar(64)  DEFAULT '' COMMENT '更新者',
  update_time              datetime     DEFAULT NULL COMMENT '更新时间',
  remark                   varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (assignment_attachment_id),
  KEY idx_assignment_attachment_assignment_id (assignment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业附件表';

-- ----------------------------
-- 12. 作业提交表
-- ----------------------------
DROP TABLE IF EXISTS edu_assignment_submission;
CREATE TABLE edu_assignment_submission (
  submission_id        bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '作业提交ID',
  assignment_id        bigint(20)    NOT NULL COMMENT '作业ID',
  course_id            bigint(20)    NOT NULL COMMENT '课程ID',
  student_user_id      bigint(20)    NOT NULL COMMENT '学生用户ID',
  submit_round         int(11)       DEFAULT 1 COMMENT '第几次提交',
  submit_remark        varchar(500)  DEFAULT '' COMMENT '提交说明',
  submit_time          datetime      DEFAULT NULL COMMENT '提交时间',
  late_flag            char(1)       DEFAULT '0' COMMENT '是否逾期（0否 1是）',
  is_latest            char(1)       DEFAULT '1' COMMENT '是否当前最新提交（0否 1是）',
  review_status        char(1)       DEFAULT '0' COMMENT '批改状态（0未批改 1已批改）',
  score                decimal(6,2)  DEFAULT NULL COMMENT '得分',
  teacher_comment      varchar(1000) DEFAULT '' COMMENT '教师评语',
  review_user_id       bigint(20)    DEFAULT NULL COMMENT '批改教师用户ID',
  review_time          datetime      DEFAULT NULL COMMENT '批改时间',
  create_by            varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time          datetime      DEFAULT NULL COMMENT '创建时间',
  update_by            varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time          datetime      DEFAULT NULL COMMENT '更新时间',
  remark               varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (submission_id),
  KEY idx_submission_assignment_student (assignment_id, student_user_id),
  KEY idx_submission_course_id (course_id),
  KEY idx_submission_latest (is_latest),
  KEY idx_submission_review_status (review_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业提交表';

-- ----------------------------
-- 13. 作业提交附件表
-- ----------------------------
DROP TABLE IF EXISTS edu_submission_attachment;
CREATE TABLE edu_submission_attachment (
  submission_attachment_id bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '提交附件ID',
  submission_id            bigint(20)   NOT NULL COMMENT '作业提交ID',
  file_name                varchar(255) NOT NULL COMMENT '文件原名称',
  file_url                 varchar(255) NOT NULL COMMENT '文件地址',
  file_suffix              varchar(20)  DEFAULT '' COMMENT '文件后缀',
  file_size                bigint(20)   DEFAULT 0 COMMENT '文件大小，单位字节',
  upload_user_id           bigint(20)   NOT NULL COMMENT '上传人用户ID',
  create_by                varchar(64)  DEFAULT '' COMMENT '创建者',
  create_time              datetime     DEFAULT NULL COMMENT '创建时间',
  update_by                varchar(64)  DEFAULT '' COMMENT '更新者',
  update_time              datetime     DEFAULT NULL COMMENT '更新时间',
  remark                   varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (submission_attachment_id),
  KEY idx_submission_attachment_submission_id (submission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业提交附件表';

-- ----------------------------
-- 14. 课程公告表
-- ----------------------------
DROP TABLE IF EXISTS edu_course_notice;
CREATE TABLE edu_course_notice (
  notice_id            bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  course_id            bigint(20)    NOT NULL COMMENT '课程ID',
  title                varchar(100)  NOT NULL COMMENT '公告标题',
  content              text COMMENT '公告内容',
  notice_type          char(1)       DEFAULT '1' COMMENT '公告类型（1课程公告 2课程提醒）',
  top_flag             char(1)       DEFAULT '0' COMMENT '是否置顶（0否 1是）',
  publish_user_id      bigint(20)    NOT NULL COMMENT '发布人用户ID',
  publish_time         datetime      DEFAULT NULL COMMENT '发布时间',
  status               char(1)       DEFAULT '1' COMMENT '状态（0草稿 1已发布 2已撤回）',
  create_by            varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time          datetime      DEFAULT NULL COMMENT '创建时间',
  update_by            varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time          datetime      DEFAULT NULL COMMENT '更新时间',
  remark               varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (notice_id),
  KEY idx_notice_course_id (course_id),
  KEY idx_notice_publish_user_id (publish_user_id),
  KEY idx_notice_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程公告表';

-- ----------------------------
-- 15. 用户消息提醒表
-- ----------------------------
DROP TABLE IF EXISTS edu_user_message;
CREATE TABLE edu_user_message (
  message_id           bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  user_id              bigint(20)    NOT NULL COMMENT '接收用户ID',
  course_id            bigint(20)    DEFAULT NULL COMMENT '课程ID',
  assignment_id        bigint(20)    DEFAULT NULL COMMENT '作业ID',
  notice_id            bigint(20)    DEFAULT NULL COMMENT '公告ID',
  biz_type             varchar(32)   NOT NULL COMMENT '业务类型（assignment_deadline/assignment_graded/new_assignment/new_course/course_notice）',
  message_title        varchar(100)  NOT NULL COMMENT '消息标题',
  message_content      varchar(500)  NOT NULL COMMENT '消息内容',
  sender_user_id       bigint(20)    DEFAULT NULL COMMENT '发送人用户ID',
  send_time            datetime      DEFAULT NULL COMMENT '发送时间',
  read_status          char(1)       DEFAULT '0' COMMENT '已读状态（0未读 1已读）',
  read_time            datetime      DEFAULT NULL COMMENT '已读时间',
  link_path            varchar(255)  DEFAULT '' COMMENT '页面跳转路径',
  expire_time          datetime      DEFAULT NULL COMMENT '过期时间',
  create_by            varchar(64)   DEFAULT '' COMMENT '创建者',
  create_time          datetime      DEFAULT NULL COMMENT '创建时间',
  update_by            varchar(64)   DEFAULT '' COMMENT '更新者',
  update_time          datetime      DEFAULT NULL COMMENT '更新时间',
  remark               varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (message_id),
  KEY idx_message_user_id (user_id),
  KEY idx_message_read_status (read_status),
  KEY idx_message_biz_type (biz_type),
  KEY idx_message_send_time (send_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息提醒表';

SET FOREIGN_KEY_CHECKS = 1;
