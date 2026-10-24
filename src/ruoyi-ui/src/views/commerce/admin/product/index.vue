<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="SPU 编码" prop="spuCode">
        <el-input v-model="queryParams.spuCode" placeholder="请输入 SPU 编码" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="商品名称" prop="defaultName">
        <el-input v-model="queryParams.defaultName" placeholder="请输入商品名称" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 160px">
          <el-option v-for="dict in commerce_spu_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="类目" prop="categoryKey">
        <el-input v-model="queryParams.categoryKey" placeholder="请输入类目键" clearable style="width: 160px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="店铺 ID" prop="shopId">
        <el-input-number v-model="queryParams.shopId" :min="1" :controls="false" placeholder="请输入店铺 ID" style="width: 160px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['commerce:admin:product:add']">新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList">
      <el-table-column label="SPU 编码" align="center" prop="spuCode" min-width="150" show-overflow-tooltip />
      <el-table-column label="商品名称" align="center" prop="defaultName" min-width="180" show-overflow-tooltip />
      <el-table-column label="店铺 ID" align="center" prop="shopId" width="100" />
      <el-table-column label="品牌" align="center" prop="brandName" min-width="120" show-overflow-tooltip />
      <el-table-column label="类目" align="center" prop="categoryName" min-width="120" show-overflow-tooltip />
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <dict-tag :options="commerce_spu_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="评分" align="center" prop="rating" width="90" />
      <el-table-column label="SKU 数" align="center" prop="skuCount" width="90" />
      <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['commerce:admin:product:edit']">编辑</el-button>
          <el-button link type="primary" icon="Goods" @click="openSkuDialog(scope.row)" v-hasPermi="['commerce:admin:product:list']">SKU 管理</el-button>
          <el-button
            v-if="statusAction(scope.row)"
            link
            type="warning"
            :icon="statusAction(scope.row).icon"
            @click="handleStatusChange(scope.row)"
            v-hasPermi="['commerce:admin:product:edit']"
          >{{ statusAction(scope.row).label }}</el-button>
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

    <!-- 新增 / 编辑 SPU -->
    <el-dialog :title="title" v-model="open" width="760px" append-to-body>
      <el-form ref="productRef" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="店铺 ID" prop="shopId">
              <el-input-number v-model="form.shopId" :min="1" :controls="false" placeholder="请输入店铺 ID" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="SPU 编码" prop="spuCode">
              <el-input v-model="form.spuCode" placeholder="请输入 SPU 编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌名称" prop="brandName">
              <el-input v-model="form.brandName" placeholder="请输入品牌名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类目键" prop="categoryKey">
              <el-input v-model="form.categoryKey" placeholder="请输入类目键" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类目名称" prop="categoryName">
              <el-input v-model="form.categoryName" placeholder="请输入类目名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角标" prop="badge">
              <el-input v-model="form.badge" placeholder="如 NEW / HOT" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品名称" prop="defaultName">
              <el-input v-model="form.defaultName" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="英文名称" prop="nameEn">
              <el-input v-model="form.nameEn" placeholder="请输入英文名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评分" prop="rating">
              <el-input-number v-model="form.rating" :min="0" :max="5" :precision="2" :step="0.1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主图地址" prop="defaultImageUrl">
              <el-input v-model="form.defaultImageUrl" placeholder="请输入主图 URL" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述" prop="defaultDescription">
              <el-input v-model="form.defaultDescription" type="textarea" :rows="2" placeholder="请输入商品描述" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卖点标签 JSON" prop="tagsJson">
              <el-input v-model="form.tagsJson" type="textarea" :rows="3" placeholder='如 ["新品","包邮"]' />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="图片列表 JSON" prop="imagesJson">
              <el-input v-model="form.imagesJson" type="textarea" :rows="3" placeholder='如 ["https://..."]' />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格参数 JSON" prop="specsJson">
              <el-input v-model="form.specsJson" type="textarea" :rows="3" placeholder='如 {"材质":"棉"}' />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="颜色选项 JSON" prop="colorsJson">
              <el-input v-model="form.colorsJson" type="textarea" :rows="3" placeholder='如 ["黑色","白色"]' />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="尺寸选项 JSON" prop="sizesJson">
              <el-input v-model="form.sizesJson" type="textarea" :rows="3" placeholder='如 ["S","M","L"]' />
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

    <!-- SKU 管理 -->
    <el-dialog :title="'SKU 管理 - ' + (currentSpu.defaultName || '')" v-model="skuOpen" width="980px" append-to-body>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="Plus" @click="handleAddSku" v-hasPermi="['commerce:admin:product:edit']">新增 SKU</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="skuLoading" :data="skuList" size="small" border>
        <el-table-column label="SKU 编码" align="center" prop="skuCode" min-width="140" />
        <el-table-column label="条码" align="center" prop="barcode" min-width="120" show-overflow-tooltip />
        <el-table-column label="销售属性" align="center" prop="attributesJson" min-width="150" show-overflow-tooltip />
        <el-table-column label="状态" align="center" width="90">
          <template #default="scope">
            <dict-tag :options="commerce_sku_status" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="申报品名" align="center" prop="declaredName" min-width="120" show-overflow-tooltip />
        <el-table-column label="申报价值" align="center" width="120">
          <template #default="scope">{{ formatMoney(scope.row.declaredValueAmount, scope.row.declaredCurrencyCode) }}</template>
        </el-table-column>
        <el-table-column label="危险品" align="center" width="90">
          <template #default="scope">{{ scope.row.dangerousGoodsFlag ? '是' : '否' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="handleUpdateSku(scope.row)" v-hasPermi="['commerce:admin:product:edit']">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDeleteSku(scope.row)" v-hasPermi="['commerce:admin:product:edit']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="skuTotal > 0"
        :total="skuTotal"
        v-model:page="skuQuery.pageNum"
        v-model:limit="skuQuery.pageSize"
        @pagination="getSkuList"
      />
      <template #footer>
        <el-button @click="skuOpen = false">关 闭</el-button>
      </template>
    </el-dialog>

    <!-- 新增 / 编辑 SKU -->
    <el-dialog :title="skuTitle" v-model="skuFormOpen" width="760px" append-to-body>
      <el-form ref="skuFormRef" :model="skuForm" :rules="skuRules" label-width="115px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="SKU 编码" prop="skuCode">
              <el-input v-model="skuForm.skuCode" placeholder="请输入 SKU 编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="条码" prop="barcode">
              <el-input v-model="skuForm.barcode" placeholder="请输入条码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="skuForm.status" placeholder="请选择状态" style="width: 100%">
                <el-option v-for="dict in commerce_sku_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售属性 JSON" prop="attributesJson">
              <el-input v-model="skuForm.attributesJson" placeholder='如 {"颜色":"黑色","尺寸":"M"}' />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重量(克)" prop="weightGrams">
              <el-input-number v-model="skuForm.weightGrams" :min="0" :precision="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="长(mm)" prop="lengthMm">
              <el-input-number v-model="skuForm.lengthMm" :min="0" :precision="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宽(mm)" prop="widthMm">
              <el-input-number v-model="skuForm.widthMm" :min="0" :precision="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="高(mm)" prop="heightMm">
              <el-input-number v-model="skuForm.heightMm" :min="0" :precision="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原产国" prop="originCountryCode">
              <el-input v-model="skuForm.originCountryCode" placeholder="如 CN / US" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="HS 编码" prop="hsCode">
              <el-input v-model="skuForm.hsCode" placeholder="请输入 HS 编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="材质" prop="material">
              <el-input v-model="skuForm.material" placeholder="请输入材质" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电池类型" prop="batteryType">
              <el-input v-model="skuForm.batteryType" placeholder="如 LITHIUM / NONE" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">申报信息</el-divider>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申报品名" prop="declaredName">
              <el-input v-model="skuForm.declaredName" placeholder="请输入申报品名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申报价值(元)" prop="declaredValueYuan">
              <el-input-number v-model="skuForm.declaredValueYuan" :min="0" :precision="2" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申报币种" prop="declaredCurrencyCode">
              <el-input v-model="skuForm.declaredCurrencyCode" placeholder="如 USD / CNY" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="危险品" prop="dangerousGoodsFlag">
              <el-switch v-model="skuForm.dangerousGoodsFlag" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitSku">确 定</el-button>
          <el-button @click="skuFormOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CommerceAdminProduct">
