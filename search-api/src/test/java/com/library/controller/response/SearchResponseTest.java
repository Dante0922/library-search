package com.library.controller.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SearchResponseTest {

    @DisplayName("Response 생성 테스트")
    @Test
    void makeSearchResponse() throws Exception{
        //given
        String givenTitle = "HTTP 완벽가이드";
        String givenAuthor = "데이빗 고올리";
        String givenPublisher = "인사이트";
        LocalDate givenPubDate = LocalDate.of(2014,12,15);
        String givenIsbn = "9788966261208";
        //when
        SearchResponse response = SearchResponse.builder()
            .title(givenTitle)
            .author(givenAuthor)
            .publisher(givenPublisher)
            .pubDate(givenPubDate)
            .isbn(givenIsbn)
            .build();

        //then
        assertEquals(response.title(), givenTitle);
        assertEquals(response.author(), givenAuthor);
        assertEquals(response.publisher(), givenPublisher);
        assertEquals(response.pubDate(), givenPubDate);
        assertEquals(response.isbn(), givenIsbn);

    }

}