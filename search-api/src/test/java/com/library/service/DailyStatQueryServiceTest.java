package com.library.service;

import com.library.controller.response.StatResponse;
import com.library.repository.DailyStatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DailyStatQueryServiceTest {

    @DisplayName("findQueryCount 조회 시 일별 조회 후 쿼리개수 반환")
    @Test
    void findQueryCount() throws Exception {
        //given
        DailyStatRepository repo = mock(DailyStatRepository.class);
        DailyStatQueryService service = new DailyStatQueryService(repo);

        String query = "HTTP";
        LocalDate date = LocalDate.of(2025, 8, 11);

        //when
        StatResponse actual = service.findQueryCount(query, date);

        //then

        then(repo).should(times(1)).countByQueryAndEventDateTimeBetween(query, LocalDateTime.of(2025, 8, 11, 0, 0, 0), LocalDateTime.of(2025, 8, 11, 23, 59, 59, 999999999));

    }
}