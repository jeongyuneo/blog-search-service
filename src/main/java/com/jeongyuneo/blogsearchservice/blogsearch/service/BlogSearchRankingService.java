package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchRankingResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchRankingResponseElement;
import com.jeongyuneo.blogsearchservice.blogsearch.entity.BlogSearch;
import com.jeongyuneo.blogsearchservice.blogsearch.repository.BlogSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BlogSearchRankingService {

    private final BlogSearchRepository blogSearchRepository;

    public void increaseSearchCount(String keyword) {
        BlogSearch blogSearch = blogSearchRepository.findByKeyword(keyword);
        if (blogSearch == null) {
            blogSearchRepository.save(BlogSearch.from(keyword));
            return;
        }
        blogSearch.increase();
    }

    public BlogSearchRankingResponse getSearchRanking() {
        List<BlogSearchRankingResponseElement> responseElements = blogSearchRepository.findTop10ByOrderByCountDesc()
                .stream()
                .map(BlogSearchRankingResponseElement::from)
                .collect(Collectors.toList());
        return BlogSearchRankingResponse.from(responseElements);
    }
}
