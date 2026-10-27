# 跨境电商平台测试用例

- 生成时间：2026/10/24 23:05:00
- 测试环境：http://127.0.0.1:8080
- 后端接口用例：165 项（通过 165 / 失败 0 / 跳过 0）
- 后端单元用例：43 项
- 用例数据来源：`tests/api-test.mjs` 运行结果与 `ruoyi-commerce/src/test/java` 单元测试

## 一、后端单元测试用例

| 测试类 | 覆盖点 | 用例数 |
| --- | --- | ---: |
| OrderStateMachineTest | 合法流转、非法流转、幂等、可取消状态、空状态 | 8 |
| CommerceNoUtilsTest | 订单/支付/退款/履约/运单/客户/结算单号前缀、批量唯一性、长度 | 4 |
| TaxServiceImplTest | 税率计算四舍五入、无规则、零金额、空国家、空税率 | 5 |
| CouponServiceImplTest | 有效券、缺失、门槛、过期、币种/国家不匹配、固定/百分比/封顶、异常类型 | 10 |
| SkuPriceServiceImplTest | 价格命中、跨国家回退、无价格、区间重叠、销售价高于划线价、时间非法 | 8 |
| InventoryServiceImplTest | 锁定选仓、库存不足、无库存、多仓跳过、扣减/释放条件失败、记录不存在 | 8 |

## 二、后端 API 集成测试用例

### A 认证与权限

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| AUTH-001 | admin 登录成功 | POST | `/login` |
| AUTH-002 | admin 错误密码登录失败 | POST | `/login` |
| AUTH-003 | 无 token 访问商品列表返回 401 | GET | `/commerce/admin/product/list` |
| AUTH-004 | shopadmin 访问平台店铺列表返回 403 | GET | `/commerce/platform/shop/list` |
| AUTH-005 | shopadmin 访问运营商品列表返回 403 | GET | `/commerce/admin/product/list` |

### B 运营-商品

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| ADMIN-PRODUCT-001 | 商品列表 | GET | `/commerce/admin/product/list` |
| ADMIN-PRODUCT-002 | SKU 下拉选项 | GET | `/commerce/admin/product/skuOptions` |
| ADMIN-PRODUCT-003 | 商品详情（种子 SPU 1） | GET | `/commerce/admin/product/{spuId}` |
| ADMIN-PRODUCT-004 | 新增 SPU + SKU | POST | `/commerce/admin/product` |
| ADMIN-PRODUCT-005 | 更新商品 | PUT | `/commerce/admin/product` |
| ADMIN-PRODUCT-006 | 商品状态 DRAFT→PUBLISHED→ON_SALE→OFF_SALE | POST | `/commerce/admin/product/status` |
| ADMIN-PRODUCT-007 | SKU 列表 | GET | `/commerce/admin/product/sku/list` |
| ADMIN-PRODUCT-008 | SKU 详情 | GET | `/commerce/admin/product/sku/{skuId}` |
| ADMIN-PRODUCT-009 | 更新 SKU | PUT | `/commerce/admin/product/sku` |
| ADMIN-PRODUCT-010 | SKU 可售性查询 | GET | `/commerce/admin/product/availability` |
| ADMIN-PRODUCT-011 | 删除商品（先删 SKU 再删 SPU） | DELETE | `/commerce/admin/product/{spuIds}` |

### C 运营-价格

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| ADMIN-PRICE-001 | 价格列表 | GET | `/commerce/admin/price/list` |
| ADMIN-PRICE-002 | 价格解析（skuId=1, US, DIRECT, USD） | GET | `/commerce/admin/price/resolve` |
| ADMIN-PRICE-003 | 新增价格 | POST | `/commerce/admin/price` |
| ADMIN-PRICE-004 | 更新价格 | PUT | `/commerce/admin/price` |
| ADMIN-PRICE-005 | 非法区间与区间重叠被拒绝 | POST | `/commerce/admin/price` |
| ADMIN-PRICE-006 | 价格详情 | GET | `/commerce/admin/price/{priceId}` |
| ADMIN-PRICE-007 | 删除价格 | DELETE | `/commerce/admin/price/{priceIds}` |

