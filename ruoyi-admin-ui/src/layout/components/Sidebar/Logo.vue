<!-- index.vue -->
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
    rgba(255, 140, 0, 0.15) 0%,
    rgba(255, 100, 100, 0.12) 25%,
    rgba(100, 150, 255, 0.12) 75%,
    rgba(30, 144, 255, 0.15) 100%
  );

  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
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

    // 子菜单弹出层样式
    .el-menu--popup {
      background: linear-gradient(135deg,
        rgba(255, 140, 0, 0.95) 0%,
        rgba(30, 144, 255, 0.95) 100%
      ) !important;
      backdrop-filter: blur(20px);
      border: 1px solid rgba(255, 255, 255, 0.2);
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
      border-radius: 8px;
      padding: 4px;

      .el-menu-item {
        background: transparent !important;
        color: #ffffff !important;

        &:hover {
          background: rgba(255, 255, 255, 0.2) !important;
        }
      }
    }
  }

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
}

.glass-sidebar ::v-deep .el-menu--collapse {
  .el-menu-item,
  .el-submenu__title {
    margin: 4px auto;
    width: calc(100% - 16px);
  }
}
</style>

<!-- ============================================ -->
<!-- Logo.vue -->
<template>
  <div class="glass-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title">{{ title }} </h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 class="sidebar-title">{{ title }} </h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.png'
import variables from '@/assets/styles/variables.scss'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    variables() {
      return variables
    },
    sideTheme() {
      return this.$store.state.settings.sideTheme
    }
  },
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      logo: logoImg
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.glass-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  text-align: center;
  overflow: hidden;
  border-bottom: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    & .sidebar-logo {
      width: 32px;
      height: 32px;
      vertical-align: middle;
      margin-right: 12px;
      filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
      transition: transform 0.3s ease;

      &:hover {
        transform: scale(1.1);
      }
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #ffffff;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
      letter-spacing: 0.5px;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>
