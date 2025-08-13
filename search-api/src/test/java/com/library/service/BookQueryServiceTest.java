package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BookQueryServiceTest {

    @DisplayName("search 시 인자가 그대로 넘어가고 naverRepo를 호출한다.")
    @Test
    void search() {
        // given
        BookRepository naverBookRepository = mock(BookRepository.class);
        BookRepository kakaoBookRepository = mock(BookRepository.class);

        BookQueryService service = new BookQueryService(naverBookRepository, kakaoBookRepository);

        String query = "HTTP";
        int page = 1, size = 10;
        SearchResponse r1 = mock(SearchResponse.class);
        SearchResponse r2 = mock(SearchResponse.class);
        PageResult<SearchResponse> expected =
            new PageResult<>(page, size, 2, List.of(r1, r2));

        given(naverBookRepository.search(query, page, size)).willReturn(expected);

        // when
        PageResult<SearchResponse> actual = service.search(query, page, size);

        // then
        assertSame(expected, actual);
        then(naverBookRepository).should(times(1)).search(query, page, size);
        then(kakaoBookRepository).should(times(0)).search(query, page, size);
        then(naverBookRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void search_레포지토리_예외_전파() {
        // given
        BookRepository naverBookRepository = mock(BookRepository.class);
        BookRepository kakaoBookRepository = mock(BookRepository.class);

        BookQueryService service = new BookQueryService(naverBookRepository, kakaoBookRepository);


        String query = "ERR";
        int page = 1, size = 10;
        RuntimeException boom = new RuntimeException("boom");
        when(naverBookRepository.search(query, page, size)).thenThrow(boom);

        // when / then
        RuntimeException e = assertThrows(RuntimeException.class,
            () -> service.search(query, page, size));

        assertSame(boom, e);
        verify(naverBookRepository).search(query, page, size);
        verifyNoMoreInteractions(naverBookRepository);
    }
}