<template>
  <div class="section-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <h1 class="page-title">论坛版块</h1>
      <p class="page-description">选择您感兴趣的游戏版块，与其他玩家交流讨论</p>
    </div>

    <!-- 筛选和搜索区域 -->
    <div class="filter-section">
      <div class="filter-row">
        <!-- 游戏类型筛选 -->
        <div class="filter-item">
          <label class="filter-label">游戏类型</label>
          <select v-model="selectedGameTypeId" @change="onGameTypeChange" class="filter-select">
            <option value="">全部类型</option>
            <option v-for="gameType in gameTypes" :key="gameType.typeId" :value="gameType.typeId">
              {{ gameType.typeName }}
            </option>
          </select>
        </div>

        <!-- 游戏筛选 -->
        <div class="filter-item" v-if="selectedGameTypeId">
          <label class="filter-label">游戏</label>
          <select v-model="selectedGameId" @change="onGameChange" class="filter-select">
            <option value="">全部游戏</option>
            <option v-for="game in filteredGames" :key="game.gameId" :value="game.gameId">
              {{ game.gameName }}
            </option>
          </select>
        </div>

        <!-- 搜索框 -->
        <div class="search-item">
          <div class="search-wrapper">
            <input
                v-model="searchKeyword"
                @input="onSearch"
                placeholder="搜索版块..."
                class="search-input"
            />
            <button @click="clearSearch" class="clear-btn" v-if="searchKeyword">
              ×
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <p class="loading-text">加载中...</p>
    </div>

    <!-- 版块列表 -->
    <div v-else class="sections-grid">
      <div v-if="sections.length === 0" class="no-data">
        <div class="no-data-icon">📁</div>
        <p>暂无相关版块</p>
      </div>
      <div
          v-else
          v-for="section in sections"
          :key="section.sectionId"
          class="section-card"
          @click="goToSection(section)"
      >
        <div class="section-header">
          <h3 class="section-name">{{ section.sectionName }}</h3>
          <span class="game-tag">{{ section.gameName }}</span>
        </div>
        <p class="section-description">{{ section.sectionDescription }}</p>
        <div class="section-footer">
          <span class="create-time">{{ formatDate(section.createTime) }}</span>
        </div>
      </div>
    </div>

    <!-- 热门版块推荐 -->
    <div class="hot-sections" v-if="!searchKeyword && !selectedGameId">
      <h2 class="section-title">热门版块</h2>
      <div class="hot-sections-list">
        <div
            v-for="hotSection in hotSections"
            :key="'hot-' + hotSection.sectionId"
            class="hot-section-item"
            @click="goToSection(hotSection)"
        >
          <span class="hot-section-name">{{ hotSection.sectionName }}</span>
          <span class="hot-section-game">{{ hotSection.gameName }}</span>
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
import axios from 'axios'
import {applyTheme, createScrollListener, scrollToTop, toggleTheme} from '@/utils/backToTopUtils.js'
import BackToTopToggle from "@/components/user/index/BackToTopToggle.vue";


