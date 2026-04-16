const fs = require("fs");
const path = require("path");

const ROOT = path.resolve(__dirname, "..");
const SCHEMA_FILE = path.join(ROOT, "sql", "xuexitong_teaching_schema.sql");

const JAVA_DOMAIN_DIR = path.join(ROOT, "ruoyi-system", "src", "main", "java", "com", "ruoyi", "system", "domain", "education");
const JAVA_MAPPER_DIR = path.join(ROOT, "ruoyi-system", "src", "main", "java", "com", "ruoyi", "system", "mapper", "education");
const JAVA_SERVICE_DIR = path.join(ROOT, "ruoyi-system", "src", "main", "java", "com", "ruoyi", "system", "service", "education");
const JAVA_SERVICE_IMPL_DIR = path.join(ROOT, "ruoyi-system", "src", "main", "java", "com", "ruoyi", "system", "service", "impl", "education");
const XML_DIR = path.join(ROOT, "ruoyi-system", "src", "main", "resources", "mapper", "education");
const CONTROLLER_DIR = path.join(ROOT, "ruoyi-admin", "src", "main", "java", "com", "ruoyi", "web", "controller", "education");
const API_DIR = path.join(ROOT, "ruoyi-ui", "src", "api", "education");
const VIEW_DIR = path.join(ROOT, "ruoyi-ui", "src", "views", "education");

const BASE_ENTITY_COLUMNS = new Set(["create_by", "create_time", "update_by", "update_time", "remark"]);
const FORM_SKIP_COLUMNS = new Set(["createBy", "createTime", "updateBy", "updateTime"]);

