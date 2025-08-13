package com.library.repository;

import com.library.*;
import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.feign.KakaoClient;
import com.library.feign.NaverClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class KakaoBookRepositoryTest {


    @Test
    void search_정상_매핑() {
        // given
        KakaoClient kakaoClient = mock(KakaoClient.class);
        KakaoBookRepository repo = new KakaoBookRepository(kakaoClient);

        String query = "HTTP";
        int page = 1, size = 10;

        // Kakao 문서: authors(List), datetime(ISO-8601 with offset)
        Document d1 = new Document(
            "HTTP 완벽가이드",
            List.of("데이빗 고올리"),
            "9788966261208",
            "인사이트",
            "2014-12-15T00:00:00+09:00"
        );
        Document d2 = new Document(
            "토비의 스프링 3.1",
            List.of("이일민"),
            "9788960773431",
            "에이콘",
            "2012-09-01T00:00:00+09:00"
        );

        // Kakao 응답(record) – mock 대신 실제 값으로 구성
        Meta meta = new Meta(false, 10, 10); // 내부 record라고 가정
        KakaoBookResponse resp = new KakaoBookResponse(List.of(d1, d2), meta);

        given(kakaoClient.search(query, page, size)).willReturn(resp);

        // when
        PageResult<SearchResponse> result = repo.search(query, page, size);

        // then
        assertEquals(page, result.page());
        assertEquals(size, result.size());
        assertEquals(10, result.totalElement());
        assertEquals(2, result.contents().size());

        SearchResponse r1 = result.contents().get(0);
        assertEquals("HTTP 완벽가이드", r1.title());
        assertEquals("데이빗 고올리", r1.author());
        assertEquals("인사이트", r1.publisher());
        assertEquals(LocalDate.of(2014, 12, 15), r1.pubDate());
        assertEquals("9788966261208", r1.isbn());

        SearchResponse r2 = result.contents().get(1);
        assertEquals("토비의 스프링 3.1", r2.title());
        assertEquals("이일민", r2.author());
        assertEquals("에이콘", r2.publisher());
        assertEquals(LocalDate.of(2012, 9, 1), r2.pubDate());
        assertEquals("9788960773431", r2.isbn());

        then(kakaoClient).should().search(query, page, size);
        then(kakaoClient).shouldHaveNoMoreInteractions();
    }
}