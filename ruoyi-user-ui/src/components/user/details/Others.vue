<template>
  <div class="user-profile">
    <div v-if="loading" class="loading">加载中...</div>

    <template v-else-if="userProfile">
      <!-- 用户信息卡片 -->
      <div class="profile-card">
        <div class="profile-header">
          <img
              :src="getImageUrl(userProfile.avatar) || '/images/headPortrait/default.jpg'"
              alt="头像"
              class="avatar"
          />
          <div class="profile-info">
            <h1 class="username">{{ userProfile.nickName }}</h1>
            <p class="user-handle">@{{ userProfile.userName }}</p>
            <div class="user-meta">
              <span class="meta-item">
                <i class="icon">👤</i>
                {{ getUserTypeText(userProfile.userType) }}
              </span>
              <span class="meta-item">
                <i class="icon">📅</i>
                注册于 {{ formatDate(userProfile.createTime) }}
              </span>
            </div>
          </div>
          <button
              v-if="!isCurrentUser"
              @click="handleFollowToggle"
              :class="['follow-btn', { 'following': isFollowing }]"
              :disabled="followLoading"
          >
            {{ followLoading ? '处理中...' : (isFollowing ? '已关注' : '关注') }}
          </button>
        </div>

        <!-- 统计信息 -->
        <div class="stats-bar">
          <div class="stat-item" @click="activeTab = 'posts'">
            <div class="stat-value">{{ userPosts.length }}</div>
            <div class="stat-label">帖子</div>
          </div>
          <div class="stat-item" @click="activeTab = 'following'">
            <div class="stat-value">{{ followingList.length }}</div>
            <div class="stat-label">关注</div>
          </div>
          <div class="stat-item" @click="activeTab = 'followers'">
            <div class="stat-value">{{ followersList.length }}</div>
            <div class="stat-label">粉丝</div>
          </div>
        </div>
      </div>

      <!-- 标签导航 -->
      <div class="tabs">
        <button
            :class="['tab-btn', { active: activeTab === 'posts' }]"
            @click="activeTab = 'posts'"
        >
          帖子
        </button>
        <button
            :class="['tab-btn', { active: activeTab === 'following' }]"
            @click="activeTab = 'following'"
        >
          关注
        </button>
        <button
            :class="['tab-btn', { active: activeTab === 'followers' }]"
            @click="activeTab = 'followers'"
        >
          粉丝
        </button>
      </div>

      <!-- 内容区域 -->
      <div class="content-area">
        <!-- 帖子列表 -->
        <div v-if="activeTab === 'posts'" class="posts-section">
          <div v-if="loadingPosts" class="loading-state">加载中...</div>
          <div v-else-if="userPosts.length === 0" class="empty-state">
            <div class="empty-icon">📝</div>
            <p>还没有发布任何帖子</p>
          </div>
          <div v-else class="posts-grid">
            <div
                v-for="post in userPosts"
                :key="post.postId"
                class="post-card"
                @click="goToPost(post.postId)"
            >
              <img
                  v-if="post.photo"
                  :src="getImageUrl(post.photo)"
                  alt=""
                  class="post-image"
              />
              <div class="post-content">
                <h3 class="post-title">{{ post.postTitle }}</h3>
                <div class="post-stats">
                  <span class="stat">👁️ {{ post.postViews || 0 }}</span>
                  <span class="stat">⭐ {{ post.postLikes || 0 }}</span>
                  <span class="stat">💬 {{ post.postComments || 0 }}</span>
                </div>
                <div class="post-date">{{ formatDate(post.createTime) }}</div>
              </div>
            </div>
          </div>
        </div>
        <!-- 关注列表 -->
        <div v-if="activeTab === 'following'" class="users-section">
          <div v-if="loadingFollowing" class="loading-state">加载中...</div>
          <div v-else-if="followingList.length === 0" class="empty-state">
            <div class="empty-icon">👥</div>
            <p>还没有关注任何用户</p>
          </div>
          <div v-else class="users-list">
            <div
                v-for="user in followingList"
                :key="user.followingId"
                class="user-card"
            >
              <img
                  :src="getImageUrl(user.followingAvatar) || '/images/headPortrait/default.jpg'"
                  alt=""
                  class="user-avatar"
              />
              <div class="user-info">
                <h4 class="user-name">{{ user.followingNickName }}</h4>
              </div>
            </div>
          </div>
        </div>

        <!-- 粉丝列表 -->
        <div v-if="activeTab === 'followers'" class="users-section">
          <div v-if="loadingFollowers" class="loading-state">加载中...</div>
          <div v-else-if="followersList.length === 0" class="empty-state">
            <div class="empty-icon">✨</div>
            <p>还没有粉丝</p>
          </div>
          <div v-else class="users-list">
            <div
                v-for="user in followersList"
                :key="user.userId"
                class="user-card"
            >
              <img
                  :src="getImageUrl(user.followerAvatar) || '/images/headPortrait/default.jpg'"
                  alt=""
                  class="user-avatar"
              />
              <div class="user-info">
                <h4 class="user-name">{{ user.followerNickName }}</h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>

    <div v-else class="error-state">
      <p>用户不存在</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { API_URLS } from '@/api/apiUrls.js'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const userProfile = ref(null)
