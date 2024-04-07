package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {
    @Autowired
    private CandidateComponent candidateComponent;
    @MockBean
    private CandidateRepository candidateRepository;

    @Test
    void getCandidatById() throws CandidateNotFoundException {
        //Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .birthDate(LocalDate.now())
                .id(123L)
                .hasExtraTime(false)
                .email("test1@test")
                .candidateEvaluationGridEntities(Set.of())
                .build();
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));
        //then-when
        assertDoesNotThrow(()-> candidateComponent.getCandidatById(123L));
    }

    @Test
    void getNoCandidatById(){
        //Given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());
        //Then - when
        assertThrows(CandidateNotFoundException.class, () -> candidateComponent.getCandidatById(1235L));
    }

}
