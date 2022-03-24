<template>
  <div>
    <el-card style="width:40%;margin: 10px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="form."></el-input>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Person"
}
</script>

<style scoped>

</style>
