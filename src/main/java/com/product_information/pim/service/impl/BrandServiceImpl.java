package com.product_information.pim.service.impl;

import com.product_information.pim.dto.request.BrandCreateRequest;
import com.product_information.pim.dto.request.BrandUpdateRequest;
import com.product_information.pim.dto.response.BrandResponse;
import com.product_information.pim.entity.Brand;
import com.product_information.pim.exception.DuplicateResourceException;
import com.product_information.pim.exception.ResourceNotFoundException;
import com.product_information.pim.mapper.BrandMapper;
import com.product_information.pim.repository.BrandRepository;
import com.product_information.pim.repository.ProductRepository;
import com.product_information.pim.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final BrandMapper brandMapper;

    @Override
    public BrandResponse create(BrandCreateRequest request) {
        log.info("Creating brand: {}", request.getName());

        if (brandRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Brand", "name", request.getName());
        }

        Brand brand = brandMapper.toEntity(request);
        brand = brandRepository.save(brand);

        log.info("Brand created successfully with id: {}", brand.getId());
        return brandMapper.toResponse(brand, 0L);
    }

    @Override
    public BrandResponse update(Integer id, BrandUpdateRequest request) {
        log.info("Updating brand with id: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        if (request.getName() != null && !request.getName().equals(brand.getName())) {
            if (brandRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException("Brand", "name", request.getName());
            }
        }

        brandMapper.updateEntity(brand, request);
        brand = brandRepository.save(brand);

        Long productCount = productRepository.countByBrandId(id);

        log.info("Brand updated successfully with id: {}", id);
        return brandMapper.toResponse(brand, productCount);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getById(Integer id) {
        log.info("Fetching brand with id: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        Long productCount = productRepository.countByBrandId(id);
        return brandMapper.toResponse(brand, productCount);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getBySlug(String slug) {
        log.info("Fetching brand with slug: {}", slug);

        Brand brand = brandRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "slug", slug));

        Long productCount = productRepository.countByBrandId(brand.getId());
        return brandMapper.toResponse(brand, productCount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAll() {
        log.info("Fetching all brands");

        return brandRepository.findAll().stream()
                .map(brand -> {
                    Long productCount = productRepository.countByBrandId(brand.getId());
                    return brandMapper.toResponse(brand, productCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandResponse> getAll(Pageable pageable) {
        log.info("Fetching all brands with pagination");

        return brandRepository.findAll(pageable)
                .map(brand -> {
                    Long productCount = productRepository.countByBrandId(brand.getId());
                    return brandMapper.toResponse(brand, productCount);
                });
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting brand with id: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        brandRepository.delete(brand);

        log.info("Brand deleted successfully with id: {}", id);
    }
}
