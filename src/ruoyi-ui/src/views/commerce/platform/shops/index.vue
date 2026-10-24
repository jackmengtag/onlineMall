<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="店铺编码" prop="shopCode">
        <el-input v-model="queryParams.shopCode" placeholder="请输入店铺编码" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="店铺名称" prop="shopName">
        <el-input v-model="queryParams.shopName" placeholder="请输入店铺名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="店铺状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
          <el-option v-for="dict in commerce_shop_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="店铺类型" prop="shopType">
        <el-select v-model="queryParams.shopType" placeholder="请选择类型" clearable style="width: 200px">
          <el-option v-for="dict in commerce_shop_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['commerce:platform:shop:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['commerce:platform:shop:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shopList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="店铺" min-width="220">
        <template #default="scope">
          <div class="shop-cell">
            <el-image v-if="scope.row.logoUrl" :src="scope.row.logoUrl" fit="cover" class="shop-logo" />
            <span>{{ scope.row.shopName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="编码" align="center" prop="shopCode" width="140" />
      <el-table-column label="类型" align="center" width="100">
        <template #default="scope">
          <dict-tag :options="commerce_shop_type" :value="scope.row.shopType" />
        </template>
      </el-table-column>
      <el-table-column label="国家" align="center" prop="countryCode" width="80" />
      <el-table-column label="币种" align="center" prop="currencyCode" width="80" />
      <el-table-column label="佣金率" align="center" width="100">
        <template #default="scope">{{ formatRate(scope.row.commissionRate) }}</template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <dict-tag :options="commerce_shop_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="已结算" align="right" width="170">
        <template #default="scope">{{ formatMoney(scope.row.settledAmount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="待结算" align="right" width="170">
        <template #default="scope">{{ formatMoney(scope.row.pendingAmount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="240" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['commerce:platform:shop:edit']">修改</el-button>
          <el-dropdown @command="status => handleStatusChange(scope.row, status)" v-hasPermi="['commerce:platform:shop:status']">
            <el-button link type="primary" icon="Switch">状态</el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="ACTIVE" :disabled="scope.row.status === 'ACTIVE'">启用</el-dropdown-item>
                <el-dropdown-item command="SUSPENDED" :disabled="scope.row.status === 'SUSPENDED'">暂停</el-dropdown-item>
                <el-dropdown-item command="CLOSED" :disabled="scope.row.status === 'CLOSED'">关闭</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['commerce:platform:shop:remove']">删除</el-button>
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
      <el-form ref="shopRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="店铺编码" prop="shopCode">
              <el-input v-model="form.shopCode" placeholder="请输入店铺编码" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="店铺名称" prop="shopName">
              <el-input v-model="form.shopName" placeholder="请输入店铺名称" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="店铺 Logo" prop="logoUrl">
              <el-input v-model="form.logoUrl" placeholder="请输入 Logo 图片地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="国家/地区" prop="countryCode">
              <el-input v-model="form.countryCode" placeholder="如 US / DE / SG" maxlength="2" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结算币种" prop="currencyCode">
              <el-input v-model="form.currencyCode" placeholder="如 USD / EUR" maxlength="3" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="店铺类型" prop="shopType">
              <el-select v-model="form.shopType" placeholder="请选择店铺类型" style="width: 100%">
                <el-option v-for="dict in commerce_shop_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="店铺状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择店铺状态" style="width: 100%">
                <el-option v-for="dict in commerce_shop_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="佣金率(%)" prop="commissionRate">
              <el-input-number v-model="form.commissionRate" :min="0" :max="100" :precision="2" :step="0.5" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" maxlength="500" show-word-limit />
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

<script setup name="PlatformShop">
import { listPlatformShop, getPlatformShop, addPlatformShop, updatePlatformShop, delPlatformShop, changePlatformShopStatus } from '@/api/commerce/platform/shop'
import { formatMoney } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_shop_type, commerce_shop_status } = useDict('commerce_shop_type', 'commerce_shop_status')

const shopList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const multiple = ref(true)
const total = ref(0)
const title = ref('')

const statusLabels = { ACTIVE: '启用', SUSPENDED: '暂停', CLOSED: '关闭' }

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    shopCode: undefined,
    shopName: undefined,
    status: undefined,
    shopType: undefined
  },
  rules: {
    shopCode: [{ required: true, message: '店铺编码不能为空', trigger: 'blur' }],
    shopName: [{ required: true, message: '店铺名称不能为空', trigger: 'blur' }],
    contactEmail: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }],
    shopType: [{ required: true, message: '请选择店铺类型', trigger: 'change' }],
    status: [{ required: true, message: '请选择店铺状态', trigger: 'change' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 佣金率显示（0.03 -> 3.00%） */
function formatRate(value) {
  if (value === null || value === undefined || value === '') {
    return '—'
  }
  return `${(Number(value) * 100).toFixed(2)}%`
}

/** 查询店铺列表 */
function getList() {
  loading.value = true
  listPlatformShop(queryParams.value).then(response => {
    shopList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    shopId: undefined,
    shopCode: undefined,
    shopName: undefined,
    logoUrl: undefined,
    contactName: undefined,
    contactEmail: undefined,
    countryCode: 'US',
    currencyCode: 'USD',
    shopType: 'SELF',
    commissionRate: 5,
    status: 'ACTIVE',
    settledAmount: 0,
    pendingAmount: 0,
    remark: undefined
  }
  proxy.resetForm('shopRef')
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.shopId)
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = '新增店铺'
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const shopId = row.shopId || ids.value[0]
  getPlatformShop(shopId).then(response => {
    const shop = response.data || {}
    form.value = {
      ...shop,
      commissionRate: shop.commissionRate !== null && shop.commissionRate !== undefined ? Number((Number(shop.commissionRate) * 100).toFixed(2)) : undefined
    }
    open.value = true
    title.value = '修改店铺'
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs['shopRef'].validate(valid => {
    if (!valid) {
      return
    }
    const payload = {
      ...form.value,
      commissionRate: form.value.commissionRate !== null && form.value.commissionRate !== undefined && form.value.commissionRate !== ''
        ? Number((form.value.commissionRate / 100).toFixed(4))
        : 0
    }
    if (form.value.shopId != undefined) {
      updatePlatformShop(payload).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        open.value = false
        getList()
      })
    } else {
      addPlatformShop(payload).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        open.value = false
        getList()
      })
    }
  })
}

/** 状态变更 */
function handleStatusChange(row, status) {
  proxy.$modal.confirm(`确认将店铺"${row.shopName}"${statusLabels[status] || status}？`).then(() => {
    return changePlatformShopStatus(row.shopId, status)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('状态修改成功')
  }).catch(() => {})
}

/** 删除按钮操作 */
function handleDelete(row) {
  const shopIds = row.shopId || ids.value
  proxy.$modal.confirm('是否确认删除店铺编号为"' + shopIds + '"的数据项？').then(() => {
    return delPlatformShop(shopIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

getList()
</script>

<style scoped>
.shop-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}
.shop-logo {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  flex-shrink: 0;
}
</style>
