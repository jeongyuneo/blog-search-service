package com.jeongyuneo.blogsearchservice.blogsearch.dto.kakaoapi;

import lombok.*;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Document {

    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;
    private Date datetime;
}
