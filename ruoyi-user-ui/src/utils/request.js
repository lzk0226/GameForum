import axios from 'axios'

const service = axios.create({
    baseURL: '/api', // 配置你的代理前缀
    timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(config => {
    // 可以在这里添加 token
    return config
}, error => {
    return Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(response => {
    return response.data
}, error => {
    return Promise.reject(error)
})

export default service
