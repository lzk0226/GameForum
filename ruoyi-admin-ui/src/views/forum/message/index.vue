<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="接收者昵称" prop="receiverNickName">
        <el-input
          v-model="queryParams.receiverNickName"
          placeholder="请输入接收者昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发送者昵称" prop="senderNickName">
        <el-input
          v-model="queryParams.senderNickName"
          placeholder="请输入发送者昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息类型" prop="messageType">
        <el-select v-model="queryParams.messageType" placeholder="请选择消息类型" clearable>
          <el-option label="点赞" value="0" />
          <el-option label="评论" value="1" />
          <el-option label="关注" value="2" />
          <el-option label="收藏" value="3" />
          <el-option label="系统消息" value="4" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否已读" prop="isRead">
        <el-select v-model="queryParams.isRead" placeholder="请选择是否已读" clearable>
          <el-option label="未读" value="0" />
          <el-option label="已读" value="1" />
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['forum:message:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['forum:message:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="接收者" align="center" prop="receiverNickName" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.receiverNickName || '未知用户' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发送者" align="center" prop="senderNickName" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.senderNickName || '系统' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="消息类型" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="messageTypeTag(scope.row.messageType)">
            {{ messageTypeLabel(scope.row.messageType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="消息内容" align="center" prop="messageContent" min-width="200" show-overflow-tooltip />
      <el-table-column label="关联类型" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.relatedType" :type="scope.row.relatedType === '0' ? 'primary' : 'success'" size="small">
            {{ relatedTypeLabel(scope.row.relatedType) }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="关联内容" align="center" min-width="150" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.relatedType === '0'">
            {{ truncateText(scope.row.postTitle, 10) }}
          </span>
          <span v-else-if="scope.row.relatedType === '1'" v-html="truncateText(stripHtml(scope.row.commentContent), 10)">
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="已读状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isRead === '1' ? 'success' : 'warning'">
            {{ scope.row.isRead === '1' ? '已读' : '未读' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['forum:message:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['forum:message:remove']"
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

    <!-- 添加或修改用户消息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="接收者用户ID" prop="receiverId">
          <el-input v-model="form.receiverId" placeholder="请输入接收者用户ID" />
        </el-form-item>
        <el-form-item label="发送者用户ID" prop="senderId">
          <el-input v-model="form.senderId" placeholder="请输入发送者用户ID（系统消息可为空）" />
        </el-form-item>
        <el-form-item label="消息类型" prop="messageType">
          <el-select v-model="form.messageType" placeholder="请选择消息类型">
            <el-option label="点赞" value="0" />
            <el-option label="评论" value="1" />
            <el-option label="关注" value="2" />
            <el-option label="收藏" value="3" />
            <el-option label="系统消息" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="消息内容" prop="messageContent">
          <el-input v-model="form.messageContent" type="textarea" placeholder="请输入消息内容" />
        </el-form-item>
        <el-form-item label="关联类型" prop="relatedType">
          <el-select v-model="form.relatedType" placeholder="请选择关联类型" clearable>
            <el-option label="帖子" value="0" />
            <el-option label="评论" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联ID" prop="relatedId">
          <el-input v-model="form.relatedId" placeholder="请输入关联ID" />
        </el-form-item>
        <el-form-item label="是否已读" prop="isRead">
          <el-select v-model="form.isRead" placeholder="请选择是否已读">
            <el-option label="未读" value="0" />
            <el-option label="已读" value="1" />
          </el-select>
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
import { listMessage, getMessage, delMessage, addMessage, updateMessage } from "@/api/forum/message"

export default {
  name: "Message",
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
      // 用户消息表格数据
      messageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        receiverNickName: null,
        senderNickName: null,
        messageType: null,
        messageContent: null,
        relatedType: null,
        relatedId: null,
        isRead: null,
        readTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        receiverId: [
          { required: true, message: "接收者用户ID不能为空", trigger: "blur" }
        ],
        messageType: [
          { required: true, message: "消息类型不能为空", trigger: "change" }
        ],
        messageContent: [
          { required: true, message: "消息内容不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询用户消息列表 */
    getList() {
      this.loading = true
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 消息类型标签
    messageTypeLabel(type) {
      const typeMap = {
        '0': '点赞',
        '1': '评论',
        '2': '关注',
        '3': '收藏',
        '4': '系统消息'
      }
      return typeMap[type] || '未知'
    },
    // 消息类型标签颜色
    messageTypeTag(type) {
      const tagMap = {
        '0': 'danger',
        '1': 'primary',
        '2': 'success',
        '3': 'warning',
        '4': 'info'
      }
      return tagMap[type] || ''
    },
    // 关联类型标签
    relatedTypeLabel(type) {
      return type === '0' ? '帖子' : type === '1' ? '评论' : '未知'
    },
    // 截断文本
    truncateText(text, maxLength) {
      if (!text) return '-'
      if (text.length <= maxLength) return text
      return text.substring(0, maxLength) + '...'
    },
    // 去除HTML标签
    stripHtml(html) {
      if (!html) return ''
      const div = document.createElement('div')
      div.innerHTML = html
      return div.textContent || div.innerText || ''
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        messageId: null,
        receiverId: null,
        senderId: null,
        messageType: null,
        messageContent: null,
        relatedType: null,
        relatedId: null,
        isRead: '0',
        delFlag: null,
        createTime: null,
        readTime: null
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
      this.ids = selection.map(item => item.messageId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加用户消息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const messageId = row.messageId || this.ids
      getMessage(messageId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改用户消息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.messageId != null) {
            updateMessage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMessage(this.form).then(response => {
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
      const messageIds = row.messageId || this.ids
      this.$modal.confirm('是否确认删除选中的用户消息？').then(function() {
        return delMessage(messageIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('forum/message/export', {
        ...this.queryParams
      }, `message_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
/* 确保表格内容对齐 */
.el-table {
  font-size: 14px;
}
</style>
