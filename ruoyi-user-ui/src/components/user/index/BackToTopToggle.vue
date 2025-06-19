<template>
  <div class="button-container">
    <!-- 原功能按钮（主题切换/返回顶部/返回上一级） -->
    <button class="toggle-btn main-btn" @click="handleMainClick" :title="buttonTitle" :class="themeClass">
      <!-- 在主页且在顶部：显示主题切换图标 -->
      <svg v-if="isHomePage && atTop" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="icon">
        <!-- 简约太阳图标 -->
        <circle cx="12" cy="12" r="5" fill="currentColor"/>
        <line x1="12" y1="1" x2="12" y2="4" stroke="currentColor" stroke-width="2"/>
        <line x1="12" y1="20" x2="12" y2="23" stroke="currentColor" stroke-width="2"/>
        <line x1="4.22" y1="4.22" x2="6.34" y2="6.34" stroke="currentColor" stroke-width="2"/>
        <line x1="17.66" y1="17.66" x2="19.78" y2="19.78" stroke="currentColor" stroke-width="2"/>
        <line x1="1" y1="12" x2="4" y2="12" stroke="currentColor" stroke-width="2"/>
        <line x1="20" y1="12" x2="23" y2="12" stroke="currentColor" stroke-width="2"/>
        <line x1="4.22" y1="19.78" x2="6.34" y2="17.66" stroke="currentColor" stroke-width="2"/>
        <line x1="17.66" y1="6.34" x2="19.78" y2="4.22" stroke="currentColor" stroke-width="2"/>
      </svg>
      <!-- 非主页且在顶部：显示返回上一级图标 -->
      <svg v-else-if="!isHomePage && atTop" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="icon">
        <!-- 返回箭头图标 -->
        <line x1="19" y1="12" x2="5" y2="12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
        <polyline points="12 19 5 12 12 5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round"/>
      </svg>
      <!-- 不在顶部：显示返回顶部图标 -->
      <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="icon">
        <!-- 简约向上箭头图标 -->
        <line x1="12" y1="19" x2="12" y2="5" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
        <polyline points="5 12 12 5 19 12" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round"/>
      </svg>
    </button>

    <!-- 刷新按钮 -->
    <button class="toggle-btn refresh-btn" @click="handleRefresh" title="刷新页面" :class="themeClass">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="icon">
        <!-- 刷新图标 -->
        <path d="M21 12a9 9 0 0 0-9-9 9.75 9.75 0 0 0-6.74 2.74L3 8" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M3 3v5h5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
              stroke-linejoin="round"/>
        <path d="M3 12a9 9 0 0 0 9 9 9.75 9.75 0 0 0 6.74-2.74L21 16" fill="none" stroke="currentColor" stroke-width="2"
              stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M21 21v-5h-5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
              stroke-linejoin="round"/>
      </svg>
    </button>

    <!-- 分享按钮 -->
    <button class="toggle-btn share-btn" @click="handleShare" title="分享链接" :class="themeClass">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="icon">
        <!-- 分享图标 -->
        <circle cx="18" cy="5" r="3" fill="none" stroke="currentColor" stroke-width="2"/>
        <circle cx="6" cy="12" r="3" fill="none" stroke="currentColor" stroke-width="2"/>
        <circle cx="18" cy="19" r="3" fill="none" stroke="currentColor" stroke-width="2"/>
        <line x1="8.59" y1="13.51" x2="15.42" y2="17.49" stroke="currentColor" stroke-width="2"/>
        <line x1="15.41" y1="6.51" x2="8.59" y2="10.49" stroke="currentColor" stroke-width="2"/>
      </svg>
    </button>

    <!-- 复制成功提示 -->
    <div v-if="showCopySuccess" class="copy-success-toast" :class="themeClass">
      已复制链接
    </div>
  </div>
</template>

<script>
import {NavigationManager} from '@/utils/backToTopUtils.js'

