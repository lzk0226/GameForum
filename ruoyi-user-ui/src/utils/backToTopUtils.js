// 回到顶部和主题切换功能的工具函数

/**
 * 处理滚动事件，判断是否在页面顶部
 * @returns {boolean} 是否在页面顶部
 */
export function isPageAtTop() {
    return window.scrollY === 0;
}

/**
 * 平滑滚动到页面顶部
 */
export function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

/**
 * 智能返回上一步功能
 * 如果有历史记录则返回上一步，否则关闭窗口/标签页
 */
export function goBack() {
    // 检查是否有历史记录可以返回
    if (window.history.length > 1) {
        window.history.back();
    } else {
        // 如果没有历史记录，尝试关闭窗口
        // 注意：现代浏览器出于安全考虑，只允许关闭由脚本打开的窗口
        try {
            window.close();
        } catch (error) {
            // 如果无法关闭窗口，可以导航到一个默认页面
            console.warn('无法关闭窗口，将导航到首页');
            window.location.href = '/';
        }
    }
}

/**
 * 检查当前页面是否可以返回
 * @returns {boolean} 是否可以返回上一步
 */
export function canGoBack() {
    return window.history.length > 1;
}

/**
 * 检查当前窗口是否是通过脚本打开的
 * @returns {boolean} 是否可以关闭窗口
 */
export function canCloseWindow() {
    try {
        // 尝试访问 window.opener 来判断是否是弹出窗口
        return window.opener !== null;
    } catch (error) {
        return false;
    }
}

/**
 * 切换主题
 * @param {string} currentTheme 当前主题 ('light' 或 'dark')
 * @returns {string} 切换后的主题
 */
export function toggleTheme(currentTheme) {
    return currentTheme === 'light' ? 'dark' : 'light';
}

/**
 * 应用主题样式
 * @param {string} theme 主题名称 ('light' 或 'dark')
 */
export function applyTheme(theme) {
    if (theme === 'dark') {
        document.documentElement.style.setProperty('--btn-bg', '#222');
        document.documentElement.style.setProperty('--btn-color', '#ffd');
        document.documentElement.style.setProperty('--btn-hover-bg', '#444');
        document.documentElement.style.backgroundColor = '#121212';
        document.documentElement.style.color = '#eee';
    } else {
        document.documentElement.style.setProperty('--btn-bg', '#444');
        document.documentElement.style.setProperty('--btn-color', '#fff');
        document.documentElement.style.setProperty('--btn-hover-bg', '#666');
        document.documentElement.style.backgroundColor = '#fff';
        document.documentElement.style.color = '#000';
    }
}

/**
 * 创建滚动监听器
 * @param {Function} callback 滚动事件回调函数
 * @returns {Function} 返回清理函数
 */
export function createScrollListener(callback) {
    const handleScroll = () => {
        callback(isPageAtTop());
    };

    window.addEventListener('scroll', handleScroll);

    // 返回清理函数
    return () => {
        window.removeEventListener('scroll', handleScroll);
    };
}

/**
 * 主题管理器类
 */
export class ThemeManager {
    constructor(initialTheme = 'light') {
        this.theme = initialTheme;
        this.applyTheme();
    }

    toggle() {
        this.theme = toggleTheme(this.theme);
        this.applyTheme();
        return this.theme;
    }

    applyTheme() {
        applyTheme(this.theme);
    }

    getCurrentTheme() {
        return this.theme;
    }

    setTheme(theme) {
        this.theme = theme;
        this.applyTheme();
    }
}

/**
 * 导航管理器类
 * 提供统一的导航功能管理
 */
export class NavigationManager {
    constructor() {
        this.canGoBack = this.checkCanGoBack();
        this.canClose = this.checkCanClose();
    }

    checkCanGoBack() {
        return window.history.length > 1;
    }

    checkCanClose() {
        return canCloseWindow();
    }

    /**
     * 执行返回操作
     * @param {string} fallbackUrl 无法返回时的备用URL
     */
    goBack(fallbackUrl = '/') {
        if (this.canGoBack) {
            window.history.back();
        } else if (this.canClose) {
            window.close();
        } else {
            window.location.href = fallbackUrl;
        }
    }

    /**
     * 获取返回按钮的提示文本
     * @returns {string} 提示文本
     */
    getBackButtonTitle() {
        if (this.canGoBack) {
            return '返回上一页';
        } else if (this.canClose) {
            return '关闭窗口';
        } else {
            return '返回首页';
        }
    }
}