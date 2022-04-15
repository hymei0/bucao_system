import { createStore } from 'vuex'
import Vue from 'vue'

// 实现组价之间的通信
export default createStore({
  // 变量状态
  state: {
    user: {}
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
    }
  },
  actions: {
    SET_USER({commit}, user) {
      this.state.user = user
    }
  },
  getters: {
    getUser: (state) => state.user
  },
  modules: {
  }
})
