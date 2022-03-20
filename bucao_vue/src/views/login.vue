<template>
<div style="width: 100%; height: 100vh;overflow: hidden">
    <div style="width: 400px;margin: 150px auto">
      <div style="color: black;font-size: 30px;text-align: center;padding: 30px 0">欢迎登录</div>
      <el-form
          :model="form"
          status-icon
          :rules="rules"
          label-width="120px"
          class="demo-ruleForm"
      >
        <el-form-item label="账号" prop="id">
          <el-input prefix-icon="el-icon-user-solid" v-model.number="form.id" placeholder="身份证/手机号" />
        </el-form-item>

        <el-form-item label="密码" prop="psd">
          <el-input prefix-icon="el-icon-lock" v-model="form.psd" type="password" autocomplete="off" placeholder="请输入密码" show-password/>
        </el-form-item>


        <el-form-item>
          <el-button type="primary" @click="submitForm">登录</el-button>
          <el-button style="" @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
</div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "login",
  data(){
    return{
      form:{},
      rules :{
        psd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        id: [{ required: true, message: '请输入身份证号或手机号', trigger: 'blur' }],
      }
    }
  },
  methods:{
   submitForm(){
      console.log(this.form)
      request.post("/api/User_info/login",this.form).then(res=>
          {
            if(res.code==='1')
            {
              this.$message({
              type:"success",
              message:"登录成功"
              })
              //登录成功页面跳转，跳转到主页
               this.$router.push("/")
            }
            else
            {
              this.$message({
                type:"error",
                message:res.msg
              })
            }
          }
      ).catch(err=>{
        this.$message.error('登录失败，请稍后再试！')
      })
    },
    resetForm(){
         this.form={}
    }
  }
}
</script>

<style scoped>

</style>
