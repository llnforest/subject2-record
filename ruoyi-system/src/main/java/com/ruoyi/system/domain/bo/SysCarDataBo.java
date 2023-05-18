package com.ruoyi.system.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 计时操作业务对象 sys_car
 *
 * @author lynn
 * @date 2023-04-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysCarDataBo extends BaseEntity {

    /**
     * 车辆ID
     */
    @NotNull(message = "车辆ID不能为空", groups = { EditGroup.class })
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

    /**
     * 状态
     */
    private Long useStatus;


}
