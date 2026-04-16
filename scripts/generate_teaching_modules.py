from pathlib import Path
import re


ROOT = Path(__file__).resolve().parents[1]
SCHEMA_FILE = ROOT / "sql" / "xuexitong_teaching_schema.sql"

JAVA_DOMAIN_DIR = ROOT / "ruoyi-system" / "src" / "main" / "java" / "com" / "ruoyi" / "system" / "domain" / "education"
JAVA_MAPPER_DIR = ROOT / "ruoyi-system" / "src" / "main" / "java" / "com" / "ruoyi" / "system" / "mapper" / "education"
JAVA_SERVICE_DIR = ROOT / "ruoyi-system" / "src" / "main" / "java" / "com" / "ruoyi" / "system" / "service" / "education"
JAVA_SERVICE_IMPL_DIR = ROOT / "ruoyi-system" / "src" / "main" / "java" / "com" / "ruoyi" / "system" / "service" / "impl" / "education"
XML_DIR = ROOT / "ruoyi-system" / "src" / "main" / "resources" / "mapper" / "education"
CONTROLLER_DIR = ROOT / "ruoyi-admin" / "src" / "main" / "java" / "com" / "ruoyi" / "web" / "controller" / "education"
API_DIR = ROOT / "ruoyi-ui" / "src" / "api" / "education"
VIEW_DIR = ROOT / "ruoyi-ui" / "src" / "views" / "education"

BASE_ENTITY_COLUMNS = {"create_by", "create_time", "update_by", "update_time", "remark"}
FORM_SKIP_COLUMNS = {"createBy", "createTime", "updateBy", "updateTime"}

