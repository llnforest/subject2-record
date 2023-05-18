package com.ruoyi.system.controller;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.ruoyi.system.domain.bo.SysCarBo;
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
import com.ruoyi.system.domain.vo.SysCarDataVo;
import com.ruoyi.system.domain.bo.SysCarDataBo;
import com.ruoyi.system.service.ISysCarDataService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 计时操作
 *
 * @author lynn
 * @date 2023-04-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/carData")
public class SysCarDataController extends BaseController {

    private final ISysCarDataService iSysCarDataService;

    /**
     * 查询计时操作列表
     */
    @SaCheckPermission("system:carData:list")
    @GetMapping("/list")
    public TableDataInfo<SysCarDataVo> list(SysCarDataBo bo, PageQuery pageQuery) {
        return iSysCarDataService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出计时操作列表
     */
    @SaCheckPermission("system:carData:export")
    @Log(title = "计时操作", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysCarDataBo bo, HttpServletResponse response) {
        List<SysCarDataVo> list = iSysCarDataService.queryList(bo);
        ExcelUtil.exportExcel(list, "计时操作", SysCarDataVo.class, response);
    }


    /**
     * 增加一次操作
     *
     * @param id 主键串
     */
    @SaCheckPermission("system:carData:addOneTime")
    @Log(title = "计时操作", businessType = BusinessType.UPDATE)
    @PutMapping("/addOneTime/{id}")
    public R<Void> addOneTime(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        return toAjax(iSysCarDataService.addOneTime(id));
    }

    /**
     * 撤销一次操作
     *
     * @param id 主键串
     */
    @SaCheckPermission("system:carData:delOneTime")
    @Log(title = "计时操作", businessType = BusinessType.UPDATE)
    @PutMapping("/delOneTime/{id}")
    public R<Void> delOneTime(@NotNull(message = "主键不能为空")
                              @PathVariable Long id) {
        return toAjax(iSysCarDataService.delOneTime(id));
    }

    /**
     * 强制结束
     */
    @SaCheckPermission("system:carData:finishCar")
    @Log(title = "计时操作", businessType = BusinessType.UPDATE)
    @PutMapping("/finishCar/{id}")
    public R<Void> finishCar(@NotNull(message="主键不能为空") @PathVariable Long id) {
        return toAjax(iSysCarDataService.finishCar(id));
    }
}