const activeTab = ref('posts')
const isFollowing = ref(false)
const followLoading = ref(false)

const userPosts = ref([])
const followingList = ref([])
const followersList = ref([])

const loadingPosts = ref(false)
const loadingFollowing = ref(false)
const loadingFollowers = ref(false)

// 获取当前登录用户信息
const getCurrentUser = () => {
  const userInfoStr = localStorage.getItem('userInfo')
  if (!userInfoStr) return null
  try {
    return JSON.parse(userInfoStr)
  } catch {
    return null
  }
}

// 判断是否是当前用户自己的主页
const isCurrentUser = computed(() => {
  const currentUser = getCurrentUser()
  return currentUser && currentUser.userId === userProfile.value?.userId
})

// 获取认证数据
const getAuthData = () => {
  const accessToken = localStorage.getItem('accessToken')
  if (!accessToken) return null
  return { token: accessToken }
}

const getImageUrl = (path) => {
  if (!path) return ''
  const cleanPath = path.replace(/\\/g, '/')
  if (cleanPath.startsWith('http')) return cleanPath
  return API_URLS.getGamePhoto() + cleanPath
}

// API 请求封装
const apiRequest = async (url, options = {}) => {
  const authData = getAuthData()

  try {
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers
    }

    if (authData) {
      headers['Authorization'] = `Bearer ${authData.token}`
    }

    const response = await fetch(url, {
      ...options,
      headers
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

// 加载用户信息
const loadUserProfile = async (userId) => {
  try {
    const result = await apiRequest(API_URLS.getUserProfile(userId))
    userProfile.value = result.data
  } catch (error) {
    console.error('加载用户信息失败:', error)
    userProfile.value = null
  }
}

// 检查关注状态
const checkFollowStatus = async (userId) => {
  if (!getAuthData()) return

  try {
    const result = await apiRequest(API_URLS.checkFollowStatus(userId))
    isFollowing.value = result.data || false
  } catch (error) {
    console.error('检查关注状态失败:', error)
  }
}

// 加载用户帖子
const loadUserPosts = async (userId) => {
  loadingPosts.value = true
  try {
    const result = await apiRequest(API_URLS.getPostsByUser(userId))
    userPosts.value = result.data || []
  } catch (error) {
    console.error('加载帖子失败:', error)
    userPosts.value = []
  } finally {
    loadingPosts.value = false
  }
}

// 加载关注列表
const loadFollowing = async (userId) => {
  loadingFollowing.value = true
  try {
    // 判断是查看自己还是查看别人
    const currentUser = getCurrentUser()
    let result

    if (currentUser && currentUser.userId === userId) {
      // 查看自己的主页:使用 /following/my
      result = await apiRequest(API_URLS.getMyFollowing())
    } else {
      // 查看别人的主页:使用 /following/{userId}
      result = await apiRequest(API_URLS.getUserFollowing(userId))
    }

    followingList.value = result.data || []
  } catch (error) {
    console.error('加载关注列表失败:', error)
    followingList.value = []
  } finally {
    loadingFollowing.value = false
  }
}

// 加载粉丝列表
const loadFollowers = async (userId) => {
  loadingFollowers.value = true
  try {
    // 判断是查看自己还是查看别人
    const currentUser = getCurrentUser()
    let result

    if (currentUser && currentUser.userId === userId) {
      // 查看自己的主页:使用 /follower/my
      result = await apiRequest(API_URLS.getMyFollowers())
    } else {
      // 查看别人的主页:使用 /follower/{userId}
      result = await apiRequest(API_URLS.getUserFollowers(userId))
    }

    followersList.value = result.data || []
  } catch (error) {
    console.error('加载粉丝列表失败:', error)
    followersList.value = []
  } finally {
    loadingFollowers.value = false
  }
}

// 处理关注/取消关注
const handleFollowToggle = async () => {
  if (!getAuthData()) {
    router.push('/loginregister')
    return
  }

  followLoading.value = true
  try {
    if (isFollowing.value) {
      await apiRequest(API_URLS.unfollowUser(userProfile.value.userId), { method: 'DELETE' })
      isFollowing.value = false
      alert('已取消关注')
    } else {
      await apiRequest(API_URLS.followUser(userProfile.value.userId), { method: 'POST' })
      isFollowing.value = true
      alert('关注成功')
    }
    await loadFollowers(userProfile.value.userId)
  } catch (error) {
    alert(error.message || '操作失败')
  } finally {
    followLoading.value = false
  }
}

// 跳转到帖子详情
const goToPost = (postId) => {
  router.push(`/postDetail/${postId}`)
}

// 辅助函数
const getUserTypeText = (type) => {
  const types = { '00': '系统用户', '10': '普通用户' }
  return types[type] || '未知'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '暂无'
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(async () => {
  // 更健壮地获取用户ID
  let userId = null

  // 优先从路径参数获取
  if (route.params.userId) {
    userId = Number(route.params.userId)
  }
  // 其次从查询参数获取
  else if (route.query.userId) {
    userId = Number(route.query.userId)
  }
  // 如果都没有，尝试从当前路径解析
  else {
    const pathSegments = route.path.split('/')
    const lastSegment = pathSegments[pathSegments.length - 1]
    const potentialId = Number(lastSegment)
    if (!isNaN(potentialId) && potentialId > 0) {
      userId = potentialId
    }
  }

  console.log('获取到的用户ID:', userId) // 调试用

  if (!userId || isNaN(userId)) {
    alert('用户ID不存在或格式错误')
    router.push('/')
    return
  }

  loading.value = true

  await loadUserProfile(userId)

  if (userProfile.value) {
    await Promise.all([
      checkFollowStatus(userId),
      loadUserPosts(userId),
      loadFollowing(userId),
      loadFollowers(userId)
    ])
  }

  loading.value = false
})
</script>

<style scoped>
.user-profile {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.loading, .loading-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
  font-size: 15px;
}

.error-state {
  text-align: center;
  padding: 100px 20px;
  color: #999;
}

/* 用户信息卡片 */
.profile-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  margin-bottom: 20px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 32px;
  background: white;
  border-bottom: 1px solid #e8e8e8;
  position: relative;
}

.avatar {
  width: 96px;
  height: 96px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #f0f0f0;
}

.profile-info {
  flex: 1;
}

.username {
  margin: 0 0 6px 0;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
}

.user-handle {
  margin: 0 0 12px 0;
  color: #666;
  font-size: 15px;
}

.user-meta {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #666;
}

.icon {
  font-size: 15px;
}

.follow-btn {
  padding: 10px 28px;
  border: 1px solid #e0e0e0;
  background: white;
  color: #333;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
}

.follow-btn:hover:not(:disabled) {
  background: #fafafa;
  border-color: #333;
}

.follow-btn.following {
  background: #333;
  color: white;
  border-color: #333;
}

.follow-btn.following:hover:not(:disabled) {
  background: #555;
}

.follow-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 统计栏 */
.stats-bar {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
}

.stat-item {
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: background 0.2s;
}

.stat-item:not(:last-child) {
  border-right: 1px solid #f0f0f0;
}

.stat-item:hover {
  background: #fafafa;
}

.stat-value {
  font-size: 22px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #999;
}

/* 标签导航 */
.tabs {
  display: flex;
  gap: 0;
  margin-bottom: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.tab-btn {
  flex: 1;
  padding: 14px 20px;
  border: none;
  background: white;
  color: #666;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border-bottom: 2px solid transparent;
}

.tab-btn:hover {
  color: #333;
  background: #fafafa;
}

.tab-btn.active {
  color: #1a1a1a;
  font-weight: 600;
  border-bottom-color: #333;
  background: white;
}

/* 内容区域 */
.content-area {
  min-height: 400px;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 80px 20px;
  background: white;
  border-radius: 12px;
}

.empty-icon {
  font-size: 56px;
  margin-bottom: 16px;
  opacity: 0.3;
}

.empty-state p {
  color: #999;
  font-size: 15px;
}

/* 帖子网格 */
.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.post-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.2s;
}

.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.post-image {
  width: 100%;
  height: 180px;
  object-fit: cover;
  background: #f5f5f5;
}

.post-content {
  padding: 16px;
}

.post-title {
  margin: 0 0 12px 0;
  font-size: 15px;
  font-weight: 500;
  color: #1a1a1a;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
}

.post-stats {
  display: flex;
  gap: 14px;
  margin-bottom: 10px;
}

.stat {
  font-size: 13px;
  color: #999;
}

.post-date {
  font-size: 12px;
  color: #bbb;
}

/* 用户列表 */
.users-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
}

.user-card {
  background: white;
  border-radius: 12px;
  padding: 24px 16px;
  text-align: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: all 0.2s;
  cursor: pointer;
}

.user-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.user-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 12px;
  border: 2px solid #f0f0f0;
}

.user-info {
  text-align: center;
}

.user-name {
  margin: 0;
  font-size: 15px;
  font-weight: 500;
  color: #1a1a1a;
}

/* 响应式 */
@media (max-width: 768px) {
  .user-profile {
    padding: 16px;
  }

  .profile-header {
    flex-direction: column;
    text-align: center;
    padding: 24px 20px;
  }

  .profile-info {
    width: 100%;
  }

  .user-meta {
    justify-content: center;
  }

  .follow-btn {
    width: 100%;
  }

  .username {
    font-size: 22px;
  }

  .tabs {
    border-radius: 8px;
  }

  .tab-btn {
    font-size: 13px;
    padding: 12px 16px;
  }

  .posts-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .users-list {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 12px;
  }
}
</style>