-- ============================================================================
-- NovaCross 四端演示数据（MySQL 8 / 库 ry-cloud）
-- 依赖：ry_20260417.sql、quartz.sql、commerce_ddl_mysql.sql、commerce_menu_dict_mysql.sql
-- ============================================================================

USE `ry-cloud`;
SET NAMES utf8mb4;

DELETE FROM audit_log;
DELETE FROM shop_settlement;
DELETE FROM shop_user;
DELETE FROM shop;
DELETE FROM cart_item;
DELETE FROM cart;
DELETE FROM channel_listing;
DELETE FROM channel_config;
DELETE FROM platform_config;
DELETE FROM promotion;
DELETE FROM coupon;
DELETE FROM sku_price;
DELETE FROM inventory_transaction;
DELETE FROM inventory;
DELETE FROM product_sku;
DELETE FROM product_spu;
DELETE FROM customer_address;
DELETE FROM customer;

INSERT INTO shop (shop_id, shop_code, shop_name, logo_url, contact_name, contact_email, country_code, currency_code, shop_type, commission_rate, status, settled_amount, pending_amount, create_by, create_time, update_by, update_time, remark) VALUES
(1,'SHOP-NOVA','NovaCross 诺瓦跨境优选','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=minimal%20letter%20N%20logo%20teal&image_size=square','Alice Chen','ops@novacross.com','US','USD','SELF',0.0300,'ACTIVE',1289000,186400,'admin',NOW(),'admin',NOW(),'平台自营旗舰店'),
(2,'SHOP-AURORA','Aurora Home 家居生活馆','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=minimal%20home%20logo%20beige&image_size=square','Mark Lee','mark@aurora-home.com','DE','EUR','THIRD_PARTY',0.0800,'ACTIVE',420600,58200,'admin',NOW(),'admin',NOW(),'三方家居店铺'),
(3,'SHOP-TRAIL','TrailNest 户外装备','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=outdoor%20mountain%20logo%20green&image_size=square','Nina Park','nina@trailnest.com','SG','USD','THIRD_PARTY',0.0700,'SUSPENDED',98000,12400,'admin',NOW(),'admin',NOW(),'户外垂类店铺');

INSERT INTO shop_settlement (settlement_no, shop_id, period_start, period_end, order_count, gross_amount, commission_amount, net_amount, status, paid_at, create_by, create_time, update_by, update_time, remark) VALUES
('STL20261001001',1,'2026-10-01','2026-10-15',326,4289000,128670,4160330,'PAID','2026-10-18 10:00:00','admin',NOW(),'admin',NOW(),'上半月结算'),
('STL20261016001',1,'2026-10-16','2026-10-23',118,1864000,55920,1808080,'PENDING',NULL,'admin',NOW(),'admin',NOW(),'待打款'),
('STL20261001002',2,'2026-10-01','2026-10-15',64,582000,46560,535440,'PENDING',NULL,'admin',NOW(),'admin',NOW(),'三方店铺结算');

INSERT INTO platform_config (config_key, config_value, config_group, create_by, create_time, update_by, update_time, remark) VALUES
('platform.name','NovaCross 跨境优选','GENERAL','admin',NOW(),'admin',NOW(),'平台名称'),
('platform.default_currency','USD','GENERAL','admin',NOW(),'admin',NOW(),'默认币种'),
('order.payment_timeout_minutes','30','ORDER','admin',NOW(),'admin',NOW(),'支付超时自动取消'),
('order.auto_complete_days','15','ORDER','admin',NOW(),'admin',NOW(),'妥投后自动完成'),
('inventory.safety_stock','50','INVENTORY','admin',NOW(),'admin',NOW(),'默认安全库存'),
('settlement.commission_rate','0.05','SETTLEMENT','admin',NOW(),'admin',NOW(),'平台默认佣金'),
('aftersale.window_days','30','AFTER_SALE','admin',NOW(),'admin',NOW(),'售后申请时限'),
('security.risk_score_threshold','80','RISK','admin',NOW(),'admin',NOW(),'风控拦截阈值');

