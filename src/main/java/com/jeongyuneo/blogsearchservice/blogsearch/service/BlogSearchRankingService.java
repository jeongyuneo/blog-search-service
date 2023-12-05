package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchRankingResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchRankingResponseElement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BlogSearchRankingService {

    private static final String BOOK_SEARCH_RANKING_KEY = "bookSearchRanking";
    private static final int RANKING_INCREMENT_SCORE = 1;

    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void increaseSearchCount(String keyword) {
        redisTemplate.opsForZSet().incrementScore(BOOK_SEARCH_RANKING_KEY, keyword, RANKING_INCREMENT_SCORE);
    }

    @Transactional(readOnly = true)
    public BlogSearchRankingResponse getSearchRanking() {
        List<BlogSearchRankingResponseElement> responseElements = blogSearchRepository.findTop10ByOrderByCountDesc()
                .stream()
                .map(BlogSearchRankingResponseElement::from)
                .collect(Collectors.toList());
        return BlogSearchRankingResponse.from(responseElements);
    }
}
