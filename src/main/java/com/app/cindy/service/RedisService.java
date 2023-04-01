package com.app.cindy.service;

import com.app.cindy.domain.product.Product;
import com.app.cindy.dto.product.ProductRes;
import com.app.cindy.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class RedisService {
    private final  RedisTemplate<String, String> redisTemplate;

    public void addToViewingHistory(String currentUserId, String currentProductId) {
        String key = "product:" + currentProductId + ":viewed-by";
        redisTemplate.opsForZSet().add(key, currentUserId, System.currentTimeMillis());
        String userKey = "user:" + currentUserId + ":viewing-history";
        redisTemplate.opsForSet().add(userKey, currentProductId);
    }


    public List<Long> getRecentlyViewedProducts(String currentUserId, String currentProductId) {
        String key = "product:" + currentProductId + ":viewed-by";
        Set<String> viewedBy = redisTemplate.opsForZSet().range(key, 0, -1);

        Objects.requireNonNull(viewedBy).remove(currentUserId);

        Set<String> viewedProducts = new HashSet<>();
        for (String userId : viewedBy) {
            String historyKey = "user:" + userId + ":viewing-history";
            viewedProducts.addAll(Objects.requireNonNull(redisTemplate.opsForSet().members(historyKey)));
            if (viewedProducts.size() > 10) {
                break;
            }
        }

        viewedProducts.remove(currentProductId);

        List<Long> productIds = new ArrayList<>();

        for (String productId : viewedProducts) {
            Long id = Long.parseLong(productId);
            productIds.add(id);
        }

        return productIds;

    }


    public Set<String> getViewingHistory(String currentUserId) {
        // Retrieve the set of viewed product IDs for the current user
        String key = "user:" + currentUserId + ":viewing-history";
        return redisTemplate.opsForSet().members(key);
    }
}
