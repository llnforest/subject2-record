package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.system.domain.SysCarData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.bo.SysCarBo;
import com.ruoyi.system.domain.vo.SysCarVo;
import com.ruoyi.system.domain.SysCar;
import com.ruoyi.system.mapper.SysCarMapper;
import com.ruoyi.system.service.ISysCarService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 车辆管理Service业务层处理
 *
 * @author lynn
 * @date 2023-04-20
 */
@RequiredArgsConstructor
@Service
public class SysCarServiceImpl implements ISysCarService {

    private final SysCarMapper baseMapper;

    /**
     * 查询车辆管理
     */
    @Override
    public SysCarVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询车辆管理列表
     */
    @Override
    public TableDataInfo<SysCarVo> queryPageList(SysCarBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysCar> lqw = buildQueryWrapper(bo);
        Page<SysCarVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询车辆管理列表
     */
    @Override
    public List<SysCarVo> queryList(SysCarBo bo) {
        LambdaQueryWrapper<SysCar> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysCar> buildQueryWrapper(SysCarBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysCar> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCarNo()), SysCar::getCarNo, bo.getCarNo());
        lqw.eq(StringUtils.isNotBlank(bo.getCarNum()), SysCar::getCarNum, bo.getCarNum());
        lqw.eq(bo.getStatus() != null, SysCar::getStatus, bo.getStatus());
        lqw.orderByDesc(SysCar::getStatus);
        lqw.orderByAsc(SysCar::getCarNo);
        return lqw;
    }

    /**
     * 新增车辆管理
     */
    @Override
    public Boolean insertByBo(SysCarBo bo) {
        SysCar add = BeanUtil.toBean(bo, SysCar.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改车辆管理
     */
    @Override
    public Boolean updateByBo(SysCarBo bo) {
        SysCar update = BeanUtil.toBean(bo, SysCar.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysCar entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除车辆管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
    /**
     * 批量删除车辆管理
     */
    @Override
    public Map<Long, SysCar> getCarMap(SysCarBo bo) {
        LambdaQueryWrapper<SysCar> lbq = new LambdaQueryWrapper<>();
        lbq.select(SysCar::getId,SysCar::getCarNo,SysCar::getCarNum);
        lbq.eq(bo.getStatus() != null,SysCar::getStatus, bo.getStatus());
        return baseMapper.selectList(lbq).stream().collect(Collectors.toMap(SysCar::getId, o -> o));
//        Map<Long, SysCar> SysCarMap = new HashMap<>();
//        for (SysCar sysCar : baseMapper.selectList(lbq)) {
//            SysCarMap.put(sysCar.getId(), sysCar);
//        }
//        return SysCarMap;
    }

}
