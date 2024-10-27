package com.app.cindy.service;

import com.app.cindy.domain.board.Board;
import com.app.cindy.domain.product.Product;
import com.app.cindy.dto.product.ProductRes;
import com.app.cindy.repository.BoardRepository;
import com.app.cindy.repository.ProductRepository;
import kotlinx.serialization.descriptors.PrimitiveKind;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
@Service
public class RedisService {
    private final  RedisTemplate<String, String> redisTemplate;
    private final BoardRepository boardRepository;

    public void incrementViewCount(Long boardId) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        String key = "boardId::" + boardId;
        String hashKey = "views";

        hashOperations.increment(key, hashKey, 1L);
        System.out.println("Current view count: " + hashOperations.get(key, hashKey));
    }

    @Scheduled(fixedRate = 300000) // 1 minute (60000 ms)
    public void syncViewCountsToDb() {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        for (String key : redisTemplate.keys("boardId::*")) {
            String hashKey = "views";
            String boardIdStr = key.split("::")[1];
            Long boardId = Long.valueOf(boardIdStr);

            String viewCountStr = hashOperations.get(key, hashKey);
            if (viewCountStr != null) {
                Long viewCount = Long.valueOf(viewCountStr);
                Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException("Board not found"));
                board.setView(viewCount);
                boardRepository.save(board);
                redisTemplate.delete(key);
            }
        }
    }

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
