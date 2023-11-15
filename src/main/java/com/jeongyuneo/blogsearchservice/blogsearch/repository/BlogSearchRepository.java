package com.jeongyuneo.blogsearchservice.blogsearch.repository;

import com.jeongyuneo.blogsearchservice.blogsearch.entity.BlogSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogSearchRepository extends JpaRepository<BlogSearch, Long> {

    BlogSearch findByKeyword(String keyword);

    List<BlogSearch> findTop10ByOrderByCountDesc();
}
