package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 计时记录对象 sys_car_record
 *
 * @author lynn
 * @date 2023-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_car_record")
public class SysCarRecord extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 车辆ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 车辆id
     */
    private Long carId;
    /**
     * 状态
     */
    private Long status;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 训练时长
     */
    private Long timeLong;
    /**
     * 分析日期
     */
    private Date analyseDate;

}
