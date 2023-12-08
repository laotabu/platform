<!-- AssetDetailsDialog.vue -->
<template>
  <div>
    <el-dialog
      title="填写资产清单明细"
      :visible.sync="dialogVisible"
      width="100%"
      @close="dialogClosed"
    >
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddDetails">添加</el-button>

      <el-button
        type="success"
        icon="el-icon-delete"
        size="mini"
        @click="handleDeleteDetails"
      >删除</el-button>

      <el-button
        type="danger"
        icon="el-icon-delete"
        size="mini"
        @click="handleDeleteAllDetails"
      >清空</el-button>
<!--      <el-button-->
<!--        type="danger"-->
<!--        icon="el-icon-delete"-->
<!--        size="mini"-->
<!--        @click="sendListOfAssets"-->
<!--      >确认</el-button>-->

      <el-table
        v-loading="loading"
        :data="assetList"
        :row-class-name="rowClassName"
        @selection-change="handleSelectionChange"
        ref="tb"
      >
        <el-table-column type="selection" width="30" align="center" />
        <el-table-column label="序号" align="center" prop="xh" width="50"></el-table-column>

        <el-table-column label="资产名称" align="center" >
          <template slot-scope="scope">
              <el-input  v-model="assetList[scope.row.xh-1].assetName" placeholder="请输入资产名称" ></el-input>
          </template>
        </el-table-column>
        <el-table-column label="资产类别" align="center">
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].assetType" placeholder="请输入资产类别"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="品牌" align="center">
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].brand" placeholder="请输入品牌"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="规格型号" align="center" >
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].specificationAndModel" placeholder="请输入规格型号"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="单位" align="center" >
          <template slot-scope="scope">
            <el-select v-model="assetList[scope.row.xh-1].unit" filterable placeholder="请选择单位">
              <el-option
                v-for="item in units"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
<!--            <el-input  v-model="assetList[scope.row.xh-1].unit" placeholder="请输入单位"></el-input>-->
          </template>
        </el-table-column>
        <el-table-column label="数量" align="center" >
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].quantity" placeholder="请输入数量" type="number"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="预估金额" align="center" >
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].estimatedAmount" placeholder="请输入预估金额"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="资产编号" align="center" >
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].assetNumber" placeholder="请输入资产编号"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="原值" align="center" >
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].originalPrice" placeholder="请输入原值"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="报销单号" align="center" >
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].reimbursementNumber" placeholder="请输入报销单号" ></el-input>
          </template>
        </el-table-column>
        <el-table-column label="存放地点" align="center" >
          <template slot-scope="scope">
            <el-cascader v-model="assetList[scope.row.xh-1].placeOfStorage" :options="deptOptions"
                         clearable
                         :show-all-levels="false"
                         :props="{emitPath:false}" ></el-cascader>
          </template>
        </el-table-column>
        <el-table-column label="保管人名称" align="center" >
          <template slot-scope="scope">
            <el-input  v-model="assetList[scope.row.xh-1].preserverName" placeholder="请输入保管人名称" ></el-input>
          </template>
        </el-table-column>
      </el-table>

    </el-dialog>
  </div>
</template>

<script>
import {getUnits} from "@/api/integration/workflow/assetsDetails"
import {deptCascader} from "@/api/system/user"
export default {
  props: {
    visibility: {
      type: Boolean,
      default: false,
    },
    assetListP: {
      type: Array
    }
  },
  data() {
    return {
      dialogVisible: this.visibility,
      detail: '',
      //选中的从表数据
      checkedDetail: [],
      assetList:[],
      loading: false,
      brands: [{
        value: 'DELL',
        label: '戴尔'
      },{
        value: 'LENOVO',
        label: '联想'
      }],
      units: [],
      deptOptions: [],
    };
  },

  created() {
    deptCascader().then(response => {
      this.deptOptions = response.data;
    });
    getUnits().then(response=>{
      this.units = response.data;
    })
  },
  watch: {
    visibility(newVal) {
      this.dialogVisible = newVal;
    },
    dialogVisible(newVal) {
      this.$emit('visibility-change', newVal); // 通过 input 事件同步父组件的值
    },
    assetListP(newVal){
      this.assetList = newVal;
    }
  },
  methods: {
    dialogClosed() {
      this.detail = '';
      this.dialogVisible = false
      if (this.assetList != undefined && this.assetList.length != 0){
        let validRes = this.validateFun(this.assetList)
        if(validRes.status){
          // 部门只需要最低层部门就行
          this.$emit('array-event', this.assetList);
        } else{
          this.$modal.msgError(validRes.msg);
        }
      }else {
        this.$emit('array-event', new Array());
      }

    },
    saveDetail() {
      console.log('保存明细:', this.detail);
      this.dialogVisible = false;
    },
    rowClassName({ row, rowIndex }) {
      row.xh = rowIndex + 1;
    },
    handleAddDetails() {
      if (this.assetList === undefined) {
        this.assetList = new Array();
      }
      if (this.assetList !== undefined && this.assetList.length !== 0){
        let validRes = this.validateFun(this.assetList)
        if(validRes.status){
          this.addObj()
        } else{
          this.$modal.msgError(validRes.msg);
        }
      }else {
        this.addObj()
      }
    },

    addObj(){
      let obj = {};
      obj.assetName = "";
      obj.assetType = "";
      obj.brand = "";
      obj.specificationAndModel = "";
      obj.unit = "";
      obj.quantity = "";
      obj.estimatedAmount = "";
      obj.assetNumber = "";
      obj.originalPrice = "";
      obj.reimbursementNumber = "";
      obj.placeOfStorage = "";
      obj.preserverName = "";
      this.assetList.push(obj);
    },

    handleDeleteDetails() {
      if (this.checkedDetail.length == 0) {
        this.$alert("请先选择要删除的数据", "提示", {
          confirmButtonText: "确定",
        });
      } else {
        this.assetList = this.assetList.filter(e => !this.checkedDetail.includes(e.xh))
      }
    },
    handleDeleteAllDetails() {
      this.assetList = undefined;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.checkedDetail = selection.map(item => item.xh)
      // this.single = selection.length!==1
      // this.multiple = !selection.length
    },
    validateFun(list){
      let status = true
      let msg = ""
      // list.forEach(e=>{
      //   if(e.assetName === ""){
      //     status = false
      //     msg = "资产名字不能为空"
      //   }else if(e.assetType === ""){
      //     status = false
      //     msg = "资产类别不能为空"
      //   }else if(e.brand === ""){
      //     status = false
      //     msg = "品牌不能为空"
      //   }else if(e.specificationAndModel === ""){
      //     status = false
      //     msg = "规格型号不能为空"
      //   }else if(e.unit === ""){
      //     status = false
      //     msg = "单位不能为空"
      //   }else if(e.quantity === ""){
      //     status = false
      //     msg = "数量不能为空"
      //   }else if(e.estimatedAmount === ""){
      //     status = false
      //     msg = "预估金额不能为空"
      //   }else if(e.assetNumber === ""){
      //     status = false
      //     msg = "资产编号不能为空"
      //   }else if(e.originalPrice === ""){
      //     status = false
      //     msg = "原值不能为空"
      //   }else if(e.reimbursementNumber === ""){
      //     status = false
      //     msg = "报销单号不能为空"
      //   }else if(e.placeOfStorage === ""){
      //     status = false
      //     msg = "存放地点不能为空"
      //   }else if(e.preserverName === ""){
      //     status = false
      //     msg = "保管人不能为空"
      //   }
      // })
      return {"status":status,"msg":msg}
    }



  },
};
</script>

