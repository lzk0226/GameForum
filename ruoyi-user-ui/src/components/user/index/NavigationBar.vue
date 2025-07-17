<template>
  <nav class="navigation-bar">
    <div class="nav-content">
      <!-- 左侧：论坛/商城切换 -->
      <div class="nav-tabs">
        <button class="nav-tab active" @click="switchToForum">论坛</button>
        <!--        <button class="nav-tab" @click="switchToShopping">商城</button>-->
      </div>

      <!-- 中间：搜索框 -->
      <div class="search-container">
        <div class="search-box">
          <input
              type="text"
              placeholder="搜索帖子、游戏..."
              v-model="searchQuery"
              @keyup.enter="handleSearch"
              class="search-input"
          >
          <button class="search-btn" @click="handleSearch">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="11" cy="11" r="8"></circle>
              <path d="M21 21l-4.35-4.35"></path>
            </svg>
          </button>
        </div>
      </div>

      <!-- 右侧：发布按钮和用户头像 -->
      <div class="nav-actions">
        <button class="publish-btn" @click="goToNewPost">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19"></line>
            <line x1="5" y1="12" x2="19" y2="12"></line>
          </svg>
          发布
        </button>

        <div class="user-avatar" @click="handleUserClick">
          <img
              v-if="userToken && userAvatar"
              :src="getImageUrl(userAvatar)"
              :alt="userName"
              class="avatar-img"
          >
          <div v-else class="default-avatar">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
              <circle cx="12" cy="7" r="4"></circle>
            </svg>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import API_URLS from '@/api/apiUrls.js';

export default {
  name: 'NavigationBar',
  data() {
    return {
      searchQuery: '',
      userToken: null,
      userAvatar: '',
      userName: ''
    }
  },
  mounted() {
    this.checkUserStatus()
  },
  methods: {
    checkUserStatus() {
      try {
        this.userToken = localStorage.getItem('accessToken')

        const userInfoStr = localStorage.getItem('userInfo')
        if (userInfoStr) {
          const userInfo = JSON.parse(userInfoStr)
          this.userName = userInfo.nickName || userInfo.userName
          this.userAvatar = this.getAvatarUrl(userInfo.avatar)
        }

        const tokenExpiration = localStorage.getItem('tokenExpiration')
        if (tokenExpiration && Date.now() > parseInt(tokenExpiration)) {
          this.clearUserStatus()
        }
      } catch (error) {
        console.error('检查用户状态时出错:', error)
        this.clearUserStatus()
      }
    },

    getAvatarUrl(avatarPath) {
      if (!avatarPath) return ''
      if (avatarPath.startsWith('http://') || avatarPath.startsWith('https://')) {
        return avatarPath
      }
      return avatarPath.startsWith('/') ? avatarPath : `/${avatarPath}`
    },

    clearUserStatus() {
      this.userToken = null
      this.userAvatar = ''
      this.userName = ''
    },

    switchToForum() {
      console.log('当前在论坛页面')
    },

    switchToShopping() {
      window.open('/shopping', '_blank')
    },

    handleSearch() {
      if (this.searchQuery.trim()) {
        // 修复：正确使用 Vue Router 的路由方法
        this.$router.push({
          name: 'Search', // 确保路由配置中有命名为 'Search' 的路由
          query: { q: this.searchQuery }
        })
      }
    },

    goToNewPost() {
      if (!this.userToken) {
        this.$router.push(API_URLS.getLoginRegisterPage())
        return
      }
      this.$router.push(API_URLS.getNewPostPage())
    },

    handleUserClick() {
      this.$router.push(this.userToken ? API_URLS.getProfileContainerPage() : API_URLS.getLoginRegisterPage())
    },

    getImageUrl(path) {
      if (!path) return ''
      let cleanPath = path.replace(/\\/g, '/')
      if (cleanPath.startsWith('http')) return cleanPath

      // 去除开头多余的斜杠
      if (cleanPath.startsWith('/')) {
        cleanPath = cleanPath.substring(1)
      }

      // 确保 BASE_URL 结尾没有斜杠
      let baseUrl = API_URLS.getPhotos()  // 或改为你实际使用的头像基础路径
      if (baseUrl.endsWith('/')) {
        baseUrl = baseUrl.slice(0, -1)
      }

      return `${baseUrl}/${cleanPath}`
    },
  }
}
</script>

<style scoped>
/* 样式部分保持不变 */
.navigation-bar {
  width: 100%;
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  padding: 0 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
}

.nav-tabs {
  display: flex;
  gap: 2px;
}

.nav-tab {
  padding: 8px 16px;
  border: none;
  background: transparent;
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.nav-tab:hover {
  background: #f3f4f6;
  color: #374151;
}

.nav-tab.active {
  background: #3b82f6;
  color: white;
}

.search-container {
  flex: 1;
  max-width: 400px;
  margin: 0 40px;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  width: 100%;
  padding: 10px 40px 10px 16px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s ease;
}

.search-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-btn {
  position: absolute;
  right: 8px;
  padding: 6px;
  border: none;
  background: transparent;
  color: #6b7280;
  cursor: pointer;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s ease;
}

.search-btn:hover {
  color: #3b82f6;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.publish-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.publish-btn:hover {
  background: #2563eb;
}

.user-avatar {
  width: 36px;
  height: 36px;
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid #e5e7eb;
  transition: border-color 0.2s ease;
}

.user-avatar:hover {
  border-color: #3b82f6;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.default-avatar {
  width: 100%;
  height: 100%;
  background: #f3f4f6;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6b7280;
}

@media (max-width: 768px) {
  .nav-content {
    padding: 0 10px;
  }

  .search-container {
    margin: 0 20px;
    max-width: 200px;
  }

  .nav-tab {
    padding: 6px 12px;
    font-size: 13px;
  }

  .publish-btn {
    padding: 6px 12px;
    font-size: 13px;
  }

  .user-avatar {
    width: 32px;
    height: 32px;
  }
}
</style>