export default defineComponent({
  name: "Section-page",
  components: {BackToTopToggle},
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
    // 响应式数据
    const sections = ref([])
    const gameTypes = ref([])
    const games = ref([])
    const filteredGames = ref([])
    const hotSections = ref([])
    const loading = ref(false)

    // 筛选条件
    const selectedGameTypeId = ref('')
    const selectedGameId = ref('')
    const searchKeyword = ref('')

    // 搜索防抖定时器
    let searchTimer = null

    // API 基础路径
    const API_BASE = 'http://localhost:8080'

    // 获取所有游戏类型
    const fetchGameTypes = async () => {
      try {
        const response = await axios.get(`${API_BASE}/user/gameType/all`)
        if (response.data.code === 200) {
          gameTypes.value = response.data.data
        }
      } catch (error) {
        console.error('获取游戏类型失败:', error)
      }
    }

    // 获取所有游戏
    const fetchGames = async () => {
      try {
        const response = await axios.get(`${API_BASE}/user/game/list`)
        if (response.data.code === 200) {
          games.value = response.data.data
        }
      } catch (error) {
        console.error('获取游戏列表失败:', error)
      }
    }

    // 根据游戏类型筛选游戏
    const filterGamesByType = (gameTypeId) => {
      if (!gameTypeId) {
        filteredGames.value = []
        return
      }

      // 这里需要调用API根据类型获取游戏
      fetchGamesByType(gameTypeId)
    }

    // 根据游戏类型获取游戏列表
    const fetchGamesByType = async (gameTypeId) => {
      try {
        const response = await axios.get(`${API_BASE}/user/game/type/${gameTypeId}`)
        if (response.data.code === 200) {
          filteredGames.value = response.data.data
        }
      } catch (error) {
        console.error('获取游戏列表失败:', error)
        filteredGames.value = []
      }
    }

    // 获取版块列表
    const fetchSections = async () => {
      loading.value = true
      try {
        let url = `${API_BASE}/user/section/list`

        // 如果有选择游戏，则根据游戏ID获取版块
        if (selectedGameId.value) {
          url = `${API_BASE}/user/section/game/${selectedGameId.value}`
        }

        const response = await axios.get(url)
        if (response.data.code === 200) {
          sections.value = response.data.data
        }
      } catch (error) {
        console.error('获取版块列表失败:', error)
        sections.value = []
      } finally {
        loading.value = false
      }
    }

    // 搜索版块
    const searchSections = async (keyword) => {
      if (!keyword.trim()) {
        fetchSections()
        return
      }

      loading.value = true
      try {
        const response = await axios.get(`${API_BASE}/user/section/search`, {
          params: {name: keyword}
        })
        if (response.data.code === 200) {
          sections.value = response.data.data
        }
      } catch (error) {
        console.error('搜索版块失败:', error)
        sections.value = []
      } finally {
        loading.value = false
      }
    }

    // 获取热门版块
    const fetchHotSections = async () => {
      try {
        const response = await axios.get(`${API_BASE}/user/section/hot`, {
          params: {limit: 5}
        })
        if (response.data.code === 200) {
          hotSections.value = response.data.data
        }
      } catch (error) {
        console.error('获取热门版块失败:', error)
      }
    }

    // 游戏类型改变事件
    const onGameTypeChange = () => {
      selectedGameId.value = ''
      filterGamesByType(selectedGameTypeId.value)
      fetchSections()
    }

    // 游戏改变事件
    const onGameChange = () => {
      fetchSections()
    }

    // 搜索输入事件（防抖）
    const onSearch = () => {
      if (searchTimer) {
        clearTimeout(searchTimer)
      }

      searchTimer = setTimeout(() => {
        searchSections(searchKeyword.value)
      }, 300)
    }

    // 清除搜索
    const clearSearch = () => {
      searchKeyword.value = ''
      fetchSections()
    }

    // 跳转到版块详情
    const goToSection = (section) => {
      const url = `/section?sectionId=${section.sectionId}`
      window.open(url, '_blank') // 在新标签页中打开
    }

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    // 监听搜索关键词变化
    watch(searchKeyword, (newVal) => {
      if (!newVal) {
        fetchSections()
      }
    })

    // 组件挂载时执行
    onMounted(async () => {
      await Promise.all([
        fetchGameTypes(),
        fetchGames(),
        fetchSections(),
        fetchHotSections()
      ])
    })

    return {
      sections,
      gameTypes,
      games,
      filteredGames,
      hotSections,
      loading,
      selectedGameTypeId,
      selectedGameId,
      searchKeyword,
      onGameTypeChange,
      onGameChange,
      onSearch,
      clearSearch,
      goToSection,
      formatDate
    }
  }
})
</script>

<style scoped>
/* 基础样式 */
.section-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  background-color: #fafbfc;
  min-height: 100vh;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 页面头部 */