MODULES = {
    "edu_teacher_profile": {"class_name": "EduTeacherProfile", "business_name": "教师档案", "api_name": "teacher", "query_fields": ["teacherNo", "teacherName", "status"], "like_fields": {"teacherNo", "teacherName"}, "list_fields": ["teacherId", "teacherNo", "teacherName", "deptId", "jobTitle", "phone", "status", "createTime"], "dict_fields": {"status": "sys_normal_disable"}, "textarea_fields": {"intro", "remark"}, "form_exclude_fields": {"delFlag"}, "upload_fields": {}},
    "edu_student_profile": {"class_name": "EduStudentProfile", "business_name": "学生档案", "api_name": "student", "query_fields": ["studentNo", "studentName", "studyStatus", "status"], "like_fields": {"studentNo", "studentName"}, "list_fields": ["studentId", "studentNo", "studentName", "deptId", "majorName", "className", "studyStatus", "status"], "dict_fields": {"studyStatus": "edu_study_status", "status": "sys_normal_disable"}, "textarea_fields": {"remark"}, "form_exclude_fields": {"delFlag"}, "upload_fields": {}},
    "edu_term": {"class_name": "EduTerm", "business_name": "学期管理", "api_name": "term", "query_fields": ["termCode", "termName", "status"], "like_fields": {"termCode", "termName"}, "list_fields": ["termId", "termCode", "termName", "schoolYear", "semesterNo", "startDate", "endDate", "status"], "dict_fields": {"status": "edu_term_status"}, "textarea_fields": {"remark"}, "form_exclude_fields": set(), "upload_fields": {}},
    "edu_course": {"class_name": "EduCourse", "business_name": "课程管理", "api_name": "course", "query_fields": ["courseCode", "courseName", "status"], "like_fields": {"courseCode", "courseName"}, "list_fields": ["courseId", "courseCode", "courseName", "termId", "deptId", "credit", "totalHours", "selectedCount", "status"], "dict_fields": {"courseType": "edu_course_type", "status": "edu_course_status"}, "textarea_fields": {"intro", "completeRule", "remark"}, "form_exclude_fields": {"delFlag"}, "upload_fields": {"courseCover": "image"}},
    "edu_course_teacher": {"class_name": "EduCourseTeacher", "business_name": "授课安排", "api_name": "courseTeacher", "query_fields": ["courseId", "teacherUserId", "status"], "like_fields": set(), "list_fields": ["courseTeacherId", "courseId", "teacherUserId", "teacherRole", "isOwner", "orderNum", "status"], "dict_fields": {"teacherRole": "edu_teacher_role", "isOwner": "sys_yes_no", "status": "sys_normal_disable"}, "textarea_fields": {"remark"}, "form_exclude_fields": set(), "upload_fields": {}},
    "edu_course_chapter": {"class_name": "EduCourseChapter", "business_name": "课程章节", "api_name": "chapter", "query_fields": ["courseId", "chapterTitle", "status"], "like_fields": {"chapterTitle"}, "list_fields": ["chapterId", "courseId", "parentId", "chapterTitle", "chapterType", "orderNum", "durationMinutes", "status"], "dict_fields": {"status": "sys_normal_disable"}, "textarea_fields": {"chapterDesc", "remark"}, "form_exclude_fields": set(), "upload_fields": {}},
    "edu_course_resource": {"class_name": "EduCourseResource", "business_name": "学习资源", "api_name": "resource", "query_fields": ["courseId", "resourceTitle", "resourceType", "status"], "like_fields": {"resourceTitle"}, "list_fields": ["resourceId", "courseId", "chapterId", "resourceTitle", "resourceType", "fileName", "downloadCount", "status"], "dict_fields": {"resourceType": "edu_resource_type", "status": "sys_normal_disable"}, "textarea_fields": {"remark"}, "form_exclude_fields": {"delFlag"}, "upload_fields": {"fileUrl": "file"}},
    "edu_course_enrollment": {"class_name": "EduCourseEnrollment", "business_name": "选课管理", "api_name": "enrollment", "query_fields": ["courseId", "studentUserId", "enrollStatus"], "like_fields": set(), "list_fields": ["enrollmentId", "courseId", "termId", "studentUserId", "sourceType", "enrollStatus", "progressPercent", "lastStudyTime"], "dict_fields": {"sourceType": "edu_enroll_source_type", "enrollStatus": "edu_enroll_status"}, "textarea_fields": {"remark"}, "form_exclude_fields": set(), "upload_fields": {}},
    "edu_learning_progress": {"class_name": "EduLearningProgress", "business_name": "学习进度", "api_name": "progress", "query_fields": ["courseId", "studentUserId", "completedFlag"], "like_fields": set(), "list_fields": ["progressId", "courseId", "chapterId", "studentUserId", "learnMinutes", "progressPercent", "completedFlag", "lastStudyTime"], "dict_fields": {"completedFlag": "edu_progress_completed_flag"}, "textarea_fields": {"remark"}, "form_exclude_fields": set(), "upload_fields": {}},
    "edu_assignment": {"class_name": "EduAssignment", "business_name": "作业管理", "api_name": "assignment", "query_fields": ["courseId", "title", "publishStatus"], "like_fields": {"title"}, "list_fields": ["assignmentId", "courseId", "title", "assignmentType", "totalScore", "submitLimit", "deadlineTime", "publishStatus"], "dict_fields": {"assignmentType": "edu_assignment_type", "allowResubmit": "sys_yes_no", "publishStatus": "edu_assignment_publish_status"}, "textarea_fields": {"content", "remark"}, "form_exclude_fields": set(), "upload_fields": {}},
    "edu_assignment_attachment": {"class_name": "EduAssignmentAttachment", "business_name": "作业附件", "api_name": "assignmentAttachment", "query_fields": ["assignmentId", "fileName"], "like_fields": {"fileName"}, "list_fields": ["assignmentAttachmentId", "assignmentId", "fileName", "fileSuffix", "fileSize", "orderNum", "uploadUserId"], "dict_fields": {}, "textarea_fields": {"remark"}, "form_exclude_fields": set(), "upload_fields": {"fileUrl": "file"}},
    "edu_assignment_submission": {"class_name": "EduAssignmentSubmission", "business_name": "作业提交", "api_name": "submission", "query_fields": ["assignmentId", "studentUserId", "reviewStatus"], "like_fields": set(), "list_fields": ["submissionId", "assignmentId", "courseId", "studentUserId", "submitRound", "reviewStatus", "score", "submitTime"], "dict_fields": {"lateFlag": "sys_yes_no", "isLatest": "sys_yes_no", "reviewStatus": "edu_review_status"}, "textarea_fields": {"submitRemark", "teacherComment", "remark"}, "form_exclude_fields": set(), "upload_fields": {}},
    "edu_submission_attachment": {"class_name": "EduSubmissionAttachment", "business_name": "提交附件", "api_name": "submissionAttachment", "query_fields": ["submissionId", "fileName"], "like_fields": {"fileName"}, "list_fields": ["submissionAttachmentId", "submissionId", "fileName", "fileSuffix", "fileSize", "uploadUserId"], "dict_fields": {}, "textarea_fields": {"remark"}, "form_exclude_fields": set(), "upload_fields": {"fileUrl": "file"}},
    "edu_course_notice": {"class_name": "EduCourseNotice", "business_name": "课程公告", "api_name": "notice", "query_fields": ["courseId", "title", "status"], "like_fields": {"title"}, "list_fields": ["noticeId", "courseId", "title", "noticeType", "topFlag", "status", "publishTime"], "dict_fields": {"noticeType": "edu_notice_type", "topFlag": "sys_yes_no", "status": "edu_notice_status"}, "textarea_fields": {"content", "remark"}, "form_exclude_fields": set(), "upload_fields": {}},
    "edu_user_message": {"class_name": "EduUserMessage", "business_name": "消息提醒", "api_name": "message", "query_fields": ["userId", "messageTitle", "readStatus"], "like_fields": {"messageTitle"}, "list_fields": ["messageId", "userId", "bizType", "messageTitle", "readStatus", "sendTime", "expireTime"], "dict_fields": {"readStatus": "edu_message_read_status"}, "textarea_fields": {"messageContent", "remark"}, "form_exclude_fields": set(), "upload_fields": {}},
}


def snake_to_camel(name, upper_first=False):
    parts = name.split("_")
    if upper_first:
        return "".join(part[:1].upper() + part[1:] for part in parts)
    return parts[0] + "".join(part[:1].upper() + part[1:] for part in parts[1:])


def upper_first(text):
    return text[:1].upper() + text[1:] if text else text


def clean_label(comment):
    for separator in ("（", "("):
        if separator in comment:
            return comment.split(separator, 1)[0].strip()
    return comment.strip()


def java_type(sql_type):
    sql_type = sql_type.lower()
    if sql_type.startswith("bigint"):
        return "Long"
    if sql_type.startswith("int"):
        return "Integer"
    if sql_type.startswith("decimal"):
        return "BigDecimal"
    if sql_type.startswith(("date", "datetime", "timestamp")):
        return "Date"
    return "String"


def is_string(column):
    return column["java_type"] == "String"


def is_date(column):
    return column["java_type"] == "Date"


def field_prop(column_name):
    return snake_to_camel(column_name, upper_first=False)


def vue_expr(expr):
    return "{{ " + expr + " }}"


