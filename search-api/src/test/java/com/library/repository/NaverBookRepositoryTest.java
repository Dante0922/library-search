package com.library.repository;

import com.library.Item;
import com.library.NaverBookResponse;
import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.feign.NaverClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class NaverBookRepositoryTest {

    @Test
    void search_정상_매핑() {
        // given
        NaverClient naverClient = mock(NaverClient.class);
        NaverBookRepository service = new NaverBookRepository(naverClient);

        String query = "HTTP";
        int page = 1, size = 10;

        // Naver API 아이템 목
        Item i1 = mock(Item.class);
        given(i1.getTitle()).willReturn("HTTP 완벽가이드");
        given(i1.getAuthor()).willReturn("데이빗 고올리");
        given(i1.getPublisher()).willReturn("인사이트");
        given(i1.getPubDate()).willReturn("20141215");
        given(i1.getIsbn()).willReturn("9788966261208");

        Item i2 = mock(Item.class);
        given(i2.getTitle()).willReturn("토비의 스프링 3.1");
        given(i2.getAuthor()).willReturn("이일민");
        given(i2.getPublisher()).willReturn("에이콘");
        given(i2.getPubDate()).willReturn("20120901");
        given(i2.getIsbn()).willReturn("9788960773431");

        NaverBookResponse naverResp = mock(NaverBookResponse.class);
        given(naverResp.getItems()).willReturn(List.of(i1, i2));
        given(naverResp.getTotal()).willReturn(2);

        given(naverClient.search(query, page, size)).willReturn(naverResp);

        // when
        PageResult<SearchResponse> result = service.search(query, page, size);

        // then
        assertEquals(page, result.page());
        assertEquals(size, result.size());
        assertEquals(2, result.totalElement());
        assertEquals(2, result.contents().size());

        SearchResponse r1 = result.contents().get(0);
        assertEquals("HTTP 완벽가이드", r1.title());
        assertEquals("데이빗 고올리", r1.author());
        assertEquals("인사이트", r1.publisher());
        assertEquals(LocalDate.of(2014,12,15), r1.pubDate());
        assertEquals("9788966261208", r1.isbn());

        SearchResponse r2 = result.contents().get(1);
        assertEquals("토비의 스프링 3.1", r2.title());
        assertEquals("이일민", r2.author());
        assertEquals("에이콘", r2.publisher());
        assertEquals(LocalDate.of(2012,9,1), r2.pubDate());
        assertEquals("9788960773431", r2.isbn());

        then(naverClient).should().search(query, page, size);
        then(naverClient).shouldHaveNoMoreInteractions();
    }

    @Test
    void search_빈목록() {
        // given
        NaverClient naverClient = mock(NaverClient.class);
        NaverBookRepository sut = new NaverBookRepository(naverClient);

        String query = "NONE";
        int page = 3, size = 5;

        NaverBookResponse naverResp = mock(NaverBookResponse.class);
        given(naverResp.getItems()).willReturn(List.of());
        given(naverResp.getTotal()).willReturn(0);

        given(naverClient.search(query, page, size)).willReturn(naverResp);

        // when
        PageResult<SearchResponse> result = sut.search(query, page, size);

        // then
        assertEquals(page, result.page());
        assertEquals(size, result.size());
        assertEquals(0, result.totalElement());
        assertTrue(result.contents().isEmpty());

        then(naverClient).should().search(query, page, size);
        then(naverClient).shouldHaveNoMoreInteractions();
    }
}