INSERT INTO channel_config (channel_code, channel_name, channel_type, country_code, currency_code, endpoint, app_key, status, last_sync_time, create_by, create_time, update_by, update_time, remark) VALUES
('DIRECT','独立站 NovaCross.com','DIRECT','US','USD','https://api.novacross.com','nc-direct','ACTIVE',NOW(),'admin',NOW(),'admin',NOW(),'自营商城'),
('AMAZON','Amazon 北美站','PLATFORM','US','USD','https://sellingpartnerapi-na.amazon.com','amz-us','ACTIVE',NOW(),'admin',NOW(),'admin',NOW(),''),
('AMAZON-DE','Amazon 德国站','PLATFORM','DE','EUR','https://sellingpartnerapi-eu.amazon.com','amz-de','ACTIVE',NOW(),'admin',NOW(),'admin',NOW(),''),
('TIKTOK','TikTok Shop SG','SOCIAL','SG','USD','https://open-api.tiktokglobalshop.com','tt-sg','PAUSED',NULL,'admin',NOW(),'admin',NOW(),''),
('SHOPEE','Shopee 新加坡','PLATFORM','SG','USD','https://partner.shopeemobile.com','sp-sg','DISABLED',NULL,'admin',NOW(),'admin',NOW(),'');

INSERT INTO customer (customer_no, email, password, phone, display_name, country_code, locale, status, create_by, create_time, update_by, update_time, remark) VALUES
('CUS20261022001','alice@example.com',(select password from (select password from sys_user where user_name='admin') t),'+1-415-555-0101','Alice Chen','US','en-US','ACTIVE','admin',NOW(),'admin',NOW(),'演示买家'),
('CUS20261022002','bob@example.de',(select password from (select password from sys_user where user_name='admin') t),'+49-30-555-0102','Bob Müller','DE','de-DE','ACTIVE','admin',NOW(),'admin',NOW(),'演示买家'),
('CUS20261022003','carol@example.sg',(select password from (select password from sys_user where user_name='admin') t),'+65-6123-4567','Carol Tan','SG','en-SG','ACTIVE','admin',NOW(),'admin',NOW(),'演示买家');

INSERT INTO customer_address (customer_id, recipient_name, recipient_phone, country_code, state_code, city, district, address_line1, address_line2, postal_code, is_default, create_by, create_time, update_by, update_time) VALUES
(1,'Alice Chen','+1-415-555-0101','US','CA','San Francisco','SoMa','1 Market Street','Apt 1201','94105',1,'admin',NOW(),'admin',NOW()),
(2,'Bob Müller','+49-30-555-0102','DE','BE','Berlin','Mitte','Friedrichstrasse 100',NULL,'10117',1,'admin',NOW(),'admin',NOW()),
(3,'Carol Tan','+65-6123-4567','SG','SG','Singapore','Jurong','10 Jurong East Street 12',NULL,'609690',1,'admin',NOW(),'admin',NOW());