def parse_tables():
    text = SCHEMA_FILE.read_text(encoding="utf-8")
    tables = {}
    pattern = re.compile(r"CREATE TABLE\s+(\w+)\s*\((.*?)\)\s*ENGINE=", re.S)
    for table_name, body in pattern.findall(text):
        columns = []
        for raw_line in body.splitlines():
            line = raw_line.strip().rstrip(",")
            if not line or line.startswith(("PRIMARY KEY", "UNIQUE KEY", "KEY ", "CONSTRAINT")):
                continue
            match = re.match(r"(\w+)\s+([a-zA-Z]+(?:\(\d+(?:,\d+)?\))?)(.*?)(?:COMMENT\s+'([^']*)')?$", line)
            if not match:
                continue
            name, sql_type, rest, comment = match.groups()
            default_value = None
            default_match = re.search(r"DEFAULT\s+('([^']*)'|([^\s,]+))", rest, flags=re.I)
            if default_match:
                default_value = default_match.group(2) if default_match.group(2) is not None else default_match.group(3)
            columns.append(
                {
                    "column_name": name,
                    "property_name": field_prop(name),
                    "sql_type": sql_type,
                    "java_type": java_type(sql_type),
                    "comment": comment or name,
                    "label": clean_label(comment or name),
                    "nullable": "NOT NULL" not in rest.upper(),
                    "auto_increment": "AUTO_INCREMENT" in rest.upper(),
                    "default": default_value,
                    "required": ("NOT NULL" in rest.upper()) and ("AUTO_INCREMENT" not in rest.upper()) and default_value is None,
                }
            )
        tables[table_name] = columns
    return tables


def write_file(path: Path, content: str):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")


def control_type(column, module):
    prop = column["property_name"]
    upload_type = module.get("upload_fields", {}).get(prop)
    if upload_type == "image":
        return "imageUpload"
    if upload_type == "file":
        return "fileUpload"
    if prop in module.get("dict_fields", {}):
        return "select"
    if is_date(column):
        return "datetime" if "time" in prop.lower() else "date"
    if column["sql_type"].lower().startswith("text") or prop in module.get("textarea_fields", set()):
        return "textarea"
    return "input"


def query_test(column):
    prop = column["property_name"]
    if is_string(column):
        return f"{prop} != null and {prop} != ''"
    return f"{prop} != null"


def write_test(column):
    return f"{column['property_name']} != null"


def xml_parameter(column):
    return "#{" + column["property_name"] + "}"


def js_default(column):
    default_value = column["default"]
    if default_value is None or str(default_value).upper() == "NULL":
        return "null"
    if str(default_value).upper() == "CURRENT_TIMESTAMP":
        return "null"
    if column["java_type"] in {"Integer", "Long", "BigDecimal"}:
        return str(default_value)
    escaped = str(default_value).replace("\\", "\\\\").replace("'", "\\'")
    return f"'{escaped}'"


def find_column(columns, prop_name):
    for column in columns:
        if column["property_name"] == prop_name:
            return column
    return None


def build_context(table_name, module, columns):
    pk_column = next((column for column in columns if column["auto_increment"]), columns[0])
    annotated_columns = []
    for column in columns:
        current = dict(column)
        current["control_type"] = control_type(column, module)
        annotated_columns.append(current)

    query_columns = [find_column(annotated_columns, prop) for prop in module.get("query_fields", [])]
    query_columns = [column for column in query_columns if column is not None]

    list_columns = []
    for prop in module.get("list_fields", []):
        column = find_column(annotated_columns, prop)
        if column is not None:
            list_columns.append(column)
    if pk_column["property_name"] not in {column["property_name"] for column in list_columns}:
        list_columns.insert(0, pk_column)

    form_exclude_fields = set(module.get("form_exclude_fields", set())) | FORM_SKIP_COLUMNS | {pk_column["property_name"]}
    form_columns = [column for column in annotated_columns if column["property_name"] not in form_exclude_fields]

    dict_types = []
    for dict_type in module.get("dict_fields", {}).values():
        if dict_type not in dict_types:
            dict_types.append(dict_type)

    return {
        "table_name": table_name,
        "class_name": module["class_name"],
        "business_name": module["business_name"],
        "api_name": module["api_name"],
        "api_pascal": upper_first(module["api_name"]),
        "permission": f"education:{module['api_name']}:list",
        "pk_column": pk_column,
        "columns": annotated_columns,
        "query_columns": query_columns,
        "list_columns": list_columns,
        "form_columns": form_columns,
        "dict_fields": module.get("dict_fields", {}),
        "dict_types": dict_types,
        "like_fields": set(module.get("like_fields", set())),
    }


