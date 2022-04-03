import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../Layout/Layout.vue'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    // 重定向
    redirect:"/RFID_kinds",
    //嵌套路由
    children:[
      {
        path: 'RFID_kinds',
        name: 'RFID_kinds',
        component: ()=>import("@/views/RFIDkindsView"),
      },
      {
        path:'person',
        name:'person',
        component:()=>import("@/views/Person")
      },
      {
        path: 'bucao_info',
        name: 'Bucao_info',
        component: ()=>import("@/views/Bucao_info"),
      },
      {
        path: 'section',
        name: 'section',
        component: ()=>import("@/views/SectionView"),
      }
    ]

  },
  {
    path: '/login',
    name: 'login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../components/login.vue')
  },

  {
    path: '/register',
    name: 'register',

    component: () => import(/* webpackChunkName: "about" */ '../components/register.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