### D 运营-渠道

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| ADMIN-CHANNEL-001 | 渠道商品列表 | GET | `/commerce/admin/channel/list` |
| ADMIN-CHANNEL-002 | 新增渠道商品映射 | POST | `/commerce/admin/channel` |
| ADMIN-CHANNEL-003 | 更新渠道商品映射 | PUT | `/commerce/admin/channel` |
| ADMIN-CHANNEL-004 | 同步渠道商品（LIVE） | POST | `/commerce/admin/channel/sync/{listingId}` |
| ADMIN-CHANNEL-005 | 渠道商品详情 | GET | `/commerce/admin/channel/{listingId}` |
| ADMIN-CHANNEL-006 | 删除渠道商品映射 | DELETE | `/commerce/admin/channel/{listingIds}` |

### E 运营-客户与地址

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| ADMIN-CUSTOMER-001 | 客户列表 | GET | `/commerce/admin/customer/list` |
| ADMIN-CUSTOMER-002 | 新增客户 | POST | `/commerce/admin/customer` |
| ADMIN-CUSTOMER-003 | 重复邮箱创建被拒绝 | POST | `/commerce/admin/customer` |
| ADMIN-CUSTOMER-004 | 更新客户 | PUT | `/commerce/admin/customer` |
| ADMIN-CUSTOMER-005 | 新增客户收货地址 | POST | `/commerce/admin/customer/address` |
| ADMIN-CUSTOMER-006 | 客户地址列表 | GET | `/commerce/admin/customer/address/list` |
| ADMIN-CUSTOMER-007 | 更新客户地址 | PUT | `/commerce/admin/customer/address` |
| ADMIN-CUSTOMER-008 | 删除客户地址 | DELETE | `/commerce/admin/customer/address/{addressIds}` |
| ADMIN-CUSTOMER-009 | 删除客户 | DELETE | `/commerce/admin/customer/{customerIds}` |

### F 运营-购物车

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| ADMIN-CART-001 | 查询客户购物车 | GET | `/commerce/admin/cart/{customerId}` |
| ADMIN-CART-002 | 运营添加购物车明细 | POST | `/commerce/admin/cart/item` |
| ADMIN-CART-003 | 运营修改购物车数量 | PUT | `/commerce/admin/cart/item` |
| ADMIN-CART-004 | 运营删除购物车明细 | DELETE | `/commerce/admin/cart/item/{cartItemId}` |
| ADMIN-CART-005 | 运营清空购物车 | DELETE | `/commerce/admin/cart/{customerId}` |

### G 运营-促销/优惠券/税率/汇率

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| ADMIN-PROMOTION-001 | 促销列表 | GET | `/commerce/admin/promotion/list` |
| ADMIN-PROMOTION-002 | 新增促销 | POST | `/commerce/admin/promotion` |
| ADMIN-PROMOTION-003 | 更新促销 | PUT | `/commerce/admin/promotion` |
| ADMIN-PROMOTION-004 | 删除促销 | DELETE | `/commerce/admin/promotion/{promotionIds}` |
| ADMIN-COUPON-001 | 优惠券列表 | GET | `/commerce/admin/coupon/list` |
| ADMIN-COUPON-002 | 新增优惠券 | POST | `/commerce/admin/coupon` |
| ADMIN-COUPON-003 | 更新优惠券 | PUT | `/commerce/admin/coupon` |
| ADMIN-COUPON-004 | 删除优惠券 | DELETE | `/commerce/admin/coupon/{couponIds}` |
| ADMIN-TAX-001 | 税率列表 | GET | `/commerce/admin/tax/list` |
| ADMIN-TAX-002 | 新增税率 | POST | `/commerce/admin/tax` |
| ADMIN-TAX-003 | 更新税率 | PUT | `/commerce/admin/tax` |
| ADMIN-TAX-004 | 删除税率 | DELETE | `/commerce/admin/tax/{ruleIds}` |
| ADMIN-EXCHANGE-001 | 汇率列表 | GET | `/commerce/admin/exchange/list` |
| ADMIN-EXCHANGE-002 | 新增汇率 | POST | `/commerce/admin/exchange` |
| ADMIN-EXCHANGE-003 | 更新汇率 | PUT | `/commerce/admin/exchange` |
| ADMIN-EXCHANGE-004 | 删除汇率 | DELETE | `/commerce/admin/exchange/{rateIds}` |

