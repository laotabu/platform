<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流水号" prop="serialNumber">
        <el-input
          v-model="queryParams.serialNumber"
          placeholder="请输入流水号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请人" prop="applicant">
        <el-input
          v-model="queryParams.applicant"
          placeholder="请输入申请人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请日期" prop="applicationDate">
        <el-date-picker clearable
          v-model="queryParams.applicationDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择申请日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="使用部门" prop="department">
        <el-input
          v-model="queryParams.department"
          placeholder="请输入使用部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目编号" prop="projectCode">
        <el-input
          v-model="queryParams.projectCode"
          placeholder="请输入项目编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目类型" prop="projectType">
        <el-select v-model="queryParams.projectType" placeholder="请选择项目类型" clearable>
          <el-option
            v-for="dict in dict.type.fixed_assets_project_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="资金类别" prop="fundCategory">
        <el-select v-model="queryParams.fundCategory" placeholder="请选择资金类别" clearable>
          <el-option
            v-for="dict in dict.type.fixed_assets_fund_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="需求时间" prop="demandTime">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.demandTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择需求时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="申请原因" prop="outlineOfReasons">-->
<!--        <el-select v-model="queryParams.outlineOfReasons" placeholder="请选择申请原因" clearable>-->
<!--          <el-option-->
<!--            v-for="dict in dict.type.fixed_assets_outline_of_reasons"-->
<!--            :key="dict.value"-->
<!--            :label="dict.label"-->
<!--            :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="IT部配置意见" prop="configurationAdvice">-->
<!--        <el-select v-model="queryParams.configurationAdvice" placeholder="请选择IT部配置意见" clearable>-->
<!--          <el-option-->
<!--            v-for="dict in dict.type.fixed_assets_configuration_advice"-->
<!--            :key="dict.value"-->
<!--            :label="dict.label"-->
<!--            :value="dict.value"-->
<!--          />-->
<!--        </el-select>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['workflow:assets:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['workflow:assets:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['workflow:assets:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['workflow:assets:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="assetsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="id字段" align="center" prop="id" />-->
      <el-table-column label="申请人" align="center" prop="applicantName" />
      <el-table-column label="申请日期" align="center" prop="applicationDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.applicationDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="使用部门" align="center" prop="departmentName" />
      <el-table-column label="项目名称" align="center" prop="projectName" />
      <el-table-column label="项目编号" align="center" prop="projectCode" />
      <el-table-column label="项目类型" align="center" prop="projectType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fixed_assets_project_type" :value="scope.row.projectType"/>
        </template>
      </el-table-column>
      <el-table-column label="资金类别" align="center" prop="fundCategory">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fixed_assets_fund_category" :value="scope.row.fundCategory"/>
        </template>
      </el-table-column>
      <el-table-column label="领取时间" align="center" prop="demandTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.demandTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请原因" align="center" prop="outlineOfReasons">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fixed_assets_outline_of_reasons" :value="scope.row.outlineOfReasons"/>
        </template>
      </el-table-column>
      <el-table-column label="申请理由详细内容" align="center" prop="detailsOfReason" />
      <el-table-column label="是否启动环境&安全因素识别处理" align="center" prop="enableIdentificationProcessing" />
<!--      <el-table-column label="资产清单列表" align="center" prop="listOfAssets"/>-->
<!--      <el-table-column label="申请人附件说明" align="center" prop="attachments">-->
<!--        <el-button  type="primary" round @click="viewattachments(scope.row)">查看</el-button>-->
<!--      </el-table-column>-->
      <el-table-column label="IT部配置意见" align="center" prop="configurationAdvice">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fixed_assets_configuration_advice" :value="scope.row.configurationAdvice"/>
        </template>
      </el-table-column>

      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.fixed_assets_audit_status" :value="scope.row.status"/>
        </template>

      </el-table-column>

      <el-table-column label="操作" align="center" width="150px" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div class="button-group">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="viewListOfAssets(scope.row)"
            >查看清单</el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="viewAttachments(scope.row)"
            >查看附件</el-button>
          </div>

          <div class="button-group">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="viewApprove(scope.row)"
            >审批详情</el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="schedule(scope.row)"
            >查看进度</el-button>
          </div>

          <div class="button-group">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['workflow:assets:edit']"
              :disabled="scope.row.status != 0"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              :disabled="scope.row.status != 0"
              v-hasPermi="['workflow:assets:remove']"
            >删除</el-button>
          </div>

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

    <!-- 添加或修改固资申请头对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">

        <el-form-item label="申请人" prop="applicant">
          <el-select v-model="form.applicant" filterable placeholder="请选择申请人">
            <el-option
              v-for="item in applicantOptions"
              :key="item.id"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
<!--          <el-input v-model="form.applicant" placeholder="请输入申请人" />-->
        </el-form-item>
        <el-form-item label="申请日期" prop="applicationDate">
          <el-date-picker clearable
            v-model="form.applicationDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择申请日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="使用部门" prop="department">
          <treeselect v-model="form.department" :options="deptOptions" :show-count="true" placeholder="请选择使用部门" />
<!--          <el-input v-model="form.department" placeholder="请输入使用部门" />-->
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
        <el-form-item label="需求时间" prop="demandTime">
          <el-date-picker clearable
            v-model="form.demandTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择需求时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="申请原因类型" prop="outlineOfReasons">
          <el-radio-group v-model="form.outlineOfReasons">
            <el-radio
              v-for="dict in dict.type.fixed_assets_outline_of_reasons"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="申请理由详细内容" prop="detailsOfReason">
          <el-input v-model="form.detailsOfReason" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="资产清单列表" prop="listOfAssets">
          <div display="false" v-model="form.listOfAssets"></div>
          <el-badge :value="assetsLength" class="item" type="primary">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="openListOfAssetsDialog"
              v-hasPermi="['workflow:assets:add']"
            >新增</el-button>
          </el-badge>
        </el-form-item>


        <el-form-item label="申请人附件说明" prop="attachments">
          <el-upload
            class="upload-iamge"
            ref="upload"
            action=''
             accept=".txt, .docx, .pdf, .jpg, .zip, .mp4, .mp3"
            :disabled="upload.isUploading"
            :file-list="upload.fileList"
            :headers="upload.headers"
            :on-change="handleFileChange"
            :on-remove="removeFile"
            :auto-upload="false">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传txt/docx/pdf/jpg/zip/mp4/mp3文件，且不超过500kb</div>
          </el-upload>
<!--          <el-input v-model="form.attachments" type="textarea" placeholder="请输入内容" />-->
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
        <el-form-item label="预算类型" prop="budgetType">
          <el-radio-group v-model="form.budgetType">
            <el-radio
              v-for="dict in dict.type.fixed_assets_budget_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="this.upload.fileList.length > 0" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 使用 AssetDetailsDialog 组件 -->
    <list-of-assets-details :asset-list-p="this.form.listOfAssets" :visibility="listOfAssetsDialogVisible" @visibility-change="handleVisibilityChange"
                            @array-event="handleListOfAssets" />
    <!--  清单查看对话框  -->
    <el-dialog :title="listOfAssetsDetailsTitle" :visible.sync="listOfAssetsDialogViewVisible"  append-to-body>
      <el-table
        :data="listOfAssetsDetailsView"
        :row-class-name="rowClassName"
        ref="tb"
        v-loading="loadListOfAssets"
      >
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="资产类别">
                <span>{{ listOfAssetsDetailsView[scope.row.xh-1].assetType }}</span>
              </el-form-item>
              <el-form-item label="品牌">
                <span>{{ listOfAssetsDetailsView[scope.row.xh-1].brand }}</span>
              </el-form-item>
              <el-form-item label="规格型号">
                <span>{{ listOfAssetsDetailsView[scope.row.xh-1].specificationAndModel }}</span>
              </el-form-item>
              <el-form-item label="单位">
                <span>{{ units[listOfAssetsDetailsView[scope.row.xh-1].unit - 1].name }}</span>
              </el-form-item>
              <el-form-item label="数量">
                <span>{{ listOfAssetsDetailsView[scope.row.xh-1].quantity }}</span>
              </el-form-item>
              <el-form-item label="预估金额">
                <span>{{ listOfAssetsDetailsView[scope.row.xh-1].estimatedAmount }}</span>
              </el-form-item>
              <el-form-item label="存放地点" >
                <span>{{ listOfAssetsDetailsView[scope.row.xh-1].placeOfStorageName }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="序号" align="center" prop="xh" width="50"></el-table-column>
        <el-table-column label="资产名称" align="center" >
          <template slot-scope="scope">
            <span>{{ listOfAssetsDetailsView[scope.row.xh-1].assetName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="资产编号" align="center" >
          <template slot-scope="scope">
            <span>{{ listOfAssetsDetailsView[scope.row.xh-1].assetNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="原值" align="center" >
          <template slot-scope="scope">
            <span>{{ listOfAssetsDetailsView[scope.row.xh-1].originalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column label="报销单号" align="center">
          <template slot-scope="scope">
            <span>{{ listOfAssetsDetailsView[scope.row.xh-1].reimbursementNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column label="保管人名称" align="center" >
          <template slot-scope="scope">
            <span>{{ listOfAssetsDetailsView[scope.row.xh-1].preserverName }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <!--  审批进度对话框  -->
    <el-dialog
      :visible.sync="modelVisible"
      title="进度查询"
      width="1680px"
      append-to-body
    >
      <div style="position:relative;height: 100%;">
        <iframe
          id="iframe"
          :src="modelerUrl"
          frameborder="0"
          width="100%"
          height="720px"
          scrolling="auto"
        ></iframe>
      </div>
    </el-dialog>
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
    <!--查看审批详情对话框-->
    <el-dialog
      :visible.sync="viewApproveVisible"
      title="审批详情"
      append-to-body
    >
      <div class="block">
        <el-timeline>
          <el-timeline-item
            v-for="(activity, index) in activities"
            :key="index"
            :icon="activity.icon"
            :type="activity.type"
            :color="activity.color"
            :size="activity.size"
            :timestamp="activity.timestamp"
            placement="top"
          >
            <el-card>
              <h4>更新 Github 模板</h4>
              <p>{{activity.content}}</p>
            </el-card>

          </el-timeline-item>
        </el-timeline>
<!--        <el-timeline>-->
<!--          <el-timeline-item timestamp="2018/4/12" placement="top">-->

<!--          </el-timeline-item>-->
<!--          <el-timeline-item timestamp="2018/4/3" placement="top">-->
<!--            <el-card>-->
<!--              <h4>更新 Github 模板</h4>-->
<!--              <p>王小虎 提交于 2018/4/3 20:46</p>-->
<!--            </el-card>-->
<!--          </el-timeline-item>-->
<!--          <el-timeline-item timestamp="2018/4/2" placement="top">-->
<!--            <el-card>-->
<!--              <h4>更新 Github 模板</h4>-->
<!--              <p>王小虎 提交于 2018/4/2 20:46</p>-->
<!--            </el-card>-->
<!--          </el-timeline-item>-->
<!--        </el-timeline>-->
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAssets,
  getAssets,
  delAssets,
  addAssets,
  updateAssets,
  uploadAssetsFiles,
} from "@/api/integration/workflow/assets";
import { getAssetsDetailsListByIds } from '@/api/integration/workflow/assetsDetails'
import ListOfAssetsDetails from '@/views/integration/workflow/assets/components/ListOfAssetsDetails'
import {getToken} from "@/utils/auth";
import {_listUser, deptTreeSelect} from "@/api/system/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {getUnits} from "@/api/integration/workflow/assetsDetails"
import {getDefinitionsByBusinesskey} from "@/api/activiti/definition";
import VueOfficeDocx from '@vue-office/docx'
import "@vue-office/docx/lib/index.css";
import VueOfficeExcel from '@vue-office/excel';
import '@vue-office/excel/lib/index.css';
import VueOfficePdf from '@vue-office/pdf';
// import mammoth from "mammoth";
// import axios from 'axios'
import $ from 'jquery'
export default {
  name: "Assets",
  components: { ListOfAssetsDetails, Treeselect,VueOfficeDocx,VueOfficeExcel,VueOfficePdf},
  dicts: ['fixed_assets_project_type', 'fixed_assets_configuration_advice',
    'fixed_assets_outline_of_reasons', 'fixed_assets_fund_category','fixed_assets_budget_type',
    'fixed_assets_audit_status'],
  data() {
    return {
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
      // 固资申请头表格数据
      assetsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serialNumber: null,
        applicant: null,
        applicationDate: null,
        department: null,
        projectName: null,
        projectCode: null,
        projectType: null,
        fundCategory: null,
        demandTime: null,
        outlineOfReasons: null,
        detailsOfReason: null,
        enableIdentificationProcessing: null,
        listOfAssets: null,
        attachments: null,
        configurationAdvice: null,
        status: null,
        departmentName: null
      },
      // 表单参数
      form: {},
      assetsLength: 0,
      listOfAssetsDialogVisible: false,
      deptOptions: '',
      // 表单校验
      rules: {
        serialNumber: [
          { required: true, message: "流水号不能为空", trigger: "blur" }
        ],
        applicant: [
          { required: true, message: "申请人不能为空", trigger: "blur" }
        ],
        applicationDate: [
          { required: true, message: "申请日期不能为空", trigger: "blur" }
        ],
        demandTime: [
          { required: true, message: "需求时间不能为空", trigger: "blur" }
        ],
        outlineOfReasons: [
          { required: true, message: "申请原因不能为空", trigger: "change" }
        ],
        detailsOfReason: [
          { required: true, message: "申请理由详细内容不能为空", trigger: "blur" }
        ],
        listOfAssets: [
          { required: true, message: "固资申请列表不能为空", trigger: "blur" }
          // { validator: validateLength, trigger: "blur" }
        ],
        budgetType: [
          { required: true, message: "预算类型不能为空", trigger: "blur" }
          // { validator: validateLength, trigger: "blur" }
        ],
      },

      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/oos/workflow/upAssetsFile",
        fileList: [],
      },
      listOfAssetsDetailsTitle: "资产清单列表",
      listOfAssetsDetailsView: [],
      loadListOfAssets: true,
      listOfAssetsDialogViewVisible: false,
      units: '',
      applicantOptions: [],
      // 查看审批进度相关变量
      modelVisible: false,
      modelerUrl: '',
      // 查看附件相关变量
      attachmentsVisible: false,
      attachmentData: null,
      // 查看审批详情相关变量
      viewApproveVisible: false,
      activities: [{
        content: '支持使用图标',
        timestamp: '2018-04-12 20:46',
        size: 'large',
        type: 'primary',
        icon: 'el-icon-more'
      }, {
        content: '支持自定义颜色',
        timestamp: '2018-04-03 20:46',
        color: '#0bbd87'
      }, {
        content: '支持自定义尺寸',
        timestamp: '2018-04-03 20:46',
        size: 'large'
      }, {
        content: '默认样式的节点',
        timestamp: '2018-04-03 20:46'
      }]
    };
  },
  created() {
    this.getList();
    // 获取部门列表
    deptTreeSelect().then(response => {
      this.deptOptions = response.data;
    });
  },
  methods: {

    /** 查询固资申请头列表 */
    getList() {
      this.loading = true;
      listAssets(this.queryParams).then(response => {
        console.log(response)
        // getDept(response.data.)
        this.assetsList = response.rows;
        this.total = response.total;
        _listUser('').then(res => {
          let data = []
          for (const i in res) {
            // 插入子选项
            data.push({
              "id": i,
              "value": res[i].nickName+ "," + res[i].userName,
              "label": res[i].nickName + "(" + res[i].userName + ")"
            })
          }
          this.applicantOptions = data;
          this.loading = false;
        }).catch(e => {
          this.$modal.msgError("获取用户列表失败");
        })
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        serialNumber: null,
        applicant: null,
        applicationDate: null,
        department: null,
        projectName: null,
        projectCode: null,
        projectType: null,
        fundCategory: null,
        demandTime: null,
        outlineOfReasons: null,
        detailsOfReason: null,
        enableIdentificationProcessing: null,
        listOfAssets: [],
        attachments: null,
        configurationAdvice: null,
        budgetType: null,
        title: "固资申请"
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "固资申请";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      // console.log(row)
      const id = row.id || this.ids
      getAssets(id).then(response => {
        console.log(response)
        this.form = response.data;
        this.form.applicant = response.data.applicantName + "(" + response.data.applicantId + ")";
        let listOfAssetsDetailsIds = response.data.listOfAssetsStr.split(",");
        getAssetsDetailsListByIds(listOfAssetsDetailsIds).then(response => {
          this.form.listOfAssets= response.data;
          this.assetsLength = response.data.length;
            // getUnits().then(response=>{
            //   this.units = response.data;
            //   this.loadListOfAssets = false;
          this.open = true;
        });
        this.title = "修改固资申请头";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAssets(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAssets(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除固资申请头编号为"' + ids + '"的数据项？').then(function() {
        return delAssets(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('workflow/assets/export', {
        ...this.queryParams
      }, `assets_${new Date().getTime()}.xlsx`)
    },
    openListOfAssetsDialog() {
      this.listOfAssetsDialogVisible = true;
    },
    // 父子组件通信
    handleListOfAssets(listOfAssets){
      this.form.listOfAssets = listOfAssets
      console.log(listOfAssets)
      this.assetsLength = listOfAssets.length;
    },
    handleVisibilityChange(newVisibility) {
      this.listOfAssetsDialogVisible = newVisibility;
    },

    /** 上传文件到服务器 */
    handleFileChange(file,fileList){
      this.upload.fileList = fileList
    },
    removeFile(file,fileList){
      this.upload.fileList.pop(file)
    },
    submitUpload(){
      if(this.upload.fileList.length === 0){
        this.$modal.msgError("请先附件");
      }else{
        const formData = new FormData()
        this.upload.fileList.forEach((file) => {
          formData.append('files',file.raw)
        })
        uploadAssetsFiles(formData).then((response)=>{
          this.upload.open = false;
          this.upload.isUploading = false;
          this.$refs.upload.clearFiles();
          this.upload.fileList = []
          this.$alert(response.msg, "导入结果", {dangerouslyUseHTMLString: true});
          this.form.attachments = response.data.join(",")
        })
      }
    },

    viewListOfAssets(data){
      let listOfAssetsDetailsIds = data.listOfAssetsStr.split(",");
      getAssetsDetailsListByIds(listOfAssetsDetailsIds).then(response => {
        this.listOfAssetsDialogViewVisible = true;
        this.listOfAssetsDetailsView = response.data
        getUnits().then(response=>{
          this.units = response.data;
          this.loadListOfAssets = false;
        });

      })
    },
    rowClassName({ row, rowIndex }) {
      row.xh = rowIndex + 1;
    },
    viewAttachments(data){
      let attachments = data.attachments;
      if (attachments == undefined || attachments.length == 0){
        this.$modal.msgWarning("您未上传附件");
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
    viewApprove(data){
      console.log(data)
      this.viewApproveVisible = true;
    },
    schedule(data){
        getDefinitionsByBusinesskey(data.serialNumber).then(response => {
          let {deploymentID, resourceName} = response.data
          this.modelerUrl = '/bpmnjs/index.html?type=lookBpmn&instanceId=' + data.instanceId + '&deploymentFileUUID=' + deploymentID + '&deploymentName=' + encodeURI(resourceName);
          this.modelVisible = true
        })
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
.button-group {
  display: flex;
  margin-bottom: 5px; /* 调整按钮组之间的垂直间距 */
}

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
