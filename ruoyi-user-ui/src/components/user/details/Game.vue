<script>
import { defineComponent, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { applyTheme, createScrollListener, scrollToTop, toggleTheme } from '@/utils/backToTopUtils.js'
import API_URLS from '@/api/apiUrls.js'
import BackToTopToggle from "@/components/user/index/BackToTopToggle.vue"

export default defineComponent({
  name: "Game",
  components: { BackToTopToggle },
  data() {
    return {
      isAtTop: true,
      theme: 'light',
      cleanupScrollListener: null
    }
  },
  mounted() {
    this.cleanupScrollListener = createScrollListener((atTop) => {
      this.isAtTop = atTop;
    })
    applyTheme(this.theme)
  },
  beforeUnmount() {
    if (this.cleanupScrollListener) {
      this.cleanupScrollListener()
    }
  },
  methods: {
    handleScrollTop() {
      scrollToTop()
    },
    handleToggleTheme() {
      this.theme = toggleTheme(this.theme)
      applyTheme(this.theme)
    }
  },
  setup() {
    const route = useRoute()
    const game = ref(null)
    const loading = ref(true)
    const error = ref('')
    const currentImageIndex = ref(0)

    const fetchGameDetail = async () => {
      try {
        loading.value = true
        const gameId = route.query.gameId

        if (!gameId) {
          error.value = '游戏ID不能为空'
          return
        }

        // ✅ 使用配置文件中的接口
        const response = await axios.get(API_URLS.getGameDetail(gameId))

        if (response.data.code === 200) {
          game.value = response.data.data
        } else {
          error.value = response.data.msg || '获取游戏信息失败'
        }
      } catch (err) {
        error.value = '网络请求失败，请稍后重试'
        console.error('获取游戏详情失败:', err)
      } finally {
        loading.value = false
      }
    }

    const getImageUrl = (path) => {
      if (!path) return ''
      const cleanPath = path.replace(/\\/g, '/')
      if (cleanPath.startsWith('http')) return cleanPath
      return API_URLS.getGamePhoto() + cleanPath
    }

    const getIconUrl = (path) => {
      if (!path) return ''
      const cleanPath = path.replace(/\\/g, '/')
      if (cleanPath.startsWith('http')) return cleanPath
      return API_URLS.getSectionIcon() + cleanPath
    }

    const switchImage = (index) => {
      currentImageIndex.value = index
    }

    const prevImage = () => {
      if (game.value?.gameImageList?.length > 0) {
        currentImageIndex.value = currentImageIndex.value === 0
            ? game.value.gameImageList.length - 1
            : currentImageIndex.value - 1
      }
    }

    const nextImage = () => {
      if (game.value?.gameImageList?.length > 0) {
        currentImageIndex.value = currentImageIndex.value === game.value.gameImageList.length - 1
            ? 0
            : currentImageIndex.value + 1
      }
    }

    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    }

    onMounted(() => {
      fetchGameDetail()
    })

    return {
      game,
      loading,
      error,
      currentImageIndex,
      switchImage,
      prevImage,
      nextImage,
      formatDate,
      fetchGameDetail,  // ⚠️ 添加这个，否则 retry 按钮无法调用
      getImageUrl,
      getIconUrl
    }
  }
})

</script>

