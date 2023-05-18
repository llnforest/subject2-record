package com.ruoyi.system.controller;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysCar;
import com.ruoyi.system.service.ISysCarRecordService;
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
import com.ruoyi.system.domain.vo.SysCarVo;
import com.ruoyi.system.domain.bo.SysCarBo;
import com.ruoyi.system.service.ISysCarService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆管理
 *
 * @author lynn
 * @date 2023-04-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/car")
public class SysCarController extends BaseController {

    private final ISysCarService iSysCarService;
    private final ISysCarRecordService recordService;

    /**
     * 查询车辆管理列表
     */
    @SaCheckPermission("system:car:list")
    @GetMapping("/list")
    public TableDataInfo<SysCarVo> list(SysCarBo bo, PageQuery pageQuery) {
        return iSysCarService.queryPageList(bo, pageQuery);
    }

    @GetMapping("/carList")
    public R<List<SysCarVo>>  carList(SysCarBo bo) {
        bo.setStatus(1L);
        return R.ok(iSysCarService.queryList(bo));
    }

    /**
     * 导出车辆管理列表
     */
    @SaCheckPermission("system:car:export")
    @Log(title = "车辆管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysCarBo bo, HttpServletResponse response) {
        List<SysCarVo> list = iSysCarService.queryList(bo);
        ExcelUtil.exportExcel(list, "车辆管理", SysCarVo.class, response);
    }

    /**
     * 获取车辆管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:car:query")
    @GetMapping("/{id}")
    public R<SysCarVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iSysCarService.queryById(id));
    }

    /**
     * 新增车辆管理
     */
    @SaCheckPermission("system:car:add")
    @Log(title = "车辆管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysCarBo bo) {
        return toAjax(iSysCarService.insertByBo(bo));
    }

    /**
     * 修改车辆管理
     */
    @SaCheckPermission("system:car:edit")
    @Log(title = "车辆管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysCarBo bo) {
        return toAjax(iSysCarService.updateByBo(bo));
    }

    /**
     * 删除车辆管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:car:remove")
    @Log(title = "车辆管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        if(recordService.existsByCarIds(Arrays.asList(ids))) throw new RuntimeException("存在练车数据不能删除");
        return toAjax(iSysCarService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:car:changeStatus")
    @Log(title = "车辆管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody SysCarBo carBo) {
        return toAjax(iSysCarService.updateByBo(carBo));
    }
}
