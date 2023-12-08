<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文件名称" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资料来源" prop="docFrom">
        <el-input
          v-model="queryParams.docFrom"
          placeholder="请输入资料来源"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
          v-hasPermi="['integration:file:add']"
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
          v-hasPermi="['integration:file:edit']"
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
          v-hasPermi="['integration:file:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['integration:file:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fileList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="ID" align="center" prop="id" />-->
      <el-table-column label="文件名称" align="center" prop="fileName" />
      <el-table-column label="文件类型" align="center" prop="fileType" />
      <el-table-column label="文件状态" align="center" prop="status" />
      <el-table-column label="文件路径" align="center" prop="fileUrl" />
<!--      <el-table-column label="审核不通过原因" align="center" prop="failureReason" />-->
      <el-table-column label="下载权限" align="center" prop="downloadPower" />
      <el-table-column label="查看权限" align="center" prop="viewPower" />
      <el-table-column label="资料来源" align="center" prop="docFrom" />
      <el-table-column label="备注" align="center" prop="remarks" />
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="downloadFile(scope.row)"
          >下载</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['integration:file:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['integration:file:remove']"
          >删除</el-button>
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

    <!-- 添加或修改文件信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件名称" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入文件名称" />
        </el-form-item>
        <el-form-item label="上传文件" prop="fileUrl">
          <file-upload v-model="form.fileUrl"/>
        </el-form-item>
        <el-form-item label="文件类型" prop="fileType">
          <el-input v-model="form.fileType" placeholder="请输入文件类型" />
        </el-form-item>
        <el-form-item label="资料来源" prop="docFrom">
          <el-input v-model="form.docFrom" placeholder="请输入资料来源" />
        </el-form-item>
<!--        <el-form-item label="审核不通过原因" prop="failureReason">-->
<!--          <el-input v-model="form.failureReason" type="textarea" placeholder="请输入内容" />-->
<!--        </el-form-item>-->
        <el-form-item label="下载权限" prop="downloadPower">
          <el-select v-model="form.downloadPower" placeholder="请选择下载权限">
            <el-option label="是" value="是"></el-option>
            <el-option label="否" value="否"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="查看权限" prop="viewPower">
          <el-select v-model="form.viewPower" placeholder="请选择查看权限">
            <el-option label="是" value="是"></el-option>
            <el-option label="否" value="否"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="文件预览" :close-on-click-modal="false" :visible.sync="previewVisible" width="60%" top="4vh" @close="closePreview">
      <div style="max-height: 650px; overflow-x: hidden; overflow-y: scroll">
        <pdf-frame v-if="previewData.fileType==='1'" :src="previewData.viewUrl" />
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listFile, getFile, delFile, addFile, updateFile } from "@/api/integration/document/file";
import PdfFrame from '@/views/integration/file/components/PdfFrame'

export default {
  name: "File",
  components: { PdfFrame },
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
      // 文件信息表格数据
      fileList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fileName: null,
        fileType: null,
        docFrom: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fileName: [
          { required: true, message: "文件名称不能为空", trigger: "blur" }
        ],
        fileUrl: [
          { required: true, message: "文件不能为空", trigger: "change" }
        ],
        fileType: [
          { required: true, message: "文件类型不能为空", trigger: "change" }
        ],
        downloadPower: [
          { required: true, message: "下载权限不能为空", trigger: "change" }
        ],
        viewPower: [
          { required: true, message: "查看权限不能为空", trigger: "change" }
        ],
      },
      previewVisible: false,
      previewData: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询文件信息列表 */
    getList() {
      this.loading = true;
      listFile(this.queryParams).then(response => {
        this.fileList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        createBy: null,
        createTime: null,
        updateTime: null,
        fileName: null,
        fileType: null,
        status: null,
        fileUrl: null,
        failureReason: null,
        downloadPower: null,
        viewPower: null,
        remarks: null,
        docFrom: null
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
      this.title = "添加文件信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFile(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改文件信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFile(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFile(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除文件信息编号为"' + ids + '"的数据项？').then(function() {
        return delFile(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('integration/file/export', {
        ...this.queryParams
      }, `file_${new Date().getTime()}.xlsx`)
    },
    /** 在线预览文件 */
    handlePreview(data) {
      this.previewVisible = true
      this.previewData.fileType = '1'
      // this.previewData.viewUrl = data.fileUrl
      this.previewData.viewUrl = 'http://192.168.2.88:9000/test/What%20Can%20Human%20Sketches%20Do%20for%20Object%20Detection.pdf'
      // this.previewData.fileUrl = data.fileUrl
    },
    closePreview() {
      this.previewData = {}
    },
    downloadFile(data) {
      const fileURL = 'http://localhost/dev-api' + data.fileUrl; // 替换为你的文件路径
      const link = document.createElement('a');
      link.href = fileURL;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },
  }
};
</script>
