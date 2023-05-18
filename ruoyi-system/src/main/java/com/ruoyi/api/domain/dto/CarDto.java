package com.ruoyi.api.domain.dto;

import com.ruoyi.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Star
 * @description: TODO
 * @date 2023/4/21 13:31
 */
@Data
@EqualsAndHashCode
public class CarDto {
    @NotEmpty(message = "车辆编号不能为空")
    private String carNo;

    @NotNull(message = "时间戳不能为空")
    private Long time;

//    @NotEmpty(message = "签名不能为空")
    private String sign;
}
