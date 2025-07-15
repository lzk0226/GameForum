<script setup>
import {onBeforeUnmount, onMounted, reactive, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from 'axios'


const route = useRoute()
const router = useRouter()

// 搜索状态
const searchQuery = ref('')
const activeTab = ref('all')
const isLoading = ref(false)
const hasMore = ref(true)


// 导航到游戏详情页
const navigateToGame = (game) => {
  const url = `/game?gameId=${game.gameId}`
  window.open(url, '_blank')
}

// 导航到帖子详情页
const navigateToPost = (post) => {
  const url = `/postDetail/${post.postId}`
  window.open(url, '_blank')
}

// 导航到版块页面
const navigateToSection = (section) => {
  const url = `/section?sectionId=${section.sectionId}`
  window.open(url, '_blank')
}

// 导航到游戏类型页面
const navigateToGameType = (gameType) => {
  const url = `/gamepage?typeId=${gameType.typeId}`
  window.open(url, '_blank')
}

// 通用导航函数
const openInNewTab = (url) => {
  window.open(url, '_blank')
}

// 搜索结果
const searchResults = reactive({
  games: [],
  posts: [],
  sections: [],
  gameTypes: []
})

// 分页参数
const pagination = reactive({
  games: {page: 1, size: 10},
  posts: {page: 1, size: 10},
  sections: {page: 1, size: 10},
  gameTypes: {page: 1, size: 10}
})

// 搜索统计
const searchStats = reactive({
  games: 0,
  posts: 0,
  sections: 0,
  gameTypes: 0,
  total: 0
})

// 标签页配置
const tabs = [
  {key: 'all', label: '全部', icon: '🔍'},
  {key: 'games', label: '游戏', icon: '🎮'},
  {key: 'posts', label: '帖子', icon: '📝'},
  {key: 'sections', label: '版块', icon: '📋'},
  {key: 'gameTypes', label: '游戏类型', icon: '🏷️'}
]

// API 调用函数
const searchGames = async (query, page = 1) => {
  try {
    const response = await axios.get(`/user/game/search?name=${encodeURIComponent(query)}`)
    return response.data
  } catch (error) {
    console.error('搜索游戏失败:', error)
    return {code: 500, data: []}
  }
}

const searchPosts = async (query, page = 1) => {
  try {
    const response = await axios.get(`/user/post/search?title=${encodeURIComponent(query)}`)
    return response.data
  } catch (error) {
    console.error('搜索帖子失败:', error)
    return {code: 500, data: []}
  }
}

const searchSections = async (query, page = 1) => {
  try {
    const response = await axios.get(`/user/section/search?name=${encodeURIComponent(query)}`)
    return response.data
  } catch (error) {
    console.error('搜索版块失败:', error)
    return {code: 500, data: []}
  }
}

const searchGameTypes = async (query, page = 1) => {
  try {
    const response = await axios.get(`/user/gameType/search?name=${encodeURIComponent(query)}`)
    return response.data
  } catch (error) {
    console.error('搜索游戏类型失败:', error)
    return {code: 500, data: []}
  }
}

// 执行搜索
const performSearch = async (loadMore = false) => {
  if (!searchQuery.value.trim()) return

  isLoading.value = true

  try {
    const promises = []

    if (activeTab.value === 'all' || activeTab.value === 'games') {
      promises.push(searchGames(searchQuery.value, loadMore ? pagination.games.page : 1))
    }
    if (activeTab.value === 'all' || activeTab.value === 'posts') {
      promises.push(searchPosts(searchQuery.value, loadMore ? pagination.posts.page : 1))
    }
    if (activeTab.value === 'all' || activeTab.value === 'sections') {
      promises.push(searchSections(searchQuery.value, loadMore ? pagination.sections.page : 1))
    }
    if (activeTab.value === 'all' || activeTab.value === 'gameTypes') {
      promises.push(searchGameTypes(searchQuery.value, loadMore ? pagination.gameTypes.page : 1))
    }

    const results = await Promise.all(promises)

    let resultIndex = 0

    // 去重辅助函数
    const removeDuplicates = (array, key) => {
      const seen = new Set()
      return array.filter(item => {
        const identifier = item[key]
        if (seen.has(identifier)) {
          return false
        }
        seen.add(identifier)
        return true
      })
    }

    if (activeTab.value === 'all' || activeTab.value === 'games') {
      const gameResult = results[resultIndex++]
      if (gameResult.code === 200 && gameResult.data) {
        if (loadMore) {
          // 合并数据并去重
          const combined = [...searchResults.games, ...gameResult.data]
          searchResults.games = removeDuplicates(combined, 'gameId')
        } else {
          searchResults.games = removeDuplicates(gameResult.data, 'gameId')
        }
        searchStats.games = searchResults.games.length
      }
    }

    if (activeTab.value === 'all' || activeTab.value === 'posts') {
      const postResult = results[resultIndex++]
      if (postResult.code === 200 && postResult.data) {
        if (loadMore) {
          // 合并数据并去重
          const combined = [...searchResults.posts, ...postResult.data]
          searchResults.posts = removeDuplicates(combined, 'postId')
        } else {
          searchResults.posts = removeDuplicates(postResult.data, 'postId')
        }
        searchStats.posts = searchResults.posts.length
      }
    }

    if (activeTab.value === 'all' || activeTab.value === 'sections') {
      const sectionResult = results[resultIndex++]
      if (sectionResult.code === 200 && sectionResult.data) {
        if (loadMore) {
          // 合并数据并去重
          const combined = [...searchResults.sections, ...sectionResult.data]
          searchResults.sections = removeDuplicates(combined, 'sectionId')
        } else {
          searchResults.sections = removeDuplicates(sectionResult.data, 'sectionId')
        }
        searchStats.sections = searchResults.sections.length
      }
    }

    if (activeTab.value === 'all' || activeTab.value === 'gameTypes') {
      const gameTypeResult = results[resultIndex++]
      if (gameTypeResult.code === 200 && gameTypeResult.data) {
        if (loadMore) {
          // 合并数据并去重
          const combined = [...searchResults.gameTypes, ...gameTypeResult.data]
          searchResults.gameTypes = removeDuplicates(combined, 'typeId')
        } else {
          searchResults.gameTypes = removeDuplicates(gameTypeResult.data, 'typeId')
        }
        searchStats.gameTypes = searchResults.gameTypes.length
      }
    }

    // 计算总数
    searchStats.total = searchStats.games + searchStats.posts + searchStats.sections + searchStats.gameTypes

    // 检查是否还有更多数据
    if (activeTab.value !== 'all') {
      const currentTabResult = results[0]
      if (currentTabResult && currentTabResult.data) {
        hasMore.value = currentTabResult.data.length >= pagination[activeTab.value].size
      }
    }

  } catch (error) {
    console.error('搜索失败:', error)
  } finally {
    isLoading.value = false
  }
}

// 切换标签
const switchTab = (tabKey) => {
  if (activeTab.value === tabKey) return // 如果是当前标签，不需要重新搜索

  activeTab.value = tabKey
  resetPagination()

  // 如果已经有搜索结果，直接显示，不需要重新搜索
  if (searchQuery.value.trim()) {
    performSearch()
  }
}

// 重置分页
const resetPagination = () => {
  pagination.games.page = 1
  pagination.posts.page = 1
  pagination.sections.page = 1
  pagination.gameTypes.page = 1
}

// 加载更多
const loadMore = () => {
  if (isLoading.value || !hasMore.value) return

  if (activeTab.value === 'games') {
    pagination.games.page++
  } else if (activeTab.value === 'posts') {
    pagination.posts.page++
  } else if (activeTab.value === 'sections') {
    pagination.sections.page++
  } else if (activeTab.value === 'gameTypes') {
    pagination.gameTypes.page++
  }

  performSearch(true)
}

// 搜索输入处理
const handleSearch = () => {
  if (!searchQuery.value.trim()) {
    // 清空搜索结果
    searchResults.games = []
    searchResults.posts = []
    searchResults.sections = []
    searchResults.gameTypes = []
    searchStats.games = 0
    searchStats.posts = 0
    searchStats.sections = 0
    searchStats.gameTypes = 0
    searchStats.total = 0
    return
  }

  resetPagination()
  performSearch()
  // 更新 URL
  router.push({path: '/search', query: {q: searchQuery.value}})
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 2592000000) return `${Math.floor(diff / 86400000)}天前`

  return date.toLocaleDateString()
}

