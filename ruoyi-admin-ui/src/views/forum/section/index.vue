<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="版块名称" prop="sectionName">
        <el-input
          v-model="queryParams.sectionName"
          placeholder="请输入版块名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联游戏" prop="gameId">
        <el-select v-model="queryParams.gameId" placeholder="请选择关联游戏" clearable>
          <el-option
            v-for="(name, id) in gameMap"
            :key="id"
            :label="name"
            :value="parseInt(id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="显示顺序" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入显示顺序"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="正常" :value="0" />
          <el-option label="停用" :value="1" />
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
          v-hasPermi="['forum:section:add']"
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
          v-hasPermi="['forum:section:edit']"
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
          v-hasPermi="['forum:section:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['forum:section:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="版块ID" align="center" prop="sectionId" />
      <el-table-column label="版块名称" align="center" prop="sectionName" />
      <el-table-column label="版块描述" align="center" prop="sectionDescription" />
      <el-table-column label="关联游戏" align="center">
        <template slot-scope="scope">
          {{ gameMap[String(scope.row.gameId)] || '未知游戏' }}
        </template>
      </el-table-column>
      <el-table-column label="显示顺序" align="center" prop="orderNum" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == 0 ? 'success' : 'info'">
            {{ scope.row.status == 0 ? '正常' : '停用' }}
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
            v-hasPermi="['forum:section:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['forum:section:remove']"
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

    <!-- 添加或修改论坛版块对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="版块名称" prop="sectionName">
          <el-input v-model="form.sectionName" placeholder="请输入版块名称" />
        </el-form-item>
        <el-form-item label="版块描述" prop="sectionDescription">
          <el-input v-model="form.sectionDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="关联游戏" prop="gameId">
          <el-select v-model="form.gameId" placeholder="请选择关联游戏">
            <el-option
              v-for="(name, id) in gameMap"
              :key="id"
              :label="name"
              :value="parseInt(id)"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="正常" :value="0" />
            <el-option label="停用" :value="1" />
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
import { listSection, getSection, delSection, addSection, updateSection } from "@/api/forum/section"
import { listGame } from "@/api/forum/game"

export default {
  name: "Section",
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
      // 论坛版块表格数据
      sectionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sectionName: null,
        sectionDescription: null,
        gameId: null,
        orderNum: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sectionName: [
          { required: true, message: "版块名称不能为空", trigger: "blur" }
        ],
        gameId: [
          { required: true, message: "关联游戏不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getGameMap()
    this.getList()
  },
  methods: {
    getGameMap() {
      listGame().then(response => {
        const games = response.rows || []
        this.gameMap = games.reduce((map, item) => {
          map[String(item.gameId)] = item.gameName
          return map
        }, {})
      })
    },
    /** 查询论坛版块列表 */
    getList() {
      this.loading = true
      listSection(this.queryParams).then(response => {
        this.sectionList = response.rows
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
        sectionId: null,
        sectionName: null,
        sectionDescription: null,
        gameId: null,
        orderNum: null,
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
      this.ids = selection.map(item => item.sectionId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加论坛版块"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const sectionId = row.sectionId || this.ids
      getSection(sectionId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改论坛版块"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.sectionId != null) {
            updateSection(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSection(this.form).then(response => {
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
      const sectionIds = row.sectionId || this.ids
      this.$modal.confirm('是否确认删除论坛版块编号为"' + sectionIds + '"的数据项？').then(function() {
        return delSection(sectionIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('forum/section/export', {
        ...this.queryParams
      }, `section_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
