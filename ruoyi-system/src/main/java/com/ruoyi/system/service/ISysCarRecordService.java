package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysCarRecord;
import com.ruoyi.system.domain.vo.SysCarRecordVo;
import com.ruoyi.system.domain.bo.SysCarRecordBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 计时记录Service接口
 *
 * @author lynn
 * @date 2023-04-20
 */
public interface ISysCarRecordService {

    /**
     * 查询计时记录
     */
    SysCarRecordVo queryById(Long id);

    /**
     * 查询计时记录列表
     */
    TableDataInfo<SysCarRecordVo> queryPageList(SysCarRecordBo bo, PageQuery pageQuery);

    /**
     * 查询计时记录列表
     */
    List<SysCarRecordVo> queryList(SysCarRecordBo bo);

    /**
     * 新增计时记录
     */
    Boolean insertByBo(SysCarRecordBo bo);

    /**
     * 修改计时记录
     */
    Boolean updateByBo(SysCarRecordBo bo);

    /**
     * 校验并批量删除计时记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Boolean existsByCarIds(Collection<Long> ids);
}
