package com.ruoyi.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 计时记录视图对象 sys_car_record
 *
 * @author lynn
 * @date 2023-04-20
 */
@Data
@ExcelIgnoreUnannotated
public class SysCarAnalyseVo {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆ID
     */
    private Long id;

    /**
     * 练车日期
     */
    @ExcelProperty(value = "练车日期")
    private String analyseDate;

    /**
     * 车辆id
     */
    @ExcelProperty(value = "车辆id")
    private Long carId;

    /**
     * 车辆编号
     */
    @ExcelProperty(value = "车辆编号")
    private String carNo;

    /**
     * 车牌号码
     */
    @ExcelProperty(value = "车牌号码")
    private String carNum;

    /**
     * 状态
     */
    private Long status;




    /**
     * 练车人次
     */
    @ExcelProperty(value = "练车人次")
    private Long queueNum;
    /**
     * 训练时长
     */
    @ExcelProperty(value = "练车时长")
    private Long timeLong;


    /**
     * 操作人员
     */
    @ExcelProperty(value = "操作人员")
    private String createBy;



}
