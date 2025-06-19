<template>
  <div class="section-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-container">
      <div class="error-content">
        <h3>加载失败</h3>
        <p>{{ error }}</p>
        <button @click="loadSectionData" class="retry-btn">重试</button>
      </div>
    </div>

    <!-- 主内容 -->
    <div v-else class="main-content">
      <!-- 板块信息区域 -->
      <div v-if="sectionInfo" class="section-header">
        <div class="section-info">
          <div class="section-icon">
            <img v-if="gameIcon" :src="gameIcon" :alt="sectionInfo.sectionName"/>
            <div v-else class="default-icon">{{ sectionInfo.sectionName?.charAt(0) || 'S' }}</div>
          </div>
          <div class="section-details">
            <h1 class="section-name">{{ sectionInfo.sectionName }}</h1>
            <!-- 使用 sectionDescription 字段 -->
            <p v-if="sectionInfo.sectionDescription" class="section-description">
              {{ sectionInfo.sectionDescription }}
            </p>
            <div class="section-meta">
              <!--              <span class="meta-item">
                              <i class="icon-posts"></i>
                              帖子数: {{ sectionInfo.postCount || 0 }}
                            </span>-->
              <span class="meta-item" v-if="sectionInfo.createTime">
                <i class="icon-time"></i>
                创建时间: {{ formatDate(sectionInfo.createTime) }}
              </span>
              <!-- 显示关联游戏信息 -->
              <span class="meta-item" v-if="sectionInfo.gameName">
                <i class="icon-game"></i>
                游戏: {{ sectionInfo.gameName }}
              </span>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="section-actions">
          <button @click="showCreatePost" class="create-post-btn">
            <i class="icon-plus"></i>
            发帖
          </button>
        </div>
      </div>

      <!-- 帖子列表区域 -->
      <div class="posts-section">
        <div class="posts-header">
          <h2>板块帖子</h2>
          <div class="sort-options">
            <select v-model="sortType" @change="loadPosts">
              <option value="latest">最新发布</option>
              <option value="hot">热门</option>
              <option value="top">置顶</option>
            </select>
          </div>
        </div>

        <!-- 帖子加载状态 -->
        <div v-if="postsLoading" class="posts-loading">
          <div class="loading-spinner small"></div>
          <p>加载帖子中...</p>
        </div>

        <!-- 空状态 -->
        <div v-else-if="posts.length === 0" class="empty-posts">
          <div class="empty-content">
            <div class="empty-icon">📝</div>
            <h3>暂无帖子</h3>
            <p>这个板块还没有帖子，来发第一个帖子吧！</p>
            <button @click="showCreatePost" class="create-first-post-btn">发布帖子</button>
          </div>
        </div>

        <!-- 帖子列表 -->
        <div v-else class="posts-list" ref="postsListRef">
          <div
              v-for="post in posts"
              :key="post.postId"
              class="post-item"
              :class="{ 'pinned': post.isTop === '1' }"
              @click="goToPost(post.postId)"
          >
            <!-- 置顶标识 -->
            <div v-if="post.isTop === '1'" class="pin-badge">置顶</div>

            <div class="post-content">
              <div class="post-header">
                <h3 class="post-title">{{ post.postTitle }}</h3>
                <div class="post-meta">
                  <span class="author">{{ post.createBy || '匿名用户' }}</span>
                  <span class="time">{{ formatDate(post.createTime) }}</span>
                </div>
              </div>

              <div class="post-preview">
                <p class="post-excerpt">{{ getPostExcerpt(post.postContent) }}</p>
                <img v-if="post.photo" :src="getImageUrl(post.photo)" :alt="post.postTitle" class="post-image"/>
              </div>

              <div class="post-stats">
                <span class="stat-item">
                  <i class="icon-view"></i>
                  {{ post.viewCount || 0 }}
                </span>
                <span class="stat-item">
                  <i class="icon-like"></i>
                  {{ post.likeCount || 0 }}
                </span>
                <span class="stat-item">
                  <i class="icon-comment"></i>
                  {{ post.commentCount || 0 }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 懒加载提示 -->
        <div v-if="hasMore && posts.length > 0" class="load-more" ref="loadMoreTrigger">
          <div v-if="loadingMore" class="loading-more">
            <div class="loading-spinner small"></div>
            <span>加载更多帖子中...</span>
          </div>
          <div v-else class="load-more-hint">
            <span>下滑加载更多</span>
          </div>
        </div>

        <!-- 已加载完成提示 -->
        <div v-if="!hasMore && posts.length > 0" class="no-more">
          <span>已加载全部帖子</span>
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
import {defineComponent, nextTick, onMounted, onUnmounted, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import axios from 'axios'
import {applyTheme, createScrollListener, scrollToTop, toggleTheme} from '@/utils/backToTopUtils.js'
import BackToTopToggle from "@/components/user/index/BackToTopToggle.vue";

export default defineComponent({
  name: "Section",
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
    const route = useRoute()
    const router = useRouter()

    // 响应式数据
    const loading = ref(false)
    const error = ref('')
    const sectionInfo = ref(null)
    const gameIcon = ref('') // 添加游戏图标状态
    const posts = ref([])
    const postsLoading = ref(false)
    const sortType = ref('latest')
    const hasMore = ref(true)
    const loadingMore = ref(false)
    const currentPage = ref(1)
    const pageSize = ref(5) // 每次加载5条

    // DOM引用
    const postsListRef = ref(null)
    const loadMoreTrigger = ref(null)

    // 获取板块ID
    const sectionId = ref(route.query.sectionId)

    // API基础URL
    const API_BASE = ''

    // 懒加载观察器
    let intersectionObserver = null

    // 加载游戏图标
    const loadGameIcon = async (gameId) => {
      if (!gameId) return

      try {
        const response = await axios.get(`/user/game/${gameId}`)
        if (response.data.code === 200 && response.data.data) {
          const gameData = response.data.data
          if (gameData.gameIcon) {
            // 确保图标路径正确
            gameIcon.value = gameData.gameIcon.startsWith('/') ? gameData.gameIcon : `/${gameData.gameIcon}`
          }
        }
      } catch (err) {
        console.error('加载游戏图标失败:', err)
        // 图标加载失败不影响主要功能，只是显示默认图标
      }
    }

    // 初始化懒加载观察器
    const initIntersectionObserver = () => {
      if (!loadMoreTrigger.value) return

      intersectionObserver = new IntersectionObserver(
          (entries) => {
            entries.forEach(entry => {
              if (entry.isIntersecting && hasMore.value && !loadingMore.value) {
                loadMorePosts()
              }
            })
          },
          {
            root: null,
            rootMargin: '100px', // 提前100px开始加载
            threshold: 0.1
          }
      )

      intersectionObserver.observe(loadMoreTrigger.value)
    }

    // 销毁观察器
    const destroyIntersectionObserver = () => {
      if (intersectionObserver) {
        intersectionObserver.disconnect()
        intersectionObserver = null
      }
    }

    // 加载板块信息
    const loadSectionData = async () => {
      if (!sectionId.value) {
        error.value = '缺少板块ID参数'
        return
      }

      loading.value = true
      error.value = ''

      try {
        const response = await axios.get(`/user/section/${sectionId.value}`)
        if (response.data.code === 200) {
          sectionInfo.value = response.data.data

          // 如果板块信息包含游戏ID，则加载游戏图标
          if (sectionInfo.value.gameId) {
            await loadGameIcon(sectionInfo.value.gameId)
          }

          // 加载帖子
          await loadPosts()
          // 初始化懒加载
          await nextTick()
          initIntersectionObserver()
        } else {
          error.value = response.data.msg || '加载板块信息失败'
        }
      } catch (err) {
        console.error('加载板块信息失败:', err)
        error.value = '网络错误，请稍后重试'
      } finally {
        loading.value = false
      }
    }

    // 加载帖子列表
    const loadPosts = async (reset = true) => {
      if (!sectionId.value) return

      if (reset) {
        postsLoading.value = true
        currentPage.value = 1
        posts.value = []
        hasMore.value = true
      } else {
        loadingMore.value = true
      }

      try {
        let url = `/user/post/section/${sectionId.value}`
        const params = {
          page: reset ? 1 : currentPage.value,
          size: pageSize.value,
          sort: sortType.value
        }

        // 根据排序类型选择不同的接口
        if (sortType.value === 'hot') {
          url = `/user/post/hot`
          params.limit = pageSize.value
        } else if (sortType.value === 'top') {
          url = `/user/post/top`
          params.sectionId = sectionId.value
        }

        const response = await axios.get(url, {params})
        if (response.data.code === 200) {
          const newPosts = response.data.data || []

          if (reset) {
            posts.value = newPosts
          } else {
            posts.value = [...posts.value, ...newPosts]
          }

          // 判断是否还有更多数据
          hasMore.value = newPosts.length === pageSize.value

          if (!reset) {
            currentPage.value++
          }
        } else {
          console.error('加载帖子失败:', response.data.msg)
        }
      } catch (err) {
        console.error('加载帖子失败:', err)
      } finally {
        postsLoading.value = false
        loadingMore.value = false
      }
    }

    // 加载更多帖子
    const loadMorePosts = () => {
      if (!loadingMore.value && hasMore.value) {
        loadPosts(false)
      }
    }

    // 跳转到帖子详情
    const goToPost = (postId) => {
      router.push(`/postDetail/${postId}`)
    }

    // 显示发帖页面
    const showCreatePost = () => {
      router.push(`/newpost?sectionId=${sectionId.value}`)
    }

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      const now = new Date()
      const diff = now - date

      if (diff < 60000) {
        return '刚刚'
      } else if (diff < 3600000) {
        return `${Math.floor(diff / 60000)}分钟前`
      } else if (diff < 86400000) {
        return `${Math.floor(diff / 3600000)}小时前`
      } else if (diff < 604800000) {
        return `${Math.floor(diff / 86400000)}天前`
      } else {
        return date.toLocaleDateString('zh-CN')
      }
    }

    // 获取帖子摘要
    const getPostExcerpt = (content) => {
      if (!content) return ''
      const plainText = content.replace(/<[^>]*>/g, '')
      return plainText.length > 100 ? plainText.substring(0, 100) + '...' : plainText
    }

    // 获取图片URL
    const getImageUrl = (photo) => {
      if (!photo) return ''
      if (photo.startsWith('http')) return photo
      return `${API_BASE}/${photo}`
    }

    // 监听路由参数变化
    watch(() => route.query.sectionId, (newSectionId) => {
      if (newSectionId && newSectionId !== sectionId.value) {
        sectionId.value = newSectionId
        destroyIntersectionObserver()
        // 重置游戏图标
        gameIcon.value = ''
        loadSectionData()
      }
    })

    // 监听排序变化
    watch(sortType, () => {
      destroyIntersectionObserver()
      loadPosts(true).then(() => {
        nextTick(() => {
          initIntersectionObserver()
        })
      })
    })

    // 组件挂载时加载数据
    onMounted(() => {
      loadSectionData()
    })

    // 组件卸载时清理观察器
    onUnmounted(() => {
      destroyIntersectionObserver()
    })

    return {
      loading,
      error,
      sectionInfo,
      gameIcon, // 导出游戏图标状态
      posts,
      postsLoading,
      sortType,
      hasMore,
      loadingMore,
      postsListRef,
      loadMoreTrigger,
      loadSectionData,
      loadPosts,
      loadMorePosts,
      goToPost,
      showCreatePost,
      formatDate,
      getPostExcerpt,
      getImageUrl
    }
  }
})
</script>

