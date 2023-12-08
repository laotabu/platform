<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>IT周报板块</h2>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>记录日常</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>难点</h4>
            <ul>
              <li>别急，别催</li>
<!--              <li>Spring Security</li>-->
<!--              <li>JWT</li>-->
<!--              <li>MyBatis</li>-->
<!--              <li>Druid</li>-->
<!--              <li>Fastjson</li>-->
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>痛点</h4>
            <ul>
              <li>遇事不决刷新缓存</li>
<!--              <li>Vuex</li>-->
<!--              <li>Element-ui</li>-->
<!--              <li>Axios</li>-->
<!--              <li>Sass</li>-->
<!--              <li>Quill</li>-->
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />

    <el-dialog  :close-on-click-modal="false" :visible.sync="previewVisible" width="100%"  @close="closePreview">
      <div style="overflow-x: hidden; overflow-y: scroll">
        <weeklyFrame />
      </div>
    </el-dialog>

  </div>
</template>

<script>
import weeklyFrame from '@/views/integration/it/weekly/components/weeklyFrame'

export default {
  name: "weekly",
  components: { weeklyFrame },
  data(){
    return {
      previewVisible:""
    }
  },
  methods: {
    closePreview() {
      this.previewVisible = false
    },
  },
  created() {
    this.previewVisible = true
  }
};
</script>
