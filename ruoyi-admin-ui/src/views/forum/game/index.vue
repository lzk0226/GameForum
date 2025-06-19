<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="游戏名称" prop="gameName">
        <el-input
          v-model="queryParams.gameName"
          placeholder="请输入游戏名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="游戏类型ID" prop="gameTypeId">
        <el-input
          v-model="queryParams.gameTypeId"
          placeholder="请输入游戏类型ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="正常使用" :value="0" />
          <el-option label="废弃" :value="1" />
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
          v-hasPermi="['forum:game:add']"
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
          v-hasPermi="['forum:game:edit']"
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
          v-hasPermi="['forum:game:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['forum:game:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="gameList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="游戏ID" align="center" prop="gameId" />
      <el-table-column label="游戏名称" align="center" prop="gameName" />
      <el-table-column label="游戏简介" align="center" prop="gameDescription" />
      <el-table-column label="游戏类型" align="center">
        <template slot-scope="scope">
          {{ gameTypeMap[String(scope.row.gameTypeId)] || '未知类型' }}
        </template>
      </el-table-column>

      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == 0 ? 'success' : 'info'">
            {{ scope.row.status == 0 ? '正常使用' : '废弃' }}
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
            v-hasPermi="['forum:game:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['forum:game:remove']"
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

    <!-- 添加或修改游戏对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="游戏名称" prop="gameName">
          <el-input v-model="form.gameName" placeholder="请输入游戏名称" />
        </el-form-item>
        <el-form-item label="游戏简介" prop="gameDescription">
          <el-input v-model="form.gameDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="游戏类型" prop="gameTypeId">
          <el-select v-model="form.gameTypeId" placeholder="请选择游戏类型">
            <el-option
              v-for="(name, id) in gameTypeMap"
              :key="id"
              :label="name"
              :value="parseInt(id)"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="正常使用" :value="0" />
            <el-option label="废弃" :value="1" />
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
import { listGame, getGame, delGame, addGame, updateGame } from "@/api/forum/game"
import { listGameType } from "@/api/forum/gameType"

export default {
  name: "Game",
  data() {
    return {
      gameTypeMap: {},
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
      // 游戏表格数据
      gameList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        gameName: null,
        gameDescription: null,
        gameTypeId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        gameName: [
          { required: true, message: "游戏名称不能为空", trigger: "blur" }
        ],
        gameTypeId: [
          { required: true, message: "游戏类型ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getGameTypeMap()  // 获取游戏类型名称
    this.getList()
  },
  methods: {
    getGameTypeMap() {
      listGameType().then(response => {
        const types = response.rows || []
        this.gameTypeMap = types.reduce((map, item) => {
          map[String(item.typeId)] = item.typeName
          return map
        }, {})
        this.getList()
      })
    },
    /** 查询游戏列表 */
    getList() {
      this.loading = true
      listGame(this.queryParams).then(response => {
        this.gameList = response.rows
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
        gameId: null,
        gameName: null,
        gameDescription: null,
        gameTypeId: null,
        status: null,
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
      this.ids = selection.map(item => item.gameId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加游戏"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const gameId = row.gameId || this.ids
      getGame(gameId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改游戏"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.gameId != null) {
            updateGame(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addGame(this.form).then(response => {
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
      const gameIds = row.gameId || this.ids
      this.$modal.confirm('是否确认删除游戏编号为"' + gameIds + '"的数据项？').then(function() {
        return delGame(gameIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('forum/game/export', {
        ...this.queryParams
      }, `game_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
