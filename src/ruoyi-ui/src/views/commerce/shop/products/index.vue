<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="商品编码" prop="spuCode">
        <el-input
          v-model="queryParams.spuCode"
          placeholder="请输入商品编码"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="defaultName">
        <el-input
          v-model="queryParams.defaultName"
          placeholder="请输入商品名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="商品状态" clearable style="width: 200px">
          <el-option
            v-for="dict in commerce_spu_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList">
      <el-table-column label="SPU 编码" align="center" prop="spuCode" width="150" />
      <el-table-column label="商品名称" align="left" prop="defaultName" :show-overflow-tooltip="true">
        <template #default="scope">
          <div class="product-cell">
            <el-image
              v-if="scope.row.defaultImageUrl"
              :src="scope.row.defaultImageUrl"
              :preview-src-list="[scope.row.defaultImageUrl]"
              fit="cover"
              class="product-image"
              preview-teleported
            />
            <span>{{ scope.row.defaultName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="类目" align="center" prop="categoryName" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_spu_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="评分" align="center" prop="rating" width="100">
        <template #default="scope">
          <span>{{ scope.row.rating != null ? scope.row.rating : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="SKU 数" align="center" prop="skuCount" width="100" />
      <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleViewSku(scope.row)"
            v-hasPermi="['commerce:shop:product:query']"
          >查看 SKU</el-button>
          <el-button
            v-if="scope.row.status === 'ON_SALE'"
            link
            type="warning"
            icon="Bottom"
            @click="handleChangeStatus(scope.row, 'OFF_SALE')"
            v-hasPermi="['commerce:shop:product:edit']"
          >下架</el-button>
          <el-button
            v-else
            link
            type="success"
            icon="Top"
            @click="handleChangeStatus(scope.row, 'ON_SALE')"
            v-hasPermi="['commerce:shop:product:edit']"
          >上架</el-button>
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

    <!-- SKU 列表 -->
    <el-dialog v-model="skuOpen" title="SKU 列表" width="900px" append-to-body>
      <el-descriptions v-if="currentProduct" :column="3" border class="mb8">
        <el-descriptions-item label="商品编码">{{ currentProduct.spuCode }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ currentProduct.defaultName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="commerce_spu_status" :value="currentProduct.status" />
        </el-descriptions-item>
      </el-descriptions>
      <el-table v-loading="skuLoading" :data="skuList" border>
        <el-table-column label="SKU 编码" align="center" prop="skuCode" width="160" />
        <el-table-column label="条码" align="center" prop="barcode" width="140" />
        <el-table-column label="销售属性" align="center" prop="attributesJson" :show-overflow-tooltip="true" />
        <el-table-column label="重量(g)" align="center" prop="weightGrams" width="100" />
        <el-table-column label="申报价值" align="center" width="130">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.declaredValueAmount || 0, scope.row.declaredCurrencyCode || 'CNY') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" width="100">
          <template #default="scope">
            <dict-tag :options="commerce_sku_status" :value="scope.row.status" />
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="skuOpen = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ShopProduct">
import { listShopProduct, getShopProduct, changeShopProductStatus } from '@/api/commerce/shop/product'
import { formatMoney } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_spu_status, commerce_sku_status } = useDict('commerce_spu_status', 'commerce_sku_status')

const loading = ref(false)
const skuLoading = ref(false)
const showSearch = ref(true)
const productList = ref([])
const total = ref(0)
const skuOpen = ref(false)
const skuList = ref([])
const currentProduct = ref(null)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  spuCode: undefined,
  defaultName: undefined,
  status: undefined
})

function getList() {
  loading.value = true
  listShopProduct(queryParams.value).then(response => {
    productList.value = response.rows || []
    total.value = response.total || 0
  }).finally(() => {
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  queryParams.value.spuCode = undefined
  queryParams.value.defaultName = undefined
  queryParams.value.status = undefined
  handleQuery()
}

function handleViewSku(row) {
  currentProduct.value = row
  skuList.value = []
  skuOpen.value = true
  skuLoading.value = true
  getShopProduct(row.spuId).then(response => {
    const data = response.data || {}
    currentProduct.value = data.spu || row
    skuList.value = data.skus || []
  }).finally(() => {
    skuLoading.value = false
  })
}

function handleChangeStatus(row, status) {
  const action = status === 'ON_SALE' ? '上架' : '下架'
  proxy.$modal.confirm(`是否确认${action}商品"${row.defaultName}"？`).then(() => {
    return changeShopProductStatus(row.spuId, status)
  }).then(() => {
    proxy.$modal.msgSuccess(`${action}成功`)
    getList()
  }).catch(() => {})
}

getList()
</script>

<style scoped>
.product-cell {
  display: flex;
  align-items: center;
}
.product-image {
  width: 40px;
  height: 40px;
  margin-right: 10px;
  border-radius: 4px;
  flex-shrink: 0;
}
</style>
