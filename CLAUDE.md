# QK-Money

前后端分离的后台管理快速开发框架。二次开发即在下列两个目录内编写业务代码，与框架代码同目录并存。

| 目录 | 内容 | 技术栈 |
|---|---|---|
| `qk-money/` | 后端 | Java 17、Spring Boot 3、MyBatis-Plus、Maven 多模块 |
| `qk-money-ui/` | 前端 | Vue 3、Vite、Element Plus、TailwindCSS、Pinia |

部署与启动见 `README.md`。两个目录的结构、命令、约定及文档索引，见各自目录下的 `CLAUDE.md`。

## 功能开发入口

| 场景 | 方式 |
|---|---|
| 单个管理功能（列表 / 增删改查） | `/qk-dev` skill：需求澄清 → 设计 → 编码；各文件的写法规范由该 skill 提供 |
| 系统级需求（收银、进销存等） | 先拆分为子模块，再逐个走 `/qk-dev` |

## 建库

仓库根目录的建库脚本：框架表位于 `-- >>> qk-money >>>` 与 `-- <<< qk-money <<<` 之间，业务表添加在该区间之外。