def generate_domain(context):
    class_name = context["class_name"]
    columns = [column for column in context["columns"] if column["column_name"] not in BASE_ENTITY_COLUMNS]
    imports = {
        "com.ruoyi.common.annotation.Excel",
        "com.ruoyi.common.core.domain.BaseEntity",
        "org.apache.commons.lang3.builder.ToStringBuilder",
        "org.apache.commons.lang3.builder.ToStringStyle",
    }
    if any(is_date(column) for column in columns):
        imports.add("com.fasterxml.jackson.annotation.JsonFormat")
        imports.add("java.util.Date")
    if any(column["java_type"] == "BigDecimal" for column in columns):
        imports.add("java.math.BigDecimal")

    lines = ["package com.ruoyi.system.domain.education;", ""]
    for item in sorted(imports):
        lines.append(f"import {item};")
    lines.extend(
        [
            "",
            "/**",
            f" * {context['business_name']}对象 {context['table_name']}",
            " */",
            f"public class {class_name} extends BaseEntity",
            "{",
            "    private static final long serialVersionUID = 1L;",
            "",
        ]
    )
    for column in columns:
        excel_args = [f'name = "{column["label"]}"']
        dict_type = context["dict_fields"].get(column["property_name"])
        if dict_type:
            excel_args.append(f'dictType = "{dict_type}"')
        lines.append(f"    /** {column['comment']} */")
        if is_date(column):
            pattern = "yyyy-MM-dd HH:mm:ss" if "time" in column["property_name"].lower() else "yyyy-MM-dd"
            lines.append(f'    @JsonFormat(pattern = "{pattern}")')
            excel_args.append(f'dateFormat = "{pattern}"')
        lines.append(f"    @Excel({', '.join(excel_args)})")
        lines.append(f"    private {column['java_type']} {column['property_name']};")
        lines.append("")
    for column in columns:
        prop = column["property_name"]
        cap = upper_first(prop)
        lines.extend(
            [
                f"    public void set{cap}({column['java_type']} {prop})",
                "    {",
                f"        this.{prop} = {prop};",
                "    }",
                "",
                f"    public {column['java_type']} get{cap}()",
                "    {",
                f"        return {prop};",
                "    }",
                "",
            ]
        )
    lines.extend(
        [
            "    @Override",
            "    public String toString()",
            "    {",
            "        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)",
        ]
    )
    for column in columns:
        lines.append(f'            .append("{column["property_name"]}", get{upper_first(column["property_name"])}())')
    lines.extend(
        [
            '            .append("createBy", getCreateBy())',
            '            .append("createTime", getCreateTime())',
            '            .append("updateBy", getUpdateBy())',
            '            .append("updateTime", getUpdateTime())',
            '            .append("remark", getRemark())',
            "            .toString();",
            "    }",
            "}",
            "",
        ]
    )
    return "\n".join(lines)


def generate_mapper(context):
    class_name = context["class_name"]
    pk = context["pk_column"]
    return "\n".join(
        [
            "package com.ruoyi.system.mapper.education;",
            "",
            "import java.util.List;",
            f"import com.ruoyi.system.domain.education.{class_name};",
            "",
            "/**",
            f" * {context['business_name']}Mapper接口",
            " */",
            f"public interface {class_name}Mapper",
            "{",
            f"    public {class_name} select{class_name}By{upper_first(pk['property_name'])}({pk['java_type']} {pk['property_name']});",
            "",
            f"    public List<{class_name}> select{class_name}List({class_name} {context['api_name']});",
            "",
            f"    public int insert{class_name}({class_name} {context['api_name']});",
            "",
            f"    public int update{class_name}({class_name} {context['api_name']});",
            "",
            f"    public int delete{class_name}By{upper_first(pk['property_name'])}({pk['java_type']} {pk['property_name']});",
            "",
            f"    public int delete{class_name}By{upper_first(pk['property_name'])}s({pk['java_type']}[] {pk['property_name']}s);",
            "}",
            "",
        ]
    )


def generate_service(context):
    class_name = context["class_name"]
    pk = context["pk_column"]
    return "\n".join(
        [
            "package com.ruoyi.system.service.education;",
            "",
            "import java.util.List;",
            f"import com.ruoyi.system.domain.education.{class_name};",
            "",
            "/**",
            f" * {context['business_name']}Service接口",
            " */",
            f"public interface I{class_name}Service",
            "{",
            f"    public {class_name} select{class_name}By{upper_first(pk['property_name'])}({pk['java_type']} {pk['property_name']});",
            "",
            f"    public List<{class_name}> select{class_name}List({class_name} {context['api_name']});",
            "",
            f"    public int insert{class_name}({class_name} {context['api_name']});",
            "",
            f"    public int update{class_name}({class_name} {context['api_name']});",
            "",
            f"    public int delete{class_name}By{upper_first(pk['property_name'])}s({pk['java_type']}[] {pk['property_name']}s);",
            "",
            f"    public int delete{class_name}By{upper_first(pk['property_name'])}({pk['java_type']} {pk['property_name']});",
            "}",
            "",
        ]
    )


def generate_service_impl(context):
    class_name = context["class_name"]
    pk = context["pk_column"]
    mapper_name = f"{class_name}Mapper"
    service_name = f"I{class_name}Service"
    return "\n".join(
        [
            "package com.ruoyi.system.service.impl.education;",
            "",
            "import java.util.List;",
            "import org.springframework.beans.factory.annotation.Autowired;",
            "import org.springframework.stereotype.Service;",
            "import com.ruoyi.common.utils.DateUtils;",
            f"import com.ruoyi.system.domain.education.{class_name};",
            f"import com.ruoyi.system.mapper.education.{mapper_name};",
            f"import com.ruoyi.system.service.education.{service_name};",
            "",
            "/**",
            f" * {context['business_name']}Service业务层处理",
            " */",
            "@Service",
            f"public class {class_name}ServiceImpl implements {service_name}",
            "{",
            "    @Autowired",
            f"    private {mapper_name} {context['api_name']}Mapper;",
            "",
            "    @Override",
            f"    public {class_name} select{class_name}By{upper_first(pk['property_name'])}({pk['java_type']} {pk['property_name']})",
            "    {",
            f"        return {context['api_name']}Mapper.select{class_name}By{upper_first(pk['property_name'])}({pk['property_name']});",
            "    }",
            "",
            "    @Override",
            f"    public List<{class_name}> select{class_name}List({class_name} {context['api_name']})",
            "    {",
            f"        return {context['api_name']}Mapper.select{class_name}List({context['api_name']});",
            "    }",
            "",
            "    @Override",
            f"    public int insert{class_name}({class_name} {context['api_name']})",
            "    {",
            f"        {context['api_name']}.setCreateTime(DateUtils.getNowDate());",
            f"        return {context['api_name']}Mapper.insert{class_name}({context['api_name']});",
            "    }",
            "",
            "    @Override",
            f"    public int update{class_name}({class_name} {context['api_name']})",
            "    {",
            f"        {context['api_name']}.setUpdateTime(DateUtils.getNowDate());",
            f"        return {context['api_name']}Mapper.update{class_name}({context['api_name']});",
            "    }",
            "",
            "    @Override",
            f"    public int delete{class_name}By{upper_first(pk['property_name'])}s({pk['java_type']}[] {pk['property_name']}s)",
            "    {",
            f"        return {context['api_name']}Mapper.delete{class_name}By{upper_first(pk['property_name'])}s({pk['property_name']}s);",
            "    }",
            "",
            "    @Override",
            f"    public int delete{class_name}By{upper_first(pk['property_name'])}({pk['java_type']} {pk['property_name']})",
            "    {",
            f"        return {context['api_name']}Mapper.delete{class_name}By{upper_first(pk['property_name'])}({pk['property_name']});",
            "    }",
            "}",
            "",
        ]
    )