import {
  listProduct,
  getProduct,
  addProduct,
  updateProduct,
  changeProductStatus,
  listSku,
  getSku,
  addSku,
  updateSku,
  delSku
} from '@/api/commerce/admin/product'
import { formatMoney, fromMinor, toMinor } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_spu_status, commerce_sku_status } = useDict('commerce_spu_status', 'commerce_sku_status')

const productList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const open = ref(false)
const title = ref('')
const skuOpen = ref(false)
const skuFormOpen = ref(false)
const skuTitle = ref('')
const skuLoading = ref(false)
const skuList = ref([])
const skuTotal = ref(0)
const currentSpu = ref({})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    spuCode: undefined,
    defaultName: undefined,
    status: undefined,
    categoryKey: undefined,
    shopId: undefined
  },
  rules: {
    shopId: [{ required: true, message: '店铺 ID 不能为空', trigger: 'blur' }],
    spuCode: [{ required: true, message: 'SPU 编码不能为空', trigger: 'blur' }],
    defaultName: [{ required: true, message: '商品名称不能为空', trigger: 'blur' }]
  },
  skuQuery: {
    pageNum: 1,
    pageSize: 10,
    spuId: undefined
  },
  skuForm: {},
  skuRules: {
    skuCode: [{ required: true, message: 'SKU 编码不能为空', trigger: 'blur' }],
    status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
  }
})

