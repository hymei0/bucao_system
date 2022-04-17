
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
      <el-table-column prop="orderno" label="订单号" sortable align= "center" min-width="120%"/> <!--prop:属性名  label:表头的名字-->
      <el-table-column prop="subject" label="订单名" align= "center" min-width="50%"/>
      <el-table-column prop="userId" label="用户账号" align= "center" min-width="50%"/>
      <el-table-column prop="roomId" label="病房号" align= "center" min-width="50%"/>
      <el-table-column prop="createtime" label="订单创建时间" align= "center" min-width="90%"/>
      <el-table-column prop="expenses" label="应缴费用(￥)"  align= "center" min-width="50%"/>
      <el-table-column prop="paytime" label="缴费时间" align= "center" min-width="100%" />
      <el-table-column prop="state" label="支付状态" align= "center" min-width="60%"/>
      <el-table-column fix="right" label="操作" align= "center">
        <!--        内容修改区-->
        <template #default="scope">
          <el-button  type="text"  @click="handleEdit(scope.row)">编辑</el-button>
          <el-button  type="text"  @click="handlebuy(scope.row)" v-bind:disabled="scope.row.state===0" style="color: green">支付</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.orderno)">
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

      <div style="margin-top: 5px;margin-left: 10px;display: flex">
        <el-upload
            :action=excelUploadUrl
            :on-success="handleUploadSuccess"
            :show-file-list=false
            :limit="1"
            accept='.xlsx'
            style="display: inline-block; margin: 0 10px"
        >
          <el-button  type="primary" size="small" style="width: 50px;margin-left: 10px" ><el-icon><upload /></el-icon></el-button>

        </el-upload>

        <el-button  type="primary" size="small" style="width: 50px;margin-left: 10px" @click="exportdata"><el-icon><download /></el-icon></el-button>

      </div>

    </div>
    <el-dialog v-model="dialogVisible" title="订单信息" width="30%" >
      <el-form :model="form" label-width="120px" :rules="rules">
        <el-form-item label="订单号" prop="orderno">
          <el-input v-model="form.orderno" autocomplete="off"  style="width:70%" disabled/>
        </el-form-item>
        <el-form-item label="病人帐号" prop="userId">
          <el-select v-model="form.userId" class="m-2" @change="order_nums(form.userId)"  placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in userIDoptions"
                :key="item.ID"
                :label="item.ID"
                :value="item.ID"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="病房号" prop="roomId">
          <el-select v-model="form.roomId" class="m-2"   placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in roomoptions"
                :key="item.id"
                :label="item.id"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="订单名称" prop="subject">
          <el-input v-model="form.subject" style="width:70%"/>
        </el-form-item>
        <el-form-item label="创建时间" prop="createtime">
          <el-date-picker v-model="form.createtime"  type="datetime" value-format="YYYY-MM-DD HH:mm:ss"  placeholder="选择日期" style="width:70%"/>
        </el-form-item>

        <el-form-item label="应缴费用" prop="expenses">
          <el-input v-model="form.expenses" type="digit"  style="width:70%"/>
        </el-form-item>
        <el-form-item label="缴费时间" prop="paytime">
          <el-date-picker
              v-model="form.paytime"
              type="datetime"
              placeholder="选择日期"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width:70%"/>
        </el-form-item>


        <el-form-item label="支付状态" prop="state">
          <el-select v-model="form.state" class="m-2"   placeholder="Select" size="large" >
            <el-option
                v-for="item in Stateoptions"
                :key="item.value"
                :label="item.value"
                :value="item.value"
            />
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
import {getDateNums} from "@/utils/getorderno";
import {ElMessage} from "element-plus";
import {format} from "@/utils/gettime";

var XLSX = require("xlsx");
const samll = ref(false)
const background = ref(true)
const disabled = ref(false)

