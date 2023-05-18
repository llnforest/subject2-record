package com.ruoyi.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysCar;
import com.ruoyi.system.domain.bo.SysCarBo;
import com.ruoyi.system.domain.bo.SysCarAnalyseBo;
import com.ruoyi.system.domain.vo.SysCarAnalyseVo;
import com.ruoyi.system.service.ISysCarAnalyseService;
import com.ruoyi.system.service.ISysCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 计时统计
 *
 * @author lynn
 * @date 2023-04-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/carAnalyse")
public class SysCarAnalyseController extends BaseController {

    private final ISysCarAnalyseService iSysCarAnalyseService;

    private final ISysCarService iSysCarService;
    /**
     * 查询计时统计列表
     */
    @SaCheckPermission("system:carAnalyse:list")
    @GetMapping("/list")
    public TableDataInfo<SysCarAnalyseVo> list(SysCarAnalyseBo bo, PageQuery pageQuery) {
        Map<Long, SysCar> carMap = iSysCarService.getCarMap(new SysCarBo());
        TableDataInfo<SysCarAnalyseVo> sysCarAnalyseVoTableDataInfo = iSysCarAnalyseService.queryPageList(bo, pageQuery);
        for (SysCarAnalyseVo row : sysCarAnalyseVoTableDataInfo.getRows()) {
            row.setCarNo(carMap.get(row.getCarId()).getCarNo());
            row.setCarNum(carMap.get(row.getCarId()).getCarNum());
        }
        return sysCarAnalyseVoTableDataInfo;
    }

    /**
     * 导出计时统计列表
     */
    @SaCheckPermission("system:carAnalyse:export")
    @Log(title = "计时统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysCarAnalyseBo bo, HttpServletResponse response) {
        Map<Long, SysCar> carMap = iSysCarService.getCarMap(new SysCarBo());
        List<SysCarAnalyseVo> list = iSysCarAnalyseService.queryList(bo);
        for (SysCarAnalyseVo row : list) {
            row.setCarNo(carMap.get(row.getCarId()).getCarNo());
            row.setCarNum(carMap.get(row.getCarId()).getCarNum());
        }
        ExcelUtil.exportExcel(list, "计时统计", SysCarAnalyseVo.class, response);
    }

    /**
     * 导出每日计时统计列表
     */
    @SaCheckPermission("system:carAnalyse:exportPerDay")
    @Log(title = "每日计时统计", businessType = BusinessType.EXPORT)
    @PostMapping("/exportPerDay")
    public void exportPerDay(SysCarAnalyseBo bo, HttpServletResponse response) {
        Map<Long, SysCar> carMap = iSysCarService.getCarMap(new SysCarBo());
        List<SysCarAnalyseVo> list = iSysCarAnalyseService.queryListPerDay(bo);
        for (SysCarAnalyseVo row : list) {
            row.setCarNo(carMap.get(row.getCarId()).getCarNo());
            row.setCarNum(carMap.get(row.getCarId()).getCarNum());
        }
        ExcelUtil.exportExcel(list, "计时统计", SysCarAnalyseVo.class, response);
    }



}
