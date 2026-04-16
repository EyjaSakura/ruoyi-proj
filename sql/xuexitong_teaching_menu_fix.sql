-- 学习通教学管理平台菜单修复脚本
-- 适用场景：
-- 1. 已执行 sql/ruoyi.sql、sql/xuexitong_teaching_schema.sql、sql/xuexitong_teaching_demo_init.sql
-- 2. 业务表和演示数据已导入，但后台左侧菜单没有“教学管理”
-- 3. 可重复执行，兼容 MySQL 5.7 / UTF-8（建议 utf8mb4）

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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

-- 可选核验 SQL：
-- SELECT menu_id, menu_name, path, menu_type, parent_id FROM sys_menu WHERE path = 'education' OR parent_id = (SELECT menu_id FROM sys_menu WHERE path = 'education' AND menu_type = 'M' LIMIT 1) ORDER BY parent_id, order_num;
-- SELECT r.role_key, m.menu_name, m.path FROM sys_role_menu rm JOIN sys_role r ON rm.role_id = r.role_id JOIN sys_menu m ON rm.menu_id = m.menu_id WHERE m.path = 'education' OR m.parent_id = (SELECT menu_id FROM sys_menu WHERE path = 'education' AND menu_type = 'M' LIMIT 1) ORDER BY r.role_key, m.order_num;
