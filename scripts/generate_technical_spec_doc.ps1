param(
    [string]$OutputPath = "g:\Graduationproject2026\ProjectFile\20260409_SpringBoot_Web_XueXiTongJiaoXueGuanLiPingTai\XueXiTongJiaoXueGuanLiPingTai\doc\学习通教学管理系统技术说明文档.docx"
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Set-ParagraphStyle {
    param(
        [Parameter(Mandatory = $true)]$Selection,
        [string]$FontName = "宋体",
        [double]$FontSize = 12,
        [bool]$Bold = $false,
        [int]$Alignment = 0,
        [double]$Before = 0,
        [double]$After = 0
    )

    $Selection.Font.NameFarEast = $FontName
    $Selection.Font.Name = $FontName
    $Selection.Font.Size = $FontSize
    $Selection.Font.Bold = [int]$Bold
    $Selection.ParagraphFormat.Alignment = $Alignment
    $Selection.ParagraphFormat.SpaceBefore = $Before
    $Selection.ParagraphFormat.SpaceAfter = $After
    $Selection.ParagraphFormat.LineSpacingRule = 0
}

function Add-Paragraph {
    param(
        [Parameter(Mandatory = $true)]$Selection,
        [Parameter(Mandatory = $true)][string]$Text,
        [string]$FontName = "宋体",
        [double]$FontSize = 12,
        [bool]$Bold = $false,
        [int]$Alignment = 0,
        [double]$Before = 0,
        [double]$After = 0
    )

    Set-ParagraphStyle -Selection $Selection -FontName $FontName -FontSize $FontSize -Bold $Bold -Alignment $Alignment -Before $Before -After $After
    $Selection.TypeText($Text)
    $Selection.TypeParagraph()
}

function Add-BlankLine {
    param([Parameter(Mandatory = $true)]$Selection)
    $Selection.TypeParagraph()
}

function Add-SectionTitle {
    param(
        [Parameter(Mandatory = $true)]$Selection,
        [Parameter(Mandatory = $true)][string]$Text
    )
    Add-Paragraph -Selection $Selection -Text $Text -FontName "黑体" -FontSize 16 -Bold $true -Before 6 -After 6
}

function Add-SubTitle {
    param(
        [Parameter(Mandatory = $true)]$Selection,
        [Parameter(Mandatory = $true)][string]$Text
    )
    Add-Paragraph -Selection $Selection -Text $Text -FontName "黑体" -FontSize 14 -Bold $true -Before 4 -After 4
}

function Add-Table {
    param(
        [Parameter(Mandatory = $true)]$Document,
        [Parameter(Mandatory = $true)]$Selection,
        [Parameter(Mandatory = $true)][string[]]$Headers,
        [Parameter(Mandatory = $true)][object[]]$Rows
    )

    $rowCount = $Rows.Count + 1
    $colCount = $Headers.Count
    $range = $Selection.Range
    $table = $Document.Tables.Add($range, $rowCount, $colCount)
    $table.Borders.Enable = 1
    $table.Rows.Alignment = 1
    $table.Range.Font.NameFarEast = "宋体"
    $table.Range.Font.Name = "宋体"
    $table.Range.Font.Size = 10.5
    $table.AllowAutoFit = $true

    for ($c = 1; $c -le $colCount; $c++) {
        $table.Cell(1, $c).Range.Text = [string]$Headers[$c - 1]
        $table.Cell(1, $c).Range.Bold = 1
        $table.Cell(1, $c).Range.ParagraphFormat.Alignment = 1
    }

    for ($r = 0; $r -lt $Rows.Count; $r++) {
        $row = $Rows[$r]
        for ($c = 0; $c -lt $colCount; $c++) {
            $table.Cell($r + 2, $c + 1).Range.Text = [string]$row[$c]
        }
    }

    try {
        $table.AutoFitBehavior(1) | Out-Null
    } catch {
    }

    $Selection.SetRange($table.Range.End, $table.Range.End)
    $Selection.TypeParagraph()
}

function Add-NumberedLines {
    param(
        [Parameter(Mandatory = $true)]$Selection,
        [Parameter(Mandatory = $true)][string[]]$Lines
    )
    foreach ($line in $Lines) {
        Add-Paragraph -Selection $Selection -Text $line -FontName "宋体" -FontSize 12
    }
}

$projectName = "学习通教学管理系统"
$docTitle = "学习通教学管理系统技术说明文档"
$projectVersion = "v3.8.9"
$docDate = "2026年04月10日"

$techRows = @(
    @("前端框架", "Vue 2.6.12", "采用 Vue + Vue Router + Vuex 构建前端单页应用"),
    @("UI 组件库", "Element UI 2.15.14", "提供表单、表格、弹窗、布局等基础组件"),
    @("前端工程化", "Vue CLI 4.4.6", "支持开发、构建、打包与代理配置"),
    @("HTTP 通信", "Axios 0.28.1", "统一封装前后端接口请求"),
    @("图表组件", "ECharts 5.4.0", "用于教学统计与可视化展示"),
    @("后端语言", "Java 8", "由 Maven 根工程属性 java.version 定义"),
    @("后端框架", "Spring Boot 2.5.15", "负责 Web 容器、自动配置与应用启动"),
    @("安全框架", "Spring Security 5.7.12 + JWT 0.9.1", "实现登录认证、权限鉴权与令牌管理"),
    @("ORM/持久层", "MyBatis + Mapper XML", "负责 SQL 映射与数据访问"),
    @("分页插件", "PageHelper 1.4.7", "提供列表分页能力"),
    @("连接池", "Druid 1.2.23", "提供数据库连接池与监控页面"),
    @("缓存", "Redis", "用于登录状态、验证码与缓存数据存储"),
    @("数据库", "MySQL 5.7+", "业务脚本说明建议使用 MySQL 5.7 / UTF-8（推荐 utf8mb4）"),
    @("接口文档", "Swagger 3.0.0", "自动生成接口文档与调试入口"),
    @("定时任务", "Quartz", "提供任务调度与执行日志能力"),
    @("Excel 处理", "Apache POI 4.1.2", "用于导入导出业务数据")
)

$moduleRows = @(
    @("ruoyi-admin", "Web 服务入口", "系统启动类、全局配置、REST 控制器、运行配置文件"),
    @("ruoyi-framework", "框架支撑层", "包含安全、数据源、拦截器、AOP、Web 配置等"),
    @("ruoyi-system", "系统与业务核心层", "系统基础业务与教育业务 Domain、Mapper、Service"),
    @("ruoyi-common", "公共基础组件", "常量、工具类、通用实体、异常与注解"),
    @("ruoyi-quartz", "定时任务模块", "提供任务调度管理与任务执行能力"),
    @("ruoyi-generator", "代码生成模块", "支持根据表结构生成前后端 CRUD 代码"),
    @("ruoyi-ui", "前端应用", "Vue 管理端页面、路由、状态管理、接口封装"),
    @("sql", "数据库脚本目录", "包含若依基础库、Quartz、教学业务结构与演示数据脚本"),
    @("scripts", "辅助脚本目录", "包含教学模块代码生成脚本等辅助工具")
)

$businessRows = @(
    @("基础平台能力", "用户、角色、部门、岗位、菜单、字典、参数、日志、监控、定时任务、代码生成、Swagger 等", "沿用若依平台能力，作为教学平台的基础管理底座"),
    @("教师档案", "teacher", "维护教师基础档案、院系、职称、研究方向等信息"),
    @("学生档案", "student", "维护学生档案、专业班级与学籍状态"),
    @("学期管理", "term", "维护学年学期、开学日期、选课时间窗口"),
    @("课程管理", "course", "维护课程号、课程名称、所属学期、院系、学分与选课规则"),
    @("授课安排", "courseTeacher", "建立课程与授课教师的关联关系"),
    @("课程章节", "chapter", "维护课程目录树、章节说明与学习时长"),
    @("学习资源", "resource", "维护课程/章节资源及文件上传信息"),
    @("选课管理", "enrollment", "管理学生选课、退课、完成状态和课程进度"),
    @("学习进度", "progress", "管理学生在课程章节维度的学习进度"),
    @("作业管理", "assignment", "维护作业标题、类型、要求、截止时间、发布状态"),
    @("作业附件", "assignmentAttachment", "维护作业附件元数据与上传文件"),
    @("作业提交", "submission", "记录学生作业提交、评分、批改状态与教师评语"),
    @("提交附件", "submissionAttachment", "记录学生提交附件元数据与文件"),
    @("课程公告", "notice", "维护课程公告、提醒、发布时间与置顶状态"),
    @("消息提醒", "message", "维护站内消息、业务关联、阅读状态、过期时间"),
    @("教学统计", "statistics", "展示课程规模、教师工作量、待批改作业等统计信息")
)

$dbRows = @(
    @("sys_user", "若依基础表", "系统用户主表，教学模块通过 user_id 逻辑关联教师、学生与消息对象"),
    @("sys_dept", "若依基础表", "系统部门表，教学模块复用为院系组织结构"),
    @("sys_role / sys_user_role", "若依基础表", "保存系统角色及用户角色关系，教学扩展 student、teacher、master 三类角色"),
    @("sys_dict_type / sys_dict_data", "若依基础表", "保存教学状态、类型等字典数据"),
    @("edu_teacher_profile", "教师档案表", "教师工号、姓名、院系、职称、研究方向等"),
    @("edu_student_profile", "学生档案表", "学号、姓名、院系、专业班级、学籍状态等"),
    @("edu_term", "学期表", "学期编码、学年、学期序号、时间范围"),
    @("edu_course", "课程表", "学期、院系、课程号、课程名称、学分、课时、容量等"),
    @("edu_course_teacher", "授课关联表", "课程与教师的多对多关系及教师角色"),
    @("edu_course_chapter", "课程章节表", "课程目录树结构、祖级路径与章节属性"),
    @("edu_course_resource", "学习资源表", "课程资源、章节资源、文件元数据"),
    @("edu_course_enrollment", "选课记录表", "学生选课、退课、学习进度和完成情况"),
    @("edu_learning_progress", "学习进度表", "学生对章节的学习分钟数和完成进度"),
    @("edu_assignment", "作业表", "作业标题、类型、要求、总分、截止时间、发布状态"),
    @("edu_assignment_attachment", "作业附件表", "作业附件元数据"),
    @("edu_assignment_submission", "作业提交表", "学生提交记录、批改状态、得分与评语"),
    @("edu_submission_attachment", "提交附件表", "学生作业提交附件元数据"),
    @("edu_course_notice", "课程公告表", "公告标题、内容、类型、状态与发布时间"),
    @("edu_user_message", "消息提醒表", "用户消息、业务关联、阅读状态与过期时间")
)

$relationLines = @(
    "1. 系统用户复用若依 sys_user，教师档案表与学生档案表分别通过 user_id 与系统用户建立一对一逻辑关系。",
    "2. 院系信息复用 sys_dept，教师、学生、课程等业务对象通过 dept_id 与院系建立逻辑关联。",
    "3. 学期表 edu_term 与课程表 edu_course 为一对多关系；一门课程只归属一个学期。",
    "4. 课程与教师通过 edu_course_teacher 建立多对多关系，支持主讲教师、助教与负责人标记。",
    "5. 一门课程可以包含多个章节、多个资源、多个公告、多个作业与多条选课记录。",
    "6. 作业与作业附件是一对多关系；作业与学生提交记录也是一对多关系。",
    "7. 学生提交与提交附件是一对多关系，用于记录作业上交材料。",
    "8. 学习进度表细化到课程章节维度，用于记录学生的章节学习状态。",
    "9. 消息提醒表可关联课程、作业、公告等业务对象，用于业务通知闭环。"
)

$configRows = @(
    @("后端 HTTP 端口", "8080", "ruoyi-admin/src/main/resources/application.yml"),
    @("前端开发端口", "80", "ruoyi-ui/vue.config.js"),
    @("前端开发环境接口前缀", "/dev-api", "ruoyi-ui/.env.development"),
    @("前端测试环境接口前缀", "/stage-api", "ruoyi-ui/.env.staging"),
    @("前端生产环境接口前缀", "/prod-api", "ruoyi-ui/.env.production"),
    @("文件上传目录", "D:/ruoyi/uploadPath", "application.yml 当前开发配置"),
    @("Redis 地址", "localhost:6379 / database 0", "application.yml 当前开发配置"),
    @("MySQL 地址", "localhost:3308/20260409_xuexitongjiaoxueguanlipingtai", "application-druid.yml 当前开发配置"),
    @("数据库账号", "root / 123456", "仅当前开发配置，生产环境必须替换"),
    @("Druid 控制台", "/druid/*，账号 ruoyi / 123456", "仅当前开发配置，生产环境必须替换"),
    @("JWT 有效期", "30 分钟", "application.yml token.expireTime"),
    @("JWT 密钥", "abcdefghijklmnopqrstuvwxyz", "仅当前开发配置，生产环境必须替换")
)

$deploymentLines = @(
    "1. 数据库初始化顺序建议为：sql/ruoyi.sql -> sql/quartz.sql -> sql/xuexitong_teaching_schema.sql -> sql/xuexitong_teaching_demo_init.sql -> sql/xuexitong_teaching_menu_fix.sql（如需修正菜单）。",
    "2. 后端环境建议安装 JDK 1.8、Maven 3.6+、MySQL 5.7+、Redis 5.x/6.x。",
    "3. 前端环境建议安装 Node.js 与 npm，并在 ruoyi-ui 目录执行 npm install 安装依赖。",
    "4. 后端构建命令：mvn clean package -DskipTests。",
    "5. 后端启动命令：java -jar ruoyi-admin/target/ruoyi-admin.jar。",
    "6. 前端开发命令：npm run dev；前端生产构建命令：npm run build:prod。",
    "7. 开发模式下前端通过 vue.config.js 的代理规则将 /dev-api 请求转发到 http://localhost:8080。",
    "8. 生产部署时需要将前端 dist 目录发布到 Web 服务器，并保证接口前缀与网关/反向代理配置一致。",
    "9. 首次上线前必须替换数据库口令、Druid 控制台口令、JWT 密钥，并根据服务器环境调整上传目录与 Redis 地址。"
)

$securityLines = @(
    "1. 系统认证基于 Spring Security + JWT，实现前后端分离场景下的无状态认证。",
    "2. 权限控制沿用若依的角色、菜单、按钮权限模型，前端通过权限标识控制页面操作按钮显示。",
    "3. 系统支持动态菜单加载与多角色授权，教学模块权限标识以 education:xxx:list 等形式命名。",
    "4. 系统集成 Redis，用于验证码、登录态相关缓存与通用缓存管理。",
    "5. 系统启用 XSS 过滤规则，默认匹配 /system/*、/monitor/*、/tool/* 等路径。",
    "6. 系统支持操作日志、登录日志、在线用户监控、缓存监控、服务监控与 Druid SQL 监控。"
)

$extensionLines = @(
    "1. 教学业务模块遵循 Domain -> Mapper -> Service -> Controller -> API -> View 的标准分层结构，便于继续扩展。",
    "2. 项目内提供 scripts/generate_teaching_modules.py 等脚本，可用于根据业务表快速生成教学模块代码骨架。",
    "3. 业务状态、类型等枚举值统一通过若依字典表维护，避免硬编码在前后端页面中。",
    "4. 前端页面集中在 ruoyi-ui/src/views/education，接口封装集中在 ruoyi-ui/src/api/education，便于模块化维护。",
    "5. 新增业务表时建议同时补充 SQL 结构、字典数据、菜单权限、前端路由、接口文档和演示数据。",
    "6. 当前项目已对多个教学页面做了《关联名称下拉化》改造，后续扩展时建议继续保持关联字段名称化展示，避免直接输入业务 ID。"
)

$word = $null
$document = $null

try {
    $outputDir = Split-Path -Parent $OutputPath
    if (-not (Test-Path $outputDir)) {
        New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
    }
    if (Test-Path $OutputPath) {
        Remove-Item -LiteralPath $OutputPath -Force
    }

    $word = New-Object -ComObject Word.Application
    $word.Visible = $false
    $word.DisplayAlerts = 0
    $document = $word.Documents.Add()
    $selection = $word.Selection

    Add-BlankLine -Selection $selection
    Add-BlankLine -Selection $selection
    Add-Paragraph -Selection $selection -Text $docTitle -FontName "黑体" -FontSize 22 -Bold $true -Alignment 1 -After 12
    Add-Paragraph -Selection $selection -Text "基于 RuoYi-Vue 的毕业设计项目技术说明" -FontName "楷体" -FontSize 14 -Alignment 1 -After 20
    Add-Paragraph -Selection $selection -Text "项目名称：$projectName" -FontName "宋体" -FontSize 14 -Alignment 1
    Add-Paragraph -Selection $selection -Text "项目版本：$projectVersion" -FontName "宋体" -FontSize 14 -Alignment 1
    Add-Paragraph -Selection $selection -Text "文档日期：$docDate" -FontName "宋体" -FontSize 14 -Alignment 1
    Add-Paragraph -Selection $selection -Text "适用对象：指导教师、项目成员、系统运维人员、毕业设计答辩材料编制人员" -FontName "宋体" -FontSize 12 -Alignment 1

    $selection.InsertBreak(7)

    Add-SectionTitle -Selection $selection -Text "1. 文档概述"
    Add-Paragraph -Selection $selection -Text "本文档用于说明《学习通教学管理系统》的技术架构、模块组成、数据库设计、部署方式与扩展建议。文档内容以当前仓库代码、配置文件及 SQL 脚本为依据整理，适合作为毕业设计项目的技术说明文档、项目交付资料与系统维护参考文档。"
    Add-Table -Document $document -Selection $selection -Headers @("项目", "内容") -Rows @(
        @("系统名称", $projectName),
        @("文档名称", $docTitle),
        @("系统版本", $projectVersion),
        @("基础框架", "RuoYi-Vue 3.8.9"),
        @("架构模式", "Spring Boot + Vue 前后端分离 B/S 架构"),
        @("文档依据", "仓库代码、Maven 配置、前端工程配置、SQL 结构脚本、演示数据脚本")
    )

    Add-SectionTitle -Selection $selection -Text "2. 项目简介"
    Add-Paragraph -Selection $selection -Text "本项目是在若依前后端分离框架基础上，面向高校教学业务场景进行二次开发形成的《学习通教学管理系统》。系统主要服务于课程教学管理过程，围绕教师、学生、学期、课程、授课安排、资源、选课、学习进度、作业、公告、消息和统计等业务对象建立完整的数据模型与管理流程。"
    Add-Paragraph -Selection $selection -Text "从业务定位上看，该系统兼具平台化管理能力与教学过程管理能力：一方面复用若依已有的用户、角色、菜单、字典、日志、监控、定时任务等基础能力；另一方面新增 14 张教学业务表和一组教育业务页面，实现对教学全过程的信息化支撑。"
    Add-Paragraph -Selection $selection -Text "系统当前预置了学生（student）、教师（teacher）、院系管理员（master）等角色，并保留若依管理员角色，用于覆盖教学平台的典型使用场景。"

    Add-SectionTitle -Selection $selection -Text "3. 技术栈与总体架构"
    Add-SubTitle -Selection $selection -Text "3.1 技术栈"
    Add-Table -Document $document -Selection $selection -Headers @("类别", "技术/组件", "说明") -Rows $techRows
    Add-SubTitle -Selection $selection -Text "3.2 架构说明"
    Add-Paragraph -Selection $selection -Text "系统整体采用典型的前后端分离架构。浏览器端访问 Vue 管理后台页面，页面通过 Axios 调用后端 REST 接口；后端由 Spring Boot 提供统一 Web 容器与控制器入口，业务逻辑在 Service 层编排，通过 MyBatis Mapper 访问 MySQL 数据库；登录态、验证码与缓存信息通过 Redis 管理；权限与身份认证由 Spring Security 与 JWT 协同实现。"
    Add-Paragraph -Selection $selection -Text "可简化理解为：浏览器/管理端 -> Vue 页面与组件 -> Axios 接口层 -> Spring Boot 控制器 -> Service 业务层 -> Mapper 数据访问层 -> MySQL/Redis。"

    Add-SectionTitle -Selection $selection -Text "4. 工程结构说明"
    Add-Table -Document $document -Selection $selection -Headers @("模块/目录", "职责定位", "主要内容") -Rows $moduleRows
    Add-Paragraph -Selection $selection -Text "在后端代码组织上，教学业务遵循若依标准分层模式：Domain 定义实体对象，Mapper 定义数据访问接口和 XML SQL 映射，Service 定义业务接口与实现，Controller 提供对外 REST API。前端则采用 views 页面、api 接口封装、components 公共组件的结构进行维护。"

    Add-SectionTitle -Selection $selection -Text "5. 业务模块说明"
    Add-Table -Document $document -Selection $selection -Headers @("模块分类/名称", "接口标识", "说明") -Rows $businessRows
    Add-Paragraph -Selection $selection -Text "除标准 CRUD 页面外，项目还提供教学统计页面。根据 EducationStatisticsMapper.xml，统计模块包含课程规模统计、教师工作量统计、已发布作业待批改统计等查询，为管理端提供教学概览能力。"

    Add-SectionTitle -Selection $selection -Text "6. 数据库设计说明"
    Add-SubTitle -Selection $selection -Text "6.1 数据库总体说明"
    Add-Paragraph -Selection $selection -Text "数据库层分为两部分：一部分沿用若依基础表，如 sys_user、sys_dept、sys_role、sys_user_role、sys_dict_type、sys_dict_data 等；另一部分为教学业务扩展表，由 sql/xuexitong_teaching_schema.sql 定义。该脚本共创建 14 张教学业务表，并在演示数据脚本中补充字典、角色与样例数据。"
    Add-SubTitle -Selection $selection -Text "6.2 主要数据表"
    Add-Table -Document $document -Selection $selection -Headers @("表名", "类型", "说明") -Rows $dbRows
    Add-SubTitle -Selection $selection -Text "6.3 核心关系说明"
    Add-NumberedLines -Selection $selection -Lines $relationLines
    Add-SubTitle -Selection $selection -Text "6.4 字典与角色设计"
    Add-Paragraph -Selection $selection -Text "根据 sql/xuexitong_teaching_demo_init.sql，系统为教学模块预置了课程类型、课程状态、选课状态、作业类型、公告类型、消息已读状态等字典类型，并预置 student、teacher、master 三类业务角色。这样可以保证前后端状态码、标签与权限控制保持一致。"

    Add-SectionTitle -Selection $selection -Text "7. 关键配置说明"
    Add-Table -Document $document -Selection $selection -Headers @("配置项", "当前值/示例", "来源") -Rows $configRows
    Add-Paragraph -Selection $selection -Text "需要特别说明的是，上表中的数据库账号、Druid 控制台账号和 JWT 密钥均为当前开发配置，仅用于本地调试与课程设计环境说明，正式部署时应立即替换。"

    Add-SectionTitle -Selection $selection -Text "8. 安全与权限设计"
    Add-NumberedLines -Selection $selection -Lines $securityLines

    Add-SectionTitle -Selection $selection -Text "9. 部署与运行说明"
    Add-SubTitle -Selection $selection -Text "9.1 运行环境建议"
    Add-Paragraph -Selection $selection -Text "后端建议环境为 JDK 1.8、Maven 3.6 及以上、MySQL 5.7 或更高版本、Redis 5.x/6.x；前端建议环境为 Node.js 与 npm。若部署在 Windows 环境，可按当前 application.yml 中的上传目录配置进行调整；若部署在 Linux 环境，应将 ruoyi.profile 改为 Linux 目录。"
    Add-SubTitle -Selection $selection -Text "9.2 初始化与部署步骤"
    Add-NumberedLines -Selection $selection -Lines $deploymentLines
    Add-SubTitle -Selection $selection -Text "9.3 运行访问说明"
    Add-Paragraph -Selection $selection -Text "开发模式下，前端默认运行在 80 端口，后端默认运行在 8080 端口，前端通过代理将 /dev-api 请求转发至后端。生产模式下，前端可部署为静态资源，后端以独立 Spring Boot Jar 方式运行，也可通过 Nginx、Apache 等服务器统一反向代理。"

    Add-SectionTitle -Selection $selection -Text "10. 开发规范与扩展建议"
    Add-NumberedLines -Selection $selection -Lines $extensionLines

    Add-SectionTitle -Selection $selection -Text "11. 项目特点与技术总结"
    Add-Paragraph -Selection $selection -Text "从技术实现上看，本项目的主要特点有三点：其一，复用若依成熟的权限、安全、日志、监控和生成器能力，大幅降低基础平台开发成本；其二，围绕教学业务形成了较完整的数据实体与业务闭环，支持从课程建模到学习过程跟踪的全过程管理；其三，项目结构清晰、模块边界明确，适合作为毕业设计项目展示《快速开发框架二次开发》的工程能力。"
    Add-Paragraph -Selection $selection -Text "从交付与维护角度看，本项目已经具备二次开发基础，后续可以继续向教师端、学生端、小程序端、数据可视化大屏、学习行为分析、消息推送联动等方向扩展。"

    Add-SectionTitle -Selection $selection -Text "12. 结论"
    Add-Paragraph -Selection $selection -Text "综上，学习通教学管理系统是一个基于 RuoYi-Vue 3.8.9 二次开发完成的前后端分离管理系统。项目采用 Java + Spring Boot + Vue 的主流技术路线，具备较好的工程规范性、模块化水平和可扩展性，能够支撑毕业设计中对系统设计、数据库设计、权限控制、业务管理和部署运行的综合技术说明需求。"

    $document.SaveAs([ref]$OutputPath)
}
finally {
    if ($document -ne $null) {
        $document.Close()
    }
    if ($word -ne $null) {
        $word.Quit()
    }
    [System.GC]::Collect()
    [System.GC]::WaitForPendingFinalizers()
}

