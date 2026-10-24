<template>
  <div class="app-container" v-loading="loading">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="Check" :loading="saving" @click="handleSave" v-hasPermi="['commerce:platform:config:edit']">保存配置</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button icon="Refresh" @click="getList">刷新</el-button>
      </el-col>
      <el-col :span="10" class="tip">
        <span>修改配置后需刷新缓存方可生效</span>
      </el-col>
    </el-row>

    <el-empty v-if="!loading && groups.length === 0" description="暂无配置项" />

    <el-card v-for="group in groups" :key="group.name" shadow="never" class="mb8">
      <template #header>
        <span class="group-title">{{ group.label }}</span>
        <span class="group-name">{{ group.name }}</span>
      </template>
      <el-form label-width="280px" label-position="right">
        <el-form-item v-for="item in group.items" :key="item.configId || item.configKey">
          <template #label>
            <span class="config-key" :title="item.configKey">{{ item.configKey }}</span>
          </template>
          <el-input v-model="item.configValue" placeholder="请输入配置值" />
          <div class="config-remark">{{ item.remark || '暂无说明' }}</div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup name="PlatformSettings">
import { listPlatformConfig, updatePlatformConfig } from '@/api/commerce/platform/config'

const { proxy } = getCurrentInstance()

const configList = ref([])
const loading = ref(false)
const saving = ref(false)

const groupLabels = {
  GENERAL: '基础设置',
  ORDER: '订单设置',
  INVENTORY: '库存设置',
  SETTLEMENT: '结算设置',
  AFTER_SALE: '售后设置',
  RISK: '风控设置'
}

const groups = computed(() => {
  const result = []
  configList.value.forEach(item => {
    const name = item.configGroup || 'GENERAL'
    let group = result.find(entry => entry.name === name)
    if (!group) {
      group = { name, label: groupLabels[name] || name, items: [] }
      result.push(group)
    }
    group.items.push(item)
  })
  return result
})

/** 查询平台配置 */
function getList() {
  loading.value = true
  listPlatformConfig().then(response => {
    configList.value = response.rows || []
  }).finally(() => {
    loading.value = false
  })
}

/** 批量保存配置 */
function handleSave() {
  if (!configList.value.length) {
    proxy.$modal.msgWarning('暂无可保存的配置项')
    return
  }
  const payload = configList.value.map(item => ({
    configId: item.configId,
    configKey: item.configKey,
    configValue: item.configValue,
    configGroup: item.configGroup,
    remark: item.remark
  }))
  saving.value = true
  updatePlatformConfig(payload).then(() => {
    proxy.$modal.msgSuccess('保存成功，请刷新缓存后生效')
    getList()
  }).finally(() => {
    saving.value = false
  })
}

getList()
</script>

<style scoped>
.tip {
  display: flex;
  align-items: center;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
.group-title {
  font-weight: 600;
}
.group-name {
  margin-left: 10px;
  font-size: 12px;
  color: var(--el-text-color-placeholder);
}
.config-key {
  display: inline-block;
  max-width: 260px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
  font-family: Consolas, Monaco, monospace;
}
.config-remark {
  width: 100%;
  margin-top: 4px;
  font-size: 12px;
  line-height: 1.4;
  color: var(--el-text-color-secondary);
}
</style>