export default {
  name: "Myorder",
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
      orderform:{},
      edi:false,
      user: {},
      tag:'',   //1表示编辑修改数据，0表示新增数据
//对象区
      //RFID标签类别信息表
      Ordertable:[],
      options:[],
      userIDoptions:[],
      roomoptions:[],
      Stateoptions:[{value:'已支付'},{value:'未支付'}],
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
        userId: [{ required: true, message: '请输入证件号', trigger: 'blur' }],
        orderno: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        roomId: [{ required: true, message: '请选择病房号', trigger: 'blur' }],
        createtime: [{ required: true, message: '请选择订单创建时间', trigger: 'blur' }],
        state: [{ required: true, message: '请选择支付状态', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.load()
    let userStr = sessionStorage.getItem("user_info") || "{}"
    this.user = JSON.parse(userStr)
    // 请求服务端，确认当前登录用户的 合法信息
    request.get("/User_info/" + this.user.id).then(res => {
      if(res.code === '1'){
        this.user = res.data
      }
    })
  },

//方法区
  methods:{
    handlebuy(row) {
      // 请求成功跳转沙箱支付的页面
      window.open("http://localhost:9090/alipay/pay?subject=" + row.subject + "&traceNo=" + row.orderno + "&totalAmount=" + row.expenses);
      console.log("http://localhost:9090/alipay/pay?subject=" + row.subject + "&traceNo=" + row.orderno + "&totalAmount=" + row.expenses)
      this.load()

      // //this.orderform.paytime = JSON.parse(JSON.stringify(format()))
      // console.log(this.orderform)
      // request.get("/Order/buy", {
      //   params: {
      //     orderno: this.currentPage,
      //     paytime: this.orderform.paytime,
      //     search: this.search
      //   }
      // }).then(res => {
      //   // window.open(res.data)
      //   // location.href = (res.data)
      //   this.orderform=JSON.parse(JSON.stringify(row))
      //   this.orderform.paytime = JSON.parse(JSON.stringify(format()))
      //   this.orderform.state =  JSON.parse(JSON.stringify("已支付"))
      //   request.put("/Order", this.orderform).then(re=> {
      //     if (re.code === '1') {
      //       window.open(res.data)
      //       // window.open("http://localhost:9090/alipay/pay?subject=" + row.subject + "&traceNo=" + row.orderno + "&totalAmount=" + row.expenses);
      //       console.log("http://localhost:9090/alipay/pay?subject=" + row.subject + "&traceNo=" + row.orderno + "&totalAmount=" + row.expenses)
      //
      //     } else {
      //       this.$message({
      //         type: "warning",
      //         message: res.msg
      //       })
      //       this.form = {}
      //     }
      //   }).catch(err => {
      //     this.$message.error('修改失败，请稍后再试！')
      //   })
      // })
    },
    //随机生成订单唯一的编号，加上用户的uid，每个用户都有属于自己的唯一uid（让后台去处理），生成随机订单号
    order_nums(userid) {
      var outTradeNo = ""; //订单号

      for (var i = 0; i < 6; i++) //6位随机数，用以加在时间戳后面。
      {
        outTradeNo += Math.floor(Math.random() * 10);
      }
      outTradeNo = String(getDateNums(new Date())) + String(outTradeNo) + String(userid);
      this.form.orderno=outTradeNo;
      return outTradeNo;
    },

    //标红table指定行
    tableRowClassName({row, rowIndex}) {

      //判断条件（quantityStock ，warningLimit ，quantityStock 列表字段信息）
      if (row.state === "未支付") {
        // 后面的css样式
        return 'warning-row';

      } else {
        return 'success-row';
      }

      return '';
    },


    handleSelectionChange(val) {
      this.ids = val.map(v => v.orderno)   // [{id,name}, {id,name}] => [id,id]
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
      this.form.subject='医疗费'

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
    handleDelete(id){
      request.delete("/Order/"+id).then(res=>{
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
      console.log(this.form)
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
        }).catch(err =>{
          this.$message.error('添加失败，请稍后再试！')
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