def generate_controller(context):
    class_name = context["class_name"]
    pk = context["pk_column"]
    service_name = f"I{class_name}Service"
    return "\n".join(
        [
            "package com.ruoyi.web.controller.education;",
            "",
            "import java.util.List;",
            "import javax.servlet.http.HttpServletResponse;",
            "import org.springframework.beans.factory.annotation.Autowired;",
            "import org.springframework.security.access.prepost.PreAuthorize;",
            "import org.springframework.web.bind.annotation.DeleteMapping;",
            "import org.springframework.web.bind.annotation.GetMapping;",
            "import org.springframework.web.bind.annotation.PathVariable;",
            "import org.springframework.web.bind.annotation.PostMapping;",
            "import org.springframework.web.bind.annotation.PutMapping;",
            "import org.springframework.web.bind.annotation.RequestBody;",
            "import org.springframework.web.bind.annotation.RequestMapping;",
            "import org.springframework.web.bind.annotation.RestController;",
            "import com.ruoyi.common.annotation.Log;",
            "import com.ruoyi.common.core.controller.BaseController;",
            "import com.ruoyi.common.core.domain.AjaxResult;",
            "import com.ruoyi.common.core.page.TableDataInfo;",
            "import com.ruoyi.common.enums.BusinessType;",
            "import com.ruoyi.common.utils.poi.ExcelUtil;",
            f"import com.ruoyi.system.domain.education.{class_name};",
            f"import com.ruoyi.system.service.education.{service_name};",
            "",
            "/**",
            f" * {context['business_name']}Controller",
            " */",
            "@RestController",
            f'@RequestMapping("/education/{context["api_name"]}")',
            f"public class {class_name}Controller extends BaseController",
            "{",
            "    @Autowired",
            f"    private {service_name} {context['api_name']}Service;",
            "",
            f'    @PreAuthorize("@ss.hasPermi(\'{context["permission"]}\')")',
            '    @GetMapping("/list")',
            f"    public TableDataInfo list({class_name} {context['api_name']})",
            "    {",
            "        startPage();",
            f"        List<{class_name}> list = {context['api_name']}Service.select{class_name}List({context['api_name']});",
            "        return getDataTable(list);",
            "    }",
            "",
            f'    @PreAuthorize("@ss.hasPermi(\'{context["permission"]}\')")',
            f'    @Log(title = "{context["business_name"]}", businessType = BusinessType.EXPORT)',
            '    @PostMapping("/export")',
            f"    public void export(HttpServletResponse response, {class_name} {context['api_name']})",
            "    {",
            f"        List<{class_name}> list = {context['api_name']}Service.select{class_name}List({context['api_name']});",
            f"        ExcelUtil<{class_name}> util = new ExcelUtil<{class_name}>({class_name}.class);",
            f'        util.exportExcel(response, list, "{context["business_name"]}数据");',
            "    }",
            "",
            f'    @PreAuthorize("@ss.hasPermi(\'{context["permission"]}\')")',
            f'    @GetMapping(value = "/{{{pk["property_name"]}}}")',
            f"    public AjaxResult getInfo(@PathVariable {pk['java_type']} {pk['property_name']})",
            "    {",
            f"        return success({context['api_name']}Service.select{class_name}By{upper_first(pk['property_name'])}({pk['property_name']}));",
            "    }",
            "",
            f'    @PreAuthorize("@ss.hasPermi(\'{context["permission"]}\')")',
            f'    @Log(title = "{context["business_name"]}", businessType = BusinessType.INSERT)',
            "    @PostMapping",
            f"    public AjaxResult add(@RequestBody {class_name} {context['api_name']})",
            "    {",
            f"        {context['api_name']}.setCreateBy(getUsername());",
            f"        return toAjax({context['api_name']}Service.insert{class_name}({context['api_name']}));",
            "    }",
            "",
            f'    @PreAuthorize("@ss.hasPermi(\'{context["permission"]}\')")',
            f'    @Log(title = "{context["business_name"]}", businessType = BusinessType.UPDATE)',
            "    @PutMapping",
            f"    public AjaxResult edit(@RequestBody {class_name} {context['api_name']})",
            "    {",
            f"        {context['api_name']}.setUpdateBy(getUsername());",
            f"        return toAjax({context['api_name']}Service.update{class_name}({context['api_name']}));",
            "    }",
            "",
            f'    @PreAuthorize("@ss.hasPermi(\'{context["permission"]}\')")',
            f'    @Log(title = "{context["business_name"]}", businessType = BusinessType.DELETE)',
            f'    @DeleteMapping("/{{{pk["property_name"]}s}}")',
            f"    public AjaxResult remove(@PathVariable {pk['java_type']}[] {pk['property_name']}s)",
            "    {",
            f"        return toAjax({context['api_name']}Service.delete{class_name}By{upper_first(pk['property_name'])}s({pk['property_name']}s));",
            "    }",
            "}",
            "",
        ]
    )


