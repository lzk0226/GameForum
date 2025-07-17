<template>
  <div class="post-detail">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 帖子内容 -->
    <div v-else-if="post" class="post-container">

      <!-- 帖子主体 -->
      <article class="post-main">
        <!-- 标签 -->
        <div class="badges">
          <span v-if="post.topFlag === '1'" class="badge top">置顶</span>
          <span v-if="post.hotFlag === '1'" class="badge hot">热门</span>
        </div>

        <!-- 标题 -->
        <h1>{{ post.postTitle }}</h1>

        <!-- 作者信息 -->
        <div class="meta">
          <div class="author">
            <div class="avatar" :style="post.avatar ? `background-image: url(${getImageUrl(post.avatar)})` : ''">
              {{ !post.avatar ? post.nickName.charAt(0) : '' }}
            </div>
            <div>
              <div class="name">{{ post.nickName }}</div>
              <div class="section">{{ post.sectionName }}</div>
            </div>
          </div>
          <div class="time">{{ formatTime(post.createTime) }}</div>
        </div>

        <!-- 图片 -->
        <div v-if="post.photo" class="image" @click="previewImage">
          <img :src="getImageUrl(post.photo)" :alt="post.postTitle"/>
        </div>

        <!-- 内容 -->
        <div class="content" v-html="post.postContent"></div>

        <!-- 统计 -->
        <div class="stats">
          <span><i class="icon-like"></i>{{ post.likeCount || 0 }}</span>
          <span><i class="icon-comment"></i>{{ post.commentCount || 0 }}</span>
          <span><i class="icon-view"></i>{{ post.viewCount || 0 }}</span>
        </div>

        <!-- 操作按钮 -->
        <div class="actions">
          <button @click="toggleLike" class="btn" :class="{ liked: hasLiked }" :disabled="likeLoading">
            <i class="icon-like"></i>{{
              likeLoading ? (hasLiked ? '取消中...' : '点赞中...') : (hasLiked ? '已点赞' : '点赞')
            }}
          </button>
          <button @click="scrollToComments" class="btn">
            <i class="icon-comment"></i>评论
          </button>
        </div>
      </article>

      <!-- 评论区 -->
      <section class="comments" ref="commentsSection">
        <h3>评论 ({{ comments.length }})</h3>

        <!-- 发表评论 -->
        <div v-if="isLoggedIn" class="comment-form">
          <textarea v-model="newComment" placeholder="写下你的评论..." maxlength="5000"></textarea>
          <div class="form-actions">
            <span>{{ newComment.length }}/5000</span>
            <button @click="submitComment" :disabled="!newComment.trim() || submittingComment">
              {{ submittingComment ? '发布中...' : '发布评论' }}
            </button>
          </div>
        </div>

        <div v-else class="login-prompt">请登录后发表评论</div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <div v-if="loadingComments" class="loading">
            <div class="spinner"></div>
            <p>加载评论中...</p>
          </div>

          <div v-else-if="comments.length === 0" class="no-comments">
            暂无评论，快来发表第一条评论吧~
          </div>

          <div v-else>
            <div v-for="comment in comments" :key="comment.commentId" class="comment">
              <!-- 主评论 -->
              <div class="comment-main">
                <div class="avatar"
                     :style="comment.userAvatar ? `background-image: url(${getImageUrl(comment.userAvatar)})` : ''">
                  {{ !comment.userAvatar ? comment.nickName.charAt(0) : '' }}
                </div>
                <div class="comment-body">
                  <div class="comment-header">
                    <span class="name">{{ comment.nickName }}</span>
                    <span class="time">{{ formatTime(comment.createTime) }}</span>
                  </div>
                  <div class="comment-content" v-html="comment.commentContent"></div>
                  <div class="comment-actions">
                    <button @click="toggleCommentLike(comment)" :class="{ liked: comment.hasLiked }"
                            :disabled="comment.likeLoading">
                      <i class="icon-like"></i>点赞{{ comment.likeCount || 0 }}
                    </button>
                    <button @click="replyToComment(comment)">
                      <i class="icon-reply"></i>回复
                    </button>
                    <!-- 删除按钮 - 只有自己的评论才显示 -->
                    <button v-if="canDeleteComment(comment)" @click="deleteComment(comment)"
                            :disabled="comment.deleting" class="delete-btn">
                      <i class="icon-delete"></i>{{ comment.deleting ? '删除中...' : '删除' }}
                    </button>
                  </div>

                  <!-- 回复框 -->
                  <div v-if="replyingTo === comment.commentId && isLoggedIn" class="reply-form">
                    <textarea v-model="replyContent" :placeholder="`回复 @${comment.nickName}:`"
                              maxlength="5000"></textarea>
                    <div class="form-actions">
                      <span>{{ replyContent.length }}/5000</span>
                      <button @click="cancelReply">取消</button>
                      <button @click="submitReply(comment.commentId)"
                              :disabled="!replyContent.trim() || submittingReply">
                        {{ submittingReply ? '回复中...' : '回复' }}
                      </button>
                    </div>
                  </div>

                  <!-- 子评论 -->
                  <div v-if="comment.children?.length" class="replies">
                    <div v-for="reply in comment.children" :key="reply.commentId" class="reply">
                      <div class="avatar"
                           :style="reply.userAvatar ? `background-image: url(${getImageUrl(reply.userAvatar)})` : ''">
                        {{ !reply.userAvatar ? reply.nickName.charAt(0) : '' }}
                      </div>
                      <div class="reply-body">
                        <div class="reply-header">
                          <span class="name">{{ reply.nickName }}</span>
                          <span class="time">{{ formatTime(reply.createTime) }}</span>
                        </div>
                        <div class="reply-content" v-html="reply.commentContent"></div>
                        <div class="reply-actions">
                          <button @click="toggleCommentLike(reply)" :class="{ liked: reply.hasLiked }"
                                  :disabled="reply.likeLoading">
                            <i class="icon-like"></i>点赞{{ reply.likeCount || 0 }}
                          </button>
                          <!-- 删除按钮 - 只有自己的回复才显示 -->
                          <button v-if="canDeleteComment(reply)" @click="deleteComment(reply)"
                                  :disabled="reply.deleting" class="delete-btn">
                            <i class="icon-delete"></i>{{ reply.deleting ? '删除中...' : '删除' }}
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>

    <!-- 错误状态 -->
    <div v-else class="error">
      <p>帖子不存在或已被删除</p>
      <button @click="goBack" class="btn">返回</button>
    </div>

    <!-- 图片预览 -->
    <div v-if="showImagePreview" class="preview-overlay" @click="closeImagePreview">
      <div class="preview-container">
        <img :src="previewImageUrl" alt="预览图片"/>
        <button @click="closeImagePreview" class="close-btn">×</button>
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
import axios from 'axios'
import BackToTopToggle from "@/components/user/index/BackToTopToggle.vue"
import {applyTheme, createScrollListener, scrollToTop, toggleTheme} from '@/utils/backToTopUtils.js'
import API_URLS from '@/api/apiUrls.js' // 引入 API_URLS

