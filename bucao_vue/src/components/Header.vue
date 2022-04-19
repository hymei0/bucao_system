<!-- 头部组件栏组件 -->
<template>
  <div  style="height: 70px;border-bottom: 1px solid #2c3e50; display: flex;background-color: #334157" >
    <div  style="width:500px;padding-left: 20px;padding-top:5px;font-weight: bold;color:cadetblue;font-size: x-large" >
      <h4 class="title" style="display: flex" >
        <img src="../assets/img/logo.png" style="width: 60px;height: 60px" alt="">
        <span style="padding-top: 15px;padding-left: 10px">江南大学附属医院布草智能柜管理系统</span>
      </h4>
    </div>
    <div style="flex:1">
      <h4 style="padding-top: 35px;padding-left: 50px; color: aliceblue">欢迎{{user.pri}}</h4>
    </div>
    <div style="width:150px;padding-top:5px;display: flex">
      <h4 style="padding-top: 35px;color: aliceblue">{{user.mname}}</h4>
      <el-dropdown>
        <el-avatar :size="63" :src="user.portrait" style=" border-radius: 50%;" />
        <span class="el-dropdown-link">
         <el-icon class="right">
           <arrow-down/>
         </el-icon>
       </span>
        <template #dropdown>
          <el-dropdown-menu style="width: 100px">
            <el-dropdown-item @click="$router.push('/personM')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')" >退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Header",
  data(){
    return{
      user: {}
    }
  },
  created() {
    let str = sessionStorage.getItem("user_info") || "{}"
    //类型转换
    this.user = JSON.parse(str)

    //请求服务端，确认当前登录用户的 合法信息
          request.get("/ManagerInfo/"+ this.user.id).then(re=> {
            if (re.code === '1') {
              this.user = re.data
            }
          })

    if(this.user.portrait===null)
    //默认头像
    {
      this.user.portrait='https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  methods:{

  }

}
</script>

<style scoped>

</style>
