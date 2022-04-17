
<style scoped>

</style>
<template>
  <div class="Bucao_info" style="padding:10px">
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">主页</el-breadcrumb-item>
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
    <el-table :data="Bucao_infotable" border stripe style="width: 100%" @selection-change="handleSelectionChange"> <!--显示表格边框和斑马纹-->
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="rfno" label="布草类型" sortable /> <!--prop:属性名  label:表头的名字-->
      <el-table-column prop="rfid" label="RFID编号" sortable />
      <el-table-column prop="state" label="布草状态" />
      <el-table-column prop="washtimes" label=洗涤次数 />
      <el-table-column prop="indate" label=入库时间 />
      <el-table-column prop="outdate" label=出库时间 />
      <el-table-column fix="right" label="操作" >
        <!--        内容修改区-->
        <template #default="scope">
          <el-button  type="text"  @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.rfno,scope.row.rfid)">
            <template #reference>
              <el-button  type="danger" >删除</el-button>
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
          <el-button  type="primary" size="small" style="width: 50px;margin-left: 10px" >导入</el-button>

        </el-upload>

        <el-button  type="primary" size="small" style="width: 50px;margin-left: 10px" @click="exportdata">导出</el-button>

      </div>
    </div>
    <el-dialog v-model="dialogVisible" title="布草信息管理" width="30%" :before-close="handleClose">
      <el-form :model="form" label-width="120px" :rules="rules">
        <el-form-item label="布草类型" prop="rfno">
          <el-select v-model="form.rfno" class="m-2" placeholder="Select" size="large" v-bind:disabled="edi">
            <el-option
                v-for="item in options"
                :key="item.kind+item.note"
                :label="item.kind+item.note"
                :value="item.RFNO"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="RFID编号" prop="rfid">
          <el-input v-model="form.rfid" style="width:70%" autocomplete="off" v-bind:disabled="edi"/>
        </el-form-item>
        <el-form-item label="状  态" prop="state">
          <el-select v-model="form.state" class="m-2" placeholder="Select" size="large">
            <el-option
                v-for="item in options1"
                :key="item.lable"
                :label="item.lable"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="洗涤次数" prop="washtimes">
          <el-input v-model="form.washtimes" autocomplete="off"  style="width:70%"/>
        </el-form-item>
        <el-form-item label="入库时间" prop="indate">
          <el-date-picker v-model="form.indate" type="date" placeholder="选择日期" style="width:70%"/>
        </el-form-item>
        <el-form-item label="出库时间" v-if="form.state=='已报废'" prop="outdate">
          <el-date-picker v-model="form.outdate" type="date" placeholder="选择日期" style="width:70%"/>
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
  name: "homeU",
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
      Bucao_infotable:[],
      options:[],
      ids: [],
      excelUploadUrl:'http://localhost:9090/Bucao_info/import',
      //布草状态：
      options1:[
        {
          value: '使用中',
          label: '使用中',
        },
        {
          value: '洗涤中',
          label: '洗涤中',
        },
        {
          value: '待回收',
          label: '待回收',
        },
        {
          value: '闲置中',
          label: '闲置中',
        },
        {
          value: '已报废',
          label: '已报废',
        },
        {
          value: '已回收',
          label: '已回收',
        },
        {
          value: '未知',
          label: '未知',
        }
      ],
      //表单验证
      rules :{
        rfno: [{ required: true, message: '请选择布草类型', trigger: 'blur' }],
        rfid: [{ required: true, message: '请输入RFID编码', trigger: 'blur' }],
        state: [{ required: true, message: '请选择布草状态', trigger: 'blur' }],
        washtimes: [{ required: true, message: '请输入洗涤次数', trigger: 'blur' }],
        indate: [{required: true, message: '请选择入库时间', trigger: 'blur' }],
        outdate: [{required: true, message: '请选择报废时间', trigger: 'blur' }]
      }
    }

  },
  created() {
    this.load()
  },

//方法区
  methods:{
    handleSelectionChange(val) {
      this.ids = val.map(v => [v.rfno,v.rfid])   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      console.log(this.ids)
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/Bucao_info/deleteBatch", this.ids).then(res => {
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
      location.href = "http://" + "localhost" + ":9090/Bucao_info/export";
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
      request.get("/rfid_kinds/bucaoinfo" ).then(re =>{
        this.options=re
      })
      request.get("/Bucao_info",  {
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        console.log(res)
        this.Bucao_infotable=res.data.records
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
    handleDelete(rfno1,rfid1){

      request.delete("/Bucao_info",{
        params:{
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
        request.put("/Bucao_info",this.form).then(res=>{
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

        request.post("/Bucao_info",this.form).then(res=>{
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
