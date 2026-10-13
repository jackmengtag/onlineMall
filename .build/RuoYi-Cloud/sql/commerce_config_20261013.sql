-- ============================================================================
-- 跨境电商平台 · Nacos 配置与网关路由（对应 ruoyi-commerce 模块）
-- 说明：若依微服务配置存放于 Nacos（见 sql/ry_config_20260918.sql 的 config_info 表）。
--       执行前请确认已导入 ry_config_20260918.sql；数据库、账号、密码请按实际环境调整。
-- ============================================================================

-- 1. 新增 ruoyi-commerce 模块配置（数据库 ry-commerce，对应文档中的 commerce schema）
INSERT INTO config_info(id, data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key)
VALUES (10, 'ruoyi-commerce-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  data:\n    redis:\n      host: localhost\n      port: 6379\n      password:\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-commerce?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: password\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.commerce\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'跨境交易闭环接口文档\'\n    # 描述\n    description: \'跨境交易闭环接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '9e9b3c0f119ae5b182daef4096311319', NOW(), NOW(), 'admin', '127.0.0.1', '', 'public', '跨境交易闭环', NULL, NULL, 'yaml', NULL, '');

-- 2. 为网关 ruoyi-gateway-dev.yml 增加 /commerce/** 路由
--    StripPrefix=1 后转发到 ruoyi-commerce，例如 GET /commerce/order/list -> ruoyi-commerce:/order/list
UPDATE config_info
SET content = REPLACE(content,
'            # 系统模块\n            - id: ruoyi-system\n              uri: lb://ruoyi-system\n              predicates:\n                - Path=/system/**\n              filters:\n                - StripPrefix=1\n',
'            # 系统模块\n            - id: ruoyi-system\n              uri: lb://ruoyi-system\n              predicates:\n                - Path=/system/**\n              filters:\n                - StripPrefix=1\n            # 跨境交易闭环\n            - id: ruoyi-commerce\n              uri: lb://ruoyi-commerce\n              predicates:\n                - Path=/commerce/**\n              filters:\n                - StripPrefix=1\n')
WHERE data_id = 'ruoyi-gateway-dev.yml' AND group_id = 'DEFAULT_GROUP';

-- 3. 限流策略（可选）：为跨境电商模块增加 Sentinel 网关限流规则
UPDATE config_info
SET content = REPLACE(content,
'	{\n        \"resource\": \"ruoyi-job\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    }',
'	{\n        \"resource\": \"ruoyi-job\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"ruoyi-commerce\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    }')
WHERE data_id = 'sentinel-ruoyi-gateway' AND group_id = 'DEFAULT_GROUP';
