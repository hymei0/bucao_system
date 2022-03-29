import { createStore } from 'vuex'
import Vue from 'vue'

// 实现组价之间的通信
export default createStore({
  // 变量状态
  state: {
    person_info:{
      id:'',
      psd:'',
      rule:'' // ‘u’表示普通用户，‘m’表示管理员
    },
    currentPathName: ''


  },
  //
  getters: {
  },
  //
  mutations: {
    UpdataPersonInfo(person_info,uform)
    {
      this.state.person_info=uform
    },
    setPath (state) {
      state.currentPathName = localStorage.getItem("currentPathName")
    },
  },
  //
  actions: {
  },
  //
  modules: {
  }
})
