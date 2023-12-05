package com.jeongyuneo.blogsearchservice.blogsearch.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchRankingResponseElement {

    private String keyword;
    private int count;

    public static BlogSearchRankingResponseElement of(String keyword, Double count) {
        return new BlogSearchRankingResponseElement(keyword, count.intValue());
    }
}
