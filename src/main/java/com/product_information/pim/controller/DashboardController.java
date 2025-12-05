package com.product_information.pim.controller;

import com.product_information.pim.dto.response.DashboardStatsResponse;
import com.product_information.pim.dto.response.ProductResponse;
import com.product_information.pim.service.DashboardService;
import com.product_information.pim.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ProductService productService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        DashboardStatsResponse response = dashboardService.getDashboardStats();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{id}/full-detail")
    public ResponseEntity<ProductResponse> getFullProductDetail(@PathVariable Integer id) {
        ProductResponse response = productService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/export")
    public ResponseEntity<String> exportProducts(
            @RequestParam(defaultValue = "json") String format) {
        String content;
        MediaType mediaType;
        String filename;

        if ("csv".equalsIgnoreCase(format)) {
            content = dashboardService.exportProductsToCsv();
            mediaType = MediaType.parseMediaType("text/csv");
            filename = "products.csv";
        } else {
            content = dashboardService.exportProductsToJson();
            mediaType = MediaType.APPLICATION_JSON;
            filename = "products.json";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(mediaType)
                .body(content);
    }
}
