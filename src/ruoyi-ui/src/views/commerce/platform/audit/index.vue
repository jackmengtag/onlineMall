<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="模块" prop="module">
        <el-select v-model="queryParams.module" placeholder="请选择模块" clearable style="width: 160px">
          <el-option v-for="item in moduleOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="店铺 ID" prop="shopId">
        <el-input v-model="queryParams.shopId" placeholder="请输入店铺 ID" clearable style="width: 160px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 260px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="auditList">
      <el-table-column label="时间" align="center" width="170">
        <template #default="scope">{{ parseTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="模块" align="center" width="110">
        <template #default="scope">{{ formatModule(scope.row.module) }}</template>
      </el-table-column>
      <el-table-column label="动作" align="center" prop="action" width="110" />
      <el-table-column label="对象" min-width="160" :show-overflow-tooltip="true">
        <template #default="scope">
          <span>{{ scope.row.targetType || '—' }}</span>
          <span v-if="scope.row.targetId" class="target-id">#{{ scope.row.targetId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="center" prop="operator" width="120" />
      <el-table-column label="结果" align="center" width="100">
        <template #default="scope">
          <dict-tag :options="commerce_audit_result" :value="scope.row.result" />
        </template>
      </el-table-column>
      <el-table-column label="IP" align="center" prop="ip" width="140" />
      <el-table-column label="详情" align="center" width="100" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">查看</el-button>
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

    <el-dialog title="审计日志详情" v-model="open" width="640px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="时间">{{ parseTime(detail.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="模块">{{ formatModule(detail.module) }}</el-descriptions-item>
        <el-descriptions-item label="动作">{{ detail.action }}</el-descriptions-item>
        <el-descriptions-item label="结果">
          <dict-tag :options="commerce_audit_result" :value="detail.result" />
        </el-descriptions-item>
        <el-descriptions-item label="对象类型">{{ detail.targetType || '—' }}</el-descriptions-item>
        <el-descriptions-item label="对象 ID">{{ detail.targetId || '—' }}</el-descriptions-item>
        <el-descriptions-item label="操作人">{{ detail.operator || '—' }}</el-descriptions-item>
        <el-descriptions-item label="店铺 ID">{{ detail.shopId || '平台级' }}</el-descriptions-item>
        <el-descriptions-item label="IP" :span="2">{{ detail.ip || '—' }}</el-descriptions-item>
        <el-descriptions-item label="详情" :span="2">
          <div class="detail-text">{{ detail.detail || '—' }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="open = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="PlatformAudit">
import { listPlatformAudit } from '@/api/commerce/platform/audit'

const { commerce_audit_result } = useDict('commerce_audit_result')

const moduleOptions = [
  { label: '店铺', value: 'SHOP' },
  { label: '渠道', value: 'CHANNEL' },
  { label: '平台设置', value: 'CONFIG' }
]

const auditList = ref([])
const dateRange = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const detail = ref({})

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    module: undefined,
    shopId: undefined
  }
})

const { queryParams } = toRefs(data)

/** 模块名称格式化 */
function formatModule(module) {
  if (!module) {
    return '—'
  }
  const item = moduleOptions.find(option => option.value === module)
  return item ? item.label : module
}

/** 查询审计日志列表 */
function getList() {
  loading.value = true
  listPlatformAudit({
    ...queryParams.value,
    beginTime: dateRange.value && dateRange.value.length ? dateRange.value[0] : undefined,
    endTime: dateRange.value && dateRange.value.length ? dateRange.value[1] : undefined
  }).then(response => {
    auditList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = []
  queryParams.value.module = undefined
  queryParams.value.shopId = undefined
  handleQuery()
}

/** 查看详情 */
function handleView(row) {
  detail.value = { ...row }
  open.value = true
}

getList()
</script>

<style scoped>
.target-id {
  margin-left: 6px;
  color: var(--el-text-color-secondary);
}
.detail-text {
  white-space: pre-wrap;
  word-break: break-all;
  line-height: 1.6;
}
</style>
