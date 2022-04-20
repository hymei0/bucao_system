<template>
  <div :style="background" class="bgBackground">
    <div class="typer"  >
      <div class="typer-content" >
        <p class="typer-static" >&nbsp;</p>
        <!-- 动态变化的内容-->
        <p class="typer-dynamic" >
          <span class="cut" >
            <span class="word" v-for="(letter,index) in words" :key="index">{{letter}}</span>
          </span>
          <!-- 模拟光标-->
          <span class="typer-cursor"></span>
        </p>
      </div>
    </div>
    <div style="position: absolute;padding-top: 18%;color: cadetblue ;margin-left: 3%;margin-right: 2%">
      <h3>医院介绍</h3>
      <p style="padding-top: 1%;text-indent:2em;">
        医院床位总规模2000张，员工3000余名，其中高级职称783名，博士125名，正副教授141名，硕博士导师77名。医院设立50个临床医技专科，拥有美国瓦里安VitalBeam智能化直线加速器、3.0T压缩感知核磁共振、256排宽体极速CT、美国GE DSA一体化复合手术室、史道斯一体化腔镜手术室等国际先进的医疗大型设备，以建设“国内一流、省内领先、特色鲜明的研究型医院”为战略目标，以人才建设、科教创新为支撑，以科研转化为动力，以肿瘤、烧创伤、消化等学科为引领，医、教、研协同发展，努力提供满足人民群众对美好健康生活向往的医疗服务。
      </p>
    </div>

  </div>

</template>

<script>
import request from "@/utils/request";

export default {
  name: "home",
  data () {
    return {
      // 顶部导航背景图片配置
      background: {
        // 背景图片地址
        backgroundImage: 'url(' + require('../../assets/img/background/background1.jpg') + ')',
        // 背景图片是否重复
        backgroundRepeat: 'no-repeat',
        // 背景图片大小
        backgroundSize: 'cover',
        // 背景颜色
        backgroundColor: '#000',
        // 背景图片位置
        backgroundPosition: 'center top'
      },

      words:[],               //字母数组push，pop的载体
      str:"仁济天下，至臻至善。",          //str初始化
      letters:[],             //str分解后的字母数组
      order:1,                //表示当前是第几句话
      user:{},
    }
  },
  watch:{                     //监听order值的变化，改变str的内容
    order(old,newV){
      if(this.order%4==1){
        this.str="仁济天下，至臻至善。"
      }
      else if(this.order%4==2){
        this.str="悬壶济世，白衣丹心。"
      }
      else if(this.order%4==3){
        this.str="医病祛疾，精诚之至。"
      }
      else{
        this.str="精医先锋，仁心出众。"
      }

    }

  },

  mounted(){            //页面初次加载后调用begin()开始动画
    this.begin()
  },
  methods:{
    //开始输入的效果动画
    begin(){
      this.letters=this.str.split("")
      for(var i=0;i<this.letters.length;i++){
        setTimeout(this.write(i),i*400);
      }
    },
    //开始删除的效果动画
    back(){
      let L=this.letters.length;
      for(var i=0;i<L;i++){
        setTimeout(this.wipe(i),i*15);
      }
    },
    //输入字母
    write(i){
      return ()=>{
        let L=this.letters.length;
        this.words.push(this.letters[i]);
        let that=this;
        /*如果输入完毕，在2s后开始删除*/
        if(i==L-1){
          setTimeout(function(){
            that.back();
          },1500);

        }
      }
    },
    //擦掉(删除)字母
    wipe(i){
      return ()=>{
        this.words.pop(this.letters[i]);
        /*如果删除完毕，在300ms后开始输入*/
        if(this.words.length==0){
          this.order++;
          let that=this;
          setTimeout(function(){
            that.begin();
          },300);

        }
      }
    },

  },
}
</script>


<style scoped lang="less">
@thePink: #038681;
.typer{
  margin-top: 18%;
  margin-left: 35%;
  box-sizing: border-box;
  height: 10%;
}
.typer .typer-content{
  font-weight: bold;
  font-size: 30px;
  display: flex;
  flex-direction: row;
  letter-spacing: 2px
}
.typer-dynamic{
  position: relative;
}
.cut{
  color: @thePink;
}
.typer-cursor{
  position: absolute;
  height: 100%;
  width: 3px;
  top: 0;
  right: -10px;
  background-color: @thePink;
  animation: flash 1.5s linear infinite;
}

.bgBackground{
  width:100%;
  height:auto;
}

#login {
  background: url("../../assets/img/background/background2.jpg") no-repeat;
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

</style>
