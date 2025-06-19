<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="购买用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入购买用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入商品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="购买数量" prop="quantity">
        <el-input
          v-model="queryParams.quantity"
          placeholder="请输入购买数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单价" prop="unitPrice">
        <el-input
          v-model="queryParams.unitPrice"
          placeholder="请输入单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总金额" prop="totalAmount">
        <el-input
          v-model="queryParams.totalAmount"
          placeholder="请输入总金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="优惠金额" prop="discountAmount">
        <el-input
          v-model="queryParams.discountAmount"
          placeholder="请输入优惠金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实付金额" prop="actualAmount">
        <el-input
          v-model="queryParams.actualAmount"
          placeholder="请输入实付金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付方式" prop="paymentMethod">
        <el-input
          v-model="queryParams.paymentMethod"
          placeholder="请输入支付方式"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付流水号" prop="paymentNo">
        <el-input
          v-model="queryParams.paymentNo"
          placeholder="请输入支付流水号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系邮箱" prop="contactEmail">
        <el-input
          v-model="queryParams.contactEmail"
          placeholder="请输入联系邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付时间" prop="payTime">
        <el-date-picker clearable
          v-model="queryParams.payTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择支付时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="发货时间" prop="deliveryTime">
        <el-date-picker clearable
          v-model="queryParams.deliveryTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择发货时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="完成时间" prop="completeTime">
        <el-date-picker clearable
          v-model="queryParams.completeTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择完成时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="订单过期时间" prop="expireTime">
        <el-date-picker clearable
          v-model="queryParams.expireTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择订单过期时间">
        </el-date-picker>
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
          v-hasPermi="['purchase:order:add']"
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
          v-hasPermi="['purchase:order:edit']"
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
          v-hasPermi="['purchase:order:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['purchase:order:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单ID" align="center" prop="orderId" />
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="购买用户ID" align="center" prop="userId" />
      <el-table-column label="商品ID" align="center" prop="productId" />
      <el-table-column label="商品名称" align="center" prop="productName" />
      <el-table-column label="购买数量" align="center" prop="quantity" />
      <el-table-column label="单价" align="center" prop="unitPrice" />
      <el-table-column label="总金额" align="center" prop="totalAmount" />
      <el-table-column label="优惠金额" align="center" prop="discountAmount" />
      <el-table-column label="实付金额" align="center" prop="actualAmount" />
      <el-table-column label="支付方式" align="center" prop="paymentMethod" />
      <el-table-column label="支付流水号" align="center" prop="paymentNo" />
      <el-table-column label="订单状态" align="center" prop="orderStatus" />
      <el-table-column label="发货状态" align="center" prop="deliveryStatus" />
      <el-table-column label="联系邮箱" align="center" prop="contactEmail" />
      <el-table-column label="买家留言" align="center" prop="buyerNote" />
      <el-table-column label="管理员备注" align="center" prop="adminNote" />
      <el-table-column label="支付时间" align="center" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="发货时间" align="center" prop="deliveryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.deliveryTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="completeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单过期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>
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
            v-hasPermi="['purchase:order:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['purchase:order:remove']"
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

    <!-- 添加或修改CDK订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="购买用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入购买用户ID" />
        </el-form-item>
        <el-form-item label="商品ID" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入商品ID" />
        </el-form-item>
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="购买数量" prop="quantity">
          <el-input v-model="form.quantity" placeholder="请输入购买数量" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input v-model="form.unitPrice" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="总金额" prop="totalAmount">
          <el-input v-model="form.totalAmount" placeholder="请输入总金额" />
        </el-form-item>
        <el-form-item label="优惠金额" prop="discountAmount">
          <el-input v-model="form.discountAmount" placeholder="请输入优惠金额" />
        </el-form-item>
        <el-form-item label="实付金额" prop="actualAmount">
          <el-input v-model="form.actualAmount" placeholder="请输入实付金额" />
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-input v-model="form.paymentMethod" placeholder="请输入支付方式" />
        </el-form-item>
        <el-form-item label="支付流水号" prop="paymentNo">
          <el-input v-model="form.paymentNo" placeholder="请输入支付流水号" />
        </el-form-item>
        <el-form-item label="联系邮箱" prop="contactEmail">
          <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" />
        </el-form-item>
        <el-form-item label="买家留言" prop="buyerNote">
          <el-input v-model="form.buyerNote" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="管理员备注" prop="adminNote">
          <el-input v-model="form.adminNote" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="支付时间" prop="payTime">
          <el-date-picker clearable
            v-model="form.payTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择支付时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="发货时间" prop="deliveryTime">
          <el-date-picker clearable
            v-model="form.deliveryTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择发货时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="完成时间" prop="completeTime">
          <el-date-picker clearable
            v-model="form.completeTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择完成时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="订单过期时间" prop="expireTime">
          <el-date-picker clearable
            v-model="form.expireTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择订单过期时间">
          </el-date-picker>
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
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from "@/api/purchase/order"

export default {
  name: "Order",
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
      // CDK订单表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        userId: null,
        productId: null,
        productName: null,
        quantity: null,
        unitPrice: null,
        totalAmount: null,
        discountAmount: null,
        actualAmount: null,
        paymentMethod: null,
        paymentNo: null,
        orderStatus: null,
        deliveryStatus: null,
        contactEmail: null,
        buyerNote: null,
        adminNote: null,
        payTime: null,
        deliveryTime: null,
        completeTime: null,
        expireTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "购买用户ID不能为空", trigger: "blur" }
        ],
        productId: [
          { required: true, message: "商品ID不能为空", trigger: "blur" }
        ],
        productName: [
          { required: true, message: "商品名称不能为空", trigger: "blur" }
        ],
        quantity: [
          { required: true, message: "购买数量不能为空", trigger: "blur" }
        ],
        unitPrice: [
          { required: true, message: "单价不能为空", trigger: "blur" }
        ],
        totalAmount: [
          { required: true, message: "总金额不能为空", trigger: "blur" }
        ],
        actualAmount: [
          { required: true, message: "实付金额不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询CDK订单列表 */
    getList() {
      this.loading = true
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows
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
        orderId: null,
        orderNo: null,
        userId: null,
        productId: null,
        productName: null,
        quantity: null,
        unitPrice: null,
        totalAmount: null,
        discountAmount: null,
        actualAmount: null,
        paymentMethod: null,
        paymentNo: null,
        orderStatus: null,
        deliveryStatus: null,
        contactEmail: null,
        buyerNote: null,
        adminNote: null,
        payTime: null,
        deliveryTime: null,
        completeTime: null,
        expireTime: null,
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
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加CDK订单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderId = row.orderId || this.ids
      getOrder(orderId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改CDK订单"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderId != null) {
            updateOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addOrder(this.form).then(response => {
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
      const orderIds = row.orderId || this.ids
      this.$modal.confirm('是否确认删除CDK订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('purchase/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