### H 运营-订单全链路

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| ADMIN-ORDER-001 | 创建订单并校验金额构成 | POST | `/commerce/admin/order` |
| ADMIN-ORDER-002 | 订单详情（明细/快照/历史） | GET | `/commerce/admin/order/{orderId}` |
| ADMIN-ORDER-003 | 订单状态历史 | GET | `/commerce/admin/order/history/{orderId}` |
| ADMIN-ORDER-004 | 订单列表可查到新订单 | GET | `/commerce/admin/order/list` |
| ADMIN-ORDER-005 | 创建支付单 | POST | `/commerce/admin/payment` |
| ADMIN-ORDER-006 | 模拟支付成功并校验订单 PAID | POST | `/commerce/admin/payment/mock/{paymentId}` |
| ADMIN-ORDER-007 | 支付事件非空 | GET | `/commerce/admin/payment/events/{paymentId}` |
| ADMIN-ORDER-008 | 订单拆分履约单 | POST | `/commerce/admin/fulfillment/split/{orderId}` |
| ADMIN-ORDER-009 | 履约状态 ALLOCATED→PICKING→PACKED | POST | `/commerce/admin/fulfillment/status` |
| ADMIN-ORDER-010 | 履约发货并校验订单 SHIPPED | POST | `/commerce/admin/fulfillment/ship` |
| ADMIN-ORDER-011 | 运单列表 | GET | `/commerce/admin/shipment/list` |
| ADMIN-ORDER-012 | 运单详情含 trackingEvents | GET | `/commerce/admin/shipment/{shipmentId}` |
| ADMIN-ORDER-013 | 追加轨迹 IN_TRANSIT | POST | `/commerce/admin/shipment/track` |
| ADMIN-ORDER-014 | 运单签收并校验订单 DELIVERED | POST | `/commerce/admin/shipment/deliver/{shipmentId}` |
| ADMIN-ORDER-015 | 订单完成 COMPLETED | POST | `/commerce/admin/order/complete/{orderId}` |
| ADMIN-ORDER-016 | 发起部分退款（实付 10%） | POST | `/commerce/admin/refund` |
| ADMIN-ORDER-017 | 退款审核通过并完成 | POST | `/commerce/admin/refund/approve/{refundId}` |
| ADMIN-ORDER-018 | 未支付订单完成被拒绝 | POST | `/commerce/admin/order/complete/{orderId}` |
| ADMIN-ORDER-019 | 订单取消并释放库存锁定 | POST | `/commerce/admin/order/cancel/{orderId}` |

### I 运营-库存/履约/物流/看板/对账

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| ADMIN-INVENTORY-001 | 库存列表 | GET | `/commerce/admin/inventory/list` |
| ADMIN-INVENTORY-002 | 库存详情 | GET | `/commerce/admin/inventory/{inventoryId}` |
| ADMIN-INVENTORY-003 | 库存流水 | GET | `/commerce/admin/inventory/transactions` |
| ADMIN-INVENTORY-004 | 手工调整库存 +10 并读回 | POST | `/commerce/admin/inventory/adjust` |
| ADMIN-INVENTORY-005 | 新增库存记录 | POST | `/commerce/admin/inventory` |
| ADMIN-FULFILLMENT-001 | 履约单列表 | GET | `/commerce/admin/fulfillment/list` |
| ADMIN-FULFILLMENT-002 | 履约单详情 | GET | `/commerce/admin/fulfillment/{fulfillmentId}` |
| ADMIN-SHIPMENT-001 | 运单列表 | GET | `/commerce/admin/shipment/list` |
| ADMIN-SHIPMENT-002 | 运单详情 | GET | `/commerce/admin/shipment/{shipmentId}` |
| ADMIN-DASHBOARD-001 | 运营看板汇总 | GET | `/commerce/admin/dashboard/summary` |
| ADMIN-RECONCILIATION-001 | 对账汇总 | GET | `/commerce/admin/reconciliation/summary` |
| ADMIN-RECONCILIATION-002 | 对账明细列表 | GET | `/commerce/admin/reconciliation/list` |