// 截取文本
const truncateText = (text, maxLength = 100) => {
  if (!text) return ''
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}

// 监听路由变化
watch(() => route.query.q, (newQuery) => {
  if (newQuery) {
    searchQuery.value = newQuery
    resetPagination()
    performSearch()
  }
}, {immediate: true})

// 无限滚动
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const windowHeight = window.innerHeight
  const documentHeight = document.documentElement.scrollHeight

  if (scrollTop + windowHeight >= documentHeight - 100) {
    loadMore()
  }
}

onMounted(() => {
  // 从 URL 获取搜索词
  if (route.query.q) {
    searchQuery.value = route.query.q
    performSearch()
  }

  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)
})

// 组件卸载时移除滚动监听
onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <div class="search-container">
    <!-- 搜索头部 -->
    <div class="search-header">
      <div class="search-box">
        <input
            v-model="searchQuery"
            type="text"
            placeholder="搜索游戏、帖子、版块..."
            class="search-input"
            @keyup.enter="handleSearch"
        >
        <button @click="handleSearch" class="search-btn">
          <span class="search-icon">🔍</span>
        </button>
      </div>
    </div>

    <!-- 搜索结果统计 -->
    <div class="search-stats" v-if="searchQuery && searchStats.total > 0">
      <span class="stats-text">找到 <strong>{{ searchStats.total }}</strong> 个结果</span>
      <span class="search-keyword">关于 "{{ searchQuery }}"</span>
    </div>

    <!-- 标签页导航 -->
    <div class="tabs-nav">
      <button
          v-for="tab in tabs"
          :key="tab.key"
          :class="['tab-btn', { active: activeTab === tab.key }]"
          @click="switchTab(tab.key)"
      >
        <span class="tab-icon">{{ tab.icon }}</span>
        <span class="tab-label">{{ tab.label }}</span>
        <span v-if="searchStats[tab.key] > 0" class="tab-count">{{ searchStats[tab.key] }}</span>
      </button>
    </div>

    <!-- 搜索结果内容 -->
    <div class="search-content">
      <!-- 加载状态 -->
      <div v-if="isLoading && searchResults.games.length === 0" class="loading-state">
        <div class="loading-spinner"></div>
        <p>搜索中...</p>
      </div>

      <!-- 无结果状态 -->
      <div v-else-if="!isLoading && searchStats.total === 0 && searchQuery" class="empty-state">
        <div class="empty-icon">😔</div>
        <h3>没有找到相关结果</h3>
        <p>尝试调整搜索关键词或浏览其他内容</p>
      </div>

      <!-- 搜索结果 -->
      <div v-else class="results-container">
        <!-- 游戏结果 -->
        <div v-if="(activeTab === 'all' || activeTab === 'games') && searchResults.games.length > 0"
             class="result-section">
          <h3 v-if="activeTab === 'all'" class="section-title">
            <span class="title-icon">🎮</span>
            游戏 ({{ searchStats.games }})
          </h3>
          <div class="games-grid">
            <div
                v-for="game in searchResults.games"
                :key="game.gameId"
                class="game-card clickable"
                @click="navigateToGame(game)"
            >
              <div class="game-cover">
                <img :src="game.gameIcon || '/placeholder-game.jpg'" :alt="game.gameName"/>
              </div>
              <div class="game-info">
                <h4 class="game-name">{{ game.gameName }}</h4>
                <p class="game-desc">{{ truncateText(game.gameDescription) }}</p>
                <div class="game-meta">
                  <span class="game-type">{{ game.gameTypeName }}</span>
                  <span class="game-rating">⭐ {{ game.gameRating || 'N/A' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 帖子结果 -->
        <div v-if="(activeTab === 'all' || activeTab === 'posts') && searchResults.posts.length > 0"
             class="result-section">
          <h3 v-if="activeTab === 'all'" class="section-title">
            <span class="title-icon">📝</span>
            帖子 ({{ searchStats.posts }})
          </h3>
          <div class="posts-list">
            <div
                v-for="post in searchResults.posts"
                :key="post.postId"
                class="post-card clickable"
                @click="navigateToPost(post)"
            >
              <div class="post-header">
                <h4 class="post-title">{{ post.postTitle }}</h4>
                <span class="post-time">{{ formatTime(post.createTime) }}</span>
              </div>
              <p class="post-content">{{ truncateText(post.postContent) }}</p>
              <div class="post-meta">
                <span class="post-author">👤 {{ post.createBy }}</span>
                <span class="post-section">📋 {{ post.sectionName }}</span>
                <span class="post-stats">
                    👍 {{ post.likeCount || 0 }}
                   👁️ {{ post.viewCount || 0 }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 版块结果 -->
        <div v-if="(activeTab === 'all' || activeTab === 'sections') && searchResults.sections.length > 0"
             class="result-section">
          <h3 v-if="activeTab === 'all'" class="section-title">
            <span class="title-icon">📋</span>
            版块 ({{ searchStats.sections }})
          </h3>
          <div class="sections-grid">
            <div
                v-for="section in searchResults.sections"
                :key="section.sectionId"
                class="section-card clickable"
                @click="navigateToSection(section)"
            >
              <div class="section-icon">📋</div>
              <div class="section-info">
                <h4 class="section-name">{{ section.sectionName }}</h4>
                <p class="section-desc">{{ truncateText(section.sectionDescription) }}</p>
                <div class="section-meta">
                  <span class="section-game">🎮 {{ section.gameName }}</span>
                  <span class="section-posts">📝 {{ section.postCount || 0 }} 帖子</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 游戏类型结果 -->
        <div v-if="(activeTab === 'all' || activeTab === 'gameTypes') && searchResults.gameTypes.length > 0"
             class="result-section">
          <h3 v-if="activeTab === 'all'" class="section-title">
            <span class="title-icon">🏷️</span>
            游戏类型 ({{ searchStats.gameTypes }})
          </h3>
          <div class="game-types-list">
            <div
                v-for="gameType in searchResults.gameTypes"
                :key="gameType.typeId"
                class="game-type-card clickable"
                @click="navigateToGameType(gameType)"
            >
              <div class="game-type-icon">🏷️</div>
              <div class="game-type-info">
                <h4 class="game-type-name">{{ gameType.typeName }}</h4>
                <p class="game-type-desc">{{ truncateText(gameType.typeDescription) }}</p>
                <div class="game-type-meta">
                  <span class="game-count">🎮 {{ gameType.gameCount || 0 }} 个游戏</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 加载更多状态 -->
      <div v-if="isLoading && searchStats.total > 0" class="load-more-state">
        <div class="loading-spinner small"></div>
        <span>加载更多...</span>
      </div>
    </div>
  </div>
</template>

<style scoped>

/* 添加到现有CSS中的可点击样式 */

.clickable {
  cursor: pointer;
  user-select: none;
}

.game-card.clickable:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.post-card.clickable:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.section-card.clickable:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.game-type-card.clickable:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

/* 可选：添加点击时的反馈效果 */
.clickable:active {
  transform: scale(0.98);
  transition: transform 0.1s;
}

/* 鼠标悬停时显示提示 */
.clickable::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 123, 255, 0.05);
  opacity: 0;
  transition: opacity 0.3s;
  pointer-events: none;
  border-radius: inherit;
}

.clickable:hover::after {
  opacity: 1;
}

/* 确保卡片有相对定位以支持伪元素 */
.game-card, .post-card, .section-card, .game-type-card {
  position: relative;
}


.search-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
}

