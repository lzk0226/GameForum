<template>
  <div class="post-detail">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <!-- 帖子内容 -->
    <div v-else-if="post" class="container">
      <!-- 左侧帖子内容 -->
      <div class="left-panel">
        <div class="post-card">
          <div class="post-header">
            <h1 class="post-title">{{ post.postTitle }}</h1>

            <div class="post-meta">
              <div class="author-info">
                <div class="avatar" :style="post.avatar ? `background-image: url(${getImageUrl(post.avatar)})` : ''">
                  {{ !post.avatar ? post.nickName.charAt(0) : '' }}
                </div>
                <div class="author-details">
                  <span class="author-name">{{ post.nickName }}</span>
                  <div class="post-info">
                    <span class="section-tag">{{ post.sectionName }}</span>
                  </div>
                </div>
                <button
                    v-if="isLoggedIn && !isOwnPost"
                    @click="toggleFollow"
                    :class="['follow-btn', { following: hasFollowed }]"
                    :disabled="followLoading">
                  {{ followLoading ? '处理中...' : (hasFollowed ? '已关注' : '+ 关注') }}
                </button>
              </div>
              <div class="time-stats">
                <span class="post-time">{{ formatTime(post.createTime) }}</span>
              </div>
            </div>
          </div>

          <div class="post-content">
            <div class="rich-content" v-html="post.postContent"></div>
          </div>

          <div class="post-stats">
            <div class="stat-item" @click="toggleLike" :class="{ active: hasLiked }" :disabled="likeLoading">
              <span>👍</span>
              <span>{{ post.likeCount || 0 }}</span>
            </div>
            <div class="stat-item" @click="scrollToComments">
              <span>💬</span>
              <span>{{ post.commentCount || 0 }}</span>
            </div>
            <div class="stat-item" @click="toggleFavorite" :class="{ active: hasFavorited, favorited: hasFavorited }" :disabled="favoriteLoading">
              <span>⭐</span>
              <span>{{ hasFavorited ? '已收藏' : '收藏' }}</span>
            </div>
            <div class="stats-info">
              <span>👁️ <span>{{ post.viewCount || 0 }}</span></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧评论区 -->
      <div class="right-panel">
        <div class="comments-section">
          <div class="comments-header">
            评论 (<span>{{ comments.length }}</span>)
          </div>

          <!-- 评论列表 -->
          <div class="comments-list" id="commentsList">
            <div v-if="loadingComments" class="loading">
              <div class="spinner"></div>
              <p>加载评论中...</p>
            </div>

            <div v-else-if="comments.length === 0" class="no-comments">
              暂无评论，快来发表第一条评论吧~
            </div>

            <div v-else>
              <div v-for="comment in comments" :key="comment.commentId" class="comment">
                <div class="comment-main">
                  <div class="avatar" :style="comment.userAvatar ? `background-image: url(${getImageUrl(comment.userAvatar)})` : ''">
                    {{ !comment.userAvatar ? comment.nickName.charAt(0) : '' }}
                  </div>
                  <div class="comment-body">
                    <div class="comment-header">
                      <span class="name">{{ comment.nickName }}</span>
                      <span class="time">{{ formatTime(comment.createTime) }}</span>
                    </div>
                    <div class="comment-content" v-html="comment.commentContent"></div>
                    <div class="comment-actions">
                      <button @click="toggleCommentLike(comment)" :class="{ liked: comment.hasLiked }">
                        <span>👍</span>
                        {{ comment.likeCount || 0 }}
                      </button>
                      <button @click="replyToComment(comment)">
                        <span>💬</span>回复
                      </button>
                      <button v-if="canDeleteComment(comment)" @click="deleteComment(comment)" class="delete-btn">
                        <span>🗑️</span>删除
                      </button>
                    </div>

                    <!-- 回复框 -->
                    <div v-if="replyingTo === comment.commentId && isLoggedIn" class="reply-form">
                      <textarea v-model="replyContent" :placeholder="`回复 @${comment.nickName}:`" maxlength="1000"></textarea>
                      <div class="form-actions">
                        <span class="char-count">{{ replyContent.length }}/1000</span>
                        <button class="btn-cancel" @click="cancelReply">取消</button>
                        <button class="btn btn-primary" @click="submitReply(comment.commentId)" :disabled="!replyContent.trim() || submittingReply">
                          {{ submittingReply ? '回复中...' : '回复' }}
                        </button>
                      </div>
                    </div>

                    <!-- 子评论 -->
                    <div v-if="comment.children?.length" class="replies">
                      <div v-for="reply in comment.children" :key="reply.commentId" class="reply">
                        <div class="avatar" :style="reply.userAvatar ? `background-image: url(${getImageUrl(reply.userAvatar)})` : ''">
                          {{ !reply.userAvatar ? reply.nickName.charAt(0) : '' }}
                        </div>
                        <div class="reply-body">
                          <div class="reply-header">
                            <span class="name">{{ reply.nickName }}</span>
                            <span class="time">{{ formatTime(reply.createTime) }}</span>
                          </div>
                          <div class="reply-content" v-html="reply.commentContent"></div>
                          <div class="reply-actions">
                            <button @click="toggleCommentLike(reply)" :class="{ liked: reply.hasLiked }">
                              <span>👍</span>{{ reply.likeCount || 0 }}
                            </button>
                            <button v-if="canDeleteComment(reply)" @click="deleteComment(reply)" class="delete-btn">
                              <span>🗑️</span>删除
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

          <!-- 发表评论框 -->
          <div class="comment-form" v-if="isLoggedIn">
            <textarea id="commentInput" v-model="newComment" class="comment-input" placeholder="写下你的评论..." maxlength="1000" @input="updateCharCount"></textarea>
            <div class="comment-actions">
              <span class="char-count" id="charCount">{{ newComment.length }}/1000</span>
              <button class="btn btn-primary" @click="submitComment" :disabled="!newComment.trim() || submittingComment">
                {{ submittingComment ? '发表中...' : '发表评论' }}
              </button>
            </div>
          </div>

          <div v-else class="login-prompt">
            请登录后发表评论
          </div>
        </div>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else class="error">
      <p>帖子不存在或已被删除</p>
      <button @click="goBack" class="btn btn-primary">返回</button>
    </div>

    <BackToTopToggle
        :atTop="isAtTop"
        :theme="theme"
        :isHomePage="false"
        @toggle-theme="handleToggleTheme"
        @scroll-top="handleScrollTop"
    />
  </div>
