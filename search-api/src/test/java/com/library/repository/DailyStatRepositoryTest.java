package com.library.repository;

import com.library.entity.DailyStat;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
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
    void save() throws Exception{
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
}