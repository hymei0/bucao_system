<template>
<div style="width: 100%; height: 100vh;display: flex;overflow: hidden">
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
  <div>
    <div style="width:85%;margin: 100px 210px">
      <h1 class="title" style="display: flex" >
        <img src="../assets/img/logo.png" style="width: 60px;height: 60px" alt="">
        <span style="padding-top: 5px;padding-left: 10px">布草智能柜管理系统</span>
      </h1>

      <el-card style="width:85%;margin: 70px -100px">
        <h2 style="text-align: center;padding-bottom: 20px">登 录</h2>
        <div align="center">
          <el-radio-group v-model="form.roles">
            <el-radio label="user" style="font-size: large">普通部门</el-radio>
            <el-radio label="manager" style="font-size: large">管 理 员</el-radio>
          </el-radio-group>
        </div>
        <el-form
            :model="form"
            status-icon
            :rules="rules"
            label-width="100px"
            class="demo-ruleForm"
            style="padding-top: 20px">

          <el-form-item label="账 号" prop="id">
            <el-input prefix-icon="avatar" v-model="form.id" placeholder="身份证/手机号" style="width: 80%" />
          </el-form-item>

          <el-form-item label="密 码" prop="psd">
            <el-input style="width: 80%" prefix-icon="Lock" v-model="form.psd" type="password" autocomplete="off" placeholder="请输入密码" show-password/>
          </el-form-item>
          <el-form-item label="验证码" prop="validCode">
            <div style="display: flex" >
              <el-input prefix-icon="key" v-model="form.validCode" style="width: 80%;" placeholder="请输入验证码" autocomplete="off"></el-input>
              <ValidCode @input="createValidCode" />
            </div>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm" style="position: absolute;width: 100px;top:1px">登录</el-button>
            <el-button  @click="resetForm" style="position: absolute;right:90px;top:1px;width:100px">重置</el-button>
            <el-button type="text" @click="$router.push('/register') " style="position: absolute;right:0px;top:5px;width:60px" v-if="form.roles==='user'">前往注册</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</div>
</template>

<script>
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode"
import {ArrowLeft, ArrowRight} from '@element-plus/icons-vue'
import {activeRouter} from "@/utils/permission";


export default {
  name: "login",
  components: {
    ValidCode,
  },
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
      form:{role:'user'},//默认为普通部门登录
      validCode:'',//存放自动生成的验证码
      rules :{
        psd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        id: [{ required: true, message: '请输入身份证号或手机号', trigger: 'blur' }],
        validCode:[{required: true, message: '请输入验证码', trigger: 'blur' }],
        roles: [{ required: true, message: '请选择角色', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    //在登入页删除部门信息
    sessionStorage.removeItem("user_info")
    //跟据客户屏幕比例，自动适应
    window.onresize = () => {
      const windowWidth = document.body.clientWidth
      const windowHeight = document.body.clientHeight
      //屏幕高宽比
      const windowAspectRatio = windowHeight / windowWidth
      let videoWidth
      let videoHeight
      if (windowAspectRatio < 0.5625) {
        videoWidth = windowWidth
        videoHeight = videoWidth * 0.5625
        this.fixStyle = {
          height: windowWidth * 0.5625 + 'px',
          width: windowWidth + 'px',
          'margin-bottom': (windowHeight - videoHeight) / 2 + 'px',
          'margin-left': 'initial'
        }
      } else {
        videoHeight = windowHeight
        videoWidth = videoHeight / 0.5625
        this.fixStyle = {
          height: windowHeight + 'px',
          width: windowHeight / 0.5625 + 'px',
          'margin-left': (windowWidth - videoWidth) / 2 + 'px',
          'margin-bottom': 'initial'
        }
      }
    }
    window.onresize()
  },
  methods:{
    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },
    submitForm(){
      // this.$refs[this.form].validate((valid)=>{
      //   if(!valid)
      //    {
      //      this.$message({
      //        type:"error",
      //        message:"输入有误"
      //      })
      //    }
      //  })
     if (!this.form.validCode) {
       this.$message.error("请填写验证码")
       return
     }
      if (!this.form.roles) {
        this.$message.error("请选择角色")
        return
      }
     if(this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
       this.$message.error("验证码错误")
       return
     }
     console.log(this.form)
      if(this.form.roles==='user') {
        request.post("/User_info/login", this.form).then(res => {
              if (res.code === '1') {
                sessionStorage.setItem("user_info", JSON.stringify(res.data))
                //登录成功页面跳转，跳转到主页
                this.$router.push("/LayoutU")
                this.$message({
                  type: "success",
                  message: "登录成功",
                })

              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            }
        ).catch(err => {
          this.$message.error('登录失败，请稍后再试！')
        })
      }else{
        request.post("/ManagerInfo/login", this.form).then(res => {
              if (res.code === '1') {
                sessionStorage.setItem("user_info", JSON.stringify(res.data))
                //登录成功页面跳转，跳转到主页
                this.$router.push("/")

                this.$message({
                  type: "success",
                  message: "登录成功",
                })

              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            }
        ).catch(err => {
          this.$message.error('登录失败，请稍后再试！')
        })

      }
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
  height: 570px;
  margin-left: 15%;
  margin-top: 10%;
}
.img{
  width: 500px;
  height: 500px;
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
  margin-left: 16%;
  margin-top: -45%;
}
#right{
  width: 45px;
  height: 45px;
  margin-left: 670px;
  margin-top: -6.45%;
}

</style>
