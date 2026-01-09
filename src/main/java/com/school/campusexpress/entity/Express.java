package com.school.campusexpress.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("express")
@Schema(description = "快递实体类")
public class Express {

    @TableId(type = IdType.AUTO)
    @Schema(description = "快递ID")
    private Long id;

    @NotBlank(message = "快递单号不能为空")
    @TableField("tracking_number")
    @Schema(description = "快递单号")
    private String trackingNumber;

    @TableField("company")
    @Schema(description = "快递公司")
    private String company;

    @NotBlank(message = "收件人姓名不能为空")
    @TableField("receiver_name")
    @Schema(description = "收件人姓名")
    private String receiverName;

    @NotBlank(message = "收件人电话不能为空")
    @TableField("receiver_phone")
    @Schema(description = "收件人电话")
    private String receiverPhone;

    @Schema(description = "取件码")
    private String pickupCode;

    @TableField("station_id")
    @Schema(description = "快递站ID")
    private Long stationId;

    @Schema(description = "状态(0=待取件,1=已取件)")
    private Integer status;

    @TableField("remark")
    @Schema(description = "备注")
    private String remark;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}