<template>
  <div class="app-container">


    <el-table v-loading="loading" :data="tastList">
      <!--      <el-table-column label="流程ID" align="center" prop="id"/>-->
      <el-table-column label="流程名称" align="center" prop="instanceName"/>
      <el-table-column label="任务节点名称" align="center" prop="name"/>
      <el-table-column label="任务状态" align="center" prop="status"/>
      <el-table-column label="办理人" align="center" prop="assignee"/>
      <el-table-column label="创建时间" align="center" prop="createdDate"/>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="examineAndApprove (scope.row)"
          >审批
<!--            v-hasPermi="['workflow:leave:edit']"-->
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 审批对话框 -->
    <el-dialog :title="title" :visible.sync="open" v-if="open"  append-to-body>
      <!--   请假流程表单【历史】   -->
      <leaveHistoryForm :businessKey="businessKey" v-if="'leave'==definitionKey"/>
      <!--   请假流程表单【当前环节】  -->
      <leaveForm :form="form" label-width="100px" class="demo-dynamic" v-if="'leave'==definitionKey" />

      <!--   固资申请表单【历史】   -->
<!--      <fixedAssetsHistoryForm :businessKey="businessKey" v-if="'fixedAssetRequest'==definitionKey"/>-->
      <!--   固资申请表单【当前环节】   -->
      <fixedAssetsForm :stage="form" class="demo-dynamic" v-if="'fixedAssetRequest'==definitionKey" ref="fixedAssetsForm"/>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">通 过</el-button>
        <el-button type="danger" @click="cancel">驳 回</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>


import {listTask, formDataShow, dynamicformDataSave, customizedformDataSave} from "@/api/activiti/task";
  import leaveHistoryForm from "@/views/integration/workflow/leave/form/leaveHistoryForm";
  import leaveForm from "@/views/integration/workflow/leave/form/leaveForm";
  import fixedAssetsHistoryForm from "@/views/integration/workflow/assets/form/fixedAssetsHistoryForm"
  import fixedAssetsForm from "@/views/integration/workflow/assets/form/fixedAssetsForm"

  export default {
    name: "task",
    components: {leaveHistoryForm, leaveForm, fixedAssetsHistoryForm,fixedAssetsForm},
    data() {
      return {
        id:'',
        definitionKey: '',
        businessKey: '',
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 请假表格数据
        tastList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        // 表单参数
        form: {
          formData:[]
        },
        // 表单校验
        rules: {},

      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询请假列表 */
      getList() {
        this.loading = true;
        listTask(this.queryParams).then(response => {
          this.tastList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },

      // 取消按钮
      cancel() {
        if(this.definitionKey == 'fixedAssetRequest'){
          customizedformDataSave('assets','reject',this.id,this.form.formData).then(response => {
            console.log(response)
            this.$modal.msgSuccess("审批成功");
            this.open = false;
            this.getList();
          });
        }
        this.open = false;
        this.reset();

      },
      // 表单重置
      reset() {
        this.definitionKey = '',
          this.businessKey = '',
          this.form = {
            formData:[],
          };
        this.resetForm("form");
      },

      /** 审批按钮操作 */
      examineAndApprove(row) {
        console.log(row)
        this.reset();
        this.definitionKey = row.definitionKey;
        this.businessKey = row.businessKey;
        this.id=row.id;
        formDataShow(row.id).then(
          response => {
            console.log(response)
            this.storeResponseData(response)
          }
        );
      },
      /** 提交按钮 */
      submitForm() {
        // 处理数据
        if(this.checkfixedAssetRequestData()){
          if (this.definitionKey == 'fixedAssetRequest'){
            // 由于一开始表单设计好了，没有看到动态表单这个，此处不做模仿
            customizedformDataSave('assets','approve',this.id,this.form.formData).then(response => {
              console.log(response)
              this.$modal.msgSuccess("审批成功");
              this.open = false;
              this.getList();
            });
          }else {
            // 动态表单
            dynamicformDataSave(this.id,this.form.formData).then(response => {
              this.$modal.msgSuccess("审批成功");
              this.open = false;
              this.getList();
            });
          }
        }
      },


      checkfixedAssetRequestData(){
        switch (this.definitionKey){
          case "fixedAssetRequest":
            // 获取子组件的值
            let fixedAssetsForm = this.$refs.fixedAssetsForm
            // console.log(assetstitle)
            let sign = false
            fixedAssetsForm.$refs["form"].validate(valid => {
              if (!valid){
                this.$modal.msgError("请检查固资申请详细清单文件填写是否完整");
              }else {
                // 设置审核信息
                let auditData = {
                  "status": 0,
                  "remark": ""
                }
                if (this.form.formData.stage == "it"){
                  auditData.remark = ""
                }else if(this.form.formData.stage == "buyer"){
                  auditData.remark = ""
                }
                fixedAssetsForm.form.customerWorkflowFixedAssetsAuditData = auditData;
                this.form.formData = fixedAssetsForm.form
                console.log(this.form.formData)
                sign = true
              }
            })

            return sign;
            break;
          case "leave":
            return true;
            break;
        }
      },
      checkAssetsDetails(assetsDetails){
        // todo 财务要检查
        return true;
      },
      /**
       * 保存响应数据【表单回写的数据】
       * @param response
       */
      storeResponseData(response){
        let datas = response.data;
        let formData = []
        switch (this.definitionKey){
          case "leave":
            for (let i = 0; i < datas.length; i++) {
              // FormProperty_3qipis2--__!!radio--__!!审批意见--__!!i--__!!同意--__--不同意
              // FormProperty_0lffpcm--__!!textarea--__!!批注--__!!f--__!!null
              let strings = datas[i].split('--__!!')
              let controlValue = null
              let controlDefault = null
              switch (strings[1]) {
                case 'radio':
                  controlValue = 0;
                  controlDefault = strings[4]
                  break;
                // default:
              }
              formData.push({
                controlId: strings[0],
                controlType: strings[1],
                controlLable: strings[2],
                controlIsParam: strings[3],
                controlValue: controlValue,
                controlDefault: controlDefault
              })
            }
            this.title = "审批";
            break;
          case "fixedAssetRequest":
            this.title = "资产申请审批";
            formData = {
              // 当前流程的businessKey
              "businessKey": this.businessKey,
              // 控制流程变量  "FormProperty_003g8u6-stage-it"
              "stage": datas[0].split('-')[2]
            }
        }
        this.form.formData = formData;
        this.open = true;
      },
    }
  };
</script>