const MODULES = {
  edu_teacher_profile: { class_name: "EduTeacherProfile", business_name: "教师档案", api_name: "teacher", query_fields: ["teacherNo", "teacherName", "status"], like_fields: ["teacherNo", "teacherName"], list_fields: ["teacherId", "teacherNo", "teacherName", "deptId", "jobTitle", "phone", "status", "createTime"], dict_fields: { status: "sys_normal_disable" }, textarea_fields: ["intro", "remark"], form_exclude_fields: ["delFlag"], upload_fields: {} },
  edu_student_profile: { class_name: "EduStudentProfile", business_name: "学生档案", api_name: "student", query_fields: ["studentNo", "studentName", "studyStatus", "status"], like_fields: ["studentNo", "studentName"], list_fields: ["studentId", "studentNo", "studentName", "deptId", "majorName", "className", "studyStatus", "status"], dict_fields: { studyStatus: "edu_study_status", status: "sys_normal_disable" }, textarea_fields: ["remark"], form_exclude_fields: ["delFlag"], upload_fields: {} },
  edu_term: { class_name: "EduTerm", business_name: "学期管理", api_name: "term", query_fields: ["termCode", "termName", "status"], like_fields: ["termCode", "termName"], list_fields: ["termId", "termCode", "termName", "schoolYear", "semesterNo", "startDate", "endDate", "status"], dict_fields: { status: "edu_term_status" }, textarea_fields: ["remark"], form_exclude_fields: [], upload_fields: {} },
  edu_course: { class_name: "EduCourse", business_name: "课程管理", api_name: "course", query_fields: ["courseCode", "courseName", "status"], like_fields: ["courseCode", "courseName"], list_fields: ["courseId", "courseCode", "courseName", "termId", "deptId", "credit", "totalHours", "selectedCount", "status"], dict_fields: { courseType: "edu_course_type", status: "edu_course_status" }, textarea_fields: ["intro", "completeRule", "remark"], form_exclude_fields: ["delFlag"], upload_fields: { courseCover: "image" } },
  edu_course_teacher: { class_name: "EduCourseTeacher", business_name: "授课安排", api_name: "courseTeacher", query_fields: ["courseId", "teacherUserId", "status"], like_fields: [], list_fields: ["courseTeacherId", "courseId", "teacherUserId", "teacherRole", "isOwner", "orderNum", "status"], dict_fields: { teacherRole: "edu_teacher_role", isOwner: "sys_yes_no", status: "sys_normal_disable" }, textarea_fields: ["remark"], form_exclude_fields: [], upload_fields: {} },
  edu_course_chapter: { class_name: "EduCourseChapter", business_name: "课程章节", api_name: "chapter", query_fields: ["courseId", "chapterTitle", "status"], like_fields: ["chapterTitle"], list_fields: ["chapterId", "courseId", "parentId", "chapterTitle", "chapterType", "orderNum", "durationMinutes", "status"], dict_fields: { status: "sys_normal_disable" }, textarea_fields: ["chapterDesc", "remark"], form_exclude_fields: [], upload_fields: {} },
  edu_course_resource: { class_name: "EduCourseResource", business_name: "学习资源", api_name: "resource", query_fields: ["courseId", "resourceTitle", "resourceType", "status"], like_fields: ["resourceTitle"], list_fields: ["resourceId", "courseId", "chapterId", "resourceTitle", "resourceType", "fileName", "downloadCount", "status"], dict_fields: { resourceType: "edu_resource_type", status: "sys_normal_disable" }, textarea_fields: ["remark"], form_exclude_fields: ["delFlag"], upload_fields: { fileUrl: "file" } },
  edu_course_enrollment: { class_name: "EduCourseEnrollment", business_name: "选课管理", api_name: "enrollment", query_fields: ["courseId", "studentUserId", "enrollStatus"], like_fields: [], list_fields: ["enrollmentId", "courseId", "termId", "studentUserId", "sourceType", "enrollStatus", "progressPercent", "lastStudyTime"], dict_fields: { sourceType: "edu_enroll_source_type", enrollStatus: "edu_enroll_status" }, textarea_fields: ["remark"], form_exclude_fields: [], upload_fields: {} },
  edu_learning_progress: { class_name: "EduLearningProgress", business_name: "学习进度", api_name: "progress", query_fields: ["courseId", "studentUserId", "completedFlag"], like_fields: [], list_fields: ["progressId", "courseId", "chapterId", "studentUserId", "learnMinutes", "progressPercent", "completedFlag", "lastStudyTime"], dict_fields: { completedFlag: "edu_progress_completed_flag" }, textarea_fields: ["remark"], form_exclude_fields: [], upload_fields: {} },
  edu_assignment: { class_name: "EduAssignment", business_name: "作业管理", api_name: "assignment", query_fields: ["courseId", "title", "publishStatus"], like_fields: ["title"], list_fields: ["assignmentId", "courseId", "title", "assignmentType", "totalScore", "submitLimit", "deadlineTime", "publishStatus"], dict_fields: { assignmentType: "edu_assignment_type", allowResubmit: "sys_yes_no", publishStatus: "edu_assignment_publish_status" }, textarea_fields: ["content", "remark"], form_exclude_fields: [], upload_fields: {} },
  edu_assignment_attachment: { class_name: "EduAssignmentAttachment", business_name: "作业附件", api_name: "assignmentAttachment", query_fields: ["assignmentId", "fileName"], like_fields: ["fileName"], list_fields: ["assignmentAttachmentId", "assignmentId", "fileName", "fileSuffix", "fileSize", "orderNum", "uploadUserId"], dict_fields: {}, textarea_fields: ["remark"], form_exclude_fields: [], upload_fields: { fileUrl: "file" } },
  edu_assignment_submission: { class_name: "EduAssignmentSubmission", business_name: "作业提交", api_name: "submission", query_fields: ["assignmentId", "studentUserId", "reviewStatus"], like_fields: [], list_fields: ["submissionId", "assignmentId", "courseId", "studentUserId", "submitRound", "reviewStatus", "score", "submitTime"], dict_fields: { lateFlag: "sys_yes_no", isLatest: "sys_yes_no", reviewStatus: "edu_review_status" }, textarea_fields: ["submitRemark", "teacherComment", "remark"], form_exclude_fields: [], upload_fields: {} },
  edu_submission_attachment: { class_name: "EduSubmissionAttachment", business_name: "提交附件", api_name: "submissionAttachment", query_fields: ["submissionId", "fileName"], like_fields: ["fileName"], list_fields: ["submissionAttachmentId", "submissionId", "fileName", "fileSuffix", "fileSize", "uploadUserId"], dict_fields: {}, textarea_fields: ["remark"], form_exclude_fields: [], upload_fields: { fileUrl: "file" } },
  edu_course_notice: { class_name: "EduCourseNotice", business_name: "课程公告", api_name: "notice", query_fields: ["courseId", "title", "status"], like_fields: ["title"], list_fields: ["noticeId", "courseId", "title", "noticeType", "topFlag", "status", "publishTime"], dict_fields: { noticeType: "edu_notice_type", topFlag: "sys_yes_no", status: "edu_notice_status" }, textarea_fields: ["content", "remark"], form_exclude_fields: [], upload_fields: {} },
  edu_user_message: { class_name: "EduUserMessage", business_name: "消息提醒", api_name: "message", query_fields: ["userId", "messageTitle", "readStatus"], like_fields: ["messageTitle"], list_fields: ["messageId", "userId", "bizType", "messageTitle", "readStatus", "sendTime", "expireTime"], dict_fields: { readStatus: "edu_message_read_status" }, textarea_fields: ["messageContent", "remark"], form_exclude_fields: [], upload_fields: {} }
};

function snakeToCamel(name, upper = false) {
  const parts = name.split("_");
  const built = parts.map((part, index) => (index === 0 && !upper ? part : part.charAt(0).toUpperCase() + part.slice(1)));
  return built.join("");
}

function upperFirst(text) {
  return text ? text.charAt(0).toUpperCase() + text.slice(1) : text;
}

function cleanLabel(comment) {
  for (const separator of ["（", "("]) {
    if (comment.includes(separator)) return comment.split(separator)[0].trim();
  }
  return comment.trim();
}

function javaType(sqlType) {
  const value = sqlType.toLowerCase();
  if (value.startsWith("bigint")) return "Long";
  if (value.startsWith("int")) return "Integer";
  if (value.startsWith("decimal")) return "BigDecimal";
  if (value.startsWith("date") || value.startsWith("datetime") || value.startsWith("timestamp")) return "Date";
  return "String";
}

function isString(column) {
  return column.java_type === "String";
}

function isDate(column) {
  return column.java_type === "Date";
}

function fieldProp(columnName) {
  return snakeToCamel(columnName, false);
}

function vueExpr(expr) {
  return "{{ " + expr + " }}";
}

function writeFile(filePath, content) {
  fs.mkdirSync(path.dirname(filePath), { recursive: true });
  fs.writeFileSync(filePath, content, "utf8");
}

