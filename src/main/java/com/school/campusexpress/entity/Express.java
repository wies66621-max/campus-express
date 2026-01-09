package com.school.campusexpress.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("express")
@Schema(description = "快递实体类")
public class Express {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "快递ID")
    private Long id;

    @NotBlank(message = "快递单号不能为空")
    @Schema(description = "快递单号")
    private String expressNo;

    @NotBlank(message = "收件人姓名不能为空")
    @Schema(description = "收件人姓名")
    private String recipientName;

    @NotBlank(message = "收件人电话不能为空")
    @Schema(description = "收件人电话")
    private String recipientPhone;

    @Schema(description = "快递公司")
    private String expressCompany;

    @Schema(description = "快递状态：0-待入库，1-已入库，2-已取件，3-已取消")
    private Integer status;

    @Schema(description = "取件码")
    private String pickupCode;

    @Schema(description = "入库时间")
    private LocalDateTime storageTime;

    @Schema(description = "取件时间")
    private LocalDateTime pickupTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}