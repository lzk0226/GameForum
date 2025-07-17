<template>
  <div class="post-list">
    <div class="header">
      <h2>最近热贴</h2>
      <div class="search-bar">
        <input
            v-model="searchKeyword"
            @keyup.enter="searchPosts"
            placeholder="搜索帖子标题..."
            class="search-input"
        />
        <button @click="searchPosts" class="search-btn">搜索</button>
      </div>
    </div>

    <!-- 加载中状态 -->
    <div v-if="loading && !posts.length" class="loading">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 帖子列表 -->
    <div class="posts" v-else>
      <div v-for="post in posts" :key="post.postId" class="post-item">
        <!-- 标签 -->
        <div v-if="post.topFlag === '1'" class="badge top-badge">置顶</div>
        <div v-if="post.hotFlag === '1'" class="badge hot-badge">热门</div>

        <!-- 帖子图片 -->
        <div v-if="post.photo" class="post-image">
          <img :src="getImageUrl(post.photo)" :alt="post.postTitle"/>
        </div>

        <!-- 帖子内容 -->
        <div class="post-content">
          <h3 class="post-title" @click="viewPost(post)">{{ post.postTitle }}</h3>
          <div class="post-excerpt" v-html="getExcerpt(post.postContent)"></div>

          <div class="post-meta">
            <div class="author-info">
              <img
                  v-if="post.avatar"
                  :src="getImageUrl(post.avatar)"
                  :alt="post.nickName"
                  class="avatar"
              />
              <div v-else class="avatar-placeholder">{{ post.nickName.charAt(0) }}</div>
              <span class="author-name">{{ post.nickName }}</span>
              <span class="section-name">{{ post.sectionName }}</span>
            </div>
            <div class="post-time">{{ formatTime(post.createTime) }}</div>
          </div>

          <div class="post-stats">
            <span class="stat-item">
              <i class="icon-like"></i>{{ post.likeCount }} 点赞
            </span>
            <span class="stat-item">
              <i class="icon-comment"></i>{{ post.commentCount }} 评论
            </span>
            <span class="stat-item">
              <i class="icon-view"></i>{{ post.viewCount }} 浏览
            </span>
          </div>

          <div class="post-actions">
            <button
                @click="toggleLike(post)"
                class="action-btn like-btn"
                :class="{ liked: post.hasLiked, loading: post.likeLoading }"
                :disabled="post.likeLoading"
            >
              <i class="icon-like"></i>
              {{ post.likeLoading ? (post.hasLiked ? '取消中...' : '点赞中...') : (post.hasLiked ? '已点赞' : '点赞') }}
            </button>
            <button @click="viewPost(post)" class="action-btn view-btn">查看详情</button>
          </div>
        </div>
      </div>

      <!-- 底部状态 -->
      <div v-if="loading && posts.length" class="load-more">
        <div class="loading-spinner"></div>
        <p>加载更多中...</p>
      </div>

      <div v-else-if="!hasMore && posts.length" class="no-more">没有更多帖子了</div>
      <div v-else-if="!posts.length" class="no-data"><p>暂无帖子数据</p></div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { API_URLS } from '@/api/apiUrls'

