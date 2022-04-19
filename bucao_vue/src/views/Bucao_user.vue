
<style scoped>

</style>
<template>
  <div class="Bucao_user" style="padding:10px">
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">布草管理</el-breadcrumb-item>
      <el-breadcrumb-item style="font-size: large; ">布草-部门信息</el-breadcrumb-item>
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
    <el-table :data="Bucao_usertable" border stripe style="width: 100%" @selection-change="handleSelectionChange"> <!--显示表格边框和斑马纹-->
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="userId" label="部门账号" />
      <el-table-column prop="userName" label="部门姓名" sortable />
      <el-table-column prop="roomId" label="所在病房" sortable />
      <el-table-column prop="rfidx" label="布草RFID编号" />
      <el-table-column fix="right" label="操作" >
        <!--        内容修改区-->
        <template #default="scope">
          <el-button  type="text" style="color:greenyellow" @click="handledetail(scope.row.rfno,scope.row.rfid)">详情</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.userId,scope.row.rfno,scope.row.rfid)">
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


        </el-pagination>
      </div>
      <!--    导入导出-->
      <div style="margin-top: 5px;margin-left: 10px">
        <el-button  type="primary" size="small" style="width: 50px;margin-left: 10px" @click="exportdata">导出</el-button>
      </div>
    </div>
    <!--        对话框-->
    <el-dialog v-model="dialogVisible" title="布草分布管理" width="30%" >
      <el-form :model="form" label-width="120px" :rules="rules">
        <el-form-item label="病人账号" prop="userId">
          <el-select v-model="form.userId" class="m-2" @change="GetUserName" placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in useroptions"
                :key="item.id"
                :label="item.id"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="病房号" prop="userId">
          <el-select v-model="form.roomId" class="m-2" placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in roomoptions"
                :key="item.id"
                :label="item.id"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门姓名" prop="num">
          <el-input v-model="form.userName" style="width:70%" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="布草RFID编码" prop="rfidx">
          <el-select v-model="form.rfidx" class="m-2" placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in bucaooptions"
                :key="item.RFNO+item.RFID"
                :label="item.RFNO+item.RFID"
                :value="item.RFNO+item.RFID"
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
    </el-dialog >

    <!--    详情页面的对话框-->
    <el-dialog v-model="dialogVisible1" title="布草详细信息" width="30%" >
      <el-form :model="form1" label-width="120px" >
        <el-form-item label="布草类型" prop="rfno">
          <el-input v-model="form1.rfno" style="width:70%" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="编号" prop="rfid">
          <el-input v-model="form1.rfid" style="width:70%" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="状  态" prop="state">
          <el-input v-model="form1.state" style="width:70%" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="洗涤次数" prop="washtimes">
          <el-input v-model="form1.washtimes" autocomplete="off"  style="width:70%" disabled/>
        </el-form-item>
        <el-form-item label="入库时间" prop="indate">
          <el-date-picker v-model="form1.indate" type="date" placeholder="选择日期" style="width:70%" disabled/>
        </el-form-item>
        <el-form-item label="出库时间" v-if="form1.state=='已报废'" prop="outdate">
          <el-date-picker v-model="form1.outdate" type="date" placeholder="选择日期" style="width:70%" disabled/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible1 = false">关闭</el-button>
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
  name: "Bucao_user",
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
      dialogVisible1:false, //控制详细信息对话框的显示
      form:{},
      form1:{},
      edi:false,
      tag:'',   //1表示编辑修改数据，0表示新增数据
//对象区
      //RFID标签类别信息表
      Bucao_usertable:[],
      useroptions:[],
      roomoptions:[],
      bucaooptions:[],
      ids: [],
      excelUploadUrl:'http://localhost:9090/Bucao_user/import',

      //表单验证
      rules :{
        userId: [{ required: true, message: '请选择布草类型', trigger: 'blur' }],
        rfidx: [{ required: true, message: '请选择布草RFID编码', trigger: 'blur' }],
        userName: [{ required: true, message: '请输入布草数量', trigger: 'blur' }],
        roomId: [{required: true, message: '请选择病房号', trigger: 'blur' }]
      }
    }

  },
  created() {
    this.load()
  },

//方法区
  methods:{
    //获取部门姓名函数
    GetUserName(){
      request.get("/User_info/"+this.form.userId).then(res=>{
        this.form.userName=res.data.uname
      })
    },
    handleSelectionChange(val) {
      this.ids = val.map(v => [v.rfno,v.rfid,v.roomId,v.userId])   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      console.log(this.ids)
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/Bucao_user/deleteBatch", this.ids).then(res => {
        if (res.code === '1') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    //数据导出：法一：从后端的数据库中导出
    exportdata() {
      location.href = "http://" + "localhost" + ":9090/Bucao_user/export";
    },
    //添加按钮事件处理
    add()
    {
      this.tag='0'
      this.edi=false
      this.dialogVisible=true
      this.form={} //清空表单
      request.get("/Bucao_info/foruser" ,{
        params:{
          rfid:'A'
        }
      }).then(re =>{
        this.bucaooptions=re
      })
      request.get("/Room_info/forbucao" ).then(re =>{
        this.roomoptions=re.data
      })
      request.get("/Bucao_user/forbucao" ).then(re =>{
        this.useroptions=re.data
        console.log(this.useroptions)
      })
    },
    //详情按钮事件处理
    handledetail(id1,id2){
      this.dialogVisible1=true

      request.get("/Bucao_info/detail",{
        params:{
          rfno:id1,
          rfid:id2
        }
      }).then(res=>{
        if(res.code==='1')
        {
          this.form1=res.data
          console.log(this.form1)
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
    //查询
    load(){

      request.get("/Bucao_user",  {
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        this.Bucao_usertable=res.data.records
        this.total=res.data.total
        for(var i = 0;i<this.total;i++){
          this.Bucao_usertable[i].rfidx=this.Bucao_usertable[i].rfno+this.Bucao_usertable[i].rfid
        }
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
    handleDelete(id,rfno1,rfid1){
      request.delete("/Bucao_user",{
        params:{
          userId:id,
          rfno:rfno1,
          rfid:rfid1
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

        request.put("/Bucao_user",this.form).then(res=>{
          if(res.code==='1')
          {
            this.$message({
              type:"success",
              message:"修改成功"
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
        this.form.rfid=this.form.rfidx.match(/\d+/g).toString()
        this.form.rfno=this.form.rfidx.match(/[a-zA-Z]/ig).join('')

        request.post("/Bucao_user",this.form).then(res=>{
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
