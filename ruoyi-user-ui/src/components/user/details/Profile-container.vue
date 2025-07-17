<template>
  <div class="profile-container">
    <div v-if="loading" class="loading">加载中...</div>

    <template v-else>
      <!-- 用户头像和基本信息 -->
      <div class="profile-header">
        <div class="avatar-section">
          <div class="avatar-wrapper" @click="openModal('avatar')">
            <img :src="getImageUrl(userInfo.avatar) || '/images/headPortrait/default.jpg'" alt="头像" class="avatar"/>
            <div class="avatar-overlay">
              <span>更换头像</span>
            </div>
          </div>
        </div>
        <div class="user-info">
          <h2>{{ userInfo.nickName }}</h2>
          <p>@{{ userInfo.userName }}</p>
          <div class="user-meta">
            <span>{{ getUserTypeText(userInfo.userType) }}</span>
            <span>注册于 {{ formatDate(userInfo.createTime) }}</span>
          </div>
        </div>
        <div class="header-actions">
          <button @click="logout" class="btn-logout">退出登录</button>
        </div>
      </div>

      <!-- 个人信息 -->
      <div class="info-section">
        <div class="section-header">
          <h3>个人信息</h3>
          <button @click="openModal('edit')" class="btn-primary">编辑</button>
        </div>
        <div class="info-grid">
          <div class="info-item">
            <label>昵称</label>
            <span>{{ userInfo.nickName }}</span>
          </div>
          <div class="info-item">
            <label>性别</label>
            <span>{{ getSexText(userInfo.sex) }}</span>
          </div>
          <div class="info-item">
            <label>邮箱</label>
            <span>{{ userInfo.email || '未填写' }}</span>
          </div>
          <div class="info-item">
            <label>手机号</label>
            <span>{{ userInfo.phonenumber || '未填写' }}</span>
          </div>
        </div>
      </div>

      <!-- 安全设置 -->
      <div class="info-section">
        <h3>安全设置</h3>
        <div class="security-actions">
          <button @click="openModal('password')" class="btn-secondary">修改密码</button>
          <button @click="openModal('deactivate')" class="btn-danger">注销账户</button>
        </div>
      </div>
    </template>

    <!-- 通用模态框 -->
    <div v-if="activeModal" class="modal" @click="closeModal">
      <div class="modal-content" :class="{ danger: activeModal === 'deactivate' }" @click.stop>
        <!-- 编辑信息 -->
        <template v-if="activeModal === 'edit'">
          <h3>编辑个人信息</h3>
          <form @submit.prevent="updateProfile">
            <input v-model="editForm.nickName" placeholder="昵称" required/>
            <select v-model="editForm.sex">
              <option value="0">男</option>
              <option value="1">女</option>
              <option value="2">未知</option>
            </select>
            <input v-model="editForm.email" type="email" placeholder="邮箱"/>
            <input v-model="editForm.phonenumber" placeholder="手机号"/>
            <textarea v-model="editForm.remark" placeholder="个人简介" rows="3"></textarea>
            <div class="modal-actions">
              <button type="button" @click="closeModal" class="btn-secondary">取消</button>
              <button type="submit" :disabled="loading" class="btn-primary">
                {{ loading ? '保存中...' : '保存' }}
              </button>
            </div>
          </form>
        </template>

        <!-- 修改密码 -->
        <template v-if="activeModal === 'password'">
          <h3>修改密码</h3>
          <form @submit.prevent="updatePassword">
            <input v-model="passwordForm.oldPassword" type="password" placeholder="当前密码" required/>
            <input v-model="passwordForm.newPassword" type="password" placeholder="新密码" required/>
            <input v-model="passwordForm.confirmPassword" type="password" placeholder="确认新密码" required/>
            <div class="modal-actions">
              <button type="button" @click="closeModal" class="btn-secondary">取消</button>
              <button type="submit" :disabled="loading" class="btn-primary">
                {{ loading ? '修改中...' : '确认修改' }}
              </button>
            </div>
          </form>
        </template>

        <!-- 注销账户 -->
        <template v-if="activeModal === 'deactivate'">
          <h3>⚠️ 注销账户</h3>
          <p>注销后您将无法登录此账户，此操作无法撤销。</p>
          <input v-model="deactivateConfirm" :placeholder="`请输入用户名 ${userInfo.userName} 确认`"/>
          <div class="modal-actions">
            <button @click="closeModal" class="btn-secondary">取消</button>
            <button
                @click="deactivateAccount"
                :disabled="deactivateConfirm !== userInfo.userName || loading"
                class="btn-danger"
            >
              {{ loading ? '注销中...' : '确认注销' }}
            </button>
          </div>
        </template>

        <!-- 更换头像 -->
        <template v-if="activeModal === 'avatar'">
          <h3>更换头像</h3>
          <div class="avatar-upload">
            <input ref="fileInput" type="file" @change="handleFileSelect" accept="image/*" style="display: none"/>
            <div class="upload-area" @click="$refs.fileInput.click()">
              <div v-if="previewUrl" class="preview">
                <img :src="previewUrl" alt="预览"/>
              </div>
              <div v-else class="upload-placeholder">
                <span>点击选择图片</span>
                <small>支持 JPG、PNG，小于 5MB</small>
              </div>
            </div>
          </div>
          <div class="modal-actions">
            <button @click="closeModal" class="btn-secondary">取消</button>
            <button @click="uploadAvatar" :disabled="!selectedFile || loading" class="btn-primary">
              {{ loading ? '上传中...' : '确认更换' }}
            </button>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { API_URLS } from '@/api/apiUrls.js'

