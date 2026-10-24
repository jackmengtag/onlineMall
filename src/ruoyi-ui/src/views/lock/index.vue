<template>
  <div class="lock-container">
    <div class="lock-panel">
      <el-avatar :size="80" :src="userStore.avatar" />
      <div class="lock-name">{{ userStore.nickName || userStore.name }}</div>
      <div class="lock-tip">屏幕已锁定，请输入登录密码解锁</div>
      <el-input
        v-model="password"
        type="password"
        placeholder="请输入登录密码"
        show-password
        size="large"
        class="lock-input"
        @keyup.enter="handleUnlock"
      >
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" size="large" class="lock-button" :loading="loading" @click="handleUnlock">
        解锁
      </el-button>
      <el-button link class="lock-logout" @click="handleLogout">返回登录</el-button>
    </div>
  </div>
</template>

<script setup name="Lock">
import { ElMessage } from 'element-plus'
import { Lock } from '@element-plus/icons-vue'
import useUserStore from '@/store/modules/user'
import useLockStore from '@/store/modules/lock'
import { unlockScreen } from '@/api/login'

const router = useRouter()
const userStore = useUserStore()
const lockStore = useLockStore()

const password = ref('')
const loading = ref(false)

function handleUnlock() {
  if (!password.value) {
    ElMessage.warning('请输入登录密码')
    return
  }
  loading.value = true
  unlockScreen(password.value).then(() => {
    ElMessage.success('解锁成功')
    lockStore.unlockScreen()
    router.push(lockStore.lockPath || '/index')
  }).finally(() => {
    loading.value = false
  })
}

function handleLogout() {
  userStore.logOut().then(() => {
    lockStore.unlockScreen()
    router.push('/login')
  })
}
</script>

<style scoped lang="scss">
.lock-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: linear-gradient(135deg, #1f2d3d 0%, #304156 100%);
}

.lock-panel {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 360px;
  padding: 40px 36px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.08);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.lock-name {
  margin-top: 16px;
  font-size: 20px;
  font-weight: 600;
  color: #fff;
}

.lock-tip {
  margin-top: 10px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.65);
}

.lock-input {
  margin-top: 24px;
}

.lock-button {
  width: 100%;
  margin-top: 20px;
}

.lock-logout {
  margin-top: 12px;
  color: rgba(255, 255, 255, 0.65);
}
</style>