function parseTables() {
  const text = fs.readFileSync(SCHEMA_FILE, "utf8");
  const tables = {};
  const regex = /CREATE TABLE\s+(\w+)\s*\(([\s\S]*?)\)\s*ENGINE=/g;
  let match;
  while ((match = regex.exec(text)) !== null) {
    const tableName = match[1];
    const body = match[2];
    const columns = [];
    for (const rawLine of body.split(/\r?\n/)) {
      const line = rawLine.trim().replace(/,$/, "");
      if (!line || /^(PRIMARY KEY|UNIQUE KEY|KEY |CONSTRAINT)/.test(line)) continue;
      const columnMatch = line.match(/^(\w+)\s+([a-zA-Z]+(?:\(\d+(?:,\d+)?\))?)(.*?)(?:COMMENT\s+'([^']*)')?$/);
      if (!columnMatch) continue;
      const [, name, sqlType, rest, comment] = columnMatch;
      const defaultMatch = rest.match(/DEFAULT\s+('([^']*)'|([^\s,]+))/i);
      const defaultValue = defaultMatch ? (defaultMatch[2] !== undefined ? defaultMatch[2] : defaultMatch[3]) : null;
      columns.push({
        column_name: name,
        property_name: fieldProp(name),
        sql_type: sqlType,
        java_type: javaType(sqlType),
        comment: comment || name,
        label: cleanLabel(comment || name),
        nullable: !rest.toUpperCase().includes("NOT NULL"),
        auto_increment: rest.toUpperCase().includes("AUTO_INCREMENT"),
        default: defaultValue,
        required: rest.toUpperCase().includes("NOT NULL") && !rest.toUpperCase().includes("AUTO_INCREMENT") && defaultValue === null
      });
    }
    tables[tableName] = columns;
  }
  return tables;
}

function controlType(column, module) {
  const prop = column.property_name;
  const uploadType = (module.upload_fields || {})[prop];
  if (uploadType === "image") return "imageUpload";
  if (uploadType === "file") return "fileUpload";
  if ((module.dict_fields || {})[prop]) return "select";
  if (isDate(column)) return prop.toLowerCase().includes("time") ? "datetime" : "date";
  if (column.sql_type.toLowerCase().startsWith("text") || (module.textarea_fields || []).includes(prop)) return "textarea";
  return "input";
}

function queryTest(column) {
  return isString(column) ? `${column.property_name} != null and ${column.property_name} != ''` : `${column.property_name} != null`;
}

function writeTest(column) {
  return `${column.property_name} != null`;
}

function xmlParameter(column) {
  return `#{${column.property_name}}`;
}

function jsDefault(column) {
  const value = column.default;
  if (value === null || String(value).toUpperCase() === "NULL" || String(value).toUpperCase() === "CURRENT_TIMESTAMP") return "null";
  if (["Integer", "Long", "BigDecimal"].includes(column.java_type)) return String(value);
  return `'${String(value).replace(/\\/g, "\\\\").replace(/'/g, "\\'")}'`;
}

function findColumn(columns, propName) {
  return columns.find((column) => column.property_name === propName);
}

function buildContext(tableName, module, columns) {
  const pkColumn = columns.find((column) => column.auto_increment) || columns[0];
  const annotatedColumns = columns.map((column) => ({ ...column, control_type: controlType(column, module) }));
  const queryColumns = (module.query_fields || []).map((prop) => findColumn(annotatedColumns, prop)).filter(Boolean);
  const listColumns = (module.list_fields || []).map((prop) => findColumn(annotatedColumns, prop)).filter(Boolean);
  if (!listColumns.some((column) => column.property_name === pkColumn.property_name)) listColumns.unshift(pkColumn);
  const excludeSet = new Set([...(module.form_exclude_fields || []), ...FORM_SKIP_COLUMNS, pkColumn.property_name]);
  const formColumns = annotatedColumns.filter((column) => !excludeSet.has(column.property_name));
  const dictTypes = Array.from(new Set(Object.values(module.dict_fields || {})));
  return {
    table_name: tableName,
    class_name: module.class_name,
    business_name: module.business_name,
    api_name: module.api_name,
    api_pascal: upperFirst(module.api_name),
    permission: `education:${module.api_name}:list`,
    pk_column: pkColumn,
    columns: annotatedColumns,
    query_columns: queryColumns,
    list_columns: listColumns,
    form_columns: formColumns,
    dict_fields: module.dict_fields || {},
    dict_types: dictTypes,
    like_fields: new Set(module.like_fields || [])
  };
}

