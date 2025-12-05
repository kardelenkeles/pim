package com.product_information.pim.service.impl;

import com.product_information.pim.dto.request.ProductCreateRequest;
import com.product_information.pim.dto.request.ProductUpdateRequest;
import com.product_information.pim.dto.response.ProductResponse;
import com.product_information.pim.entity.*;
import com.product_information.pim.enums.ProductStatus;
import com.product_information.pim.exception.DuplicateResourceException;
import com.product_information.pim.exception.ResourceNotFoundException;
import com.product_information.pim.mapper.ProductImageMapper;
import com.product_information.pim.mapper.ProductMapper;
import com.product_information.pim.repository.*;
import com.product_information.pim.service.ProductService;
import com.product_information.pim.service.QualityScoreService;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductImageRepository productImageRepository;
    private final QualityRepository qualityRepository;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final QualityScoreService qualityScoreService;

    @Override
    public ProductResponse create(ProductCreateRequest request) {
        log.info("Creating product with barcode: {}", request.getBarcode());

        if (productRepository.existsByBarcode(request.getBarcode())) {
            throw new DuplicateResourceException("Product", "barcode", request.getBarcode());
        }

        if (request.getBrandId() != null) {
            brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.getBrandId()));
        }

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        }

        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);

        if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
            List<ProductAttribute> attributes = productMapper.toAttributeEntities(
                    savedProduct.getId(),
                    request.getAttributes());
            productAttributeRepository.saveAll(attributes);
        }

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ProductImage> images = request.getImages().stream()
                    .map(img -> productImageMapper.toEntity(savedProduct.getId(), img))
                    .collect(Collectors.toList());
            productImageRepository.saveAll(images);
        }

        // Calculate and save quality score
        qualityScoreService.updateQualityScore(savedProduct.getId());

        log.info("Product created successfully with id: {}", savedProduct.getId());
        return getFullProductResponse(savedProduct.getId());
    }

    @Override
    public ProductResponse update(Integer id, ProductUpdateRequest request) {
        log.info("Updating product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (request.getBarcode() != null && !request.getBarcode().equals(product.getBarcode())) {
            if (productRepository.existsByBarcode(request.getBarcode())) {
                throw new DuplicateResourceException("Product", "barcode", request.getBarcode());
            }
        }

        if (request.getBrandId() != null) {
            brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", request.getBrandId()));
        }

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        }

        productMapper.updateEntity(product, request);
        product = productRepository.save(product);

        if (request.getAttributes() != null) {
            productAttributeRepository.deleteByProductId(id);
            if (!request.getAttributes().isEmpty()) {
                List<ProductAttribute> attributes = productMapper.toAttributeEntities(
                        id,
                        request.getAttributes());
                productAttributeRepository.saveAll(attributes);
            }
        }

        if (request.getImages() != null) {
            productImageRepository.deleteByProductId(id);
            if (!request.getImages().isEmpty()) {
                List<ProductImage> images = request.getImages().stream()
                        .map(img -> productImageMapper.toEntity(id, img))
                        .collect(Collectors.toList());
                productImageRepository.saveAll(images);
            }
        }

        // Recalculate quality score after update
        qualityScoreService.updateQualityScore(id);

        log.info("Product updated successfully with id: {}", id);
        return getFullProductResponse(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Integer id) {
        log.info("Fetching product with id: {}", id);
        return getFullProductResponse(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getByBarcode(String barcode) {
        log.info("Fetching product with barcode: {}", barcode);

        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "barcode", barcode));

        return getFullProductResponse(product.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        log.info("Fetching all products");

        return productRepository.findAll().stream()
                .map(product -> getFullProductResponse(product.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAll(Pageable pageable) {
        log.info("Fetching all products with pagination");

        return productRepository.findAll(pageable)
                .map(product -> getFullProductResponse(product.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchWithFilters(
            String keyword,
            ProductStatus status,
            Integer categoryId,
            Integer brandId,
            Pageable pageable) {
        log.info("Searching products with filters - keyword: {}, status: {}, categoryId: {}, brandId: {}",
                keyword, status, categoryId, brandId);

        return productRepository.searchProductsWithFilters(keyword, status, categoryId, brandId, pageable)
                .map(product -> getFullProductResponse(product.getId()));
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(product);

        log.info("Product deleted successfully with id: {}", id);
    }

    @Override
    public void updateStatus(Integer id, ProductStatus status) {
        log.info("Updating product status for id: {} to {}", id, status);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setStatus(status);
        productRepository.save(product);

        log.info("Product status updated successfully");
    }

    private ProductResponse getFullProductResponse(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Category category = categoryRepository.findById(product.getCategoryId()).orElse(null);
        Brand brand = brandRepository.findById(product.getBrandId()).orElse(null);
        List<ProductAttribute> attributes = productAttributeRepository.findByProductId(productId);
        List<ProductImage> images = productImageRepository.findByProductIdOrderByOrderAsc(productId);
        Quality quality = qualityRepository.findByProductId(productId).orElse(null);

        return productMapper.toFullResponse(product, category, brand, attributes, images, quality);
    }
}
