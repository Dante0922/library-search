package com.library;


import java.util.List;

// TODO: 프로젝트 내 컨벤션 통일성은 중요하다. record 와 class 중 하나로 선택할 것
public record KakaoBookResponse(
    List<Document> documents,
    Meta meta
) {
}
