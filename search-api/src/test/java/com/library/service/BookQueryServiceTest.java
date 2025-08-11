package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BookQueryServiceTest {

    @Test
    void search_레포지토리_위임() {
        // given
        BookRepository repo = mock(BookRepository.class);
        BookQueryService service = new BookQueryService(repo);

        String query = "HTTP";
        int page = 1, size = 10;
        SearchResponse r1 = mock(SearchResponse.class);
        SearchResponse r2 = mock(SearchResponse.class);
        PageResult<SearchResponse> expected =
            new PageResult<>(page, size, 2, List.of(r1, r2));

        given(repo.search(query, page, size)).willReturn(expected);

        // when
        PageResult<SearchResponse> actual = service.search(query, page, size);

        // then
        assertSame(expected, actual);
        then(repo).should(times(1)).search(query, page, size);
        then(repo).shouldHaveNoMoreInteractions();
    }

    @Test
    void search_레포지토리_예외_전파() {
        // given
        BookRepository repo = mock(BookRepository.class);
        BookQueryService sut = new BookQueryService(repo);

        String query = "ERR";
        int page = 1, size = 10;
        RuntimeException boom = new RuntimeException("boom");
        when(repo.search(query, page, size)).thenThrow(boom);

        // when / then
        RuntimeException e = assertThrows(RuntimeException.class,
            () -> sut.search(query, page, size));
        assertSame(boom, e);
        verify(repo).search(query, page, size);
        verifyNoMoreInteractions(repo);
    }
}