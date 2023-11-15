package com.jeongyuneo.blogsearchservice.blogsearch.dto.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IncreaseBlogSearchCountEvent {

    private final String keyword;

    public static IncreaseBlogSearchCountEvent from(String keyword) {
        return new IncreaseBlogSearchCountEvent(keyword);
    }
}