export default {
  name: 'BackToTopToggle',
  props: {
    atTop: Boolean,
    theme: {
      type: String,
      default: 'light' // 'light' 或 'dark'
    },
    isHomePage: {
      type: Boolean,
      default: false // 是否在主页
    },
    fallbackUrl: {
      type: String,
      default: '/' // 无法返回时的备用URL
    }
  },
  emits: ['toggle-theme', 'scroll-top', 'go-back'],
  data() {
    return {
      showCopySuccess: false,
      navigationManager: null
    }
  },
  computed: {
    buttonTitle() {
      if (this.isHomePage && this.atTop) {
        return '切换昼夜模式';
      } else if (!this.isHomePage && this.atTop) {
        // 使用导航管理器获取更准确的提示文本
        return this.navigationManager ? this.navigationManager.getBackButtonTitle() : '返回上一级';
      } else {
        return '返回顶部';
      }
    },
    themeClass() {
      return this.theme === 'light' ? 'light-btn' : 'dark-btn'
    }
  },
  mounted() {
    // 初始化导航管理器
    this.navigationManager = new NavigationManager();
  },
  methods: {
    handleMainClick() {
      if (this.isHomePage && this.atTop) {
        // 主页且在顶部：切换主题
        this.$emit('toggle-theme');
      } else if (!this.isHomePage && this.atTop) {
        // 非主页且在顶部：智能返回上一级
        this.handleGoBack();
      } else {
        // 不在顶部：返回顶部
        this.$emit('scroll-top');
      }
    },
    handleGoBack() {
      // 使用导航管理器处理返回逻辑
      if (this.navigationManager) {
        this.navigationManager.goBack(this.fallbackUrl);
      } else {
        // 备用方案：使用传统的返回逻辑
        this.goBackFallback();
      }
      // 仍然触发事件，让父组件知道返回操作已执行
      this.$emit('go-back');
    },
    goBackFallback() {
      // 传统的返回逻辑作为备用方案
      if (window.history.length > 1) {
        window.history.back();
      } else {
        try {
          window.close();
        } catch (error) {
          // 如果无法关闭窗口，导航到备用URL
          window.location.href = this.fallbackUrl;
        }
      }
    },
    handleRefresh() {
      window.location.reload();
    },
    async handleShare() {
      try {
        const currentUrl = window.location.href;
        await navigator.clipboard.writeText(currentUrl);
        this.showCopySuccess = true;

        // 3秒后隐藏提示
        setTimeout(() => {
          this.showCopySuccess = false;
        }, 3000);
      } catch (err) {
        console.error('复制失败:', err);
        // 降级处理：创建临时输入框进行复制
        const textArea = document.createElement('textarea');
        textArea.value = window.location.href;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);

        this.showCopySuccess = true;
        setTimeout(() => {
          this.showCopySuccess = false;
        }, 3000);
      }
    }
  }
}
</script>

<style scoped>
.button-container {
  position: fixed;
  right: 20px;
  bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: center;
}

.toggle-btn {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  padding: 0;
}

.toggle-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.25);
}

.toggle-btn:active {
  transform: scale(0.95);
}

/* 白天模式按钮：白色背景，黑色图标 */
.light-btn {
  background-color: #fff;
  color: #000;
}

/* 夜晚模式按钮：黑色背景，白色图标 */
.dark-btn {
  background-color: #000;
  color: #fff;
}

.icon {
  width: 24px;
  height: 24px;
}

/* 复制成功提示样式 */
.copy-success-toast {
  position: absolute;
  right: 60px;
  bottom: 50%;
  transform: translateY(50%);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  white-space: nowrap;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s ease-out;
  z-index: 1000;
}

.copy-success-toast.light-btn {
  background-color: #fff;
  color: #000;
  border: 1px solid #e0e0e0;
}

.copy-success-toast.dark-btn {
  background-color: #333;
  color: #fff;
  border: 1px solid #444;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(50%) translateX(10px);
  }
  to {
    opacity: 1;
    transform: translateY(50%) translateX(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .button-container {
    right: 16px;
    bottom: 16px;
    gap: 10px;
  }

  .toggle-btn {
    width: 44px;
    height: 44px;
  }

  .icon {
    width: 20px;
    height: 20px;
  }

  .copy-success-toast {
    right: 55px;
    font-size: 12px;
    padding: 6px 12px;
  }
}
</style>