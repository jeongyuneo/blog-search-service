package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchResponse;
import com.jeongyuneo.blogsearchservice.blogsearch.dto.BlogSearchServiceRequest;

public interface BlogSearchService {

    BlogSearchResponse searchByQuery(BlogSearchServiceRequest blogSearchServiceRequest);
}
