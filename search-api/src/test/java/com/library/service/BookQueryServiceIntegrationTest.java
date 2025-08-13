package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.repository.BookRepository;
import com.library.repository.KakaoBookRepository;
import com.library.repository.NaverBookRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spockframework.spring.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class BookQueryServiceIntegrationTest {

    @Autowired
    BookQueryService bookQueryService;

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    NaverBookRepository naverBookRepository;

    @MockBean
    KakaoBookRepository kakaoBookRepository;


    @DisplayName("정상 상황에서는 Circuit의 상태가 CLOSED이고 naverRepo를 호출한다.")
    @Test
    void search() {
        // given
        String query = "HTTP";
        int page = 1, size = 10;
        SearchResponse r1 = mock(SearchResponse.class);
        SearchResponse r2 = mock(SearchResponse.class);
        PageResult<SearchResponse> expected =
            new PageResult<>(page, size, 2, List.of(r1, r2));

        given(naverBookRepository.search(query, page, size)).willReturn(expected);

        // when
        PageResult<SearchResponse> actual = bookQueryService.search(query, page, size);

        // then
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get();
        assertEquals(circuitBreaker.getState(), CircuitBreaker.State.CLOSED);
        then(kakaoBookRepository).should(times(0)).search(query, page, size);

        then(naverBookRepository).should(times(1)).search(query, page, size);
        then(naverBookRepository).shouldHaveNoMoreInteractions();
    }

    @DisplayName("CircuitBreaker가 OPEN으로 발동하고 kakaoRepo를 호출한다.")
    @Test
    void searchWithFallback() {
        // given
        // CircuitBreaker 설정을 완화해서 재적용한다.
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .slidingWindowSize(1)
            .minimumNumberOfCalls(1)
            .failureRateThreshold(50)
            .build();
        circuitBreakerRegistry.circuitBreaker("naverSearch", config);


        String query = "HTTP";
        int page = 1, size = 10;
        SearchResponse r1 = mock(SearchResponse.class);
        SearchResponse r2 = mock(SearchResponse.class);
        PageResult<SearchResponse> expected =
            new PageResult<>(page, size, 2, List.of(r1, r2));

        given(naverBookRepository.search(query, page, size)).willThrow(new RuntimeException("naver 호출 실패!!"));
        given(kakaoBookRepository.search(query, page, size)).willReturn(expected);

        // when
        PageResult<SearchResponse> actual = bookQueryService.search(query, page, size);

        // then
        // naverRepo를 호출 시 CircuitBreaker가 발동하고 kakaoRepo를 호출한다
        assertSame(expected, actual);
        then(naverBookRepository).should(times(1)).search(query, page, size);
        then(naverBookRepository).shouldHaveNoMoreInteractions();

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get();
        assertEquals(circuitBreaker.getState(), CircuitBreaker.State.OPEN);
        then(kakaoBookRepository).should(times(1)).search(query, page, size);

        // 실패가 1건인지 체크
        assertEquals(circuitBreaker.getMetrics().getNumberOfFailedCalls(), 1);
    }
}