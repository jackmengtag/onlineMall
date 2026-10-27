# NovaCross 跨境电商平台（RuoYi-Cloud 微服务版）

基于 **RuoYi-Cloud 3.6.8**（Spring Boot 4.0.3 / Spring Cloud 2025.1.0 / Spring Cloud Alibaba 2025.1.0 / JDK 17+）构建的跨境电商平台，覆盖买家商城、运营后台、店铺中心、平台中心四端。

## 一、服务与端口

| 服务 | 模块 | 端口 | 说明 |
| --- | --- | --- | --- |
| 网关 | `ruoyi-gateway` | 8080 | 统一入口、鉴权、路由、验证码 |
| 认证中心 | `ruoyi-auth` | 9200 | 登录、登出、注册、令牌刷新 |
| 系统服务 | `ruoyi-modules/ruoyi-system` | 9201 | 用户、角色、菜单、字典、日志、公告 |
| 代码生成 | `ruoyi-modules/ruoyi-gen` | 9202 | 代码生成器 |
| 定时任务 | `ruoyi-modules/ruoyi-job` | 9203 | Quartz 任务调度 |
| 跨境电商 | `ruoyi-modules/ruoyi-commerce` | 9204 | 商品、订单、库存、定价、支付、履约、结算、买家端 |
| 文件服务 | `ruoyi-modules/ruoyi-file` | 9300 | 本地 / MinIO 文件存储 |
| 监控中心 | `ruoyi-visual/ruoyi-monitor` | 9100 | Spring Boot Admin |
| 注册/配置中心 | Nacos 3.0.2 | 8848 / 9848 / 8081 | 服务发现与配置中心，控制台 8081 |
| 数据库 | MySQL 8 | 3306 | 业务库 `ry-cloud`，Nacos 配置库 `ry-config` |
| 缓存 | Redis | 6379 | 登录态、验证码、买家令牌 |

## 二、模块结构

```
src
├── ruoyi-gateway          网关（路由 / 鉴权 / 验证码）
├── ruoyi-auth             认证服务
├── ruoyi-api
│   └── ruoyi-api-system   系统服务 Feign 接口与 LoginUser 模型
├── ruoyi-common           通用模块（core / redis / security / log / datasource / swagger ...）
├── ruoyi-modules
│   ├── ruoyi-system       系统管理服务
│   ├── ruoyi-gen          代码生成服务
│   ├── ruoyi-job          定时任务服务
│   ├── ruoyi-file         文件服务
│   └── ruoyi-commerce     跨境电商业务服务
├── ruoyi-visual           监控服务
├── ruoyi-ui               管理后台（Vue 3 + Element Plus + Vite）
├── mall-ui                买家商城（原生 HTML/CSS/JS）
├── sql                    初始化脚本（系统库、Nacos 配置库、跨境电商库）
└── tests                  测试资产
```

## 三、网关路由

| 路径 | 目标服务 |
| --- | --- |
| `/auth/**` | `ruoyi-auth` |
| `/code/**` | `ruoyi-gen` |
| `/schedule/**` | `ruoyi-job` |
| `/system/**` | `ruoyi-system` |
| `/file/**` | `ruoyi-file` |
| `/commerce/**`、`/api/**` | `ruoyi-commerce` |

`/api/**` 为买家端接口，使用买家自有令牌（`X-Customer-Token`），已加入网关白名单，不再校验若依 JWT。

## 四、数据库初始化

```bash
# 1. 业务库（系统表 + 跨境电商表 + 演示数据）
mysql -uroot -p123456 -e "CREATE DATABASE IF NOT EXISTS \`ry-cloud\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
mysql -uroot -p123456 ry-cloud < sql/ry_20260321.sql
mysql -uroot -p123456 ry-cloud < sql/commerce_ddl_mysql.sql
mysql -uroot -p123456 ry-cloud < sql/commerce_demo_data.sql
mysql -uroot -p123456 ry-cloud < sql/commerce_menu_dict_mysql.sql

# 2. Nacos 配置库（内置 application-dev.yml、ruoyi-gateway-dev.yml、ruoyi-commerce-dev.yml 等）
mysql -uroot -p123456 < sql/ry_config_20260311.sql
```

Nacos 使用 MySQL 作为配置存储时，需在 `conf/application.properties` 配置：

```properties
spring.sql.init.platform=mysql
db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/ry-config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
db.user.0=root
db.password.0=123456
nacos.core.auth.server.identity.key=ruoyi
nacos.core.auth.server.identity.value=ruoyi
nacos.core.auth.plugin.nacos.token.secret.key=VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=
nacos.console.port=8081
```

## 五、启动顺序

```bash
# 1. 中间件
#    MySQL、Redis 按本机环境启动
#    Nacos：bin/startup.cmd -m standalone（JDK 17+）

# 2. 后端（每个模块独立进程）
java -jar ruoyi-modules/ruoyi-system/target/ruoyi-modules-system.jar
java -jar ruoyi-modules/ruoyi-commerce/target/ruoyi-modules-commerce.jar
java -jar ruoyi-auth/target/ruoyi-auth.jar
java -jar ruoyi-gateway/target/ruoyi-gateway.jar

# 3. 前端
cd ruoyi-ui && npm install && npm run dev     # http://localhost:8090
cd mall-ui  && npm install && npm run dev     # http://localhost:8091
```

前端开发代理均指向网关 `http://localhost:8080`。

## 六、账号

| 端 | 账号 | 密码 |
| --- | --- | --- |
| 管理后台（超级管理员） | admin | admin123 |
| 店铺中心（店铺管理员） | shopadmin | admin123 |

## 七、从单体版迁移的说明

1. 原 `ruoyi-admin`、`ruoyi-framework`、`ruoyi-system`、`ruoyi-quartz`、`ruoyi-generator`、`ruoyi-common` 已替换为 RuoYi-Cloud 官方模块。
2. `ruoyi-commerce` 迁移为独立微服务 `ruoyi-modules-commerce`，包名仍为 `com.ruoyi.commerce`。
3. 权限注解由 `@PreAuthorize("@ss.hasPermi('x')")` 统一改为 `@RequiresPermissions("x")`。
4. 登录用户模型由 `LoginUser.getUser()` 调整为 `LoginUser.getSysUser()`。
5. 买家端 `@Anonymous` 改为网关白名单 `/api/**`。
6. 管理后台保留 Vue 3 版本（`ruoyi-ui`），以兼容已有跨境电商页面；登录、登出、验证码、路由接口已切换到 `/auth/**`、`/code`、`/system/user/getInfo`、`/system/menu/getRouters`。
7. 后端统一由网关 `8080` 访问，前端不再直连业务服务。

## 八、构建

```bash
# 全量打包（跳过测试）
mvn -DskipTests package

# 仅构建跨境电商服务
mvn -pl ruoyi-modules/ruoyi-commerce -am -DskipTests package
```
