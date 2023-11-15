package com.jeongyuneo.blogsearchservice.blogsearch.service;

import com.jeongyuneo.blogsearchservice.blogsearch.repository.BlogSearchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("검색 랭킹 서비스 동시성 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ConcurrentBlogSearchRankingServiceTest {

    @Autowired
    private BlogSearchRankingService blogSearchRankingService;

    @Autowired
    private BlogSearchRepository blogSearchRepository;

    @Test
    void 하나의_키워드를_동시에_100번_검색하면_검색횟수가_100_증가한다() throws InterruptedException {
        // given
        int numberOfThreads = 100;
        String keyword = "동시 검색 키워드";

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        // when
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    blogSearchRankingService.increaseSearchCount(keyword);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // then
        assertThat(
                blogSearchRepository.findByKeyword(keyword)
                        .getCount()
        ).isEqualTo(numberOfThreads);
    }
}