<style scoped>
.section-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

/* 加载状态 */
.loading-container, .error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

.loading-spinner.small {
  width: 24px;
  height: 24px;
  border-width: 3px;
  margin-bottom: 8px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.error-content {
  text-align: center;
}

.retry-btn {
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 16px;
}

.retry-btn:hover {
  background-color: #0056b3;
}

/* 主内容 */
.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 板块头部 */
.section-header {
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.section-info {
  display: flex;
  gap: 16px;
  flex: 1;
}

.section-icon {
  flex-shrink: 0;
}

.section-icon img {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  object-fit: cover;
  border: 2px solid #e9ecef;
}

.default-icon {
  width: 64px;
  height: 64px;
  background: linear-gradient(45deg, #007bff, #0056b3);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  border-radius: 8px;
}

.section-details {
  flex: 1;
}

.section-name {
  margin: 0 0 8px 0;
  font-size: 28px;
  color: #333;
  font-weight: 600;
}

.section-description {
  margin: 0 0 16px 0;
  color: #666;
  font-size: 16px;
  line-height: 1.5;
  background: #f8f9fa;
  padding: 12px 16px;
  border-radius: 6px;
  border-left: 4px solid #007bff;
}

.section-meta {
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #888;
  font-size: 14px;
}

.section-actions {
  flex-shrink: 0;
}

.create-post-btn {
  padding: 12px 24px;
  background: linear-gradient(45deg, #007bff, #0056b3);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.create-post-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.3);
}

/* 帖子区域 */
.posts-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.posts-header {
  padding: 20px 24px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.posts-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.sort-options select {
  padding: 6px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.posts-loading {
  padding: 40px;
  text-align: center;
}

/* 空状态 */
.empty-posts {
  padding: 60px 24px;
  text-align: center;
}

.empty-content {
  max-width: 300px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-content h3 {
  margin: 0 0 8px 0;
  color: #666;
}

.empty-content p {
  margin: 0 0 24px 0;
  color: #888;
  font-size: 14px;
}

.create-first-post-btn {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* 帖子列表 */
.posts-list {
  padding: 0;
}

.post-item {
  position: relative;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.post-item:hover {
  background-color: #f8f9fa;
}

.post-item:last-child {
  border-bottom: none;
}

.post-item.pinned {
  background-color: #fff8e1;
}

.pin-badge {
  position: absolute;
  top: 16px;
  right: 24px;
  background: #ff9800;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.post-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.post-title {
  margin: 0;
  font-size: 18px;
  color: #333;
  font-weight: 500;
  line-height: 1.4;
  flex: 1;
}

.post-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
  color: #888;
  text-align: right;
  flex-shrink: 0;
}

.post-preview {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.post-excerpt {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  flex: 1;
}

.post-image {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  flex-shrink: 0;
}

.post-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #888;
  font-size: 13px;
}

/* 懒加载相关样式 */
.load-more {
  padding: 20px;
  text-align: center;
  border-top: 1px solid #f0f0f0;
}

.loading-more {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #666;
}

.load-more-hint {
  color: #999;
  font-size: 14px;
}

.no-more {
  padding: 20px;
  text-align: center;
  border-top: 1px solid #f0f0f0;
  color: #999;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .section-container {
    padding: 16px;
  }

  .section-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }

  .section-actions {
    align-self: flex-end;
  }

  .post-header {
    flex-direction: column;
    gap: 8px;
  }

  .post-meta {
    text-align: left;
  }

  .post-preview {
    flex-direction: column;
    gap: 12px;
  }

  .post-image {
    width: 100%;
    height: 120px;
  }

  .section-meta {
    flex-direction: column;
    gap: 8px;
  }
}

/* 图标样式（可以替换为实际的图标字体） */
.icon-posts::before {
  content: "📄";
}

.icon-time::before {
  content: "🕒";
}

.icon-game::before {
  content: "🎮";
}

.icon-plus::before {
  content: "+";
}

.icon-view::before {
  content: "👁";
}

.icon-like::before {
  content: "❤";
}

.icon-comment::before {
  content: "💬";
}
</style>