/* 搜索头部 */
.search-header {
  margin-bottom: 30px;
}

.search-box {
  display: flex;
  max-width: 600px;
  margin: 0 auto;
  background: white;
  border-radius: 50px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.search-input {
  flex: 1;
  padding: 15px 25px;
  border: none;
  outline: none;
  font-size: 16px;
  background: transparent;
}

.search-input::placeholder {
  color: #999;
}

.search-btn {
  padding: 15px 25px;
  border: none;
  background: #007bff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background: #0056b3;
}

.search-icon {
  font-size: 18px;
}

/* 搜索统计 */
.search-stats {
  text-align: center;
  margin-bottom: 30px;
  color: #666;
}

.stats-text {
  margin-right: 15px;
}

.search-keyword {
  color: #007bff;
  font-weight: 500;
}

/* 标签页导航 */
.tabs-nav {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
  border-bottom: 2px solid #f0f0f0;
  overflow-x: auto;
  padding-bottom: 10px;
}

.tab-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  border: none;
  background: transparent;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s;
  white-space: nowrap;
  color: #666;
}

.tab-btn:hover {
  background: #f8f9fa;
  color: #007bff;
}

.tab-btn.active {
  background: #007bff;
  color: white;
}

.tab-icon {
  font-size: 16px;
}

