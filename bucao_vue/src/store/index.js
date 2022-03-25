import { createStore } from 'vuex'
import Vue from 'vue'

// 实现组价之间的通信
export default createStore({
  // 变量状态
  state: {
    person_info:{
      id:'',
      psd:''
    }

  },
  //
  getters: {
  },
  //
  mutations: {
    UpdataPersonInfo(person_info,msg)
    {
      state.person_info=msg
    }
  },
  //
  actions: {
  },
  //
  modules: {
  }
})
