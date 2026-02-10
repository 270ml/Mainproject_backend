package com.kdt03.fashion_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kdt03.fashion_api.domain.InternalProducts;
import com.kdt03.fashion_api.domain.dto.SimilarProductProjection;

public interface RecommandRepository extends JpaRepository<InternalProducts, String> {
    @Query(value = """
            SELECT np.product_id as productId,
                   np.title as title,
                   np.price as price,
                   np.image_url as imageUrl,
                   np.product_link as productLink,
                   1 - (p.embedding <=> np.embedding) as similarityScore
            from internal_products p
            join naver_products np on true
            where p.product_id = :internalImageId
            order by p.embedding <=> np.embedding desc
            limit 10
            """, nativeQuery = true)
    List<SimilarProductProjection> findSimilarProducts(@Param("internalImageId") String internalImageId);
}
