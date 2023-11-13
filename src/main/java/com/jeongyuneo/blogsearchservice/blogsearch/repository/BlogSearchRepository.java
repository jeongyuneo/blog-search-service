package com.jeongyuneo.blogsearchservice.blogsearch.repository;

import com.jeongyuneo.blogsearchservice.blogsearch.entity.BlogSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogSearchRepository extends JpaRepository<BlogSearch, Long> {

    Optional<BlogSearch> findByKeyword(String keyword);
}
