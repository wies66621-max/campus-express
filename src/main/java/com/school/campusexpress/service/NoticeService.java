package com.school.campusexpress.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school.campusexpress.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {

    Notice addNotice(Notice notice);

    Notice updateNotice(Notice notice);

    boolean deleteNotice(Long id);

    Page<Notice> getNoticePage(Integer status, Integer pageNum, Integer pageSize);

    List<Notice> getEnabledNoticeList(Integer limit);

    Notice getNoticeById(Long id);
}
