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

      <!-- 统计信息卡片 -->
      <div class="stats-section">
        <div class="stat-card" @click="activeTab = 'posts'">
          <div class="stat-number">{{ myPosts.length }}</div>
          <div class="stat-label">帖子</div>
        </div>
        <div class="stat-card" @click="activeTab = 'following'">
          <div class="stat-number">{{ followingList.length }}</div>
          <div class="stat-label">关注</div>
        </div>
        <div class="stat-card" @click="activeTab = 'followers'">
          <div class="stat-number">{{ followersList.length }}</div>
          <div class="stat-label">粉丝</div>
        </div>
        <div class="stat-card" @click="activeTab = 'favorites'">
          <div class="stat-number">{{ favoritesList.length }}</div>
          <div class="stat-label">收藏</div>
        </div>
      </div>

      <!-- 标签页导航 -->
      <div class="tabs-container">
        <div class="tabs">
          <button
              :class="['tab', { active: activeTab === 'info' }]"
              @click="activeTab = 'info'">
            个人信息
          </button>
          <button
              :class="['tab', { active: activeTab === 'posts' }]"
              @click="activeTab = 'posts'">
            我的帖子
          </button>
          <button
              :class="['tab', { active: activeTab === 'favorites' }]"
              @click="activeTab = 'favorites'">
            我的收藏
          </button>
          <button
              :class="['tab', { active: activeTab === 'following' }]"
              @click="activeTab = 'following'">
            关注列表
          </button>
          <button
              :class="['tab', { active: activeTab === 'followers' }]"
              @click="activeTab = 'followers'">
            粉丝列表
          </button>
          <button
              :class="['tab', { active: activeTab === 'security' }]"
              @click="activeTab = 'security'">
            安全设置
          </button>
        </div>
      </div>

      <!-- 标签页内容 -->
      <div class="tab-content">
        <!-- 个人信息 -->
        <div v-if="activeTab === 'info'" class="info-section">
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

        <!-- 我的帖子 -->
        <div v-if="activeTab === 'posts'" class="list-section">
          <div v-if="loadingPosts" class="loading-small">加载中...</div>
          <div v-else-if="myPosts.length === 0" class="empty-state">
            <p>还没有发布任何帖子</p>
          </div>
          <div v-else class="post-list">
            <div v-for="post in myPosts" :key="post.postId" class="post-item" @click="goToPost(post.postId)">
              <img v-if="post.photo" :src="getImageUrl(post.photo)" alt="" class="post-thumbnail"/>
              <div class="post-info">
                <h4>{{ post.postTitle }}</h4>
                <div class="post-meta">
                  <span>{{ formatDate(post.createTime) }}</span>
                  <span>浏览 {{ post.postViews || 0 }}</span>
                  <span>点赞 {{ post.postLikes || 0 }}</span>
                  <span>评论 {{ post.postComments || 0 }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 我的收藏 -->
        <div v-if="activeTab === 'favorites'" class="list-section">
          <div v-if="loadingFavorites" class="loading-small">加载中...</div>
          <div v-else-if="favoritesList.length === 0" class="empty-state">
            <p>还没有收藏任何帖子</p>
          </div>
          <div v-else class="post-list">
            <div v-for="favorite in favoritesList" :key="favorite.postId" class="post-item" @click="goToPost(favorite.postId)">
              <img v-if="favorite.photo" :src="getImageUrl(favorite.photo)" alt="" class="post-thumbnail"/>
              <div class="post-info">
                <h4>{{ favorite.postTitle }}</h4>
                <div class="post-meta">
                  <span>作者: {{ favorite.nickName }}</span>
                  <span>收藏于 {{ formatDate(favorite.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 关注列表 -->
        <div v-if="activeTab === 'following'" class="list-section">
          <div v-if="loadingFollowing" class="loading-small">加载中...</div>
          <div v-else-if="followingList.length === 0" class="empty-state">
            <p>还没有关注任何用户</p>
          </div>
          <div v-else class="user-list">
            <div v-for="user in followingList" :key="user.userId" class="user-item">
              <img :src="getImageUrl(user.followingAvatar) || '/images/headPortrait/default.jpg'" alt="" class="user-avatar"/>
              <div class="user-info-item">
                <h4>{{ user.followingNickName }}</h4>
<!--                <p>@{{ user.followingNickName }}</p>-->
              </div>
              <button @click="handleUnfollow(user.followingId)" class="btn-secondary">取消关注</button>
            </div>
          </div>
        </div>

        <!-- 粉丝列表 -->
        <div v-if="activeTab === 'followers'" class="list-section">
          <div v-if="loadingFollowers" class="loading-small">加载中...</div>
          <div v-else-if="followersList.length === 0" class="empty-state">
            <p>还没有粉丝</p>
          </div>
          <div v-else class="user-list">
            <div v-for="user in followersList" :key="user.userId" class="user-item">
              <img :src="getImageUrl(user.followerAvatar) || '/images/headPortrait/default.jpg'" alt="" class="user-avatar"/>
              <div class="user-info-item">
                <h4>{{ user.followerNickName }}</h4>
<!--                <p>@{{ user.followerNickName }}</p>-->
              </div>
<!--              <button
                  @click="handleFollowToggle(user.userId)"
                  :class="['btn-primary', { 'btn-secondary': isFollowing(user.userId) }]">
                {{ isFollowing(user.userId) ? '已关注' : '关注' }}
              </button>-->
            </div>
          </div>
        </div>

        <!-- 安全设置 -->
        <div v-if="activeTab === 'security'" class="info-section">
          <h3>安全设置</h3>
          <div class="security-actions">
            <button @click="openModal('password')" class="btn-secondary">修改密码</button>
            <button @click="openModal('deactivate')" class="btn-danger">注销账户</button>
          </div>
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
          <p>注销后您将无法登录此账户,此操作无法撤销。</p>
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
                <small>支持 JPG、PNG,小于 5MB</small>
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
const activeTab = ref('info')

// 列表数据
const myPosts = ref([])
const favoritesList = ref([])
const followingList = ref([])
const followersList = ref([])

// 加载状态
const loadingPosts = ref(false)
const loadingFavorites = ref(false)
const loadingFollowing = ref(false)
const loadingFollowers = ref(false)

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
const initUserInfo = async () => {
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

  // 加载所有列表数据
  await Promise.all([
    loadMyPosts(),
    loadFavorites(),
    loadFollowing(),
    loadFollowers()
  ])
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

// 加载我的帖子
const loadMyPosts = async () => {
  loadingPosts.value = true
  try {
    const result = await apiRequest(API_URLS.getMyPosts())
    myPosts.value = result.data || []
  } catch (error) {
    console.error('加载帖子失败:', error)
    myPosts.value = []
  } finally {
    loadingPosts.value = false
  }
}

// 加载收藏列表
const loadFavorites = async () => {
  loadingFavorites.value = true
  try {
    const result = await apiRequest(API_URLS.getMyFavorites())
    favoritesList.value = result.data || []
  } catch (error) {
    console.error('加载收藏失败:', error)
    favoritesList.value = []
  } finally {
    loadingFavorites.value = false
  }
}

// 加载关注列表
const loadFollowing = async () => {
  loadingFollowing.value = true
  try {
    const result = await apiRequest(API_URLS.getMyFollowing())
    // 后端返回的数据已经包含了用户信息
    // 数据格式: { userId, nickName, userName, avatar, ... }
    followingList.value = result.data || []
    console.log('关注列表数据:', followingList.value)
  } catch (error) {
    console.error('加载关注列表失败:', error)
    followingList.value = []
  } finally {
    loadingFollowing.value = false
  }
}

// 加载粉丝列表
const loadFollowers = async () => {
  loadingFollowers.value = true
  try {
    const result = await apiRequest(API_URLS.getMyFollowers())
    // 后端返回的数据已经包含了用户信息
    // 数据格式: { userId, nickName, userName, avatar, ... }
    followersList.value = result.data || []
    console.log('粉丝列表数据:', followersList.value)
  } catch (error) {
    console.error('加载粉丝列表失败:', error)
    followersList.value = []
  } finally {
    loadingFollowers.value = false
  }
}

// 检查是否已关注
const isFollowing = (userId) => {
  return followingList.value.some(user => user.userId === userId)
}

// 处理关注/取消关注
const handleFollowToggle = async (userId) => {
  try {
    if (isFollowing(userId)) {
      await apiRequest(API_URLS.unfollowUser(userId), { method: 'DELETE' })
      alert('取消关注成功')
    } else {
      await apiRequest(API_URLS.followUser(userId), { method: 'POST' })
      alert('关注成功')
    }
    await loadFollowing()
  } catch (error) {
    alert(error.message || '操作失败')
  }
}

// 取消关注
const handleUnfollow = async (userId) => {
  if (!confirm('确定要取消关注吗?')) return

  try {
    await apiRequest(API_URLS.unfollowUser(userId), { method: 'DELETE' })
    alert('取消关注成功')
    await loadFollowing()
  } catch (error) {
    alert(error.message || '取消关注失败')
  }
}

// 跳转到帖子详情
const goToPost = (postId) => {
  router.push(`/postDetail/${postId}`)
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
  if (confirm('确定要退出登录吗?')) {
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
        const authData = getAuthData()
        if (!authData) {
          alert('用户认证失败,请重新登录')
          return
        }

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

        if (!uploadResponse.ok) {
          throw new Error('头像上传失败')
        }

        const uploadResult = await uploadResponse.json()
        if (uploadResult.code !== 200) {
          throw new Error(uploadResult.message || '头像上传失败')
        }

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
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

.loading, .loading-small {
  text-align: center;
  padding: 50px;
  color: #666;
}

.loading-small {
  padding: 20px;
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

/* 统计信息卡片 */
.stats-section {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #007bff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 标签页 */
.tabs-container {
  background: white;
  border-radius: 12px;
  padding: 15px 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tabs {
  display: flex;
  gap: 10px;
  overflow-x: auto;
}

.tab {
  padding: 10px 20px;
  background: transparent;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  transition: all 0.3s;
  white-space: nowrap;
}

.tab:hover {
  background: #f8f9fa;
}

.tab.active {
  background: #007bff;
  color: white;
}

/* 标签页内容 */
.tab-content {
  min-height: 400px;
}

.info-section {
  background: white;
  border-radius: 12px;
  padding: 25px;
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

/* 列表区域 */
.list-section {
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-state p {
  font-size: 16px;
}

/* 帖子列表 */
.post-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.post-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.post-item:hover {
  background: #f8f9fa;
  border-color: #007bff;
}

.post-thumbnail {
  width: 120px;
  height: 80px;
  border-radius: 6px;
  object-fit: cover;
  flex-shrink: 0;
}

.post-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 8px;
}

.post-info h4 {
  margin: 0;
  font-size: 16px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.post-meta {
  display: flex;
  gap: 15px;
  font-size: 13px;
  color: #999;
}

/* 用户列表 */
.user-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  transition: all 0.3s;
}

.user-item:hover {
  background: #f8f9fa;
  border-color: #007bff;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.user-info-item {
  flex: 1;
}

.user-info-item h4 {
  margin: 0 0 5px 0;
  font-size: 16px;
  color: #333;
}

.user-info-item p {
  margin: 0;
  font-size: 14px;
  color: #999;
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

  .stats-section {
    grid-template-columns: repeat(2, 1fr);
  }

  .tabs {
    gap: 5px;
  }

  .tab {
    padding: 8px 15px;
    font-size: 13px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .security-actions {
    flex-direction: column;
  }

  .post-item {
    flex-direction: column;
  }

  .post-thumbnail {
    width: 100%;
    height: 150px;
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