</template>

<script>
import axios from 'axios'
import BackToTopToggle from "@/components/user/index/BackToTopToggle.vue"
import {applyTheme, createScrollListener, scrollToTop, toggleTheme} from '@/utils/backToTopUtils.js'
import API_URLS from '@/api/apiUrls.js'

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
      hasFavorited: false,
      favoriteLoading: false,
      hasFollowed: false,
      followLoading: false,
      newComment: '',
      submittingComment: false,
      replyingTo: null,
      replyContent: '',
      submittingReply: false,
      currentUserId: null,
      gameId: null,
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
    },
    isOwnPost() {
      return this.currentUserId && this.post && this.post.userId === this.currentUserId
    }
  },
  async mounted() {
    await this.getCurrentUser()
    await this.loadPostDetail()
    await this.loadComments()
    if (this.isLoggedIn) {
      await this.checkFavoriteStatus()
      await this.checkFollowStatus()
    }
    this.cleanupScrollListener = createScrollListener((atTop) => {
      this.isAtTop = atTop;
    });
    applyTheme(this.theme);

    // 监听图片加载完成，确保图片缩放生效
    this.$nextTick(() => {
      this.processImages();
    });
  },
  beforeUnmount() {
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
    // 处理图片缩放
    processImages() {
      const richContent = document.querySelector('.rich-content');
      if (!richContent) return;

      const images = richContent.querySelectorAll('img');
      images.forEach(img => {
        // 移除可能导致滚动的样式
        img.style.maxWidth = '100%';
        img.style.height = 'auto';
        img.style.display = 'block';
        img.style.margin = '16px auto';

        // 确保图片不会超出容器
        if (img.naturalWidth > richContent.clientWidth) {
          img.style.width = '100%';
        }

        // 添加加载完成后的处理
        if (img.complete) {
          this.adjustImageSize(img, richContent);
        } else {
          img.onload = () => {
            this.adjustImageSize(img, richContent);
          };
        }
      });
    },
    // 调整图片尺寸
    adjustImageSize(img, container) {
      const containerWidth = container.clientWidth;
      const imgWidth = img.naturalWidth;

      if (imgWidth > containerWidth) {
        img.style.width = '100%';
        img.style.height = 'auto';
      }
    },
    async getCurrentUser() {
      if (!this.isLoggedIn) return
      try {
        const token = this.getToken()
        if (!token) return
        const payload = JSON.parse(atob(token.split('.')[1]))
        const userId = payload.userId || payload.sub || payload.id
        if (!userId) return
        const {data} = await axios.get(API_URLS.getUserProfile(userId), {
          headers: this.getAuthHeaders()
        })
        if (data.success || data.code === 200) {
          this.currentUserId = data.data.userId
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        if (error.response?.status === 401 || error.response?.status === 403) {
          localStorage.removeItem('accessToken')
          localStorage.removeItem('token')
          localStorage.removeItem('Admin-Token')
          localStorage.removeItem('access_token')
        }
      }
    },
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
          await this.getGameIdBySectionId()
          if (this.isLoggedIn) await this.checkLikeStatus()

          // 在内容加载后处理图片
          this.$nextTick(() => {
            setTimeout(() => {
              this.processImages();
            }, 100);
          });
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
    async getGameIdBySectionId() {
      if (!this.post?.sectionId) return
      try {
        const {data} = await axios.get(API_URLS.getGameIdBySectionId(this.post.sectionId))
        if (data.success || data.code === 200) {
          this.gameId = data.data
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
    async checkFavoriteStatus() {
      if (!this.postId || !this.isLoggedIn) return
      try {
        const {data} = await axios.get(API_URLS.checkPostFavoriteStatus(this.postId), {
          headers: this.getAuthHeaders()
        })
        if (data.success || data.code === 200) {
          this.hasFavorited = data.data || false
        }
      } catch (error) {
        console.error('检查收藏状态失败:', error)
      }
    },
    async checkFollowStatus() {
      if (!this.post?.userId || !this.isLoggedIn || this.isOwnPost) return
      try {
        const {data} = await axios.get(API_URLS.checkFollowStatus(this.post.userId), {
          headers: this.getAuthHeaders()
        })
        if (data.success || data.code === 200) {
          this.hasFollowed = data.data || false
        }
      } catch (error) {
        console.error('检查关注状态失败:', error)
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
          response = await axios.delete(url, {headers: this.getAuthHeaders()})
        } else {
          response = await axios.post(url, {}, {headers: this.getAuthHeaders()})
        }
        const {data} = response
        if (data.success || data.code === 200) {
          this.hasLiked = !this.hasLiked
          this.post.likeCount = Math.max(0, (this.post.likeCount || 0) + (this.hasLiked ? 1 : -1))
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
    async toggleFavorite() {
      if (!this.isLoggedIn) return this.$message?.error('请先登录')
      if (this.favoriteLoading) return
      this.favoriteLoading = true
      try {
        let response
        if (this.hasFavorited) {
          response = await axios.delete(API_URLS.removePostFavorite(this.postId), {
            headers: this.getAuthHeaders()
          })
        } else {
          response = await axios.post(API_URLS.addPostFavorite(this.postId), {}, {
            headers: this.getAuthHeaders()
          })
        }
        const {data} = response
        if (data.success || data.code === 200) {
          this.hasFavorited = !this.hasFavorited
          this.$message?.success(this.hasFavorited ? '收藏成功' : '取消收藏成功')
        } else {
          this.$message?.error(data.message || '操作失败')
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
        this.$message?.error('操作失败，请重试')
      } finally {
        this.favoriteLoading = false
      }
    },
    async toggleFollow() {
      if (!this.isLoggedIn) return this.$message?.error('请先登录')
      if (this.followLoading || this.isOwnPost) return
      this.followLoading = true
      try {
        let response
        if (this.hasFollowed) {
          response = await axios.delete(API_URLS.unfollowUser(this.post.userId), {
            headers: this.getAuthHeaders()
          })
        } else {
          response = await axios.post(API_URLS.followUser(this.post.userId), {}, {
            headers: this.getAuthHeaders()
          })
        }
        const {data} = response
        if (data.success || data.code === 200) {
          this.hasFollowed = !this.hasFollowed
          this.$message?.success(this.hasFollowed ? '关注成功' : '取消关注成功')
        } else {
          this.$message?.error(data.message || '操作失败')
        }
      } catch (error) {
        console.error('关注操作失败:', error)
        this.$message?.error('操作失败，请重试')
      } finally {
        this.followLoading = false
      }
    },
    async submitComment() {
      if (!this.newComment.trim()) return
      this.submittingComment = true
      try {
        const requestData = {
          postId: parseInt(this.postId),
          commentContent: this.newComment.trim(),
          gameId: this.gameId || this.post.gameId
        }
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
    async submitReply(parentId) {
      if (!this.replyContent.trim()) return
      this.submittingReply = true
      try {
        const requestData = {
          postId: parseInt(this.postId),
          commentContent: this.replyContent.trim(),
          parentId: parseInt(parentId),
          gameId: this.gameId || this.post.gameId
        }
        const {data} = await axios.post(API_URLS.createComment(), requestData, {
          headers: this.getAuthHeaders()
        })
        if (data.success || data.code === 200) {
          this.$message?.success('回复成功')
          this.cancelReply()
          await this.loadComments()
          this.post.commentCount = (this.post.commentCount || 0) + 1
        } else {
          this.$message?.error(data.message || data.msg || '回复失败')
        }
      } catch (error) {
        console.error('回复请求失败:', error)
        this.$message?.error('回复失败，请重试')
      } finally {
        this.submittingReply = false
      }
    },
    async toggleCommentLike(comment) {
      if (!this.isLoggedIn) return this.$message?.error('请先登录')
      if (comment.likeLoading) return
      comment.likeLoading = true
      try {
        const url = API_URLS.toggleCommentLike(comment.commentId)
        let response
        if (comment.hasLiked) {
          response = await axios.delete(url, {headers: this.getAuthHeaders()})
        } else {
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
    canDeleteComment(comment) {
      return this.currentUserId && comment.userId === this.currentUserId
    },
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
      document.getElementById('commentsList')?.scrollIntoView({behavior: 'smooth'})
    },
    updateCharCount() {
      // 字符计数自动更新，由 v-model 处理
    },
    goBack() {
      this.$router.go(-1)
    },
    getImageUrl(path) {
      if (!path) return ''
      const cleanPath = path.replace(/\\/g, '/')
      if (cleanPath.startsWith('http')) return cleanPath
      return API_URLS.getPostPhotos() + cleanPath
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
/* ========== 重置样式 ========== */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* ========== 全局容器 ========== */
.post-detail {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f5f5 0%, #fafafa 100%);
  padding: 20px;
}

/* ========== 加载和错误状态 ========== */
.loading, .error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  color: #666;
  text-align: center;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid rgba(0, 123, 255, 0.1);
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error p {
  font-size: 16px;
  margin-bottom: 20px;
}

/* ========== 主容器布局 ========== */
.container {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 420px;
  gap: 24px;
  align-items: start;
}

.left-panel {
  min-width: 0;
  padding-bottom: 40px;
}

.right-panel {
  position: sticky;
  top: 20px;
  height: calc(100vh - 40px);
  display: flex;
  flex-direction: column;
}

/* ========== 帖子卡片样式 ========== */
.post-card, .comments-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: box-shadow 0.3s ease;
}

.post-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

/* ========== 帖子头部 ========== */
.post-header {
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.post-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 16px;
  line-height: 1.4;
  word-break: break-word;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.author-info {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  flex: 1;
}

.avatar {
  width: 44px;
  height: 44px;
  min-width: 44px;
  border-radius: 50%;
  background-size: cover;
  background-position: center;
  background-color: #007bff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 16px;
  flex-shrink: 0;
}

.author-details {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.author-name {
  font-weight: 600;
  color: #1a1a1a;
  font-size: 14px;
}

.post-info {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.section-tag {
  display: inline-block;
  padding: 4px 10px;
  background: #e8f4fd;
  border-radius: 4px;
  font-size: 12px;
  color: #0066cc;
  font-weight: 500;
}

.time-stats {
  display: flex;
  flex-direction: column;
  gap: 8px;
  text-align: right;
  white-space: nowrap;
}

.post-time {
  font-size: 13px;
  color: #999;
}

.stats-info {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #666;
  justify-content: flex-end;
}

/* ========== 帖子内容 ========== */
.post-content {
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
  /* 移除固定高度和滚动条，让内容自然流动 */
}

.rich-content {
  font-size: 15px;
  line-height: 1.8;
  color: #333;
  word-break: break-word;
  overflow-wrap: break-word;
  /* 确保内容不会溢出容器 */
  overflow: hidden;
}

/* ========== 富文本内容样式 ========== */
.rich-content h1,
.rich-content h2,
.rich-content h3,
.rich-content h4,
.rich-content h5,
.rich-content h6 {
  margin: 16px 0 12px;
  font-weight: 600;
  color: #1a1a1a;
}

.rich-content h1 { font-size: 24px; }
.rich-content h2 { font-size: 20px; }
.rich-content h3 { font-size: 18px; }
.rich-content h4 { font-size: 16px; }

.rich-content p {
  margin-bottom: 12px;
}

.rich-content a {
  color: #007bff;
  text-decoration: none;
}

.rich-content a:hover {
  text-decoration: underline;
}

.rich-content blockquote {
  margin: 12px 0;
  padding: 12px 16px;
  border-left: 4px solid #007bff;
  background: #f9f9f9;
  color: #666;
}

.rich-content code {
  background: #f5f5f5;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  color: #d73a49;
}

.rich-content pre {
  background: #2d2d2d;
  color: #f8f8f2;
  padding: 16px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 12px 0;
  font-size: 13px;
  line-height: 1.6;
}

.rich-content pre code {
  background: none;
  color: inherit;
  padding: 0;
}

.rich-content ul,
.rich-content ol {
  margin: 12px 0 12px 20px;
}

.rich-content li {
  margin-bottom: 6px;
}

.rich-content table {
  width: 100%;
  border-collapse: collapse;
  margin: 12px 0;
}

.rich-content table td,
.rich-content table th {
  border: 1px solid #ddd;
  padding: 8px 12px;
  text-align: left;
}

.rich-content table th {
  background: #f5f5f5;
  font-weight: 600;
}

/* ========== 图片样式（关键修复） ========== */
.rich-content img {
  max-width: 100% !important;
  height: auto !important;
  display: block;
  margin: 16px auto;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
  cursor: zoom-in;
  /* 确保图片不会超出容器 */
  object-fit: contain;
}

.rich-content img:hover {
  transform: scale(1.02);
}

.rich-content figure {
  margin: 16px 0;
  text-align: center;
  max-width: 100%;
  overflow: hidden;
}

.rich-content figcaption {
  font-size: 13px;
  color: #999;
  margin-top: 8px;
}

/* ========== 帖子统计 ========== */
.post-stats {
  padding: 16px 24px;
  display: flex;
  gap: 32px;
  background: #fafafa;
  border-top: 1px solid #f0f0f0;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #666;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  user-select: none;
}

.stat-item:hover {
  color: #007bff;
  transform: translateY(-2px);
}

.stat-item.active {
  color: #ff6b6b;
}

.stat-item:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* ========== 评论区域 ========== */
.comments-section {
  padding: 20px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.comments-header {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.comments-list {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 16px;
  padding-right: 8px;
}

.comments-list::-webkit-scrollbar {
  width: 6px;
}

.comments-list::-webkit-scrollbar-track {
  background: #f5f5f5;
  border-radius: 3px;
}

.comments-list::-webkit-scrollbar-thumb {
  background: #d0d0d0;
  border-radius: 3px;
}

.comments-list::-webkit-scrollbar-thumb:hover {
  background: #b0b0b0;
}

.no-comments {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  font-size: 14px;
}

/* ========== 评论样式 ========== */
.comment {
  padding: 16px 0;
  border-bottom: 1px solid #f5f5f5;
  display: flex;
  gap: 12px;
}

.comment:last-child {
  border-bottom: none;
}

.comment-main .avatar,
.reply .avatar {
  width: 36px;
  height: 36px;
  min-width: 36px;
  border-radius: 50%;
  background-size: cover;
  background-position: center;
  background-color: #007bff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  flex-shrink: 0;
}

.comment-body {
  flex: 1;
  min-width: 0;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.comment-header .name {
  font-weight: 600;
  font-size: 14px;
  color: #1a1a1a;
}

.comment-header .time {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

.comment-content {
  margin: 8px 0;
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  word-break: break-word;
  overflow-wrap: break-word;
}

.comment-actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
  flex-wrap: wrap;
}

.comment-actions button {
  background: none;
  border: none;
  color: #999;
  font-size: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all 0.2s ease;
  padding: 4px 0;
  font-weight: 500;
}

.comment-actions button:hover {
  color: #007bff;
}

.comment-actions button.liked {
  color: #ff6b6b;
}

.comment-actions .delete-btn {
  color: #d9534f;
}

.comment-actions .delete-btn:hover {
  color: #c9302c;
}

/* ========== 回复区域 ========== */
.replies {
  margin-top: 12px;
  padding-left: 48px;
  border-left: 2px solid #f0f0f0;
}

.reply {
  padding: 12px 0;
  display: flex;
  gap: 10px;
  border-bottom: 1px dashed #f5f5f5;
}

.reply:last-child {
  border-bottom: none;
}

.reply-body {
  flex: 1;
  min-width: 0;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 4px;
}

.reply-header .name {
  font-weight: 600;
  font-size: 13px;
  color: #1a1a1a;
}

.reply-header .time {
  font-size: 11px;
  color: #999;
  white-space: nowrap;
}

.reply-content {
  margin: 4px 0;
  font-size: 13px;
  color: #333;
  line-height: 1.6;
  word-break: break-word;
  overflow-wrap: break-word;
}

.reply-actions {
  display: flex;
  gap: 10px;
  margin-top: 6px;
}

.reply-actions button {
  background: none;
  border: none;
  color: #999;
  font-size: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 3px;
  transition: color 0.2s ease;
  padding: 2px 0;
}

.reply-actions button:hover {
  color: #007bff;
}

.reply-actions button.liked {
  color: #ff6b6b;
}

.reply-actions .delete-btn {
  color: #d9534f;
}

.reply-actions .delete-btn:hover {
  color: #c9302c;
}

/* ========== 回复输入框 ========== */
.reply-form {
  margin-top: 12px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 6px;
  border: 1px solid #f0f0f0;
}

.reply-form textarea {
  width: 100%;
  min-height: 60px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  outline: none;
  font-family: inherit;
  font-size: 13px;
  line-height: 1.5;
  transition: border-color 0.2s ease;
}

.reply-form textarea:focus {
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  gap: 10px;
}

.form-actions .char-count {
  font-size: 12px;
  color: #999;
}

/* ========== 按钮样式 ========== */
.btn {
  padding: 8px 16px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-size: 13px;
  font-weight: 500;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.btn-primary {
  background: #007bff;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #ffffff;
}

.btn-primary:disabled {
  background: #ccc;
  cursor: not-allowed;
  opacity: 0.6;
}

.btn-cancel {
  background: #e9ecef;
  color: #333;
}

.btn-cancel:hover {
  background: #dde0e4;
}

/* ========== 评论表单 ========== */
.comment-form {
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
}

.comment-input {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  outline: none;
  resize: vertical;
  margin-bottom: 10px;
  font-family: inherit;
  font-size: 13px;
  line-height: 1.5;
  transition: border-color 0.2s ease;
}

.comment-input:focus {
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.login-prompt {
  text-align: center;
  color: #999;
  padding: 16px 12px;
  background: #fafafa;
  border-radius: 6px;
  border: 1px dashed #e0e0e0;
  font-size: 13px;
}

/* ========== 响应式设计 ========== */
@media (max-width: 1024px) {
  .container {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .right-panel {
    position: static;
    height: auto;
    max-height: none;
  }

  .post-title {
    font-size: 24px;
  }

  .post-meta {
    flex-direction: column;
    gap: 12px;
  }

  .time-stats {
    text-align: left;
  }
}

@media (max-width: 768px) {
  .post-detail {
    padding: 12px;
  }

  .post-card, .comments-section {
    border-radius: 6px;
  }

  .post-header {
    padding: 16px;
  }

  .post-title {
    font-size: 20px;
  }

  .post-content {
    padding: 16px;
  }

  .post-stats {
    gap: 16px;
    padding: 12px 16px;
  }

  .comments-section {
    padding: 16px;
  }

  .comment {
    padding: 12px 0;
  }

  .replies {
    padding-left: 36px;
  }

  .comment-actions {
    gap: 8px;
  }

  .comment-input {
    min-height: 70px;
    padding: 10px;
  }
}
/* ========== 关注按钮样式 ========== */
.follow-btn {
  padding: 6px 14px;
  font-size: 13px;
  font-weight: 500;
  border-radius: 4px;
  border: 1px solid #007bff;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s ease;
}

.follow-btn:hover {
  background-color: #0056b3;
  border-color: #0056b3;
}

.follow-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.follow-btn.following {
  background-color: #f0f0f0;
  color: #666;
  border-color: #ccc;
}

.follow-btn.following:hover {
  background-color: #e0e0e0;
  border-color: #bbb;
}

</style>