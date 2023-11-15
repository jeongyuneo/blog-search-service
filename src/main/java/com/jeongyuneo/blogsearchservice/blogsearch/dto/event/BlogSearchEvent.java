package com.jeongyuneo.blogsearchservice.blogsearch.dto.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BlogSearchEvent {

    private final String keyword;

    public static BlogSearchEvent from(String keyword) {
        return new BlogSearchEvent(keyword);
    }
}
