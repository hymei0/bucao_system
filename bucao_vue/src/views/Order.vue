
<style scoped>

</style>
<template>
  <div class="order" style="padding:10px">
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">用户管理</el-breadcrumb-item>
      <el-breadcrumb-item style="font-size: large; ">订单信息</el-breadcrumb-item>
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
    <el-table :row-class-name="tableRowClassName" v-model:data="Ordertable" border stripe style="width: 100%" @selection-change="handleSelectionChange"> <!--显示表格边框和斑马纹-->
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="userid" label="证件号" sortable /> <!--prop:属性名  label:表头的名字-->
      <el-table-column prop="roomId" label="病房号" />
      <el-table-column prop="uname" label="名字" />
      <el-table-column prop="sex" label="性别" />
      <el-table-column prop="telephone" label="联系电话" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="days" label="住院天数" />
      <el-table-column prop="expenses" label="欠费情况(￥)" style="color: red"/>
      <el-table-column fix="right" label="操作" >
        <!--        内容修改区-->
        <template #default="scope">
          <el-button  type="text"  @click="handleEdit(scope.row)">编辑</el-button>
          <el-button  type="text"  @click="handlebuy(scope.row)" v-bind:disabled="scope.row.expenses<=0" style="color: green">支付</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.userid,scope.row.roomId)">
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

        <el-button  type="primary" size="small" style="width: 50px;margin-left: 10px" @click="handleDownload">导出</el-button>

      </div>
    </div>
    <el-dialog v-model="dialogVisible" title="用户信息" width="30%" :before-close="handleClose">
      <el-form :model="form" label-width="120px" :rules="rules">

        <el-form-item label="账  号" prop="userid">
          <el-select v-model="form.userid" class="m-2" @change="GetUserName" default-first-option="true" placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in userIDoptions"
                :key="item.ID"
                :label="item.ID"
                :value="item.ID"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="姓  名" prop="uname">
          <el-input v-model="form.uname" autocomplete="off"  style="width:70%" disabled/>
        </el-form-item>
        <el-form-item label="性  别" prop="sex">
          <el-input v-model.number="form.sex"   style="width:70%" disabled/>
        </el-form-item>
        <el-form-item label="电 话" prop="telephone">
          <el-input v-model.number="form.telephone"   style="width:70%" disabled/>
        </el-form-item>
        <el-form-item label="病房号" prop="roomId">
          <el-select v-model="form.roomId" class="m-2"  default-first-option="true" placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in roomoptions"
                :key="item.id"
                :label="item.id"
                :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="住院天数" prop="days">
          <el-input v-model.number="form.days"   style="width:70%"/>
        </el-form-item>
        <el-form-item label="欠费情况" prop="days">
          <el-input v-model="form.expenses" type="digit"  style="width:70%"/>
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

var XLSX = require("xlsx");
const samll = ref(false)
const background = ref(true)
const disabled = ref(false)

export default {
  name: "order",
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
      Ordertable:[],
      options:[],
      userIDoptions:[],
      roomoptions:[],
      ids: [],
      filesUploadUrl: "http://" + "localhost" + ":9090/files/upload",   //头像图片上传地址
      excelUploadUrl:'http://localhost:9090/Order/import',
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
        userid: [{ required: true, message: '请输入证件号', trigger: 'blur' }],
        uname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        roomId: [{ required: true, message: '请选择病房号', trigger: 'blur' }],
        sex: [{ required: true, message: '请选择性别', trigger: 'blur' }],
        psd: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        telephone: [{required: true, message: '请输入电话号码', trigger: 'blur' }]
      }
    }

  },
  created() {
    this.load()
  },

//方法区
  methods:{
    handlebuy(row) {
      request.get("/order/buy/" + bookId).then(res => {
        // 请求成功跳转沙箱支付的页面
        window.open(res.data)
      })
    },

    //标红table指定行
    tableRowClassName({row, rowIndex}) {

      //判断条件（quantityStock ，warningLimit ，quantityStock 列表字段信息）
      if (row.expenses > 0) {
        // 后面的css样式
        return 'warning-row';

      } else {
        return 'success-row';
      }

      return '';
    },

    //获取用户姓名函数
    GetUserName(){
      request.get("/User_info/"+this.form.userid).then(res=>{
        this.form.uname=res.data.uname
        this.form.sex=res.data.sex
        this.form.telephone=res.data.telephone
      })
    },

    handleSelectionChange(val) {
      this.ids = val.map(v => [v.userid,v.roomId])   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {

      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/Order/deleteBatch", this.ids).then(res => {
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
      location.href = "http://" + "localhost" + ":9090/Order/export";
    },
    //添加按钮事件处理
    add()
    {
      this.tag='0'
      this.edi=false
      this.dialogVisible=true
      this.form={} //清空表单
    },
    //查询
    load(){
      request.get("/Room_info/selectall" ).then(re =>{
        this.roomoptions=re.data
      })
      request.get("/User_info/selectall" ).then(re =>{
        this.userIDoptions=re.data
      })
      request.get("/Order",  {
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        this.Ordertable=res.data.records
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
    handleDelete(id1,id2){
      request.delete("/Order",{
        params:{
          userid:id1,
          roomId:id2
        }
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
        request.put("/Order",this.form).then(res=>{
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
          this.$message.error('修改失败，请稍后再试！')
        })
      }
      else  //新增
      {
        console.log(this.options)

        request.post("/Order",this.form).then(res=>{
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
    },
    //数据导出：从前端导出
    handleDownload() {
      var workbook = XLSX.utils.book_new();//新建一个新的工作表
      var worksheet = XLSX.utils.json_to_sheet(this.Ordertable,{heard:["userid","roomId","uname","sex","telephone","address",,"days","expenses"]});//从 JS 值数组的数组创建工作表
      XLSX.utils.book_append_sheet(workbook, worksheet, "RFID分类表");//将工作表附加到工作簿
      // let workbook = XLSX.utils.table_to_book(document.getElementById('table'))//通过抓取页面中的 HTML TABLE 创建工作表
      try {
        XLSX.utils.sheet_add_aoa(worksheet, [
          // <-- Do nothing in row 4
          [ "证件号", "病房号","名字", "性别","电话","地址","住院天数","应缴费用"/*F1*/]  // <-- Write "abc" to cell E5
        ]);
        XLSX.writeFile(workbook, '订单信息.xlsx')//导出工作表
      } catch(e) {
        console.log(e, workbook);
      }
    },
  }
}
</script>

<style scoped>

/deep/ .el-table .success-row {
  color: rgb(27, 33, 27) !important;
}

/deep/ .el-table .warning-row {
  color: #f51f1f !important;
}

</style>
