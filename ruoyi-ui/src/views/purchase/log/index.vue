<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动金额" prop="changeAmount">
        <el-input
          v-model="queryParams.changeAmount"
          placeholder="请输入变动金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动前余额" prop="beforeBalance">
        <el-input
          v-model="queryParams.beforeBalance"
          placeholder="请输入变动前余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动后余额" prop="afterBalance">
        <el-input
          v-model="queryParams.afterBalance"
          placeholder="请输入变动后余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联订单ID" prop="relatedOrderId">
        <el-input
          v-model="queryParams.relatedOrderId"
          placeholder="请输入关联订单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动说明" prop="description">
        <el-input
          v-model="queryParams.description"
          placeholder="请输入变动说明"
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
          v-hasPermi="['purchase:log:add']"
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
          v-hasPermi="['purchase:log:edit']"
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
          v-hasPermi="['purchase:log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['purchase:log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志ID" align="center" prop="logId" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="变动类型" align="center" prop="changeType" />
      <el-table-column label="变动金额" align="center" prop="changeAmount" />
      <el-table-column label="变动前余额" align="center" prop="beforeBalance" />
      <el-table-column label="变动后余额" align="center" prop="afterBalance" />
      <el-table-column label="关联订单ID" align="center" prop="relatedOrderId" />
      <el-table-column label="变动说明" align="center" prop="description" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['purchase:log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['purchase:log:remove']"
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

    <!-- 添加或修改余额变动日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="变动金额" prop="changeAmount">
          <el-input v-model="form.changeAmount" placeholder="请输入变动金额" />
        </el-form-item>
        <el-form-item label="变动前余额" prop="beforeBalance">
          <el-input v-model="form.beforeBalance" placeholder="请输入变动前余额" />
        </el-form-item>
        <el-form-item label="变动后余额" prop="afterBalance">
          <el-input v-model="form.afterBalance" placeholder="请输入变动后余额" />
        </el-form-item>
        <el-form-item label="关联订单ID" prop="relatedOrderId">
          <el-input v-model="form.relatedOrderId" placeholder="请输入关联订单ID" />
        </el-form-item>
        <el-form-item label="变动说明" prop="description">
          <el-input v-model="form.description" placeholder="请输入变动说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/purchase/log"

export default {
  name: "Log",
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
      // 余额变动日志表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        changeType: null,
        changeAmount: null,
        beforeBalance: null,
        afterBalance: null,
        relatedOrderId: null,
        description: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        changeType: [
          { required: true, message: "变动类型不能为空", trigger: "change" }
        ],
        changeAmount: [
          { required: true, message: "变动金额不能为空", trigger: "blur" }
        ],
        beforeBalance: [
          { required: true, message: "变动前余额不能为空", trigger: "blur" }
        ],
        afterBalance: [
          { required: true, message: "变动后余额不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询余额变动日志列表 */
    getList() {
      this.loading = true
      listLog(this.queryParams).then(response => {
        this.logList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        logId: null,
        userId: null,
        changeType: null,
        changeAmount: null,
        beforeBalance: null,
        afterBalance: null,
        relatedOrderId: null,
        description: null,
        createTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.logId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加余额变动日志"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const logId = row.logId || this.ids
      getLog(logId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改余额变动日志"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.logId != null) {
            updateLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLog(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const logIds = row.logId || this.ids
      this.$modal.confirm('是否确认删除余额变动日志编号为"' + logIds + '"的数据项？').then(function() {
        return delLog(logIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('purchase/log/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