.tab-count {
  background: rgba(255, 255, 255, 0.3);
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
}

.tab-btn.active .tab-count {
  background: rgba(255, 255, 255, 0.3);
}

.tab-btn:not(.active) .tab-count {
  background: #e9ecef;
  color: #666;
}

/* 加载和空状态 */
.loading-state, .empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

.loading-spinner.small {
  width: 20px;
  height: 20px;
  border-width: 2px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.empty-icon {
  font-size: 60px;
  margin-bottom: 20px;
}

.empty-state h3 {
  margin-bottom: 10px;
  color: #333;
}

/* 结果容器 */
.results-container {
  space-y: 40px;
}

.result-section {
  margin-bottom: 40px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  font-size: 20px;
  color: #333;
  border-bottom: 2px solid #007bff;
  padding-bottom: 10px;
}

.title-icon {
  font-size: 22px;
}

/* 游戏卡片 */
.games-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.game-card {
  display: flex;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.game-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.game-cover {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
}

.game-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.game-info {
  padding: 15px;
  flex: 1;
}

.game-name {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.game-desc {
  margin: 0 0 12px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.4;
}

.game-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
}

.game-type {
  background: #e3f2fd;
  color: #1976d2;
  padding: 4px 8px;
  border-radius: 4px;
}

.game-rating {
  color: #ff9800;
  font-weight: 500;
}

/* 帖子列表 */
.posts-list {
  space-y: 15px;
}

.post-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 15px;
}

.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.post-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
  flex: 1;
  margin-right: 15px;
}

