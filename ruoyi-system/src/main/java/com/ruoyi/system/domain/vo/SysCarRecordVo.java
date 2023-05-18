package com.ruoyi.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SysCarRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆ID
     */
    @ExcelProperty(value = "记录ID")
    private Long id;

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
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_car_record_status")
    private Long status;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 训练时长
     */
    @ExcelProperty(value = "训练时长")
    private Long timeLong;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 上传人
     */
    @ExcelProperty(value = "操作人")
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;


}
