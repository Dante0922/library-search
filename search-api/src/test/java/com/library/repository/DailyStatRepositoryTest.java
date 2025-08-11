package com.library.repository;

import com.library.entity.DailyStat;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class DailyStatRepositoryTest {

    @Autowired
    DailyStatRepository dailyStatRepository;

    @Autowired
    EntityManager entityManager;


    @DisplayName("저장 후 조회")
    @Test
    void save() throws Exception {
        //given
        String query = "HTTP";
        LocalDateTime eventTime = LocalDateTime.now();

        //when
        DailyStat dailyStat = new DailyStat(query, eventTime);
        DailyStat saved = dailyStatRepository.saveAndFlush(dailyStat);

        //then
        assertNotNull(saved.getId());

        //when
        entityManager.clear();
        Optional<DailyStat> result = dailyStatRepository.findById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals(result.get().getQuery(), query);
    }


    @DisplayName("쿼리의 일별 카운트를 조회한다.")
    @Test
    void query_count_retrieve() throws Exception {
        //given
        String query = "HTTP";
        LocalDateTime now = LocalDateTime.of(2025, 8, 10, 0, 0, 0);
        DailyStat dailyStat1 = new DailyStat(query, now.plusMinutes(10));
        DailyStat dailyStat2 = new DailyStat(query, now.minusMinutes(11));
        DailyStat dailyStat3 = new DailyStat(query, now.plusMinutes(10));
        DailyStat dailyStat4 = new DailyStat("JAVA", now.plusMinutes(10));

        dailyStatRepository.saveAll(
            List.of(dailyStat1, dailyStat2, dailyStat3, dailyStat4)
        );
        //when

        long result = dailyStatRepository.countByQueryAndEventDateTimeBetween(query, now, now.plusDays(1));

        //then
        assertEquals(result, 2);
    }
}