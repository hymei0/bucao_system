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
</template>

<script>
import Header from "@/components/Header";
import Aside from "@/components/Aside";

export default {
  name: "Layout",
  components:{
    Aside,
    Header
  }
}
</script>

<style scoped>

</style>
