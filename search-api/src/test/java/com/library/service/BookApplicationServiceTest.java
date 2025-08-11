package com.library.service;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.entity.DailyStat;
import com.library.repository.DailyStatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@DataJpaTest
@Import({BookApplicationService.class, DailyStatCommandService.class})
class BookApplicationServiceTest {

    @Autowired
    BookApplicationService service;

    @Autowired
    DailyStatRepository repository;

    @MockBean
    BookQueryService bookQueryService; // 외부 API 호출 Mock

    @DisplayName("search 메서드 호출 시 결과 반환과 통계 데이터 저장을 처리한다.")
    @Test
    void search() throws Exception{
        //given
        String query = "HTTP";
        int page = 1;
        int size = 10;
        LocalDateTime eventTime = LocalDateTime.now();
        DailyStat dailyStat = new DailyStat(query, eventTime);

        //when
        // 외부 API 호출 Mock: 실제 구현/네트워크 제거
        PageResult<SearchResponse> mockResult = mock(PageResult.class);
        when(bookQueryService.search(query, page, size)).thenReturn(mockResult);

        // when
        LocalDateTime before = LocalDateTime.now();
        service.search(query,page,size);
        LocalDateTime after = LocalDateTime.now();

        //then
        // 외부 검색 서비스가 올바른 파라미터로 호출되었는지
        verify(bookQueryService, times(1)).search(query, page, size);
        // DailyStat가 DB에 저장되었는지 및 값 검증
        List<DailyStat> stats = repository.findAll();
        assertThat(stats).hasSize(1);
        DailyStat saved = stats.get(0);
        assertThat(saved.getQuery()).isEqualTo(query);
        assertThat(saved.getEventDateTime()).isBetween(before, after);
    }

}