### J 店铺端

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| SHOP-DASHBOARD-001 | 店铺工作台汇总 | GET | `/commerce/shop/dashboard/summary` |
| SHOP-PRODUCT-001 | 店铺商品列表 | GET | `/commerce/shop/product/list` |
| SHOP-PRODUCT-002 | 店铺商品详情 | GET | `/commerce/shop/product/{spuId}` |
| SHOP-PRODUCT-003 | 店铺商品 SKU 列表 | GET | `/commerce/shop/product/sku/list` |
| SHOP-PRODUCT-004 | 店铺商品上下架 | POST | `/commerce/shop/product/status` |
| SHOP-ORDER-001 | 店铺订单列表 | GET | `/commerce/shop/order/list` |
| SHOP-ORDER-002 | 店铺订单详情 | GET | `/commerce/shop/order/{orderId}` |
| SHOP-ORDER-003 | 店铺取消订单 | POST | `/commerce/shop/order/cancel/{orderId}` |
| SHOP-LOGISTICS-001 | 店铺物流列表 | GET | `/commerce/shop/logistics/list` |
| SHOP-LOGISTICS-002 | 店铺运单详情 | GET | `/commerce/shop/logistics/{shipmentId}` |
| SHOP-LOGISTICS-003 | 店铺录入物流轨迹 | POST | `/commerce/shop/logistics/track` |
| SHOP-CUSTOMER-001 | 店铺客户列表 | GET | `/commerce/shop/customer/list` |
| SHOP-REPORT-001 | 店铺经营报表 | GET | `/commerce/shop/report/summary` |
| SHOP-MARKETING-001 | 店铺促销列表 | GET | `/commerce/shop/marketing/promotion/list` |
| SHOP-MARKETING-002 | 店铺新增促销 | POST | `/commerce/shop/marketing/promotion` |
| SHOP-MARKETING-003 | 店铺更新促销 | PUT | `/commerce/shop/marketing/promotion` |
| SHOP-MARKETING-004 | 店铺删除促销 | DELETE | `/commerce/shop/marketing/promotion/{promotionIds}` |
| SHOP-MARKETING-005 | 店铺优惠券列表 | GET | `/commerce/shop/marketing/coupon/list` |
| SHOP-PROFILE-001 | 店铺资料查询 | GET | `/commerce/shop/profile` |
| SHOP-PROFILE-002 | 店铺资料更新 | PUT | `/commerce/shop/profile` |
| SHOP-SETTLEMENT-001 | 店铺结算单列表 | GET | `/commerce/shop/settlement/list` |
| SHOP-SETTLEMENT-002 | 生成结算单 | POST | `/commerce/shop/settlement/generate` |
| SHOP-SETTLEMENT-003 | 结算单打款 | POST | `/commerce/shop/settlement/pay/{settlementId}` |

### K 平台端

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| PLATFORM-DASHBOARD-001 | 平台驾驶舱汇总 | GET | `/commerce/platform/dashboard/summary` |
| PLATFORM-SHOP-001 | 平台店铺列表 | GET | `/commerce/platform/shop/list` |
| PLATFORM-SHOP-002 | 平台店铺详情 | GET | `/commerce/platform/shop/{shopId}` |
| PLATFORM-SHOP-003 | 平台新增店铺 | POST | `/commerce/platform/shop` |
| PLATFORM-SHOP-004 | 平台更新店铺 | PUT | `/commerce/platform/shop` |
| PLATFORM-SHOP-005 | 平台调整店铺状态 | POST | `/commerce/platform/shop/status` |
| PLATFORM-SHOP-006 | 平台删除店铺 | DELETE | `/commerce/platform/shop/{shopIds}` |
| PLATFORM-USER-001 | 平台用户列表 | GET | `/commerce/platform/user/list` |
| PLATFORM-AUDIT-001 | 平台审计日志列表 | GET | `/commerce/platform/audit/list` |
| PLATFORM-CHANNEL-001 | 平台渠道配置列表 | GET | `/commerce/platform/channel/list` |
| PLATFORM-CHANNEL-002 | 平台新增渠道配置 | POST | `/commerce/platform/channel` |
| PLATFORM-CHANNEL-003 | 平台更新渠道配置 | PUT | `/commerce/platform/channel` |
| PLATFORM-CHANNEL-004 | 平台删除渠道配置 | DELETE | `/commerce/platform/channel/{channelConfigIds}` |
| PLATFORM-CONFIG-001 | 平台设置列表 | GET | `/commerce/platform/config/list` |
| PLATFORM-CONFIG-002 | 平台设置更新 | PUT | `/commerce/platform/config` |

