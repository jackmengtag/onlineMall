<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="活动编码" prop="promotionCode">
        <el-input v-model="queryParams.promotionCode" placeholder="请输入活动编码" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="活动名称" prop="promotionName">
        <el-input v-model="queryParams.promotionName" placeholder="请输入活动名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
          <el-option v-for="dict in commerce_biz_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['commerce:admin:promotion:add']">新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="promotionList">
      <el-table-column label="活动编码" align="center" prop="promotionCode" min-width="140" show-overflow-tooltip />
      <el-table-column label="活动名称" align="center" prop="promotionName" min-width="140" show-overflow-tooltip />
      <el-table-column label="活动类型" align="center" prop="promotionType" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_promotion_type" :value="scope.row.promotionType" />
        </template>
      </el-table-column>
      <el-table-column label="范围" align="center" prop="scopeType" width="100" />
      <el-table-column label="范围值" align="center" prop="scopeValue" min-width="110" show-overflow-tooltip />
      <el-table-column label="优惠值" align="center" prop="discountValue" width="100" />
      <el-table-column label="门槛金额" align="center" width="120">
        <template #default="scope">{{ formatMoney(scope.row.thresholdAmount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="币种" align="center" prop="currencyCode" width="80" />
      <el-table-column label="店铺 ID" align="center" prop="shopId" width="90" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="160">
        <template #default="scope">{{ parseTime(scope.row.startTime) }}</template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="160">
        <template #default="scope">{{ parseTime(scope.row.endTime) }}</template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template #default="scope">
          <dict-tag :options="commerce_biz_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['commerce:admin:promotion:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['commerce:admin:promotion:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" v-model="open" width="680px" append-to-body>
      <el-form ref="promotionRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="活动编码" prop="promotionCode">
              <el-input v-model="form.promotionCode" placeholder="请输入活动编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动名称" prop="promotionName">
              <el-input v-model="form.promotionName" placeholder="请输入活动名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活动类型" prop="promotionType">
              <el-select v-model="form.promotionType" placeholder="请选择活动类型" style="width: 100%">
                <el-option v-for="dict in commerce_promotion_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="范围类型" prop="scopeType">
              <el-input v-model="form.scopeType" placeholder="如 ALL / CATEGORY / SKU" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="范围值" prop="scopeValue">
              <el-input v-model="form.scopeValue" placeholder="请输入范围值" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优惠值" prop="discountValue">
              <el-input-number v-model="form.discountValue" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="门槛金额(元)" prop="thresholdAmountYuan">
              <el-input-number v-model="form.thresholdAmountYuan" :min="0" :precision="2" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-input v-model="form.currencyCode" placeholder="如 USD / CNY" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker v-model="form.startTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker v-model="form.endTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="店铺 ID" prop="shopId">
              <el-input-number v-model="form.shopId" :min="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option v-for="dict in commerce_biz_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CommerceAdminPromotion">
import { listPromotion, getPromotion, addPromotion, updatePromotion, delPromotion } from '@/api/commerce/admin/promotion'
import { formatMoney, fromMinor, toMinor } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_promotion_type, commerce_biz_status } = useDict('commerce_promotion_type', 'commerce_biz_status')

const promotionList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const title = ref('')

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    promotionCode: undefined,
    promotionName: undefined,
    status: undefined
  },
  rules: {
    promotionCode: [{ required: true, message: '活动编码不能为空', trigger: 'blur' }],
    promotionName: [{ required: true, message: '活动名称不能为空', trigger: 'blur' }],
    promotionType: [{ required: true, message: '活动类型不能为空', trigger: 'change' }],
    currencyCode: [{ required: true, message: '币种不能为空', trigger: 'blur' }],
    startTime: [{ required: true, message: '开始时间不能为空', trigger: 'change' }],
    endTime: [{ required: true, message: '结束时间不能为空', trigger: 'change' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listPromotion(queryParams.value).then(response => {
    promotionList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    promotionId: undefined,
    promotionCode: undefined,
    promotionName: undefined,
    promotionType: 'DISCOUNT',
    scopeType: 'ALL',
    scopeValue: undefined,
    discountValue: 0,
    thresholdAmountYuan: 0,
    currencyCode: 'USD',
    startTime: undefined,
    endTime: undefined,
    shopId: undefined,
    status: 'ACTIVE',
    remark: undefined
  }
  proxy.resetForm('promotionRef')
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

function handleAdd() {
  reset()
  open.value = true
  title.value = '新增促销活动'
}

function handleUpdate(row) {
  reset()
  getPromotion(row.promotionId).then(response => {
    const promotion = response.data || {}
    form.value = {
      ...promotion,
      thresholdAmountYuan: fromMinor(promotion.thresholdAmount)
    }
    open.value = true
    title.value = '修改促销活动'
  })
}

function submitForm() {
  proxy.$refs['promotionRef'].validate(valid => {
    if (!valid) {
      return
    }
    const payload = {
      promotionId: form.value.promotionId,
      shopId: form.value.shopId,
      promotionCode: form.value.promotionCode,
      promotionName: form.value.promotionName,
      promotionType: form.value.promotionType,
      scopeType: form.value.scopeType,
      scopeValue: form.value.scopeValue,
      discountValue: form.value.discountValue,
      thresholdAmount: toMinor(form.value.thresholdAmountYuan),
      currencyCode: form.value.currencyCode,
      startTime: form.value.startTime,
      endTime: form.value.endTime,
      status: form.value.status,
      remark: form.value.remark
    }
    if (form.value.promotionId != undefined) {
      updatePromotion(payload).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        open.value = false
        getList()
      })
    } else {
      addPromotion(payload).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        open.value = false
        getList()
      })
    }
  })
}

function handleDelete(row) {
  proxy.$modal.confirm('是否确认删除活动编码为"' + row.promotionCode + '"的数据项？').then(() => {
    return delPromotion(row.promotionId)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

getList()
</script>
