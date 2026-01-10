package com.school.campusexpress.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.common.R;
import com.school.campusexpress.entity.Station;
import com.school.campusexpress.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "快递站管理", description = "管理员快递站管理接口")
@RestController
@RequestMapping("/admin/station")
public class AdminStationController {

    @Autowired
    private StationService stationService;

    @Operation(summary = "添加快递站")
    @PostMapping("/add")
    public R<Station> addStation(@Valid @RequestBody Station station) {
        try {
            Station result = stationService.addStation(station);
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "快递站列表")
    @GetMapping("/list")
    public R<Page<Station>> getStationList(
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<Station> page = stationService.getStationList(status, pageNum, pageSize);
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "快递站详情")
    @GetMapping("/{id}")
    public R<Station> getStationById(@Parameter(description = "快递站ID") @PathVariable Long id) {
        try {
            Station station = stationService.getStationById(id);
            if (station != null) {
                return R.success(station);
            }
            return R.error("快递站不存在");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "更新快递站")
    @PutMapping("/update")
    public R<Station> updateStation(@RequestBody Station station) {
        try {
            Station result = stationService.updateStation(station);
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "删除快递站")
    @DeleteMapping("/delete/{id}")
    public R<String> deleteStation(@Parameter(description = "快递站ID") @PathVariable Long id) {
        try {
            boolean success = stationService.deleteStation(id);
            if (success) {
                return R.success("删除成功");
            }
            return R.error("删除失败");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
