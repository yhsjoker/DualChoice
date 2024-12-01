import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

// 配置 axios
axios.defaults.baseURL = 'http://localhost:8090'
axios.defaults.headers['Content-Type'] = 'application/json'
app.config.globalProperties.$http = axios

// 使用 Element Plus
app.use(ElementPlus)

// 使用路由
app.use(router)

// 挂载应用
app.mount('#app')
