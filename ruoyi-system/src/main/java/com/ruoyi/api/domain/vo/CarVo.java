package com.ruoyi.api.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Star
 * @description: TODO
 * @date 2023/4/21 13:31
 */
@Data
public class CarVo {
    private String carNo;

    private Date endTime;

    private String circleNum;
}
