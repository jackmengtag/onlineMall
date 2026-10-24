<template>
  <div class="app-container">
    <el-card shadow="never" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>店铺资料</span>
          <div>
            <el-button type="primary" :loading="saving" @click="submitForm" v-hasPermi="['commerce:shop:profile:edit']">保 存</el-button>
            <el-button @click="getProfile">重 置</el-button>
          </div>
        </div>
      </template>

      <el-form ref="profileRef" :model="form" :rules="rules" label-width="110px" class="profile-form">
        <el-form-item label="店铺名称" prop="shopName">
          <el-input v-model="form.shopName" placeholder="请输入店铺名称" maxlength="100" />
        </el-form-item>
        <el-form-item label="店铺 Logo" prop="logoUrl">
          <div class="logo-row">
            <el-input v-model="form.logoUrl" placeholder="请输入 Logo 图片地址" />
            <el-image v-if="form.logoUrl" :src="form.logoUrl" fit="cover" class="logo-preview" />
          </div>
        </el-form-item>
        <el-form-item label="联系人" prop="contactName">
          <el-input v-model="form.contactName" placeholder="请输入联系人" maxlength="50" />
        </el-form-item>
        <el-form-item label="联系邮箱" prop="contactEmail">
          <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" maxlength="100" />
        </el-form-item>
        <el-form-item label="国家/地区" prop="countryCode">
          <el-input v-model="form.countryCode" placeholder="请输入国家/地区代码，如 CN、US" maxlength="10" />
        </el-form-item>
        <el-form-item label="结算币种" prop="currencyCode">
          <el-select v-model="form.currencyCode" placeholder="请选择结算币种" style="width: 100%">
            <el-option v-for="item in currencyOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="平台佣金比例">
          <el-input :model-value="commissionRateText" disabled />
        </el-form-item>
        <el-form-item label="店铺状态">
          <el-input :model-value="statusText" disabled />
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup name="ShopManagement">
import { getShopProfile, updateShopProfile } from '@/api/commerce/shop/management'

const { proxy } = getCurrentInstance()
const { commerce_shop_status } = useDict('commerce_shop_status')

const loading = ref(false)
const saving = ref(false)
const form = ref({})

const currencyOptions = ['CNY', 'USD', 'EUR', 'GBP', 'JPY', 'HKD']

const rules = {
  shopName: [{ required: true, message: '店铺名称不能为空', trigger: 'blur' }],
  contactEmail: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }]
}

const commissionRateText = computed(() => {
  if (form.value.commissionRate === null || form.value.commissionRate === undefined || form.value.commissionRate === '') {
    return '-'
  }
  const rate = Number(form.value.commissionRate)
  return isNaN(rate) ? '-' : (rate * 100).toFixed(2) + '%'
})

const statusText = computed(() => {
  if (!form.value.status) {
    return '-'
  }
  return proxy.selectDictLabel(commerce_shop_status.value, form.value.status) || form.value.status
})

function getProfile() {
  loading.value = true
  getShopProfile().then(response => {
    const data = response.data || {}
    form.value = {
      shopId: data.shopId,
      shopName: data.shopName,
      logoUrl: data.logoUrl,
      contactName: data.contactName,
      contactEmail: data.contactEmail,
      countryCode: data.countryCode,
      currencyCode: data.currencyCode,
      commissionRate: data.commissionRate,
      status: data.status
    }
  }).finally(() => {
    loading.value = false
  })
}

function submitForm() {
  proxy.$refs['profileRef'].validate(valid => {
    if (!valid) {
      return
    }
    saving.value = true
    const data = {
      shopId: form.value.shopId,
      shopName: form.value.shopName,
      logoUrl: form.value.logoUrl,
      contactName: form.value.contactName,
      contactEmail: form.value.contactEmail,
      countryCode: form.value.countryCode,
      currencyCode: form.value.currencyCode
    }
    updateShopProfile(data).then(() => {
      proxy.$modal.msgSuccess('保存成功')
      getProfile()
    }).finally(() => {
      saving.value = false
    })
  })
}

getProfile()
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.profile-form {
  max-width: 720px;
}
.logo-row {
  display: flex;
  align-items: center;
  width: 100%;
}
.logo-row .el-input {
  flex: 1;
}
.logo-preview {
  width: 72px;
  height: 72px;
  margin-left: 12px;
  border-radius: 6px;
  border: 1px solid #ebeef5;
  flex-shrink: 0;
}
</style>
