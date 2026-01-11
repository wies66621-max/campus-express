package com.school.campusexpress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.campusexpress.entity.Notice;
import com.school.campusexpress.mapper.NoticeMapper;
import com.school.campusexpress.service.NoticeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public Notice addNotice(Notice notice) {
        if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
            throw new RuntimeException("公告标题不能为空");
        }
        if (notice.getContent() == null || notice.getContent().trim().isEmpty()) {
            throw new RuntimeException("公告内容不能为空");
        }
        if (notice.getCreatorId() == null) {
            throw new RuntimeException("发布人ID不能为空");
        }

        notice.setStatus(notice.getStatus() == null ? 1 : notice.getStatus());
        notice.setIsDeleted(0);
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());

        save(notice);
        return notice;
    }

    @Override
    public Notice updateNotice(Notice notice) {
        if (notice.getId() == null) {
            throw new RuntimeException("公告ID不能为空");
        }

        Notice existNotice = getById(notice.getId());
        if (existNotice == null) {
            throw new RuntimeException("公告不存在");
        }

        notice.setUpdateTime(LocalDateTime.now());
        updateById(notice);
        return getById(notice.getId());
    }

    @Override
    public boolean deleteNotice(Long id) {
        if (id == null) {
            throw new RuntimeException("公告ID不能为空");
        }

        Notice notice = getById(id);
        if (notice == null) {
            throw new RuntimeException("公告不存在");
        }

        return removeById(id);
    }

    @Override
    public Page<Notice> getNoticePage(Integer status, Integer pageNum, Integer pageSize) {
        Page<Notice> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();

        if (status != null) {
            wrapper.eq(Notice::getStatus, status);
        }

        wrapper.orderByDesc(Notice::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public List<Notice> getEnabledNoticeList(Integer limit) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, 1);
        wrapper.orderByDesc(Notice::getCreateTime);

        if (limit != null && limit > 0) {
            wrapper.last("LIMIT " + limit);
        }

        return list(wrapper);
    }

    @Override
    public Notice getNoticeById(Long id) {
        if (id == null) {
            throw new RuntimeException("公告ID不能为空");
        }
        return getById(id);
    }
}