function generateDomain(context) {
  const columns = context.columns.filter((column) => !BASE_ENTITY_COLUMNS.has(column.column_name));
  const imports = new Set(["com.ruoyi.common.annotation.Excel", "com.ruoyi.common.core.domain.BaseEntity", "org.apache.commons.lang3.builder.ToStringBuilder", "org.apache.commons.lang3.builder.ToStringStyle"]);
  if (columns.some(isDate)) {
    imports.add("com.fasterxml.jackson.annotation.JsonFormat");
    imports.add("java.util.Date");
  }
  if (columns.some((column) => column.java_type === "BigDecimal")) imports.add("java.math.BigDecimal");
  const lines = ["package com.ruoyi.system.domain.education;", ""];
  Array.from(imports).sort().forEach((item) => lines.push(`import ${item};`));
  lines.push("", "/**", ` * ${context.business_name}对象 ${context.table_name}`, " */", `public class ${context.class_name} extends BaseEntity`, "{", "    private static final long serialVersionUID = 1L;", "");
  columns.forEach((column) => {
    const excelArgs = [`name = "${column.label}"`];
    const dictType = context.dict_fields[column.property_name];
    if (dictType) excelArgs.push(`dictType = "${dictType}"`);
    lines.push(`    /** ${column.comment} */`);
    if (isDate(column)) {
      const pattern = column.property_name.toLowerCase().includes("time") ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd";
      lines.push(`    @JsonFormat(pattern = "${pattern}")`);
      excelArgs.push(`dateFormat = "${pattern}"`);
    }
    lines.push(`    @Excel(${excelArgs.join(", ")})`);
    lines.push(`    private ${column.java_type} ${column.property_name};`, "");
  });
  columns.forEach((column) => {
    const cap = upperFirst(column.property_name);
    lines.push(`    public void set${cap}(${column.java_type} ${column.property_name})`, "    {", `        this.${column.property_name} = ${column.property_name};`, "    }", "", `    public ${column.java_type} get${cap}()`, "    {", `        return ${column.property_name};`, "    }", "");
  });
  lines.push("    @Override", "    public String toString()", "    {", "        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)");
  columns.forEach((column) => lines.push(`            .append("${column.property_name}", get${upperFirst(column.property_name)}())`));
  lines.push('            .append("createBy", getCreateBy())', '            .append("createTime", getCreateTime())', '            .append("updateBy", getUpdateBy())', '            .append("updateTime", getUpdateTime())', '            .append("remark", getRemark())', "            .toString();", "    }", "}", "");
  return lines.join("\n");
}

function generateMapper(context) {
  const pk = context.pk_column;
  return ["package com.ruoyi.system.mapper.education;", "", "import java.util.List;", `import com.ruoyi.system.domain.education.${context.class_name};`, "", "/**", ` * ${context.business_name}Mapper接口`, " */", `public interface ${context.class_name}Mapper`, "{", `    public ${context.class_name} select${context.class_name}By${upperFirst(pk.property_name)}(${pk.java_type} ${pk.property_name});`, "", `    public List<${context.class_name}> select${context.class_name}List(${context.class_name} ${context.api_name});`, "", `    public int insert${context.class_name}(${context.class_name} ${context.api_name});`, "", `    public int update${context.class_name}(${context.class_name} ${context.api_name});`, "", `    public int delete${context.class_name}By${upperFirst(pk.property_name)}(${pk.java_type} ${pk.property_name});`, "", `    public int delete${context.class_name}By${upperFirst(pk.property_name)}s(${pk.java_type}[] ${pk.property_name}s);`, "}", ""].join("\n");
}

function generateService(context) {
  const pk = context.pk_column;
  return ["package com.ruoyi.system.service.education;", "", "import java.util.List;", `import com.ruoyi.system.domain.education.${context.class_name};`, "", "/**", ` * ${context.business_name}Service接口`, " */", `public interface I${context.class_name}Service`, "{", `    public ${context.class_name} select${context.class_name}By${upperFirst(pk.property_name)}(${pk.java_type} ${pk.property_name});`, "", `    public List<${context.class_name}> select${context.class_name}List(${context.class_name} ${context.api_name});`, "", `    public int insert${context.class_name}(${context.class_name} ${context.api_name});`, "", `    public int update${context.class_name}(${context.class_name} ${context.api_name});`, "", `    public int delete${context.class_name}By${upperFirst(pk.property_name)}s(${pk.java_type}[] ${pk.property_name}s);`, "", `    public int delete${context.class_name}By${upperFirst(pk.property_name)}(${pk.java_type} ${pk.property_name});`, "}", ""].join("\n");
}

function generateServiceImpl(context) {
  const pk = context.pk_column;
  const mapperName = `${context.class_name}Mapper`;
  return ["package com.ruoyi.system.service.impl.education;", "", "import java.util.List;", "import org.springframework.beans.factory.annotation.Autowired;", "import org.springframework.stereotype.Service;", "import com.ruoyi.common.utils.DateUtils;", `import com.ruoyi.system.domain.education.${context.class_name};`, `import com.ruoyi.system.mapper.education.${mapperName};`, `import com.ruoyi.system.service.education.I${context.class_name}Service;`, "", "/**", ` * ${context.business_name}Service业务层处理`, " */", "@Service", `public class ${context.class_name}ServiceImpl implements I${context.class_name}Service`, "{", "    @Autowired", `    private ${mapperName} ${context.api_name}Mapper;`, "", "    @Override", `    public ${context.class_name} select${context.class_name}By${upperFirst(pk.property_name)}(${pk.java_type} ${pk.property_name})`, "    {", `        return ${context.api_name}Mapper.select${context.class_name}By${upperFirst(pk.property_name)}(${pk.property_name});`, "    }", "", "    @Override", `    public List<${context.class_name}> select${context.class_name}List(${context.class_name} ${context.api_name})`, "    {", `        return ${context.api_name}Mapper.select${context.class_name}List(${context.api_name});`, "    }", "", "    @Override", `    public int insert${context.class_name}(${context.class_name} ${context.api_name})`, "    {", `        ${context.api_name}.setCreateTime(DateUtils.getNowDate());`, `        return ${context.api_name}Mapper.insert${context.class_name}(${context.api_name});`, "    }", "", "    @Override", `    public int update${context.class_name}(${context.class_name} ${context.api_name})`, "    {", `        ${context.api_name}.setUpdateTime(DateUtils.getNowDate());`, `        return ${context.api_name}Mapper.update${context.class_name}(${context.api_name});`, "    }", "", "    @Override", `    public int delete${context.class_name}By${upperFirst(pk.property_name)}s(${pk.java_type}[] ${pk.property_name}s)`, "    {", `        return ${context.api_name}Mapper.delete${context.class_name}By${upperFirst(pk.property_name)}s(${pk.property_name}s);`, "    }", "", "    @Override", `    public int delete${context.class_name}By${upperFirst(pk.property_name)}(${pk.java_type} ${pk.property_name})`, "    {", `        return ${context.api_name}Mapper.delete${context.class_name}By${upperFirst(pk.property_name)}(${pk.property_name});`, "    }", "}", ""].join("\n");
}

