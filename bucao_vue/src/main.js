import Vue, { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn' //国际化，是表格英文转化为中文
import Vuex from 'vuex'


import * as icons from '@element-plus/icons'

const app = createApp(App)
app.use(store).use(router).use(Vuex).use(ElementPlus, {locale}).mount('#app')

// 统一注册Icon图标
Object.keys(icons).forEach(key => {
    app.component(key, icons[key])
})

//Vue.use(ElementUI);


