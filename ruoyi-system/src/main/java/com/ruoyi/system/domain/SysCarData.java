package com.ruoyi.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 计时操作对象 sys_car
 *
 * @author lynn
 * @date 2023-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_car")
public class SysCarData extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 车辆ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 车辆编号
     */
    private String carNo;
    /**
     * 车牌号码
     */
    private String carNum;
    /**
     * 等候人数
     */
    private Long queueNum;
    private Long status;
    /**
     * 状态
     */
    private Long useStatus;
    /**
     * 本次练车圈数
     */
    private Long circleTimes;
    /**
     * 本次车载开始时间
     */
    private Date startTime;
    /**
     * 本次车载结束时间
     */
    private Date endTime;
    /**
     * 本次中心开始时间
     */
    private Date trueStartTime;
    /**
     * 本次中心结束时间
     */
    private Date trueEndTime;

}
