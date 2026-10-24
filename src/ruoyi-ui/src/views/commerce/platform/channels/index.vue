<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="渠道编码" prop="channelCode">
        <el-input v-model="queryParams.channelCode" placeholder="请输入渠道编码" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="渠道名称" prop="channelName">
        <el-input v-model="queryParams.channelName" placeholder="请输入渠道名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="渠道状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
          <el-option v-for="dict in commerce_channel_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['commerce:platform:channel:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['commerce:platform:channel:remove']">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="channelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="渠道" min-width="200">
        <template #default="scope">
          <div class="channel-name">{{ scope.row.channelName }}</div>
          <div class="channel-code">{{ scope.row.channelCode }}</div>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" width="120">
        <template #default="scope">
          <dict-tag :options="commerce_channel_type" :value="scope.row.channelType" />
        </template>
      </el-table-column>
      <el-table-column label="国家" align="center" prop="countryCode" width="80" />
      <el-table-column label="币种" align="center" prop="currencyCode" width="80" />
      <el-table-column label="Endpoint" prop="endpoint" min-width="220" :show-overflow-tooltip="true" />
      <el-table-column label="AppKey" align="center" width="150">
        <template #default="scope">{{ maskAppKey(scope.row.appKey) }}</template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <dict-tag :options="commerce_channel_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="最近同步" align="center" width="170">
        <template #default="scope">{{ scope.row.lastSyncTime ? parseTime(scope.row.lastSyncTime) : '—' }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['commerce:platform:channel:edit']">修改</el-button>
          <el-button
            v-if="scope.row.status === 'ACTIVE'"
            link
            type="warning"
            icon="VideoPause"
            @click="handleToggleStatus(scope.row, 'PAUSED')"
            v-hasPermi="['commerce:platform:channel:edit']"
          >暂停</el-button>
          <el-button
            v-else
            link
            type="success"
            icon="VideoPlay"
            @click="handleToggleStatus(scope.row, 'ACTIVE')"
            v-hasPermi="['commerce:platform:channel:edit']"
          >启用</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['commerce:platform:channel:remove']">删除</el-button>
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
      <el-form ref="channelRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="渠道编码" prop="channelCode">
              <el-input v-model="form.channelCode" placeholder="如 AMAZON" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="渠道名称" prop="channelName">
              <el-input v-model="form.channelName" placeholder="请输入渠道名称" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="渠道类型" prop="channelType">
              <el-select v-model="form.channelType" placeholder="请选择渠道类型" style="width: 100%">
                <el-option v-for="dict in commerce_channel_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="渠道状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择渠道状态" style="width: 100%">
                <el-option v-for="dict in commerce_channel_status" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="国家/地区" prop="countryCode">
              <el-input v-model="form.countryCode" placeholder="如 US / DE / SG" maxlength="2" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-input v-model="form.currencyCode" placeholder="如 USD / EUR" maxlength="3" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="Endpoint" prop="endpoint">
              <el-input v-model="form.endpoint" placeholder="请输入接口地址" maxlength="255" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="AppKey" prop="appKey">
              <el-input v-model="form.appKey" placeholder="请输入应用 Key" maxlength="100" />
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

<script setup name="PlatformChannel">
import { listPlatformChannel, getPlatformChannel, addPlatformChannel, updatePlatformChannel, delPlatformChannel } from '@/api/commerce/platform/channel'

const { proxy } = getCurrentInstance()
const { commerce_channel_type, commerce_channel_status } = useDict('commerce_channel_type', 'commerce_channel_status')

const channelList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const multiple = ref(true)
const total = ref(0)
const title = ref('')

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    channelCode: undefined,
    channelName: undefined,
    status: undefined
  },
  rules: {
    channelCode: [{ required: true, message: '渠道编码不能为空', trigger: 'blur' }],
    channelName: [{ required: true, message: '渠道名称不能为空', trigger: 'blur' }],
    channelType: [{ required: true, message: '请选择渠道类型', trigger: 'change' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** AppKey 脱敏 */
function maskAppKey(value) {
  if (!value) {
    return '—'
  }
  if (value.length <= 6) {
    return '******'
  }
  return `${value.slice(0, 4)}****${value.slice(-4)}`
}

/** 查询渠道配置列表 */
function getList() {
  loading.value = true
  listPlatformChannel(queryParams.value).then(response => {
    channelList.value = response.rows
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
    channelConfigId: undefined,
    channelCode: undefined,
    channelName: undefined,
    channelType: 'DIRECT',
    countryCode: 'US',
    currencyCode: 'USD',
    endpoint: undefined,
    appKey: undefined,
    status: 'ACTIVE',
    remark: undefined
  }
  proxy.resetForm('channelRef')
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
  ids.value = selection.map(item => item.channelConfigId)
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = '新增渠道配置'
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const channelConfigId = row.channelConfigId || ids.value[0]
  getPlatformChannel(channelConfigId).then(response => {
    form.value = response.data
    open.value = true
    title.value = '修改渠道配置'
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs['channelRef'].validate(valid => {
    if (!valid) {
      return
    }
    if (form.value.channelConfigId != undefined) {
      updatePlatformChannel(form.value).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        open.value = false
        getList()
      })
    } else {
      addPlatformChannel(form.value).then(() => {
        proxy.$modal.msgSuccess('新增成功')
        open.value = false
        getList()
      })
    }
  })
}

/** 启用/暂停 */
function handleToggleStatus(row, status) {
  const action = status === 'ACTIVE' ? '启用' : '暂停'
  proxy.$modal.confirm(`确认${action}渠道"${row.channelName}"？`).then(() => {
    return updatePlatformChannel({
      channelConfigId: row.channelConfigId,
      channelCode: row.channelCode,
      channelName: row.channelName,
      channelType: row.channelType,
      countryCode: row.countryCode,
      currencyCode: row.currencyCode,
      endpoint: row.endpoint,
      appKey: row.appKey,
      status,
      remark: row.remark
    })
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess(`${action}成功`)
  }).catch(() => {})
}

/** 删除按钮操作 */
function handleDelete(row) {
  const channelConfigIds = row.channelConfigId || ids.value
  proxy.$modal.confirm('是否确认删除渠道配置编号为"' + channelConfigIds + '"的数据项？').then(() => {
    return delPlatformChannel(channelConfigIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {})
}

getList()
</script>

<style scoped>
.channel-name {
  font-weight: 500;
}
.channel-code {
  margin-top: 2px;
  font-size: 12px;
  color: var(--el-text-color-secondary);
}
</style>
