<template>
  <div class="announcement">
    <h2>公告</h2>
    <div class="announcement-content-wrapper">
      <div v-if="loading" class="loading">
        加载中...
      </div>
      <div v-else-if="error" class="error">
        {{ error }}
      </div>
      <div v-else-if="announcements.length === 0" class="no-data">
        暂无公告
      </div>
      <div v-else class="announcement-list">
        <div v-for="(item, index) in recentAnnouncements" :key="index" class="announcement-item">
          <p class="announcement-title">{{ item.titel }}</p>
          <p class="announcement-content">{{ item.content }}</p>
          <p class="announcement-date">{{ formatDate(item.time) }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import API_URLS from '@/api/apiUrls.js'; // 引入 API URL 配置

export default {
  name: 'Announcement',
  data() {
    return {
      announcements: [],
      loading: false,
      error: null
    }
  },
  computed: {
    recentAnnouncements() {
      // 按时间排序并取最近的3条
      return this.announcements
          .sort((a, b) => new Date(b.time) - new Date(a.time))
          .slice(0, 3)
    }
  },
  mounted() {
    this.fetchAnnouncements()
  },
  methods: {
    async fetchAnnouncements() {
      this.loading = true
      this.error = null

      try {
        const response = await axios.get(API_URLS.getAnnouncementsList())

        if (response.data.success && response.data.code === 200) {
          this.announcements = response.data.data || []
        } else {
          throw new Error(response.data.message || '获取公告失败')
        }
      } catch (error) {
        console.error('获取公告列表失败:', error)
        this.error = '获取公告失败，请稍后重试'
      } finally {
        this.loading = false
      }
    },

    formatDate(dateString) {
      try {
        const date = new Date(dateString)
        return date.toLocaleDateString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit'
        })
      } catch (error) {
        return dateString
      }
    }
  }
}
</script>

<style scoped>
.announcement {
  width: 100%;
  height: 200px; /* 固定高度 */
  padding: 15px;
  border: 1px solid #ddd;
  box-sizing: border-box;
  background-color: #fff;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
}

.announcement h2 {
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #333;
  flex-shrink: 0; /* 标题不收缩 */
}

.announcement-content-wrapper {
  flex: 1; /* 占据剩余空间 */
  overflow-y: auto; /* 垂直滚动 */
  overflow-x: hidden; /* 隐藏横向滚动 */
  min-height: 0; /* 允许flex子项收缩 */
}

.announcement-list {
  height: 100%;
}

.announcement-item {
  padding: 12px 0;
  border-bottom: 1px solid #eee;
  flex-shrink: 0; /* 公告项不收缩 */
}

.announcement-item:last-child {
  border-bottom: none;
}

.announcement-title {
  margin: 0 0 5px 0;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  word-wrap: break-word;
}

.announcement-content {
  margin: 0 0 5px 0;
  font-size: 13px;
  color: #666;
  line-height: 1.4;
  word-wrap: break-word;
}

.announcement-date {
  margin: 0;
  font-size: 12px;
  color: #888;
}

.loading, .error, .no-data {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #888;
  font-size: 14px;
}

.error {
  color: #f56565;
}

/* 自定义滚动条样式 */
.announcement-content-wrapper::-webkit-scrollbar {
  width: 6px;
}

.announcement-content-wrapper::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.announcement-content-wrapper::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.announcement-content-wrapper::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>