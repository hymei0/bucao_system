
<template>
  <!-- 组件主盒子 -->
  <div class="stbox">
    <!-- 面包屑导航 -->
    <el-breadcrumb prefix-icon="arrow-right-bold " style="width: 100%;margin-top: 10px;margin-left: 10px">
      <el-breadcrumb-item style="font-size: large; ">图表</el-breadcrumb-item>
      <el-breadcrumb-item style="font-size: large; ">系统数据可视化</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索，切换 -->
    <el-row :gutter="23">
      <el-col :span="18">
        <el-divider></el-divider>
        <el-alert
            title="欢迎使用 , 系统已正常运行38天"
            type="success">
          </el-alert>
      </el-col>
      <el-col :span="6">
      </el-col>
    </el-row>
    <!-- 统计图 -->
    <el-row :gutter="23">
      <el-col :span="8" class="text-c">
        <div class="st-gbox">
          <div class="cavasbox" ref="INDATA_chart"></div>
        </div>
      </el-col>
      <el-col :span="8" class="text-c">
        <div class="st-gbox">
          <div class="cavasbox" ref="OUTDATA_Echart"></div>
        </div>
      </el-col>
      <el-col :span="8" class="text-c">
        <div class="st-gbox">
          <div class="cavasbox" ref="ScrapEchart"></div>
        </div>
      </el-col>
    </el-row>
    <!-- 统计图 -->
    <div>
      <el-row :gutter="23">
        <el-col :span="12" class="text-c">
          <div class="paybox">
            <div class="cavasbox" ref="BucaoKindsEchart"></div>
          </div>
        </el-col>
        <el-col :span="12" class="text-c">
          <div class="paybox">
            <div class="cavasbox" ref="Distribute_Echart"></div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script type="text/ecmascript-6">

