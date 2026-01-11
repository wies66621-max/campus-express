package com.school.campusexpress.controller;

import com.school.campusexpress.common.R;
import com.school.campusexpress.entity.Notice;
import com.school.campusexpress.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
@Tag(name = "公告查询", description = "公共公告查询接口")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    @Operation(summary = "启用状态的公告列表")
    public R<List<Notice>> listEnabledNotice(
            @Parameter(description = "返回数量限制，默认5条") @RequestParam(defaultValue = "5") Integer limit) {
        try {
            List<Notice> list = noticeService.getEnabledNoticeList(limit);
            return R.success(list);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "公告详情")
    public R<Notice> getNoticeDetail(@PathVariable Long id) {
        try {
            Notice notice = noticeService.getNoticeById(id);
            if (notice != null && notice.getStatus() == 1) {
                return R.success(notice);
            }
            return R.error("公告不存在或已停用");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
