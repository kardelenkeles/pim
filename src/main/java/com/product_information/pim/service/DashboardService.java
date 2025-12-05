package com.product_information.pim.service;

import com.product_information.pim.dto.response.DashboardStatsResponse;

public interface DashboardService {
    DashboardStatsResponse getDashboardStats();

    String exportProductsToCsv();

    String exportProductsToJson();
}
