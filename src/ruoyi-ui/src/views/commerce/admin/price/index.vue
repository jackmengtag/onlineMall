<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="SKU" prop="skuId">
        <el-select
          v-model="queryParams.skuId"
          filterable
          remote
          clearable
          :remote-method="searchSkus"
          :loading="skuLoading"
          placeholder="输入 SKU 编码搜索"
          style="width: 220px"
          @focus="searchSkus('')"
        >
          <el-option
            v-for="sku in skuOptions"
            :key="sku.skuId"
            :label="sku.skuCode + ' ' + (sku.spuName || '')"
            :value="sku.skuId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="国家" prop="countryCode">
        <el-input v-model="queryParams.countryCode" placeholder="请输入国家编码" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="渠道" prop="channelCode">
        <el-input v-model="queryParams.channelCode" placeholder="请输入渠道编码" clearable style="width: 150px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="币种" prop="currencyCode">
        <el-input v-model="queryParams.currencyCode" placeholder="请输入币种" clearable style="width: 130px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 150px">
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
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['commerce:admin:price:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Search" @click="openResolve" v-hasPermi="['commerce:admin:price:query']">解析价格</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="priceList">
      <el-table-column label="SKU 编码" align="center" prop="skuCode" min-width="150" show-overflow-tooltip />
      <el-table-column label="商品名称" align="center" prop="spuName" min-width="160" show-overflow-tooltip />
      <el-table-column label="国家" align="center" prop="countryCode" width="90" />
      <el-table-column label="渠道" align="center" prop="channelCode" width="100" />
      <el-table-column label="币种" align="center" prop="currencyCode" width="80" />
      <el-table-column label="划线价" align="center" width="130">
        <template #default="scope">{{ formatMoney(scope.row.listAmount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="销售价" align="center" width="130">
        <template #default="scope">{{ formatMoney(scope.row.saleAmount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="生效时间" align="center" width="160">
        <template #default="scope">{{ parseTime(scope.row.effectiveFrom) }}</template>
      </el-table-column>
      <el-table-column label="失效时间" align="center" width="160">
        <template #default="scope">{{ parseTime(scope.row.effectiveTo) }}</template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="90">
        <template #default="scope">
          <dict-tag :options="commerce_biz_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['commerce:admin:price:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['commerce:admin:price:remove']">删除</el-button>
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

    <!-- 新增 / 修改价格 -->
    <el-dialog :title="title" v-model="open" width="620px" append-to-body>
      <el-form ref="priceRef" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="SKU" prop="skuId">
              <el-select
                v-model="form.skuId"
                filterable
                remote
                clearable
                :remote-method="searchSkus"
                :loading="skuLoading"
                placeholder="输入 SKU 编码搜索"
                style="width: 100%"
                @focus="searchSkus('')"
              >
                <el-option
                  v-for="sku in skuOptions"
                  :key="sku.skuId"
                  :label="sku.skuCode + ' ' + (sku.spuName || '')"
                  :value="sku.skuId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="国家" prop="countryCode">
              <el-input v-model="form.countryCode" placeholder="如 US / CN" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="渠道" prop="channelCode">
              <el-input v-model="form.channelCode" placeholder="如 DIRECT" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-input v-model="form.currencyCode" placeholder="如 USD / CNY" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option v-for="dict in commerce_biz_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="划线价(元)" prop="listAmountYuan">
              <el-input-number v-model="form.listAmountYuan" :min="0" :precision="2" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售价(元)" prop="saleAmountYuan">
              <el-input-number v-model="form.saleAmountYuan" :min="0" :precision="2" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效时间" prop="effectiveFrom">
              <el-date-picker v-model="form.effectiveFrom" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择生效时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="失效时间" prop="effectiveTo">
              <el-date-picker v-model="form.effectiveTo" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择失效时间" style="width: 100%" />
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

    <!-- 解析价格 -->
    <el-dialog title="解析价格" v-model="resolveOpen" width="620px" append-to-body>
      <el-form :model="resolveQuery" :inline="true">
        <el-form-item label="SKU">
          <el-select
            v-model="resolveQuery.skuId"
            filterable
            remote
            clearable
            :remote-method="searchSkus"
            :loading="skuLoading"
            placeholder="输入 SKU 编码搜索"
            style="width: 220px"
            @focus="searchSkus('')"
          >
            <el-option
              v-for="sku in skuOptions"
              :key="sku.skuId"
              :label="sku.skuCode + ' ' + (sku.spuName || '')"
              :value="sku.skuId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="国家">
          <el-input v-model="resolveQuery.countryCode" placeholder="国家编码" style="width: 120px" />
        </el-form-item>
        <el-form-item label="渠道">
          <el-input v-model="resolveQuery.channelCode" placeholder="渠道编码" style="width: 120px" />
        </el-form-item>
        <el-form-item label="币种">
          <el-input v-model="resolveQuery.currencyCode" placeholder="币种" style="width: 100px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" :loading="resolveLoading" @click="handleResolve">解析</el-button>
        </el-form-item>
      </el-form>

      <el-descriptions v-if="resolvedPrice" :column="2" border>
        <el-descriptions-item label="SKU ID">{{ resolvedPrice.skuId }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ resolvedPrice.currencyCode }}</el-descriptions-item>
        <el-descriptions-item label="国家">{{ resolvedPrice.countryCode }}</el-descriptions-item>
        <el-descriptions-item label="渠道">{{ resolvedPrice.channelCode }}</el-descriptions-item>
        <el-descriptions-item label="划线价">{{ formatMoney(resolvedPrice.listAmount, resolvedPrice.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="销售价">{{ formatMoney(resolvedPrice.saleAmount, resolvedPrice.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="生效时间">{{ parseTime(resolvedPrice.effectiveFrom) }}</el-descriptions-item>
        <el-descriptions-item label="失效时间">{{ parseTime(resolvedPrice.effectiveTo) }}</el-descriptions-item>
      </el-descriptions>
      <el-empty v-else-if="resolveDone" description="未找到生效价格" :image-size="60" />
      <div v-else class="resolve-tip">请选择 SKU 后点击解析</div>

      <template #footer>
        <el-button @click="resolveOpen = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CommerceAdminPrice">
import { listPrice, getPrice, addPrice, updatePrice, delPrice, resolvePrice } from '@/api/commerce/admin/price'
import { listSkuOptions } from '@/api/commerce/admin/product'
import { formatMoney, fromMinor, toMinor } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_biz_status } = useDict('commerce_biz_status')

const priceList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const open = ref(false)
const title = ref('')
const skuOptions = ref([])
const skuLoading = ref(false)
const resolveOpen = ref(false)
const resolveLoading = ref(false)
const resolvedPrice = ref(null)
const resolveDone = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    skuId: undefined,
    countryCode: undefined,
    channelCode: undefined,
    currencyCode: undefined,
    status: undefined
  },
  rules: {
    skuId: [{ required: true, message: 'SKU 不能为空', trigger: 'change' }],
    countryCode: [{ required: true, message: '国家不能为空', trigger: 'blur' }],
    channelCode: [{ required: true, message: '渠道不能为空', trigger: 'blur' }],
    currencyCode: [{ required: true, message: '币种不能为空', trigger: 'blur' }],
    saleAmountYuan: [{ required: true, message: '销售价不能为空', trigger: 'blur' }],
    effectiveFrom: [{ required: true, message: '生效时间不能为空', trigger: 'change' }],
    status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
  },
  resolveQuery: {
    skuId: undefined,
    countryCode: undefined,
    channelCode: undefined,
    currencyCode: undefined
  }
})

const { queryParams, form, rules, resolveQuery } = toRefs(data)

function getList() {
  loading.value = true
  listPrice(queryParams.value).then(response => {
    priceList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

function searchSkus(keyword) {
  skuLoading.value = true
  listSkuOptions({ keyword: keyword || undefined })
    .then(response => {
      skuOptions.value = response.data || []
    })
    .finally(() => {
      skuLoading.value = false
    })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    priceId: undefined,
    skuId: undefined,
    countryCode: undefined,
    channelCode: undefined,
    currencyCode: 'USD',
    listAmountYuan: undefined,
    saleAmountYuan: undefined,
    effectiveFrom: undefined,
    effectiveTo: undefined,
    status: 'ACTIVE'
  }
  proxy.resetForm('priceRef')
}

function handleAdd() {
  reset()
  open.value = true
  title.value = '新增价格'
}

function handleUpdate(row) {
  reset()
  getPrice(row.priceId).then(response => {
    const price = response.data || {}
    form.value = {
      ...price,
      listAmountYuan: fromMinor(price.listAmount),
      saleAmountYuan: fromMinor(price.saleAmount)
    }
    open.value = true
    title.value = '修改价格'
    nextTick(() => proxy.$refs.priceRef?.clearValidate())
  })
}

function submitForm() {
  proxy.$refs['priceRef'].validate(valid => {
    if (!valid) {
      return
    }
    const payload = {
      priceId: form.value.priceId,
      skuId: form.value.skuId,
      countryCode: form.value.countryCode,
      channelCode: form.value.channelCode,
      currencyCode: form.value.currencyCode,
      listAmount: toMinor(form.value.listAmountYuan),
      saleAmount: toMinor(form.value.saleAmountYuan),
      effectiveFrom: form.value.effectiveFrom,
      effectiveTo: form.value.effectiveTo,
      status: form.value.status
    }
    if (form.value.priceId != undefined) {
      updatePrice(payload).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        open.value = false
        getList()
      })
    } else {
      addPrice(payload).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        open.value = false
        getList()
      })
    }
  })
}

function handleDelete(row) {
  proxy.$modal.confirm('是否确认删除该价格配置？').then(() => {
    return delPrice(row.priceId)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

function openResolve() {
  resolveQuery.value = {
    skuId: queryParams.value.skuId,
    countryCode: queryParams.value.countryCode,
    channelCode: queryParams.value.channelCode,
    currencyCode: queryParams.value.currencyCode
  }
  resolvedPrice.value = null
  resolveDone.value = false
  resolveOpen.value = true
  searchSkus('')
}

function handleResolve() {
  if (!resolveQuery.value.skuId) {
    proxy.$modal.msgWarning('请选择 SKU')
    return
  }
  resolveLoading.value = true
  resolvePrice(resolveQuery.value)
    .then(response => {
      resolvedPrice.value = response.data || null
      resolveDone.value = true
    })
    .finally(() => {
      resolveLoading.value = false
    })
}

getList()
</script>

<style scoped>
.resolve-tip {
  padding: 30px 0;
  text-align: center;
  color: #909399;
}
</style>
