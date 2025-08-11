package com.library.controller;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.service.BookApplicationService;
import com.library.config.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerIntegrationTest {

    @Autowired MockMvc mvc;

    @MockBean
    BookApplicationService bookApplicationService;

    @Test
    void 검색_API_정상응답() throws Exception {
        // given
        int page = 1, size = 10;
        var r1 = SearchResponse.builder()
            .title("HTTP 완벽가이드")
            .author("데이빗 고올리")
            .publisher("인사이트")
            .pubDate(LocalDate.of(2014,12,15))
            .isbn("9788966261208")
            .build();
        var result = new PageResult<SearchResponse>(page, size, 1, List.of(r1));

        given(bookApplicationService.search("HTTP", page, size)).willReturn(result);

        // when/then
        mvc.perform(get("/books")
                .param("query", "HTTP")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page").value(page))
            .andExpect(jsonPath("$.size").value(size))
            .andExpect(jsonPath("$.totalElement").value(1))
            .andExpect(jsonPath("$.contents", hasSize(1)))
            .andExpect(jsonPath("$.contents[0].title").value("HTTP 완벽가이드"));

        BDDMockito.then(bookApplicationService).should().search("HTTP", page, size);
        BDDMockito.then(bookApplicationService).shouldHaveNoMoreInteractions();
    }

    @Test
    void 검증_실패_query_빈문자열이면_400() throws Exception {
        mvc.perform(get("/books")
                .param("query", "   ")
                .param("page", "1")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorType").value("INVALID_PARAMETER"))
            .andExpect(jsonPath("$.errorMessage", not(emptyOrNullString()))); // "입력은 비어있을 수 없습니다." 기대
    }

    @Test
    void 검증_실패_page_0이면_400() throws Exception {
        mvc.perform(get("/books")
                .param("query", "HTTP")
                .param("page", "0") // @Min(1) 위반
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorType").value("INVALID_PARAMETER"))
            .andExpect(jsonPath("$.errorMessage", not(emptyOrNullString()))); // "페이지번호는 1 이상이어야 합니다." 기대
    }

    @Test
    void 검증_실패_size_초과시_400() throws Exception {
        mvc.perform(get("/books")
                .param("query", "HTTP")
                .param("page", "1")
                .param("size", "99") // @Max(50) 위반
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorType").value("INVALID_PARAMETER"))
            .andExpect(jsonPath("$.errorMessage", not(emptyOrNullString())));
    }
}