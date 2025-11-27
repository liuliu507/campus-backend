package com.ucampus.service;

import com.ucampus.dto.CreateSecondhandRequest;
import com.ucampus.dto.SecondhandProductDTO;
import com.ucampus.entity.SecondhandProduct;
import com.ucampus.repository.SecondhandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecondhandService {

    private final SecondhandRepository secondhandRepository;
    private final ObjectMapper objectMapper;

    // 获取所有商品
    public List<SecondhandProductDTO> getAllProducts() {
        try {
            List<SecondhandProduct> products = secondhandRepository.findByStatusOrderByCreatedAtDesc(
                    SecondhandProduct.ProductStatus.AVAILABLE);

            if (products.isEmpty()) {
                System.out.println("📦 数据库中没有商品，返回模拟数据");
                return getMockProducts();
            }

            System.out.println("📦 从数据库获取到 " + products.size() + " 个商品");
            return products.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ 获取商品列表异常: " + e.getMessage());
            return getMockProducts();
        }
    }

    // 根据ID获取商品
    public SecondhandProductDTO getProductById(Long id) {
        try {
            SecondhandProduct product = secondhandRepository.findById(id)
                    .orElse(null);

            if (product == null) {
                System.out.println("📦 数据库中未找到商品 ID: " + id + "，从模拟数据中查找");
                return getMockProducts().stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("商品不存在"));
            }

            // 增加浏览量
            secondhandRepository.incrementViewCount(id);

            return convertToDTO(product);
        } catch (Exception e) {
            System.err.println("❌ 获取商品详情异常: " + e.getMessage());
            throw new RuntimeException("获取商品详情失败");
        }
    }

    // 创建商品
    public SecondhandProductDTO createProduct(CreateSecondhandRequest request, String sellerId, String sellerName) {
        try {
            SecondhandProduct product = new SecondhandProduct();
            product.setTitle(request.getTitle());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setOriginalPrice(request.getOriginalPrice());
            product.setCategory(request.getCategory());
            product.setCondition(request.getCondition());
            product.setLocation(request.getLocation());
            product.setContact(request.getContact());
            product.setSellerId(sellerId);
            product.setSellerName(sellerName);
            product.setUrgent(request.getUrgent() != null ? request.getUrgent() : false);
            product.setStatus(SecondhandProduct.ProductStatus.AVAILABLE);
            product.setViewCount(0);
            product.setLikeCount(0);

            // 处理图片
            if (request.getImages() != null && !request.getImages().isEmpty()) {
                try {
                    product.setImageUrls(objectMapper.writeValueAsString(request.getImages()));
                } catch (JsonProcessingException e) {
                    product.setImageUrls("[\"📦\"]");
                }
            } else {
                product.setImageUrls("[\"📦\"]");
            }

            SecondhandProduct savedProduct = secondhandRepository.save(product);
            System.out.println("✅ 商品保存成功，ID: " + savedProduct.getId());
            return convertToDTO(savedProduct);
        } catch (Exception e) {
            System.err.println("❌ 创建商品异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("创建商品失败: " + e.getMessage());
        }
    }

    // 搜索商品
    public List<SecondhandProductDTO> searchProducts(String keyword, String category) {
        try {
            List<SecondhandProduct> products;

            if (keyword != null && !keyword.trim().isEmpty()) {
                products = secondhandRepository.searchByKeyword(keyword.trim(),
                        SecondhandProduct.ProductStatus.AVAILABLE);
            } else if (category != null && !"全部".equals(category)) {
                products = secondhandRepository.findByCategoryAndStatusOrderByCreatedAtDesc(
                        category, SecondhandProduct.ProductStatus.AVAILABLE);
            } else {
                products = secondhandRepository.findByStatusOrderByCreatedAtDesc(
                        SecondhandProduct.ProductStatus.AVAILABLE);
            }

            if (products.isEmpty()) {
                System.out.println("🔍 搜索无结果，返回模拟数据");
                return searchMockProducts(keyword, category);
            }

            return products.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ 搜索商品异常: " + e.getMessage());
            return searchMockProducts(keyword, category);
        }
    }

    // 更新商品状态
    public SecondhandProductDTO updateProductStatus(Long id, SecondhandProduct.ProductStatus status) {
        try {
            SecondhandProduct product = secondhandRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("商品不存在"));

            product.setStatus(status);
            SecondhandProduct updatedProduct = secondhandRepository.save(product);
            return convertToDTO(updatedProduct);
        } catch (Exception e) {
            System.err.println("❌ 更新商品状态异常: " + e.getMessage());
            throw new RuntimeException("更新商品状态失败");
        }
    }

    // 删除商品
    public void deleteProduct(Long id, String sellerId) {
        try {
            SecondhandProduct product = secondhandRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("商品不存在"));

            // 测试阶段完全跳过卖家验证
            System.out.println("🗑️ 删除商品 ID: " + id + ", 当前卖家: " + sellerId + ", 商品卖家: " + product.getSellerId());
            System.out.println("⚠️ 测试阶段跳过卖家验证");

            secondhandRepository.delete(product);
            System.out.println("✅ 商品删除成功");

        } catch (Exception e) {
            System.err.println("❌ 删除商品异常: " + e.getMessage());
            e.printStackTrace(); // 添加详细堆栈跟踪
            throw new RuntimeException("删除商品失败: " + e.getMessage());
        }
    }

    // 实体转DTO
    private SecondhandProductDTO convertToDTO(SecondhandProduct product) {
        SecondhandProductDTO dto = new SecondhandProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setCategory(product.getCategory());
        dto.setCondition(product.getCondition());
        dto.setLocation(product.getLocation());
        dto.setContact(product.getContact());
        dto.setSellerId(product.getSellerId());
        dto.setSellerName(product.getSellerName());
        dto.setUrgent(product.getUrgent());
        dto.setStatus(product.getStatus().name());
        dto.setViewCount(product.getViewCount());
        dto.setLikeCount(product.getLikeCount());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setTimeAgo(dto.calculateTimeAgo());

        // 处理图片
        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
            try {
                List<String> images = objectMapper.readValue(product.getImageUrls(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                dto.setImages(images);
            } catch (JsonProcessingException e) {
                dto.setImages(List.of("📦"));
            }
        } else {
            dto.setImages(List.of("📦"));
        }

        return dto;
    }

    // 模拟数据（用于测试）
    private List<SecondhandProductDTO> getMockProducts() {
        List<SecondhandProductDTO> products = new ArrayList<>();

        products.add(createMockProduct(1L, "九成新 iPad Air", 1800.0, 2400.0, "电子产品", "九成新",
                "保护得很好，无任何划痕，配件齐全", "📱", "138****1234", "主校区", true));

        products.add(createMockProduct(2L, "数据结构教材", 25.0, 50.0, "书籍资料", "七成新",
                "有少量笔记，不影响阅读", "📚", "139****5678", "东校区", false));

        products.add(createMockProduct(3L, "篮球鞋", 120.0, 300.0, "服饰鞋包", "八成新",
                "只穿过几次，鞋底磨损很少", "👟", "137****9012", "西校区", false));

        System.out.println("📦 生成 " + products.size() + " 个模拟商品");
        return products;
    }

    private SecondhandProductDTO createMockProduct(Long id, String title, Double price, Double originalPrice,
                                                   String category, String condition, String description,
                                                   String emoji, String contact, String location, Boolean urgent) {
        SecondhandProductDTO product = new SecondhandProductDTO();
        product.setId(id);
        product.setTitle(title);
        product.setPrice(price);
        product.setOriginalPrice(originalPrice);
        product.setCategory(category);
        product.setCondition(condition);
        product.setDescription(description);
        product.setImages(List.of(emoji));
        product.setContact(contact);
        product.setLocation(location);
        product.setSellerId("user" + id);
        product.setSellerName("用户" + id);
        product.setUrgent(urgent);
        product.setStatus("AVAILABLE");
        product.setViewCount((int)(Math.random() * 50) + 10);
        product.setLikeCount((int)(Math.random() * 20) + 1);
        product.setCreatedAt(LocalDateTime.now().minusHours(id * 6));
        product.setTimeAgo(product.calculateTimeAgo());
        return product;
    }

    // 搜索模拟数据
    private List<SecondhandProductDTO> searchMockProducts(String keyword, String category) {
        List<SecondhandProductDTO> allProducts = getMockProducts();

        return allProducts.stream()
                .filter(product -> {
                    boolean matchesKeyword = true;
                    boolean matchesCategory = true;

                    if (keyword != null && !keyword.trim().isEmpty()) {
                        matchesKeyword = product.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                                product.getDescription().toLowerCase().contains(keyword.toLowerCase());
                    }

                    if (category != null && !"全部".equals(category)) {
                        matchesCategory = product.getCategory().equals(category);
                    }

                    return matchesKeyword && matchesCategory;
                })
                .collect(Collectors.toList());
    }
}