-- 商品（含前台展示字段，来自 cross-border-prototype/assets/mock.js）
INSERT INTO product_spu (shop_id, spu_code, brand_name, category_key, category_id, category_name, default_name, name_en, default_description, status, default_image_url, badge, rating, review_count, sold_count, tags_json, images_json, specs_json, colors_json, sizes_json, create_by, create_time, update_by, update_time, remark) VALUES
(1,'SPU-100231','NovaSound','audio',1001,'影音数码','NovaSound 头戴式主动降噪耳机','NovaSound ANC Headphones','双馈主动降噪与 40 小时续航，支持多点连接与低延迟游戏模式，为跨境通勤与长途飞行打造。','ON_SALE','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20over-ear%20active%20noise%20cancelling%20headphones%20matte%20graphite%20product%20photo&image_size=square_hd','热销',4.80,1264,2300,'["双馈降噪","40h 续航","多点连接"]','["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20over-ear%20anc%20headphones%20product%20photo&image_size=square_hd","https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20headphones%20angled%20view%20studio&image_size=square_hd"]','{"蓝牙版本":"5.3","续航":"40 小时（ANC 开启）","单元":"40mm 动圈","充电":"USB-C / 快充 10 分钟听 5 小时","重量":"268g","防水":"IPX4"}','["石墨黑","雾白","黛蓝"]','[]','admin',NOW(),'admin',NOW(),'原型 P001'),
(1,'SPU-100244','PulseFit','smart',1002,'智能穿戴','PulseFit 智能运动手表 S3','PulseFit Smart Watch S3','1.43 英寸 AMOLED 屏，双频 GPS 与血氧监测，14 天超长续航，泳池级防水。','ON_SALE','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20smartwatch%20black%20sport%20band%20amoled%20product%20photo&image_size=square_hd','新品',4.70,872,1600,'["血氧监测","双频 GPS","14 天续航"]','["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20smartwatch%20black%20sport%20band&image_size=square_hd"]','{"屏幕":"1.43 AMOLED","续航":"14 天","定位":"双频 GPS","防水":"5ATM","传感器":"心率 / 血氧 / 睡眠","重量":"46g"}','["曜石黑","星光银"]','["42mm","46mm"]','admin',NOW(),'admin',NOW(),'原型 P002'),
(1,'SPU-100260','FreshGo','home',1003,'家居厨房','FreshGo 便携充电榨汁杯','FreshGo Portable Blender','六叶立体刀头 30 秒破壁，USB-C 充电，400ml 随行杯体。','ON_SALE','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=portable%20usb%20rechargeable%20blender%20bottle%20with%20smoothie&image_size=square_hd','',4.50,2310,5800,'["六叶刀头","USB-C","随身携带"]','["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=portable%20blender%20bottle%20smoothie&image_size=square_hd"]','{"容量":"400ml","刀头":"六叶 304 不锈钢","充电":"USB-C","续航":"约 15 杯","重量":"430g","材质":"Tritan 杯体"}','["奶白","薄荷绿"]','[]','admin',NOW(),'admin',NOW(),'原型 P003'),
(3,'SPU-100275','TrailNest','outdoor',1004,'户外运动','TrailNest 自动速开露营帐篷','TrailNest Auto Camping Tent','3 秒液压速开，PU3000 防水涂层与双层结构，3-4 人适用。','ON_SALE','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=automatic%20pop-up%20camping%20tent%20on%20grass%20golden%20hour&image_size=square_hd','大件',4.60,486,900,'["3 秒速开","PU3000 防水","双层结构"]','["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=camping%20tent%20outdoor%20product&image_size=square_hd"]','{"适用人数":"3-4 人","展开尺寸":"240×210×135cm","防水":"PU3000mm","面料":"190T 银胶","重量":"2.9kg","收纳":"78×18×18cm"}','["苔绿","沙棕"]','["3-4 人"]','admin',NOW(),'admin',NOW(),'原型 P004'),
(1,'SPU-100288','CleanMate','home',1003,'家居厨房','CleanMate 激光导航扫地机器人','CleanMate Lidar Robot Vacuum','LDS 激光导航与 4000Pa 强吸力，自动集尘基站，支持多层地图。','ON_SALE','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=white%20lidar%20robot%20vacuum%20modern%20living%20room&image_size=square_hd','高客单',4.70,731,1100,'["激光导航","自动集尘","4000Pa"]','["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=robot%20vacuum%20white%20floor&image_size=square_hd"]','{"吸力":"4000Pa","导航":"LDS 激光","电池":"5200mAh","集尘":"自动集尘 60 天","越障":"2cm","噪音":"≤65dB"}','["瓷白"]','[]','admin',NOW(),'admin',NOW(),'原型 P005'),
(1,'SPU-100301','KeyForge','audio',1001,'影音数码','KeyForge 三模机械键盘 87 键','KeyForge Mechanical Keyboard 87','Gasket 结构 + 热插拔轴座，三模连接，87 键紧凑布局。','ON_SALE','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=compact%2087-key%20mechanical%20keyboard%20white%20keycaps&image_size=square_hd','',4.90,1580,3200,'["三模连接","热插拔","Gasket 结构"]','["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mechanical%20keyboard%20white%20desk&image_size=square_hd"]','{"配列":"87 键","连接":"有线 / 2.4G / 蓝牙","轴体":"热插拔","结构":"Gasket","电池":"4000mAh","重量":"880g"}','["奶白","深空灰"]','["青轴","茶轴","静音红轴"]','admin',NOW(),'admin',NOW(),'原型 P006'),
(2,'SPU-100318','Aurea','home',1003,'家居厨房','Aurea 陶瓷香薰加湿器','Aurea Ceramic Aroma Diffuser','超声波雾化，七彩氛围灯，静音运行。','ON_SALE','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=minimal%20ceramic%20aroma%20diffuser%20mist%20product%20photo&image_size=square_hd','',4.40,940,2100,'["超声波雾化","七彩氛围灯","静音"]','["https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ceramic%20aroma%20diffuser%20neutral%20background&image_size=square_hd"]','{"容量":"300ml","雾化":"超声波","灯光":"七彩氛围灯","噪音":"≤30dB","重量":"520g","材质":"陶瓷"}','["米白","岩灰"]','[]','admin',NOW(),'admin',NOW(),'原型 P007');

