<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="关注者昵称" prop="followerNickName">
        <el-input
          v-model="queryParams.followerNickName"
          placeholder="请输入关注者昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="被关注者昵称" prop="followingNickName">
        <el-input
          v-model="queryParams.followingNickName"
          placeholder="请输入被关注者昵称"
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['forum:follow:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['forum:follow:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="followList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="关注者昵称" align="center" prop="followerNickName" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.followerNickName || '未知用户' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="被关注者昵称" align="center" prop="followingNickName" min-width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.followingNickName || '未知用户' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关注时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['forum:follow:remove']"
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
  </div>
</template>

<script>
import { listFollow, getFollow, delFollow, addFollow, updateFollow } from "@/api/forum/follow"

export default {
  name: "Follow",
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
      // 用户关注表格数据
      followList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        followerNickName: null,
        followingNickName: null
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询用户关注列表 */
    getList() {
      this.loading = true
      listFollow(this.queryParams).then(response => {
        this.followList = response.rows
        this.total = response.total
        this.loading = false
      })
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
      this.ids = selection.map(item => item.followerId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const followerIds = row.followerId || this.ids
      const nickNames = row.followerNickName
        ? `"${row.followerNickName}" 关注 "${row.followingNickName}"`
        : '选中'
      this.$modal.confirm(`是否确认删除${nickNames}的关注记录？`).then(function() {
        return delFollow(followerIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('forum/follow/export', {
        ...this.queryParams
      }, `follow_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
/* 确保表格列宽合理 */
.el-table {
  font-size: 14px;
}
</style>
