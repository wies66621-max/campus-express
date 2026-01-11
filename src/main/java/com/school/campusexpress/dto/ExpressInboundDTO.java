package com.school.campusexpress.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "快递入库DTO")
public class ExpressInboundDTO {

    @NotBlank(message = "快递单号不能为空")
    @Schema(description = "快递单号（扫码/输入）")
    private String trackingNumber;

    @NotBlank(message = "收件人手机号不能为空")
    @Schema(description = "收件人手机号（用于自动匹配用户）")
    private String receiverPhone;

    @Schema(description = "快递公司（可选）")
    private String company;
}
