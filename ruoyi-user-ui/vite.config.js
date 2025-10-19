import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import os from 'os'

// 自动获取本机局域网 IP（过滤虚拟网卡、Docker、NAT 等）
function getLocalIP() {
  const interfaces = os.networkInterfaces()
  const virtualKeywords = ['vm', 'virtual', 'docker', 'loopback', 'hyper', 'nat', 'vbox']

  for (const name in interfaces) {
    if (virtualKeywords.some(keyword => name.toLowerCase().includes(keyword))) {
      continue // 忽略虚拟网卡
    }

    for (const iface of interfaces[name]) {
      if (
          iface.family === 'IPv4' &&
          !iface.internal /*&&
          iface.address.startsWith('192.168.') // 更保险地限制局域网 IP 段*/
      ) {
        return iface.address
      }
    }
  }

  return '127.0.0.1' // fallback
}

//const localIP = getLocalIP()
const localIP = 'localhost'
console.log(`✅ 当前自动获取的局域网 IP: ${localIP}`)

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  define: {
    'import.meta.env.VITE_BASE_URL': JSON.stringify(`http://${localIP}:8080`)
  },
  server: {
    host: '0.0.0.0',
    port: 80,
    proxy: {
      '/user': {
        target: `http://${localIP}:80`,
        changeOrigin: true,
        secure: false,
      },
      '/api': {
        target: `http://${localIP}:8080`,
        changeOrigin: true,
        secure: false,
      },
    },
  },
  build: {
    outDir: 'dist',
    assetsDir: 'assets',
  },
})


//部署环境
/*
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  define: {
    'import.meta.env.VITE_BASE_URL': JSON.stringify(`http://110.41.1.63:8080`)
  },
  server: {
    host: '0.0.0.0',
    port: 80,
    proxy: {
      '/user': {
        target: `http://110.41.1.63:80`,
        changeOrigin: true,
        secure: false,
      },
      '/api': {
        target: `http://110.41.1.63:8080`,
        changeOrigin: true,
        secure: false,
      },
    },
  },
  build: {
    outDir: 'user',
    assetsDir: 'assets',
  },
})
*/
