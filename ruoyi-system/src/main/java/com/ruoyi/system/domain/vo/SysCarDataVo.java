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
 * 计时操作视图对象 sys_car
 *
 * @author lynn
 * @date 2023-04-20
 */
@Data
@ExcelIgnoreUnannotated
public class SysCarDataVo {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆ID
     */
    @ExcelProperty(value = "车辆ID")
    private Long id;

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
     * 等候人数
     */
    @ExcelProperty(value = "等候人数")
    private Long queueNum;

    /**
     * 状态
     */
    @ExcelProperty(value = "使用状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_car_record_status")
    private Long useStatus;

    /**
     * 本次练车圈数
     */
    @ExcelProperty(value = "本次练车圈数")
    private Long circleTimes;

    /**
     * 本次中心开始时间
     */
    @ExcelProperty(value = "本次中心开始时间")
    private Date trueStartTime;

    /**
     * 本次中心结束时间
     */
    @ExcelProperty(value = "本次中心结束时间")
    private Date trueEndTime;

    private Date endTime;

}
