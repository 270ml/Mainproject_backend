package com.kdt03.fashion_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kdt03.fashion_api.domain.InternalProducts;
import com.kdt03.fashion_api.domain.dto.ProductDTO;
import com.kdt03.fashion_api.domain.dto.StyleCountDTO;

public interface ProductRepository extends JpaRepository<InternalProducts, String> {
        @Query("select new com.kdt03.fashion_api.domain.dto.ProductDTO(p.productId, p.productName, p.price, c.categoryName, p.imageUrl) "
                        + "from InternalProducts p "
                        + "left join p.category c "
                        + "left join SalesLog s on s.productId = p.productId "
                        + "and s.saleDate >= :oneYearAgo "
                        + "where (:categoryName is null or c.categoryName = :categoryName) "
                        + "group by p.productId, p.productName, p.price, c.categoryName, p.imageUrl "
                        + "order by coalesce(sum(s.saleQuantity), 0) desc")
        List<ProductDTO> findAllProducts(@Param("categoryName") String categoryName,
                        @Param("oneYearAgo") java.time.LocalDate oneYearAgo);

        @Query("select new com.kdt03.fashion_api.domain.dto.ProductDTO(p.productId, p.productName, p.price, c.categoryName, p.imageUrl)"
                        + " from InternalProducts p left join p.category c where p.productId = :productId")
        Optional<ProductDTO> findProductById(@Param("productId") String productId);

        @Query("select new com.kdt03.fashion_api.domain.dto.ProductMapDTO(p.productId, p.productName, s.styleName, v.xCoord, v.yCoord) "
                        + "from InternalProducts p "
                        + "join InternalProductVectors v on v.productId = p.productId "
                        + "left join p.style s "
                        + "where p.style is not null and v.xCoord is not null and v.yCoord is not null")
        List<com.kdt03.fashion_api.domain.dto.ProductMapDTO> findAllProductMaps();

        @Query("SELECT new com.kdt03.fashion_api.domain.dto.StyleCountDTO(s.styleName, COUNT(p)) " +
                        "FROM InternalProducts p " +
                        "JOIN p.style s " +
                        "WHERE p.style IS NOT NULL " +
                        "GROUP BY s.styleName " +
                        "ORDER BY COUNT(p) DESC")
        List<StyleCountDTO> countProductsByStyle();
}