def generate_xml(context):
    class_name = context["class_name"]
    pk = context["pk_column"]
    domain_name = f"com.ruoyi.system.domain.education.{class_name}"
    mapper_name = f"com.ruoyi.system.mapper.education.{class_name}Mapper"
    has_del_flag = any(column["column_name"] == "del_flag" for column in context["columns"])
    lines = [
        '<?xml version="1.0" encoding="UTF-8" ?>',
        "<!DOCTYPE mapper",
        'PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"',
        '"http://mybatis.org/dtd/mybatis-3-mapper.dtd">',
        f'<mapper namespace="{mapper_name}">',
        "",
        f'    <resultMap type="{domain_name}" id="{class_name}Result">',
    ]
    for column in context["columns"]:
        tag = "id" if column["column_name"] == pk["column_name"] else "result"
        lines.append(f'        <{tag} property="{column["property_name"]}" column="{column["column_name"]}" />')
    lines.extend([f"    </resultMap>", "", f'    <sql id="select{class_name}Vo">', "        select"])
    for index, column in enumerate(context["columns"]):
        suffix = "," if index < len(context["columns"]) - 1 else ""
        lines.append(f'            t.{column["column_name"]}{suffix}')
    lines.extend([f"        from {context['table_name']} t", "    </sql>", "", f'    <select id="select{class_name}List" parameterType="{domain_name}" resultMap="{class_name}Result">', f'        <include refid="select{class_name}Vo"/>', "        <where>"])
    if has_del_flag:
        lines.append("            and (t.del_flag is null or t.del_flag = '0')")
    for column in context["query_columns"]:
        if column["property_name"] in context["like_fields"]:
            lines.append(f'            <if test="{query_test(column)}"> and t.{column["column_name"]} like concat(\'%\', {xml_parameter(column)}, \'%\')</if>')
        else:
            lines.append(f'            <if test="{query_test(column)}"> and t.{column["column_name"]} = {xml_parameter(column)}</if>')
    lines.extend(["        </where>", f"        order by t.{pk['column_name']} desc", "    </select>", "", f'    <select id="select{class_name}By{upper_first(pk["property_name"])}" parameterType="{pk["java_type"]}" resultMap="{class_name}Result">', f'        <include refid="select{class_name}Vo"/>', f"        where t.{pk['column_name']} = #{{{pk['property_name']}}}"])
    if has_del_flag:
        lines.append("          and (t.del_flag is null or t.del_flag = '0')")
    lines.extend(["    </select>", "", f'    <insert id="insert{class_name}" parameterType="{domain_name}" useGeneratedKeys="true" keyProperty="{pk["property_name"]}">', f"        insert into {context['table_name']}", '        <trim prefix="(" suffix=")" suffixOverrides=",">'])
    for column in context["columns"]:
        if column["column_name"] == pk["column_name"] and pk["auto_increment"]:
            continue
        lines.append(f'            <if test="{write_test(column)}">{column["column_name"]},</if>')
    lines.extend(["        </trim>", '        <trim prefix="values (" suffix=")" suffixOverrides=",">'])
    for column in context["columns"]:
        if column["column_name"] == pk["column_name"] and pk["auto_increment"]:
            continue
        lines.append(f'            <if test="{write_test(column)}">{xml_parameter(column)},</if>')
    lines.extend(["        </trim>", "    </insert>", "", f'    <update id="update{class_name}" parameterType="{domain_name}">', f"        update {context['table_name']}", '        <trim prefix="SET" suffixOverrides=",">'])
    for column in context["columns"]:
        if column["column_name"] == pk["column_name"]:
            continue
        lines.append(f'            <if test="{write_test(column)}">{column["column_name"]} = {xml_parameter(column)},</if>')
    lines.extend(["        </trim>", f"        where {pk['column_name']} = #{{{pk['property_name']}}}", "    </update>", "", f'    <delete id="delete{class_name}By{upper_first(pk["property_name"])}" parameterType="{pk["java_type"]}">', f"        delete from {context['table_name']} where {pk['column_name']} = #{{{pk['property_name']}}}", "    </delete>", "", f'    <delete id="delete{class_name}By{upper_first(pk["property_name"])}s" parameterType="long">', f"        delete from {context['table_name']} where {pk['column_name']} in", f'        <foreach item="{pk["property_name"]}" collection="array" open="(" separator="," close=")">', f'            #{{{pk["property_name"]}}}', "        </foreach>", "    </delete>", "</mapper>", ""])
    return "\n".join(lines)


def render_query_item(column, dict_fields):
    label = column["label"]
    prop = column["property_name"]
    control = column["control_type"]
    lines = [f'      <el-form-item label="{label}" prop="{prop}">']
    if control == "select":
        dict_type = dict_fields[prop]
        lines.extend([f'        <el-select v-model="queryParams.{prop}" placeholder="请选择{label}" clearable style="width: 220px">', f'          <el-option v-for="dict in dict.type.{dict_type}" :key="dict.value" :label="dict.label" :value="dict.value" />', "        </el-select>"])
    elif control in {"date", "datetime"}:
        picker_type = "datetime" if control == "datetime" else "date"
        value_format = "yyyy-MM-dd HH:mm:ss" if control == "datetime" else "yyyy-MM-dd"
        lines.append(f'        <el-date-picker v-model="queryParams.{prop}" type="{picker_type}" value-format="{value_format}" placeholder="请选择{label}" clearable style="width: 220px" />')
    else:
        lines.append(f'        <el-input v-model="queryParams.{prop}" placeholder="请输入{label}" clearable style="width: 220px" @keyup.enter.native="handleQuery" />')
    lines.append("      </el-form-item>")
    return lines


