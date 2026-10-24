-- ============================================================================
-- NovaCross 四端菜单与字典（RuoYi-Vue 3.9.2 / MySQL 8）
-- 菜单段：3000-3299（运营中心 / 店铺中心 / 平台中心）
-- 买家商城为独立前端，不占用若依菜单。
-- ============================================================================

USE `ry-mall`;

delete from sys_menu where menu_id between 3000 and 3299;

-- 运营中心
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark) values
(3000, '运营中心', 0, 4, 'ops', null, '', 1, 0, 'M', '0', '0', '', 'list', 'admin', sysdate(), '跨境交易运营后台'),
(3010, '运营工作台', 3000, 1, 'dashboard', 'commerce/admin/dashboard/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:dashboard:view', 'chart', 'admin', sysdate(), ''),
(3020, '订单管理', 3000, 2, 'order', 'commerce/admin/order/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:order:list', 'list', 'admin', sysdate(), ''),
(3030, '促销活动', 3000, 3, 'promotion', 'commerce/admin/promotion/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:promotion:list', 'star', 'admin', sysdate(), ''),
(3040, '售后与退款', 3000, 4, 'refund', 'commerce/admin/refund/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:refund:list', 'form', 'admin', sysdate(), ''),
(3050, '履约与物流', 3000, 5, 'fulfillment', 'commerce/admin/fulfillment/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:fulfillment:list', 'build', 'admin', sysdate(), ''),
(3060, '库存中心', 3000, 6, 'inventory', 'commerce/admin/inventory/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:inventory:list', 'table', 'admin', sysdate(), ''),
(3070, '商品中心', 3000, 7, 'product', 'commerce/admin/product/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:product:list', 'table', 'admin', sysdate(), ''),
(3080, '定价中心', 3000, 8, 'price', 'commerce/admin/price/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:price:list', 'money', 'admin', sysdate(), ''),
(3090, '数据与对账', 3000, 9, 'reconciliation', 'commerce/admin/reconciliation/index', '', 1, 0, 'C', '0', '0', 'commerce:admin:reconciliation:view', 'chart', 'admin', sysdate(), '');

-- 店铺中心
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark) values
(3100, '店铺中心', 0, 5, 'shop', null, '', 1, 0, 'M', '0', '0', '', 'table', 'admin', sysdate(), '商家店铺后台'),
(3110, '店铺工作台', 3100, 1, 'dashboard', 'commerce/shop/dashboard/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:dashboard:view', 'chart', 'admin', sysdate(), ''),
(3120, '店铺商品', 3100, 2, 'products', 'commerce/shop/products/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:product:list', 'table', 'admin', sysdate(), ''),
(3130, '店铺订单', 3100, 3, 'orders', 'commerce/shop/orders/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:order:list', 'list', 'admin', sysdate(), ''),
(3140, '物流管理', 3100, 4, 'logistics', 'commerce/shop/logistics/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:logistics:list', 'server', 'admin', sysdate(), ''),
(3150, '客户服务与权限', 3100, 5, 'customer', 'commerce/shop/customer/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:customer:list', 'peoples', 'admin', sysdate(), ''),
(3160, '经营报表', 3100, 6, 'reports', 'commerce/shop/reports/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:report:view', 'chart', 'admin', sysdate(), ''),
(3170, '营销活动', 3100, 7, 'marketing', 'commerce/shop/marketing/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:marketing:list', 'star', 'admin', sysdate(), ''),
(3180, '店铺管理', 3100, 8, 'management', 'commerce/shop/management/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:profile:view', 'system', 'admin', sysdate(), ''),
(3190, '店铺结算', 3100, 9, 'settlement', 'commerce/shop/settlement/index', '', 1, 0, 'C', '0', '0', 'commerce:shop:settlement:list', 'money', 'admin', sysdate(), '');

