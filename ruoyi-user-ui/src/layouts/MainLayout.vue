<template>
  <div :class="['layout', theme]">
    <!-- Navigation bar at the top -->
    <div class="nav-container">
      <NavigationBar/>
    </div>

    <!-- Main content area -->
    <div class="main-content">
      <div class="left-column">
        <Logo/>
        <CategoryMenu/>
      </div>

      <div class="center-column">
        <Carousel/>
        <PostList/>
      </div>

      <div class="right-column">
        <Announcement/>
        <RecommendedItems/>
      </div>
    </div>

    <!-- 右下角小圆球按钮 -->
    <BackToTopToggle
        :atTop="isAtTop"
        :theme="theme"
        :isHomePage="true"
        @toggle-theme="handleToggleTheme"
        @scroll-top="handleScrollTop"
    />
  </div>
</template>

<script>
import NavigationBar from '../components/user/index/NavigationBar.vue'
import Logo from '../components/user/index/Logo.vue'
import Carousel from '../components/user/index/Carousel.vue'
import Announcement from '../components/user/index/Announcement.vue'
import CategoryMenu from '../components/user/index/Game-left.vue'
import PostList from '../components/user/index/PostList.vue'
import RecommendedItems from '../components/user/index/Section-right.vue'
import BackToTopToggle from '../components/user/index/BackToTopToggle.vue'
import {applyTheme, createScrollListener, scrollToTop, toggleTheme} from '../utils/backToTopUtils.js'

export default {
  name: 'MainLayout',
  components: {
    NavigationBar,
    Logo,
    Carousel,
    Announcement,
    CategoryMenu,
    PostList,
    RecommendedItems,
    BackToTopToggle
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
  }
}
</script>

<style scoped>
.layout {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
}

.nav-container {
  width: 100%;
  margin-bottom: 20px;
}

.main-content {
  display: flex;
  gap: 20px;
}

.left-column {
  width: 20%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.center-column {
  width: 60%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.right-column {
  width: 20%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>