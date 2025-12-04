package com.product_information.pim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityResponse {

    private Integer id;
    private Integer score;
    private String result;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