def render_table_column(column, dict_fields):
    label = column["label"]
    prop = column["property_name"]
    control = column["control_type"]
    if control == "select":
        dict_type = dict_fields[prop]
        return [f'      <el-table-column label="{label}" align="center" prop="{prop}">', '        <template slot-scope="scope">', f'          <dict-tag :options="dict.type.{dict_type}" :value="scope.row.{prop}" />', "        </template>", "      </el-table-column>"]
    if control == "imageUpload":
        return [f'      <el-table-column label="{label}" align="center" prop="{prop}" width="100">', '        <template slot-scope="scope">', f'          <image-preview :src="scope.row.{prop}" :width="50" :height="50" />', "        </template>", "      </el-table-column>"]
    if control in {"date", "datetime"}:
        return [f'      <el-table-column label="{label}" align="center" prop="{prop}" width="180">', '        <template slot-scope="scope">', f"          <span>{vue_expr(f'parseTime(scope.row.{prop})')}</span>", "        </template>", "      </el-table-column>"]
    return [f'      <el-table-column label="{label}" align="center" prop="{prop}" :show-overflow-tooltip="true" />']


def render_form_item(column, dict_fields):
    label = column["label"]
    prop = column["property_name"]
    control = column["control_type"]
    lines = [f'        <el-form-item label="{label}" prop="{prop}">']
    if control == "select":
        dict_type = dict_fields[prop]
        lines.extend([f'          <el-select v-model="form.{prop}" placeholder="请选择{label}" clearable style="width: 100%">', f'            <el-option v-for="dict in dict.type.{dict_type}" :key="dict.value" :label="dict.label" :value="dict.value" />', "          </el-select>"])
    elif control == "textarea":
        lines.append(f'          <el-input v-model="form.{prop}" type="textarea" :rows="3" placeholder="请输入{label}" />')
    elif control == "imageUpload":
        lines.append(f'          <image-upload v-model="form.{prop}" />')
    elif control == "fileUpload":
        lines.append(f'          <file-upload v-model="form.{prop}" />')
    elif control in {"date", "datetime"}:
        picker_type = "datetime" if control == "datetime" else "date"
        value_format = "yyyy-MM-dd HH:mm:ss" if control == "datetime" else "yyyy-MM-dd"
        lines.append(f'          <el-date-picker v-model="form.{prop}" type="{picker_type}" value-format="{value_format}" placeholder="请选择{label}" clearable style="width: 100%" />')
    else:
        lines.append(f'          <el-input v-model="form.{prop}" placeholder="请输入{label}" />')
    lines.append("        </el-form-item>")
    return lines


def generate_api(context):
    api_pascal = context["api_pascal"]
    api_name = context["api_name"]
    pk = context["pk_column"]["property_name"]
    return "\n".join(
        [
            "import request from '@/utils/request'",
            "",
            f"export function list{api_pascal}(query) {{",
            "  return request({",
            f"    url: '/education/{api_name}/list',",
            "    method: 'get',",
            "    params: query",
            "  })",
            "}",
            "",
            f"export function get{api_pascal}({pk}) {{",
            "  return request({",
            f"    url: '/education/{api_name}/' + {pk},",
            "    method: 'get'",
            "  })",
            "}",
            "",
            f"export function add{api_pascal}(data) {{",
            "  return request({",
            f"    url: '/education/{api_name}',",
            "    method: 'post',",
            "    data: data",
            "  })",
            "}",
            "",
            f"export function update{api_pascal}(data) {{",
            "  return request({",
            f"    url: '/education/{api_name}',",
            "    method: 'put',",
            "    data: data",
            "  })",
            "}",
            "",
            f"export function del{api_pascal}({pk}) {{",
            "  return request({",
            f"    url: '/education/{api_name}/' + {pk},",
            "    method: 'delete'",
            "  })",
            "}",
            "",
        ]
    )


