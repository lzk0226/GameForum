<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="帖子ID" prop="postId">
        <el-input
          v-model="queryParams.postId"
          placeholder="请输入帖子ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="对应游戏" prop="gameId">
        <el-select v-model="queryParams.gameId" placeholder="请选择对应游戏" clearable>
          <el-option
            v-for="(name, id) in gameMap"
            :key="id"
            :label="name"
            :value="parseInt(id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="评论用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入评论用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="父评论ID" prop="parentId">
        <el-input
          v-model="queryParams.parentId"
          placeholder="请输入父评论ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="点赞数" prop="likeCount">
        <el-input
          v-model="queryParams.likeCount"
          placeholder="请输入点赞数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="可见" :value="0" />
          <el-option label="不可见" :value="1" />
          <el-option label="已删除" :value="2" />
        </el-select>
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
          v-hasPermi="['forum:comment:add']"
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
          v-hasPermi="['forum:comment:edit']"
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
          v-hasPermi="['forum:comment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['forum:comment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="commentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="评论ID" align="center" prop="commentId" />
      <el-table-column label="评论内容" align="center" prop="commentContent" />
      <el-table-column label="帖子ID" align="center" prop="postId" />
      <el-table-column label="对应游戏" align="center">
        <template slot-scope="scope">
          {{ gameMap[String(scope.row.gameId)] || '未知游戏' }}
        </template>
      </el-table-column>
      <el-table-column label="评论用户ID" align="center" prop="userId" />
      <el-table-column label="父评论ID" align="center" prop="parentId" />
      <el-table-column label="点赞数" align="center" prop="likeCount" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['forum:comment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['forum:comment:remove']"
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

    <!-- 添加或修改评论对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="评论内容">
          <editor v-model="form.commentContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="帖子ID" prop="postId">
          <el-input v-model="form.postId" placeholder="请输入帖子ID" />
        </el-form-item>
        <el-form-item label="对应游戏" prop="gameId">
          <el-select v-model="form.gameId" placeholder="请选择对应游戏">
            <el-option
              v-for="(name, id) in gameMap"
              :key="id"
              :label="name"
              :value="parseInt(id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="评论用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入评论用户ID" />
        </el-form-item>
        <el-form-item label="父评论ID" prop="parentId">
          <el-input v-model="form.parentId" placeholder="请输入父评论ID" />
        </el-form-item>
        <el-form-item label="点赞数" prop="likeCount">
          <el-input v-model="form.likeCount" placeholder="请输入点赞数" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="可见" :value="0" />
            <el-option label="不可见" :value="1" />
            <el-option label="已删除" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listComment, getComment, delComment, addComment, updateComment } from "@/api/forum/comment"
import { listGame } from "@/api/forum/game"

export default {
  name: "Comment",
  data() {
    return {
      gameMap: {},
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
      // 评论表格数据
      commentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        commentContent: null,
        postId: null,
        gameId: null,
        userId: null,
        parentId: null,
        likeCount: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        commentContent: [
          { required: true, message: "评论内容不能为空", trigger: "blur" }
        ],
        postId: [
          { required: true, message: "帖子ID不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "评论用户ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getGameMap()  // 获取游戏名称
    this.getList()
  },
  methods: {
    // 获取状态文字
    getStatusText(status) {
      const statusMap = {
        0: '可见',
        1: '不可见',
        2: '已删除'
      }
      return statusMap[status] || '未知'
    },
    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        0: 'success',  // 可见 - 绿色
        1: 'warning',  // 不可见 - 橙色
        2: 'danger'    // 已删除 - 红色
      }
      return typeMap[status] || 'info'
    },
    // 获取游戏名称映射
    getGameMap() {
      listGame().then(response => {
        const games = response.rows || []
        this.gameMap = games.reduce((map, item) => {
          map[String(item.gameId)] = item.gameName
          return map
        }, {})
        this.getList()
      })
    },
    /** 查询评论列表 */
    getList() {
      this.loading = true
      listComment(this.queryParams).then(response => {
        this.commentList = response.rows
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
        commentId: null,
        commentContent: null,
        postId: null,
        gameId: null,
        userId: null,
        parentId: null,
        likeCount: null,
        status: 0,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.ids = selection.map(item => item.commentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加评论"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const commentId = row.commentId || this.ids
      getComment(commentId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改评论"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.commentId != null) {
            updateComment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addComment(this.form).then(response => {
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
      const commentIds = row.commentId || this.ids
      this.$modal.confirm('是否确认删除评论编号为"' + commentIds + '"的数据项？').then(function() {
        return delComment(commentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('forum/comment/export', {
        ...this.queryParams
      }, `comment_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