export default {
  name: 'PostDetail',
  components: {BackToTopToggle},
  data() {
    return {
      post: null,
      comments: [],
      loading: true,
      loadingComments: false,
      hasLiked: false,
      likeLoading: false,
      newComment: '',
      submittingComment: false,
      replyingTo: null,
      replyContent: '',
      submittingReply: false,
      showImagePreview: false,
      previewImageUrl: '',
      currentUserId: null,
      gameId: null, // 添加 gameId 字段
      isAtTop: true,
      theme: 'light',
      cleanupScrollListener: null
    }
  },
  computed: {
    postId() {
      return this.$route.params.id || new URLSearchParams(window.location.search).get('id')
    },
    isLoggedIn() {
      return !!this.getToken()
    }
  },
  async mounted() {
    await this.getCurrentUser()
    await this.loadPostDetail()
    await this.loadComments()
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
    },
    // 获取当前用户信息
    async getCurrentUser() {
      if (!this.isLoggedIn) return
      try {
        // 从token中解析用户ID
        const token = this.getToken()
        if (!token) return

        // 解析JWT token获取用户ID (简单的base64解码)
        const payload = JSON.parse(atob(token.split('.')[1]))
        const userId = payload.userId || payload.sub || payload.id

        if (!userId) {
          console.error('无法从token中获取用户ID')
          return
        }

        const {data} = await axios.get(API_URLS.getUserProfile(userId), {
          headers: this.getAuthHeaders()
        })
        if (data.success || data.code === 200) {
          this.currentUserId = data.data.userId
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        // 如果token无效，清除本地存储的token
        if (error.response?.status === 401 || error.response?.status === 403) {
          localStorage.removeItem('accessToken')
          localStorage.removeItem('token')
          localStorage.removeItem('Admin-Token')
          localStorage.removeItem('access_token')
        }
      }
    },

    // 修改 loadPostDetail 方法
    async loadPostDetail() {
      if (!this.postId) {
        this.loading = false
        return
      }

      try {
        const {data} = await axios.get(API_URLS.getPostDetail(this.postId))
        if (data.success || data.code === 200) {
          this.post = data.data
          document.title = this.post.postTitle || '帖子详情'

          // 获取游戏ID
          await this.getGameIdBySectionId()

          if (this.isLoggedIn) await this.checkLikeStatus()
        } else {
          this.$message?.error(data.message || '帖子不存在')
        }
      } catch (error) {
        console.error('加载帖子详情失败:', error)
        this.$message?.error('加载失败，请检查网络连接')
      } finally {
        this.loading = false
      }
    },

// 新增获取游戏ID的方法
    async getGameIdBySectionId() {
      if (!this.post?.sectionId) return

      try {
        const {data} = await axios.get(API_URLS.getGameIdBySectionId(this.post.sectionId))
        if (data.success || data.code === 200) {
          this.gameId = data.data
          console.log('获取到游戏ID:', this.gameId)
        } else {
          console.warn('获取游戏ID失败:', data.message)
        }
      } catch (error) {
        console.error('获取游戏ID失败:', error)
      }
    },

    async loadComments() {
      if (!this.postId) return
      this.loadingComments = true
      try {
        const {data} = await axios.get(API_URLS.getPostComments(this.postId), {
          headers: this.getAuthHeaders()
        })
        if (data.success || data.code === 200) {
          this.comments = data.data || []
        }
      } catch (error) {
        console.error('加载评论失败:', error)
      } finally {
        this.loadingComments = false
      }
    },

    async checkLikeStatus() {
      if (!this.postId || !this.isLoggedIn) return
      try {
        const {data} = await axios.get(API_URLS.checkPostLikeStatus(this.postId), {
          headers: this.getAuthHeaders()
        })
        if (data.success || data.code === 200) {
          this.hasLiked = data.data || false
        }
      } catch (error) {
        console.error('检查点赞状态失败:', error)
      }
    },

    async toggleLike() {
      if (!this.isLoggedIn) return this.$message?.error('请先登录')
      if (this.likeLoading) return

      this.likeLoading = true
      try {
        const url = API_URLS.togglePostLike(this.postId)
        let response

        if (this.hasLiked) {
          // 取消点赞
          response = await axios.delete(url, {headers: this.getAuthHeaders()})
        } else {
          // 点赞
          response = await axios.post(url, {}, {headers: this.getAuthHeaders()})
        }

        const {data} = response
        if (data.success || data.code === 200) {
          this.hasLiked = !this.hasLiked
          this.post.likeCount = Math.max(0, (this.post.likeCount || 0) + (this.hasLiked ? 1 : -1))
          this.$message?.success(this.hasLiked ? '点赞成功' : '取消点赞成功')
        } else {
          this.$message?.error(data.message || '操作失败')
        }
      } catch (error) {
        console.error('点赞操作失败:', error)
        this.$message?.error('操作失败，请重试')
      } finally {
        this.likeLoading = false
      }
    },

    // 修改 submitComment 方法
    async submitComment() {
      if (!this.newComment.trim()) return
      this.submittingComment = true

      try {
        const requestData = {
          postId: parseInt(this.postId),
          commentContent: this.newComment.trim(),
          gameId: this.gameId || this.post.gameId // 优先使用查询到的gameId
        }

        console.log('发送评论请求数据:', requestData)

        const {data} = await axios.post(API_URLS.createComment(), requestData, {
          headers: this.getAuthHeaders()
        })

        if (data.success || data.code === 200) {
          this.$message?.success('评论发布成功')
          this.newComment = ''
          await this.loadComments()
          this.post.commentCount = (this.post.commentCount || 0) + 1
        } else {
          this.$message?.error(data.message || '评论发布失败')
        }
      } catch (error) {
        console.error('发布评论失败:', error)
        this.$message?.error('发布失败，请重试')
      } finally {
        this.submittingComment = false
      }
    },

    replyToComment(comment) {
      this.replyingTo = comment.commentId
      this.replyContent = ''
    },

    cancelReply() {
      this.replyingTo = null
      this.replyContent = ''
    },

    // 修改 submitReply 方法
    async submitReply(parentId) {
      if (!this.replyContent.trim()) return
      this.submittingReply = true

      try {
        const requestData = {
          postId: parseInt(this.postId),
          commentContent: this.replyContent.trim(),
          parentId: parseInt(parentId),
          gameId: this.gameId || this.post.gameId // 优先使用查询到的gameId
        }

        console.log('发送回复请求数据:', requestData)

        const {data} = await axios.post(API_URLS.createComment(), requestData, {
          headers: this.getAuthHeaders()
        })

        if (data.success || data.code === 200) {
          this.$message?.success('回复成功')
          this.cancelReply()
          await this.loadComments()
          this.post.commentCount = (this.post.commentCount || 0) + 1
        } else {
          console.error('回复失败:', data.message || data.msg)
          this.$message?.error(data.message || data.msg || '回复失败')
        }
      } catch (error) {
        console.error('回复请求失败:', error)
        console.error('错误详情:', error.response?.data)

        if (error.response?.data?.message) {
          this.$message?.error(error.response.data.message)
        } else if (error.response?.data?.msg) {
          this.$message?.error(error.response.data.msg)
        } else {
          this.$message?.error('回复失败，请重试')
        }
      } finally {
        this.submittingReply = false
      }
    }
    ,

    async toggleCommentLike(comment) {
      if (!this.isLoggedIn) return this.$message?.error('请先登录')
      if (comment.likeLoading) return

      comment.likeLoading = true
      try {
        const url = API_URLS.toggleCommentLike(comment.commentId)
        let response

        if (comment.hasLiked) {
          // 取消点赞
          response = await axios.delete(url, {headers: this.getAuthHeaders()})
        } else {
          // 点赞
          response = await axios.post(url, {}, {headers: this.getAuthHeaders()})
        }

        const {data} = response
        if (data.success || data.code === 200) {
          comment.hasLiked = !comment.hasLiked
          comment.likeCount = Math.max(0, (comment.likeCount || 0) + (comment.hasLiked ? 1 : -1))
        } else {
          this.$message?.error(data.message || '操作失败')
        }
      } catch (error) {
        console.error('评论点赞操作失败:', error)
        this.$message?.error('操作失败，请重试')
      } finally {
        comment.likeLoading = false
      }
    },

    // 判断是否可以删除评论
    canDeleteComment(comment) {
      return this.currentUserId && comment.userId === this.currentUserId
    },

    // 删除评论（软删除）
    async deleteComment(comment) {
      if (!this.canDeleteComment(comment)) return
      if (comment.deleting) return

      if (!confirm('确定要删除这条评论吗？')) return

      comment.deleting = true
      try {
        const {data} = await axios.delete(API_URLS.deleteComment(comment.commentId), {
          headers: this.getAuthHeaders()
        })

        if (data.success || data.code === 200) {
          this.$message?.success('评论删除成功')
          await this.loadComments()
          this.post.commentCount = Math.max(0, (this.post.commentCount || 0) - 1)
        } else {
          this.$message?.error(data.message || '删除失败')
        }
      } catch (error) {
        console.error('删除评论失败:', error)
        this.$message?.error('删除失败，请重试')
      } finally {
        comment.deleting = false
      }
    },

    scrollToComments() {
      this.$refs.commentsSection?.scrollIntoView({behavior: 'smooth'})
    },

    previewImage() {
      if (this.post.photo) {
        this.previewImageUrl = this.getImageUrl(this.post.photo)
        this.showImagePreview = true
      }
    },

    closeImagePreview() {
      this.showImagePreview = false
      this.previewImageUrl = ''
    },

    getImageUrl(path) {
      if (!path) return ''
      const cleanPath = path.replace(/\\/g, '/')
      if (cleanPath.startsWith('http')) return cleanPath
      if (cleanPath.startsWith('/')) return `http://localhost:8080${cleanPath}`
      return `/${cleanPath}`
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
      if (token) headers.Authorization = `Bearer ${token}`
      return headers
    }
  }
}
</script>

