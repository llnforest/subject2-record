package com.ruoyi.api.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.api.domain.dto.CarDto;
import com.ruoyi.api.domain.vo.CarVo;
import com.ruoyi.api.service.ICarService;
import com.ruoyi.common.constant.CacheNames;
import com.ruoyi.common.exception.api.FailException;
import com.ruoyi.common.exception.api.SuccessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.redis.CacheUtils;
import com.ruoyi.common.websocket.WebSocketServer;
import com.ruoyi.system.domain.SysCarData;
import com.ruoyi.system.domain.SysCarRecord;
import com.ruoyi.system.domain.vo.SysCarDataVo;
import com.ruoyi.system.domain.vo.SysCarRecordVo;
import com.ruoyi.system.mapper.SysCarDataMapper;
import com.ruoyi.system.mapper.SysCarRecordMapper;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Star
 * @description: TODO
 * @date 2023/4/21 14:39
 */
@RequiredArgsConstructor
@Service
public class CarServiceImpl implements ICarService {
    private final SysCarDataMapper baseMapper;
    private final SysCarRecordMapper recordMapper;

    private final WebSocketServer webSocketServer;

    @Override
    @Transactional(noRollbackFor={SuccessException.class,FailException.class})
    public CarVo start(CarDto dto){
        CarVo carVo = new CarVo();
        LambdaQueryWrapper<SysCarData> lqw = buildQueryWrapper(dto);
        SysCarDataVo sysCarDataVo = baseMapper.selectVoOne(lqw);
        if(sysCarDataVo == null) throw new RuntimeException("车辆不存在或状态不可用");
        //判断车辆使用状态
        handleUseStatus(sysCarDataVo,dto);
//        carVo.setCarNo(dto.getCarNo());
//        carVo.setCircleNum("1");
//        carVo.setEndTime(DateUtil.offsetMinute(new Date(dto.getTime()),Integer.valueOf(CacheUtils.get(CacheNames.SYS_CONFIG,"sys_car_delay"))));
        return null;
    }

    @Override
    @Transactional(noRollbackFor={SuccessException.class,FailException.class})
    public Boolean finish(CarDto dto){
        LambdaQueryWrapper<SysCarData> lqw = buildQueryWrapper(dto);
        SysCarDataVo sysCarDataVo = baseMapper.selectVoOne(lqw);
        if(sysCarDataVo == null) throw new RuntimeException("车辆不存在或状态不可用");
        //判断车辆使用状态
        handleFinish(sysCarDataVo,dto);
        return true;
    }

    /**
     * 处理结束状态
     *
     * @param sysCarDataVo sys汽车数据
     * @param carDto       汽车dto
     */
    private void handleFinish(SysCarDataVo sysCarDataVo,CarDto carDto){
        if(sysCarDataVo.getUseStatus() == 1L){
            handleFinishStarting(sysCarDataVo,carDto);
        }else{
            throw new RuntimeException("车辆状态已结束");
        }
    }

    /**
     * 开始结束 正在使用中的车
     *
     * @param sysCarDataVo sys汽车数据
     * @param carDto       汽车dto
     */
    private void handleFinishStarting(SysCarDataVo sysCarDataVo,CarDto carDto){
        if(new Date().getTime() < sysCarDataVo.getTrueEndTime().getTime()){
            throw new SuccessException("时间未到,结束成功");
        }else if(sysCarDataVo.getCircleTimes() < Long.valueOf(CacheUtils.get(CacheNames.SYS_CONFIG,"sys_car_circle"))){
            throw new SuccessException("圈数未满足,结束成功");
        }else{
            updateStatusToEnd(sysCarDataVo.getId());
        }
    };
    /**
     * 处理使用状态
     *
     * @param sysCarDataVo sys汽车数据
     * @param carDto       汽车dto
     */
    private void handleUseStatus(SysCarDataVo sysCarDataVo,CarDto carDto){
        if(sysCarDataVo.getUseStatus() == 1L){
            handleUseStatusStarting(sysCarDataVo,carDto);
        }else{
            handleUseStatusUnStart(sysCarDataVo,carDto);
        }
    }

    /**
     * 处理使用状态 未开始
     *
     * @param sysCarDataVo sys汽车数据
     * @param carDto       汽车dto
     */
    private void handleUseStatusUnStart(SysCarDataVo sysCarDataVo,CarDto carDto){
        if(sysCarDataVo.getQueueNum() == 0L) throw new FailException("没有可练车时间，请联系管理员");
        updateStatusToStart(sysCarDataVo,carDto);
    };

