<template>
  <div class="app-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="促销活动" name="promotion">
        <el-form :model="promotionQuery" ref="promotionQueryRef" :inline="true" v-show="showSearch">
          <el-form-item label="活动名称" prop="promotionName">
            <el-input
              v-model="promotionQuery.promotionName"
              placeholder="请输入活动名称"
              clearable
              style="width: 200px"
              @keyup.enter="handlePromotionQuery"
            />
          </el-form-item>
          <el-form-item label="活动类型" prop="promotionType">
            <el-select v-model="promotionQuery.promotionType" placeholder="活动类型" clearable style="width: 180px">
              <el-option
                v-for="dict in commerce_promotion_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="promotionQuery.status" placeholder="状态" clearable style="width: 160px">
              <el-option
                v-for="dict in commerce_biz_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handlePromotionQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetPromotionQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              @click="handleAddPromotion"
              v-hasPermi="['commerce:shop:marketing:add']"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="Edit"
              :disabled="single"
              @click="handleUpdatePromotion"
              v-hasPermi="['commerce:shop:marketing:edit']"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="Delete"
              :disabled="multiple"
              @click="handleDeletePromotion"
              v-hasPermi="['commerce:shop:marketing:remove']"
            >删除</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getPromotionList"></right-toolbar>
        </el-row>

        <el-table v-loading="promotionLoading" :data="promotionList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="活动编码" align="center" prop="promotionCode" width="150" />
          <el-table-column label="活动名称" align="left" prop="promotionName" :show-overflow-tooltip="true" />
          <el-table-column label="活动类型" align="center" prop="promotionType" width="110">
            <template #default="scope">
              <dict-tag :options="commerce_promotion_type" :value="scope.row.promotionType" />
            </template>
          </el-table-column>
          <el-table-column label="适用范围" align="center" width="150">
            <template #default="scope">
              <span>{{ scope.row.scopeType || '-' }}{{ scope.row.scopeValue ? ' / ' + scope.row.scopeValue : '' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="优惠值" align="right" width="130">
            <template #default="scope">
              <span>{{ formatMoney(scope.row.discountValue || 0, scope.row.currencyCode || 'CNY') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="门槛金额" align="right" width="130">
            <template #default="scope">
              <span>{{ formatMoney(scope.row.thresholdAmount || 0, scope.row.currencyCode || 'CNY') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" align="center" prop="startTime" width="170">
            <template #default="scope">
              <span>{{ parseTime(scope.row.startTime) || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" width="170">
            <template #default="scope">
              <span>{{ parseTime(scope.row.endTime) || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="status" width="90">
            <template #default="scope">
              <dict-tag :options="commerce_biz_status" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-button
                link
                type="primary"
                icon="Edit"
                @click="handleUpdatePromotion(scope.row)"
                v-hasPermi="['commerce:shop:marketing:edit']"
              >修改</el-button>
              <el-button
                link
                type="danger"
                icon="Delete"
                @click="handleDeletePromotion(scope.row)"
                v-hasPermi="['commerce:shop:marketing:remove']"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="promotionTotal > 0"
          :total="promotionTotal"
          v-model:page="promotionQuery.pageNum"
          v-model:limit="promotionQuery.pageSize"
          @pagination="getPromotionList"
        />
      </el-tab-pane>

      <el-tab-pane label="优惠券" name="coupon">
        <el-form :model="couponQuery" ref="couponQueryRef" :inline="true" v-show="showSearch">
          <el-form-item label="优惠券名称" prop="couponName">
            <el-input
              v-model="couponQuery.couponName"
              placeholder="请输入优惠券名称"
              clearable
              style="width: 200px"
              @keyup.enter="handleCouponQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="couponQuery.status" placeholder="状态" clearable style="width: 160px">
              <el-option
                v-for="dict in commerce_biz_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleCouponQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetCouponQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getCouponList"></right-toolbar>
        </el-row>

        <el-table v-loading="couponLoading" :data="couponList">
          <el-table-column label="优惠券编码" align="center" prop="couponCode" width="150" />
          <el-table-column label="优惠券名称" align="left" prop="couponName" :show-overflow-tooltip="true" />
          <el-table-column label="类型" align="center" prop="couponType" width="100">
            <template #default="scope">
              <dict-tag :options="commerce_coupon_type" :value="scope.row.couponType" />
            </template>
          </el-table-column>
          <el-table-column label="优惠值" align="right" width="130">
            <template #default="scope">
              <span>{{ formatMoney(scope.row.discountValue || 0, scope.row.currencyCode || 'CNY') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="最低消费" align="right" width="130">
            <template #default="scope">
              <span>{{ formatMoney(scope.row.minAmount || 0, scope.row.currencyCode || 'CNY') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="总量" align="center" prop="totalQty" width="90" />
          <el-table-column label="已使用" align="center" prop="usedQty" width="90" />
          <el-table-column label="每人限领" align="center" prop="perUserLimit" width="100" />
          <el-table-column label="开始时间" align="center" prop="startTime" width="170">
            <template #default="scope">
              <span>{{ parseTime(scope.row.startTime) || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" width="170">
            <template #default="scope">
              <span>{{ parseTime(scope.row.endTime) || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="status" width="90">
            <template #default="scope">
              <dict-tag :options="commerce_biz_status" :value="scope.row.status" />
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="couponTotal > 0"
          :total="couponTotal"
          v-model:page="couponQuery.pageNum"
          v-model:limit="couponQuery.pageSize"
          @pagination="getCouponList"
        />
      </el-tab-pane>
    </el-tabs>

    <!-- 新增/修改促销活动 -->
    <el-dialog :title="promotionTitle" v-model="promotionOpen" width="680px" append-to-body>
      <el-form ref="promotionRef" :model="promotionForm" :rules="promotionRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="活动编码" prop="promotionCode">
              <el-input v-model="promotionForm.promotionCode" placeholder="请输入活动编码" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动名称" prop="promotionName">
              <el-input v-model="promotionForm.promotionName" placeholder="请输入活动名称" maxlength="100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="活动类型" prop="promotionType">
              <el-select v-model="promotionForm.promotionType" placeholder="请选择活动类型" style="width: 100%">
                <el-option
                  v-for="dict in commerce_promotion_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="promotionForm.status" placeholder="请选择状态" style="width: 100%">
                <el-option
                  v-for="dict in commerce_biz_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="适用范围" prop="scopeType">
              <el-select v-model="promotionForm.scopeType" placeholder="请选择适用范围" style="width: 100%">
                <el-option label="全场" value="ALL" />
                <el-option label="指定商品" value="PRODUCT" />
                <el-option label="指定类目" value="CATEGORY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="范围值" prop="scopeValue">
              <el-input v-model="promotionForm.scopeValue" placeholder="商品/类目标识，全场可留空" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="优惠值" prop="discountValue">
              <el-input-number
                v-model="promotionForm.discountValue"
                :min="0"
                :precision="2"
                :step="1"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="门槛金额" prop="thresholdAmount">
              <el-input-number
                v-model="promotionForm.thresholdAmount"
                :min="0"
                :precision="2"
                :step="1"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-select v-model="promotionForm.currencyCode" placeholder="请选择币种" style="width: 100%">
                <el-option v-for="item in currencyOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker
                v-model="promotionForm.startTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker
                v-model="promotionForm.endTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :loading="promotionSubmitting" @click="submitPromotion">确 定</el-button>
          <el-button @click="promotionOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ShopMarketing">
import {
  listShopPromotion,
  addShopPromotion,
  updateShopPromotion,
  delShopPromotion,
  listShopCoupon
} from '@/api/commerce/shop/marketing'
import { formatMoney, fromMinor, toMinor } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_promotion_type, commerce_coupon_type, commerce_biz_status } =
  useDict('commerce_promotion_type', 'commerce_coupon_type', 'commerce_biz_status')

const activeTab = ref('promotion')
const showSearch = ref(true)

const promotionLoading = ref(false)
const promotionList = ref([])
const promotionTotal = ref(0)
const promotionIds = ref([])
const single = ref(true)
const multiple = ref(true)
const promotionQuery = ref({
  pageNum: 1,
  pageSize: 10,
  promotionName: undefined,
  promotionType: undefined,
  status: undefined
})

const couponLoading = ref(false)
const couponList = ref([])
const couponTotal = ref(0)
const couponQuery = ref({
  pageNum: 1,
  pageSize: 10,
  couponName: undefined,
  status: undefined
})

const promotionOpen = ref(false)
const promotionTitle = ref('')
const promotionSubmitting = ref(false)
const promotionForm = ref({})
const promotionRules = {
  promotionCode: [{ required: true, message: '活动编码不能为空', trigger: 'blur' }],
  promotionName: [{ required: true, message: '活动名称不能为空', trigger: 'blur' }],
  promotionType: [{ required: true, message: '活动类型不能为空', trigger: 'change' }],
  startTime: [{ required: true, message: '开始时间不能为空', trigger: 'change' }],
  endTime: [{ required: true, message: '结束时间不能为空', trigger: 'change' }]
}

const currencyOptions = ['CNY', 'USD', 'EUR', 'GBP', 'JPY', 'HKD']

function getPromotionList() {
  promotionLoading.value = true
  listShopPromotion(promotionQuery.value).then(response => {
    promotionList.value = response.rows || []
    promotionTotal.value = response.total || 0
  }).finally(() => {
    promotionLoading.value = false
  })
}

function getCouponList() {
  couponLoading.value = true
  listShopCoupon(couponQuery.value).then(response => {
    couponList.value = response.rows || []
    couponTotal.value = response.total || 0
  }).finally(() => {
    couponLoading.value = false
  })
}

function handlePromotionQuery() {
  promotionQuery.value.pageNum = 1
  getPromotionList()
}

function resetPromotionQuery() {
  proxy.resetForm('promotionQueryRef')
  promotionQuery.value.promotionName = undefined
  promotionQuery.value.promotionType = undefined
  promotionQuery.value.status = undefined
  handlePromotionQuery()
}

function handleCouponQuery() {
  couponQuery.value.pageNum = 1
  getCouponList()
}

function resetCouponQuery() {
  proxy.resetForm('couponQueryRef')
  couponQuery.value.couponName = undefined
  couponQuery.value.status = undefined
  handleCouponQuery()
}

function handleSelectionChange(selection) {
  promotionIds.value = selection.map(item => item.promotionId)
  single.value = selection.length !== 1
  multiple.value = selection.length === 0
}

function resetPromotionForm() {
  promotionForm.value = {
    promotionId: undefined,
    promotionCode: undefined,
    promotionName: undefined,
    promotionType: 'DISCOUNT',
    scopeType: 'ALL',
    scopeValue: undefined,
    discountValue: 0,
    thresholdAmount: 0,
    currencyCode: 'CNY',
    startTime: undefined,
    endTime: undefined,
    status: 'ACTIVE'
  }
}

function handleAddPromotion() {
  resetPromotionForm()
  promotionTitle.value = '新增促销活动'
  promotionOpen.value = true
  nextTick(() => proxy.$refs['promotionRef'] && proxy.$refs['promotionRef'].clearValidate())
}

function handleUpdatePromotion(row) {
  resetPromotionForm()
  const promotionId = (row && row.promotionId) || promotionIds.value[0]
  const target = (row && row.promotionId) ? row : promotionList.value.find(item => item.promotionId === promotionId)
  if (!target) {
    return
  }
  promotionForm.value = {
    ...target,
    discountValue: fromMinor(target.discountValue),
    thresholdAmount: fromMinor(target.thresholdAmount)
  }
  promotionTitle.value = '修改促销活动'
  promotionOpen.value = true
  nextTick(() => proxy.$refs['promotionRef'] && proxy.$refs['promotionRef'].clearValidate())
}

function submitPromotion() {
  proxy.$refs['promotionRef'].validate(valid => {
    if (!valid) {
      return
    }
    const data = {
      ...promotionForm.value,
      discountValue: toMinor(promotionForm.value.discountValue),
      thresholdAmount: toMinor(promotionForm.value.thresholdAmount)
    }
    promotionSubmitting.value = true
    const request = data.promotionId ? updateShopPromotion(data) : addShopPromotion(data)
    request.then(() => {
      proxy.$modal.msgSuccess(data.promotionId ? '修改成功' : '新增成功')
      promotionOpen.value = false
      getPromotionList()
    }).finally(() => {
      promotionSubmitting.value = false
    })
  })
}

function handleDeletePromotion(row) {
  const ids = (row && row.promotionId) ? [row.promotionId] : promotionIds.value
  if (!ids.length) {
    return
  }
  proxy.$modal.confirm('是否确认删除选中的促销活动？').then(() => {
    return delShopPromotion(ids.join(','))
  }).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getPromotionList()
  }).catch(() => {})
}

getPromotionList()
getCouponList()
</script>