<style scoped>
.post-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.loading, .error {
  text-align: center;
  padding: 60px 20px;
  color: #666;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.post-container {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  margin-bottom: 20px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 20px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
}

.back-btn:hover {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.post-main {
  padding: 30px;
}

.badges {
  margin-bottom: 15px;
}

.badge {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
  color: white;
  margin-right: 8px;
}

.badge.top {
  background: #ff6b6b;
}

.badge.hot {
  background: #ffa500;
}

h1 {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin: 0 0 20px;
  line-height: 1.4;
}

.meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.author {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  //background: #007bff url() center/cover;
  background: #007bff center/cover;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 16px;
}

.name {
  font-weight: 600;
  color: #333;
  font-size: 16px;
}

.section {
  padding: 2px 6px;
  background: #e9ecef;
  border-radius: 8px;
  font-size: 12px;
  color: #666;
}

.time {
  color: #999;
  font-size: 14px;
}

.image {
  margin-bottom: 25px;
  text-align: center;
  cursor: pointer;
}

.image img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  transition: transform 0.3s;
}

.image img:hover {
  transform: scale(1.02);
}

.content {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 25px;
}

.stats {
  display: flex;
  gap: 25px;
  margin-bottom: 25px;
  padding: 15px 0;
  border: 1px solid #eee;
  border-left: none;
  border-right: none;
  color: #666;
  font-size: 14px;
}

