
<style scoped>

</style>
<template>
  <div class="Room_info" style="padding:10px">
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">系统信息</el-breadcrumb-item>
      <el-breadcrumb-item style="font-size: large; ">病房信息</el-breadcrumb-item>
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
      <!--    搜索区域-->
      <div style="width: 100%" align="right">
        <el-input prefix-icon="search" v-model="search"  placeholder="请输入关键字" style="width:15%" clearable/>
        <el-button  type="primary"  style="margin-left: 5px;margin-bottom: 3px" @click="load">搜索</el-button>
      </div>
    </div>

    <!--    数据展示区-->
    <el-table :data="Room_infotable" border stripe style="width: 100%"> <!--显示表格边框和斑马纹-->
      <el-table-column prop="id" label="病房号" sortable /> <!--prop:属性名  label:表头的名字-->
      <el-table-column prop="section" label="所属部门" sortable />
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

        </el-pagination>
      </div>
      <!--    导入导出-->
      <div style="margin-top: 5px;margin-left: 10px">
        <el-button  type="text"  style="width: 50px;margin-left: 10px" @click="exportdata">下载数据</el-button>
      </div>
    </div>
    <div style="padding-top: 6%;padding-left: 5px;font-size: small">
      <h3>Tips:</h3>
      <p style="margin-left: 20px;margin-top: 10px">
        1.此页面为病房的基本信息页面。
      </p>
      <p style="margin-left: 20px;margin-top: 10px">
        2.包括了病房号和所属部门组成。
      </p>
      <p style="margin-left: 20px;margin-top: 10px">
        3.点击”下载数据“可以下载数据到本地。
      </p>
    </div>

    <!--        添加的的对话框-->
    <el-dialog v-model="dialogVisible" title="病房信息管理" width="30%" >
      <el-form :model="form" label-width="120px" :rules="rules">
        <el-form-item label="病房号" prop="id">
          <el-input v-model="form.id" style="width:70%" autocomplete="off" v-bind:disabled="edi"/>
        </el-form-item>
        <el-form-item label="所属部门" prop="section">
          <el-select v-model="form.section" class="m-2" placeholder="Select" size="large">
            <el-option
                v-for="item in options"
                :key="item.id"
                :label="item.na"
                :value="item.na"/>
          </el-select>
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

const samll = ref(false)
const background = ref(true)
const disabled = ref(false)

export default {
  name: "Room_infoU",
  components: {
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
      Room_infotable:[],
      options:[],
      ids: [],
      excelUploadUrl:'http://localhost:9090/Room_info/import',
      //表单验证
      rules :{
        id: [{ required: true, message: '请输入部门编号', trigger: 'blur' }],
        section: [{ required: true, message: '请输入所属部门', trigger: 'blur' }]
      }
    }

  },
  created() {
    this.load()
  },

//方法区
  methods:{
    handleSelectionChange(val) {
      this.ids = val.map(v => v.id)   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      console.log(this.ids)
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/Room_info/deleteBatch", this.ids).then(res => {
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
        this.$message.success("导入成功")
        this.load()
      }
    },
    //数据导出：法一：从后端的数据库中导出
    exportdata() {
      location.href = "http://" + "localhost" + ":9090/Room_info/export";
    },
    //添加按钮事件处理
    add()
    {
      request.get("/Section/rfid_kinds").then(re=>{
        this.options=re
      })
      this.tag='0'
      this.edi=false
      this.dialogVisible=true
      this.form={} //清空表单
    },
    //查询
    load(){
      request.get("/Room_info",  {
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        console.log(res)
        this.Room_infotable=res.data.records
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

      request.delete("/Room_info/"+id,{
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
      if(this.tag==='1')//该项记录的主键存在，进行更新操作
      {
        request.put("/Room_info",this.form).then(res=>{
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

        request.post("/Room_info",this.form).then(res=>{
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