def generate_view(context):
    api_pascal = context["api_pascal"]
    api_name = context["api_name"]
    pk = context["pk_column"]["property_name"]
    dict_fields = context["dict_fields"]
    list_name = f"{api_name}List"
    dict_types = context["dict_types"]
    lines = ["<template>", '  <div class="app-container">', '    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="96px" v-show="showSearch">']
    for column in context["query_columns"]:
        lines.extend(render_query_item(column, dict_fields))
    lines.extend(["      <el-form-item>", '        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>', '        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>', "      </el-form-item>", "    </el-form>", "", '    <el-row :gutter="10" class="mb8">', '      <el-col :span="1.5">', f'        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="[\'{context["permission"]}\']">新增</el-button>', "      </el-col>", '      <el-col :span="1.5">', f'        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="[\'{context["permission"]}\']">修改</el-button>', "      </el-col>", '      <el-col :span="1.5">', f'        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="[\'{context["permission"]}\']">删除</el-button>', "      </el-col>", '      <el-col :span="1.5">', f'        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="[\'{context["permission"]}\']">导出</el-button>', "      </el-col>", '      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>', "    </el-row>", "", f'    <el-table v-loading="loading" :data="{list_name}" @selection-change="handleSelectionChange">', '      <el-table-column type="selection" width="55" align="center" />'])
    for column in context["list_columns"]:
        lines.extend(render_table_column(column, dict_fields))
    lines.extend(['      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">', '        <template slot-scope="scope">', f'          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="[\'{context["permission"]}\']">修改</el-button>', f'          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="[\'{context["permission"]}\']">删除</el-button>', "        </template>", "      </el-table-column>", "    </el-table>", "", '    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />', "", '    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>', '      <el-form ref="form" :model="form" :rules="rules" label-width="100px">'])
    for column in context["form_columns"]:
        lines.extend(render_form_item(column, dict_fields))
    lines.extend(["      </el-form>", '      <div slot="footer" class="dialog-footer">', '        <el-button type="primary" @click="submitForm">确 定</el-button>', '        <el-button @click="cancel">取 消</el-button>', "      </div>", "    </el-dialog>", "  </div>", "</template>", "", "<script>", f"import {{ list{api_pascal}, get{api_pascal}, del{api_pascal}, add{api_pascal}, update{api_pascal} }} from '@/api/education/{api_name}'", "", "export default {", f"  name: '{api_pascal}',"])
    if dict_types:
        lines.append(f"  dicts: [{', '.join(f\"'{item}'\" for item in dict_types)}],")
    lines.extend(["  data() {", "    return {", "      loading: true,", "      ids: [],", "      single: true,", "      multiple: true,", "      showSearch: true,", "      total: 0,", f"      {list_name}: [],", "      title: '',", "      open: false,", "      queryParams: {", "        pageNum: 1,", "        pageSize: 10,"])
    for index, column in enumerate(context["query_columns"]):
        suffix = "," if index < len(context["query_columns"]) - 1 else ""
        lines.append(f"        {column['property_name']}: null{suffix}")
    lines.extend(["      },", "      form: {},", "      rules: {"])
    required_columns = [column for column in context["form_columns"] if column["required"]]
    for index, column in enumerate(required_columns):
        trigger = "change" if column["control_type"] in {"select", "date", "datetime", "imageUpload", "fileUpload"} else "blur"
        suffix = "," if index < len(required_columns) - 1 else ""
        lines.extend([f"        {column['property_name']}: [", f"          {{ required: true, message: '请输入{column['label']}', trigger: '{trigger}' }}", f"        ]{suffix}"])
    lines.extend(["      }", "    }", "  },", "  created() {", "    this.getList()", "  },", "  methods: {", "    getList() {", "      this.loading = true", f"      list{api_pascal}(this.queryParams).then(response => {{", f"        this.{list_name} = response.rows", "        this.total = response.total", "        this.loading = false", "      }).catch(() => {", "        this.loading = false", "      })", "    },", "    cancel() {", "      this.open = false", "      this.reset()", "    },", "    reset() {", "      this.form = {", f"        {pk}: null,"])
    for index, column in enumerate(context["form_columns"]):
        suffix = "," if index < len(context["form_columns"]) - 1 else ""
        lines.append(f"        {column['property_name']}: {js_default(column)}{suffix}")
    lines.extend(["      }", "      this.resetForm('form')", "    },", "    handleQuery() {", "      this.queryParams.pageNum = 1", "      this.getList()", "    },", "    resetQuery() {", "      this.resetForm('queryForm')", "      this.handleQuery()", "    },", "    handleSelectionChange(selection) {", f"      this.ids = selection.map(item => item.{pk})", "      this.single = selection.length !== 1", "      this.multiple = selection.length === 0", "    },", "    handleAdd() {", "      this.reset()", "      this.open = true", f"      this.title = '新增{context['business_name']}'", "    },", "    handleUpdate(row) {", "      this.reset()", f"      const id = row.{pk} || this.ids[0]", f"      get{api_pascal}(id).then(response => {{", "        this.form = response.data", "        this.open = true", f"        this.title = '修改{context['business_name']}'", "      })", "    },", "    submitForm() {", "      this.$refs.form.validate(valid => {", "        if (!valid) {", "          return", "        }", f"        if (this.form.{pk} != null) {{", f"          update{api_pascal}(this.form).then(() => {{", "            this.$modal.msgSuccess('修改成功')", "            this.open = false", "            this.getList()", "          })", "        } else {", f"          add{api_pascal}(this.form).then(() => {{", "            this.$modal.msgSuccess('新增成功')", "            this.open = false", "            this.getList()", "          })", "        }", "      })", "    },", "    handleDelete(row) {", f"      const ids = row.{pk} || this.ids", f"      this.$modal.confirm('是否确认删除{context['business_name']}编号为\"' + ids + '\"的数据项？').then(() => {{", f"        return del{api_pascal}(ids)", "      }).then(() => {", "        this.getList()", "        this.$modal.msgSuccess('删除成功')", "      }).catch(() => {})", "    },", "    handleExport() {", f"      this.download('education/{api_name}/export', {{", "        ...this.queryParams", f"      }}, '{api_name}_' + new Date().getTime() + '.xlsx')", "    }", "  }", "}", "</script>", ""])
    return "\n".join(lines)


def main():
    tables = parse_tables()
    for table_name, module in MODULES.items():
        context = build_context(table_name, module, tables[table_name])
        write_file(JAVA_DOMAIN_DIR / f"{context['class_name']}.java", generate_domain(context))
        write_file(JAVA_MAPPER_DIR / f"{context['class_name']}Mapper.java", generate_mapper(context))
        write_file(JAVA_SERVICE_DIR / f"I{context['class_name']}Service.java", generate_service(context))
        write_file(JAVA_SERVICE_IMPL_DIR / f"{context['class_name']}ServiceImpl.java", generate_service_impl(context))
        write_file(CONTROLLER_DIR / f"{context['class_name']}Controller.java", generate_controller(context))
        write_file(XML_DIR / f"{context['class_name']}Mapper.xml", generate_xml(context))
        write_file(API_DIR / f"{context['api_name']}.js", generate_api(context))
        write_file(VIEW_DIR / context["api_name"] / "index.vue", generate_view(context))
    print(f"generated {len(MODULES)} education modules")


if __name__ == "__main__":
    main()
