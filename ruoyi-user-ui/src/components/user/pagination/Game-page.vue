<template>
  <div class="game-page">
    <!-- 顶部搜索栏 -->
    <div class="search-header">
      <div class="search-container">
        <div class="search-box">
          <el-input
              v-model="searchKeyword"
              placeholder="搜索游戏名称..."
              class="search-input"
              @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon>
                <Search/>
              </el-icon>
            </template>
          </el-input>
          <el-button type="primary" @click="handleSearch" class="search-btn">
            搜索
          </el-button>
        </div>
      </div>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-section">
      <div class="filter-container">
        <div class="filter-tabs">
          <el-button
              :type="activeTab === 'all' ? 'primary' : 'default'"
              @click="switchTab('all')"
              class="filter-tab"
          >
            全部游戏
          </el-button>
          <el-button
              :type="activeTab === 'hot' ? 'primary' : 'default'"
              @click="switchTab('hot')"
              class="filter-tab"
          >
            热门游戏
          </el-button>
        </div>

        <!-- 游戏类型筛选 -->
        <div class="type-filter">
          <span class="filter-label">游戏类型：</span>
          <el-button
              :type="selectedTypeId === null ? 'primary' : 'default'"
              @click="filterByType(null)"
              size="small"
              class="type-btn"
          >
            全部
          </el-button>
          <el-button
              v-for="type in gameTypes"
              :key="type.typeId"
              :type="selectedTypeId === type.typeId ? 'primary' : 'default'"
              @click="filterByType(type.typeId)"
              size="small"
              class="type-btn"
          >
            {{ type.typeName }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 游戏列表 -->
    <div class="game-content">
      <div class="game-container">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-section">
          <el-skeleton :rows="3" animated/>
        </div>

        <!-- 游戏网格 -->
        <div v-else-if="gameList.length > 0" class="game-grid">
          <div
              v-for="game in gameList"
              :key="game.gameId"
              class="game-card"
              @click="viewGameDetail(game)"
          >
            <div class="game-image">
              <img :src="getImageUrl(game.gameIcon)" :alt="game.gameName"/>
              <div class="game-overlay">
                <el-button type="primary" size="small">查看详情</el-button>
              </div>
            </div>
            <div class="game-info">
              <h3 class="game-title">{{ game.gameName }}</h3>
              <p class="game-type">{{ game.gameTypeName }}</p>
              <p class="game-desc">{{ truncateText(game.gameDescription, 60) }}</p>
              <div class="game-meta">
                <span class="create-time">{{ formatDate(game.createTime) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-state">
          <el-empty description="暂无游戏数据"/>
        </div>
      </div>
    </div>
  </div>
  <BackToTopToggle
      :atTop="isAtTop"
      :theme="theme"
      :isHomePage="false"
      @toggle-theme="handleToggleTheme"
      @scroll-top="handleScrollTop"
  />
</template>

<script>
import {defineComponent, onMounted, ref, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {Search} from '@element-plus/icons-vue'
import {useRoute, useRouter} from 'vue-router'
import axios from 'axios'
import {applyTheme, createScrollListener, scrollToTop, toggleTheme} from '@/utils/backToTopUtils.js'
import BackToTopToggle from "@/components/user/index/BackToTopToggle.vue";

export default defineComponent({
  name: "GamePage",
  components: {
    BackToTopToggle,
    Search
  },
  data() {
    return {
      isAtTop: true,
      theme: 'light',  // light / dark
      cleanupScrollListener: null
    }
  },
  mounted() {
    // 使用工具函数创建滚动监听器
    this.cleanupScrollListener = createScrollListener((atTop) => {
      this.isAtTop = atTop;
    });

    // 应用初始主题
    applyTheme(this.theme);
  },
  beforeUnmount() {
    // 清理滚动监听器
    if (this.cleanupScrollListener) {
      this.cleanupScrollListener();
    }
  },
  methods: {
    handleScrollTop() {
      scrollToTop();
    },
    handleToggleTheme() {
      this.theme = toggleTheme(this.theme);
      applyTheme(this.theme);
    }
  },
  setup() {
    const router = useRouter()
    const route = useRoute()

    // 响应式数据
    const gameList = ref([])
    const gameTypes = ref([])
    const loading = ref(false)
    const searchKeyword = ref('')
    const selectedTypeId = ref(null)
    const activeTab = ref('all')

    // API 基础地址
    const API_BASE = 'http://localhost:8080'

    // 获取游戏列表
    const fetchGameList = async (params = {}) => {
      loading.value = true
      try {
        const response = await axios.get(`${API_BASE}/user/game/list`, {params})
        if (response.data.code === 200) {
          gameList.value = response.data.data || []
        } else {
          ElMessage.error(response.data.msg || '获取游戏列表失败')
        }
      } catch (error) {
        console.error('获取游戏列表失败:', error)
        ElMessage.error('网络错误，请稍后重试')
      } finally {
        loading.value = false
      }
    }

    // 获取热门游戏列表
    const fetchHotGameList = async (limit = 20) => {
      loading.value = true
      try {
        const response = await axios.get(`${API_BASE}/user/game/hot`, {
          params: {limit}
        })
        if (response.data.code === 200) {
          gameList.value = response.data.data || []
        } else {
          ElMessage.error(response.data.msg || '获取热门游戏失败')
        }
      } catch (error) {
        console.error('获取热门游戏失败:', error)
        ElMessage.error('网络错误，请稍后重试')
      } finally {
        loading.value = false
      }
    }

    // 根据类型获取游戏列表
    const fetchGameListByType = async (typeId) => {
      loading.value = true
      try {
        const response = await axios.get(`${API_BASE}/user/game/type/${typeId}`)
        if (response.data.code === 200) {
          gameList.value = response.data.data || []
        } else {
          ElMessage.error(response.data.msg || '获取游戏列表失败')
        }
      } catch (error) {
        console.error('获取游戏列表失败:', error)
        ElMessage.error('网络错误，请稍后重试')
      } finally {
        loading.value = false
      }
    }

    // 搜索游戏
    const searchGameByName = async (name) => {
      loading.value = true
      try {
        const response = await axios.get(`${API_BASE}/user/game/search`, {
          params: {name}
        })
        if (response.data.code === 200) {
          gameList.value = response.data.data || []
        } else {
          ElMessage.error(response.data.msg || '搜索失败')
        }
      } catch (error) {
        console.error('搜索失败:', error)
        ElMessage.error('网络错误，请稍后重试')
      } finally {
        loading.value = false
      }
    }

    // 获取游戏类型列表
    const fetchGameTypes = async () => {
      try {
        const response = await axios.get(`${API_BASE}/user/gameType/all`)
        if (response.data.code === 200) {
          gameTypes.value = response.data.data || []
        }
      } catch (error) {
        console.error('获取游戏类型失败:', error)
      }
    }

    // 处理URL参数，设置初始类型
    const handleUrlParams = () => {
      const typeId = route.query.typeId
      if (typeId) {
        const typeIdNum = parseInt(typeId)
        if (!isNaN(typeIdNum)) {
          selectedTypeId.value = typeIdNum
          activeTab.value = 'all'
          // 等待游戏类型数据加载完成后再获取游戏列表
          if (gameTypes.value.length > 0) {
            fetchGameListByType(typeIdNum)
          }
        }
      }
    }

    // 处理搜索
    const handleSearch = () => {
      if (searchKeyword.value.trim()) {
        searchGameByName(searchKeyword.value.trim())
        activeTab.value = 'all'
        selectedTypeId.value = null
        // 清除URL参数
        updateUrlParams()
      } else {
        fetchGameList()
      }
    }

    // 切换标签页
    const switchTab = (tab) => {
      activeTab.value = tab
      selectedTypeId.value = null
      searchKeyword.value = ''
      // 清除URL参数
      updateUrlParams()

      if (tab === 'hot') {
        fetchHotGameList()
      } else {
        fetchGameList()
      }
    }

    // 根据类型筛选
    const filterByType = (typeId) => {
      selectedTypeId.value = typeId
      searchKeyword.value = ''

      if (typeId === null) {
        // 清除URL参数
        updateUrlParams()
        if (activeTab.value === 'hot') {
          fetchHotGameList()
        } else {
          fetchGameList()
        }
      } else {
        // 更新URL参数
        updateUrlParams(typeId)
        fetchGameListByType(typeId)
        activeTab.value = 'all'
      }
    }

    // 更新URL参数
    const updateUrlParams = (typeId = null) => {
      const query = {...route.query}
      if (typeId) {
        query.typeId = typeId
      } else {
        delete query.typeId
      }

      // 只有当查询参数发生变化时才更新路由
      if (JSON.stringify(query) !== JSON.stringify(route.query)) {
        router.replace({query})
      }
    }

    // 查看游戏详情 - 修复：跳转到单独的游戏详情页面
    const viewGameDetail = (game) => {
      // 跳转到游戏详情页面，传递游戏ID作为查询参数
      router.push({
        name: 'Game',
        query: {
          gameId: game.gameId
        }
      })
    }

    // 获取图片URL
    const getImageUrl = (imagePath) => {
      if (!imagePath) return '/placeholder-game.jpg'
      if (imagePath.startsWith('http')) return imagePath
      return `/${imagePath}`
    }

    // 截断文本
    const truncateText = (text, maxLength) => {
      if (!text) return ''
      return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
    }

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    // 监听路由查询参数变化
    watch(() => route.query.typeId, (newTypeId) => {
      if (newTypeId && gameTypes.value.length > 0) {
        const typeIdNum = parseInt(newTypeId)
        if (!isNaN(typeIdNum)) {
          selectedTypeId.value = typeIdNum
          activeTab.value = 'all'
          searchKeyword.value = ''
          fetchGameListByType(typeIdNum)
        }
      } else if (!newTypeId && selectedTypeId.value !== null) {
        // 如果URL中没有typeId但当前有选中的类型，则重置
        selectedTypeId.value = null
        if (activeTab.value === 'hot') {
          fetchHotGameList()
        } else {
          fetchGameList()
        }
      }
    })

    // 监听游戏类型数据加载完成，处理初始URL参数
    watch(gameTypes, (newGameTypes) => {
      if (newGameTypes.length > 0) {
        handleUrlParams()
      }
    })

    // 组件挂载时获取数据
    onMounted(async () => {
      await fetchGameTypes()
      // 如果没有URL参数中的typeId，则加载默认数据
      if (!route.query.typeId) {
        fetchGameList()
      }
    })

    return {
      gameList,
      gameTypes,
      loading,
      searchKeyword,
      selectedTypeId,
      activeTab,
      handleSearch,
      switchTab,
      filterByType,
      viewGameDetail,
      getImageUrl,
      truncateText,
      formatDate
    }
  }
})
</script>

<style scoped>
.game-page {
  min-height: 100vh;
  background: #f8f9fa;
}

/* 搜索头部 */
.search-header {
  background: #ffffff;
  padding: 20px 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #e9ecef;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.search-box {
  display: flex;
  gap: 12px;
  align-items: center;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  flex: 1;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  padding: 0 16px;
  height: 40px;
  border: 1px solid #dee2e6;
  box-shadow: none;
}

.search-input :deep(.el-input__wrapper:hover) {
  border-color: #adb5bd;
}

.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.search-btn {
  height: 40px;
  padding: 0 24px;
  border-radius: 8px;
  font-weight: 500;
  border: none;
}

/* 筛选区域 */
.filter-section {
  background: #ffffff;
  padding: 20px 0;
  border-bottom: 1px solid #e9ecef;
}

.filter-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.filter-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.filter-tab {
  border-radius: 8px;
  font-weight: 500;
  height: 36px;
  padding: 0 16px;
  border: 1px solid #dee2e6;
}

.filter-tab.el-button--default {
  background: #ffffff;
  color: #495057;
}

.filter-tab.el-button--default:hover {
  background: #f8f9fa;
  border-color: #adb5bd;
}

.type-filter {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.filter-label {
  font-weight: 500;
  color: #495057;
  margin-right: 12px;
  font-size: 14px;
}

.type-btn {
  border-radius: 6px;
  margin: 2px;
  height: 32px;
  padding: 0 12px;
  font-size: 13px;
  border: 1px solid #dee2e6;
}

.type-btn.el-button--default {
  background: #ffffff;
  color: #6c757d;
}

.type-btn.el-button--default:hover {
  background: #f8f9fa;
  border-color: #adb5bd;
}

/* 游戏内容区域 */
.game-content {
  padding: 30px 0;
  min-height: 500px;
}

.game-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.loading-section {
  background: #ffffff;
  border-radius: 12px;
  padding: 30px;
  border: 1px solid #e9ecef;
}

/* 游戏网格 */
.game-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.game-card {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #e9ecef;
  transition: all 0.2s ease;
  cursor: pointer;
}

.game-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border-color: #dee2e6;
}

.game-image {
  position: relative;
  height: 180px;
  overflow: hidden;
  background: #f8f9fa;
}

.game-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.game-card:hover .game-image img {
  transform: scale(1.05);
}

.game-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.game-card:hover .game-overlay {
  opacity: 1;
}

.game-overlay .el-button {
  border-radius: 6px;
  font-size: 13px;
  padding: 8px 16px;
}

.game-info {
  padding: 16px;
}

.game-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #212529;
  line-height: 1.4;
}

.game-type {
  color: #6c757d;
  font-size: 13px;
  font-weight: 500;
  margin: 0 0 10px 0;
  padding: 2px 8px;
  background: #f8f9fa;
  border-radius: 4px;
  display: inline-block;
}

.game-desc {
  color: #6c757d;
  font-size: 13px;
  line-height: 1.5;
  margin: 0 0 12px 0;
}

.game-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #adb5bd;
}

/* 空状态 */
.empty-state {
  background: #ffffff;
  border-radius: 12px;
  padding: 60px 30px;
  text-align: center;
  border: 1px solid #e9ecef;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .search-box {
    flex-direction: column;
    align-items: stretch;
  }

  .search-btn {
    width: 100%;
  }

  .filter-tabs {
    justify-content: center;
  }

  .type-filter {
    justify-content: center;
  }

  .game-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}
</style>