package com.school.campusexpress.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notice")
@Schema(description = "系统公告实体类")
public class Notice {

    @TableId(type = IdType.AUTO)
    @Schema(description = "公告ID")
    private Long id;

    @TableField("title")
    @Schema(description = "公告标题")
    private String title;

    @TableField("content")
    @Schema(description = "公告内容")
    private String content;

    @TableField("status")
    @Schema(description = "状态(1-启用,0-停用)")
    private Integer status;

    @TableField("creator_id")
    @Schema(description = "发布人ID")
    private Long creatorId;

    @TableField("is_deleted")
    @TableLogic
    @Schema(description = "逻辑删除标记")
    private Integer isDeleted;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
