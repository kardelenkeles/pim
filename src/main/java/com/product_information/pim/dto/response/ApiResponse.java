package com.product_information.pim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private List<T> data;
    private Long total;

    public ApiResponse(List<T> data) {
        this.data = data;
        this.total = (long) data.size();
    }
}
