<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules"  label-width="140px" >
      <el-form-item label="申请人" prop="applicant">
        <el-input v-model="form.applicant" disabled />
      </el-form-item>
      <el-form-item label="申请日期" prop="applicationDate">
        <el-date-picker clearable
                        v-model="form.applicationDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        disabled>
        </el-date-picker>
      </el-form-item>
      <el-form-item label="需求时间" prop="demandTime">
        <el-date-picker clearable
                        v-model="form.demandTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        disabled>
        </el-date-picker>
      </el-form-item>
      <el-form-item label="申请原因类型" prop="outlineOfReasons">
        <el-radio-group v-model="form.outlineOfReasons" disabled>
          <el-radio
            v-for="dict in dict.type.fixed_assets_outline_of_reasons"
            :key="dict.value"
            :label="dict.value"
          >{{dict.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="申请理由详细内容" prop="detailsOfReason" >
        <el-input v-model="form.detailsOfReason" type="textarea" disabled/>
      </el-form-item>
      <el-form-item label="使用部门" prop="department">
        <el-input v-model="form.department" disabled />
      </el-form-item>
      <el-form-item label="预算类型" prop="budgetType">
        <el-radio-group v-model="form.budgetType" disabled>
          <el-radio
            v-for="dict in dict.type.fixed_assets_budget_type"
            :key="dict.value"
            :label="dict.value"
          >{{dict.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="资产清单列表" >
        <el-table
          :data="form.listOfAssets"
          :row-class-name="rowClassName"
          ref="tb"
          v-loading="loadListOfAssets"
        >
          <el-table-column type="expand">
            <template slot-scope="scope">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="资产类别">
                  <span>{{ form.listOfAssets[scope.row.xh-1].assetType }}</span>
                </el-form-item>
                <el-form-item label="品牌">
                  <span>{{ form.listOfAssets[scope.row.xh-1].brand }}</span>
                </el-form-item>
                <el-form-item label="规格型号">
                  <span>{{ form.listOfAssets[scope.row.xh-1].specificationAndModel }}</span>
                </el-form-item>
                <el-form-item label="单位">
                  <span>{{ units[form.listOfAssets[scope.row.xh-1].unit - 1].name }}</span>
                </el-form-item>
                <el-form-item label="数量">
                  <span>{{ form.listOfAssets[scope.row.xh-1].quantity }}</span>
                </el-form-item>
                <el-form-item label="预估金额">
                  <span>{{ form.listOfAssets[scope.row.xh-1].estimatedAmount }}</span>
                </el-form-item>
                <el-form-item label="存放地点" >
                    <span>{{ form.listOfAssets[scope.row.xh-1].placeOfStorageName }}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column label="序号" align="center" prop="xh" width="50"></el-table-column>
          <el-table-column label="资产名称" align="center" >
            <template slot-scope="scope">
                <span>{{ form.listOfAssets[scope.row.xh-1].assetName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="资产编号" align="center" >
            <template slot-scope="scope">
              <span>{{ form.listOfAssets[scope.row.xh-1].assetNumber }}</span>
            </template>
<!--            <template slot-scope="scope">-->
<!--              <el-input  v-model="form.listOfAssets[scope.row.xh-1].assetNumber" placeholder="请输入资产编号"></el-input>-->
<!--            </template>-->
          </el-table-column>
          <el-table-column label="原值" align="center" >
            <template slot-scope="scope">
              <span>{{ form.listOfAssets[scope.row.xh-1].originalPrice }}</span>
            </template>
<!--            <template slot-scope="scope">-->
<!--              <el-input  v-model="form.listOfAssets[scope.row.xh-1].originalPrice" placeholder="请输入原值"></el-input>-->
<!--            </template>-->
          </el-table-column>
          <el-table-column label="报销单号" align="center">
            <template slot-scope="scope">
<!--              {{form.listOfAssets[scope.row.xh-1].reimbursementNumber}}   'listOfAssets['+scope.row.xh-1+']reimbursementNumber'-->
              <el-form-item :prop="'listOfAssets[' + scope.$index +'].reimbursementNumber'" :rules="rules.listOfAssets.reimbursementNumber" >
                 <el-input  type="textarea" v-model="form.listOfAssets[scope.$index].reimbursementNumber" placeholder="请输入报销单号"></el-input>
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column label="保管人名称" align="center" >
            <template slot-scope="scope">
              <span>{{ form.listOfAssets[scope.row.xh-1].preserverName }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="申请人附件说明" prop="attachments">
        <el-button style="margin-left: 10px;" size="small" type="success" @click="viewFile">查看附件</el-button>
      </el-form-item>
      <el-form-item label="IT部配置意见" prop="configurationAdvice">
        <el-radio-group v-model="form.configurationAdvice">
          <el-radio
            v-for="dict in dict.type.fixed_assets_configuration_advice"
            :key="dict.value"
            :label="dict.value"
          >{{dict.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="form.projectName" placeholder="请输入项目名称" />
      </el-form-item>
      <el-form-item label="项目编号" prop="projectCode">
        <el-input v-model="form.projectCode" placeholder="请输入项目编号" />
      </el-form-item>
      <el-form-item label="项目类型" prop="projectType">
        <el-radio-group v-model="form.projectType">
          <el-radio
            v-for="dict in dict.type.fixed_assets_project_type"
            :key="dict.value"
            :label="dict.value"
          >{{dict.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="资金类别" prop="fundCategory">
        <el-radio-group v-model="form.fundCategory">
          <el-radio
            v-for="dict in dict.type.fixed_assets_fund_category"
            :key="dict.value"
            :label="dict.value"
          >{{dict.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <!--附件预览对话框-->
    <el-dialog
      :visible.sync="attachmentsVisible"
      title="附件预览"
      width="100%"
      append-to-body
      :fullscreen='true'
    >
      <el-carousel indicator-position="outside" height="800px" :autoplay="false">
        <el-carousel-item v-for="item in this.attachmentData" :key="item.url">
          <vue-office-docx :src="item.url"  v-if="item.extension == 'docx' || item.extension == 'doc'"/>
          <!--            <div id="officeDocx" v-if="item.extension == 'docx' || item.extension == 'doc'" />-->
          <vue-office-excel :src="item.url"   v-if="item.extension == 'xlsx'"/>
          <vue-office-pdf :src="item.url"   v-if="item.extension == 'pdf' "/>
          <img :key="item.url" :src="item.url" v-if="item.extension == 'jpg' || item.extension == 'png'" class="imgView">
          <div class="txtView"  v-if="item.extension == 'txt'" >
            {{showTxtContent(item.url)}}
          </div>
          <!--          <h1>{{item.url}} - {{item.extension}}</h1>-->
        </el-carousel-item>
      </el-carousel>
    </el-dialog>
  </div>


</template>

<script>
import {
  getAssetsInfoByBusinessKey,
} from "@/api/integration/workflow/assets";
import {
  getAssetsDetailsListByIds
} from  "@/api/integration/workflow/assetsDetails"
import {getUnits} from "@/api/integration/workflow/assetsDetails"
import VueOfficeDocx from '@vue-office/docx'
import "@vue-office/docx/lib/index.css";
import VueOfficeExcel from '@vue-office/excel';
import '@vue-office/excel/lib/index.css';
import VueOfficePdf from '@vue-office/pdf';
import $ from 'jquery'
export default {
  name: "fixedAssetsForm",
  dicts: ['fixed_assets_project_type', 'fixed_assets_configuration_advice',
    'fixed_assets_outline_of_reasons', 'fixed_assets_fund_category','fixed_assets_budget_type'],
  props: {
    stage: {
      type: Object
    }
  },
  components: {VueOfficeDocx, VueOfficeExcel, VueOfficePdf},
  data() {
    return {
      // 查询参数
      // queryParams: {
      //   pageNum: 1,
      //   pageSize: 10,
      //   serialNumber: null,
      //   applicant: null,
      //   applicationDate: null,
      //   department: null,
      //   projectName: null,
      //   projectCode: null,
      //   projectType: null,
      //   fundCategory: null,
      //   demandTime: null,
      //   outlineOfReasons: null,
      //   detailsOfReason: null,
      //   enableIdentificationProcessing: null,
      //   listOfAssets: null,
      //   attachments: null,
      //   configurationAdvice: null,
      // },
      // 表单参数
      form: {},
      deptOptions: [],
      units:"",
      loadListOfAssets: true,
      // 表单校验
      rules: {
        configurationAdvice: [
          { required: this.stage.formData.stage=='it', message: "IT部配置意见不能为空", trigger: "blur" }
        ],
        // 表单校验遇到对象数组的时候
        listOfAssets: {
          reimbursementNumber: [
            { required: this.stage.formData.stage=='buyer', message: "报销单不能为空", trigger: "blur" }
          ]
        }
        // applicant: [
        //   { required: true, message: "申请人不能为空", trigger: "blur" }
        // ],
        // applicationDate: [
        //   { required: true, message: "申请日期不能为空", trigger: "blur" }
        // ],
        // demandTime: [
        //   { required: true, message: "需求时间不能为空", trigger: "blur" }
        // ],
        // outlineOfReasons: [
        //   { required: true, message: "申请原因不能为空", trigger: "change" }
        // ],
        // detailsOfReason: [
        //   { required: true, message: "申请理由详细内容不能为空", trigger: "blur" }
        // ],
        // listOfAssets: [
        //   { required: true, message: "固资申请列表不能为空", trigger: "blur" }
        //   // { validator: validateLength, trigger: "blur" }
        // ],
        // budgetType: [
        //   { required: true, message: "预算类型不能为空", trigger: "blur" }
        //   // { validator: validateLength, trigger: "blur" }
        // ],
      },
      // 附件预览
      attachmentsVisible: false,
      attachmentData: null


    };
  },
  created() {
    this.getFixedAssetsInfo(this.stage.formData.businessKey);
  },
  methods: {

    /** 回写表单 */
    getFixedAssetsInfo(businessKey){
      getAssetsInfoByBusinessKey(businessKey).then(response => {
        this.form = response.data;
        let listOfAssets =  this.form.listOfAssetsStr.split(',').map(Number);
        getAssetsDetailsListByIds(listOfAssets).then(response => {
          this.form.listOfAssets = response.data;

          getUnits().then(response=>{
            this.units = response.data;
            this.loadListOfAssets = false;
          });
        })
      })
    },
    rowClassName({ row, rowIndex }) {
      row.xh = rowIndex + 1;
    },
    viewFile(){
      let attachments = this.form.attachments;
      if (attachments == undefined || attachments == ''){
        this.$modal.msgWarning("未上传附件");
      }else {
        let attachmentUrls = attachments.split(",");
        let data = [];
        for (const url of attachmentUrls) {
          data.push({
            "extension": url.split(".").pop().split("?")[0],
            "url": url
          })
        }
        this.attachmentData = data;
        this.attachmentsVisible = true;
      }
    },
    showTxtContent(url){
      return $.ajax({
        url: url,
        type: 'get',
        headers: {
          'Content-Type': 'application/json;charset=UTF-8'
        },
        async: false
      }).responseText;
    }

  }
};
</script>

<style scoped>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
/*.el-carousel__container {*/
/*  height: 100%;*/
/*}*/
.vue-office-pdf {
  max-width: 100%;
  max-height: 100%;
}

.vue-office-excel{
  max-width: 100%;
  max-height: 100%;
}
.vue-office-docx {
  max-width: 100%;
  max-height: 100%;
}
.imgView {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.txtView {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #fff;
  padding: 22px 32px 28px;
  border-radius: 10px;
  height: 100%;
  overflow: auto;
}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