const { queryParams, form, rules, skuQuery, skuForm, skuRules } = toRefs(data)

function getList() {
  loading.value = true
  listProduct(queryParams.value).then(response => {
    productList.value = response.rows
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

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    spuId: undefined,
    shopId: undefined,
    spuCode: undefined,
    brandName: undefined,
    categoryKey: undefined,
    categoryName: undefined,
    defaultName: undefined,
    nameEn: undefined,
    badge: undefined,
    rating: 5,
    defaultDescription: undefined,
    defaultImageUrl: undefined,
    tagsJson: undefined,
    imagesJson: undefined,
    specsJson: undefined,
    colorsJson: undefined,
    sizesJson: undefined
  }
  proxy.resetForm('productRef')
}

function handleAdd() {
  reset()
  open.value = true
  title.value = '新增商品'
}

function handleUpdate(row) {
  reset()
  getProduct(row.spuId).then(response => {
    form.value = { ...(response.data.spu || {}) }
    open.value = true
    title.value = '编辑商品'
    nextTick(() => proxy.$refs.productRef?.clearValidate())
  })
}

function submitForm() {
  proxy.$refs['productRef'].validate(valid => {
    if (!valid) {
      return
    }
    const payload = {
      spuId: form.value.spuId,
      shopId: form.value.shopId,
      spuCode: form.value.spuCode,
      brandName: form.value.brandName,
      categoryKey: form.value.categoryKey,
      categoryName: form.value.categoryName,
      defaultName: form.value.defaultName,
      nameEn: form.value.nameEn,
      badge: form.value.badge,
      rating: form.value.rating,
      defaultDescription: form.value.defaultDescription,
      defaultImageUrl: form.value.defaultImageUrl,
      tagsJson: form.value.tagsJson,
      imagesJson: form.value.imagesJson,
      specsJson: form.value.specsJson,
      colorsJson: form.value.colorsJson,
      sizesJson: form.value.sizesJson,
      status: form.value.status
    }
    if (form.value.spuId != undefined) {
      updateProduct(payload).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        open.value = false
        getList()
      })
    } else {
      addProduct(payload).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        open.value = false
        getList()
      })
    }
  })
}

