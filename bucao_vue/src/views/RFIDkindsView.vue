<template>
  <div class="home" style="padding:10px">
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">系统信息</el-breadcrumb-item>
      <el-breadcrumb-item style="font-size: large; ">RFID标签分类</el-breadcrumb-item>
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
        <el-input prefix-icon="search" v-model="search"  placeholder="请输入关键字" style="width:15%" clearable select-when-unmatched/>
        <el-button  type="primary"  style="margin-left: 5px;margin-bottom: 3px" @click="load">搜索</el-button>
      </div>

    </div>

<!--    数据展示区-->
    <div id="table">
    <el-table :data="RFIDtable" border stripe style="width: 100%" @selection-change="handleSelectionChange"> <!--显示表格边框和斑马纹-->
      <el-table-column type="selection" width="55"></el-table-column>
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
    </div>
<!--    分页-->
    <div style="display: flex">
      <div class="demo-pagination-block">
        <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[40,30,20,10,5]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
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


    <!--        添加的的对话框1-->
    <el-dialog v-model="dialogVisible" title="RFID标签类型" width="30%" :before-close="handleClose">
      <el-form :model="form" label-width="120px" :rules="rules">
        <el-form-item label="序列号" prop="rfno">
          <el-input v-model="form.rfno" autocomplete="off"  style="width:70%"  v-bind:disabled="edi"/>
        </el-form-item>
        <el-form-item label="布草类型" prop="kind">
          <el-input v-model="form.kind" style="width:70%"/>
        </el-form-item>
        <el-form-item label="库   存" prop="stock">
          <el-input v-model="form.stock" style="width:70%"/>
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
        <el-form-item label="布草名称" prop="note" >
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

    <!--        添加的的对话框2-->
    <el-dialog v-model="dialogVisible2" title="RFID标签类型" width="30%" :before-close="handleClose">
<!--      <el-upload-->
<!--          name="wlwDeviceFile"-->
<!--          ref="upload"-->
<!--          action=""-->
<!--          accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"-->
<!--          :format="['xls','xlsx']"-->
<!--          :on-change="handleExcelFileChange"-->
<!--          :on-exceed="handleExcelFileExceed"-->
<!--          :on-remove="handleExcelFileRemove"-->
<!--          :file-list="excelFileList"-->
<!--          :limit="1"-->
<!--          :auto-upload="false"-->
<!--      >-->
<!--        <el-button style="width: 50px" size="small" type="warning" plain>选择文件</el-button>-->
<!--      </el-upload>-->
      <el-upload
          action="http://localhost:9090/user/import"
          :on-success="handleUploadSuccess"
          :show-file-list=false
          :limit="1"
          accept='.xlsx'
          style="display: inline-block; margin: 0 10px"
      >
        <el-button type="primary">导入</el-button>
      </el-upload>
      <el-button style="width: 50px" size="small"  @click="exportdata">导入</el-button>
    </el-dialog>
  </div>
</template>

<script>



