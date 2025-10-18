package com.example.demo.repository;

import com.example.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
            
                    SELECT id, user_id, content, created_at,
                   ST_X(location::geometry) AS lon,
                   ST_Y(location::geometry) AS lat
            FROM posts
            WHERE ST_DWithin(
                location::geography,
                ST_MakePoint(:lon, :lat)::geography,
                :distance
            )
            ORDER BY created_at DESC
            """, nativeQuery = true)
    List<Map<String, Object>> findPostsNearby(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("distance") double distanceInMeters
    );

    List<Post> findByUserId(String userId);
}