.stats span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.actions {
  display: flex;
  gap: 15px;
}

.btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border: 1px solid #ddd;
  border-radius: 25px;
  background: white;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn:hover:not(:disabled) {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.btn.liked {
  background: #ff6b6b;
  color: white;
  border-color: #ff6b6b;
}

.comments {
  border-top: 1px solid #eee;
  padding: 30px;
}

.comments h3 {
  margin: 0 0 20px;
  color: #333;
  font-size: 20px;
}

.comment-form, .reply-form {
  margin-bottom: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
}

.comment-form textarea, .reply-form textarea {
  width: 100%;
  padding: 15px;
  border: none;
  outline: none;
  resize: vertical;
  font-size: 14px;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  border-top: 1px solid #eee;
  background: #f8f9fa;
}

.form-actions span {
  font-size: 12px;
  color: #999;
}

.form-actions button {
  padding: 6px 16px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 15px;
  cursor: pointer;
  font-size: 14px;
  margin-left: 8px;
}

.form-actions button:hover:not(:disabled) {
  background: #0056b3;
}

.form-actions button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.login-prompt {
  text-align: center;
  padding: 20px;
  color: #666;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 20px;
}

.no-comments {
  text-align: center;
  padding: 40px;
  color: #999;
}

.comment {
  margin-bottom: 20px;
}

.comment-main {
  display: flex;
  gap: 12px;
}

.comment .avatar {
  width: 36px;
  height: 36px;
  font-size: 14px;
}

.comment-body {
  flex: 1;
  background: #f8f9fa;
  padding: 15px;
  border-radius: 12px;
}

.comment-header, .reply-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.comment-header .name, .reply-header .name {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.comment-header .time, .reply-header .time {
  font-size: 12px;
  color: #999;
}

.comment-content, .reply-content {
  margin-bottom: 12px;
  font-size: 14px;
  line-height: 1.6;
  color: #333;
}

.comment-actions {
  display: flex;
  gap: 15px;
}

.comment-actions button {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  border-radius: 12px;
  font-size: 13px;
  transition: all 0.3s;
}

.comment-actions button:hover {
  background: #e9ecef;
}

.comment-actions button.liked {
  color: #ff6b6b;
}

.replies {
  margin-top: 15px;
  padding-left: 20px;
  border-left: 2px solid #e9ecef;
}

.reply {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.reply .avatar {
  width: 32px;
  height: 32px;
  font-size: 12px;
}

.reply-body {
  flex: 1;
  background: white;
  padding: 12px;
  border-radius: 8px;
}

.reply-body button {
  padding: 2px 6px;
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  border-radius: 8px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 3px;
}

.reply-body button:hover {
  background: #f0f0f0;
}

.reply-body button.liked {
  color: #ff6b6b;
}

.preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.preview-container {
  position: relative;
  max-width: 90%;
  max-height: 90%;
}

.preview-container img {
  max-width: 100%;
  max-height: 100%;
  border-radius: 8px;
}

.close-btn {
  position: absolute;
  top: -40px;
  right: 0;
  background: none;
  border: none;
  color: white;
  font-size: 30px;
  cursor: pointer;
}

@media (max-width: 768px) {
  .post-detail {
    padding: 10px;
  }

  .post-main, .comments {
    padding: 20px;
  }

  h1 {
    font-size: 24px;
  }

  .meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .stats {
    gap: 15px;
    font-size: 13px;
  }

  .actions {
    flex-direction: column;
  }

  .btn {
    justify-content: center;
  }
}
</style>