.page-header {
  text-align: center;
  margin-bottom: 32px;
  padding: 40px 24px;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.page-title {
  font-size: 2.25rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 8px 0;
  letter-spacing: -0.025em;
}

.page-description {
  font-size: 1rem;
  color: #64748b;
  margin: 0;
  font-weight: 400;
}

/* 筛选区域 */
.filter-section {
  background: white;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-wrap: wrap;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 140px;
}

.filter-label {
  font-size: 0.875rem;
  font-weight: 500;
  color: #374151;
  margin: 0;
}

.filter-select {
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 0.875rem;
  background: white;
  color: #374151;
  transition: all 0.2s ease;
  cursor: pointer;
}

.filter-select:hover {
  border-color: #9ca3af;
}

.filter-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* 搜索区域 */
.search-item {
  margin-left: auto;
}

.search-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input {
  padding: 10px 40px 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 0.875rem;
  width: 240px;
  background: white;
  transition: all 0.2s ease;
}

.search-input:hover {
  border-color: #9ca3af;
}

.search-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-input::placeholder {
  color: #9ca3af;
}

.clear-btn {
  position: absolute;
  right: 8px;
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  font-size: 20px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.clear-btn:hover {
  color: #374151;
  background: #f3f4f6;
}

/* 加载状态 */
.loading {
  text-align: center;
  padding: 60px 20px;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid #e5e7eb;
  border-top: 3px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 16px;
}

.loading-text {
  color: #6b7280;
  font-size: 0.875rem;
  margin: 0;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 版块网格 */
.sections-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

/* 版块卡片 */
.section-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  transition: all 0.2s ease;
  cursor: pointer;
}

.section-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  border-color: #3b82f6;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
  gap: 12px;
}

.section-name {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
  line-height: 1.4;
  flex: 1;
}

.game-tag {
  background: #eff6ff;
  color: #1e40af;
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 500;
  white-space: nowrap;
  border: 1px solid #dbeafe;
}

.section-description {
  color: #6b7280;
  line-height: 1.5;
  margin: 0 0 16px 0;
  font-size: 0.875rem;
}

.section-footer {
  display: flex;
  justify-content: flex-end;
}

.create-time {
  font-size: 0.75rem;
  color: #9ca3af;
}

/* 无数据状态 */
.no-data {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 20px;
  color: #6b7280;
}

.no-data-icon {
  font-size: 3rem;
  margin-bottom: 16px;
  opacity: 0.5;
}

.no-data p {
  font-size: 1rem;
  margin: 0;
}

/* 热门版块 */
.hot-sections {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e5e7eb;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #f3f4f6;
}

.hot-sections-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.hot-section-item {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f8fafc;
  padding: 8px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid #e2e8f0;
  font-size: 0.875rem;
}

.hot-section-item:hover {
  background: #3b82f6;
  color: white;
  border-color: #3b82f6;
  transform: translateY(-1px);
}

.hot-section-name {
  font-weight: 500;
}

.hot-section-game {
  opacity: 0.7;
  font-size: 0.8125rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .section-page {
    padding: 16px;
  }

  .page-header {
    padding: 32px 20px;
    margin-bottom: 24px;
  }

  .page-title {
    font-size: 1.875rem;
  }

  .filter-section {
    padding: 20px;
  }

  .filter-row {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }

  .search-item {
    margin-left: 0;
  }

  .search-input {
    width: 100%;
  }

  .sections-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .hot-sections-list {
    flex-direction: column;
    align-items: flex-start;
  }

  .hot-section-item {
    align-self: flex-start;
  }
}

@media (max-width: 480px) {
  .section-page {
    padding: 12px;
  }

  .page-title {
    font-size: 1.625rem;
  }

  .filter-section,
  .hot-sections {
    padding: 16px;
  }

  .section-card {
    padding: 16px;
  }
}
</style>