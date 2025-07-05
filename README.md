# 游戏论坛管理系统
课后练手作业：游戏论坛项目

基于Java+Springboot+vue的游戏论坛商城管理系统

## 项目简介

本项目是基于**若依框架**前后端分版二次开发的游戏论坛管理系统。

最初计划开发一个类似小黑盒迷你版的网页项目，但开发过程中作者懒癌发作将商城部分功能废弃。不过数据库中相关表结构（`biz_cdk`系列表）仍然保留，有兴趣的开发者可以基于现有数据库结构继续完善商城功能。

## 技术栈

- **后端**: SpringBoot + MyBatis + Java
- **前端**: Vue3 + Vite
- **数据库**: MySQL
- **缓存**: Redis

## 运行环境要求

- **Node.js**: 最新稳定版本
- **MySQL**: 数据库服务
- **JDK**: 版本17（不得高于17）
- **Redis**: 缓存服务（若依框架必需）

### Redis 安装指南

如果对Redis不熟悉，可以参考以下视频教程快速上手： [Redis Windows服务端的安装配置及测试](https://www.bilibili.com/video/BV1Z5411y7sC/?share_source=copy_web&vd_source=a1706ffb383303429274dfc19fc58b2a)

*观看视频列表中的3个短视频（每个1-2分钟）即可掌握基本使用*

## 数据库配置

数据库文件位于 `SQL` 文件夹中，使用 Navicat 导入即可。

> **注意**: 若依自带的 `qrtz` 相关表未删除，如不需要可手动删除。

## 配置修改

在运行项目前，需要修改以下配置：

### 1. 数据库配置

**文件位置**: `ruoyi-admin/src/main/resources/application-druid.yml`

修改以下内容：

- 数据库地址
- 用户名
- 密码

### 2. 图片保存路径

**文件位置**: `ruoyi-user/src/main/java/com/ruoyi/user/controller/PostController.java`

修改第494行的 `path` 变量，设置为您的本地图片保存路径。

## 项目启动

### 前置准备

1. 使用**管理员身份**打开 IDEA
2. 在终端中分别进入前端项目目录安装依赖：

```bash
# 进入管理端前端目录
cd ruoyi-admin-ui
npm i

# 进入用户端前端目录  
cd ruoyi-user-ui
npm i
```

### 启动方式

#### 用户端启动

```bash
# 在 ruoyi-user-ui 目录下执行
npm run serve:user
```

#### 管理端启动

```bash
# 在 ruoyi-admin-ui 目录下执行
npm run dev
```

## 项目特点以及缺陷

- 用户端(`ruoyi-user-ui`)可以作为独立项目运行
- 管理端暂时不支持图片管理功能（原设计未考虑，后续懒得修改）
- 基于成熟的若依框架，稳定性较好

## 技术细节说明

### 评论状态关联

评论功能的状态关联最初是直接使用数据库触发器完成的，后来意识到应该在后端处理，但由于项目已接近收尾阶段就懒得修改了😄。

如果您希望删除这些触发器改为后端处理，可以使用以下SQL命令：

```sql
-- 删除已存在的触发器
DROP TRIGGER IF EXISTS `tr_section_status_update`;
DROP TRIGGER IF EXISTS `tr_post_status_update`;
DROP TRIGGER IF EXISTS `tr_post_insert_status_check`;
DROP TRIGGER IF EXISTS `tr_comment_insert_status_check`;
```

## 后续改进路线

### 技术栈方面

1. **完善商城功能**：基于现有的`biz_cdk`系列数据表，继续完善商城功能，提高系统的完整性和实用性。
2. **添加图片管理功能**：在管理端添加图片管理功能，方便管理员对图片进行上传、删除、修改等操作。
3. **更新 JDK 版本**：随着 JDK 的不断更新，考虑升级 JDK 版本，以获取更好的性能和功能。
4. **引入微服务架构**：如果系统规模不断扩大，可以考虑引入微服务架构，将系统拆分成多个小型的、自治的服务，提高系统的可扩展性和容错性。

### UI设计方面

**设计风格统一化**：当前UI设计秉承"能用就行"的原则，最初登录页面设计得比较花哨，但后续主页开发时为了简化工作量采用了简洁风格，导致整体设计风格不够统一。后续开发建议：

- 制定统一的设计规范和色彩体系
- 重新梳理登录页面和主页的视觉风格，保持一致性
- 逐步美化各个页面，提升用户体验
- 考虑引入成熟的UI组件库来保证设计的一致性

## 扩展说明

如需完善商城功能，可以基于现有的 `biz_cdk` 系列数据表进行开发，相关表结构已在数据库中保留。