-- 平台中心
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark) values
(3200, '平台中心', 0, 6, 'platform', null, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', sysdate(), '平台治理后台'),
(3210, '平台驾驶舱', 3200, 1, 'dashboard', 'commerce/platform/dashboard/index', '', 1, 0, 'C', '0', '0', 'commerce:platform:dashboard:view', 'chart', 'admin', sysdate(), ''),
(3220, '店铺管理', 3200, 2, 'shops', 'commerce/platform/shops/index', '', 1, 0, 'C', '0', '0', 'commerce:platform:shop:list', 'table', 'admin', sysdate(), ''),
(3230, '用户与权限', 3200, 3, 'users', 'commerce/platform/users/index', '', 1, 0, 'C', '0', '0', 'commerce:platform:user:list', 'peoples', 'admin', sysdate(), ''),
(3240, '风控与审计', 3200, 4, 'audit', 'commerce/platform/audit/index', '', 1, 0, 'C', '0', '0', 'commerce:platform:audit:list', 'lock', 'admin', sysdate(), ''),
(3250, '渠道配置', 3200, 5, 'channels', 'commerce/platform/channels/index', '', 1, 0, 'C', '0', '0', 'commerce:platform:channel:list', 'link', 'admin', sysdate(), ''),
(3260, '平台设置', 3200, 6, 'settings', 'commerce/platform/settings/index', '', 1, 0, 'C', '0', '0', 'commerce:platform:config:list', 'tool', 'admin', sysdate(), '');

-- 按钮权限（运营）
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark) values
(3021, '订单查询', 3020, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:admin:order:query', '#', 'admin', sysdate(), ''),
(3022, '订单取消', 3020, 2, '', '', '', 1, 0, 'F', '0', '0', 'commerce:admin:order:cancel', '#', 'admin', sysdate(), ''),
(3031, '活动维护', 3030, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:admin:promotion:edit', '#', 'admin', sysdate(), ''),
(3041, '退款审核', 3040, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:admin:refund:approve', '#', 'admin', sysdate(), ''),
(3051, '履约操作', 3050, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:admin:fulfillment:edit', '#', 'admin', sysdate(), ''),
(3061, '库存调整', 3060, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:admin:inventory:adjust', '#', 'admin', sysdate(), ''),
(3071, '商品维护', 3070, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:admin:product:edit', '#', 'admin', sysdate(), ''),
(3081, '价格维护', 3080, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:admin:price:edit', '#', 'admin', sysdate(), ''),
(3111, '店铺资料', 3180, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:profile:edit', '#', 'admin', sysdate(), ''),
(3121, '商品查询', 3120, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:product:query', '#', 'admin', sysdate(), ''),
(3122, '商品编辑', 3120, 2, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:product:edit', '#', 'admin', sysdate(), ''),
(3123, '订单查询', 3130, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:order:query', '#', 'admin', sysdate(), ''),
(3124, '订单操作', 3130, 2, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:order:edit', '#', 'admin', sysdate(), ''),
(3125, '轨迹录入', 3140, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:logistics:edit', '#', 'admin', sysdate(), ''),
(3126, '活动新增', 3170, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:marketing:add', '#', 'admin', sysdate(), ''),
(3127, '活动编辑', 3170, 2, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:marketing:edit', '#', 'admin', sysdate(), ''),
(3128, '活动删除', 3170, 3, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:marketing:remove', '#', 'admin', sysdate(), ''),
(3129, '结算生成', 3190, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:settlement:generate', '#', 'admin', sysdate(), ''),
(3131, '结算打款', 3190, 2, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:settlement:pay', '#', 'admin', sysdate(), ''),
(3161, '营销维护', 3170, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:shop:marketing:edit', '#', 'admin', sysdate(), ''),
(3221, '店铺维护', 3220, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:platform:shop:edit', '#', 'admin', sysdate(), ''),
(3251, '渠道维护', 3250, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:platform:channel:edit', '#', 'admin', sysdate(), ''),
(3261, '设置维护', 3260, 1, '', '', '', 1, 0, 'F', '0', '0', 'commerce:platform:config:edit', '#', 'admin', sysdate(), '');

-- 同名 path=dashboard 的三个页面需要唯一路由名，否则动态路由会互相覆盖
update sys_menu set route_name = 'OpsDashboard'      where menu_id = 3010;
update sys_menu set route_name = 'ShopDashboard'     where menu_id = 3110;
update sys_menu set route_name = 'PlatformDashboard' where menu_id = 3210;

-- 字典
delete from sys_dict_data where dict_type like 'commerce_%';
delete from sys_dict_type where dict_type like 'commerce_%';

insert into sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) values
('商品状态','commerce_spu_status','0','admin',sysdate(),''),
('SKU 状态','commerce_sku_status','0','admin',sysdate(),''),
('渠道映射状态','commerce_listing_status','0','admin',sysdate(),''),
('客户状态','commerce_customer_status','0','admin',sysdate(),''),
('订单状态','commerce_order_status','0','admin',sysdate(),''),
('支付状态','commerce_payment_status','0','admin',sysdate(),''),
('履约状态','commerce_fulfillment_status','0','admin',sysdate(),''),
('售后状态','commerce_after_sale_status','0','admin',sysdate(),''),
('退款状态','commerce_refund_status','0','admin',sysdate(),''),
('履约单状态','commerce_fulfillment_order_status','0','admin',sysdate(),''),
('运单状态','commerce_shipment_status','0','admin',sysdate(),''),
('税率类型','commerce_tax_type','0','admin',sysdate(),''),
('优惠券类型','commerce_coupon_type','0','admin',sysdate(),''),
('活动类型','commerce_promotion_type','0','admin',sysdate(),''),
('启停状态','commerce_biz_status','0','admin',sysdate(),''),
('库存变动类型','commerce_inventory_tx_type','0','admin',sysdate(),''),
('店铺状态','commerce_shop_status','0','admin',sysdate(),''),
('店铺类型','commerce_shop_type','0','admin',sysdate(),''),
('结算状态','commerce_settlement_status','0','admin',sysdate(),''),
('渠道类型','commerce_channel_type','0','admin',sysdate(),''),
('渠道状态','commerce_channel_status','0','admin',sysdate(),''),
('审计结果','commerce_audit_result','0','admin',sysdate(),'');

insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(1,'草稿','DRAFT','commerce_spu_status','','info','Y','0','admin',sysdate(),''),
(2,'已发布','PUBLISHED','commerce_spu_status','','primary','N','0','admin',sysdate(),''),
(3,'在售','ON_SALE','commerce_spu_status','','success','N','0','admin',sysdate(),''),
(4,'暂停','SUSPENDED','commerce_spu_status','','warning','N','0','admin',sysdate(),''),
(5,'下架','OFF_SALE','commerce_spu_status','','danger','N','0','admin',sysdate(),''),
(1,'启用','ACTIVE','commerce_sku_status','','success','Y','0','admin',sysdate(),''),
(2,'停用','INACTIVE','commerce_sku_status','','info','N','0','admin',sysdate(),''),
(3,'冻结','FROZEN','commerce_sku_status','','warning','N','0','admin',sysdate(),''),
(1,'草稿','DRAFT','commerce_listing_status','','info','Y','0','admin',sysdate(),''),
(2,'待同步','PENDING','commerce_listing_status','','warning','N','0','admin',sysdate(),''),
(3,'在售','LIVE','commerce_listing_status','','success','N','0','admin',sysdate(),''),
(4,'暂停','PAUSED','commerce_listing_status','','warning','N','0','admin',sysdate(),''),
(5,'关闭','CLOSED','commerce_listing_status','','danger','N','0','admin',sysdate(),''),
(1,'正常','ACTIVE','commerce_customer_status','','success','Y','0','admin',sysdate(),''),
(2,'停用','INACTIVE','commerce_customer_status','','info','N','0','admin',sysdate(),''),
(3,'黑名单','BLOCKED','commerce_customer_status','','danger','N','0','admin',sysdate(),''),
(1,'已创建','CREATED','commerce_order_status','','info','Y','0','admin',sysdate(),''),
(2,'待支付','PENDING_PAYMENT','commerce_order_status','','warning','N','0','admin',sysdate(),''),
(3,'已支付','PAID','commerce_order_status','','success','N','0','admin',sysdate(),''),
(4,'已确认','CONFIRMED','commerce_order_status','','success','N','0','admin',sysdate(),''),
(5,'履约中','FULFILLING','commerce_order_status','','primary','N','0','admin',sysdate(),''),
(6,'已发货','SHIPPED','commerce_order_status','','primary','N','0','admin',sysdate(),''),
(7,'运输中','IN_TRANSIT','commerce_order_status','','primary','N','0','admin',sysdate(),''),
(8,'已妥投','DELIVERED','commerce_order_status','','success','N','0','admin',sysdate(),''),
(9,'已完成','COMPLETED','commerce_order_status','','success','N','0','admin',sysdate(),''),
(10,'已取消','CANCELLED','commerce_order_status','','danger','N','0','admin',sysdate(),''),
(1,'未支付','UNPAID','commerce_payment_status','','warning','Y','0','admin',sysdate(),''),
(2,'已授权','AUTHORIZED','commerce_payment_status','','primary','N','0','admin',sysdate(),''),
(3,'部分支付','PARTIALLY_PAID','commerce_payment_status','','warning','N','0','admin',sysdate(),''),
(4,'已支付','PAID','commerce_payment_status','','success','N','0','admin',sysdate(),''),
(5,'退款中','REFUNDING','commerce_payment_status','','warning','N','0','admin',sysdate(),''),
(6,'部分退款','PARTIALLY_REFUNDED','commerce_payment_status','','warning','N','0','admin',sysdate(),''),
(7,'已退款','REFUNDED','commerce_payment_status','','info','N','0','admin',sysdate(),''),
(8,'失败','FAILED','commerce_payment_status','','danger','N','0','admin',sysdate(),''),
(1,'未履约','UNFULFILLED','commerce_fulfillment_status','','info','Y','0','admin',sysdate(),''),
(2,'部分履约','PARTIALLY_FULFILLED','commerce_fulfillment_status','','warning','N','0','admin',sysdate(),''),
(3,'已履约','FULFILLED','commerce_fulfillment_status','','success','N','0','admin',sysdate(),''),
(4,'已退货','RETURNED','commerce_fulfillment_status','','danger','N','0','admin',sysdate(),''),
(1,'无售后','NONE','commerce_after_sale_status','','info','Y','0','admin',sysdate(),''),
(2,'已申请','REQUESTED','commerce_after_sale_status','','warning','N','0','admin',sysdate(),''),
(3,'处理中','PROCESSING','commerce_after_sale_status','','primary','N','0','admin',sysdate(),''),
(4,'已解决','RESOLVED','commerce_after_sale_status','','success','N','0','admin',sysdate(),''),
(5,'已驳回','REJECTED','commerce_after_sale_status','','danger','N','0','admin',sysdate(),''),
(1,'已申请','REQUESTED','commerce_refund_status','','warning','Y','0','admin',sysdate(),''),
(2,'已审核','APPROVED','commerce_refund_status','','primary','N','0','admin',sysdate(),''),
(3,'处理中','PROCESSING','commerce_refund_status','','primary','N','0','admin',sysdate(),''),
(4,'已成功','SUCCEEDED','commerce_refund_status','','success','N','0','admin',sysdate(),''),
(5,'失败','FAILED','commerce_refund_status','','danger','N','0','admin',sysdate(),''),
(6,'已驳回','REJECTED','commerce_refund_status','','danger','N','0','admin',sysdate(),''),
(1,'已创建','CREATED','commerce_fulfillment_order_status','','info','Y','0','admin',sysdate(),''),
(2,'已分配','ALLOCATED','commerce_fulfillment_order_status','','primary','N','0','admin',sysdate(),''),
(3,'拣货中','PICKING','commerce_fulfillment_order_status','','warning','N','0','admin',sysdate(),''),
(4,'已打包','PACKED','commerce_fulfillment_order_status','','warning','N','0','admin',sysdate(),''),
(5,'已发货','SHIPPED','commerce_fulfillment_order_status','','success','N','0','admin',sysdate(),''),
(6,'已签收','DELIVERED','commerce_fulfillment_order_status','','success','N','0','admin',sysdate(),''),
(7,'已取消','CANCELLED','commerce_fulfillment_order_status','','danger','N','0','admin',sysdate(),''),
(1,'已创建','CREATED','commerce_shipment_status','','info','Y','0','admin',sysdate(),''),
(2,'已打单','LABEL_PRINTED','commerce_shipment_status','','primary','N','0','admin',sysdate(),''),
(3,'运输中','IN_TRANSIT','commerce_shipment_status','','primary','N','0','admin',sysdate(),''),
(4,'已签收','DELIVERED','commerce_shipment_status','','success','N','0','admin',sysdate(),''),
(5,'异常','EXCEPTION','commerce_shipment_status','','danger','N','0','admin',sysdate(),''),
(6,'已退回','RETURNED','commerce_shipment_status','','warning','N','0','admin',sysdate(),''),
(1,'增值税','VAT','commerce_tax_type','','primary','Y','0','admin',sysdate(),''),
(2,'消费税','GST','commerce_tax_type','','primary','N','0','admin',sysdate(),''),
(3,'关税','DUTY','commerce_tax_type','','warning','N','0','admin',sysdate(),''),
(1,'固定金额','FIXED','commerce_coupon_type','','primary','Y','0','admin',sysdate(),''),
(2,'百分比','PERCENT','commerce_coupon_type','','success','N','0','admin',sysdate(),''),
(1,'折扣','DISCOUNT','commerce_promotion_type','','primary','Y','0','admin',sysdate(),''),
(2,'满减','FULL_REDUCTION','commerce_promotion_type','','success','N','0','admin',sysdate(),''),
(3,'限时抢购','FLASH_SALE','commerce_promotion_type','','danger','N','0','admin',sysdate(),''),
(1,'启用','ACTIVE','commerce_biz_status','','success','Y','0','admin',sysdate(),''),
(2,'停用','INACTIVE','commerce_biz_status','','info','N','0','admin',sysdate(),''),
(1,'锁定','RESERVE','commerce_inventory_tx_type','','warning','Y','0','admin',sysdate(),''),
(2,'释放','RELEASE','commerce_inventory_tx_type','','info','N','0','admin',sysdate(),''),
(3,'扣减','DEDUCT','commerce_inventory_tx_type','','primary','N','0','admin',sysdate(),''),
(4,'回补','RESTORE','commerce_inventory_tx_type','','success','N','0','admin',sysdate(),''),
(5,'调整','ADJUST','commerce_inventory_tx_type','','info','N','0','admin',sysdate(),''),
(1,'正常','ACTIVE','commerce_shop_status','','success','Y','0','admin',sysdate(),''),
(2,'暂停','SUSPENDED','commerce_shop_status','','warning','N','0','admin',sysdate(),''),
(3,'关闭','CLOSED','commerce_shop_status','','danger','N','0','admin',sysdate(),''),
(1,'自营','SELF','commerce_shop_type','','primary','Y','0','admin',sysdate(),''),
(2,'三方','THIRD_PARTY','commerce_shop_type','','info','N','0','admin',sysdate(),''),
(1,'待结算','PENDING','commerce_settlement_status','','warning','Y','0','admin',sysdate(),''),
(2,'已打款','PAID','commerce_settlement_status','','success','N','0','admin',sysdate(),''),
(1,'独立站','DIRECT','commerce_channel_type','','primary','Y','0','admin',sysdate(),''),
(2,'第三方平台','PLATFORM','commerce_channel_type','','success','N','0','admin',sysdate(),''),
(3,'社交渠道','SOCIAL','commerce_channel_type','','warning','N','0','admin',sysdate(),''),
(1,'启用','ACTIVE','commerce_channel_status','','success','Y','0','admin',sysdate(),''),
(2,'暂停','PAUSED','commerce_channel_status','','warning','N','0','admin',sysdate(),''),
(3,'停用','DISABLED','commerce_channel_status','','info','N','0','admin',sysdate(),''),
(1,'成功','SUCCESS','commerce_audit_result','','success','Y','0','admin',sysdate(),''),
(2,'失败','FAIL','commerce_audit_result','','danger','N','0','admin',sysdate(),'');
