<template>
  <!--头部-->
  <Header/>
  <!--主体-->
  <div style="display: flex;margin-top: 0px" >
    <!--侧边栏-->
    <Aside/>
    <!--内容区-->
    <router-view style="flex: 1"></router-view>
  </div>
<!--  <div style="display: flex;margin-top: 0px" v-if="user.roles==='user'">-->
<!--    &lt;!&ndash;侧边栏&ndash;&gt;-->
<!--    <AsideU/>-->
<!--    &lt;!&ndash;内容区&ndash;&gt;-->
<!--    <router-view style="flex: 1"></router-view>-->
<!--  </div>-->
</template>

<script>
import Header from "@/components/Header";
import Aside from "@/components/Aside";

export default {
  name: "Layout",
  components:{
    Aside,
    Header
  },
  data(){
    return{
      user: {}
    }
  },
  created() {
    let str = sessionStorage.getItem("user_info") || "{}"
    //类型转换
    this.user = JSON.parse(str)

    // //请求服务端，确认当前登录用户的 合法信息
    // request.get("/User_info/" + this.user.id).then(res => {
    //   if (res.code === '1') {
    //     this.user = res.data
    //   } else {
    //     request.get("/ManagerInfo/" + this.user.id).then(re => {
    //       if (re.code === '1') {
    //         this.user = re.data
    //       }
    //     })
    //   }
    // })
  }
}
</script>

<style scoped>

</style>
