package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysCar;
import com.ruoyi.system.domain.vo.SysCarVo;
import com.ruoyi.system.domain.bo.SysCarBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 车辆管理Service接口
 *
 * @author lynn
 * @date 2023-04-20
 */
public interface ISysCarService {

    /**
     * 查询车辆管理
     */
    SysCarVo queryById(Long id);

    /**
     * 查询车辆管理列表
     */
    TableDataInfo<SysCarVo> queryPageList(SysCarBo bo, PageQuery pageQuery);

    /**
     * 查询车辆管理列表
     */
    List<SysCarVo> queryList(SysCarBo bo);

    /**
     * 新增车辆管理
     */
    Boolean insertByBo(SysCarBo bo);

    /**
     * 修改车辆管理
     */
    Boolean updateByBo(SysCarBo bo);

    /**
     * 校验并批量删除车辆管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Map<Long, SysCar> getCarMap(SysCarBo bo);
}