import * as echarts from 'echarts';
import request from "@/utils/request";
export default {
  name: "statistic",
  data() {
    return {
      machineNo: '',
      type: 'day',

      //  布草入库总量条形统计图
      INDATAoption: {
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b}月 : {c}"
        },
        legend: {
          data: [{
            name: '2022年布草入库总量',
            icon: 'rect'
          }],
          top: 1,
          left: 1,
          itemGap: 10,
          itemWidth: 12,
          itemHeight: 12,
          textStyle: {
            fontSize: 12,
            color: "#323232"
          }
        },
        grid: {
          left: 50,
          right: 10,
          top: 30,
          bottom: 30,
          borderWidth: 1
        },
        xAxis: {
          type: 'category',
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
          axisLine: {
            lineStyle: {
              color: "#999999",
              width: 1
            }
          },
          axisLabel: {
            margin: 14,
            height: 70,
            interval: 0,
            textStyle: {
              fontSize: 10,
              color: "#999999"
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: "#999999",
              width: 1
            }
          },
          axisLabel: {
            margin: 14,
            textStyle: {
              fontSize: 10,
              color: "#999999"
            }
          }
        },
        series: [{
          name: '2022年布草入库总量',
          type: 'bar',
          barGap: 0,
          data: [0,0,0,0,0,0,0,0,0,0,0,0],
          barWidth: 10,
          itemStyle: {
            normal: {
              color: new echarts.graphic.LinearGradient(
                0, 0, 0, 1,
                [
                  { offset: 0, color: '#83bff6' },
                  { offset: 0.5, color: '#188df0' },
                  { offset: 1, color: '#188df0' }
                ]
              )
            },
            emphasis: {
              color: new echarts.graphic.LinearGradient(
                0, 0, 0, 1,
                [
                  { offset: 0, color: '#2378f7' },
                  { offset: 0.7, color: '#2378f7' },
                  { offset: 1, color: '#83bff6' }
                ]
              )
            }
          }
        }]
      },
      //  布草回收情况折线统计图
      OUTDATA_option: {
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b}月 : {c}"
        },
        legend: {
          data: [{
            name: '2022年布草回收情况',
            icon: 'rect'
          }],
          top: 1,
          left: 1,
          itemGap: 10,
          itemWidth: 12,
          itemHeight: 12,
          textStyle: {
            fontSize: 12,
            color: "#323232"
          }
        },
        grid: {
          left: 50,
          right: 10,
          top: 30,
          bottom: 30,
          borderWidth: 1
        },
        xAxis: {
          type: 'category',
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
          axisLine: {
            lineStyle: {
              color: "#999999",
              width: 1
            }
          },
          axisLabel: {
            margin: 14,
            height: 70,
            interval: 0,
            textStyle: {
              fontSize: 10,
              color: "#999999"
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: "#999999",
              width: 1
            }
          },
          axisLabel: {
            margin: 14,
            textStyle: {
              fontSize: 10,
              color: "#999999"
            }
          }
        },
        series: [{
          name: '2022年布草回收情况',
          //   type: 'bar',
          type: 'line',
          barGap: 0,
          data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          barWidth: 10,
          itemStyle: {
            color: "#108ff9"
          }
        }]
      },
      //  布草报废情况条形统计图
      Scrapoption: {
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b}月 : {c}"
        },
        legend: {
          data: [{
            name: '2022年布草报废情况汇总',
            icon: 'rect'
          }],
          top: 1,
          left: 1,
          itemGap: 10,
          itemWidth: 12,
          itemHeight: 12,
          textStyle: {
            fontSize: 12,
            color: "#323232"
          }
        },
        grid: {
          left: 50,
          right: 10,
          top: 30,
          bottom: 30,
          borderWidth: 1
        },
        xAxis: {
          type: 'category',
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
          axisLine: {
            lineStyle: {
              color: "#999999",
              width: 1
            }
          },
          axisLabel: {
            margin: 14,
            height: 70,
            interval: 0,
            textStyle: {
              fontSize: 10,
              color: "#999999"
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: "#999999",
              width: 1
            }
          },
          axisLabel: {
            margin: 14,
            textStyle: {
              fontSize: 10,
              color: "#999999"
            }
          }
        },
        series: [{
          name: '2022年布草报废情况汇总',
          type: 'bar',
          barGap: 0,
          data: [0, 0,0,0,0,0,0,0,0,0,0,0],
          barWidth: 10,
          itemStyle: {
            color: "#48cefd"
          }
        }]
      },
      //  医院布草类型信息统计图
      BucaoKindsoption: {
        backgroundColor: '#2c343c',
        title: {
          text: '医院布草类型统计信息',
          left: 10,
          top: 5,
          textStyle: {
            fontSize: 12,
            color: '#ccc'
          }
        },

        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },

        visualMap: {
          show: false,
          min: 80,
          max: 600,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [
          {
            name: '医院布草类型统计信息',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: [].sort(function (a, b) { return a.value - b.value; }),
            roseType: 'area',
            label: {
              normal: {
                textStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                }
              }
            },
            labelLine: {
              normal: {
                lineStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                },
                smooth: 0.2,
                length: 10,
                length2: 20
              }
            },
            itemStyle: {
              normal: {
                color: '#e71414',
                shadowBlur: 200,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function (idx) {
              return Math.random() * 200;
            }
          }
        ]
      },
      Distribute_option: {
        backgroundColor: '#2c343c',
        title: {
          text: '医院布草分布信息',
          left: 10,
          top: 5,
          textStyle: {
            fontSize: 12,
            color: '#ccc'
          }
        },

        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },

        visualMap: {
          show: false,
          min: 80,
          max: 600,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [
          {
            name: '医院布草分布信息',
            type: 'pie',
            radius: '55%',
            center: ['50%', '50%'],
            data: [
            ].sort(function (a, b) { return a.value - b.value; }),
            roseType: 'radius',
            label: {
              normal: {
                textStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                }
              }
            },
            labelLine: {
              normal: {
                lineStyle: {
                  color: 'rgba(255, 255, 255, 0.3)'
                },
                smooth: 0.2,
                length: 10,
                length2: 20
              }
            },
            itemStyle: {
              normal: {
                color: '#c23531',
                shadowBlur: 200,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },

            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function (idx) {
              return Math.random() * 200;
            }
          }
        ]
      },

    }
  },
  // 导入组件
  components: {
    // 点聚合组件
  },
  // 创建完毕状态(里面是操作)
  created() {

  },
  // 挂载结束状态(里面是操作)
  mounted() {
    this.getINDATA()
    this.getOUTDATA_()
    this.getScrap()
    this.getBucaoKinds()
    this.getDistribute_()
  },
  // 里面的函数只有调用才会执行
  methods: {
    // 布草入库数据统计
    getINDATA() {
      this.chart1 = echarts.init(this.$refs.INDATA_chart)
      request.get("/Bucao_info/indata").then(res=>{
          for(var i=0;i<res.data.length;i++)
          {
             this.INDATAoption.series[0].data[parseInt(res.data[i].month)-1]=res.data[i].num
          }
        this.chart1.setOption(this.INDATAoption,true)
      })

    },
    // 布草出库数据统计
    getOUTDATA_() {
      this.chart2 = echarts.init(this.$refs.OUTDATA_Echart)
      request.get("/Bucao_info/outdata").then(res=>{
        for(var i=0;i<res.data.length;i++)
        {
          this.OUTDATA_option.series[0].data[parseInt(res.data[i].month)-1]=res.data[i].num
        }
        this.chart2.setOption(this.OUTDATA_option,true)
      })

    },
    // 布草报废数据统计
    getScrap() {
      this.chart3 = echarts.init(this.$refs.ScrapEchart)
      request.get("/Bucao_info/outdata").then(res=>{
        for(var i=0;i<res.data.length;i++)
        {
          this.Scrapoption.series[0].data[parseInt(res.data[i].month)-1]=res.data[i].num
        }
        this.chart3.setOption(this.Scrapoption,true)
      })

    },
    // 布草种类库存据统计
    getBucaoKinds() {
      this.chart4 = echarts.init(this.$refs.BucaoKindsEchart)
      request.get("/rfid_kinds/kinds_stocks").then(res=>{
        for(var i=0;i<res.data.length;i++)
        {
          this.BucaoKindsoption.series[0].data.push({name:res.data[i].kind,value:res.data[i].stocks})
        }
        this.BucaoKindsoption.series[0].data.sort(function (a, b) { return a.value - b.value; }),

            this.chart4.setOption(this.BucaoKindsoption,true)
      })
    },
    // 布草分布数据统计
    getDistribute_() {
      this.chart5 = echarts.init(this.$refs.Distribute_Echart)
      request.get("/rfid_kinds/section_stocks").then(res=>{
        for(var i=0;i<res.data.length;i++)
        {
          this.Distribute_option.series[0].data.push({name:res.data[i].section,value:res.data[i].stocks})
        }
        this.Distribute_option.series[0].data.sort(function (a, b) { return a.value - b.value; }),

            this.chart5.setOption(this.Distribute_option,true)
      })

    }

  }
};
</script>
<style>
.stbox {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}
.stbgc {
  background-color: #fff;
  height: 60px;
  line-height: 60px;
  border-radius: 5px;
  padding: 0px 16px;
}
.stsearch {
  text-align: center;
}
.text-c {
  text-align: center;
}
.st-gbox {
  background-color: #fff;
  margin-top: 20px;
  border-radius: 5px;
  height: 30vh;
  box-sizing: border-box;
  padding: 10px;
}
.cavasbox {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
}
.paybox {
  width: 100%;
  background-color: #fff;
  box-sizing: border-box;
  border-radius: 5px;
  margin-top: 20px;
  height: 32vh;
}
</style>
