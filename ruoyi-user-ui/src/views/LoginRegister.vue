<template>
  <div class="login-container">
    <!-- 粒子背景 -->
    <div class="particles-bg">
      <div class="particle" v-for="i in 20" :key="i" :style="getParticleStyle(i)"></div>
    </div>

    <!-- 浮动装饰元素 -->
    <div class="floating-elements">
      <div v-for="(icon, index) in gameIcons" :key="index"
           class="game-icon"
           :style="getIconStyle(index)">
        {{ icon }}
      </div>
    </div>

    <div class="main-card" :class="{ 'register-mode': isRegister }">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="logo-section">
          <div class="logo-icon">🎮</div>
          <h1 class="brand-title">GameHub</h1>
        </div>
        <div class="header-text">
          <h2 class="welcome-title">{{ headerData.title }}</h2>
          <p class="welcome-subtitle">{{ headerData.subtitle }}</p>
        </div>
      </div>

      <!-- 切换标签 -->
      <div class="tab-container">
        <div class="tab-slider" :class="{ 'slide-right': isRegister }"></div>
        <div v-for="(tab, index) in tabs" :key="index"
             class="tab"
             :class="{ active: tab.active }"
             @click="switchTab(tab.isRegister)">
          <span class="tab-icon">{{ tab.icon }}</span>
          <span>{{ tab.label }}</span>
        </div>
      </div>

      <!-- 表单区域 -->
      <div class="form-wrapper">
        <!-- 成功动画覆盖层 -->
        <div v-if="showSuccess" class="success-overlay">
          <div class="success-animation active">
            <div class="success-circle">
              <div class="checkmark" :class="{ show: showCheckmark }">✓</div>
            </div>
            <div class="success-text">登录成功！</div>
          </div>
        </div>

        <!-- 主表单 -->
        <form @submit.prevent="handleSubmit" class="main-form" :class="{ shrinking: showSuccess }">
          <div class="form-content" :class="{ 'register-form': isRegister }">
            <!-- 动态渲染表单字段 -->
            <div v-for="(row, rowIndex) in formFields" :key="rowIndex"
                 :class="row.length > 1 ? 'input-row' : ''">
              <div v-for="field in row" :key="field.key" class="input-group">
                <div class="input-icon">{{ field.icon }}</div>
                <input
                    v-model="form[field.key]"
                    :type="field.type"
                    :placeholder="field.placeholder"
                    :required="field.required"
                    class="form-input">
              </div>
            </div>
          </div>

          <!-- 提交按钮 -->
          <button type="submit" class="submit-button" :disabled="isLoading">
            <span v-if="isLoading" class="loading-spinner"></span>
            <span class="button-text">{{ buttonText }}</span>
          </button>

          <!-- 消息提示 -->
          <div v-if="errorMsg" class="message error-message">
            <span class="message-icon">⚠️</span>
            {{ errorMsg }}
          </div>
          <div v-if="successMsg && !showSuccess" class="message success-message">
            <span class="message-icon">✅</span>
            {{ successMsg }}
          </div>
        </form>
      </div>

      <!-- 底部信息 -->
      <div class="card-footer">
        <p class="footer-text">{{ footerText }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import API_URLS from '@/api/apiUrls.js'

const router = useRouter()
const baseURL = API_URLS.getBASEURL()

// 常量配置
const gameIcons = ['⚔️', '🛡️', '🎯', '⭐']
const particlePositions = [
  { top: '20%', left: '10%' }, { top: '60%', left: '20%' }, { top: '40%', left: '80%' },
  { top: '80%', left: '70%' }, { top: '10%', left: '90%' }, { top: '70%', left: '5%' },
  { top: '30%', left: '40%' }, { top: '90%', left: '30%' }, { top: '50%', left: '60%' },
  { top: '15%', left: '50%' }, { top: '75%', left: '85%' }, { top: '35%', left: '15%' },
  { top: '85%', left: '55%' }, { top: '25%', left: '75%' }, { top: '65%', left: '35%' },
  { top: '45%', left: '95%' }, { top: '55%', left: '25%' }, { top: '5%', left: '65%' },
  { top: '95%', left: '45%' }, { top: '75%', left: '15%' }
]

const iconPositions = [
  { top: '10%', left: '10%' }, { top: '20%', right: '15%' },
  { bottom: '25%', left: '20%' }, { bottom: '15%', right: '10%' }
]

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

// 计算属性
const headerData = computed(() => ({
  title: isRegister.value ? '创建账户' : '欢迎回来',
  subtitle: isRegister.value ? '加入我们的游戏社区' : '继续你的游戏之旅'
}))

const tabs = computed(() => [
  { label: '登录', icon: '👤', active: !isRegister.value, isRegister: false },
  { label: '注册', icon: '✨', active: isRegister.value, isRegister: true }
])

const formFields = computed(() => {
  const loginFields = [
    [{ key: 'userName', icon: '👤', type: 'text', placeholder: '输入用户名', required: true }],
    [{ key: 'password', icon: '🔐', type: 'password', placeholder: '输入密码', required: true }]
  ]

  const registerFields = [
    [
      { key: 'userName', icon: '👤', type: 'text', placeholder: '用户名', required: true },
      { key: 'nickName', icon: '🏷️', type: 'text', placeholder: '昵称', required: true }
    ],
    [
      { key: 'password', icon: '🔐', type: 'password', placeholder: '密码', required: true },
      { key: 'email', icon: '📧', type: 'email', placeholder: '邮箱 (可选)', required: false }
    ],
    [{ key: 'phonenumber', icon: '📱', type: 'tel', placeholder: '手机号 (可选)', required: false }]
  ]

  return isRegister.value ? registerFields : loginFields
})

const buttonText = computed(() => {
  if (isLoading.value) return isRegister.value ? '注册中...' : '登录中...'
  return isRegister.value ? '创建账户' : '立即登录'
})

const footerText = computed(() =>
    isRegister.value ? ' ' : '准备好开始你的冒险了吗？'
)

// 样式计算方法
const getParticleStyle = (index) => ({
  ...particlePositions[index - 1],
  animationDelay: `${-2 * index}s`
})

const getIconStyle = (index) => ({
  ...iconPositions[index],
  animationDelay: `${-2 * index}s`
})

// 监听器
watch(isRegister, () => {
  errorMsg.value = ''
  successMsg.value = ''
  showSuccess.value = false
  showCheckmark.value = false
})

// 工具函数
const saveTokens = (data) => {
  const tokenData = {
    accessToken: data.accessToken,
    refreshToken: data.refreshToken,
    tokenType: data.tokenType,
    expiresIn: data.expiresIn,
    userInfo: JSON.stringify(data.user),
    tokenExpiration: Date.now() + (data.expiresIn * 1000)
  }
  Object.entries(tokenData).forEach(([key, value]) => localStorage.setItem(key, value))
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
  const errorMap = {
    'uk_user_name': '用户名已存在',
    'email': '邮箱已存在',
    'phone': '手机号已存在'
  }

  const duplicateError = Object.entries(errorMap).find(([key]) =>
      msg.includes('Duplicate entry') && msg.includes(key)
  )

  return duplicateError ? duplicateError[1] : (isRegister.value ? '注册失败，请重试' : '登录失败，请重试')
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

      setTimeout(() => {
        showSuccess.value = true
        setTimeout(() => showCheckmark.value = true, 500)
      }, 300)

      setTimeout(() => router.push('/'), 2500)
    }
  } catch (error) {
    errorMsg.value = parseErrorMessage(error)
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
/* 基础样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
  overflow: hidden;
}

/* 容器 */
.login-container {
  min-height: 100vh;
  height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: fixed;
  top: 0;
  left: 0;
  overflow: hidden;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 背景效果 */
.particles-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  pointer-events: none;
  z-index: 1;
}

.particle {
  position: absolute;
  width: 4px;
  height: 4px;
  background: rgba(99, 102, 241, 0.3);
  border-radius: 50%;
  animation: float 15s infinite linear;
}

@keyframes float {
  0% { transform: translateY(0) rotate(0deg); opacity: 0; }
  10% { opacity: 1; }
  90% { opacity: 1; }
  100% { transform: translateY(-100vh) rotate(360deg); opacity: 0; }
}

.floating-elements {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  pointer-events: none;
  z-index: 2;
}

.game-icon {
  position: absolute;
  font-size: 24px;
  animation: floatIcon 8s ease-in-out infinite;
  opacity: 0.1;
}

@keyframes floatIcon {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(5deg); }
}

