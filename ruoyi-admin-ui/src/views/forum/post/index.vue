<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="帖子标题" prop="postTitle">
        <el-input
          v-model="queryParams.postTitle"
          placeholder="请输入帖子标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户昵称" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属版块" prop="sectionId">
        <el-select v-model="queryParams.sectionId" placeholder="请选择所属版块" clearable>
          <el-option
            v-for="(name, id) in sectionMap"
            :key="id"
            :label="name"
            :value="parseInt(id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="可浏览" value="0" />
          <el-option label="已隐藏" value="1" />
          <el-option label="已删除" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="置顶标志" prop="topFlag">
        <el-select v-model="queryParams.topFlag" placeholder="请选择" clearable>
          <el-option label="普通" value="0" />
          <el-option label="置顶" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="热门标志" prop="hotFlag">
        <el-select v-model="queryParams.hotFlag" placeholder="请选择" clearable>
          <el-option label="普通" value="0" />
          <el-option label="热门" value="1" />
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
          v-hasPermi="['forum:post:add']"
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
          v-hasPermi="['forum:post:edit']"
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
          v-hasPermi="['forum:post:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['forum:post:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="帖子ID" align="center" prop="postId" width="80" />
      <el-table-column label="帖子标题" align="center" prop="postTitle" min-width="200" show-overflow-tooltip />
      <el-table-column label="帖子内容" align="center" prop="postContent" min-width="250" show-overflow-tooltip>
        <template slot-scope="scope">
          <div v-html="scope.row.postContent" class="content-preview"></div>
        </template>
      </el-table-column>
      <el-table-column label="发帖用户" align="center" prop="nickName" width="120" />
      <el-table-column label="所属版块" align="center" width="150">
        <template slot-scope="scope">
          {{ sectionMap[String(scope.row.sectionId)] || '未知版块' }}
        </template>
      </el-table-column>
      <el-table-column label="点赞数" align="center" prop="likeCount" width="80" />
      <el-table-column label="评论数" align="center" prop="commentCount" width="80" />
      <el-table-column label="浏览数" align="center" prop="viewCount" width="80" />
      <el-table-column label="置顶标志" align="center" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.topFlag == '1' ? 'danger' : 'info'">
            {{ scope.row.topFlag == '1' ? '置顶' : '普通' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="热门标志" align="center" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.hotFlag == '1' ? 'warning' : 'info'">
            {{ scope.row.hotFlag == '1' ? '热门' : '普通' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="90">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)">
            {{ statusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['forum:post:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['forum:post:remove']"
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

    <!-- 添加或修改论坛帖子对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="帖子标题" prop="postTitle">
          <el-input v-model="form.postTitle" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="帖子内容" prop="postContent">
          <editor v-model="form.postContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="发帖用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入发帖用户ID" />
        </el-form-item>
        <el-form-item label="所属版块" prop="sectionId">
          <el-select v-model="form.sectionId" placeholder="请选择所属版块">
            <el-option
              v-for="(name, id) in sectionMap"
              :key="id"
              :label="name"
              :value="parseInt(id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="点赞数" prop="likeCount">
          <el-input v-model="form.likeCount" placeholder="请输入点赞数" />
        </el-form-item>
        <el-form-item label="评论数" prop="commentCount">
          <el-input v-model="form.commentCount" placeholder="请输入评论数" />
        </el-form-item>
        <el-form-item label="浏览数" prop="viewCount">
          <el-input v-model="form.viewCount" placeholder="请输入浏览数" />
        </el-form-item>
        <el-form-item label="置顶标志" prop="topFlag">
          <el-select v-model="form.topFlag" placeholder="请选择置顶标志">
            <el-option label="普通" value="0" />
            <el-option label="置顶" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="热门标志" prop="hotFlag">
          <el-select v-model="form.hotFlag" placeholder="请选择热门标志">
            <el-option label="普通" value="0" />
            <el-option label="热门" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="可浏览" value="0" />
            <el-option label="已隐藏" value="1" />
            <el-option label="已删除" value="2" />
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
import { listPost, getPost, delPost, addPost, updatePost } from "@/api/forum/post"
import { listSection } from "@/api/forum/section"

export default {
  name: "Post",
  data() {
    return {
      sectionMap: {},
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
      // 论坛帖子表格数据
      postList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        postTitle: null,
        postContent: null,
        userId: null,
        nickName: null,
        sectionId: null,
        likeCount: null,
        commentCount: null,
        viewCount: null,
        topFlag: null,
        hotFlag: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        postTitle: [
          { required: true, message: "帖子标题不能为空", trigger: "blur" }
        ],
        postContent: [
          { required: true, message: "帖子内容不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "发帖用户ID不能为空", trigger: "blur" }
        ],
        sectionId: [
          { required: true, message: "所属版块不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getSectionMap()
    this.getList()
  },
  methods: {
    // 获取版块映射
    getSectionMap() {
      listSection().then(response => {
        const sections = response.rows || []
        this.sectionMap = sections.reduce((map, item) => {
          map[String(item.sectionId)] = item.sectionName
          return map
        }, {})
      })
    },
    // 状态标签显示名称
    statusLabel(status) {
      if (status == '0') return '可浏览'
      if (status == '1') return '已隐藏'
      if (status == '2') return '已删除'
      return '未知状态'
    },
    // 状态标签显示类型
    statusType(status) {
      if (status === '0') return 'success'
      if (status === '1') return 'info'
      if (status === '2') return 'danger'
      return ''
    },
    /** 查询论坛帖子列表 */
    getList() {
      this.loading = true
      listPost(this.queryParams).then(response => {
        this.postList = response.rows
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
        postTitle: null,
        postContent: null,
        userId: null,
        sectionId: null,
        likeCount: 0,
        commentCount: 0,
        viewCount: 0,
        topFlag: '0',
        hotFlag: '0',
        status: '0',
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
      this.ids = selection.map(item => item.postId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加论坛帖子"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const postId = row.postId || this.ids
      getPost(postId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改论坛帖子"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.postId != null) {
            updatePost(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPost(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作（改为逻辑删除） */
    handleDelete(row) {
      const postIds = row.postId ? [row.postId] : this.ids
      this.$modal.confirm('是否确认将论坛帖子编号为 "' + postIds.join(', ') + '" 的帖子标记为已删除？')
        .then(() => {
          // 循环修改状态
          const requests = postIds.map(id => {
            const updateData = { postId: id, status: '2' } // 2 表示已删除
            return updatePost(updateData)
          })
          return Promise.all(requests)
        })
        .then(() => {
          this.getList()
          this.$modal.msgSuccess("帖子已标记为删除")
        })
        .catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('forum/post/export', {
        ...this.queryParams
      }, `post_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
/* 限制HTML内容的显示样式 */
.content-preview {
  max-height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
}

/* 移除内容预览中的图片 */
.content-preview >>> img {
  display: none;
}
</style>
