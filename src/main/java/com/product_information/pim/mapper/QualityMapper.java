package com.product_information.pim.mapper;

import com.product_information.pim.dto.response.QualityResponse;
import com.product_information.pim.entity.Quality;
import org.springframework.stereotype.Component;

@Component
public class QualityMapper {

    public QualityResponse toResponse(Quality quality) {
        return QualityResponse.builder()
                .id(quality.getId())
                .score(quality.getScore())
                .result(quality.getResult())
                .createdAt(quality.getCreatedAt())
                .updatedAt(quality.getUpdatedAt())
                .build();
    }
}
