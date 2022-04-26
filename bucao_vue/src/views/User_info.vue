
<style scoped>

</style>
<template>
  <div class="User_info" style="padding:10px">
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">用户管理</el-breadcrumb-item>
      <el-breadcrumb-item style="font-size: large; ">用户信息</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索，切换 -->
    <el-row :gutter="23">
      <el-col :span="18">
        <el-divider></el-divider>

      </el-col>
      <el-col :span="6">
      </el-col>
    </el-row>
    <!--    功能区域-->
    <div style="display: flex; margin: 10px 0"  align="left">
      <div style="width: 10%;display: flex" align="left">
        <el-button @click="add" type="primary">新增</el-button>
        <el-popconfirm title="确定删除吗？" @confirm="deleteBatch">
          <template #reference>
            <el-button type="danger" >批量删除</el-button>
          </template>
        </el-popconfirm>
      </div>
      <!--    搜索区域-->
      <div style="width: 100%" align="right">
        <el-input prefix-icon="search" v-model="search"  placeholder="请输入关键字" style="width:15%" clearable/>
        <el-button  type="primary"  style="margin-left: 5px;margin-bottom: 3px" @click="load">搜索</el-button>
      </div>
    </div>

    <!--    数据展示区-->
    <el-table :data="User_infotable" border stripe style="width: 100%" @selection-change="handleSelectionChange"> <!--显示表格边框和斑马纹-->
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="证件号" sortable /> <!--prop:属性名  label:表头的名字-->
      <el-table-column prop="uname" label="名字" />
      <el-table-column prop="sex" label="性别" />
      <el-table-column prop="portrait" label="头像">
        <template #default="scope">
          <el-image
              style="width: 100px; height: auto"
              :src="scope.row.portrait"
              preview-teleported
              :preview-src-list="[scope.row.portrait]"/>
        </template>
      </el-table-column>
      <el-table-column prop="telephone" label="联系电话" />
      <el-table-column prop="address" label="地址" />
      <el-table-column fix="right" label="操作" >
        <!--        内容修改区-->
        <template #default="scope">
          <el-button  type="text"  @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button  type="text" style="color: red" >删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!--    分页-->
    <div style="display: flex">
      <div class="demo-pagination-block">
        <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[40,30,20,10]"
          layout="total, sizes, prev, pager, next, jumper "
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >

        <!--        添加的的对话框-->
        </el-pagination>
      </div>
      <!--    导入导出-->
      <div style="margin-top: 5px;margin-left: 10px">
        <el-upload
            :action=excelUploadUrl
            :on-success="handleUploadSuccess"
            :show-file-list=false
            :limit="1"
            accept='.xlsx'
            style="display: inline-block; margin: 0 10px">
          <el-button  type="primary" size="small" style="width: 70px;margin-left: 10px" >导入<el-icon class="el-icon--right"><Upload /></el-icon></el-button>

        </el-upload>

        <el-button  type="primary" size="small" style="width: 70px;margin-left: 10px" @click="exportdata">导出<el-icon class="el-icon--right"><Download/></el-icon></el-button>

      </div>
    </div>
    <div style="padding-top: 6%;padding-left: 5px;font-size: small">
      <h3>Tips:</h3>
      <p style="margin-left: 20px;margin-top: 10px">
        1.此页面为系统的病人信息表。
      </p>
      <p style="margin-left: 20px;margin-top: 10px">
        2.可以进行基本的增删改查等功能，添加用户时密码默认为账号。
      </p>
      <p style="margin-left: 20px;margin-top: 10px">
        3.支持为用户修改头像等功能，还支持头像的预览功能。
      </p>
      <p style="margin-left: 20px;margin-top: 10px">
        4.支持数据的导入导出。
      </p>
    </div>
    <el-dialog v-model="dialogVisible" title="用户信息" width="30%" >
      <el-form :model="form" label-width="120px" :rules="rules">

        <el-form-item label="账  号" prop="id">
          <el-input v-model="form.id" style="width:70%" autocomplete="off" v-bind:disabled="edi"/>
        </el-form-item>
        <el-form-item label="姓  名" prop="uname">
          <el-input v-model="form.uname" autocomplete="off"  style="width:70%"/>
        </el-form-item>
        <el-form-item label="头 像:" prop="portrait">
          <el-upload
              class="avatar-uploader"
              :action="filesUploadUrl"
              :show-file-list="false"
              :on-success="filesUploadSuccess"
              :before-upload="beforeAvatarUpload"
              style="margin-left: 0px">
            <img v-if="form.portrait" :src="form.portrait" style="width: 50%;height: auto;margin-left: -90px" align="left" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="性  别" prop="sex">
          <el-select v-model="form.sex" class="m-2" placeholder="Select" size="large">
            <el-option
                v-for="item in options1"
                :key="item.label"
                :label="item.label"
                :value="item.label"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="电 话" prop="telephone">
          <el-input v-model.number="form.telephone"   style="width:70%"/>
        </el-form-item>
        <el-form-item label="地 址" prop="address">
          <el-input v-model.number="form.address"   style="width:70%"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>

