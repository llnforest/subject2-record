package com.ruoyi.api.service;

import com.ruoyi.api.domain.dto.CarDto;
import com.ruoyi.api.domain.vo.CarVo;

/**
 * @author Star
 * @description: TODO
 * @date 2023/4/21 14:38
 */

public interface ICarService {

    CarVo start(CarDto dto);

    Boolean finish(CarDto dto);
}