/* 主卡片 */
.main-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  width: 420px;
  height: 600px;
  max-width: 90vw;
  padding: 40px;
  position: relative;
  z-index: 10;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: width 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  display: flex;
  flex-direction: column;
}

.main-card.register-mode {
  width: 640px;
  max-width: 95vw;
}

/* 头部样式 */
.card-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 20px;
}

.logo-icon {
  font-size: 32px;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.brand-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0;
}

.welcome-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.welcome-subtitle {
  font-size: 14px;
  color: #666;
  opacity: 0.8;
}

/* 标签切换 */
.tab-container {
  position: relative;
  display: flex;
  background: #f8f9fa;
  border-radius: 12px;
  padding: 4px;
  margin-bottom: 30px;
}

.tab-slider {
  position: absolute;
  top: 4px;
  left: 4px;
  width: calc(50% - 4px);
  height: calc(100% - 8px);
  background: white;
  border-radius: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tab-slider.slide-right {
  left: calc(50% + 4px);
}

.tab {
  flex: 1;
  padding: 12px 16px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  color: #666;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  position: relative;
  z-index: 1;
}

.tab.active {
  color: #6366f1;
}

.tab-icon {
  font-size: 16px;
}

/* 表单样式 */
.form-wrapper {
  position: relative;
  min-height: 200px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.success-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
}

.success-animation {
  text-align: center;
  transform: scale(0);
  transition: all 0.5s ease;
}

.success-animation.active {
  transform: scale(1);
}

.success-circle {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #10b981, #059669);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  box-shadow: 0 10px 30px rgba(16, 185, 129, 0.3);
}

.checkmark {
  font-size: 32px;
  color: white;
  font-weight: bold;
  transform: scale(0);
  transition: all 0.3s ease;
}

.checkmark.show {
  transform: scale(1);
}

.success-text {
  font-size: 18px;
  font-weight: 600;
  color: #10b981;
}

.main-form {
  transition: all 0.5s ease;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.main-form.shrinking {
  transform: scale(0.8);
  opacity: 0;
}

.form-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  gap: 20px;
}

.form-content.register-form {
  gap: 16px;
}

.input-row {
  display: flex;
  gap: 16px;
}

.input-row .input-group {
  flex: 1;
  margin-bottom: 0;
}

.input-group {
  position: relative;
  margin-bottom: 0;
}

.input-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  color: #9ca3af;
  z-index: 1;
}

