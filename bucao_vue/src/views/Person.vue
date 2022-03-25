<template>
  <div >


    <el-card style="width:50%;margin: 120px 300px">
      <h2 style="text-align: center;padding-bottom: 50px">个人信息</h2>
      <el-form ref="form" :model="form" label-width="80px" :label-position="labelPosition" style="width: 60%;margin-left: 90px" align="center">
<!--        <el-form-item label="头像" prop="photo">-->
<!--          <el-input v-model="form.uname" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="姓 名" prop="uname">
          <el-input v-model="form.uname" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="账 号" prop="ID">
          <el-input v-model="form.ID" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地 址" prop="address">
          <el-input v-model="form.address" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="电 话" prop="telephone">
          <el-input v-model="form.telephone"></el-input>
        </el-form-item>
        <el-form-item label="住院天数" prop="days">
          <el-input v-model.number="form.days"></el-input>
        </el-form-item>
        <el-form-item label="应缴费用" prop="expenses" :precision="2">
          <el-input v-model="form.expenses" type="digit"></el-input>
        </el-form-item>
        <el-form-item style="display: flex" >
          <div style="width: 100%" align="center">
            <el-button type="success" :icon="ico" circle style="width: 40px"/>
            <el-button type="success" >保存</el-button>
            <el-button type="info"  style="margin-left: 30px">舍弃</el-button>
          </div>
        </el-form-item>

      </el-form>
    </el-card>

  </div>
</template>

<script>

// 导入图标
import {
  Edit,
} from '@element-plus/icons-vue'

import {ref} from "vue";


export default {
  name: "Person",
  data(){
    return{
      form:{},
      ico:Edit,  // 编辑按钮的图标
      labelPosition:"left"
    }
  }
}

</script>

<style scoped>

</style>
