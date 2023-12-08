<template>
  <div class="app-container">
    <el-form :model="queryCountryAreaParams" ref="countryAreaForm" size="small" :inline="true" v-show="showCountryAreaSearch" label-width="68px">
      <el-form-item label="区域英文名" prop="countryNameEN" label-width="85px">
        <el-input
          v-model="queryCountryAreaParams.countryNameEN"
          placeholder="请输入区域英文名"
          clearable
          @keyup.enter.native="handleQueryCountryArea"
        />
      </el-form-item>
      <el-form-item label="区域中文名" prop="countryNameCH" label-width="85px">
        <el-input
          v-model="queryCountryAreaParams.countryNameCH"
          placeholder="请输入区域中文名"
          clearable
          @keyup.enter.native="handleQueryCountryArea"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQueryCountryArea">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQueryCountryArea">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAllocateCustomer"
          v-hasPermi="['integration:international:addArrivalArea']"
        >分配客户</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAddCountryArea"
          v-hasPermi="['integration:international:addArrivalArea']"
        >增加运抵区域</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdateCountryArea"
          v-hasPermi="['integration:international:editArrivalArea']"
        >修改区域分组id</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showCountryAreaSearch" @queryTable="getListCountryArea"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="countryAreaList" @selection-change="selectionCountryAreaChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="区域英文名" align="center" prop="countryNameEN" />
      <el-table-column label="区域中文名" align="center" prop="countryNameCH" />
      <el-table-column label="区域分组id" align="center" prop="countryGroupId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdateCountryArea(scope.row)"
            v-hasPermi="['integration:international:editArrivalArea']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDeleteCountryArea(scope.row)"
            v-hasPermi="['integration:international:removeArrivalArea']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryCountryAreaParams.pageNum"
      :limit.sync="queryCountryAreaParams.pageSize"
      @pagination="getListCountryArea"
    />

    <!-- 修改运抵区域信息对话框 -->
    <el-dialog :title="countryAreaUpdateLayerTitle" :visible.sync="openCountryAreaUpdateLayer" width="500px" append-to-body>
      <el-form ref="countryAreaForm" :model="countryAreaForm" :rules="rules" label-width="80px">
        <el-form-item label="区域分组id" prop="countryGroupId" label-width="120px">
          <el-select v-model="countryAreaForm.countryGroupId" placeholder="请选择区域分组id">
            <el-option label="1" value="1"></el-option>
            <el-option label="2" value="2"></el-option>
            <el-option label="3" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="countryAreaForm.type" v-show="false"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCountryAreaForm">确 定</el-button>
        <el-button @click="cancelCountryArea">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 分配客户对话框 -->
    <el-dialog :title="allocateCountryAreaTitle" :visible.sync="openAllocateCountryAreaLayer" width="500px" append-to-body>
      <el-form ref="allocateCountryAreaForm" :model="allocateCountryAreaData" :rules="rules" size="medium" label-width="100px">
          <el-form-item label="客户代码" prop="fNumberId">
            <el-select v-model="allocateCountryAreaData.fNumberId" placeholder="请选择客户代码" filterable clearable
                       :style="{width: '100%'}" @change="getAllocateCountryArea($event)">
              <el-option v-for="(item, index) in fNumberIdOptions" :key="index" :label="`${item.customerCode}(${item.customerName})`"
                         :value="item.customerCode" ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="运抵区域" prop="country">
            <el-cascader v-model="allocateCountryAreaData.country" :options="countryOptions" :props="countryProps"
                         :style="{width: '100%'}" placeholder="请选择运抵区域" filterable clearable :disabled="!allocateCountryAreaData.fNumberId"
                         @change="isAllCheced($event)"
            ></el-cascader>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAllocateCountryAreaForm">提交</el-button>
        <el-button @click="resetAllocateCountryAreaForm">重置</el-button>
      </div>
    </el-dialog>
    <!-- 增加运抵区域信息对话框 -->
    <el-dialog :title="countryAreaAddLayerTitle" :visible.sync="openCountryAreaAddLayer" width="500px" append-to-body>
      <el-form ref="countryAreaForm" :model="countryAreaForm" :rules="rules" label-width="80px">
        <el-form-item label="运抵区域中文名称" prop="countryNameCH" label-width="150px">
          <el-input v-model="countryAreaForm.countryNameCH"></el-input>
        </el-form-item>
        <el-form-item label="运抵区域英文名称" prop="countryNameEN" label-width="150px">
          <el-input v-model="countryAreaForm.countryNameEN"></el-input>
        </el-form-item>
        <el-form-item label="区域分组id" prop="countryGroupId" label-width="150px">
          <el-select v-model="countryAreaForm.countryGroupId" placeholder="请选择区域分组id">
            <el-option label="1" value="1"></el-option>
            <el-option label="2" value="2"></el-option>
            <el-option label="3" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="countryAreaForm.type" v-show="false"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCountryAreaForm">确 定</el-button>
        <el-button @click="cancelCountryArea">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getFNumberIdOptions, getCountryOptions, addArrivalAreaData,
  listCountryArea, getCountryArea, updateCountryAreaGroupId,
  addCountryArea, deleteCountryArea} from "@/api/integration/international/arrivalArea";
