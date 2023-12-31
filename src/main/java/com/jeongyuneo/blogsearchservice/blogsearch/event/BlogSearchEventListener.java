package com.jeongyuneo.blogsearchservice.blogsearch.event;

import com.jeongyuneo.blogsearchservice.blogsearch.service.BlogSearchRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BlogSearchEventListener {

    private final BlogSearchRankingService blogSearchRankingService;

    @EventListener
    public void increase(BlogSearchEvent event) {
        blogSearchRankingService.increaseSearchCount(event.getKeyword());
    }
}
