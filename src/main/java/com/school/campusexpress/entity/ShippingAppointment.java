package com.school.campusexpress.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("shipping_appointment")
@Schema(description = "寄件预约实体类")
public class ShippingAppointment {

    @TableId(type = IdType.AUTO)
    @Schema(description = "预约ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("user_id")
    @Schema(description = "用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @TableField(exist = false)
    @Schema(description = "用户姓名")
    private String userName;

    @TableField(exist = false)
    @Schema(description = "用户手机号")
    private String userPhone;

    @NotBlank(message = "寄件人姓名不能为空")
    @TableField("sender_name")
    @Schema(description = "寄件人姓名")
    private String senderName;

    @NotBlank(message = "寄件人手机号不能为空")
    @TableField("sender_phone")
    @Schema(description = "寄件人手机号")
    private String senderPhone;

    @TableField("sender_address")
    @Schema(description = "寄件人地址")
    private String senderAddress;

    @NotBlank(message = "收件人姓名不能为空")
    @TableField("receiver_name")
    @Schema(description = "收件人姓名")
    private String receiverName;

    @NotBlank(message = "收件人手机号不能为空")
    @TableField("receiver_phone")
    @Schema(description = "收件人手机号")
    private String receiverPhone;

    @TableField("receiver_address")
    @Schema(description = "收件人地址")
    private String receiverAddress;

    @TableField("company")
    @Schema(description = "快递公司")
    private String company;

    @TableField("package_type")
    @Schema(description = "包裹类型(0=普通,1=易碎,2=贵重)")
    private Integer packageType;

    @TableField("weight")
    @Schema(description = "重量(kg)")
    private Double weight;

    @TableField("remark")
    @Schema(description = "备注")
    private String remark;

    @TableField("status")
    @Schema(description = "状态(0=待审核,1=已通过,2=已拒绝,3=已取消)")
    private Integer status;

    @TableField("station_id")
    @Schema(description = "快递站ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long stationId;

    @TableField(exist = false)
    @Schema(description = "快递站名称")
    private String stationName;

    @TableField("audit_time")
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @TableField("auditor_id")
    @Schema(description = "审核人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long auditorId;

    @TableField(exist = false)
    @Schema(description = "审核人姓名")
    private String auditorName;

    @TableField("audit_remark")
    @Schema(description = "审核备注")
    private String auditRemark;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
