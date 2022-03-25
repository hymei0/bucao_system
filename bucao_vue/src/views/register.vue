<template>
  <div style="width: 100%; height: 100vh;overflow: hidden">
    <div style="width: 400px;margin: 90px 50px 40px 900px">
      <div style="font-weight: bold;font-size: 30px;text-align: center;padding: 50px 20px">注 册</div>
      <el-form
          :model="form"
          status-icon
          :rules="rules"
          label-width="120px"
          class="demo-ruleForm"
          style="padding-top: 0px"
          >
        <el-form-item label="姓   名" prop="uname"  >
          <el-input prefix-icon="document-checked" v-model="form.uname" placeholder="姓名" />
        </el-form-item>

        <el-form-item label="账   号  " prop="id"  >
          <el-input prefix-icon="credit-card" v-model="form.id" placeholder="身份证号" />
        </el-form-item>

        <el-form-item label="密   码   " prop="psd">
          <el-input prefix-icon="lock" v-model="form.psd" type="password" autocomplete="off" placeholder="请输入密码" show-password/>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirm"  >
          <el-input prefix-icon="lock" v-model="form.confirm" type="password" autocomplete="off" placeholder="再次输入密码" show-password/>
        </el-form-item>

        <el-form-item label="手 机 号" prop="telephone" >
          <el-input prefix-icon="cellphone" v-model="form.telephone" autocomplete="off" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="性 别" prop="sex">
          <el-radio-group v-model="form.sex" autocomplete="off">
            <el-radio label="男" style="position: absolute;left: 10px"/>
            <el-radio label="女" style="position: absolute;right: 10px"/>
          </el-radio-group>
        </el-form-item>

        <el-form-item >
          <el-button type="primary" @click="submitForm" style="position: absolute;width: 100px;top:1px">注册</el-button>
          <el-button  @click="resetForm" style="position: absolute;right:0px;top:1px;width:100px">重置</el-button>
        </el-form-item>

      </el-form>
    </div>
  </div>
</template>

<script >
import request from "@/utils/request";

export default {
  name: "register",
  data(){
    return{
      form:{},

      rules :{
        uname:[{required:true,message:"请输入姓名",trigger:'blur'}],
        psd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        id: [{ required: true, message: '请输入身份证号或手机号', trigger: 'blur' }],
        telephone:[{required:true,message:"请输入电话号码",trigger:'blur'}],
        confirm:[{required: true, message: '请确认密码', trigger: 'blur'}]
      }
    }
  },

  methods:{
    submitForm(){
      if(this.form.psd!==this.form.confirm)
      {
        this.$message({
          type:"error",
          message:'两次输入的密码不一致'
        })
        return
      }
      //  this.$refs['form'].validate((valid)=> {
      //    if (!valid) {
      //      this.$message({
      //        type:"error",
      //        message:"信息未完善"
      //      })
      //    }
      // })
      request.post("/api/User_info/register",this.form).then(res=>
          {
            if(res.code==='1')
            {
              this.$message({
                type:"success",
                message:"登录成功"
              })
              //登录成功页面跳转，跳转到登录页面
              this.$router.push("/login")
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
