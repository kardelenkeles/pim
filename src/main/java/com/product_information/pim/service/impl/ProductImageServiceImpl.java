package com.product_information.pim.service.impl;

import com.product_information.pim.dto.request.ProductImageCreateRequest;
import com.product_information.pim.dto.request.ProductImageUpdateRequest;
import com.product_information.pim.dto.response.ProductImageResponse;
import com.product_information.pim.entity.ProductImage;
import com.product_information.pim.exception.ResourceNotFoundException;
import com.product_information.pim.mapper.ProductImageMapper;
import com.product_information.pim.repository.ProductImageRepository;
import com.product_information.pim.repository.ProductRepository;
import com.product_information.pim.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;

    @Override
    @Transactional
    public ProductImageResponse addImage(ProductImageCreateRequest request) {
        log.info("Adding image to product: {}", request.getProductId());

        productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        ProductImage image = productImageMapper.toEntity(request.getProductId(), request);
        ProductImage savedImage = productImageRepository.save(image);

        log.info("Image added successfully with id: {}", savedImage.getId());
        return productImageMapper.toResponse(savedImage);
    }

    @Override
    @Transactional
    public ProductImageResponse updateImage(Integer id, ProductImageUpdateRequest request) {
        log.info("Updating image with id: {}", id);

        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));

        if (request.getImageUrl() != null) {
            image.setImageUrl(request.getImageUrl());
        }
        if (request.getAltText() != null) {
            image.setAltText(request.getAltText());
        }
        if (request.getOrder() != null) {
            image.setOrder(request.getOrder());
        }

        ProductImage updatedImage = productImageRepository.save(image);

        log.info("Image updated successfully");
        return productImageMapper.toResponse(updatedImage);
    }

    @Override
    public ProductImageResponse getById(Integer id) {
        log.info("Getting image by id: {}", id);

        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));

        return productImageMapper.toResponse(image);
    }

    @Override
    public List<ProductImageResponse> getByProductId(Integer productId) {
        log.info("Getting images for product: {}", productId);

        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        List<ProductImage> images = productImageRepository.findByProductIdOrderByOrderAsc(productId);

        return images.stream()
                .map(productImageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void reorderImages(Integer productId, Map<Integer, Integer> imageOrders) {
        log.info("Reordering images for product: {}", productId);

        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        imageOrders.forEach((imageId, order) -> {
            ProductImage image = productImageRepository.findById(imageId)
                    .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", imageId));

            if (!image.getProductId().equals(productId)) {
                throw new IllegalArgumentException("Image " + imageId + " does not belong to product " + productId);
            }

            image.setOrder(order);
            productImageRepository.save(image);
        });

        log.info("Images reordered successfully");
    }

    @Override
    @Transactional
    public void deleteImage(Integer id) {
        log.info("Deleting image with id: {}", id);

        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));

        productImageRepository.delete(image);

        log.info("Image deleted successfully");
    }
}
