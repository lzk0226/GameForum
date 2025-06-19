/*import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

createApp(App)
    .use(router)
    .mount('#app')*/

// main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import noCopyPlugin from './plugins/noCopyPlugin.js'
import './assets/style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


createApp(App)
    .use(router)
    .use(ElementPlus)
    .use(noCopyPlugin, {
        enabled: true,                    // 是否启用防复制功能
        preventRightClick: false,          // 是否禁用右键菜单
        preventKeyboardShortcuts: false,   // 是否禁用键盘快捷键
        preventSelection: true,           // 是否禁用文本选择
        preventDrag: true                 // 是否禁用拖拽
    })
    .mount('#app')