INSERT INTO product_sku (spu_id, sku_code, barcode, attributes_json, weight_grams, length_mm, width_mm, height_mm, origin_country_code, hs_code, material, battery_type, dangerous_goods_flag, declared_name, declared_value_amount, declared_currency_code, status, create_by, create_time, update_by, update_time) VALUES
(1,'SKU-100231-GR','6900000000231','{"color":"石墨黑"}',268,180,170,80,'CN','8518300000','ABS',"LITHIUM",1,'Wireless Headphones',3850,'USD','ACTIVE','admin',NOW(),'admin',NOW()),
(2,'SKU-100244-BK','6900000000244','{"color":"曜石黑","size":"46mm"}',46,45,38,11,'CN','8517620000','Aluminum',"LITHIUM",1,'Smart Watch',5200,'USD','ACTIVE','admin',NOW(),'admin',NOW()),
(3,'SKU-100260-WH','6900000000260','{"color":"奶白"}',430,90,90,220,'CN','8509400000','Tritan',"LITHIUM",1,'Portable Blender',1280,'USD','ACTIVE','admin',NOW(),'admin',NOW()),
(4,'SKU-100275-GN','6900000000275','{"color":"苔绿","size":"3-4 人"}',2900,780,180,180,'CN','6306220000','Polyester','NONE',0,'Camping Tent',3400,'USD','ACTIVE','admin',NOW(),'admin',NOW()),
(5,'SKU-100288-WH','6900000000288','{"color":"瓷白"}',3600,350,350,100,'CN','8508110000','ABS',"LITHIUM",1,'Robot Vacuum',9600,'USD','ACTIVE','admin',NOW(),'admin',NOW()),
(6,'SKU-100301-WH','6900000000301','{"color":"奶白","axis":"茶轴"}',880,360,140,40,'CN','8471606000','ABS',"LITHIUM",1,'Mechanical Keyboard',2600,'USD','ACTIVE','admin',NOW(),'admin',NOW()),
(7,'SKU-100318-CE','6900000000318','{"color":"米白"}',520,120,120,160,'CN','8479899990','Ceramic','NONE',0,'Aroma Diffuser',960,'USD','ACTIVE','admin',NOW(),'admin',NOW());

