
<style scoped>

</style>
<template>
  <div class="User_room" style="padding:10px">
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">用户管理</el-breadcrumb-item>
      <el-breadcrumb-item style="font-size: large; ">住院信息</el-breadcrumb-item>
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
        <el-button @click="add"  type="text" v-if="flag===false">办理住院</el-button>
      </div>
      <!--    搜索区域-->
      <div style="width: 100%" align="right">
        <el-input prefix-icon="search" v-model="search"  placeholder="请输入关键字" style="width:15%" clearable/>
        <el-button  type="primary"  style="margin-left: 5px;margin-bottom: 3px" @click="load">搜索</el-button>
      </div>
    </div>

    <!--    数据展示区-->
    <el-table :row-class-name="tableRowClassName" v-model:data="User_roomtable" bUser_room stripe style="width: 100%" @selection-change="handleSelectionChange"> <!--显示表格边框和斑马纹-->
      <el-table-column prop="userid" label="ID" align= "center"/> <!--prop:属性名  label:表头的名字-->
      <el-table-column prop="roomid" label="病房号" align= "center"/>
      <el-table-column prop="uname" label="名字" align= "center"/>
      <el-table-column prop="sex" label="性别" align= "center"/>
      <el-table-column prop="telephone" label="联系电话" align= "center"/>
      <el-table-column prop="comeTime" sortable label="入院日期" align= "center"/>
      <el-table-column prop="outTime" sortable label="出院日期" align= "center"/>
      <el-table-column prop="expenses" label="应缴费用(￥)" align= "center"/>
      <el-table-column fix="right" label="操作" align= "center">
        <!--        内容修改区-->
        <template #default="scope">
          <el-button  type="text"  @click="handlebuy(scope.row)" v-bind:disabled="scope.row.expenses<=0" >缴费</el-button>
          <el-button  type="text"  @click="handleOut(scope.row)" v-bind:disabled="scope.row.outTime!=null">办理出院</el-button>
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
    <div style="padding-top: 6%;padding-left: 5px;font-size: small">
      <h3>Tips:</h3>
      <p style="margin-left: 20px;margin-top: 10px">
        1.如果您还没有办理过住院手续，您可以点击办理住院按钮选择合适得病房进行办理。
      </p>
      <p style="margin-left: 20px;margin-top: 10px">
        2.办理出院前您应该先进行缴费操作，在未缴费的状态下是不能办理出院手续的。
      </p>
      <p style="margin-left: 20px;margin-top: 10px">
        3.办理出院手续时需要输入您的账号密码。
      </p>
      <p style="margin-left: 20px;margin-top: 10px">
        3.您可以点击导出按钮导将您在本院的住院信息进行导出。
      </p>
    </div>
    <el-dialog v-model="dialogVisible" title="住院信息" width="30%"  v-if="tag==='0'" >
      <el-form :model="form" label-width="120px" :rules="rules">

        <el-form-item label="账  号" prop="userid" disabled>
          <el-input v-model="form.userid" autocomplete="off"  style="width:70%" disabled/>
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
        <el-form-item label="病房号" prop="roomid">
          <el-select v-model="form.roomid" class="m-2"  placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in roomoptions"
                :key="item"
                :label="item"
                :value="item"
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

    <el-dialog v-model="dialogVisible" title="输入密码" width="30%" v-if="tag==='1'">
      <el-form :model="form" label-width="120px" :rules="rules">
        <el-form-item label="请输入密码" prop="psd">
          <el-input v-model="psd" autocomplete="off"  style="width:70%" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="update">确定</el-button
        >
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>

import { ref } from 'vue'
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {getDateNums} from "@/utils/getorderno";

var XLSX = require("xlsx");
const samll = ref(false)
const background = ref(true)
const disabled = ref(false)
import { ElLoading } from 'element-plus'

const fullscreenLoading = ref(false)

export default {
  name: "User_roomU",
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
      user:{},
      psd:'',     //帮里出院手续时需要输密码进行验证
      flag:true,  //记录当前用户是否已经住院
      orderform:{},
      paytime:'',        //记录订单生成时间
      edi:false,
      tag:'',   //1表示编辑修改数据，0表示新增数据
//对象区
      //RFID标签类别信息表
      User_roomtable:[],
      options:[],
      userIDoptions:[],
      roomoptions:[],
      ids: [],
      orderno:'',
      filesUploadUrl: "http://" + "localhost" + ":9090/files/upload",   //头像图片上传地址
      excelUploadUrl:'http://localhost:9090/User_room/import',
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
        comeTime: [{ required: true, message: '请选择入院日期', trigger: 'blur' }],
        telephone: [{required: true, message: '请输入电话号码', trigger: 'blur' }]
      }
    }

  },
  created() {
    let str = sessionStorage.getItem("user_info") || "{}"
    //类型转换
    this.user = JSON.parse(str)
    //请求服务端，确认当前登录用户的 合法信息
    request.get("/User_info/"+ this.user.id).then(re=> {
      if (re.code === '1') {
        this.user = re.data
      }
    })
    this.load()
  },

