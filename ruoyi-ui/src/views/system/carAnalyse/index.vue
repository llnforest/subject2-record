<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="练车日期">
        <el-date-picker
          v-model="daterangeAnalyseDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="车辆选择" prop="carId">
        <el-select v-model="queryParams.carId" placeholder="请选择车辆" clearable>
          <el-option
            v-for="item in carList"
            :key="item.id"
            :label="item.carNo+'号车-'+item.carNum"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="操作人" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入操作人"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:carRecord:export']"
        >页面导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExportPerDay"
          v-hasPermi="['system:carRecord:exportPerDay']"
        >每日导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="carRecordList" @selection-change="handleSelectionChange">
      <el-table-column label="练车日期" align="center"  min-width="100">
        <template slot-scope="scope">
          <span>{{ analyseDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="车辆ID" align="center" prop="carId" v-if="true" min-width="70"/>
      <el-table-column label="车辆编号" align="center" prop="carNo"  min-width="80"/>
      <el-table-column label="车牌号码" align="center" prop="carNum"  min-width="90"/>
      <el-table-column label="练车人次" align="center" prop="queueNum"  min-width="90"/>
      <el-table-column label="训练时长" align="center" prop="timeLong"  min-width="70"/>
      <el-table-column label="操作人" align="center" prop="createBy"  min-width="80"/>
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
import {carList} from "@/api/system/car";
import {listCarAnalyse} from "@/api/system/carAnalyse";

export default {
  name: "CarAnalyse",
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
      // 计时记录表格数据
      carRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 更新人时间范围
      daterangeAnalyseDate: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        carId: undefined,
        status: undefined,
        timeLong: undefined,
        createTime: undefined,
        createBy: undefined,
        updateBy: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "车辆ID不能为空", trigger: "blur" }
        ],
        carId: [
          { required: true, message: "车辆id不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        startTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        timeLong: [
          { required: true, message: "训练时长不能为空", trigger: "blur" }
        ],
      },
      carList:[],
      analyseDate:'所有日期'
    };
  },
  created() {
    this.getList();
    this.getCarList();
  },
  methods: {
    getCarList(){
      carList(this.queryParams).then(response => {
        this.carList = response.data;
      });
    },
    /** 查询计时记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeAnalyseDate && '' != this.daterangeAnalyseDate) {
        this.queryParams.params["beginAnalyseDate"] = this.daterangeAnalyseDate[0];
        this.queryParams.params["endAnalyseDate"] = this.daterangeAnalyseDate[1];
        this.analyseDate = this.daterangeAnalyseDate[0]+'~'+this.daterangeAnalyseDate[1]
      }else{
        this.analyseDate = '所有日期';
      }
      listCarAnalyse(this.queryParams).then(response => {
        this.carRecordList = response.rows;
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
        carId: undefined,
        status: undefined,
        startTime: undefined,
        endTime: undefined,
        timeLong: undefined,
        analyseDate: undefined,
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
      this.daterangeAnalyseDate = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('system/carAnalyse/export', {
        ...this.queryParams
      }, `计时统计页面数据_${new Date().getTime()}.xlsx`)
    },
    /** 导出按钮操作 */
    handleExportPerDay() {
      this.download('system/carAnalyse/exportPerDay', {
        ...this.queryParams
      }, `计时统计每日数据_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
