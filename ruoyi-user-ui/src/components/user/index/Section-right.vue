<template>
  <div class="recommended-sections">
    <h3 @click="goToSectionPage">推荐版块</h3>
    <div v-if="loading" class="loading">
      加载中...
    </div>
    <div v-else-if="error" class="error">
      加载失败
    </div>
    <div v-else class="sections-list">
      <div v-for="section in displaySections" :key="section.sectionId" class="section-item"
           @click="selectSection(section)">
        <div class="section-header">
          <h4 class="section-name">{{ section.sectionName }}</h4>
          <div class="game-badge">{{ section.gameName }}</div>
        </div>
        <p class="section-desc">{{ truncateText(section.sectionDescription, 45) }}</p>
        <div class="section-meta">
          <span class="section-tag">{{ section.remark }}</span>
          <span class="section-order">#{{ section.orderNum }}</span>
        </div>
      </div>

      <div v-if="sections.length > maxDisplay" class="show-more" @click="toggleShowAll">
        <span>{{ showAll ? '收起' : `查看全部 (${sections.length})` }}</span>
        <span class="arrow" :class="{ 'expanded': showAll }">▼</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RecommendedSections',
  data() {
    return {
      sections: [],
      loading: true,
      error: null,
      showAll: false,
      maxDisplay: 3 // 默认显示3个版块
    }
  },
  computed: {
    displaySections() {
      return this.showAll ? this.sections : this.sections.slice(0, this.maxDisplay)
    }
  },
  async mounted() {
    await this.loadSections()
  },
  methods: {
    async loadSections() {
      try {
        this.loading = true
        this.error = null

        // 获取热门版块数据，限制10个
        const response = await fetch('http://localhost:8080/user/section/hot?limit=10')
        if (!response.ok) {
          throw new Error('获取版块数据失败')
        }

        const result = await response.json()
        if (result.code !== 200) {
          throw new Error(result.msg || '获取版块数据失败')
        }

        this.sections = result.data || []

      } catch (error) {
        console.error('加载版块数据失败:', error)
        this.error = error.message || '加载数据失败'

        // 降级处理，使用所有版块接口
        try {
          const fallbackResponse = await fetch('http://localhost:8080/user/section/list')
          if (fallbackResponse.ok) {
            const fallbackResult = await fallbackResponse.json()
            if (fallbackResult.code === 200) {
              this.sections = fallbackResult.data || []
              this.error = null
            }
          }
        } catch (fallbackError) {
          console.error('降级加载也失败:', fallbackError)
        }
      } finally {
        this.loading = false
      }
    },

    // 点击标题跳转到版块页面
    goToSectionPage() {
      this.$router.push({name: 'SectionPage'})
    },

    // 点击具体版块跳转到版块详情页
    selectSection(section) {
      // 发出事件（保留原有功能）
      this.$emit('section-selected', section)

      // 跳转到版块详情页，可以传递版块ID作为参数
      this.$router.push({
        name: 'Section',
        query: {sectionId: section.sectionId} // 通过query参数传递版块ID
      })

      console.log('Selected section:', section.sectionName)
    },

    toggleShowAll() {
      this.showAll = !this.showAll
    },

    truncateText(text, maxLength) {
      if (!text) return ''
      return text.length > maxLength ? text.slice(0, maxLength) + '...' : text
    }
  }
}
</script>

<style scoped>
.recommended-sections {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background-color: #fff;
  box-sizing: border-box;
  height: fit-content;
}

.recommended-sections h3 {
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  text-align: center;
  padding-bottom: 8px;
  border-bottom: 2px solid #28a745;
  cursor: pointer; /* 添加鼠标指针样式 */
  transition: color 0.2s ease; /* 添加过渡效果 */
}

.recommended-sections h3:hover {
  color: #28a745; /* 鼠标悬停时改变颜色 */
}

.loading, .error {
  text-align: center;
  padding: 15px 8px;
  font-size: 12px;
  color: #666;
}

.error {
  color: #d32f2f;
}

.sections-list {
  margin-top: 8px;
}

.section-item {
  padding: 10px;
  margin-bottom: 12px;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  background-color: #fafafa;
  cursor: pointer;
  transition: all 0.2s ease;
}

.section-item:last-child {
  margin-bottom: 0;
}

.section-item:hover {
  border-color: #28a745;
  box-shadow: 0 2px 8px rgba(40, 167, 69, 0.15);
  transform: translateY(-1px);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 6px;
  gap: 8px;
}

.section-name {
  flex: 1;
  margin: 0;
  font-size: 13px;
  font-weight: 600;
  color: #28a745;
  line-height: 1.3;
}

.game-badge {
  background: linear-gradient(135deg, #007bff, #0056b3);
  color: white;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 9px;
  font-weight: 500;
  white-space: nowrap;
  flex-shrink: 0;
}

.section-desc {
  margin: 6px 0;
  font-size: 11px;
  color: #666;
  line-height: 1.4;
}

.section-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.section-tag {
  background-color: #e8f5e8;
  color: #2e7d32;
  padding: 2px 6px;
  border-radius: 8px;
  font-size: 9px;
  font-weight: 500;
}

.section-order {
  font-size: 10px;
  color: #999;
  font-weight: 500;
}

.show-more {
  margin-top: 12px;
  padding: 8px;
  text-align: center;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border: 1px solid #dee2e6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 12px;
  color: #495057;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
}

.show-more:hover {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border-color: #2196f3;
  color: #1976d2;
}

.arrow {
  transition: transform 0.2s;
  font-size: 10px;
}

.arrow.expanded {
  transform: rotate(180deg);
}
</style>