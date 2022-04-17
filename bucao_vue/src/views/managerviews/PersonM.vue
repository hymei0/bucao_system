<template>
  <div >
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">我的</el-breadcrumb-item>
      <el-breadcrumb-item style="font-size: large; ">个人信息</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索，切换 -->
    <el-row :gutter="23">
      <el-col :span="18">
        <el-divider></el-divider>
      </el-col>
      <el-col :span="6">
      </el-col>
    </el-row>
    <el-card style="width:50%;margin: 100px 300px">
      <h2 style="text-align: center;padding-bottom: 40px">个人信息</h2>
      <el-form ref="form" :model="form" label-width="80px" :label-position="labelPosition" style="width: 65%;margin-left: 90px" align="center">
        <el-form  ref="form" :model="form" label-width="100px" :label-position="labelPosition" style="width: 100%;display: flex;padding-bottom: 40px">
          <el-form align="left" style="width: 70%;">
              <el-form-item label="姓 名" prop="uname" >
                <a>{{form.uname}}</a>
              </el-form-item>
              <el-form-item label="账 号" prop="id">
                <a>{{form.id}}</a>
              </el-form-item>
          </el-form>
          <el-form-item  prop="portrai" align="right" style="margin-right: 0px">



            <el-upload
                class="avatar-uploader"
                :action="filesUploadUrl"
                :show-file-list="false"
                :on-success="filesUploadSuccess"
                v-bind:disabled='!edi'
                :before-upload="beforeAvatarUpload"
            >
              <img v-if="form.portrait" :size="30" :src="form.portrait" style="width: 70px;height: auto;" align="right" class="avatar" title="更换头像"/>
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>

            </el-upload>


          </el-form-item>
        </el-form>

        <el-form-item label="地 址" prop="address" >
          <el-input v-model="form.address" autocomplete="off" v-bind:disabled='!edi' ></el-input>
        </el-form-item>
        <el-form-item label="电 话" prop="telephone">
          <el-input v-model="form.telephone" v-bind:disabled='!edi'></el-input>
        </el-form-item>

        <el-form-item style="display: flex" >
          <div style="width: 100%" align="center">
            <el-button type="primary" :icon="ico"  style="width: 50px;" v-bind:disabled='edi' @click="update"/>
            <el-button type="success" style="margin-left: 30px" v-bind:disabled='!edi' @click="save">保存</el-button>
            <el-button type="info"  style="margin-left: 30px" v-bind:disabled='!edi' @click="discard">舍弃</el-button>
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
import {ElMessage} from "element-plus";
import {UploadProps} from "element-plus";

export default {
  name: "PersonM",
  //文件大小限制

  data(){
    return{
      edi:false,
      form:{},
      ico:Edit,  // 编辑按钮的图标
      labelPosition:"left",
      filesUploadUrl: "http://" + "localhost" + ":9090/files/upload",
    }
  },
  created() {
    this.load()
  },
  methods:{

    //文件上传成功回调函数
    filesUploadSuccess(res, uploadFile) {
      this.form.portrait = res.data

    },
    beforeAvatarUpload(rawFile){
      if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error('头像图片的大小不能超过2MB!')
        return false
      }
      return true
    },

    load()
    {
      let str =sessionStorage.getItem("user_info")||"{}"
      this.form=JSON.parse(str)
      //初始化头像
      if(this.form.portrait===null)
      {
        this.form.portrait='https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg'
      }
    },
    discard()
    {
      this.load()
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
                  this.load()
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
