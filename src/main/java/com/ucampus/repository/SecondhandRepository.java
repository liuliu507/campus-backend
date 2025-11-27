package com.ucampus.repository;

import com.ucampus.entity.SecondhandProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondhandRepository extends JpaRepository<SecondhandProduct, Long>, JpaSpecificationExecutor<SecondhandProduct> {

    // 根据分类和状态查找
    List<SecondhandProduct> findByCategoryAndStatusOrderByCreatedAtDesc(String category, SecondhandProduct.ProductStatus status);

    // 根据卖家ID查找
    List<SecondhandProduct> findBySellerIdOrderByCreatedAtDesc(String sellerId);

    // 搜索商品
    @Query("SELECT p FROM SecondhandProduct p WHERE " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "p.status = :status " +
            "ORDER BY p.urgent DESC, p.createdAt DESC")
    List<SecondhandProduct> searchByKeyword(@Param("keyword") String keyword,
                                            @Param("status") SecondhandProduct.ProductStatus status);

    // 查找急出商品
    List<SecondhandProduct> findByUrgentAndStatusOrderByCreatedAtDesc(Boolean urgent, SecondhandProduct.ProductStatus status);

    // 增加浏览量
    @Modifying
    @Query("UPDATE SecondhandProduct p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") Long id);

    // 根据状态查找商品
    List<SecondhandProduct> findByStatusOrderByCreatedAtDesc(SecondhandProduct.ProductStatus status);
}