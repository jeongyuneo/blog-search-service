package com.jeongyuneo.blogsearchservice.blogsearch.dto;

import com.jeongyuneo.blogsearchservice.blogsearch.dto.kakaoapi.Document;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogSearchResponseElement {

    private String title;
    private String blogName;
    private String contentSummary;
    private String url;
    private String thumbnail;
    private String createdAt;

    public static BlogSearchResponseElement from(Document document) {
        return new BlogSearchResponseElement(
                document.getTitle(),
                document.getBlogname(),
                document.getContents(),
                document.getUrl(),
                document.getThumbnail(),
                document.getDatetime()
//                LocalDateTime.ofInstant(document.getDatetime().toInstant(), ZoneId.systemDefault())
        );
    }
}