function generateController(context) {
  const pk = context.pk_column;
  return ["package com.ruoyi.web.controller.education;", "", "import java.util.List;", "import javax.servlet.http.HttpServletResponse;", "import org.springframework.beans.factory.annotation.Autowired;", "import org.springframework.security.access.prepost.PreAuthorize;", "import org.springframework.web.bind.annotation.DeleteMapping;", "import org.springframework.web.bind.annotation.GetMapping;", "import org.springframework.web.bind.annotation.PathVariable;", "import org.springframework.web.bind.annotation.PostMapping;", "import org.springframework.web.bind.annotation.PutMapping;", "import org.springframework.web.bind.annotation.RequestBody;", "import org.springframework.web.bind.annotation.RequestMapping;", "import org.springframework.web.bind.annotation.RestController;", "import com.ruoyi.common.annotation.Log;", "import com.ruoyi.common.core.controller.BaseController;", "import com.ruoyi.common.core.domain.AjaxResult;", "import com.ruoyi.common.core.page.TableDataInfo;", "import com.ruoyi.common.enums.BusinessType;", "import com.ruoyi.common.utils.poi.ExcelUtil;", `import com.ruoyi.system.domain.education.${context.class_name};`, `import com.ruoyi.system.service.education.I${context.class_name}Service;`, "", "/**", ` * ${context.business_name}Controller`, " */", "@RestController", `@RequestMapping("/education/${context.api_name}")`, `public class ${context.class_name}Controller extends BaseController`, "{", "    @Autowired", `    private I${context.class_name}Service ${context.api_name}Service;`, "", `    @PreAuthorize("@ss.hasPermi('${context.permission}')")`, '    @GetMapping("/list")', `    public TableDataInfo list(${context.class_name} ${context.api_name})`, "    {", "        startPage();", `        List<${context.class_name}> list = ${context.api_name}Service.select${context.class_name}List(${context.api_name});`, "        return getDataTable(list);", "    }", "", `    @PreAuthorize("@ss.hasPermi('${context.permission}')")`, `    @Log(title = "${context.business_name}", businessType = BusinessType.EXPORT)`, '    @PostMapping("/export")', `    public void export(HttpServletResponse response, ${context.class_name} ${context.api_name})`, "    {", `        List<${context.class_name}> list = ${context.api_name}Service.select${context.class_name}List(${context.api_name});`, `        ExcelUtil<${context.class_name}> util = new ExcelUtil<${context.class_name}>(${context.class_name}.class);`, `        util.exportExcel(response, list, "${context.business_name}数据");`, "    }", "", `    @PreAuthorize("@ss.hasPermi('${context.permission}')")`, `    @GetMapping(value = "/{${pk.property_name}}")`, `    public AjaxResult getInfo(@PathVariable ${pk.java_type} ${pk.property_name})`, "    {", `        return success(${context.api_name}Service.select${context.class_name}By${upperFirst(pk.property_name)}(${pk.property_name}));`, "    }", "", `    @PreAuthorize("@ss.hasPermi('${context.permission}')")`, `    @Log(title = "${context.business_name}", businessType = BusinessType.INSERT)`, "    @PostMapping", `    public AjaxResult add(@RequestBody ${context.class_name} ${context.api_name})`, "    {", `        ${context.api_name}.setCreateBy(getUsername());`, `        return toAjax(${context.api_name}Service.insert${context.class_name}(${context.api_name}));`, "    }", "", `    @PreAuthorize("@ss.hasPermi('${context.permission}')")`, `    @Log(title = "${context.business_name}", businessType = BusinessType.UPDATE)`, "    @PutMapping", `    public AjaxResult edit(@RequestBody ${context.class_name} ${context.api_name})`, "    {", `        ${context.api_name}.setUpdateBy(getUsername());`, `        return toAjax(${context.api_name}Service.update${context.class_name}(${context.api_name}));`, "    }", "", `    @PreAuthorize("@ss.hasPermi('${context.permission}')")`, `    @Log(title = "${context.business_name}", businessType = BusinessType.DELETE)`, `    @DeleteMapping("/{${pk.property_name}s}")`, `    public AjaxResult remove(@PathVariable ${pk.java_type}[] ${pk.property_name}s)`, "    {", `        return toAjax(${context.api_name}Service.delete${context.class_name}By${upperFirst(pk.property_name)}s(${pk.property_name}s));`, "    }", "}", ""].join("\n");
}