import request from "@/utils/request";
var XLSX = require("xlsx");


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
      dialogVisible2:false,
      form:{},
      edi:false,
      tag:'',   //1表示编辑修改数据，0表示新增数据
      excelUploadUrl: "http://localhost:9090/rfid_kinds/import",//excel文件上传接口url
      //RFID标签类别信息表
      RFIDtable:[],
      options:[],
      mesg: false,
      mes: "",
      cod: 0,
      excelFile:null,
      ids: [],
      rules :{
        rfno: [{ required: true, message: '请输入序列号', trigger: 'blur' }],
        kind: [{ required: true, message: '请输入布草类型', trigger: 'blur' }],
        stock: [{ required: true, message: '请输入库存', trigger: 'blur' }],
        section: [{ required: true, message: '请选择所属部门', trigger: 'blur' }],
        note: [{ required: true, message: '请输入布草名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.load()
  },

//方法区
  methods:{
    handleSelectionChange(val) {
      this.ids = val.map(v => v.rfno)   // [{id,name}, {id,name}] => [id,id]
    },
    deleteBatch() {
      console.log(this.ids)
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/rfid_kinds/deleteBatch", this.ids).then(res => {
        if (res.code === '1') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
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

      request.get("/rfid_kinds",  {
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
    //excel表格的导入：直接导入到后端
    handleUploadSuccess(res) {
      if (res.code === "1") {
        this.$message.success("导入成功")
        this.load()
      }
    },
    //数据导出：法一：从后端的数据库中导出
    exportdata() {
      location.href = "http://" + "localhost" + ":9090/rfid_kinds/export";
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
      request.delete("/rfid_kinds/"+id).then(res=>{
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
        request.put("/rfid_kinds",this.form).then(res=>{
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
          this.$message.error('更新失败，请稍后再试！')
      })
      }
      else  //新增
      {
        request.post("/rfid_kinds",this.form).then(res=>{
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
    },





    //excel文件导入：导入到前端将数据转化传入后端
    //文件上传成功回调函数
    handleExcelFileChange(file, fileList) {
      this.excelFile = file.raw
    },
    handleExcelFileRemove(file, fileList) {
      this.excelFile = null
    },
    // 处理导入Excel中日期问题
    formatDate(numb, format) {
      const time = new Date((numb - 1) * 24 * 3600000 + 1);
      time.setYear(time.getFullYear() - 70);
      const year = time.getFullYear() + "";
      const month = time.getMonth() + 1 + "";
      const date = time.getDate() - 1 + "";
      if (format && format.length === 1) {
        return year + format + month + format + date;
      }
      return (
          year +
          (month < 10 ? "0" + month : month) +
          (date < 10 ? "0" + date : date)
      );
    },

    // 导入Excel
    importfxx() {
      if (this.excelFile === null) {
        this.$message({
          type: "warning",
          message: "请选择要导入的附件！"
        })
      } else {
        let _this = this;
        let f = this.excelFile;
        let rABS = false; //是否将文件读取为二进制字符串

        let reader = new FileReader();
        //if (!FileReader.prototype.readAsBinaryString) {
        FileReader.prototype.readAsBinaryString = function (f) {
          let binary = "";
          let rABS = false; //是否将文件读取为二进制字符串
          let wb; //读取完成的数据
          let outdata;
          let reader = new FileReader();
          reader.onload = function (e) {
            let bytes = new Uint8Array(reader.result);
            let length = bytes.byteLength;
            for (let i = 0; i < length; i++) {
              binary += String.fromCharCode(bytes[i]);
            }
            let XLSX = require("xlsx");
            if (rABS) {
              wb = XLSX.read(btoa(fixdata(binary)), {
                //手动转化
                type: "base64",
              });
            } else {
              wb = XLSX.read(binary, {
                type: "binary",
              });
            }
            outdata = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]]); //outdata就是你想要的东西

            const keyObj = {
              序列号: "rfno",
              布草类型: "kind",
              库存: "stock",
              所属部门: "section",
              备注: "note",
            };

            outdata.forEach((item) => {
              // 将中文的键名替换成英文的
              for (let k in keyObj) {
                let newKey = keyObj[k];
                if (newKey) {
                  item[newKey] = item[k];
                  delete item[k];
                }
              }
              item["date"] = _this.formatDate(item["date"], "-"); //如果有时间格式可加上这一句
            });

            if (outdata.length > 0) {
              request.get("/rfid_kinds/insertall", this.outdata).then(res => {
                console.log(res)
              })
            }
          };
          reader.readAsArrayBuffer(f);
        };
        if (rABS) {
          reader.readAsArrayBuffer(f);
        } else {
          reader.readAsBinaryString(f);
        }
      }
    },
    //数据导出：从前端导出
    handleDownload() {
      var workbook = XLSX.utils.book_new();//新建一个新的工作表
      var worksheet = XLSX.utils.json_to_sheet(this.RFIDtable,{heard:["rfno","kind","stock","section","note"]});//从 JS 值数组的数组创建工作表
      XLSX.utils.book_append_sheet(workbook, worksheet, "RFID分类表");//将工作表附加到工作簿
      // let workbook = XLSX.utils.table_to_book(document.getElementById('table'))//通过抓取页面中的 HTML TABLE 创建工作表
      try {
        XLSX.utils.sheet_add_aoa(worksheet, [
          // <-- Do nothing in row 4
          [ "布草类型", "库存", "所属部门","备注","序列号",/*F1*/]  // <-- Write "abc" to cell E5
        ]);
        XLSX.writeFile(workbook, 'RFID标签分类数据.xlsx')//导出工作表
      } catch(e) {
        console.log(e, workbook);
      }
    },
  }
}

</script>
