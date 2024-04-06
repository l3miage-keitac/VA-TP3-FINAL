package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ExamComponentTest {
    @Autowired
    private ExamComponent examComponent;
    @MockBean
    private ExamRepository examRepository;

    @Test
    void getAllById() throws ExamNotFoundException {
        //Given
        ExamEntity examEntity1 = ExamEntity
                .builder()
                .id(123L)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .name("moi")
                .weight(12)
                .skillEntities(Set.of())
                .build();

        ExamEntity examEntity2 = ExamEntity
                .builder()
                .id(1234L)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .name("moi")
                .weight(12)
                .skillEntities(Set.of())
                .build();

        //when
        when(examRepository.findAllById(any())).thenReturn(List.of(examEntity1, examEntity2));
        Set<ExamEntity> examEntities = examComponent.getAllById(Set.of(123L,1234L));

        //then
        assertEquals(2, examEntities.size());
      //  assertThat(examEntities.stream().findFirst().get().getId()).isEqualTo(123L);
        assertTrue(examEntities.contains(examEntity1));
    }

    @Test
    void getAllByIdEmpty() throws ExamNotFoundException {
        //Given
        when(examRepository.findAllById(any())).thenReturn(List.of());

        //then-when
        assertThrows(ExamNotFoundException.class, () -> examComponent.getAllById(Set.of(123L,1234L)));

    }
}
