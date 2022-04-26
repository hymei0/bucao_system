<template>
  <!--头部-->
  <HeaderU/>
  <!--主体-->
  <div style="display: flex;margin-top: 0px" >
    <!--侧边栏-->
    <AsideU/>
    <!--内容区-->
    <router-view style="flex: 1"></router-view>
  </div>
</template>

<script>
import HeaderU from "@/components/HeaderU";
import AsideU from "@/components/AsideU";

export default {
  name: "LayoutU",
  components:{
    AsideU,
    HeaderU
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

  }
}
</script>

<style scoped>

</style>