function generateXml(context) {
  const pk = context.pk_column;
  const hasDelFlag = context.columns.some((column) => column.column_name === "del_flag");
  const lines = ['<?xml version="1.0" encoding="UTF-8" ?>', "<!DOCTYPE mapper", 'PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"', '"http://mybatis.org/dtd/mybatis-3-mapper.dtd">', `<mapper namespace="com.ruoyi.system.mapper.education.${context.class_name}Mapper">`, "", `    <resultMap type="com.ruoyi.system.domain.education.${context.class_name}" id="${context.class_name}Result">`];
  context.columns.forEach((column) => lines.push(`        <${column.column_name === pk.column_name ? "id" : "result"} property="${column.property_name}" column="${column.column_name}" />`));
  lines.push("    </resultMap>", "", `    <sql id="select${context.class_name}Vo">`, "        select");
  context.columns.forEach((column, index) => lines.push(`            t.${column.column_name}${index < context.columns.length - 1 ? "," : ""}`));
  lines.push(`        from ${context.table_name} t`, "    </sql>", "", `    <select id="select${context.class_name}List" parameterType="com.ruoyi.system.domain.education.${context.class_name}" resultMap="${context.class_name}Result">`, `        <include refid="select${context.class_name}Vo"/>`, "        <where>");
  if (hasDelFlag) lines.push("            and (t.del_flag is null or t.del_flag = '0')");
  context.query_columns.forEach((column) => lines.push(`            <if test="${queryTest(column)}"> and t.${column.column_name} ${context.like_fields.has(column.property_name) ? `like concat('%', ${xmlParameter(column)}, '%')` : `= ${xmlParameter(column)}`}</if>`));
  lines.push("        </where>", `        order by t.${pk.column_name} desc`, "    </select>", "", `    <select id="select${context.class_name}By${upperFirst(pk.property_name)}" parameterType="${pk.java_type}" resultMap="${context.class_name}Result">`, `        <include refid="select${context.class_name}Vo"/>`, `        where t.${pk.column_name} = #{${pk.property_name}}`);
  if (hasDelFlag) lines.push("          and (t.del_flag is null or t.del_flag = '0')");
  lines.push("    </select>", "", `    <insert id="insert${context.class_name}" parameterType="com.ruoyi.system.domain.education.${context.class_name}" useGeneratedKeys="true" keyProperty="${pk.property_name}">`, `        insert into ${context.table_name}`, '        <trim prefix="(" suffix=")" suffixOverrides=",">');
  context.columns.forEach((column) => { if (!(column.column_name === pk.column_name && pk.auto_increment)) lines.push(`            <if test="${writeTest(column)}">${column.column_name},</if>`); });
  lines.push("        </trim>", '        <trim prefix="values (" suffix=")" suffixOverrides=",">');
  context.columns.forEach((column) => { if (!(column.column_name === pk.column_name && pk.auto_increment)) lines.push(`            <if test="${writeTest(column)}">${xmlParameter(column)},</if>`); });
  lines.push("        </trim>", "    </insert>", "", `    <update id="update${context.class_name}" parameterType="com.ruoyi.system.domain.education.${context.class_name}">`, `        update ${context.table_name}`, '        <trim prefix="SET" suffixOverrides=",">');
  context.columns.forEach((column) => { if (column.column_name !== pk.column_name) lines.push(`            <if test="${writeTest(column)}">${column.column_name} = ${xmlParameter(column)},</if>`); });
  lines.push("        </trim>", `        where ${pk.column_name} = #{${pk.property_name}}`, "    </update>", "", `    <delete id="delete${context.class_name}By${upperFirst(pk.property_name)}" parameterType="${pk.java_type}">`, `        delete from ${context.table_name} where ${pk.column_name} = #{${pk.property_name}}`, "    </delete>", "", `    <delete id="delete${context.class_name}By${upperFirst(pk.property_name)}s" parameterType="long">`, `        delete from ${context.table_name} where ${pk.column_name} in`, `        <foreach item="${pk.property_name}" collection="array" open="(" separator="," close=")">`, `            #{${pk.property_name}}`, "        </foreach>", "    </delete>", "</mapper>", "");
  return lines.join("\n");
}

function renderQueryItem(column, dictFields) {
  const lines = [`      <el-form-item label="${column.label}" prop="${column.property_name}">`];
  if (column.control_type === "select") {
    lines.push(`        <el-select v-model="queryParams.${column.property_name}" placeholder="请选择${column.label}" clearable style="width: 220px">`, `          <el-option v-for="dict in dict.type.${dictFields[column.property_name]}" :key="dict.value" :label="dict.label" :value="dict.value" />`, "        </el-select>");
  } else if (column.control_type === "date" || column.control_type === "datetime") {
    const pickerType = column.control_type === "datetime" ? "datetime" : "date";
    const valueFormat = column.control_type === "datetime" ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd";
    lines.push(`        <el-date-picker v-model="queryParams.${column.property_name}" type="${pickerType}" value-format="${valueFormat}" placeholder="请选择${column.label}" clearable style="width: 220px" />`);
  } else {
    lines.push(`        <el-input v-model="queryParams.${column.property_name}" placeholder="请输入${column.label}" clearable style="width: 220px" @keyup.enter.native="handleQuery" />`);
  }
  lines.push("      </el-form-item>");
  return lines;
}

