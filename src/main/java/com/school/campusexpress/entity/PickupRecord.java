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
@TableName("pickup_record")
@Schema(description = "取件记录实体类")
public class PickupRecord {

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("express_id")
    @Schema(description = "快递ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long expressId;

    @TableField("operator_id")
    @Schema(description = "操作员ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long operatorId;

    @TableField("pickup_time")
    @Schema(description = "取件时间")
    private LocalDateTime pickupTime;

    @TableField("status")
    @Schema(description = "状态")
    private Integer status;

    @TableField("remark")
    @Schema(description = "备注")
    private String remark;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
