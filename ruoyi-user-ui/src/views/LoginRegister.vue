<template>
  <div class="login-container">
    <div class="background">
      <div class="decoration" v-for="i in 4" :key="i" :class="`decoration-${i}`"></div>
    </div>

    <div class="container" :class="{ loading: isLoading, 'register-mode': isRegister }">
      <div class="character-icon">🎮</div>

      <div class="header">
        <h1 class="title">{{ isRegister ? '加入我们' : '欢迎回来' }}</h1>
        <p class="subtitle">{{ isRegister ? '开始全新的游戏体验' : '继续你的冒险之旅' }}</p>
      </div>

      <div class="tabs">
        <div class="tab" :class="{ active: !isRegister }" @click="switchTab(false)">登录</div>
        <div class="tab" :class="{ active: isRegister }" @click="switchTab(true)">注册</div>
      </div>

      <div class="form-container">
        <!-- 成功动画 -->
        <div v-if="showSuccess" class="success-overlay">
          <div class="success-circle" :class="{ active: showSuccess }">
            <div class="checkmark" :class="{ show: showCheckmark }">✓</div>
          </div>
        </div>

        <!-- 表单 -->
        <form @submit.prevent="handleSubmit" class="form" :class="{ shrinking: showSuccess }">
          <!-- 登录表单 -->
          <template v-if="!isRegister">
            <div class="form-group">
              <input v-model="form.userName" type="text" placeholder="用户名" required class="form-input">
            </div>
            <div class="form-group">
              <input v-model="form.password" type="password" placeholder="密码" required class="form-input">
            </div>
          </template>

          <!-- 注册表单 -->
          <template v-else>
            <div class="form-row">
              <div class="form-group">
                <input v-model="form.userName" type="text" placeholder="用户名" required class="form-input">
              </div>
              <div class="form-group">
                <input v-model="form.nickName" type="text" placeholder="昵称" required class="form-input">
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <input v-model="form.password" type="password" placeholder="密码" required class="form-input">
              </div>
              <div class="form-group">
                <input v-model="form.email" type="email" placeholder="邮箱 (可选)" class="form-input">
              </div>
            </div>
            <div class="form-group">
              <input v-model="form.phonenumber" type="tel" placeholder="手机号 (可选)" class="form-input">
            </div>
          </template>

          <button type="submit" class="submit-btn" :disabled="isLoading">
            {{ isLoading ? (isRegister ? '注册中...' : '登录中...') : (isRegister ? '创建角色' : '开始游戏') }}
          </button>

          <!-- 错误信息 -->
          <div v-if="errorMsg" class="error-message">{{ errorMsg }}</div>
          <div v-if="successMsg && !showSuccess" class="success-message">{{ successMsg }}</div>
        </form>
      </div>

      <div class="form-footer">
        {{ isRegister ? '让我们一起创造属于你的传奇！' : '准备好开始新的冒险了吗？' }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const baseURL = 'http://localhost:8080'

// 状态管理
const isRegister = ref(false)
const isLoading = ref(false)
const showSuccess = ref(false)
const showCheckmark = ref(false)
const errorMsg = ref('')
const successMsg = ref('')

// 表单数据
const form = reactive({
  userName: '',
  password: '',
  nickName: '',
  email: '',
  phonenumber: ''
})

// 监听切换重置消息
watch(isRegister, () => {
  errorMsg.value = ''
  successMsg.value = ''
  showSuccess.value = false
  showCheckmark.value = false
})

// 工具函数
const saveTokens = (data) => {
  const items = {
    accessToken: data.accessToken,
    refreshToken: data.refreshToken,
    tokenType: data.tokenType,
    expiresIn: data.expiresIn,
    userInfo: JSON.stringify(data.user),
    tokenExpiration: Date.now() + (data.expiresIn * 1000)
  }
  Object.entries(items).forEach(([key, value]) => localStorage.setItem(key, value))
}

const apiCall = async (url, data) => {
  const response = await fetch(baseURL + url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data)
  })

  if (!response.ok) throw new Error(`HTTP ${response.status}`)

  const result = await response.json()
  if (result.code === 200) return result
  throw result
}

const resetForm = () => {
  Object.keys(form).forEach(key => form[key] = '')
}

const parseErrorMessage = (error) => {
  const msg = error.msg || error.message || '操作失败'

  // 解析数据库约束错误
  if (msg.includes('Duplicate entry') && msg.includes('uk_user_name')) {
    return '用户名已存在'
  }
  if (msg.includes('Duplicate entry') && msg.includes('email')) {
    return '邮箱已存在'
  }
  if (msg.includes('Duplicate entry') && msg.includes('phone')) {
    return '手机号已存在'
  }

  return isRegister.value ? '注册失败，请重试' : '登录失败，请重试'
}

// 主要方法
const switchTab = (register) => {
  isRegister.value = register
  resetForm()
}