function renderTableColumn(column, dictFields) {
  if (column.control_type === "select") return [`      <el-table-column label="${column.label}" align="center" prop="${column.property_name}">`, '        <template slot-scope="scope">', `          <dict-tag :options="dict.type.${dictFields[column.property_name]}" :value="scope.row.${column.property_name}" />`, "        </template>", "      </el-table-column>"];
  if (column.control_type === "imageUpload") return [`      <el-table-column label="${column.label}" align="center" prop="${column.property_name}" width="100">`, '        <template slot-scope="scope">', `          <image-preview :src="scope.row.${column.property_name}" :width="50" :height="50" />`, "        </template>", "      </el-table-column>"];
  if (column.control_type === "date" || column.control_type === "datetime") return [`      <el-table-column label="${column.label}" align="center" prop="${column.property_name}" width="180">`, '        <template slot-scope="scope">', `          <span>${vueExpr(`parseTime(scope.row.${column.property_name})`)}</span>`, "        </template>", "      </el-table-column>"];
  return [`      <el-table-column label="${column.label}" align="center" prop="${column.property_name}" :show-overflow-tooltip="true" />`];
}

function renderFormItem(column, dictFields) {
  const lines = [`        <el-form-item label="${column.label}" prop="${column.property_name}">`];
  if (column.control_type === "select") lines.push(`          <el-select v-model="form.${column.property_name}" placeholder="请选择${column.label}" clearable style="width: 100%">`, `            <el-option v-for="dict in dict.type.${dictFields[column.property_name]}" :key="dict.value" :label="dict.label" :value="dict.value" />`, "          </el-select>");
  else if (column.control_type === "textarea") lines.push(`          <el-input v-model="form.${column.property_name}" type="textarea" :rows="3" placeholder="请输入${column.label}" />`);
  else if (column.control_type === "imageUpload") lines.push(`          <image-upload v-model="form.${column.property_name}" />`);
  else if (column.control_type === "fileUpload") lines.push(`          <file-upload v-model="form.${column.property_name}" />`);
  else if (column.control_type === "date" || column.control_type === "datetime") lines.push(`          <el-date-picker v-model="form.${column.property_name}" type="${column.control_type === "datetime" ? "datetime" : "date"}" value-format="${column.control_type === "datetime" ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd"}" placeholder="请选择${column.label}" clearable style="width: 100%" />`);
  else lines.push(`          <el-input v-model="form.${column.property_name}" placeholder="请输入${column.label}" />`);
  lines.push("        </el-form-item>");
  return lines;
}

function generateApi(context) {
  const pk = context.pk_column.property_name;
  return ["import request from '@/utils/request'", "", `export function list${context.api_pascal}(query) {`, "  return request({", `    url: '/education/${context.api_name}/list',`, "    method: 'get',", "    params: query", "  })", "}", "", `export function get${context.api_pascal}(${pk}) {`, "  return request({", `    url: '/education/${context.api_name}/' + ${pk},`, "    method: 'get'", "  })", "}", "", `export function add${context.api_pascal}(data) {`, "  return request({", `    url: '/education/${context.api_name}',`, "    method: 'post',", "    data: data", "  })", "}", "", `export function update${context.api_pascal}(data) {`, "  return request({", `    url: '/education/${context.api_name}',`, "    method: 'put',", "    data: data", "  })", "}", "", `export function del${context.api_pascal}(${pk}) {`, "  return request({", `    url: '/education/${context.api_name}/' + ${pk},`, "    method: 'delete'", "  })", "}", ""].join("\n");
}

