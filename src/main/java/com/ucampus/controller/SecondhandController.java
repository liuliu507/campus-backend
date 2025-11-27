package com.ucampus.controller;

import com.ucampus.dto.CreateSecondhandRequest;
import com.ucampus.dto.SecondhandProductDTO;
import com.ucampus.entity.SecondhandProduct;
import com.ucampus.service.SecondhandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/secondhand")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SecondhandController {

    private final SecondhandService secondhandService;

    // ==================== 测试接口 ====================
    @GetMapping("/test")
    public ResponseEntity<String> testConnection() {
        System.out.println("✅ 二手交易测试端点被调用！");
        return ResponseEntity.ok("二手交易后端连接正常 - " + System.currentTimeMillis());
    }

    // ==================== 获取全部商品 ====================
    @GetMapping
    public ResponseEntity<List<SecondhandProductDTO>> getAllProducts() {
        System.out.println("🔄 接收到获取商品列表请求");
        try {
            List<SecondhandProductDTO> products = secondhandService.getAllProducts();
            System.out.println("✅ 返回商品数量: " + products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("❌ 获取商品列表失败");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    // ==================== 获取商品详情 ====================
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        System.out.println("🔄 获取商品详情 ID = " + id);
        try {
            return ResponseEntity.ok(secondhandService.getProductById(id));
        } catch (Exception e) {
            System.err.println("❌ 获取商品详情失败");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(error("获取商品详情失败"));
        }
    }

    // ==================== 创建商品 ====================
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateSecondhandRequest request) {
        System.out.println("🔄 接收到创建商品请求：" + request);

        try {
            // 测试版写死
            String sellerId = "user_" + System.currentTimeMillis();
            String sellerName = "匿名用户";

            SecondhandProductDTO product = secondhandService.createProduct(request, sellerId, sellerName);

            System.out.println("✅ 商品创建成功，ID = " + product.getId());

            return ResponseEntity.ok(product);

        } catch (Exception e) {
            System.err.println("❌ 商品创建失败");
            e.printStackTrace();   // 会打印详细错误

            return ResponseEntity
                    .badRequest()
                    .body(error("商品创建失败：" + e.getMessage()));
        }
    }

    // ==================== 搜索 ====================
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {

        System.out.println("🔄 搜索 - keyword=" + keyword + ", category=" + category);

        try {
            return ResponseEntity.ok(secondhandService.searchProducts(keyword, category));
        } catch (Exception e) {
            System.err.println("❌ 搜索失败");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    // ==================== 更新商品状态 ====================
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateProductStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        System.out.println("🔄 更新状态 ID=" + id + ", status=" + status);

        try {
            SecondhandProduct.ProductStatus newStatus =
                    SecondhandProduct.ProductStatus.valueOf(status.toUpperCase());

            return ResponseEntity.ok(secondhandService.updateProductStatus(id, newStatus));
        } catch (Exception e) {
            System.err.println("❌ 状态更新失败");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(error("状态更新失败"));
        }
    }

    // ==================== 删除商品 ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        System.out.println("🔄 删除商品 ID=" + id);

        try {
            // 测试阶段使用固定卖家ID，后续需要从登录信息中获取
            String currentSellerId = "user_test";
            System.out.println("🔍 当前卖家ID: " + currentSellerId);

            secondhandService.deleteProduct(id, currentSellerId);

            System.out.println("✅ 删除成功");
            return ResponseEntity.ok(success("删除成功"));
        } catch (Exception e) {
            System.err.println("❌ 删除失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(error("删除失败：" + e.getMessage()));
        }
    }

    // ==================== 检查商品是否存在 ====================
    @GetMapping("/{id}/exists")
    public ResponseEntity<?> checkProductExists(@PathVariable Long id) {
        System.out.println("🔍 检查商品是否存在 ID=" + id);

        try {
            // 通过服务层来检查商品是否存在，而不是直接使用repository
            // 这里我们直接调用getProductById，如果商品不存在会抛出异常
            SecondhandProductDTO product = secondhandService.getProductById(id);
            System.out.println("🔍 商品存在: " + (product != null));
            return ResponseEntity.ok(Map.of("exists", product != null));
        } catch (Exception e) {
            System.out.println("🔍 商品不存在: " + e.getMessage());
            return ResponseEntity.ok(Map.of("exists", false));
        }
    }

    // ==================== 分类 ====================
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        System.out.println("🔄 获取分类列表");
        List<String> categories = List.of(
                "电子产品", "书籍资料", "服饰鞋包", "生活用品", "运动器材", "其他"
        );
        return ResponseEntity.ok(categories);
    }

    // ====== 工具：统一的错误格式 ======
    private Map<String, Object> error(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("message", msg);
        return map;
    }

    private Map<String, Object> success(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("message", msg);
        return map;
    }
}