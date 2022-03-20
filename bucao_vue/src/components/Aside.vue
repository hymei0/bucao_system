 <!-- 侧边栏组件 -->
<template>
  <div>

    <el-menu
        style="width: 200px;min-height:calc(100vh - 50px)"
        default-active="system_data"
        class="el-menu-vertical-demo"
        background-color="#334157" text-color="#fff" active-text-color="#ffd04b"
    >

      <el-sub-menu index="system_data">
        <template #title>
          <el-icon><location /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >系统数据管理</span>
        </template>
          <el-menu-item index="RFID_kinds" style="padding-left: 60px; font-size:small">
            <ul>
              <li>
                RFID标签类型管理
              </li>
            </ul>
          </el-menu-item>
          <el-menu-item index="bucao_info" style="padding-left: 60px; font-size:small">
            <ul>
              <li>
                布草信息管理
              </li>
            </ul>
          </el-menu-item>
          <el-menu-item index="usr_info" style="padding-left: 60px; font-size:small">
            <ul>
              <li>
                用户信息管理
              </li>
            </ul>
          </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="chart">
        <template #title>
          <el-icon><location /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >图表</span>
        </template>

        <el-menu-item index="visual" style="padding-left: 60px; font-size:small">
          <ul>
            <li>
              数据可视化
            </li>
          </ul>
        </el-menu-item>
      </el-sub-menu>

      <el-sub-menu index="chart">
        <template #title>
          <el-icon><location /></el-icon>
          <span style="font-size: medium;font-weight: bold;" >系统管理</span>
        </template>

        <el-menu-item index="visual" style="padding-left: 60px; font-size:small">
          <ul>
            <li>
              数据可视化
            </li>
          </ul>
        </el-menu-item>
      </el-sub-menu>

    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside"
}
</script>

<style scoped>

</style>