import { ref } from 'vue'
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {Download} from "@element-plus/icons-vue";

const samll = ref(false)
const background = ref(true)
const disabled = ref(false)

export default {
  name: "User_info",
  components: {
    Download
  },

  data(){
    return{
//数据变量区
      search:'',                   //搜索框的值
      currentPage: 1,   //当前页面
      pageSize: 10,     // 每页的大小
      total: 0,
      dialogVisible:false,
      form:{},
      edi:false,
      tag:'',   //1表示编辑修改数据，0表示新增数据
//对象区
      //RFID标签类别信息表
      User_infotable:[],
      options:[],
      ids: [],
      filesUploadUrl: "http://" + "localhost" + ":9090/files/upload",   //头像图片上传地址
      excelUploadUrl:'http://localhost:9090/User_info/import',
      //布草状态：
      options1:[
        {
          label: '男',
        },
        {
          label: '女',
        }
      ],
      //表单验证
      rules :{
        id: [{ required: true, message: '请输入证件号', trigger: 'blur' }],
        uname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        sex: [{ required: true, message: '请选择性别', trigger: 'blur' }],
        psd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        telephone: [{required: true, message: '请输入电话号码', trigger: 'blur' }],
        address: [{required: true, message: '请输入地址', trigger: 'blur' }]
      }
    }

  },
  created() {
    this.load()
    let userStr = sessionStorage.getItem("user_info") || "{}"
    this.user = JSON.parse(userStr)
    // 请求服务端，确认当前登录用户的 合法信息
    request.get("/ManagerInfo/" + this.user.id).then(res => {
      if(res.code === '1'){
        this.user = res.data
      }
    })
  },

//方法区
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
    handleSelectionChange(val) {
      this.ids = val.map(v => v.id)   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      console.log(this.ids)
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/User_info/deleteBatch", this.ids).then(res => {
        if (res.code === '1') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    //excel表格的导入：直接导入到后端
    handleUploadSuccess(res) {
      if (res.code === "1") {
        this.$message.success("一共成功导入"+res.data+"条数据")
        this.load()
      }
    },
    //数据导出：法一：从后端的数据库中导出
    exportdata() {
      location.href = "http://" + "localhost" + ":9090/User_info/export";
    },
    //添加按钮事件处理
    add()
    {
      this.tag='0'
      this.edi=false
      this.dialogVisible=true
      this.form={} //清空表单

      this.form.roles="user"
    },
    //查询
    load(){

      request.get("/User_info",  {
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        console.log(res)
        this.User_infotable=res.data.records
        this.total=res.data.total
      })
    },
    //编辑按钮事件处理
    handleEdit(row){
      this.tag='1'
      this.edi=true
      this.form=JSON.parse(JSON.stringify(row))//对表单的数据进行深拷贝
      this.dialogVisible=true   //打开弹窗
    },
    //删除按钮事件处理
    handleDelete(id){

      request.delete("/User_info/"+id,{
      }).then(res=>{
        if(res.code==='1')
        {
          this.$message({
            type:"success",
            message:"删除成功"
          })
        }
        else {
          this.$message({
            type: "warning",
            message: res.msg
          })
        }
        this.load()
      })

    },
    //表格大小事件处理：改变当前每页个数
    handleSizeChange()
    {
      this.load()
    },
    //表格页面跳转事件处理：改变当前页码触发
    handleCurrentChange()
    {
      this.load()
    },
    /*对话框按钮*/
    save()
    {
      if(this.form.id==null || this.form.uname==null || this.form.address==null||this.form.telephone==null||this.form.sex==null)
      {
        this.$message.error('表单未填写完整，添加失败')
        return
      }
      if(this.tag==='1')//该项记录的主键存在，进行更新操作
      {
        request.put("/User_info",this.form).then(res=>{
          if(res.code==='1')
          {
            this.$message({
              type:"success",
              message:"操作成功"
            })
            this.load()
            this.dialogVisible=false
          }
          else
          {
            this.$message({
              type:"warning",
              message:res.msg
            })
            this.form={}
          }
        }).catch(err =>{
          this.$message.error('添加失败，请稍后再试！')
        })
      }
      else  //新增
      {
        console.log(this.options)

        request.post("/User_info",this.form).then(res=>{
          console.log(res)
          if(res.code==='1')
          {
            this.$message({
              type:"success",
              message:"操作成功"
            })
            this.load()
            this.dialogVisible=false
          }
          else
          {
            this.$message({
              type:"Error",
              message:res.msg
            })
            this.form={}
          }
        })
      }
    }
  }
}
</script>
