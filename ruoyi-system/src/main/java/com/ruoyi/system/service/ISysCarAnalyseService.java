package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.bo.SysCarAnalyseBo;
import com.ruoyi.system.domain.vo.SysCarAnalyseVo;

import java.util.Collection;
import java.util.List;

/**
 * 计时记录Service接口
 *
 * @author lynn
 * @date 2023-04-20
 */
public interface ISysCarAnalyseService {

    /**
     * 查询计时记录
     */
    SysCarAnalyseVo queryById(Long id);

    /**
     * 查询计时记录列表
     */
    TableDataInfo<SysCarAnalyseVo> queryPageList(SysCarAnalyseBo bo, PageQuery pageQuery);

    /**
     * 查询计时记录列表
     */
    List<SysCarAnalyseVo> queryList(SysCarAnalyseBo bo);


    List<SysCarAnalyseVo> queryListPerDay(SysCarAnalyseBo bo);
}
