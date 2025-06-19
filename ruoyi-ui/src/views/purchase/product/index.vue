<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联游戏ID" prop="gameId">
        <el-input
          v-model="queryParams.gameId"
          placeholder="请输入关联游戏ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="原价" prop="originalPrice">
        <el-input
          v-model="queryParams.originalPrice"
          placeholder="请输入原价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="售价" prop="salePrice">
        <el-input
          v-model="queryParams.salePrice"
          placeholder="请输入售价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="折扣率" prop="discountRate">
        <el-input
          v-model="queryParams.discountRate"
          placeholder="请输入折扣率"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="库存数量" prop="stockCount">
        <el-input
          v-model="queryParams.stockCount"
          placeholder="请输入库存数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已售数量" prop="soldCount">
        <el-input
          v-model="queryParams.soldCount"
          placeholder="请输入已售数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平台" prop="platform">
        <el-input
          v-model="queryParams.platform"
          placeholder="请输入平台"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域" prop="region">
        <el-input
          v-model="queryParams.region"
          placeholder="请输入区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否自动发货" prop="autoDelivery">
        <el-input
          v-model="queryParams.autoDelivery"
          placeholder="请输入是否自动发货"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序权重" prop="sortOrder">
        <el-input
          v-model="queryParams.sortOrder"
          placeholder="请输入排序权重"
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
          v-hasPermi="['purchase:product:add']"
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
          v-hasPermi="['purchase:product:edit']"
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
          v-hasPermi="['purchase:product:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['purchase:product:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="CDK商品ID" align="center" prop="productId" />
      <el-table-column label="商品名称" align="center" prop="productName" />
      <el-table-column label="商品描述" align="center" prop="productDescription" />
      <el-table-column label="关联游戏ID" align="center" prop="gameId" />
      <el-table-column label="商品类型" align="center" prop="productType" />
      <el-table-column label="原价" align="center" prop="originalPrice" />
      <el-table-column label="售价" align="center" prop="salePrice" />
      <el-table-column label="折扣率" align="center" prop="discountRate" />
      <el-table-column label="库存数量" align="center" prop="stockCount" />
      <el-table-column label="已售数量" align="center" prop="soldCount" />
      <el-table-column label="平台" align="center" prop="platform" />
      <el-table-column label="区域" align="center" prop="region" />
      <el-table-column label="是否自动发货" align="center" prop="autoDelivery" />
      <el-table-column label="排序权重" align="center" prop="sortOrder" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['purchase:product:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['purchase:product:remove']"
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

    <!-- 添加或修改CDK商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品描述" prop="productDescription">
          <el-input v-model="form.productDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="关联游戏ID" prop="gameId">
          <el-input v-model="form.gameId" placeholder="请输入关联游戏ID" />
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input v-model="form.originalPrice" placeholder="请输入原价" />
        </el-form-item>
        <el-form-item label="售价" prop="salePrice">
          <el-input v-model="form.salePrice" placeholder="请输入售价" />
        </el-form-item>
        <el-form-item label="折扣率" prop="discountRate">
          <el-input v-model="form.discountRate" placeholder="请输入折扣率" />
        </el-form-item>
        <el-form-item label="库存数量" prop="stockCount">
          <el-input v-model="form.stockCount" placeholder="请输入库存数量" />
        </el-form-item>
        <el-form-item label="已售数量" prop="soldCount">
          <el-input v-model="form.soldCount" placeholder="请输入已售数量" />
        </el-form-item>
        <el-form-item label="平台" prop="platform">
          <el-input v-model="form.platform" placeholder="请输入平台" />
        </el-form-item>
        <el-form-item label="区域" prop="region">
          <el-input v-model="form.region" placeholder="请输入区域" />
        </el-form-item>
        <el-form-item label="是否自动发货" prop="autoDelivery">
          <el-input v-model="form.autoDelivery" placeholder="请输入是否自动发货" />
        </el-form-item>
        <el-form-item label="排序权重" prop="sortOrder">
          <el-input v-model="form.sortOrder" placeholder="请输入排序权重" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
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
import { listProduct, getProduct, delProduct, addProduct, updateProduct } from "@/api/purchase/product"

export default {
  name: "Product",
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
      // CDK商品表格数据
      productList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productName: null,
        productDescription: null,
        gameId: null,
        productType: null,
        originalPrice: null,
        salePrice: null,
        discountRate: null,
        stockCount: null,
        soldCount: null,
        platform: null,
        region: null,
        autoDelivery: null,
        sortOrder: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        productName: [
          { required: true, message: "商品名称不能为空", trigger: "blur" }
        ],
        gameId: [
          { required: true, message: "关联游戏ID不能为空", trigger: "blur" }
        ],
        productType: [
          { required: true, message: "商品类型不能为空", trigger: "change" }
        ],
        originalPrice: [
          { required: true, message: "原价不能为空", trigger: "blur" }
        ],
        salePrice: [
          { required: true, message: "售价不能为空", trigger: "blur" }
        ],
        stockCount: [
          { required: true, message: "库存数量不能为空", trigger: "blur" }
        ],
        platform: [
          { required: true, message: "平台不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询CDK商品列表 */
    getList() {
      this.loading = true
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows
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
        productId: null,
        productName: null,
        productDescription: null,
        gameId: null,
        productType: null,
        originalPrice: null,
        salePrice: null,
        discountRate: null,
        stockCount: null,
        soldCount: null,
        platform: null,
        region: null,
        autoDelivery: null,
        sortOrder: null,
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
      this.ids = selection.map(item => item.productId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加CDK商品"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const productId = row.productId || this.ids
      getProduct(productId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改CDK商品"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.productId != null) {
            updateProduct(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addProduct(this.form).then(response => {
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
      const productIds = row.productId || this.ids
      this.$modal.confirm('是否确认删除CDK商品编号为"' + productIds + '"的数据项？').then(function() {
        return delProduct(productIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('purchase/product/export', {
        ...this.queryParams
      }, `product_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
