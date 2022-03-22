<template>
  <div class="home" style="padding:10px">
<!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button @click="add" type="primary">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0"  align="right">
      <el-input v-model="search" placeholder="请输入关键字" style="width:20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">搜索</el-button>
    </div>

<!--    数据展示区-->
    <el-table :data="RFIDtable" border stripe >style="width: 100%"> <!--显示表格边框和斑马纹-->
      <el-table-column prop="rfno" label="序列号" sortable /> <!--prop:属性名  label:表头的名字-->
      <el-table-column prop="kind" label="布草类型" />
      <el-table-column prop="stock" label="库存" />
      <el-table-column prop="section" label="所属部门" />
      <el-table-column prop="note" label="备注" />
      <el-table-column fix="right" label="操作" >
<!--        内容修改区-->
        <template #default="scope">
          <el-button  type="text"  @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.rfno)">
          <template #reference>
            <el-button  type="danger" >删除</el-button>
          </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
<!--    分页-->
    <div class="demo-pagination-block">
      <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[40,30,20,10,5]"
          :small="small"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >

<!--        添加的的对话框-->
      </el-pagination>
    </div>
    <el-dialog v-model="dialogVisible" title="RFID标签类型" width="30%" :before-close="handleClose">
      <el-form :model="form" label-width="120px">
        <el-form-item label="序 列 号">
          <el-input v-model="form.rfno" autocomplete="off"  style="width:70%"/>
        </el-form-item>
        <el-form-item label="布草类型">
          <el-input v-model="form.kind" style="width:70%"/>
        </el-form-item>
        <el-form-item label="库   存">
          <el-input v-model="form.stock" style="width:70%"/>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-select v-model="form.section" class="m-2" placeholder="Select" size="large">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备  注">
          <el-input v-model="form.note" style="width:70%"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button
        >
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
  name: 'RFIDkindsView',
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

      tag:'',   //1表示编辑修改数据，0表示新增数据
//对象区
      //RFID标签类别信息表
      RFIDtable:[],
      options:[
        {
          value: '住院部',
          label: '住院部',
        },
        {
          value: '急诊',
          label: '急诊',
        },
        {
          value: 'Option3',
          label: 'Option3',
        },
        {
          value: 'Option4',
          label: 'Option4',
        },
        {
          value: 'Option5',
          label: 'Option5',
        },
      ]
    }
  },
  created() {
    this.load()
  },

//方法区
  methods:{
    //添加按钮事件处理
    add()
    {
      this.tag='0'
      this.dialogVisible=true
      this.form={} //清空表单
    },
    //查询
    load(){
      request.get("/api/rfid_kinds",  {
        params:{
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        console.log(res)
        this.RFIDtable=res.data.records
        this.total=res.data.total
      })
    },
    //编辑按钮事件处理
    handleEdit(row){
      this.tag='1'
      this.form=JSON.parse(JSON.stringify(row))//对表单的数据进行深拷贝
      this.dialogVisible=true   //打开弹窗
    },
    //删除按钮事件处理
    handleDelete(id){
      console.log(id)
      request.delete("/api/rfid_kinds/"+id).then(res=>{
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
        request.put("/api/rfid_kinds",this.form).then(res=>{
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
        request.post("/api/rfid_kinds",this.form).then(res=>{
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
              type:"error",
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
