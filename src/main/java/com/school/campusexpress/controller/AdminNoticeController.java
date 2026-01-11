package com.school.campusexpress.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.annotation.RequireAuth;
import com.school.campusexpress.common.R;
import com.school.campusexpress.entity.Notice;
import com.school.campusexpress.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
@Tag(name = "公告管理", description = "管理员公告管理接口")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @PostMapping("/add")
    @Operation(summary = "新增公告")
    @RequireAuth(roles = {"admin"})
    public R<Notice> addNotice(@RequestBody Notice notice, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            notice.setCreatorId(userId);
            Notice result = noticeService.addNotice(notice);
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    @Operation(summary = "编辑公告")
    @RequireAuth(roles = {"admin"})
    public R<Notice> updateNotice(@RequestBody Notice notice) {
        try {
            Notice result = noticeService.updateNotice(notice);
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除公告")
    @RequireAuth(roles = {"admin"})
    public R<String> deleteNotice(@PathVariable Long id) {
        try {
            boolean success = noticeService.deleteNotice(id);
            if (success) {
                return R.success("删除成功");
            }
            return R.error("删除失败");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "公告列表（分页）")
    @RequireAuth(roles = {"admin"})
    public R<Page<Notice>> getNoticePage(
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<Notice> page = noticeService.getNoticePage(status, pageNum, pageSize);
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "公告详情")
    @RequireAuth(roles = {"admin"})
    public R<Notice> getNoticeById(@PathVariable Long id) {
        try {
            Notice notice = noticeService.getNoticeById(id);
            if (notice != null) {
                return R.success(notice);
            }
            return R.error("公告不存在");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
