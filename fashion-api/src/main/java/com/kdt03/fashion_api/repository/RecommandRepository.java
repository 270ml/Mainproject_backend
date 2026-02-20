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
                   1 - (pv.embedding <=> npv.embedding) as similarityScore
            from nineounce_product_vectors pv
            join naver_product_vectors npv on true
            join naver_products np on npv.product_id = np.product_id
            where pv.product_id = :internalImageId
            order by pv.embedding <=> npv.embedding
            limit 10
            """, nativeQuery = true)
    List<SimilarProductProjection> findSimilarProducts(@Param("internalImageId") String internalImageId);

    @Query(value = """
            WITH random_samples AS (
                SELECT ipv.*, ip.product_name, ip.price, ip.image_url
                FROM nineounce_product_vectors ipv
                JOIN nineounce_products ip ON ipv.product_id = ip.product_id
                WHERE ipv.product_id != :baseId AND ipv.embedding IS NOT NULL
                ORDER BY RANDOM()
                LIMIT 10
            )
            SELECT rs.product_id as productId,
                   rs.product_name as title,
                   rs.price as price,
                   rs.image_url as imageUrl,
                   '' as productLink,
                   1 - (p.embedding <=> rs.embedding) as similarityScore
            FROM nineounce_product_vectors p
            JOIN random_samples rs ON true
            WHERE p.product_id = :baseId
            ORDER BY similarityScore DESC
            """, nativeQuery = true)
    List<SimilarProductProjection> findRandom10SimilarProducts(@Param("baseId") String baseId);

    @Query(value = """
            WITH random_samples AS (
                SELECT ipv.*, ip.product_name, ip.price, ip.image_url
                FROM nineounce_product_vectors ipv
                JOIN nineounce_products ip ON ipv.product_id = ip.product_id
                WHERE ipv.embedding IS NOT NULL
                ORDER BY RANDOM()
                LIMIT 10
            )
            SELECT rs.product_id as productId,
                   rs.product_name as title,
                   rs.price as price,
                   rs.image_url as imageUrl,
                   '' as productLink,
                   1 - (cast(:embedding as vector) <=> rs.embedding) as similarityScore
            FROM random_samples rs
            ORDER BY similarityScore DESC
            """, nativeQuery = true)
    List<SimilarProductProjection> findRandom10ByEmbedding(@Param("embedding") float[] embedding);
}
