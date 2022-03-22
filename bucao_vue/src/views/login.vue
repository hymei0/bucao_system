<template>
<div style="width: 100%; height: 100vh;overflow: hidden">
    <div style="width: 400px;margin: 90px 50px 40px 900px">
      <div style="font-weight: bold;font-size: 30px;text-align: center;padding: 50px 20px">登 录</div>
      <el-form
          :model="form"
          status-icon
          :rules="rules"
          label-width="120px"
          class="demo-ruleForm"
          style="padding-top: 20px">
        <el-form-item label="账 号" prop="id">
          <el-input prefix-icon="el-icon-user-solid" v-model.number="form.id" placeholder="身份证/手机号" />
        </el-form-item>

        <el-form-item label="密 码" prop="psd">
          <el-input prefix-icon="el-icon-lock" v-model="form.psd" type="password" autocomplete="off" placeholder="请输入密码" show-password/>
        </el-form-item>


        <el-form-item>
          <el-button type="primary" @click="submitForm" style="position: absolute;width: 100px;top:1px">注册</el-button>
          <el-button  @click="resetForm" style="position: absolute;right:0px;top:1px;width:100px">重置</el-button>
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
     // this.$refs['form'].validate((valid)=>{
     //   if(!valid)
     //   {
     //     this.$message({
     //       type:"error",
     //       message:"输入有误"
     //     })
     //   }
     // })
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
