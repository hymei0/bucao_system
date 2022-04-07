import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../Layout/Layout.vue'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    // 重定向
    redirect:"/home",
    //嵌套路由
    children:[
      {
        path: 'home',
        name: 'Home',
        component: ()=>import("@/views/Home"),
      },
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
      },
      {
        path: 'user_info',
        name: 'User_info',
        component: ()=>import("@/views/User_info"),
      },
      {
        path: 'order',
        name: 'Order',
        component: ()=>import("@/views/Order"),
      },
      {
        path: 'bucao_user',
        name: 'Bucao_user',
        component: ()=>import("@/views/Bucao_user"),
      },
      {
        path: 'bucao_room',
        name: 'Bucao_room',
        component: ()=>import("@/views/Bucao_room"),
      },
      {
        path: 'room_info',
        name: 'Room_info',
        component: ()=>import("@/views/Room_info"),
      },
      {
        path: 'manager',
        name: 'Manager',
        component: ()=>import("@/views/managerviews/Manager.vue"),
      }
    ]

  },
  {
    path: '/login',
    name: 'login',
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