<template>
  <div class="game">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error">
      <div class="error-icon">⚠️</div>
      <p>{{ error }}</p>
      <button @click="fetchGameDetail" class="retry-btn">重试</button>
    </div>

    <!-- 游戏详情内容 -->
    <div v-else-if="game" class="game-content">
      <!-- 游戏头部信息 -->
      <div class="game-header">
        <div class="game-icon">
          <img :src="getIconUrl(game.gameIcon)" :alt="game.gameName"/>
        </div>
        <div class="game-info">
          <h1 class="game-title">{{ game.gameName }}</h1>
          <div class="game-meta">
            <span class="game-type">{{ game.gameTypeName }}</span>
            <span class="game-date">发布时间：{{ formatDate(game.createTime) }}</span>
          </div>
          <p class="game-description">{{ game.gameDescription }}</p>
          <div v-if="game.remark" class="game-remark">
            <span class="remark-label">备注：</span>
            {{ game.remark }}
          </div>
        </div>
      </div>

      <!-- 游戏截图画廊 -->
      <div v-if="game.gameImageList && game.gameImageList.length > 0" class="game-gallery">
        <h2>游戏相关图片</h2>

        <!-- 主图展示 -->
        <div class="main-image-container">
          <button @click="prevImage" class="nav-btn prev-btn">‹</button>
          <div class="main-image">
            <img
                :src="getImageUrl(game.gameImageList[currentImageIndex])"
                :alt="`${game.gameName} 截图 ${currentImageIndex + 1}`"
            />
          </div>
          <button @click="nextImage" class="nav-btn next-btn">›</button>
        </div>

        <!-- 缩略图列表 -->
        <div class="thumbnail-list">
          <div
              v-for="(image, index) in game.gameImageList"
              :key="index"
              class="thumbnail"
              :class="{ active: index === currentImageIndex }"
              @click="switchImage(index)"
          >
            <img :src="getImageUrl(image)" :alt="`缩略图 ${index + 1}`"/>
          </div>
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

<style scoped>
.game-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
}

/* 加载状态 */
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 错误状态 */
.error {
  text-align: center;
  padding: 40px;
  color: #dc3545;
}

.error-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.retry-btn {
  background: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 16px;
}

.retry-btn:hover {
  background: #0056b3;
}

/* 游戏头部 */
.game-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.game-icon {
  flex-shrink: 0;
}

.game-icon img {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.game-info {
  flex: 1;
}

.game-title {
  font-size: 32px;
  font-weight: bold;
  color: #2c3e50;
  margin: 0 0 16px 0;
}

.game-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.game-type {
  background: #007bff;
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
}

.game-date {
  color: #6c757d;
  font-size: 14px;
}

.game-description {
  font-size: 16px;
  line-height: 1.6;
  color: #495057;
  margin-bottom: 16px;
}

.game-remark {
  background: #f8f9fa;
  padding: 12px 16px;
  border-radius: 8px;
  border-left: 4px solid #007bff;
}

.remark-label {
  font-weight: bold;
  color: #007bff;
}

/* 游戏画廊 */
.game-gallery {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.game-gallery h2 {
  font-size: 24px;
  color: #2c3e50;
  margin: 0 0 24px 0;
}

/* 主图容器 */
.main-image-container {
  position: relative;
  margin-bottom: 20px;
}

.main-image {
  text-align: center;
}

.main-image img {
  max-width: 100%;
  max-height: 500px;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

/* 导航按钮 */
.nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  font-size: 24px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  z-index: 2;
}

.nav-btn:hover {
  background: rgba(0, 0, 0, 0.8);
  transform: translateY(-50%) scale(1.1);
}

.prev-btn {
  left: 20px;
}

.next-btn {
  right: 20px;
}

/* 缩略图列表 */
.thumbnail-list {
  display: flex;
  gap: 12px;
  justify-content: center;
  flex-wrap: wrap;
}

.thumbnail {
  width: 80px;
  height: 60px;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  border: 3px solid transparent;
  transition: all 0.3s ease;
}

.thumbnail:hover {
  border-color: #007bff;
  transform: scale(1.05);
}

.thumbnail.active {
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .game-detail {
    padding: 16px;
  }

  .game-header {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }

  .game-icon img {
    width: 100px;
    height: 100px;
  }

  .game-title {
    font-size: 24px;
  }

  .game-meta {
    justify-content: center;
  }

  .nav-btn {
    width: 40px;
    height: 40px;
    font-size: 18px;
  }

  .prev-btn {
    left: 10px;
  }

  .next-btn {
    right: 10px;
  }

  .thumbnail {
    width: 60px;
    height: 45px;
  }
}
</style>