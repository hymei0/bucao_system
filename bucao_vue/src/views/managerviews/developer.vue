
<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">联系开发人员</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索，切换 -->
    <el-row :gutter="13">
      <el-col :span="18">
        <el-divider></el-divider>
      </el-col>
      <el-col :span="3">
      </el-col>
    </el-row>
  <div style="display: flex">
    <el-card style="width: 35%; margin-top: 10%;margin-left:10%; height: 30%">
      <el-form ref="form"  label-width="100px">
        <el-form-item label="后端开发">
          <a>何元梅</a>
        </el-form-item>
        <el-form-item label="联系电话">
          <a>13384785347</a>
        </el-form-item>
        <el-form-item label="邮箱地址">
          <a>1033180311@stu.jiangnan.edu.cn</a>
        </el-form-item>
      </el-form>

    </el-card>
    <el-card style="width: 35%; margin-left: 10%; ;margin-top:10%;height: 30%">
      <el-form ref="form"  label-width="100px">
        <el-form-item label="前端开发">
          <a>吴宇韬</a>
        </el-form-item>
        <el-form-item label="联系电话">
          <a>17621539812</a>
        </el-form-item>
        <el-form-item label="邮箱地址">
          <a>1033180333@stu.jiangnan.edu.cn</a>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
    <div style="width:40%;margin-left: 25%;margin-top: 10%">
      <p align="center"><el-icon><bell-filled /></el-icon> 系统在使用过程中出现任何问题请及时联系我们</p>
      <p align="center">@Copyright 何元梅 吴宇韬</p>
    </div>
  </div>
</template>

<script>


export default {
  name: "developer"
}
</script>

<style>

</style>
