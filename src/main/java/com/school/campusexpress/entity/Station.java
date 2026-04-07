package com.school.campusexpress.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("station")
@Schema(description = "快递站实体类")
public class Station {

    @TableId(type = IdType.AUTO)
    @Schema(description = "快递站ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("station_name")
    @Schema(description = "快递站名称")
    private String stationName;

    @TableField("location")
    @Schema(description = "位置")
    private String location;

    @TableField("contact_phone")
    @Schema(description = "联系电话")
    private String contactPhone;

    @TableField("status")
    @Schema(description = "状态(0=停用,1=启用)")
    private Integer status;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
