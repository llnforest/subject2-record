package com.ruoyi.api.controller;

import com.ruoyi.api.domain.dto.CarDto;
import com.ruoyi.api.domain.vo.CarVo;
import com.ruoyi.api.service.ICarService;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.annotation.Sign;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.exception.api.SuccessException;
import com.ruoyi.common.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 车载接口
 *
 * @author Star
 * @description: TODO
 * @date 2023/4/21 13:19
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/car")
public class ApiController {
    private final WebSocketServer webSocketServer;
    private final ICarService carService;

    /**
     * 开始训练
     *
     * @param dto dto
     * @return {@link R}<{@link CarVo}>
     */
    @PostMapping("/start")
    @RepeatSubmit
    @Sign
    public R<CarVo> start(@Validated @RequestBody CarDto dto){
        return R.ok("第1圈开启成功",carService.start(dto));
    }

    /**
     * 结束训练
     *
     * @param dto dto
     * @return {@link R}<{@link Void}>
     */
    @PostMapping("/finish")
    @RepeatSubmit
    public R<Void> finish(@Validated @RequestBody CarDto dto){
        return R.result(carService.finish(dto));
    }
}
