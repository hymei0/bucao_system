<!-- 头部组件栏组件 -->
<template>
  <div  style="height: 60px;border-bottom: 1px solid #2c3e50; display: flex;background-color: #334157" >
    <div style="width:300px;padding-left: 20px;padding-top:5px;font-weight: bold;color:cadetblue;font-size: x-large" >布草智能柜管理系统</div>
    <div style="flex:1"></div>
    <div style="width:100px;padding-top:20px">
      <el-dropdown>
       <span class="el-dropdown-link">
          登录/注册
         <el-icon class="el-icon--right">
           <arrow-down />
         </el-icon>
       </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')">退出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header"
}
</script>

<style scoped>

</style>