//方法区
  methods:{
    update(){
      if(this.psd===''||this.psd!=this.user.psd)
      {
        this.$message({
          type:"error",
          message:"请输入正确密码"
        })
      }
      else
      {
        this.form.outTime=JSON.parse(JSON.stringify(this.format()))
        request.put("/User_room",this.form).then(res=>{
          if(res.code==='1')
          {
            this.$message({
              type:"success",
              message:"办理成功"
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
          }
        }).catch(err =>{
          this.$message.error('办理出院失败，请稍后再试！')
        })
      }


    },
    handlebuy(row) {
      this.orderform.userId=JSON.parse(JSON.stringify(row.userid))
      this.orderform.roomId=JSON.parse(JSON.stringify(row.roomid))
      this.orderform.orderno=JSON.parse(JSON.stringify(this.order_nums(row.userid)))
      this.orderform.createtime=JSON.parse(JSON.stringify(this.format().toString()))
      this.orderform.subject="医疗费"
      this.orderform.expenses=JSON.parse(JSON.stringify(row.expenses))
      this.orderform.paytime=null
      this.orderform.state="未支付"

      request.post("/Order",this.orderform).then(res=>{
        this.LOADING("正在创建订单")
        if(res.code==='-1') {
          this.$message.error("订单创建失败")
          return
        }else {
          this.form = JSON.parse(JSON.stringify(row))
          this.form.expenses = 0.00
          request.put("/User_room", this.form).then(re => {
            if (re.code === '-1') {
              this.$message.error("订单创建失败")
              return
            }
          })
        }

      })
    },

    LOADING(text)  {
      const loading = ElLoading.service({
        lock: true,
        text: text,
        background: 'rgba(0, 0, 0, 0.7)',
      })
      setTimeout(() => {
        this.$router.push("/MyOrder")
        this.$message.success("订单创建成功")
        loading.close()
      }, 2000)
    },

    /**
     * 获取当前时间
     * 格式YYYY-MM-DD
     */
    format () {
      var date = new Date()
      var month = date.getMonth() + 1
      var strDate = date.getDate()
      if (month >= 1 && month <= 9) {
        month = '0' + month
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = '0' + strDate
      }
      var currentDate = date.getFullYear() + '-' + month + '-' + strDate + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds()
      return currentDate
    },

//随机生成订单唯一的编号，加上部门的uid，每个部门都有属于自己的唯一uid（让后台去处理），生成随机订单号
    order_nums(userid) {
      var outTradeNo = ""; //订单号


      for (var i = 0; i < 6; i++) //6位随机数，用以加在时间戳后面。
      {
        outTradeNo += Math.floor(Math.random() * 10);
      }
      outTradeNo = String(getDateNums(new Date())) + String(outTradeNo) + String(userid);
      return outTradeNo;
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

    //获取部门姓名函数
    GetUserName(){
      request.get("/User_info/"+this.form.userid).then(res=>{
        this.form.uname=res.data.uname
        this.form.sex=res.data.sex
        this.form.telephone=res.data.telephone
      })
    },

    handleSelectionChange(val) {
      this.ids = val.map(v => [v.userid,v.roomid])   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {

      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/User_room/deleteBatch", this.ids).then(res => {
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
      location.href = "http://" + "localhost" + ":9090/User_room/export";
    },
    //办理住院
    add()
    {
      this.form={} //清空表单
      this.tag='0'
      this.edi=false
      this.dialogVisible=true
      this.form.userid=JSON.parse(JSON.stringify(this.user.id))
      this.form.uname=JSON.parse(JSON.stringify(this.user.uname))
      this.form.sex=JSON.parse(JSON.stringify(this.user.sex))
      this.form.telephone=JSON.parse(JSON.stringify(this.user.telephone))
      this.form.comeTime=JSON.parse(JSON.stringify(this.format()))
      this.form.outTime=null
      this.form.expenses=10.00
    },
    //查询
    load(){

      request.get("/User_room/foruser",  {
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
          userid:this.user.id,
        }
      }).then(res =>{
        this.User_roomtable=res.data.records
        this.total=res.data.total
      })
      request.get("/User_room/suitableroom" ).then(re =>{
        this.roomoptions=re.data
      })
      request.get("/User_room/suitableuser" ).then(re =>{
       for(var i=0;i<re.data.length;i++)
       {
         if(re.data[i]==this.user.id){
           this.flag=false
           break
         }
         this.flag=true
       }
      })
      //this.roomoptions.removed(1)
    },
    //编辑按钮事件处理
    handleOut(row){
      if(row.expenses>0){
        this.$message({
          type:"warning",
          message:"请先缴费"
        })
        return
      }

      this.psd=''
      this.tag='1'
      this.edi=true
      this.form=JSON.parse(JSON.stringify(row))//对表单的数据进行深拷贝
      this.dialogVisible=true   //打开弹窗
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
        request.put("/User_room",this.form).then(res=>{
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
        request.post("/User_room",this.form).then(res=>{
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
      var worksheet = XLSX.utils.json_to_sheet(this.User_roomtable,{heard:["userid","roomId","uname","sex","telephone","address",,"days","expenses"]});//从 JS 值数组的数组创建工作表
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
