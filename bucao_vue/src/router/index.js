import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../Layout/Layout.vue'
import LayoutU from '../Layout/LayoutU.vue'

const routes = [
  //*************************** 面向管理员的主页   *************************
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
        path:'/personM',
        name:'personM',
        component:()=>import("@/views/managerviews/PersonM")
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
        path: 'statistics',
        name: 'statistics',
        component: ()=>import("@/charts/statistics.vue"),
      },
      {
        path: 'datavisual',
        name: 'datavisual',
        component: ()=>import("@/charts/Datavisual.vue"),
      },
      {
        path: 'user_room',
        name: 'user_room',
        component: ()=>import("@/views/User_room.vue"),
      },
      {
        path: '/manager',
        name: 'Manager',
        component: ()=>import("@/views/managerviews/Manager.vue"),
      },
      {
        path: 'personM',
        name: 'PersonM',
        component: ()=>import("@/views/managerviews/PersonM.vue"),
      },
      {
        path: 'developer',
        name: 'developer',
        component: ()=>import("@/views/managerviews/developer.vue"),
      }
    ]

  },
    //*************************** 面向用户的主页 *************************
  {
    path: '/LayoutU',
    name: 'LayoutU',
    component: LayoutU,
    // 重定向
    redirect:"/homeU",
    //嵌套路由
    children:[
      {
        path: '/homeU',
        name: 'HomeU',
        component: ()=>import("@/views/userviews/HomeU"),
      },
      {
        path: '/statisticsU',
        name: 'statisticsU',
        component: ()=>import("@/charts/statisticsU.vue"),
      },

      {
        path: '/User_roomU',
        name: 'User_roomU',
        component: ()=>import("@/views/userviews/User_roomU"),
      },
      {
        path: '/managerU',
        name: 'ManagerU',
        component: ()=>import("@/views/userviews/ManagerU.vue"),
      },
      {
        path: '/MyOrder',
        name: 'MyOrder',
        component: ()=>import("@/views/userviews/MyOrder"),
      },
      {
        path: '/Mybucao',
        name: 'Mybucao',
        component: ()=>import("@/views/userviews/Mybucao"),
      },
      {
        path: '/bucao_infoU',
        name: 'Bucao_infoU',
        component: ()=>import("@/views/userviews/Bucao_infoU"),
      },
      {
        path: '/RFID_kindsU',
        name: 'RFID_kindsU',
        component: ()=>import("@/views/userviews/RFIDkindsViewU"),
      },
      {
        path: '/Room_infoU',
        name: 'Room_infoU',
        component: ()=>import("@/views/userviews/Room_infoU"),
      },
      {
        path: '/SectionU',
        name: 'SectionU',
        component: ()=>import("@/views/userviews/SectionViewU"),
      },
      {
        path:'/person',
        name:'person',
        component:()=>import("@/views/userviews/Person")
      },
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
