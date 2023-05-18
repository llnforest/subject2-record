package com.ruoyi.system.mapper;
import org.apache.ibatis.annotations.Param;

import com.ruoyi.system.domain.SysCarRecord;
import com.ruoyi.system.domain.vo.SysCarRecordVo;
import com.ruoyi.common.core.mapper.BaseMapperPlus;

/**
 * 计时记录Mapper接口
 *
 * @author lynn
 * @date 2023-04-20
 */
public interface SysCarRecordMapper extends BaseMapperPlus<SysCarRecordMapper, SysCarRecord, SysCarRecordVo> {
    int deleteByCarIdAndStatusOnOne(@Param("carId") Long carId, @Param("status") Long status);

}
