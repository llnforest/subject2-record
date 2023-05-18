package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.domain.SysCarRecord;
import com.ruoyi.system.mapper.SysCarRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.bo.SysCarDataBo;
import com.ruoyi.system.domain.vo.SysCarDataVo;
import com.ruoyi.system.domain.SysCarData;
import com.ruoyi.system.mapper.SysCarDataMapper;
import com.ruoyi.system.service.ISysCarDataService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 计时操作Service业务层处理
 *
 * @author lynn
 * @date 2023-04-20
 */
@RequiredArgsConstructor
@Service
public class SysCarDataServiceImpl implements ISysCarDataService {

    private final SysCarDataMapper baseMapper;
    private final SysCarRecordMapper recordMapper;

    /**
     * 查询计时操作
     */
    @Override
    public SysCarDataVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询计时操作列表
     */
    @Override
    public TableDataInfo<SysCarDataVo> queryPageList(SysCarDataBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysCarData> lqw = buildQueryWrapper(bo);
        Page<SysCarDataVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询计时操作列表
     */
    @Override
    public List<SysCarDataVo> queryList(SysCarDataBo bo) {
        LambdaQueryWrapper<SysCarData> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysCarData> buildQueryWrapper(SysCarDataBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysCarData> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCarNo()), SysCarData::getCarNo, bo.getCarNo());
        lqw.eq(StringUtils.isNotBlank(bo.getCarNum()), SysCarData::getCarNum, bo.getCarNum());
        lqw.eq(SysCarData::getStatus, 1);
        lqw.eq(bo.getQueueNum() != null, SysCarData::getQueueNum, bo.getQueueNum());
        lqw.eq(bo.getUseStatus() != null, SysCarData::getUseStatus, bo.getUseStatus());
        lqw.orderByAsc(SysCarData::getCarNo);
        return lqw;
    }

    /**
     * 新增计时操作
     */
    @Override
    public Boolean insertByBo(SysCarDataBo bo) {
        SysCarData add = BeanUtil.toBean(bo, SysCarData.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改计时操作
     */
    @Override
    public Boolean updateByBo(SysCarDataBo bo) {
        SysCarData update = BeanUtil.toBean(bo, SysCarData.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysCarData entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除计时操作
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 增加一次
     */
    @Override
    @Transactional
    public Boolean addOneTime(Long id) {
        //获取数据
        SysCarDataVo sysCarDataVo = baseMapper.selectVoById(id);
        if (sysCarDataVo == null) return false;
        //更新车辆候车人次数据
        sysCarDataVo.setQueueNum(sysCarDataVo.getQueueNum() + 1);
        if(baseMapper.updateById(BeanUtil.toBean(sysCarDataVo,SysCarData.class)) < 1) return false;
        // 新增记录数据
        SysCarRecord sysCarRecord = new SysCarRecord();
        sysCarRecord.setCarId(sysCarDataVo.getId());
        sysCarRecord.setStatus(0L);
        if(recordMapper.insert(sysCarRecord) < 1) throw new RuntimeException();
        return true;
    }

    /**
     * 增加一次
     */
    @Override
    @Transactional
    public Boolean delOneTime(Long id) {
        //获取数据
        SysCarDataVo sysCarDataVo = baseMapper.selectVoById(id);
        if (sysCarDataVo == null || sysCarDataVo.getQueueNum() < 1) return false;
        //更新车辆候车人次数据
        sysCarDataVo.setQueueNum(sysCarDataVo.getQueueNum() - 1);
        if(baseMapper.updateById(BeanUtil.toBean(sysCarDataVo,SysCarData.class)) < 1) return false;
        // 删除记录数据
        SysCarRecord sysCarRecord = new SysCarRecord();
        sysCarRecord.setCarId(sysCarDataVo.getId());
        sysCarRecord.setStatus(0L);
        if(recordMapper.deleteByCarIdAndStatusOnOne(sysCarDataVo.getId(),0L) < 1) throw new RuntimeException("不存在多余的等候人次");
        return true;
    }

    /**
     * 增加一次
     */
    @Override
    @Transactional
    public Boolean finishCar(long id) {
        //更新车辆候车人次数据
        SysCarData sysCarData = new SysCarData();
        sysCarData.setId(id);
        sysCarData.setUseStatus(2l);
        sysCarData.setEndTime(new Date());
        sysCarData.setTrueEndTime(new Date());
        if(baseMapper.updateById(sysCarData) < 1) return false;

        // 更新记录中的车辆
        QueryWrapper<SysCarRecord> qw = new QueryWrapper<>();
        qw.eq("car_id",id);
        qw.eq("status",1L);
        SysCarRecord sysCarRecord = recordMapper.selectOne(qw);
        if(sysCarRecord != null) {
            sysCarRecord.setStatus(2L);
            sysCarRecord.setEndTime(new Date());
            sysCarRecord.setTimeLong(DateUtil.between(sysCarRecord.getStartTime(),sysCarRecord.getEndTime(), DateUnit.MINUTE));
            recordMapper.updateById(sysCarRecord);
        }
        return true;
    }
}
