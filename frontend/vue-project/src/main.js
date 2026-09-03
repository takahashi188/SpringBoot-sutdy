import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import '@/assets/styles/style.css'
import axios from 'axios';

// axiosのデフォルト設定
axios.defaults.withCredentials = true; // クッキーを含める設定

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
