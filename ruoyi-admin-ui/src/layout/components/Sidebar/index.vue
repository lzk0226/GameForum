<template>
  <div class="glass-sidebar" :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar :class="settings.sideTheme" wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="'transparent'"
        :text-color="'#ffffff'"
        :unique-opened="true"
        :active-text-color="'#ffffff'"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="(route, index) in sidebarRouters"
          :key="route.path  + index"
          :item="route"
          :base-path="route.path"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex"
import Logo from "./Logo"
import SidebarItem from "./SidebarItem"
import variables from "@/assets/styles/variables.scss"

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["sidebarRouters", "sidebar"]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>

<style lang="scss" scoped>
.glass-sidebar {
  position: relative;
  height: 100%;

  // 磨砂玻璃背景
  background: linear-gradient(135deg,
    rgba(255, 140, 0, 0.15) 0%,      // 橙色起点
    rgba(255, 100, 100, 0.12) 25%,   // 橙红过渡
    rgba(100, 150, 255, 0.12) 75%,   // 蓝紫过渡
    rgba(30, 144, 255, 0.15) 100%    // 蓝色终点
  );

  // 毛玻璃效果
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);

  // 边框光晕
  border-right: 1px solid rgba(255, 255, 255, 0.18);
  box-shadow: 4px 0 24px rgba(0, 0, 0, 0.1);

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg,
      rgba(255, 140, 0, 0.03) 0%,
      rgba(30, 144, 255, 0.03) 100%
    );
    pointer-events: none;
    z-index: 0;
  }

  // 菜单样式
  ::v-deep .el-menu {
    border-right: none;
    background: transparent !important;
    position: relative;
    z-index: 1;

    .el-menu-item,
    .el-submenu__title {
      color: #ffffff !important;
      background: transparent !important;
      position: relative;
      margin: 4px 8px;
      border-radius: 10px;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

      &:hover {
        background: rgba(255, 255, 255, 0.15) !important;
        backdrop-filter: blur(10px);
        transform: translateX(4px);
        box-shadow: 0 4px 12px rgba(255, 140, 0, 0.2);
      }
    }

    .el-menu-item.is-active {
      background: linear-gradient(90deg,
        rgba(255, 140, 0, 0.25) 0%,
        rgba(30, 144, 255, 0.25) 100%
      ) !important;
      backdrop-filter: blur(10px);
      color: #ffffff !important;
      font-weight: 600;
      box-shadow: 0 4px 16px rgba(255, 140, 0, 0.3),
      inset 0 1px 0 rgba(255, 255, 255, 0.3);
      border-left: 3px solid rgba(255, 140, 0, 0.8);

      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 4px;
        height: 60%;
        background: linear-gradient(180deg, #ff8c00, #1e90ff);
        border-radius: 0 4px 4px 0;
        box-shadow: 0 0 10px rgba(255, 140, 0, 0.5);
      }
    }

    .el-submenu.is-active > .el-submenu__title {
      background: rgba(255, 255, 255, 0.12) !important;
      color: #ffffff !important;
    }
  }

  // 滚动条样式
  ::v-deep .el-scrollbar {
    .scrollbar-wrapper {
      overflow-x: hidden !important;
    }

    .el-scrollbar__thumb {
      background: rgba(255, 255, 255, 0.3) !important;
      border-radius: 4px;

      &:hover {
        background: rgba(255, 255, 255, 0.5) !important;
      }
    }
  }

  // Logo区域适配
  &.has-logo {
    ::v-deep .logo-container {
      background: transparent !important;
      backdrop-filter: blur(10px);
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
  }
}

// 折叠状态优化
.glass-sidebar ::v-deep .el-menu--collapse {
  .el-menu-item,
  .el-submenu__title {
    margin: 4px auto;
    width: calc(100% - 16px);
  }
}

// 响应式调整
@media screen and (max-width: 768px) {
  .glass-sidebar {
    backdrop-filter: blur(15px);
  }
}
</style>
