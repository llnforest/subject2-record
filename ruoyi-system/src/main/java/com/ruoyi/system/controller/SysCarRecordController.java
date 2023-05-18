package com.ruoyi.system.controller;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ruoyi.system.domain.SysCar;
import com.ruoyi.system.domain.bo.SysCarBo;
import com.ruoyi.system.service.ISysCarService;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.validate.QueryGroup;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.vo.SysCarRecordVo;
import com.ruoyi.system.domain.bo.SysCarRecordBo;
import com.ruoyi.system.service.ISysCarRecordService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 计时记录
 *
 * @author lynn
 * @date 2023-04-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/carRecord")
public class SysCarRecordController extends BaseController {

    private final ISysCarRecordService iSysCarRecordService;

    private final ISysCarService iSysCarService;
    /**
     * 查询计时记录列表
     */
    @SaCheckPermission("system:carRecord:list")
    @GetMapping("/list")
    public TableDataInfo<SysCarRecordVo> list(SysCarRecordBo bo, PageQuery pageQuery) {
        Map<Long, SysCar> carMap = iSysCarService.getCarMap(new SysCarBo());
        TableDataInfo<SysCarRecordVo> sysCarRecordVoTableDataInfo = iSysCarRecordService.queryPageList(bo, pageQuery);
        for (SysCarRecordVo row : sysCarRecordVoTableDataInfo.getRows()) {
            row.setCarNo(carMap.get(row.getCarId()).getCarNo());
            row.setCarNum(carMap.get(row.getCarId()).getCarNum());
        }
        return sysCarRecordVoTableDataInfo;
    }

    /**
     * 导出计时记录列表
     */
    @SaCheckPermission("system:carRecord:export")
    @Log(title = "计时记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysCarRecordBo bo, HttpServletResponse response) {
        Map<Long, SysCar> carMap = iSysCarService.getCarMap(new SysCarBo());
        List<SysCarRecordVo> list = iSysCarRecordService.queryList(bo);
        for (SysCarRecordVo row : list) {
            row.setCarNo(carMap.get(row.getCarId()).getCarNo());
            row.setCarNum(carMap.get(row.getCarId()).getCarNum());
        }
        ExcelUtil.exportExcel(list, "计时记录", SysCarRecordVo.class, response);
    }



}
