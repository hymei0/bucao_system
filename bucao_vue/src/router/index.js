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
        component: ()=>import("@/views/HomeView"),
      }
    ]

  },
  {
    path: '/login',
    name: 'login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/login.vue')
  },
  {
    path: '/register',
    name: 'register',

    component: () => import(/* webpackChunkName: "about" */ '../views/register.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