const router = useRouter()

// 状态管理
const loading = ref(true)
const userInfo = ref({})
const activeModal = ref(null)

// 表单数据
const editForm = reactive({
  userId: null,
  nickName: '',
  sex: '',
  email: '',
  phonenumber: '',
  remark: ''
})

const passwordForm = reactive({
  userId: null,
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 头像上传
const selectedFile = ref(null)
const previewUrl = ref('')
const deactivateConfirm = ref('')

// 获取认证数据
const getAuthData = () => {
  const accessToken = localStorage.getItem('accessToken')
  const userInfoStr = localStorage.getItem('userInfo')

  if (!accessToken || !userInfoStr) {
    router.push('/login')
    return null
  }

  try {
    return {
      token: accessToken,
      userInfo: JSON.parse(userInfoStr)
    }
  } catch {
    router.push('/login')
    return null
  }
}

const getImageUrl = (path) => {
  if (!path) return ''
  const cleanPath = path.replace(/\\/g, '/')
  if (cleanPath.startsWith('http')) return cleanPath
  return API_URLS.getGamePhoto() + cleanPath
}

// 初始化用户信息
const initUserInfo = () => {
  const authData = getAuthData()
  if (!authData) return

  userInfo.value = authData.userInfo

  // 初始化表单
  Object.assign(editForm, {
    userId: authData.userInfo.userId,
    nickName: authData.userInfo.nickName,
    sex: authData.userInfo.sex,
    email: authData.userInfo.email,
    phonenumber: authData.userInfo.phonenumber,
    remark: authData.userInfo.remark
  })

  passwordForm.userId = authData.userInfo.userId
  loading.value = false
}

// API 请求封装
const apiRequest = async (url, options = {}) => {
  const authData = getAuthData()
  if (!authData) return null

  try {
    const response = await fetch(url, {
      ...options,
      headers: {
        'Authorization': `Bearer ${authData.token}`,
        'Content-Type': 'application/json',
        ...options.headers
      }
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const result = await response.json()
    if (result.code !== 200) {
      throw new Error(result.message || '请求失败')
    }
    return result
  } catch (error) {
    console.error('API request error:', error)
    throw error
  }
}

// 模态框管理
const openModal = (type) => {
  activeModal.value = type
}

const closeModal = () => {
  const currentModal = activeModal.value
  activeModal.value = null

  // 重置表单
  if (currentModal === 'password') {
    Object.assign(passwordForm, {
      userId: passwordForm.userId,
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
  } else if (currentModal === 'avatar') {
    selectedFile.value = null
    previewUrl.value = ''
  } else if (currentModal === 'deactivate') {
    deactivateConfirm.value = ''
  }
}

// 退出登录
const logout = () => {
  if (confirm('确定要退出登录吗？')) {
    localStorage.clear()
    router.push('/')
  }
}

// 更新个人信息
const updateProfile = async () => {
  if (loading.value) return

  loading.value = true
  try {
    await apiRequest(API_URLS.updateProfile(), {
      method: 'PUT',
      body: JSON.stringify(editForm)
    })

    Object.assign(userInfo.value, editForm)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))

    closeModal()
    alert('更新成功')
  } catch (error) {
    alert(error.message || '更新失败')
  } finally {
    loading.value = false
  }
}

// 修改密码
const updatePassword = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }

  if (loading.value) return

  loading.value = true
  try {
    await apiRequest(API_URLS.updatePassword(), {
      method: 'PUT',
      body: JSON.stringify({
        userId: passwordForm.userId,
        oldPassword: passwordForm.oldPassword,
        newPassword: passwordForm.newPassword
      })
    })

    closeModal()
    alert('密码修改成功')
  } catch (error) {
    alert(error.message || '密码修改失败')
  } finally {
    loading.value = false
  }
}

// 注销账户
const deactivateAccount = async () => {
  if (loading.value) return

  loading.value = true
  try {
    await apiRequest(API_URLS.deactivateAccount(userInfo.value.userId), {
      method: 'PUT'
    })

    localStorage.clear()
    router.push('/loginregister')
    alert('账户已注销')
  } catch (error) {
    alert(error.message || '注销失败')
  } finally {
    loading.value = false
  }
}

// 文件选择处理
const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (file.size > 5 * 1024 * 1024) {
    alert('图片大小不能超过 5MB')
    return
  }

  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }

  selectedFile.value = file

  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target.result
  }
  reader.readAsDataURL(file)
}

