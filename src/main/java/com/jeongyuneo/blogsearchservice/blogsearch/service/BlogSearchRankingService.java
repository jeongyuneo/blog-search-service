package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.entity.BlogSearch;
import com.jeongyuneo.blogsearchservice.blogsearch.repository.BlogSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogSearchRankingService {

    private final BlogSearchRepository blogSearchRepository;

    public void increaseSearchCount(String keyword) {
        blogSearchRepository.findByKeyword(keyword)
                .ifPresentOrElse(
                        BlogSearch::increase,
                        () -> blogSearchRepository.save(BlogSearch.from(keyword))
                );
    }
}
