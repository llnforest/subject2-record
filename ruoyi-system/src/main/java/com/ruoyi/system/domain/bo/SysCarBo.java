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
 * 车辆管理业务对象 sys_car
 *
 * @author lynn
 * @date 2023-04-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysCarBo extends BaseEntity {

    /**
     * 车辆ID
     */
    @NotNull(message = "车辆ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 车辆编号
     */
    @NotBlank(message = "车辆编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carNo;

    /**
     * 车牌号码
     */
    @NotBlank(message = "车牌号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carNum;

    /**
     * 车牌状态
     */
    @NotNull(message = "车辆状态", groups = { AddGroup.class, EditGroup.class })
    private Long status;


}
