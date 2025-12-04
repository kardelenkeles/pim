package com.product_information.pim.service.impl;

import com.product_information.pim.dto.request.ProductAttributeRequest;
import com.product_information.pim.dto.request.ProductAttributeUpdateRequest;
import com.product_information.pim.dto.response.ProductAttributeResponse;
import com.product_information.pim.entity.ProductAttribute;
import com.product_information.pim.exception.BusinessException;
import com.product_information.pim.exception.DuplicateResourceException;
import com.product_information.pim.exception.ResourceNotFoundException;
import com.product_information.pim.mapper.ProductAttributeMapper;
import com.product_information.pim.repository.ProductAttributeRepository;
import com.product_information.pim.repository.ProductRepository;
import com.product_information.pim.service.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final ProductRepository productRepository;
    private final ProductAttributeMapper productAttributeMapper;

    @Override
    public ProductAttributeResponse create(ProductAttributeRequest request) {
        log.info("Creating product attribute for product ID: {} with key: {}",
                request.getProductId(), request.getKey());

        // Validate product exists
        productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        // Validate key is not empty or whitespace only
        if (request.getKey().trim().isEmpty()) {
            throw new BusinessException("Attribute key cannot be empty or whitespace only");
        }

        // Check for duplicate key for the same product
        if (productAttributeRepository.findByProductIdAndKey(request.getProductId(), request.getKey()).isPresent()) {
            throw new DuplicateResourceException("ProductAttribute",
                    "key for product " + request.getProductId(), request.getKey());
        }

        ProductAttribute attribute = productAttributeMapper.toEntity(request);
        ProductAttribute savedAttribute = productAttributeRepository.save(attribute);

        log.info("Product attribute created successfully with id: {}", savedAttribute.getId());
        return productAttributeMapper.toResponse(savedAttribute);
    }

    @Override
    public ProductAttributeResponse update(Integer id, ProductAttributeUpdateRequest request) {
        log.info("Updating product attribute with id: {}", id);

        ProductAttribute attribute = productAttributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductAttribute", "id", id));

        // Validate key if provided
        if (request.getKey() != null) {
            if (request.getKey().trim().isEmpty()) {
                throw new BusinessException("Attribute key cannot be empty or whitespace only");
            }

            // Check for duplicate key for the same product (excluding current attribute)
            productAttributeRepository.findByProductIdAndKey(attribute.getProductId(), request.getKey())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new DuplicateResourceException("ProductAttribute",
                                    "key for product " + attribute.getProductId(), request.getKey());
                        }
                    });
        }

        // Validate value if provided
        if (request.getValue() != null && request.getValue().trim().isEmpty()) {
            throw new BusinessException("Attribute value cannot be empty or whitespace only");
        }

        productAttributeMapper.updateEntity(attribute, request);
        ProductAttribute updatedAttribute = productAttributeRepository.save(attribute);

        log.info("Product attribute updated successfully with id: {}", id);
        return productAttributeMapper.toResponse(updatedAttribute);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductAttributeResponse getById(Integer id) {
        log.info("Fetching product attribute with id: {}", id);

        ProductAttribute attribute = productAttributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductAttribute", "id", id));

        return productAttributeMapper.toResponse(attribute);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductAttributeResponse> getByProductId(Integer productId) {
        log.info("Fetching product attributes for product id: {}", productId);

        // Validate product exists
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        return productAttributeRepository.findByProductId(productId).stream()
                .map(productAttributeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductAttributeResponse> getByKey(String key) {
        log.info("Fetching product attributes with key: {}", key);

        if (key == null || key.trim().isEmpty()) {
            throw new BusinessException("Attribute key cannot be null or empty");
        }

        return productAttributeRepository.findByKey(key).stream()
                .map(productAttributeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting product attribute with id: {}", id);

        ProductAttribute attribute = productAttributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductAttribute", "id", id));

        productAttributeRepository.delete(attribute);

        log.info("Product attribute deleted successfully with id: {}", id);
    }

    @Override
    public void deleteByProductId(Integer productId) {
        log.info("Deleting all product attributes for product id: {}", productId);

        // Validate product exists
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        productAttributeRepository.deleteByProductId(productId);

        log.info("All product attributes deleted successfully for product id: {}", productId);
    }
}