// 上传头像
const uploadAvatar = async () => {
  if (!selectedFile.value || loading.value) return

  loading.value = true
  try {
    const ext = selectedFile.value.name.split('.').pop()
    const fileName = `avatar_${Date.now()}_${Math.random().toString(36).substr(2, 9)}.${ext}`
    const avatarPath = `images/headPortrait/${fileName}`

    const reader = new FileReader()
    reader.onload = async (e) => {
      try {
        // 获取认证数据
        const authData = getAuthData()
        if (!authData) {
          alert('用户认证失败，请重新登录')
          return
        }

        // 上传头像文件
        const uploadResponse = await fetch(API_URLS.uploadAvatar(), {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authData.token}`
          },
          body: JSON.stringify({
            fileName,
            base64Data: e.target.result
          })
        })

        // 检查上传响应
        if (!uploadResponse.ok) {
          throw new Error('头像上传失败')
        }

        const uploadResult = await uploadResponse.json()
        if (uploadResult.code !== 200) {
          throw new Error(uploadResult.message || '头像上传失败')
        }

        // 更新用户头像路径
        await apiRequest(API_URLS.updateProfile(), {
          method: 'PUT',
          body: JSON.stringify({
            userId: userInfo.value.userId,
            avatar: avatarPath
          })
        })

        userInfo.value.avatar = avatarPath
        localStorage.setItem('userInfo', JSON.stringify(userInfo.value))

        closeModal()
        alert('头像更换成功')
      } catch (error) {
        console.error('头像上传错误:', error)
        alert(error.message || '头像上传失败')
      } finally {
        loading.value = false
      }
    }

    reader.readAsDataURL(selectedFile.value)
  } catch (error) {
    console.error('文件读取错误:', error)
    alert('文件读取失败')
    loading.value = false
  }
}

// 辅助函数
const getUserTypeText = (type) => ({'00': '系统用户', '10': '普通用户'}[type] || '未知')
const getSexText = (sex) => ({'0': '男', '1': '女', '2': '未知'}[sex] || '未知')
const formatDate = (dateStr) => dateStr ? new Date(dateStr).toLocaleDateString('zh-CN') : '暂无'

onMounted(initUserInfo)
</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

.loading {
  text-align: center;
  padding: 50px;
  color: #666;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 30px;
  background: white;
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.header-actions {
  margin-left: auto;
}

.btn-logout {
  padding: 8px 16px;
  background: #6c757d;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s;
}

.btn-logout:hover {
  background: #5a6268;
}

.avatar-wrapper {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s;
  font-size: 14px;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.user-info h2 {
  margin: 0 0 5px 0;
  font-size: 24px;
  color: #333;
}

.user-info p {
  margin: 0 0 10px 0;
  color: #666;
}

.user-meta {
  display: flex;
  gap: 15px;
  font-size: 14px;
  color: #999;
}

.info-section {
  background: white;
  border-radius: 12px;
  padding: 25px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3, .info-section h3 {
  margin: 0 0 20px 0;
  font-size: 18px;
  color: #333;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-item label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.info-item span {
  color: #333;
}

.security-actions {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

/* 按钮样式 */
.btn-primary, .btn-secondary, .btn-danger {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #0056b3;
}

.btn-secondary {
  background: #f8f9fa;
  color: #666;
  border: 1px solid #ddd;
}

.btn-secondary:hover {
  background: #e9ecef;
}

.btn-danger {
  background: #dc3545;
  color: white;
}

.btn-danger:hover:not(:disabled) {
  background: #c82333;
}

.btn-primary:disabled, .btn-danger:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* 模态框样式 */
.modal {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  padding: 30px;
  width: 90%;
  max-width: 400px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-content.danger {
  border-top: 4px solid #dc3545;
}

.modal-content h3 {
  margin: 0 0 20px 0;
  color: #333;
}

.modal-content form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.modal-content input,
.modal-content select,
.modal-content textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
}

.modal-content input:focus,
.modal-content select:focus,
.modal-content textarea:focus {
  outline: none;
  border-color: #007bff;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 头像上传 */
.avatar-upload {
  margin-bottom: 20px;
}

.upload-area {
  border: 2px dashed #ddd;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-area:hover {
  border-color: #007bff;
}

.preview img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  gap: 5px;
  color: #666;
}

.upload-placeholder small {
  color: #999;
}

/* 响应式 */
@media (max-width: 768px) {
  .profile-container {
    padding: 10px;
  }

  .profile-header {
    flex-direction: column;
    text-align: center;
  }

  .header-actions {
    margin-left: 0;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .security-actions {
    flex-direction: column;
  }

  .modal-content {
    width: 95%;
    padding: 20px;
  }

  .modal-actions {
    flex-direction: column;
  }
}
</style>