### L 买家端

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| STOREFRONT-CATALOG-001 | 商品分类 | GET | `/api/storefront/categories` |
| STOREFRONT-CATALOG-002 | 商品分页 | GET | `/api/storefront/products` |
| STOREFRONT-CATALOG-003 | 商品分类筛选 | GET | `/api/storefront/products` |
| STOREFRONT-CATALOG-004 | 商品搜索 | GET | `/api/storefront/products` |
| STOREFRONT-CATALOG-005 | 商品价格升序排序 | GET | `/api/storefront/products` |
| STOREFRONT-CATALOG-006 | 商品详情 | GET | `/api/storefront/products/{id}` |
| STOREFRONT-AUTH-001 | 买家错误密码登录失败 | POST | `/api/storefront/login` |
| STOREFRONT-AUTH-002 | 买家登录成功 | POST | `/api/storefront/login` |
| STOREFRONT-AUTH-003 | 买家账户信息 | GET | `/api/storefront/account` |
| STOREFRONT-CART-001 | 未登录访问购物车返回 401 | GET | `/api/storefront/cart` |
| STOREFRONT-CART-002 | 买家加入购物车 | POST | `/api/storefront/cart` |
| STOREFRONT-CART-003 | 买家修改购物车数量 | PUT | `/api/storefront/cart` |
| STOREFRONT-CART-004 | 买家购物车列表 | GET | `/api/storefront/cart` |
| STOREFRONT-CART-005 | 买家删除购物车明细 | DELETE | `/api/storefront/cart/{cartItemId}` |
| STOREFRONT-ADDRESS-001 | 买家地址列表 | GET | `/api/storefront/addresses` |
| STOREFRONT-ADDRESS-002 | 买家新增地址 | POST | `/api/storefront/addresses` |
| STOREFRONT-ORDER-001 | 买家创建 USD 订单 | POST | `/api/storefront/orders` |
| STOREFRONT-ORDER-002 | 买家创建 EUR 订单 | POST | `/api/storefront/orders` |
| STOREFRONT-ORDER-003 | 买家创建 CNY 订单 | POST | `/api/storefront/orders` |
| STOREFRONT-ORDER-004 | 买家订单列表 | GET | `/api/storefront/orders` |
| STOREFRONT-ORDER-005 | 买家订单详情 | GET | `/api/storefront/orders/{orderNo}` |
| STOREFRONT-ORDER-006 | 买家支付 USD 订单 | POST | `/api/storefront/orders/{orderNo}/pay` |
| STOREFRONT-AFTERSALE-001 | 买家申请售后 | POST | `/api/storefront/aftersales` |
| STOREFRONT-AFTERSALE-002 | 买家售后列表 | GET | `/api/storefront/aftersales` |
| STOREFRONT-CART-006 | 购物车币种跟随结算币种 | GET | `/api/storefront/cart` |
| STOREFRONT-AUTH-004 | 买家登出后访问账户返回 401 | POST | `/api/storefront/logout` |

### M 边界与业务规则

| 用例 ID | 用例名称 | 方法 | 路径 |
| --- | --- | --- | --- |
| BOUNDARY-001 | 划线价低于销售价被拒绝 | POST | `/commerce/admin/price` |
| BOUNDARY-002 | SKU 不存在下单失败 | POST | `/commerce/admin/order` |
| BOUNDARY-003 | 库存不足下单失败（调整到极小后恢复） | POST | `/commerce/admin/order` |
| BOUNDARY-004 | 退款超过实付金额被拒绝 | POST | `/commerce/admin/refund` |
| BOUNDARY-005 | 重复支付回调幂等（同 providerEventId） | POST | `/commerce/admin/payment/callback` |
| BOUNDARY-006 | 外部渠道订单 externalOrderId 幂等 | POST | `/commerce/admin/order/external` |
| BOUNDARY-007 | 优惠券未达门槛被拒绝 | POST | `/commerce/admin/order` |
| BOUNDARY-008 | 订单快照包含跨境申报字段 | GET | `/commerce/admin/order/{orderId}` |
| BOUNDARY-009 | 跨店铺商品混合下单被拒绝 | POST | `/api/storefront/orders` |
| BOUNDARY-010 | 订单归属商品实际所属店铺 | POST | `/api/storefront/orders` |
| BOUNDARY-011 | 店铺端不能查看其他店铺订单 | GET | `/commerce/shop/order/{orderId}` |

## 三、页面级测试用例

| 范围 | 页面 / 流程 | 用例数 |
| --- | --- | ---: |
| 运营后台 | 工作台、订单、促销、售后、履约、库存、商品、定价、对账 | 9 |
| 平台中心 | 驾驶舱、店铺、用户、审计、渠道、设置 | 6 |
| 店铺中心 | 工作台、商品、订单、物流、客户、报表、营销、店铺资料、结算 | 9 |
| 买家商城 | 首页、详情、购物车、结算、订单、账户、登录 | 7 |
| 关键交互 | 弹窗、SKU 弹窗、订单详情、运单轨迹、上下架、币种切换、地址填写、支付、售后、登出 | 10 |

## 四、执行方式

```bash
# 后端单元测试
mvn -B -pl ruoyi-commerce -am test

# 后端 API 集成测试（需先启动后端、MySQL、Redis）
node tests/api-test.mjs

# 重新生成本用例文档
node tests/generate-test-cases.mjs
```
