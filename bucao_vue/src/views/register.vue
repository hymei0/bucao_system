<template>
  <div style="width: 100%; height: 100vh;overflow: hidden;display: flex">
    <div>
      <el-card class="top">
        <div  align="center">
          <img class="img" :src="imgArr[index]" alt="">
        </div>
        <h4 align="center">{{text[index]}}</h4>
      </el-card>
      <div id="left">
        <a href="javascript:void(0)" @click="left" v-show='index!=0'>
          <el-button class="left" plain :icon="direction1"/>
        </a>
      </div>
      <div id="right">
        <a href="javascript:void(0)" @click="right" v-show='index<imgArr.length-1'>
          <el-button class="right" plain :icon="direction2"/>
        </a>
      </div>
    </div>

    <div style="width:100%;margin-top: 120px;margin-left: 180px">
      <h1 class="title" style="display: flex;margin-left:20px" >
        <img src="../assets/img/logo.png" style="width: 60px;height: 60px" alt="">
        <span style="padding-top: 5px;padding-left: 10px">布草智能柜管理系统</span>
      </h1>

      <el-card style="width:90%;margin: 30px -80px">
        <h2 style="text-align: center;padding-bottom: 20px">注册</h2>
        <el-form
            :model="form"
            status-icon
            :rules="rules"
            label-width="120px"
            class="demo-ruleForm"
            style="padding-top: 0px"
        >
          <el-form-item label="姓   名" prop="uname"  >
            <el-input style="width: 80%" prefix-icon="document-checked" v-model="form.uname" placeholder="姓名" />
          </el-form-item>

          <el-form-item label="账   号  " prop="id"  >
            <el-input style="width: 80%" prefix-icon="credit-card" v-model="form.id" placeholder="身份证号" />
          </el-form-item>

          <el-form-item label="密   码   " prop="psd">
            <el-input  style="width: 80%" prefix-icon="lock" v-model="form.psd" type="password" autocomplete="off" placeholder="请输入密码" show-password/>
          </el-form-item>

          <el-form-item label="确认密码" prop="confirm"  >
            <el-input style="width: 80%" prefix-icon="lock" v-model="form.confirm" type="password" autocomplete="off" placeholder="再次输入密码" show-password/>
          </el-form-item>

          <el-form-item label="手 机 号" prop="telephone" >
            <el-input style="width: 80%" prefix-icon="cellphone" v-model="form.telephone" autocomplete="off" placeholder="请输入手机号" />
          </el-form-item>

          <el-form-item label="性 别" prop="sex">
            <el-radio-group  style="width: 80%" v-model="form.sex" autocomplete="off">
              <el-radio label="男" style="position: absolute;left: 10px"/>
              <el-radio label="女" style="position: absolute;right: 80px"/>
            </el-radio-group>
          </el-form-item>

          <el-form-item >
            <el-button type="primary" @click="submitForm" style="position: absolute;width: 100px;top:1px">注册</el-button>
            <el-button  @click="resetForm" style="position: absolute;right:80px;top:1px;width:100px">重置</el-button>
          </el-form-item>

        </el-form>
      </el-card>
    </div>

  </div>
</template>

<script >
import request from "@/utils/request";
import {ArrowLeft, ArrowRight} from '@element-plus/icons-vue'

export default {
  name: "register",
  data(){
    return{
      imgArr:[
        require("../assets/img/布草清点系统.png"),
        require("../assets/img/bucao1.png"),
        require("../assets/img/bucao2.png"),
        require( "../assets/img/布草洗涤柜.png"),
        require("../assets/img/rfid布草回收柜.png")
      ],
      text:[
        "智能布草清点管理系统",
        "智能布草入柜，整理",
        "布草智能柜应用",
        "RFID布草智能布草处理流程",
        "RFID智能布草回收柜"
      ],
      direction1:ArrowLeft,
      direction2:ArrowRight,
      index:0,
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
    },

    //向左切换图片
    left:function(){
      this.index--;
    },
    //向右切换图片
    right:function(){
      this.index++;
    }
  }
}
</script>

<style scoped>
.top{
  width: 600px;
  height: 580px;
  margin-left: 10%;
  margin-top: 10%;
}
.img{
  width: 500px;
  height: 520px;
}
.left{
  border: aliceblue;
  width: 20px;
  height: 60px;
}
.right{
  width: 20px;
  border: aliceblue;
  height: 60px;
}
#left{
  width: 45px;
  height: 45px;
  margin-left: 11%;
  margin-top: -50%;
}
#right{
  width: 45px;
  height: 45px;
  margin-left: 635px;
  margin-top: -6.55%;
}
</style>
