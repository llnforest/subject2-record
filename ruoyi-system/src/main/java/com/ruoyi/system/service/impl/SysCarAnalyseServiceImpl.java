package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysCarAnalyse;
import com.ruoyi.system.domain.bo.SysCarAnalyseBo;
import com.ruoyi.system.domain.vo.SysCarAnalyseVo;
import com.ruoyi.system.mapper.SysCarAnalyseMapper;
import com.ruoyi.system.service.ISysCarAnalyseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 计时统计Service业务层处理
 *
 * @author lynn
 * @date 2023-04-20
 */
@RequiredArgsConstructor
@Service
public class SysCarAnalyseServiceImpl implements ISysCarAnalyseService {

    private final SysCarAnalyseMapper baseMapper;

    /**
     * 查询计时统计
     */
    @Override
    public SysCarAnalyseVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询计时统计列表
     */
    @Override
    public TableDataInfo<SysCarAnalyseVo> queryPageList(SysCarAnalyseBo bo, PageQuery pageQuery) {
        Map<String, Object> params = bo.getParams();
        String analyseDate = "'所有日期'";
        if(params.get("beginAnalyseDate") != null && params.get("endAnalyseDate") != null){
            analyseDate = "'"+params.get("beginAnalyseDate") +"~"+params.get("endAnalyseDate") +"'";
        }
        QueryWrapper<SysCarAnalyse> lqw = buildQueryWrapper(bo);
        lqw.select("car_id","sum(time_long) as time_long","count(1) as queue_num",analyseDate+" as analyse_date","create_by");
        lqw.groupBy(Arrays.asList("car_id"));
        lqw.orderByAsc(Arrays.asList("car_id"));

        Page<SysCarAnalyseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询计时统计列表
     */
    @Override
    public List<SysCarAnalyseVo> queryList(SysCarAnalyseBo bo) {
        Map<String, Object> params = bo.getParams();
        String analyseDate = "'所有日期'";
        if(params.get("beginAnalyseDate") != null && params.get("endAnalyseDate") != null){
            analyseDate = "'"+params.get("beginAnalyseDate") +"~"+params.get("endAnalyseDate") +"'";
        }
        QueryWrapper<SysCarAnalyse> lqw = buildQueryWrapper(bo);
        lqw.select("car_id","sum(time_long) as time_long","count(1) as queue_num",analyseDate+" as analyse_date","create_by");
        lqw.groupBy(Arrays.asList("car_id"));
        lqw.orderByAsc(Arrays.asList("car_id"));

        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询计时统计列表
     */
    @Override
    public List<SysCarAnalyseVo> queryListPerDay(SysCarAnalyseBo bo) {
        QueryWrapper<SysCarAnalyse> lqw = buildQueryWrapper(bo);
        lqw.select("car_id","sum(time_long) as time_long","count(1) as queue_num","analyse_date","create_by");
        lqw.groupBy(Arrays.asList("car_id","analyse_date"));
        lqw.orderByDesc("analyse_date");
        lqw.orderByAsc(Arrays.asList("car_id"));
        return baseMapper.selectVoList(lqw);
    }

    private QueryWrapper<SysCarAnalyse> buildQueryWrapper(SysCarAnalyseBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<SysCarAnalyse> lqw = Wrappers.query();
        lqw.eq(bo.getCarId() != null, "car_id", bo.getCarId());
        lqw.eq("status", 2L);
        lqw.between(params.get("beginAnalyseDate") != null && params.get("endAnalyseDate") != null,
            "analyse_date" ,params.get("beginAnalyseDate"), params.get("endAnalyseDate"));
        lqw.eq(StringUtils.isNotBlank(bo.getCreateBy()), "create_by", bo.getCreateBy());
        return lqw;
    }


}
