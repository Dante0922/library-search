package com.library.service;

import com.library.entity.DailyStat;
import com.library.repository.DailyStatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(DailyStatCommandService.class)
class DailyStatCommandServiceTest {

    @Autowired
    DailyStatCommandService service;

    @Autowired
    DailyStatRepository repository;

    @DisplayName("save 시 저장된 값이 입력 값과 일치한다")
    @Test
    void save_value_matches_input() {
        // given
        String query = "HTTP";
        LocalDateTime eventTime = LocalDateTime.now();
        DailyStat dailyStat = new DailyStat(query, eventTime);

        // when
        service.save(dailyStat);

        // then
        DailyStat saved = repository.findById(dailyStat.getId()).orElseThrow();
        assertThat(saved.getQuery()).isEqualTo(query);
        assertThat(saved.getEventDateTime()).isEqualTo(eventTime);
    }
}