const handleSubmit = async () => {
  errorMsg.value = ''
  successMsg.value = ''
  isLoading.value = true

  try {
    if (isRegister.value) {
      await apiCall('/user/profile/register', form)
      successMsg.value = '注册成功！欢迎加入我们的游戏世界！'
      setTimeout(() => {
        resetForm()
        switchTab(false)
      }, 2000)
    } else {
      const response = await apiCall('/user/profile/login', form)
      saveTokens(response.data)

      //successMsg.value = `登录成功！欢迎回来，${response.data.user.nickName || response.data.user.userName}`

      // 登录成功动画
      setTimeout(() => {
        showSuccess.value = true
        setTimeout(() => showCheckmark.value = true, 800)
      }, 500)

      setTimeout(() => router.push('/'), 3000)
    }
  } catch (error) {
    errorMsg.value = parseErrorMessage(error)
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.login-container {
  font-family: 'Microsoft YaHei', sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 25%, #f093fb 50%, #f5576c 75%, #4facfe 100%);
  background-size: 400% 400%;
  animation: gradientShift 15s infinite;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  inset: 0;
  overflow: hidden;
}

@keyframes gradientShift {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

.background {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.decoration {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  animation: float 20s infinite linear;
}

.decoration-1 { width: 200px; height: 200px; top: 10%; left: 10%; }
.decoration-2 { width: 150px; height: 150px; top: 60%; right: 10%; animation-delay: -5s; }
.decoration-3 { width: 100px; height: 100px; top: 30%; right: 30%; animation-delay: -10s; }
.decoration-4 { width: 80px; height: 80px; bottom: 20%; left: 20%; animation-delay: -15s; }

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(0deg); opacity: 0.3; }
  50% { transform: translateY(-100px) rotate(180deg); opacity: 0.6; }
}

.container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(30px);
  border-radius: 25px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
  width: 450px;
  max-width: 90vw;
  padding: 50px;
  position: relative;
  transition: all 0.5s ease;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.container.register-mode {
  width: 750px;
  max-width: 95vw;
}

.container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, #ff6b6b, #4ecdc4, #45b7d1);
  background-size: 300% 100%;
  animation: rainbow 4s linear infinite;
}

@keyframes rainbow {
  0% { background-position: 0% 50%; }
  100% { background-position: 300% 50%; }
}

.character-icon {
  position: absolute;
  top: -35px;
  right: -25px;
  width: 90px;
  height: 90px;
  background: linear-gradient(135deg, #ff6b6b, #4ecdc4);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 35px;
  animation: floatIcon 4s ease-in-out infinite;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
}

@keyframes floatIcon {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-15px); }
}

.header {
  text-align: center;
  margin-bottom: 35px;
}

.title {
  font-size: 32px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea, #764ba2);
  background-clip: text;
  -webkit-background-clip: text;
  color: transparent;
  margin-bottom: 12px;
}

.subtitle {
  color: #666;
  font-size: 16px;
  opacity: 0.8;
}

.tabs {
  display: flex;
  margin-bottom: 35px;
  background: rgba(102, 126, 234, 0.08);
  border-radius: 30px;
  padding: 6px;
}

.tab {
  flex: 1;
  padding: 14px;
  text-align: center;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.4s ease;
  font-weight: 600;
  color: #666;
}

.tab.active {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
  transform: translateY(-3px);
}

.form-container {
  position: relative;
}

.form {
  transition: all 1s ease;
}

.form.shrinking {
  transform: scale(0);
  opacity: 0;
  border-radius: 50%;
}

.success-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 100;
}

.success-circle {
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, #4ecdc4, #44a08d);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: scale(0);
  transition: all 0.8s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  box-shadow: 0 20px 40px rgba(78, 205, 196, 0.4);
}

.success-circle.active {
  transform: scale(1);
}

.checkmark {
  font-size: 40px;
  color: white;
  font-weight: bold;
  opacity: 0;
  transform: scale(0);
  transition: all 0.6s ease;
}

.checkmark.show {
  opacity: 1;
  transform: scale(1);
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 25px;
}

.form-row .form-group {
  flex: 1;
  margin-bottom: 0;
}

.form-group {
  margin-bottom: 25px;
}

.form-input {
  width: 100%;
  padding: 18px 25px;
  border: 2px solid rgba(102, 126, 234, 0.15);
  border-radius: 30px;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.9);
  transition: all 0.4s ease;
  outline: none;
}

.form-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 30px rgba(102, 126, 234, 0.25);
  transform: translateY(-8px);
  background: white;
  z-index: 10;
  position: relative;
}

.submit-btn {
  width: 100%;
  padding: 18px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 18px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.4s ease;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-4px);
  box-shadow: 0 15px 35px rgba(102, 126, 234, 0.4);
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.error-message {
  color: #ff6b6b;
  font-size: 14px;
  margin-top: 15px;
  text-align: center;
  animation: shake 0.6s ease;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-8px); }
  75% { transform: translateX(8px); }
}

.success-message {
  color: #4ecdc4;
  font-size: 14px;
  margin-top: 15px;
  text-align: center;
  animation: bounce 0.6s ease;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.form-footer {
  text-align: center;
  margin-top: 25px;
  color: #666;
  font-size: 15px;
  opacity: 0.8;
}

.loading {
  opacity: 0.7;
  pointer-events: none;
}

/* 响应式 */
@media (max-width: 768px) {
  .container {
    width: 95vw !important;
    padding: 40px 30px;
  }

  .form-row {
    flex-direction: column;
    gap: 0;
  }

  .form-row .form-group {
    margin-bottom: 25px;
  }
}
</style>