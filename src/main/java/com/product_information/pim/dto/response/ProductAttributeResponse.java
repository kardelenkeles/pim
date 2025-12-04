package com.product_information.pim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeResponse {

    private Integer id;
    private Integer productId;
    private String key;
    private String value;
}
