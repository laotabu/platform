<template>
    <!-- 审批对话框 -->
    <el-form :model="form" ref="form" label-width="100px" class="demo-dynamic">
      <el-form-item
        v-for="(domain, index) in form.formData"
        :label="domain.controlLable"
        :key="index"
      >
        <el-radio-group v-model="domain.controlValue" v-if="'radio'==domain.controlType">
          <el-radio v-for="(defaults,indexd) in domain.controlDefault.split('--__--')"
                    :label=indexd
                    :key="indexd"
                    >{{defaults}}
          </el-radio>
        </el-radio-group>
        <el-input type="textarea" v-model="domain.controlValue" v-if="'textarea'==domain.controlType"
        ></el-input>
      </el-form-item>
    </el-form>
</template>

<script>

export default {
  name: "leaveForm",
  props: {
    form: {
      type: Object
    }
  },
  data() {
    return {
    };
  },
  methods: {

  }
};
</script>