function generateView(context) {
  const pk = context.pk_column.property_name;
  const listName = `${context.api_name}List`;
  const lines = ["<template>", '  <div class="app-container">', '    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px" v-show="showSearch">'];
  context.query_columns.forEach((column) => lines.push(...renderQueryItem(column, context.dict_fields)));
  lines.push("      <el-form-item>", '        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>', '        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>', "      </el-form-item>", "    </el-form>", "", '    <el-row :gutter="10" class="mb8">', '      <el-col :span="1.5">', `        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['${context.permission}']">新增</el-button>`, "      </el-col>", '      <el-col :span="1.5">', `        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['${context.permission}']">修改</el-button>`, "      </el-col>", '      <el-col :span="1.5">', `        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['${context.permission}']">删除</el-button>`, "      </el-col>", '      <el-col :span="1.5">', `        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['${context.permission}']">导出</el-button>`, "      </el-col>", '      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>', "    </el-row>", "", `    <el-table v-loading="loading" :data="${listName}" @selection-change="handleSelectionChange">`, '      <el-table-column type="selection" width="55" align="center" />');
  context.list_columns.forEach((column) => lines.push(...renderTableColumn(column, context.dict_fields)));
  lines.push('      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">', '        <template slot-scope="scope">', `          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['${context.permission}']">修改</el-button>`, `          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['${context.permission}']">删除</el-button>`, "        </template>", "      </el-table-column>", "    </el-table>", "", '    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />', "", '    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>', '      <el-form ref="form" :model="form" :rules="rules" label-width="100px">');
  context.form_columns.forEach((column) => lines.push(...renderFormItem(column, context.dict_fields)));
  lines.push("      </el-form>", '      <div slot="footer" class="dialog-footer">', '        <el-button type="primary" @click="submitForm">确 定</el-button>', '        <el-button @click="cancel">取 消</el-button>', "      </div>", "    </el-dialog>", "  </div>", "</template>", "", "<script>", `import { list${context.api_pascal}, get${context.api_pascal}, del${context.api_pascal}, add${context.api_pascal}, update${context.api_pascal} } from '@/api/education/${context.api_name}'`, "", "export default {", `  name: '${context.api_pascal}',`);
  if (context.dict_types.length) lines.push(`  dicts: [${context.dict_types.map((item) => `'${item}'`).join(", ")}],`);
  lines.push("  data() {", "    return {", "      loading: true,", "      ids: [],", "      single: true,", "      multiple: true,", "      showSearch: true,", "      total: 0,", `      ${listName}: [],`, "      title: '',", "      open: false,", "      queryParams: {", "        pageNum: 1,", "        pageSize: 10,");
  context.query_columns.forEach((column, index) => lines.push(`        ${column.property_name}: null${index < context.query_columns.length - 1 ? "," : ""}`));
  lines.push("      },", "      form: {},", "      rules: {");
  const requiredColumns = context.form_columns.filter((column) => column.required);
  requiredColumns.forEach((column, index) => lines.push(`        ${column.property_name}: [`, `          { required: true, message: '请输入${column.label}', trigger: '${["select", "date", "datetime", "imageUpload", "fileUpload"].includes(column.control_type) ? "change" : "blur"}' }`, `        ]${index < requiredColumns.length - 1 ? "," : ""}`));
  lines.push("      }", "    }", "  },", "  created() {", "    this.getList()", "  },", "  methods: {", "    getList() {", "      this.loading = true", `      list${context.api_pascal}(this.queryParams).then(response => {`, `        this.${listName} = response.rows`, "        this.total = response.total", "        this.loading = false", "      }).catch(() => {", "        this.loading = false", "      })", "    },", "    cancel() {", "      this.open = false", "      this.reset()", "    },", "    reset() {", "      this.form = {", `        ${pk}: null,`);
  context.form_columns.forEach((column, index) => lines.push(`        ${column.property_name}: ${jsDefault(column)}${index < context.form_columns.length - 1 ? "," : ""}`));
  lines.push("      }", "      this.resetForm('form')", "    },", "    handleQuery() {", "      this.queryParams.pageNum = 1", "      this.getList()", "    },", "    resetQuery() {", "      this.resetForm('queryForm')", "      this.handleQuery()", "    },", "    handleSelectionChange(selection) {", `      this.ids = selection.map(item => item.${pk})`, "      this.single = selection.length !== 1", "      this.multiple = selection.length === 0", "    },", "    handleAdd() {", "      this.reset()", "      this.open = true", `      this.title = '新增${context.business_name}'`, "    },", "    handleUpdate(row) {", "      this.reset()", `      const id = row.${pk} || this.ids[0]`, `      get${context.api_pascal}(id).then(response => {`, "        this.form = response.data", "        this.open = true", `        this.title = '修改${context.business_name}'`, "      })", "    },", "    submitForm() {", "      this.$refs.form.validate(valid => {", "        if (!valid) return", `        if (this.form.${pk} != null) {`, `          update${context.api_pascal}(this.form).then(() => {`, "            this.$modal.msgSuccess('修改成功')", "            this.open = false", "            this.getList()", "          })", "        } else {", `          add${context.api_pascal}(this.form).then(() => {`, "            this.$modal.msgSuccess('新增成功')", "            this.open = false", "            this.getList()", "          })", "        }", "      })", "    },", "    handleDelete(row) {", `      const ids = row.${pk} || this.ids`, `      this.$modal.confirm('是否确认删除${context.business_name}编号为\"' + ids + '\"的数据项？').then(() => {`, `        return del${context.api_pascal}(ids)`, "      }).then(() => {", "        this.getList()", "        this.$modal.msgSuccess('删除成功')", "      }).catch(() => {})", "    },", "    handleExport() {", `      this.download('education/${context.api_name}/export', {`, "        ...this.queryParams", `      }, '${context.api_name}_' + new Date().getTime() + '.xlsx')`, "    }", "  }", "}", "</script>", "");
  return lines.join("\n");
}

function main() {
  const tables = parseTables();
  Object.entries(MODULES).forEach(([tableName, module]) => {
    const context = buildContext(tableName, module, tables[tableName]);
    writeFile(path.join(JAVA_DOMAIN_DIR, `${context.class_name}.java`), generateDomain(context));
    writeFile(path.join(JAVA_MAPPER_DIR, `${context.class_name}Mapper.java`), generateMapper(context));
    writeFile(path.join(JAVA_SERVICE_DIR, `I${context.class_name}Service.java`), generateService(context));
    writeFile(path.join(JAVA_SERVICE_IMPL_DIR, `${context.class_name}ServiceImpl.java`), generateServiceImpl(context));
    writeFile(path.join(CONTROLLER_DIR, `${context.class_name}Controller.java`), generateController(context));
    writeFile(path.join(XML_DIR, `${context.class_name}Mapper.xml`), generateXml(context));
    writeFile(path.join(API_DIR, `${context.api_name}.js`), generateApi(context));
    writeFile(path.join(VIEW_DIR, context.api_name, "index.vue"), generateView(context));
  });
  console.log(`generated ${Object.keys(MODULES).length} education modules`);
}

main();
