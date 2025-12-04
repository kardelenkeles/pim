package com.product_information.pim.service;

import com.product_information.pim.dto.request.BrandCreateRequest;
import com.product_information.pim.dto.request.BrandUpdateRequest;
import com.product_information.pim.dto.response.BrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {

    BrandResponse create(BrandCreateRequest request);

    BrandResponse update(Integer id, BrandUpdateRequest request);

    BrandResponse getById(Integer id);

    BrandResponse getBySlug(String slug);

    List<BrandResponse> getAll();

    Page<BrandResponse> getAll(Pageable pageable);

    void delete(Integer id);
}
