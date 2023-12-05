package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchRankingResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchRankingResponseElement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BlogSearchRankingService {

    private static final String BOOK_SEARCH_RANKING_KEY = "bookSearchRanking";
    private static final int RANKING_INCREMENT_SCORE = 1;
    private static final int RANKING_START = 0;
    private static final int RANKING_END = 9;

    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void increaseSearchCount(String keyword) {
        redisTemplate.opsForZSet().incrementScore(BOOK_SEARCH_RANKING_KEY, keyword, RANKING_INCREMENT_SCORE);
    }

    @Transactional(readOnly = true)
    public BlogSearchRankingResponse getSearchRanking() {
        return BlogSearchRankingResponse.from(
                Optional.ofNullable(redisTemplate.opsForZSet().reverseRangeWithScores(BOOK_SEARCH_RANKING_KEY, RANKING_START, RANKING_END))
                        .map(keywords -> keywords.stream()
                                .map(keyword -> BlogSearchRankingResponseElement.of(keyword.getValue(), keyword.getScore()))
                                .collect(Collectors.toList()))
                        .orElseGet(Collections::emptyList)
        );
    }
}
