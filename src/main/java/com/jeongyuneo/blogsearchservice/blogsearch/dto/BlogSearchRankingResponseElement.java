package com.jeongyuneo.blogsearchservice.blogsearch.dto;

import com.jeongyuneo.blogsearchservice.blogsearch.entity.BlogSearch;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchRankingResponseElement {

    private String keyword;
    private int count;

    public static BlogSearchRankingResponseElement from(BlogSearch blogSearch) {
        return new BlogSearchRankingResponseElement(blogSearch.getKeyword(), blogSearch.getCount());
    }
}