    /**
     * 处理使用状态 使用中
     *
     * @param sysCarDataVo sys汽车数据
     * @param carDto       汽车dto
     */
    private void handleUseStatusStarting(SysCarDataVo sysCarDataVo,CarDto carDto){
        if(new Date().getTime() < sysCarDataVo.getTrueEndTime().getTime()){
            updateStatusToContinue(sysCarDataVo);
            throw new SuccessException("时间未到,继续训练第"+sysCarDataVo.getCircleTimes()+"圈");
        }else if(sysCarDataVo.getCircleTimes() < new Long(CacheUtils.get(CacheNames.SYS_CONFIG,"sys_car_circle"))){
            updateStatusToContinue(sysCarDataVo);
            throw new SuccessException("圈数未满足,继续训练第"+sysCarDataVo.getCircleTimes()+"圈");
        }else{
            updateStatusToEnd(sysCarDataVo.getId());
            handleUseStatusUnStart(sysCarDataVo,carDto);
        }
    };

    /**
     * 更新状态继续
     *
     * @param sysCarDataVo sys汽车数据
     */
    private void updateStatusToContinue(SysCarDataVo sysCarDataVo){
        //开始车辆表
        SysCarData sysCarData = new SysCarData();
        sysCarData.setId(sysCarDataVo.getId());
        sysCarData.setCircleTimes(sysCarDataVo.getCircleTimes() + 1);

        if(baseMapper.updateById(sysCarData) < 1) throw new RuntimeException("网络异常,继续失败");
    }

    /**
     * 更新状态开始
     *
     * @param sysCarDataVo sys汽车数据
     * @param carDto       汽车dto
     */
    private void updateStatusToStart(SysCarDataVo sysCarDataVo,CarDto carDto){
        int timeLong = Integer.valueOf(CacheUtils.get(CacheNames.SYS_CONFIG,"sys_car_delay"));
        //开始车辆表
        SysCarData sysCarData = new SysCarData();
        sysCarData.setId(sysCarDataVo.getId());
        sysCarData.setQueueNum(sysCarDataVo.getQueueNum() -1);
        sysCarData.setUseStatus(1L);
        sysCarData.setCircleTimes(1L);
        sysCarData.setStartTime(new Date(carDto.getTime()));
        sysCarData.setEndTime(DateUtil.offsetMinute(new Date(carDto.getTime()),timeLong));
        sysCarData.setTrueStartTime(new Date());
        sysCarData.setTrueEndTime(DateUtil.offsetMinute(new Date(),timeLong));

        if(baseMapper.updateById(sysCarData) < 1) throw new RuntimeException("网络异常,开始失败");
        //结束记录表
        LambdaQueryWrapper<SysCarRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysCarRecord::getCarId,sysCarDataVo.getId());
        lqw.eq(SysCarRecord::getStatus,0L);
        SysCarRecordVo sysCarRecordVo = recordMapper.selectVoOne(lqw);
        if(sysCarRecordVo == null)  throw new RuntimeException("无可开始人次,开始失败");

        SysCarRecord sysCarRecord = new SysCarRecord();
        sysCarRecord.setId(sysCarRecordVo.getId());
        sysCarRecord.setStartTime(new Date());
        sysCarRecord.setEndTime(DateUtil.offsetMinute(new Date(),timeLong));
        sysCarRecord.setStatus(1L);
        sysCarRecord.setTimeLong(new Long(timeLong));
        sysCarRecord.setAnalyseDate(DateUtil.date());
        if(recordMapper.updateById(sysCarRecord) < 1)  throw new RuntimeException("开始人次时异常,开始失败");

    }

    /**
     * 更新状态结束
     *
     * @param id id
     */
    private void updateStatusToEnd(Long id){
        //结束车辆表
        SysCarData sysCarData = new SysCarData();
        sysCarData.setId(id);
        sysCarData.setUseStatus(2L);
        sysCarData.setTrueEndTime(new Date());
        if(baseMapper.updateById(sysCarData) < 1) throw new RuntimeException("网络异常,结束失败");
        //结束记录表
        LambdaQueryWrapper<SysCarRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysCarRecord::getCarId,id);
        lqw.eq(SysCarRecord::getStatus,1L);
        SysCarRecordVo sysCarRecordVo = recordMapper.selectVoOne(lqw);
        if(sysCarRecordVo == null)  throw new RuntimeException("无可结束人次,结束失败");

        SysCarRecord sysCarRecord = new SysCarRecord();
        sysCarRecord.setId(sysCarRecordVo.getId());
        sysCarRecord.setEndTime(new Date());
        sysCarRecord.setStatus(2L);
        sysCarRecord.setTimeLong(DateUtil.between(new Date(),sysCarRecordVo.getStartTime(), DateUnit.MINUTE));
        if(recordMapper.updateById(sysCarRecord) < 1)  throw new RuntimeException("结束人次时异常,结束失败");
        // 发送结束指令
        webSocketServer.sendMessage(sysCarRecordVo.getCarNo()+"号车"+sysCarData.getCarNum()+"已结束训练");
    }

    private LambdaQueryWrapper<SysCarData> buildQueryWrapper(CarDto dto) {
        LambdaQueryWrapper<SysCarData> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getCarNo()), SysCarData::getCarNo, dto.getCarNo());
        lqw.eq( SysCarData::getStatus, 1L);
        return lqw;
    }
}
