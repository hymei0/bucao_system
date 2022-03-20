import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn' //国际化，是表格英文转化为中文





createApp(App).use(store).use(router).use(ElementPlus, {locale}).mount('#app').component(
    // the registered name
    'MyComponent',
    // the implementation
    {
        /* ... */
    }
)
Vue.use(ElementUI);
