package com.ruoyi.system.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 计时记录业务对象 sys_car_record
 *
 * @author lynn
 * @date 2023-04-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysCarAnalyseBo extends BaseEntity {

    /**
     * 车辆ID
     */
    @NotNull(message = "车辆ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 车辆id
     */
    @NotNull(message = "车辆id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long carId;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * 训练时长
     */
    @NotNull(message = "训练时长不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long timeLong;

    /**
     * 训练时长
     */
    @NotNull(message = "训练时长不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long analyseDate;


}
