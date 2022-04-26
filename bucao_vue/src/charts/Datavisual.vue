<template>
  <div class="Echarts">
    <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统数据可视化</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 搜索，切换 -->
    <el-row :gutter="23">
      <el-col :span="18">
        <el-divider></el-divider>
        <el-alert
            title="欢迎使用 , 系统已正常运行 38 天"
            type="success">
        </el-alert>

      </el-col>
      <el-col :span="6">
      </el-col>
    </el-row>
    <div id="main" style="width: 100%; height: 400px"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts/core';
import { SunburstChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent } from 'echarts/components';
// 新的接口中不再包含 Canvas 渲染器，如果需要使用 SVG 渲染模式则使用 SVGRenderer
import { CanvasRenderer } from 'echarts/renderers';
echarts.use([SunburstChart, TitleComponent, TooltipComponent, CanvasRenderer]);

export default {
  name:'Home',
  data() {
    // swiper相关配置属性放在swiper_options这个变量里
    return{
      //myChart
      myChart: '',
      option: {}
    }
  },
  mounted: function () {
    //this.myChart = echarts.init(document.getElementById('main'));
    //this.myChart.setOption(this.option);
    this.initEcharts();
  },
  methods: {
    initEcharts(){
      var that = this;
      var myChart = echarts.init(document.getElementById('main'));
      that.option = {
        title:{
          text: "四季",
          subtext: '节气图',
          left: "center"
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b} '
        },
        /* legend: {
          data:['春季','夏季','秋季','冬季'],
        }, */
        series: {
          type: 'sunburst',
          data: [{
            name: '春季',
            value: 12,
            itemStyle: {color: 'hsl(10, 100%, 50%)', borderColor: 'hsl(200, 100%, 50%)', borderWidth: 2, },
            children: [
              {name: '立春', value: 2, itemStyle: {color: 'hsl(315, 100%, 50%)'},},
              {name: '雨水', value: 2, itemStyle: {color: 'hsl(330, 100%, 50%)'},},
              {name: '惊蛰', value: 2, itemStyle: {color: 'hsl(345, 100%, 50%)'},},
              {name: '春分', value: 2, itemStyle: {color: 'hsl(0, 100%, 50%)'},},
              {name: '清明', value: 2, itemStyle: {color: 'hsl(15, 100%, 50%)'},},
              {name: '谷雨', value: 2, itemStyle: {color: 'hsl(30, 100%, 50%)'},},
            ]
          }, {
            name: '夏季',
            value: 12,
            itemStyle: {color: 'hsl(100, 100%, 50%)', borderColor: 'hsl(300, 100%, 50%)', borderWidth: 2,},
            children: [
              {name: '立夏', value: 2, itemStyle: {color: 'hsl(45, 100%, 50%)'},},
              {name: '小满', value: 2, itemStyle: {color: 'hsl(60, 100%, 50%)'},},
              {name: '芒种', value: 2, itemStyle: {color: 'hsl(75, 100%, 50%)'},},
              {name: '夏至', value: 2, itemStyle: {color: 'hsl(90, 100%, 50%)'},},
              {name: '小暑', value: 2, itemStyle: {color: 'hsl(105, 100%, 50%)'},},
              {name: '大暑', value: 2, itemStyle: {color: 'hsl(120, 100%, 50%)'},},
            ]
          }, {
            name: '秋季',
            value: 12,
            itemStyle: {color: 'hsl(190, 100%, 50%)', borderColor: 'hsl(20, 100%, 50%)', borderWidth: 2,},
            children: [
              {name: '立秋', value: 2, itemStyle: {color: 'hsl(135, 100%, 50%)'},},
              {name: '处暑', value: 2, itemStyle: {color: 'hsl(150, 100%, 50%)'},},
              {name: '白露', value: 2, itemStyle: {color: 'hsl(165, 100%, 50%)'},},
              {name: '秋分', value: 2, itemStyle: {color: 'hsl(180, 100%, 50%)'},},
              {name: '寒露', value: 2, itemStyle: {color: 'hsl(195, 100%, 50%)'},},
              {name: '霜降', value: 2, itemStyle: {color: 'hsl(210, 100%, 50%)'},},
            ]
          },{
            name: '冬季',
            value: 12,
            itemStyle: {color: 'hsl(280, 100%, 50%)', borderColor: 'hsl(110, 100%, 50%)', borderWidth: 2,},
            children: [
              {name: '立冬', value: 2, itemStyle: {color: 'hsl(225, 100%, 50%)'},},
              {name: '小雪', value: 2, itemStyle: {color: 'hsl(240, 100%, 50%)'},},
              {name: '大雪', value: 2, itemStyle: {color: 'hsl(255, 100%, 50%)'},},
              {name: '冬至', value: 2, itemStyle: {color: 'hsl(270, 100%, 50%)'},},
              {name: '小寒', value: 2, itemStyle: {color: 'hsl(295, 100%, 50%)'},},
              {name: '大寒', value: 2, itemStyle: {color: 'hsl(300, 100%, 50%)'},},
            ]
          }]
        }
      };
      myChart.setOption(that.option);
    }
  }
}
</script>
