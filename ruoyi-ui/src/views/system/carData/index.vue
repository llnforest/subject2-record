<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车辆编号" prop="carNo">
        <el-input
          v-model="queryParams.carNo"
          placeholder="请输入车辆编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车牌号码" prop="carNum">
        <el-input
          v-model="queryParams.carNum"
          placeholder="请输入车牌号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="使用状态" prop="status">
        <el-select v-model="queryParams.useStatus" placeholder="请选择使用状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_car_record_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">

    </el-row>

    <el-table v-loading="loading" :data="carDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" v-if="false"/>
      <el-table-column label="车辆ID" align="center" prop="id" v-if="false"/>
      <el-table-column label="车辆编号" align="center" prop="carNo" />
      <el-table-column label="车牌号码" align="center" prop="carNum" />
      <el-table-column label="等候人次" align="center" prop="queueNum" />
      <el-table-column label="使用状态" align="center" prop="useStatus"  min-width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_car_record_status" :value="scope.row.useStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="本次练车圈数" align="center" prop="circleTimes" />
      <el-table-column label="本次开始时间" align="center" prop="trueStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.trueStartTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="本次结束时间" align="center" prop="trueEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.trueEndTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">

          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAddOneTime(scope.row)"
            v-hasPermi="['system:carData:addOneTime']"
          >增加1次</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelOneTime(scope.row)"
            v-hasPermi="['system:carData:delOneTime']"
            v-show="scope.row.queueNum >= 1"
          >撤销1次</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-close"
            @click="handleFinishCar(scope.row)"
            v-hasPermi="['system:carData:finishCar']"
            v-show="scope.row.useStatus == 1"
          >强制结束</el-button>
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

    <!-- 添加或修改计时操作对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCarData,
  getCarData,
  delCarData,
  addCarData,
  updateCarData,
  delOneTime,
  addOneTime, finishCar
} from "@/api/system/carData";

export default {
  name: "CarData",
  dicts: ['sys_car_record_status'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
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
      // 计时操作表格数据
      carDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        carNo: undefined,
        carNum: undefined,
        queueNum: undefined,
        useStatus: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "车辆ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询计时操作列表 */
    getList() {
      this.loading = true;
      listCarData(this.queryParams).then(response => {
        this.carDataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        carNo: undefined,
        carNum: undefined,
        queueNum: undefined,
        circleTimes: undefined,
        startTime: undefined,
        endTime: undefined,
        trueStartTime: undefined,
        trueEndTime: undefined,
        createTime: undefined,
        createBy: undefined,
        updateTime: undefined,
        updateBy: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加计时操作";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getCarData(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改计时操作";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateCarData(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addCarData(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 增加1次按钮操作 */
    handleAddOneTime(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(`是否确认为${row.carNo}号车【${row.carNum}】增加1人次练车？`).then(() => {
        this.loading = true;
        return addOneTime(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("增加成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 撤销1次按钮操作 */
    handleDelOneTime(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(`是否确认为${row.carNo}号车【${row.carNum}】撤销1人次练车？`).then(() => {
        this.loading = true;
        return delOneTime(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("撤销成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 撤销1次按钮操作 */
    handleFinishCar(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm(`是否确认强制结束此次练车？`).then(() => {
        this.loading = true;
        return finishCar(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("结束成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/carData/export', {
        ...this.queryParams
      }, `carData_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
