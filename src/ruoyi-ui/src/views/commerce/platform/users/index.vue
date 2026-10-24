<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="用户名" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入用户名或昵称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="绑定店铺" prop="shopId">
        <el-select v-model="queryParams.shopId" placeholder="请选择店铺" clearable filterable style="width: 240px">
          <el-option v-for="shop in shopOptions" :key="shop.shopId" :label="shop.shopName" :value="shop.shopId" />
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

    <el-table v-loading="loading" :data="userList">
      <el-table-column label="用户 ID" align="center" prop="userId" width="90" />
      <el-table-column label="用户名" prop="userName" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="昵称" prop="nickName" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="手机号" align="center" prop="phonenumber" width="140" />
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="绑定店铺" prop="shopName" min-width="180" :show-overflow-tooltip="true" />
      <el-table-column label="店铺角色" align="center" width="120">
        <template #default="scope">{{ formatShopRole(scope.row.shopRole) }}</template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)">查看绑定</el-button>
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

    <el-dialog title="用户绑定详情" v-model="open" width="640px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户 ID">{{ detail.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detail.userName }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ detail.nickName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.phonenumber }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detail.email }}</el-descriptions-item>
        <el-descriptions-item label="用户状态">
          <dict-tag :options="sys_normal_disable" :value="detail.status" />
        </el-descriptions-item>
        <el-descriptions-item label="店铺 ID">{{ detail.shopId || '—' }}</el-descriptions-item>
        <el-descriptions-item label="绑定店铺">{{ detail.shopName || '—' }}</el-descriptions-item>
        <el-descriptions-item label="店铺角色">{{ formatShopRole(detail.shopRole) }}</el-descriptions-item>
        <el-descriptions-item label="绑定状态">{{ detail.shopUserStatus || '—' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ parseTime(detail.createTime) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="open = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="PlatformUser">
import { listPlatformUser } from '@/api/commerce/platform/user'
import { listPlatformShop } from '@/api/commerce/platform/shop'

const { sys_normal_disable } = useDict('sys_normal_disable')

const userList = ref([])
const shopOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const detail = ref({})

const shopRoleLabels = {
  OWNER: '店主',
  OPERATOR: '运营',
  CUSTOMER_SERVICE: '客服',
  FINANCE: '财务'
}

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userName: undefined,
    shopId: undefined
  }
})

const { queryParams } = toRefs(data)

/** 查询用户绑定列表 */
function getList() {
  loading.value = true
  listPlatformUser(queryParams.value).then(response => {
    userList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 查询店铺下拉数据 */
function getShopOptions() {
  listPlatformShop({ pageNum: 1, pageSize: 1000 }).then(response => {
    shopOptions.value = response.rows || []
  })
}

/** 店铺角色格式化 */
function formatShopRole(role) {
  if (!role) {
    return '—'
  }
  return shopRoleLabels[role] || role
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.userName = undefined
  queryParams.value.shopId = undefined
  handleQuery()
}

/** 查看绑定详情 */
function handleView(row) {
  detail.value = { ...row }
  open.value = true
}

getShopOptions()
getList()
</script>