export default {
  name: 'PostList',
  props: {
    sectionId: {type: Number, default: null},
    userId: {type: Number, default: null},
    showMyPosts: {type: Boolean, default: false},
    showHotPosts: {type: Boolean, default: false}
  },
  data: () => ({
    posts: [],
    loading: false,
    hasMore: true,
    pageSize: 5,
    currentPage: 1,
    searchKeyword: '',
    isSearchMode: false
  }),
  mounted() {
    this.loadPosts()
    this.setupInfiniteScroll()
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    async loadPosts(reset = false) {
      if (this.loading) return
      this.loading = true

      try {
        const {url, params} = this.getApiConfig()
        //console.log('正在请求:', url, params)

        const response = await axios.get(url, {
          params,
          headers: this.getAuthHeaders()
        })

        if (response.data.success || response.data.code === 200) {
          const newPosts = response.data.data || []
          const postsWithLikeStatus = await this.initializePostsLikeStatus(newPosts)

          if (reset) {
            this.posts = postsWithLikeStatus
            this.currentPage = 1
          } else {
            const startIndex = (this.currentPage - 1) * this.pageSize
            const pagedPosts = postsWithLikeStatus.slice(startIndex, startIndex + this.pageSize)
            this.posts.push(...pagedPosts)
            this.currentPage++
          }

          this.hasMore = newPosts.length >= this.pageSize && this.posts.length < newPosts.length
        } else {
          console.error('API返回错误:', response.data)
          this.$message?.error(response.data.message || '获取数据失败')
        }
      } catch (error) {
        console.error('加载帖子失败:', error)
        this.$message?.error('加载帖子失败，请检查网络连接')
      } finally {
        this.loading = false
      }
    },

    getApiConfig() {
      let url = API_URLS.getPostList()
      let params = {}

      if (this.showMyPosts) {
        url = API_URLS.getMyPosts()
      } else if (this.showHotPosts) {
        url = API_URLS.getHotPosts()
        params.limit = this.pageSize
      } else if (this.sectionId) {
        url = API_URLS.getPostsBySection(this.sectionId)
      } else if (this.userId) {
        url = API_URLS.getPostsByUser(this.userId)
      } else if (this.isSearchMode && this.searchKeyword) {
        url = API_URLS.searchPosts()
        params.title = this.searchKeyword
      }

      return {url, params}
    },

    async initializePostsLikeStatus(posts) {
      const token = this.getToken()
      if (!token) {
        return posts.map(post => ({...post, hasLiked: false, likeLoading: false}))
      }

      return Promise.all(
          posts.map(async (post) => {
            try {
              const hasLiked = await this.checkLikeStatus(post.postId)
              return {...post, hasLiked, likeLoading: false}
            } catch (error) {
              console.error(`检查帖子 ${post.postId} 点赞状态失败:`, error)
              return {...post, hasLiked: false, likeLoading: false}
            }
          })
      )
    },

    async checkLikeStatus(postId) {
      try {
        const response = await axios.get(
            API_URLS.checkPostLikeStatus(postId),
            {headers: this.getAuthHeaders()}
        )
        return (response.data.success || response.data.code === 200) ? response.data.data || false : false
      } catch (error) {
        console.error('检查点赞状态失败:', error)
        return false
      }
    },

    async toggleLike(post) {
      const token = this.getToken()
      if (!token) return this.$message?.error('请先登录')
      if (post.likeLoading) return

      post.likeLoading = true

      try {
        let response

        if (post.hasLiked) {
          // 取消点赞
          response = await axios.delete(
              API_URLS.deletePostLike(post.postId),
              {headers: this.getAuthHeaders()}
          )
        } else {
          // 点赞
          response = await axios.post(
              API_URLS.createPostLike(post.postId),
              {},
              {headers: this.getAuthHeaders()}
          )
        }

        if (response.data.success || response.data.code === 200) {
          // 更新点赞状态和数量
          if (post.hasLiked) {
            // 取消点赞成功
            post.hasLiked = false
            post.likeCount = Math.max(0, (post.likeCount || 0) - 1)
            this.$message?.success('取消点赞成功')
          } else {
            // 点赞成功
            post.hasLiked = true
            post.likeCount = (post.likeCount || 0) + 1
            this.$message?.success('点赞成功')
          }
        } else {
          console.error('操作失败:', response.data)
          this.$message?.error(response.data.message || '操作失败')
        }
      } catch (error) {
        console.error('点赞/取消点赞请求失败:', error)
        const message = error.response?.status === 401 ? '请先登录' :
            error.response?.data?.message || '操作失败，请重试'
        this.$message?.error(message)
      } finally {
        post.likeLoading = false
      }
    },

    setupInfiniteScroll() {
      window.addEventListener('scroll', this.handleScroll)
    },

    handleScroll() {
      const {pageYOffset: scrollTop} = window
      const {innerHeight: windowHeight} = window
      const {scrollHeight: documentHeight} = document.documentElement

      if (scrollTop + windowHeight >= documentHeight - 50) {
        if (this.hasMore && !this.loading && !this.isSearchMode) {
          this.loadPosts()
        }
      }
    },

    async searchPosts() {
      this.isSearchMode = !!this.searchKeyword.trim()
      this.posts = []
      this.hasMore = !this.isSearchMode
      await this.loadPosts(true)
    },

    viewPost(post) {
      this.incrementViewCount(post.postId)
      window.open(`/postDetail/${post.postId}`, '_blank')
    },

    async incrementViewCount(postId) {
      try {
        await axios.get(API_URLS.incrementViewCount(postId))
      } catch (error) {
        console.error('更新浏览量失败:', error)
      }
    },

    /*getImageUrl(path) {
      if (!path) return ''
      const cleanPath = path.replace(/\\/g, '/')

      if (cleanPath.startsWith('http')) return cleanPath
      if (cleanPath.startsWith('/')) return API_URLS.getPostPhotos()+cleanPath
      return `/${cleanPath}`
    },*/

    getImageUrl(path) {
      if (!path) return ''
      const cleanPath = path.replace(/\\/g, '/')
      // 如果已经是完整的 http 链接，直接返回
      if (cleanPath.startsWith('http')) return cleanPath
      // 拼接成固定的图片读取地址
      return API_URLS.getPostPhotos()+cleanPath
    },


    getExcerpt(content) {
      if (!content) return ''
      const textContent = content.replace(/<[^>]*>/g, '')
      return textContent.length > 100 ? `${textContent.substring(0, 100)}...` : textContent
    },

    formatTime(timeStr) {
      if (!timeStr) return ''
      const diff = new Date() - new Date(timeStr)

      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
      if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
      if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`
      return new Date(timeStr).toLocaleDateString()
    },

    getToken() {
      return ['accessToken', 'token', 'Admin-Token', 'access_token']
          .map(key => localStorage.getItem(key))
          .find(Boolean)
    },

    getAuthHeaders() {
      const token = this.getToken()
      const headers = {'Content-Type': 'application/json'}

      if (token) {
        headers.Authorization = `Bearer ${token}`
      } else {
        console.warn('未找到登录记录')
      }

      return headers
    }
  }
}
</script>

<style scoped>
.post-list {
  width: 100%;
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #e9ecef;
}

.header h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.search-bar {
  display: flex;
  gap: 10px;
}

.search-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 20px;
  outline: none;
  width: 200px;
  transition: border-color 0.3s;
}

.search-input:focus {
  border-color: #007bff;
}

.search-btn {
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background-color: #0056b3;
}

.posts {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.post-item {
  position: relative;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.post-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.badge {
  position: absolute;
  top: 15px;
  right: 15px;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
  z-index: 1;
  color: white;
}

.top-badge {
  background-color: #ff6b6b;
}

.hot-badge {
  background-color: #ffa500;
}

.post-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.post-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.post-image:hover img {
  transform: scale(1.05);
}

.post-content {
  padding: 20px;
}

.post-title {
  margin: 0 0 15px;
  font-size: 20px;
  font-weight: bold;
  color: #333;
  cursor: pointer;
  transition: color 0.3s;
  line-height: 1.4;
}

.post-title:hover {
  color: #007bff;
}

.post-excerpt {
  margin-bottom: 15px;
  color: #666;
  line-height: 1.6;
  font-size: 14px;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #007bff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 14px;
}

.author-name {
  font-weight: 500;
  color: #333;
}

.section-name {
  padding: 2px 8px;
  background-color: #e9ecef;
  border-radius: 10px;
  font-size: 12px;
  color: #666;
}

.post-time {
  color: #999;
  font-size: 12px;
}

.post-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #666;
  font-size: 14px;
}

.post-actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: white;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.like-btn:hover:not(:disabled):not(.liked) {
  background-color: #ff6b6b;
  color: white;
  border-color: #ff6b6b;
}

.like-btn.liked {
  background-color: #ff6b6b;
  color: white;
  border-color: #ff6b6b;
}

.like-btn.liked:hover:not(:disabled) {
  background-color: #e55a5a;
  border-color: #e55a5a;
}

.like-btn.loading {
  opacity: 0.7;
}

.view-btn:hover {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.loading, .load-more {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 10px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.no-more, .no-data {
  text-align: center;
  padding: 40px;
  color: #999;
  background: white;
  border-radius: 12px;
}

/* 图标样式 */
.icon-like::before {
  content: '👍';
}

.icon-comment::before {
  content: '💬';
}

.icon-view::before {
  content: '👁';
}

/* 响应式设计 */
@media (max-width: 768px) {
  .post-list {
    padding: 10px;
  }

  .header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }

  .search-bar {
    justify-content: center;
  }

  .search-input {
    width: 100%;
    max-width: 300px;
  }

  .post-content {
    padding: 15px;
  }

  .post-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .post-stats {
    flex-wrap: wrap;
    gap: 15px;
  }

  .post-actions {
    flex-wrap: wrap;
  }
}
</style>