INSERT INTO sku_price (sku_id, country_code, channel_code, currency_code, list_amount, sale_amount, effective_from, effective_to, status, create_by, create_time, update_by, update_time) VALUES
(1,'US','DIRECT','USD',15999,12999,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(1,'DE','DIRECT','EUR',14999,11999,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(1,'CN','DIRECT','CNY',109900,89900,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(2,'US','DIRECT','USD',12999,9999,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(2,'DE','DIRECT','EUR',12499,9499,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(2,'CN','DIRECT','CNY',89900,69900,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(3,'US','DIRECT','USD',4599,3599,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(3,'DE','DIRECT','EUR',4299,3399,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(3,'CN','DIRECT','CNY',31900,24900,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(4,'US','DIRECT','USD',11999,8999,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(4,'DE','DIRECT','EUR',11499,8499,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(4,'CN','DIRECT','CNY',83900,62900,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(5,'US','DIRECT','USD',29999,24999,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(5,'DE','DIRECT','EUR',28999,23999,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(5,'CN','DIRECT','CNY',209900,179900,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(6,'US','DIRECT','USD',8999,6999,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(6,'DE','DIRECT','EUR',8499,6599,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(6,'CN','DIRECT','CNY',62900,49900,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(7,'US','DIRECT','USD',5299,4299,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(7,'DE','DIRECT','EUR',4999,3999,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
(7,'CN','DIRECT','CNY',36900,29900,'2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW());

INSERT INTO inventory (shop_id, sku_id, warehouse_code, country_code, total_qty, locked_qty, safety_qty, version, create_by, create_time, update_by, update_time, remark) VALUES
(1,1,'US-WEST','US',320,0,20,0,'admin',NOW(),'admin',NOW(),'美国仓'),
(1,1,'DE-FRA','DE',140,0,10,0,'admin',NOW(),'admin',NOW(),'德国仓'),
(1,1,'SG-SIN','SG',0,0,5,0,'admin',NOW(),'admin',NOW(),'新加坡仓'),
(1,1,'CN-SZ','CN',860,0,50,0,'admin',NOW(),'admin',NOW(),'中国仓'),
(1,2,'US-WEST','US',210,0,20,0,'admin',NOW(),'admin',NOW(),''),
(1,2,'DE-FRA','DE',96,0,10,0,'admin',NOW(),'admin',NOW(),''),
(1,2,'SG-SIN','SG',150,0,10,0,'admin',NOW(),'admin',NOW(),''),
(1,2,'CN-SZ','CN',1240,0,50,0,'admin',NOW(),'admin',NOW(),''),
(1,3,'US-WEST','US',640,0,30,0,'admin',NOW(),'admin',NOW(),''),
(1,3,'DE-FRA','DE',388,0,20,0,'admin',NOW(),'admin',NOW(),''),
(1,3,'SG-SIN','SG',220,0,10,0,'admin',NOW(),'admin',NOW(),''),
(1,3,'CN-SZ','CN',3200,0,100,0,'admin',NOW(),'admin',NOW(),''),
(3,4,'US-WEST','US',88,0,10,0,'admin',NOW(),'admin',NOW(),''),
(3,4,'DE-FRA','DE',42,0,5,0,'admin',NOW(),'admin',NOW(),''),
(3,4,'SG-SIN','SG',0,0,5,0,'admin',NOW(),'admin',NOW(),''),
(3,4,'CN-SZ','CN',460,0,30,0,'admin',NOW(),'admin',NOW(),''),
(1,5,'US-WEST','US',120,0,10,0,'admin',NOW(),'admin',NOW(),''),
(1,5,'DE-FRA','DE',64,0,5,0,'admin',NOW(),'admin',NOW(),''),
(1,5,'SG-SIN','SG',30,0,3,0,'admin',NOW(),'admin',NOW(),''),
(1,5,'CN-SZ','CN',380,0,20,0,'admin',NOW(),'admin',NOW(),''),
(1,6,'US-WEST','US',260,0,20,0,'admin',NOW(),'admin',NOW(),''),
(1,6,'DE-FRA','DE',180,0,10,0,'admin',NOW(),'admin',NOW(),''),
(1,6,'SG-SIN','SG',90,0,5,0,'admin',NOW(),'admin',NOW(),''),
(1,6,'CN-SZ','CN',1520,0,60,0,'admin',NOW(),'admin',NOW(),''),
(2,7,'US-WEST','US',420,0,20,0,'admin',NOW(),'admin',NOW(),''),
(2,7,'DE-FRA','DE',260,0,10,0,'admin',NOW(),'admin',NOW(),''),
(2,7,'SG-SIN','SG',180,0,10,0,'admin',NOW(),'admin',NOW(),''),
(2,7,'CN-SZ','CN',2100,0,80,0,'admin',NOW(),'admin',NOW(),'');

INSERT INTO channel_listing (channel_code, external_product_id, external_sku_id, sku_id, listing_status, payload_json, last_sync_time, create_by, create_time, update_by, update_time) VALUES
('AMAZON','B0NOVA0231','B0NOVA0231-GR',1,'LIVE','{"asin":"B0NOVA0231","marketplace":"US"}',NOW(),'admin',NOW(),'admin',NOW()),
('AMAZON','B0NOVA0244','B0NOVA0244-BK',2,'LIVE','{"asin":"B0NOVA0244","marketplace":"US"}',NOW(),'admin',NOW(),'admin',NOW()),
('AMAZON-DE','B0NOVA0260','B0NOVA0260-WH',3,'PENDING','{"asin":"B0NOVA0260","marketplace":"DE"}',NULL,'admin',NOW(),'admin',NOW()),
('TIKTOK','TT-100288','TT-100288-WH',5,'DRAFT','{"shopId":"sg-demo"}',NULL,'admin',NOW(),'admin',NOW()),
('SHOPEE','SP-100318','SP-100318-CE',7,'LIVE','{"shopId":"sg-demo"}',NOW(),'admin',NOW(),'admin',NOW());

INSERT INTO coupon (shop_id, coupon_code, coupon_name, coupon_type, discount_value, min_amount, currency_code, country_code, start_time, end_time, total_qty, used_qty, per_user_limit, status, create_by, create_time, update_by, update_time) VALUES
(1,'WELCOME10','新客立减 10 美元','FIXED',1000,5000,'USD','US','2026-01-01 00:00:00','2027-12-31 23:59:59',1000,12,1,'ACTIVE','admin',NOW(),'admin',NOW()),
(1,'SAVE15','全场 85 折','PERCENT',15,0,'USD','US','2026-01-01 00:00:00','2027-12-31 23:59:59',0,48,3,'ACTIVE','admin',NOW(),'admin',NOW()),
(NULL,'HOME20','平台家居满减 20','FIXED',2000,15000,'USD',NULL,'2026-09-01 00:00:00','2026-12-31 23:59:59',500,6,1,'ACTIVE','admin',NOW(),'admin',NOW());

INSERT INTO promotion (shop_id, promotion_code, promotion_name, promotion_type, scope_type, scope_value, discount_value, threshold_amount, currency_code, start_time, end_time, status, create_by, create_time, update_by, update_time) VALUES
(1,'PROMO-FALL20','秋季全场 8 折','DISCOUNT','ALL',NULL,20,0,'USD','2026-10-01 00:00:00','2026-10-31 23:59:59','ACTIVE','admin',NOW(),'admin',NOW()),
(1,'PROMO-FREESHIP','满 99 免运费','FULL_REDUCTION','ALL',NULL,990,9900,'USD','2026-10-01 00:00:00','2026-12-31 23:59:59','ACTIVE','admin',NOW(),'admin',NOW()),
(2,'PROMO-HOME15','家居专场 85 折','DISCOUNT','CATEGORY','home',15,0,'EUR','2026-10-10 00:00:00','2026-11-15 23:59:59','ACTIVE','admin',NOW(),'admin',NOW());

INSERT INTO tax_rule (country_code, category_id, tax_type, rule_name, rate, version, effective_from, effective_to, status, create_by, create_time, update_by, update_time) VALUES
('US',NULL,'VAT','US Sales Tax',0.080000,'TAX-US-2026','2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
('DE',NULL,'VAT','Germany VAT',0.190000,'TAX-DE-2026','2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
('SG',NULL,'GST','Singapore GST',0.090000,'TAX-SG-2026','2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
('CN',NULL,'VAT','China VAT',0.130000,'TAX-CN-2026','2026-01-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW());

INSERT INTO exchange_rate (base_currency, quote_currency, rate, version, effective_from, effective_to, status, create_by, create_time, update_by, update_time) VALUES
('USD','CNY',7.200000000000,'FX-2026-10','2026-10-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
('EUR','CNY',7.800000000000,'FX-2026-10','2026-10-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW()),
('USD','EUR',0.920000000000,'FX-2026-10','2026-10-01 00:00:00',NULL,'ACTIVE','admin',NOW(),'admin',NOW());

-- 店铺账号 shopadmin / admin123，绑定店铺 1
DELETE FROM sys_user_role WHERE user_id = 100;
DELETE FROM sys_user WHERE user_id = 100;
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time, update_by, update_time, remark)
VALUES (100, 103, 'shopadmin', 'NovaCross 店长', '00', 'shopadmin@novacross.com', '13800000000', '0', '', (select password from (select password from sys_user where user_name='admin') t), '0', '0', '', NULL, sysdate(), 'admin', sysdate(), '', NULL, '店铺中心演示账号');

DELETE FROM sys_role WHERE role_id = 3;
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES (3, '店铺管理员', 'shop', 3, '1', 1, 1, '0', '0', 'admin', sysdate(), '', NULL, '店铺中心角色');

INSERT INTO sys_user_role (user_id, role_id) VALUES (100, 3);
DELETE FROM sys_role_menu WHERE role_id = 3;
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 3, menu_id FROM sys_menu WHERE menu_id BETWEEN 3100 AND 3199;

INSERT INTO shop_user (shop_id, user_id, shop_role, status, create_time) VALUES (1, 100, 'OWNER', 'ACTIVE', NOW());

-- 演示订单（覆盖已支付/已完成/待支付，便于看板与列表展示）
INSERT INTO orders (order_id, order_no, shop_id, customer_id, channel_code, country_code, currency_code, subtotal_amount, discount_amount, shipping_amount, tax_amount, total_amount, order_status, payment_status, fulfillment_status, after_sale_status, placed_at, paid_at, completed_at, create_by, create_time, update_by, update_time, remark) VALUES
(1,'NC-20261024-1001',1,1,'DIRECT','US','USD',12999,0,0,1040,14039,'SHIPPED','PAID','FULFILLED','NONE','2026-10-24 09:12:00','2026-10-24 09:13:10',NULL,'admin',NOW(),'admin',NOW(),'演示订单'),
(2,'NC-20261023-1002',1,2,'DIRECT','DE','EUR',23998,2000,0,4180,26178,'COMPLETED','PARTIALLY_REFUNDED','FULFILLED','RESOLVED','2026-10-23 14:20:00','2026-10-23 14:21:05','2026-10-25 10:00:00','admin',NOW(),'admin',NOW(),'演示订单'),
(3,'NC-20261024-1003',2,3,'SHOPEE','SG','USD',4299,0,990,476,5765,'PENDING_PAYMENT','UNPAID','UNFULFILLED','NONE','2026-10-24 11:05:00',NULL,NULL,'admin',NOW(),'admin',NOW(),'演示订单');

INSERT INTO order_items (order_item_id, order_id, sku_id, sku_code, quantity, unit_price_amount, discount_amount, tax_amount, line_total_amount, currency_code, create_time) VALUES
(1,1,1,'SKU-100231-GR',1,12999,0,1040,14039,'USD',NOW()),
(2,2,2,'SKU-100244-BK',2,11999,2000,4180,26178,'EUR',NOW()),
(3,3,7,'SKU-100318-CE',1,4299,0,476,5765,'USD',NOW());

INSERT INTO order_item_snapshots (order_item_id, product_name, sku_code, attributes_json, image_url, origin_country_code, hs_code, declared_name, declared_value_amount, declared_currency_code, unit_price_amount, currency_code, create_time) VALUES
(1,'NovaSound 头戴式主动降噪耳机','SKU-100231-GR','{"color":"石墨黑"}','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20headphones%20product&image_size=square_hd','CN','8518300000','Wireless Headphones',3850,'USD',12999,'USD',NOW()),
(2,'PulseFit 智能运动手表 S3','SKU-100244-BK','{"color":"曜石黑","size":"46mm"}','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartwatch%20product&image_size=square_hd','CN','8517620000','Smart Watch',5200,'USD',11999,'EUR',NOW()),
(3,'Aurea 陶瓷香薰加湿器','SKU-100318-CE','{"color":"米白"}','https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=aroma%20diffuser%20product&image_size=square_hd','CN','8479899990','Aroma Diffuser',960,'USD',4299,'USD',NOW());

INSERT INTO order_snapshots (order_id, buyer_name, buyer_email, recipient_name, recipient_phone, country_code, state_code, city, district, address_line1, address_line2, postal_code, tax_rule_version, exchange_rate_version, exchange_rate, raw_address_json, create_time) VALUES
(1,'Alice Chen','alice@example.com','Alice Chen','+1-415-555-0101','US','CA','San Francisco','SoMa','1 Market Street','Apt 1201','94105','TAX-US-2026','FX-2026-10',7.200000000000,'{}',NOW()),
(2,'Bob Müller','bob@example.de','Bob Müller','+49-30-555-0102','DE','BE','Berlin','Mitte','Friedrichstrasse 100',NULL,'10117','TAX-DE-2026','FX-2026-10',7.800000000000,'{}',NOW()),
(3,'Carol Tan','carol@example.sg','Carol Tan','+65-6123-4567','SG','SG','Singapore','Jurong','10 Jurong East Street 12',NULL,'609690','TAX-SG-2026','FX-2026-10',7.200000000000,'{}',NOW());

INSERT INTO order_state_histories (order_id, from_status, to_status, status_type, reason, operator_type, operator_id, occurred_at, create_time) VALUES
(1,NULL,'PENDING_PAYMENT','ORDER','创建订单','USER','alice','2026-10-24 09:12:00',NOW()),
(1,'PENDING_PAYMENT','PAID','PAYMENT','支付成功','SYSTEM','MOCK','2026-10-24 09:13:10',NOW()),
(1,'PAID','SHIPPED','ORDER','已发货','SYSTEM','DHL','2026-10-24 15:00:00',NOW()),
(2,NULL,'PENDING_PAYMENT','ORDER','创建订单','USER','bob','2026-10-23 14:20:00',NOW()),
(2,'PENDING_PAYMENT','PAID','PAYMENT','支付成功','SYSTEM','MOCK','2026-10-23 14:21:05',NOW()),
(2,'DELIVERED','COMPLETED','ORDER','订单完成','SYSTEM','SYSTEM','2026-10-25 10:00:00',NOW()),
(2,'PROCESSING','RESOLVED','AFTER_SALE','部分退款成功','SYSTEM','SYSTEM','2026-10-25 10:10:00',NOW());

INSERT INTO payments (payment_no, order_id, provider, provider_payment_id, method, amount, currency_code, payment_status, idempotency_key, paid_at, create_by, create_time, update_by, update_time) VALUES
('PAY202610240001',1,'MOCK','MOCK-1001','CARD',14039,'USD','SUCCEEDED','order-1-PAY202610240001','2026-10-24 09:13:10','admin',NOW(),'admin',NOW()),
('PAY202610230002',2,'MOCK','MOCK-1002','CARD',26178,'EUR','SUCCEEDED','order-2-PAY202610230002','2026-10-23 14:21:05','admin',NOW(),'admin',NOW()),
('PAY202610240003',3,'MOCK',NULL,'CARD',5765,'USD','INITIATED','order-3-PAY202610240003',NULL,'admin',NOW(),'admin',NOW());

INSERT INTO refunds (refund_no, order_id, payment_id, amount, currency_code, refund_status, reason, provider_refund_id, requested_at, completed_at, create_by, create_time, update_by, update_time) VALUES
('RF-20261025-001',2,2,2000,'EUR','SUCCEEDED','表带轻微磨损，部分退款','MOCK-RF-001','2026-10-25 09:40:00','2026-10-25 10:10:00','admin',NOW(),'admin',NOW());

INSERT INTO fulfillment_orders (fulfillment_no, shop_id, order_id, warehouse_code, fulfillment_status, allocation_reason, estimated_ship_at, actual_ship_at, completed_at, create_by, create_time, update_by, update_time) VALUES
('FUL202610240001',1,1,'US-WEST','SHIPPED','LOCKED_STOCK','2026-10-24 18:00:00','2026-10-24 15:00:00',NULL,'admin',NOW(),'admin',NOW()),
('FUL202610230002',1,2,'DE-FRA','DELIVERED','LOCKED_STOCK','2026-10-24 12:00:00','2026-10-24 10:00:00','2026-10-25 09:30:00','admin',NOW(),'admin',NOW());

INSERT INTO fulfillment_items (fulfillment_id, order_item_id, quantity, create_time) VALUES
(1,1,1,NOW()),
(2,2,2,NOW());

INSERT INTO shipments (shipment_no, fulfillment_id, carrier_code, tracking_no, shipment_status, shipped_at, delivered_at, create_by, create_time, update_by, update_time) VALUES
('SHP202610240001',1,'DHL','DHL9001234567','IN_TRANSIT','2026-10-24 15:00:00',NULL,'admin',NOW(),'admin',NOW()),
('SHP202610230002',2,'DHL','DHL9007654321','DELIVERED','2026-10-24 10:00:00','2026-10-25 09:30:00','admin',NOW(),'admin',NOW());

INSERT INTO tracking_events (shipment_id, event_code, event_description, location, occurred_at, raw_payload_json, received_at) VALUES
(1,'SHIPPED','已交承运商','Los Angeles, US','2026-10-24 15:00:00','{}',NOW()),
(1,'IN_TRANSIT','运输中','Ontario, CA','2026-10-24 22:00:00','{}',NOW()),
(2,'DELIVERED','已签收','Berlin, DE','2026-10-25 09:30:00','{}',NOW());

INSERT INTO audit_log (shop_id, module, action, target_type, target_id, operator, detail, ip, result, create_time) VALUES
(1,'ORDER','CANCEL','ORDER','NC-20261020-0998','admin','买家申请取消，库存已释放','127.0.0.1','SUCCESS',NOW()),
(2,'SHOP','STATUS_CHANGE','SHOP','SHOP-AURORA','admin','店铺资料审核通过','127.0.0.1','SUCCESS',NOW()),
(3,'RISK','BLOCK','SHOP','SHOP-TRAIL','system','命中风控规则：结算账户异常','127.0.0.1','FAIL',NOW());
