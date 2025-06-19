<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="日排行" prop="dayRank">
        <el-input
          v-model="queryParams.dayRank"
          placeholder="请输入日排行"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="周排行" prop="weekRank">
        <el-input
          v-model="queryParams.weekRank"
          placeholder="请输入周排行"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="月排行" prop="monthRank">
        <el-input
          v-model="queryParams.monthRank"
          placeholder="请输入月排行"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="年排行" prop="yearRank">
        <el-input
          v-model="queryParams.yearRank"
          placeholder="请输入年排行"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总排行" prop="totalRank">
        <el-input
          v-model="queryParams.totalRank"
          placeholder="请输入总排行"
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
          v-hasPermi="['forum:ranking:add']"
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
          v-hasPermi="['forum:ranking:edit']"
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
          v-hasPermi="['forum:ranking:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['forum:ranking:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rankingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="帖子ID" align="center" prop="postId" />
      <el-table-column label="日排行" align="center" prop="dayRank" />
      <el-table-column label="周排行" align="center" prop="weekRank" />
      <el-table-column label="月排行" align="center" prop="monthRank" />
      <el-table-column label="年排行" align="center" prop="yearRank" />
      <el-table-column label="总排行" align="center" prop="totalRank" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['forum:ranking:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['forum:ranking:remove']"
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

    <!-- 添加或修改帖子排名对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="日排行" prop="dayRank">
          <el-input v-model="form.dayRank" placeholder="请输入日排行" />
        </el-form-item>
        <el-form-item label="周排行" prop="weekRank">
          <el-input v-model="form.weekRank" placeholder="请输入周排行" />
        </el-form-item>
        <el-form-item label="月排行" prop="monthRank">
          <el-input v-model="form.monthRank" placeholder="请输入月排行" />
        </el-form-item>
        <el-form-item label="年排行" prop="yearRank">
          <el-input v-model="form.yearRank" placeholder="请输入年排行" />
        </el-form-item>
        <el-form-item label="总排行" prop="totalRank">
          <el-input v-model="form.totalRank" placeholder="请输入总排行" />
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
import { listRanking, getRanking, delRanking, addRanking, updateRanking } from "@/api/forum/ranking"

export default {
  name: "Ranking",
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
      // 帖子排名表格数据
      rankingList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dayRank: null,
        weekRank: null,
        monthRank: null,
        yearRank: null,
        totalRank: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询帖子排名列表 */
    getList() {
      this.loading = true
      listRanking(this.queryParams).then(response => {
        this.rankingList = response.rows
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
        postId: null,
        dayRank: null,
        weekRank: null,
        monthRank: null,
        yearRank: null,
        totalRank: null,
        updateTime: null
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
      this.ids = selection.map(item => item.postId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加帖子排名"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const postId = row.postId || this.ids
      getRanking(postId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改帖子排名"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.postId != null) {
            updateRanking(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRanking(this.form).then(response => {
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
      const postIds = row.postId || this.ids
      this.$modal.confirm('是否确认删除帖子排名编号为"' + postIds + '"的数据项？').then(function() {
        return delRanking(postIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('forum/ranking/export', {
        ...this.queryParams
      }, `ranking_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
