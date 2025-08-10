package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.service.BookQueryService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookControllerTest.TestBoot.class)
@AutoConfigureMockMvc
@Import(BookController.class)
class BookControllerTest {
    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class TestBoot {}

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookQueryService bookQueryService;

    @Test
    void 검색_API_정상응답() throws Exception{
        //given
        String query = "HTTP";
        int page = 1;
        int size = 10;

        SearchResponse var1 = SearchResponse.builder()
            .title("HTTP 완벽가이드")
            .author("데이빗 고올리")
            .publisher("인사이트")
            .pubDate(LocalDate.of(2014, 12, 15))
            .isbn("9788966261208")
            .build();

        PageResult<SearchResponse> pageResult = new PageResult<>(page, size, 1, List.of(var1));

        BDDMockito.given(bookQueryService.search(query, page, size))
            .willReturn(pageResult);

        //when //then
        mvc.perform(get("/books")
            .param("query", query)
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())

            .andExpect(jsonPath("$.page").value(page))
            .andExpect(jsonPath("$.size").value(size))
            .andExpect(jsonPath("$.totalElement").value(1))
            // contents 배열
            .andExpect(jsonPath("$.contents", hasSize(1)))
            .andExpect(jsonPath("$.contents[0].title").value("HTTP 완벽가이드"))
            .andExpect(jsonPath("$.contents[0].author").value("데이빗 고올리"))
            .andExpect(jsonPath("$.contents[0].publisher").value("인사이트"))
            .andExpect(jsonPath("$.contents[0].isbn").value("9788966261208"));

        BDDMockito.then(bookQueryService)
            .should().search(query, page, size);
        BDDMockito.then(bookQueryService)
            .shouldHaveNoMoreInteractions();
    }

    @Test
    void 필수파라미터_누락시_400() throws Exception {
        mvc.perform(get("/books")
                .param("page", "1")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

}