import {delFile} from "@/api/integration/document/file";
export default {
  components: {},
  props: [],
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
      // 总条数
      total: 0,
      // 弹出区域分组修改层标题
      countryAreaUpdateLayerTitle: "",
      // 是否显示区域分组修改层
      openCountryAreaUpdateLayer: false,
      // 弹出增加区域表单层标题
      countryAreaAddLayerTitle: "",
      // 是否显示增加区域表单层
      openCountryAreaAddLayer: false,
      // 运抵区域信息表格数据
      countryAreaList: [],
      // 运抵区域表单参数
      countryAreaForm: {},
      // 查询运抵区域信息参数
      queryCountryAreaParams: {
        pageNum: 1,
        pageSize: 10,
        countryNameEN: null,
        countryNameCH: null,
        countryGroupId: null
      },
      // 显示运抵区域信息搜索条件
      showCountryAreaSearch: true,

      // 分配客户到相关区域参数
      allocateCountryAreaData: {
        fNumberId: [],
        country: [],
        isModified: false,
        allSelected: false
      },
      rules: {
        countryNameEN: [
          { required: true, message: "区域英文名不能为空", trigger: "blur" }
        ],
        countryNameCH: [
          { required: true, message: "区域中文名不能为空", trigger: "blur" }
        ],
        countryGroupId: [
          { required: true, message: "区域分组id不能为空", trigger: "change" }
        ],
        fNumberId: [{
          required: true,
          message: '请选择客户代码',
          trigger: 'change'
        }],
        country: [{
          required: true,
          type: 'array',
          message: '请至少选择一个区域',
          trigger: 'change'
        }],
      },
      fNumberIdOptions: [],
      countryOptions: [],
      countryOptionsLength: 0,
      countryProps: {
        "multiple": true
      },
      /** 打开分配客户弹框按钮 */
      openAllocateCountryAreaLayer: false,
      /** 分配客户弹框标题 */
      allocateCountryAreaTitle: "",
    }
  },
  computed: {

  },
  watch: {},
  created() {
    this.fNumberIdNotSelected = true;
    this.getFNumberId();
    this.getListCountryArea();
  },
  mounted() {},
  methods: {
    /** 多选框选中区域信息按钮 */
    selectionCountryAreaChange(selection) {
      this.ids = selection.map(item => item.countryNameEN)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 取消区域信息按钮 */
    cancelCountryArea() {
      this.openCountryAreaUpdateLayer = false;
      this.openCountryAreaAddLayer = false;
      this.resetCountryAreaForm();
    },
    /** 修改区域信息按钮操作 */
    handleUpdateCountryArea(row) {
      this.resetForm("countryAreaForm");
      const countryNameEN = row.countryNameEN || this.ids
      getCountryArea(countryNameEN).then(response => {
        this.countryAreaForm = response.data;
        this.openCountryAreaUpdateLayer = true;
        this.countryAreaUpdateLayerTitle = "修改运抵区域分组id";
        this.countryAreaForm.type = 'modify';
      });
    },
    /** 增加区域信息按钮操作 */
    handleAddCountryArea(){
      this.resetForm("countryAreaForm");
      this.countryAreaForm.type = 'add';
      this.openCountryAreaAddLayer = true;
      this.countryAreaAddLayerTitle = "新增区域";
    },
    /** 删除区域信息按钮操作 */
    handleDeleteCountryArea(row) {
      // const countryNameEN = row.countryNameEN || this.countryNameEN;
      const names = row.countryNameEN || this.ids;
      this.$modal.confirm('是否确认删除 "' + names + '" 区域').then(function() {
        return deleteCountryArea(names);
      }).then(() => {
        this.getListCountryArea();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 搜索区域信息按钮操作 */
    handleQueryCountryArea() {
      this.queryCountryAreaParams.pageNum = 1;
      this.getListCountryArea();
    },
    /** 重置区域信息按钮操作 */
    resetQueryCountryArea() {
      this.resetCountryAreaForm();
      this.handleQueryCountryArea();
    },
    /** 搜索区域信息具体方法 */
    getListCountryArea() {
      this.loading = true;
      listCountryArea(this.queryCountryAreaParams).then(response => {
        this.countryAreaList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    submitCountryAreaForm(){
      this.$refs["countryAreaForm"].validate(valid => {
        if (valid) {
            if(this.countryAreaForm.type == 'modify'){
              updateCountryAreaGroupId(this.countryAreaForm).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.openCountryAreaUpdateLayer = false;
                this.getListCountryArea();
              });
          }else if(this.countryAreaForm.type == 'add'){
              addCountryArea(this.countryAreaForm).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.openCountryAreaAddLayer = false;
                this.getListCountryArea();
              });
            }
        }

      });
    },
    resetCountryAreaForm(){
      this.countryAreaForm = {
        countryNameEN: null,
        countryNameCH: null,
        countryGroupId: null
      };
      this.resetForm("countryAreaForm");
    },

    /**  分配客户按钮  */
    handleAllocateCustomer() {
      this.resetForm("allocateCountryAreaForm");
      this.openAllocateCountryAreaLayer = true;
      this.allocateCountryAreaTitle = "分配客户";
    },
    submitAllocateCountryAreaForm() {
      this.$refs['allocateCountryAreaForm'].validate(valid => {
        if (!valid) return
        // TODO 提交表单
        let data = this.handleArrivalAreaData(this.allocateCountryAreaData)
        addArrivalAreaData(data).then(res => {
          this.resetAllocateCountryAreaForm();
          this.openAllocateCountryAreaLayer = false;
          this.$modal.msgSuccess("提交成功");
        }).catch(res => {
          console.log(res);
          this.$modal.msgError("提交失败");
        })
      })
    },
    resetAllocateCountryAreaForm(){
      this.$refs['allocateCountryAreaForm'].resetFields()
    },

    getFNumberId(){
      getFNumberIdOptions().then(res => {
        // console.log(res)
        this.fNumberIdOptions = res.customer;
      }).catch(e => {
        this.$modal.msgError("获取客户代码失败，请联系管理员");
      })
    },
    getAllocateCountryArea(customerCode) {
      // 清空运抵区域的选项
      this.allocateCountryAreaData.country = [];
      this.allocateCountryAreaData.isModified = false;
      this.countryOptionsLength = 0;
      if (customerCode != "") {
        getCountryOptions(customerCode).then(res => {
          let data = []
          const area = res.countryArea;
          let index = 0;
          for (const i in area) {
            // 插入选择头
            data.push({
              "id": i,
              "value": i,
              "label": "区域" + i,
              "children": []
            })
            for (const j in area[i]) {
              // 插入子选项
              data[index].children.push({
                "id": area[i][j].pk,
                "value": area[i][j].pk,
                "label": area[i][j].countryNameCH + "(" + area[i][j].countryNameEN + ")"
              })
              // 判断是否已经选中
              if (area[i][j].isSelected){
                // 如果是修改已经有分配区域的客户，需要额外处理，这里引入一个标志位
                this.allocateCountryAreaData.isModified = true;
                this.allocateCountryAreaData.country.push([i, area[i][j].pk]);
              }
            }
            this.countryOptionsLength += area[i].length;
            index++;
          }
          this.countryOptions = data;
          // console.log(this.countryOptions)


        }).catch(e => {
          this.$modal.msgError("获取国家区域失败，请联系管理员");
        })
      }
    },
    // 将list分装成map
    handleArrivalAreaData(data){
      let res = {
        customerCode: "",
        countryGroup: "",
        isModified: data.isModified,
        allSelected: data.allSelected
      };
      res.customerCode = data.fNumberId
      if (data.allSelected){
        res.countryGroup = 'ALL';
      }else {
        // let countrys = groupBy(data.country, item => item[0], item => item[1])
        for (let i = 0; i < data.country.length - 1; i++) {
          res.countryGroup += data.country[i][1] + "/";
        }
        res.countryGroup += data.country[data.country.length - 1][1];
      }
      return res;
    },
    isAllCheced(data){
      if(data != ""){
        if (data.length == this.countryOptionsLength){
          // 全选，可以修改一下data的值
          this.allocateCountryAreaData.allSelected = true;
        }else {
          this.allocateCountryAreaData.allSelected = false;
        }
      }
    }

  }
}

</script>
<style>
</style>
