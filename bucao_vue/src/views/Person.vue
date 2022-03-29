<template>
  <div >


    <el-card style="width:50%;margin: 100px 300px">
      <h2 style="text-align: center;padding-bottom: 40px">个人信息</h2>
      <el-form ref="form" :model="form" label-width="80px" :label-position="labelPosition" style="width: 65%;margin-left: 90px" align="center">
<!--        <el-form-item label="头像" prop="photo">-->
<!--          <el-input v-model="form.uname" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="姓 名" prop="uname">
          <el-input v-model="form.uname" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="账 号" prop="id">
          <el-input v-model="form.id" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地 址" prop="address">
          <el-input v-model="form.address" autocomplete="off" v-bind:disabled='!edi' ></el-input>
        </el-form-item>
        <el-form-item label="电 话" prop="telephone">
          <el-input v-model="form.telephone" v-bind:disabled='!edi'></el-input>
        </el-form-item>
        <el-form-item label="住院天数" prop="days">
          <el-input v-model.number="form.days" v-bind:disabled='!edi'></el-input>
        </el-form-item>
        <el-form-item label="应缴费用" prop="expenses" :precision="2">
          <el-input v-model="form.expenses" type="digit" v-bind:disabled='!edi'></el-input>
        </el-form-item>
        <el-form-item style="display: flex" >
          <div style="width: 100%" align="center">
            <el-button type="success" :icon="ico" circle style="width: 40px" v-bind:disabled='edi' @click="update"/>
            <el-button type="success" v-bind:disabled='!edi' @click="save">保存</el-button>
            <el-button type="info"  style="margin-left: 30px" v-bind:disabled='!edi'>舍弃</el-button>
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
import request from "@/utils/request";

export default {
  name: "Person",
  data(){
    return{
      edi:false,
      form:{},
      ico:Edit,  // 编辑按钮的图标
      labelPosition:"left",

    }
  },
  created() {
    let str =sessionStorage.getItem("user_info")||"{}"
    this.form=JSON.parse(str)
  },
  methods:{
    discard()
    {
      let str =sessionStorage.getItem("user_info")||"{}"
      this.form=JSON.parse(str)
    },
   update(){
     this.edi=true
    },
    save(){
      //修改接口
      request.put("/User_info",this.form).then(res=>{
        if(res.code==='1')
        {
          //从数据库中查询新更新的信息
          request.post("/User_info/login",this.form).then(re=>
              {
                if(res.code==='1')
                {
                  sessionStorage.setItem("user_info",JSON.stringify(re.data))
                  let str =sessionStorage.getItem("user_info")||"{}"
                  this.form=JSON.parse(str)
                  this.edi=false
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
            this.$message.error('修改失败，请重试！')
          })
          this.$message({
            type:"success",
            message:"操作成功"
          })
        }
        else
        {
          this.$message({
            type:"warning",
            message:res.msg
          })
        }
      }).catch(err =>{
        this.$message.error('修改失败，请稍后重试试！')
      })

    }
  }
}

</script>

<style scoped>

</style>
