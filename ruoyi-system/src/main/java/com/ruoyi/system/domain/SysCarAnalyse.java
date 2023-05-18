package com.ruoyi.system.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 计时记录对象 sys_car_record
 *
 * @author lynn
 * @date 2023-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_car_record")
public class SysCarAnalyse extends BaseEntity {

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
     * 训练时长
     */
    private Long timeLong;
    /**
     * 分析日期
     */
    private String analyseDate;

    /**
     * 练车人次
     */
    private Long queueNum;
    /**
     * 操作人员
     */
    private String createBy;


}
