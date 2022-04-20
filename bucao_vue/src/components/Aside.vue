 <!-- 侧边栏组件 -->
<template style="background-color: #334157;margin-top: 0px;">
  <div >

    <el-menu
        style="width: 200px;min-height:calc(100vh - 50px);border-right: 0px"
        :default-active="path"
        router
        class="el-menu-vertical-demo"
        background-color="#334157" text-color="#fff" active-text-color="#ffd04b"
    >
      <el-menu-item index="/home">
        <template #title>
          <el-icon><home-filled /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >主页</span>
        </template>
      </el-menu-item>

      <el-sub-menu index="/chart">
        <template #title>
          <el-icon><trend-charts /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >图表</span>
        </template>
        <el-menu-item index="/statistics" >
          <template #title>
            <el-icon><pointer /></el-icon>
            <span style="font-size: small;font-weight: bold;" >数据可视化</span>
          </template>
        </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="users">
        <template #title>
          <el-icon><user-filled /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >用户管理</span>
        </template>
        <el-menu-item index="/user_info" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><user /></el-icon>
            <span style="font-size: small;font-weight: bold;" >用户信息</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/user_room" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><rank /></el-icon>
            <span style="font-size: small;font-weight: bold;" >住院信息</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/order" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><shopping-cart-full /></el-icon>
            <span style="font-size: small;font-weight: bold;" >订单管理</span>
          </template>
        </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="bucao">
        <template #title>
          <el-icon><cherry /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >布草管理</span>
        </template>
        <el-menu-item index="/bucao_info" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><document /></el-icon>
            <span style="font-size: small;font-weight: bold;" >布草基本信息</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/bucao_user" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><success-filled /></el-icon>
            <span style="font-size: small;font-weight: bold;" >布草-用户信息</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/bucao_room" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><toilet-paper /></el-icon>
            <span style="font-size: small;font-weight: bold;" >布草-病房信息</span>
          </template>
        </el-menu-item>
      </el-sub-menu>


      <el-sub-menu index="system-data">
        <template #title>
          <el-icon><location /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >系统管理</span>
        </template>

        <el-menu-item index="/RFID_kinds" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><document /></el-icon>
            <span style="font-size: small;font-weight: bold;" > RFID标签分类</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/room_info" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><histogram /></el-icon>
            <span style="font-size: small;font-weight: bold;" >病房信息管理</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/section" style="padding-left: 60px; font-size:small">
          <template #title>
            <el-icon><compass /></el-icon>
            <span style="font-size: small;font-weight: bold;" >部门信息管理</span>
          </template>
        </el-menu-item>
      </el-sub-menu>

      <el-menu-item index="/manager">
        <template #title>
          <el-icon><avatar /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >管理员信息</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/developer">
        <template #title>
          <el-icon><phone /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >联系开发人员</span>
        </template>
      </el-menu-item>

    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return {
      path:this.$route.path
    }
  },
  created() {

  }
}
</script>

<style scoped>

</style>
