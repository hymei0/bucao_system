<template>
  <div >
    <div>
    <el-skeleton />
    <br />
    <el-skeleton style="--el-skeleton-circle-size: 100px">
      <template #template>
        <el-skeleton-item variant="circle" />
      </template>
    </el-skeleton>

    </div>
    <div style="width: 50%;margin-left: 25%;margin-top: -10%"  >
      <div style="margin-top: 15%;width: 80%">
        <el-form ref="form" :model="form" label-width="100px" :rules="rules">
          <el-form-item label="原密码" prop="psd">
            <el-input v-model="form.psd" show-password></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPass">
            <el-input v-model="form.newPass" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="confirmPass">
            <el-input v-model="form.confirmPass" show-password></el-input>
          </el-form-item>
        </el-form>
        <div style="text-align: center">
          <el-button type="primary" @click="changePsss">保存</el-button>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Password",
  data() {
    return {
      user:{},
     form: {
       password: '',
       newPass: '',
       confirmPass: ''
     },
      rules: {
        psd: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        newPass: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
        ],
        confirmPass: [
          {required: true, message: '请输入确认新密码', trigger: 'blur'},
        ],
      },
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user_info") || "{}"
    this.user = JSON.parse(userStr)
    // 请求服务端，确认当前登录用户的 合法信息
    request.get("/ManagerInfo/" + this.user.id).then(res => {
      if(res.code === '1'){
        this.user = res.data
      }
    })

  },
  methods: {
    changePsss() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (!this.form.newPass === this.form.confirmPass) {
            this.$message.error('2次输入新密码必须一致')
            return
          }
          this.user.psd=JSON.parse(JSON.stringify(this.form.psd));
          request.put("/ManagerInfo", this.user).then(res => {
            if (res.code === '1') {
              this.$message.success('修改成功')
              this.$router.push("/login")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