function statusAction(row) {
  const actions = {
    DRAFT: { label: '发布', status: 'PUBLISHED', icon: 'Upload' },
    PUBLISHED: { label: '上架', status: 'ON_SALE', icon: 'Top' },
    ON_SALE: { label: '下架', status: 'OFF_SALE', icon: 'Bottom' },
    SUSPENDED: { label: '上架', status: 'ON_SALE', icon: 'Top' },
    OFF_SALE: { label: '恢复草稿', status: 'DRAFT', icon: 'RefreshLeft' }
  }
  return actions[row.status]
}

function handleStatusChange(row) {
  const action = statusAction(row)
  if (!action) {
    return
  }
  proxy.$modal.confirm('是否确认将商品"' + row.defaultName + '"' + action.label + '？').then(() => {
    return changeProductStatus(row.spuId, action.status)
  }).then(() => {
    proxy.$modal.msgSuccess('操作成功')
    getList()
  }).catch(() => {})
}

function openSkuDialog(row) {
  currentSpu.value = row
  skuQuery.value.spuId = row.spuId
  skuQuery.value.pageNum = 1
  skuList.value = []
  skuTotal.value = 0
  skuOpen.value = true
  getSkuList()
}

function getSkuList() {
  skuLoading.value = true
  listSku(skuQuery.value).then(response => {
    skuList.value = response.rows
    skuTotal.value = response.total
    skuLoading.value = false
  })
}

function resetSkuForm() {
  skuForm.value = {
    skuId: undefined,
    spuId: currentSpu.value.spuId,
    skuCode: undefined,
    barcode: undefined,
    attributesJson: undefined,
    weightGrams: undefined,
    lengthMm: undefined,
    widthMm: undefined,
    heightMm: undefined,
    originCountryCode: undefined,
    hsCode: undefined,
    material: undefined,
    batteryType: undefined,
    dangerousGoodsFlag: false,
    declaredName: undefined,
    declaredValueYuan: undefined,
    declaredCurrencyCode: 'USD',
    status: 'ACTIVE'
  }
}

function handleAddSku() {
  resetSkuForm()
  skuTitle.value = '新增 SKU'
  skuFormOpen.value = true
  nextTick(() => proxy.$refs.skuFormRef?.clearValidate())
}

function handleUpdateSku(row) {
  resetSkuForm()
  getSku(row.skuId).then(response => {
    const sku = response.data || {}
    skuForm.value = {
      ...sku,
      declaredValueYuan: fromMinor(sku.declaredValueAmount)
    }
    skuTitle.value = '编辑 SKU'
    skuFormOpen.value = true
    nextTick(() => proxy.$refs.skuFormRef?.clearValidate())
  })
}

function submitSku() {
  proxy.$refs['skuFormRef'].validate(valid => {
    if (!valid) {
      return
    }
    const payload = {
      skuId: skuForm.value.skuId,
      spuId: skuForm.value.spuId,
      skuCode: skuForm.value.skuCode,
      barcode: skuForm.value.barcode,
      attributesJson: skuForm.value.attributesJson,
      weightGrams: skuForm.value.weightGrams,
      lengthMm: skuForm.value.lengthMm,
      widthMm: skuForm.value.widthMm,
      heightMm: skuForm.value.heightMm,
      originCountryCode: skuForm.value.originCountryCode,
      hsCode: skuForm.value.hsCode,
      material: skuForm.value.material,
      batteryType: skuForm.value.batteryType,
      dangerousGoodsFlag: skuForm.value.dangerousGoodsFlag,
      declaredName: skuForm.value.declaredName,
      declaredValueAmount: toMinor(skuForm.value.declaredValueYuan),
      declaredCurrencyCode: skuForm.value.declaredCurrencyCode,
      status: skuForm.value.status
    }
    const request = skuForm.value.skuId != undefined ? updateSku(payload) : addSku(payload)
    request.then(() => {
      proxy.$modal.msgSuccess(skuForm.value.skuId != undefined ? '修改成功' : '新增成功')
      skuFormOpen.value = false
      getSkuList()
      getList()
    })
  })
}

function handleDeleteSku(row) {
  proxy.$modal.confirm('是否确认删除 SKU"' + row.skuCode + '"？').then(() => {
    return delSku(row.skuId)
  }).then(() => {
    getSkuList()
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

getList()
</script>
