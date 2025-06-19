// plugins/noCopyPlugin.js
export default {
    install(app, options = {}) {
        const config = {
            enabled: true,
            preventRightClick: true,
            preventKeyboardShortcuts: true,
            preventSelection: true,
            preventDrag: true,
            ...options
        }

        // 创建全局遮罩
        const createOverlay = () => {
            if (document.getElementById('global-no-copy-overlay')) return

            const overlay = document.createElement('div')
            overlay.id = 'global-no-copy-overlay'
            overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        z-index: 999999;
        pointer-events: none;
        user-select: none;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        background: transparent;
      `

            // 事件监听
            if (config.preventSelection) {
                overlay.addEventListener('selectstart', (e) => e.preventDefault())
            }

            if (config.preventDrag) {
                overlay.addEventListener('dragstart', (e) => e.preventDefault())
            }

            if (config.preventRightClick) {
                overlay.addEventListener('contextmenu', (e) => e.preventDefault())
            }

            document.body.appendChild(overlay)
        }

        // 键盘事件监听
        const handleKeydown = (event) => {
            if (!config.preventKeyboardShortcuts) return

            // 防止复制、剪切、全选、保存等快捷键
            if (event.ctrlKey || event.metaKey) {
                const preventKeys = ['c', 'x', 'a', 's', 'p', 'u', 'i']
                if (preventKeys.includes(event.key.toLowerCase())) {
                    event.preventDefault()
                    return false
                }
            }

            // 防止F12开发者工具
            if (event.key === 'F12') {
                event.preventDefault()
                return false
            }

            // 防止Ctrl+Shift+I (开发者工具)
            if (event.ctrlKey && event.shiftKey && event.key === 'I') {
                event.preventDefault()
                return false
            }

            // 防止Ctrl+Shift+J (控制台)
            if (event.ctrlKey && event.shiftKey && event.key === 'J') {
                event.preventDefault()
                return false
            }

            // 防止Ctrl+Shift+C (元素检查器)
            if (event.ctrlKey && event.shiftKey && event.key === 'C') {
                event.preventDefault()
                return false
            }
        }

        // 鼠标事件监听
        const handleMousedown = (event) => {
            // 防止右键
            if (config.preventRightClick && event.button === 2) {
                event.preventDefault()
                return false
            }
        }

        // 初始化防复制功能
        const initNoCopy = () => {
            if (!config.enabled) return

            // 添加全局CSS样式
            const style = document.createElement('style')
            style.textContent = `
        * {
          -webkit-user-select: none !important;
          -moz-user-select: none !important;
          -ms-user-select: none !important;
          user-select: none !important;
        }
        
        /* 允许输入框和可编辑内容选择 */
        input, textarea, [contenteditable="true"] {
          -webkit-user-select: text !important;
          -moz-user-select: text !important;
          -ms-user-select: text !important;
          user-select: text !important;
        }
      `
            document.head.appendChild(style)

            // 创建遮罩
            createOverlay()

            // 添加事件监听
            document.addEventListener('keydown', handleKeydown, true)
            document.addEventListener('mousedown', handleMousedown, true)
            document.addEventListener('selectstart', (e) => {
                if (config.preventSelection && !e.target.matches('input, textarea, [contenteditable="true"]')) {
                    e.preventDefault()
                }
            })
            document.addEventListener('contextmenu', (e) => {
                if (config.preventRightClick) {
                    e.preventDefault()
                }
            })
            document.addEventListener('dragstart', (e) => {
                if (config.preventDrag) {
                    e.preventDefault()
                }
            })

            // 禁用图片拖拽保存
            document.addEventListener('dragstart', (e) => {
                if (e.target.tagName === 'IMG') {
                    e.preventDefault()
                }
            })
        }

        // 移除防复制功能
        const removeNoCopy = () => {
            const overlay = document.getElementById('global-no-copy-overlay')
            if (overlay) {
                overlay.remove()
            }

            document.removeEventListener('keydown', handleKeydown, true)
            document.removeEventListener('mousedown', handleMousedown, true)
        }

        // 提供全局方法
        app.config.globalProperties.$noCopy = {
            enable: () => {
                config.enabled = true
                initNoCopy()
            },
            disable: () => {
                config.enabled = false
                removeNoCopy()
            },
            toggle: () => {
                config.enabled = !config.enabled
                if (config.enabled) {
                    initNoCopy()
                } else {
                    removeNoCopy()
                }
            }
        }

        // 页面加载完成后初始化
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', initNoCopy)
        } else {
            initNoCopy()
        }
    }
}