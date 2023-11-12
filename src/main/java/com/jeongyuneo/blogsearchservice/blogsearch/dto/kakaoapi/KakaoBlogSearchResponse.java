package com.jeongyuneo.blogsearchservice.blogsearch.dto.kakaoapi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoBlogSearchResponse {

    private Meta meta;
    private List<Document> documents;
}
