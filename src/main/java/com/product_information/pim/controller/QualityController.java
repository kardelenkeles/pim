package com.product_information.pim.controller;

import com.product_information.pim.dto.response.QualityResponse;
import com.product_information.pim.service.QualityScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quality")
@RequiredArgsConstructor
public class QualityController {

    private final QualityScoreService qualityScoreService;

    @PostMapping("/product/{productId}/run")
    public ResponseEntity<Void> runQualityControl(@PathVariable Integer productId) {
        qualityScoreService.updateQualityScore(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QualityResponse> getQuality(@PathVariable Integer id) {
        QualityResponse response = qualityScoreService.getQualityById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<QualityResponse> getQualityByProductId(@PathVariable Integer productId) {
        QualityResponse response = qualityScoreService.getQualityByProductId(productId);
        return ResponseEntity.ok(response);
    }
}
