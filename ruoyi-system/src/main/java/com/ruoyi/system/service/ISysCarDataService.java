package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysCarData;
import com.ruoyi.system.domain.vo.SysCarDataVo;
import com.ruoyi.system.domain.bo.SysCarDataBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 计时操作Service接口
 *
 * @author lynn
 * @date 2023-04-20
 */
public interface ISysCarDataService {

    /**
     * 查询计时操作
     */
    SysCarDataVo queryById(Long id);

    /**
     * 查询计时操作列表
     */
    TableDataInfo<SysCarDataVo> queryPageList(SysCarDataBo bo, PageQuery pageQuery);

    /**
     * 查询计时操作列表
     */
    List<SysCarDataVo> queryList(SysCarDataBo bo);

    /**
     * 新增计时操作
     */
    Boolean insertByBo(SysCarDataBo bo);

    /**
     * 修改计时操作
     */
    Boolean updateByBo(SysCarDataBo bo);

    /**
     * 校验并批量删除计时操作信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    @Transactional
    Boolean addOneTime(Long id);

    @Transactional
    Boolean delOneTime(Long id);

    @Transactional
    Boolean finishCar(long id);
}