.post-time {
  color: #999;
  font-size: 12px;
  white-space: nowrap;
}

.post-content {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.6;
}

.post-meta {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: #999;
  flex-wrap: wrap;
}

.post-meta > span {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 版块网格 */
.sections-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.section-card {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.section-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.section-icon {
  font-size: 24px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
  border-radius: 8px;
  flex-shrink: 0;
}

.section-info {
  flex: 1;
}

.section-name {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.section-desc {
  margin: 0 0 12px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.4;
}

.section-meta {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #999;
}

/* 游戏类型列表 */
.game-types-list {
  space-y: 15px;
}

.game-type-card {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 15px;
}

.game-type-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.game-type-icon {
  font-size: 24px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff3e0;
  border-radius: 8px;
  flex-shrink: 0;
}

.game-type-info {
  flex: 1;
}

.game-type-name {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.game-type-desc {
  margin: 0 0 12px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.4;
}

.game-type-meta {
  font-size: 12px;
  color: #999;
}

.game-count {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 加载更多状态 */
.load-more-state {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 30px;
  color: #666;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-container {
    padding: 15px;
  }

  .games-grid {
    grid-template-columns: 1fr;
  }

  .sections-grid {
    grid-template-columns: 1fr;
  }

  .tabs-nav {
    gap: 5px;
  }

  .tab-btn {
    padding: 10px 15px;
    font-size: 14px;
  }

  .post-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .post-meta {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }
}

@media (max-width: 480px) {
  .search-box {
    border-radius: 25px;
  }

  .search-input, .search-btn {
    padding: 12px 20px;
  }

  .game-card {
    flex-direction: column;
  }

  .game-cover {
    width: 100%;
    height: 120px;
  }

  .section-card, .game-type-card {
    flex-direction: column;
    text-align: center;
  }

  .section-icon, .game-type-icon {
    align-self: center;
  }
}
</style>