<!-- 头部组件栏组件 -->
<template>
  <div  style="height: 70px;border-bottom: 1px solid #2c3e50; display: flex;background-color: #334157" >
    <div  style="width:500px;padding-left: 20px;padding-top:5px;font-weight: bold;color:cadetblue;font-size: x-large" >
      <h4 class="title" style="display: flex" >
        <img src="../assets/img/logo.png" style="width: 60px;height: 60px" alt="">
        <span style="padding-top: 15px;padding-left: 10px">江南大学附属医院布草智能柜管理系统</span>
      </h4>
    </div>
    <div style="flex:1"></div>
    <div style="width:100px;padding-top:10px">
      <el-dropdown>
        <el-avatar :size="50" :src="circleUrl" />
        <span class="el-dropdown-link">
         <el-icon class="right">
           <arrow-down/>
         </el-icon>
         {{user.uname}}
       </span>
        <template #dropdown>
          <el-dropdown-menu style="width: 100px">
            <el-dropdown-item @click="$router.push(url1), target='_blank'">{{ info1 }}</el-dropdown-item>
            <el-dropdown-item @click="$router.push(url2)" >{{ info2 }}</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  data(){
    return{
      circleUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      info1:"登录",
      info2:"注册",
      url1:'',
      url2:'',
      user: {}
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){

      if(this.$store.state.person_info.id==='')
      {
        this.info1="登录"
        this.url1='/login'
        this.info2="注册"
        this.url2='/register'
      }
      else{
        this.info1="个人信息"
        this.url1='/person'
        this.info2="退出登录"
        this.url2='/login'

      }
    }
  }

}
</script>

<style scoped>

</style>