.form-input {
  width: 100%;
  padding: 16px 16px 16px 48px;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 16px;
  background: white;
  transition: all 0.3s ease;
  outline: none;
}

.form-input:focus {
  border-color: #6366f1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
}

.form-input::placeholder {
  color: #9ca3af;
}

/* 按钮样式 */
.submit-button {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
  margin-top: 20px;
}

.submit-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(99, 102, 241, 0.4);
}

.submit-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 消息提示 */
.message {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  margin-top: 16px;
  animation: slideIn 0.3s ease;
}

.error-message {
  background: #fef2f2;
  color: #dc2626;
  border: 1px solid #fecaca;
}

.success-message {
  background: #f0fdf4;
  color: #16a34a;
  border: 1px solid #bbf7d0;
}

@keyframes slideIn {
  from { transform: translateY(-10px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

/* 底部样式 */
.card-footer {
  margin-top: 20px;
  text-align: center;
  flex-shrink: 0;
}

.footer-text {
  font-size: 14px;
  color: #6b7280;
  opacity: 0.8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    padding: 15px;
  }

  .main-card {
    width: 95vw !important;
    height: 90vh;
    padding: 30px 20px;
  }

  .input-row {
    flex-direction: column;
    gap: 16px;
  }

  .input-row .input-group {
    margin-bottom: 0;
  }

  .welcome-title {
    font-size: 20px;
  }

  .brand-title {
    font-size: 24px;
  }
}
</style>