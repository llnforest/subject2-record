package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.domain.SysCar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.bo.SysCarRecordBo;
import com.ruoyi.system.domain.vo.SysCarRecordVo;
import com.ruoyi.system.domain.SysCarRecord;
import com.ruoyi.system.mapper.SysCarRecordMapper;
import com.ruoyi.system.service.ISysCarRecordService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 计时记录Service业务层处理
 *
 * @author lynn
 * @date 2023-04-20
 */
@RequiredArgsConstructor
@Service
public class SysCarRecordServiceImpl implements ISysCarRecordService {

    private final SysCarRecordMapper baseMapper;

    /**
     * 查询计时记录
     */
    @Override
    public SysCarRecordVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询计时记录列表
     */
    @Override
    public TableDataInfo<SysCarRecordVo> queryPageList(SysCarRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysCarRecord> lqw = buildQueryWrapper(bo);
        Page<SysCarRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询计时记录列表
     */
    @Override
    public List<SysCarRecordVo> queryList(SysCarRecordBo bo) {
        LambdaQueryWrapper<SysCarRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysCarRecord> buildQueryWrapper(SysCarRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysCarRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCarId() != null, SysCarRecord::getCarId, bo.getCarId());
        lqw.eq(bo.getStatus() != null, SysCarRecord::getStatus, bo.getStatus());
        lqw.eq(bo.getTimeLong() != null, SysCarRecord::getTimeLong, bo.getTimeLong());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            SysCarRecord::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(StringUtils.isNotBlank(bo.getCreateBy()), SysCarRecord::getCreateBy, bo.getCreateBy());
        lqw.eq(StringUtils.isNotBlank(bo.getUpdateBy()), SysCarRecord::getUpdateBy, bo.getUpdateBy());
        return lqw;
    }

    /**
     * 新增计时记录
     */
    @Override
    public Boolean insertByBo(SysCarRecordBo bo) {
        SysCarRecord add = BeanUtil.toBean(bo, SysCarRecord.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改计时记录
     */
    @Override
    public Boolean updateByBo(SysCarRecordBo bo) {
        SysCarRecord update = BeanUtil.toBean(bo, SysCarRecord.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysCarRecord entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除计时记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 批量删除计时记录
     */
    @Override
    public Boolean existsByCarIds(Collection<Long> ids) {
        LambdaQueryWrapper<SysCarRecord> sysCarRecordLambdaQueryWrapper = new LambdaQueryWrapper();
        LambdaQueryWrapper<SysCarRecord> lqw = Wrappers.lambdaQuery();
        lqw.in(CollectionUtil.isNotEmpty(ids),SysCarRecord::getCarId,ids);
        return baseMapper.exists(lqw);

    }
}
