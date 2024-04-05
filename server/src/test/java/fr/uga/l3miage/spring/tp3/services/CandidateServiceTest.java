package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateServiceTest {
    @Autowired
    private CandidateService candidateService;
    @MockBean
    private CandidateComponent candidateComponent;

    @Test
    void getCandidateAverage() throws CandidateNotFoundException {
        //given
        CandidateEntity cand = CandidateEntity
                .builder()
                .firstname("John")
                .lastname("Doe")
                .phoneNumber("07560987")
                .email("john.doe@gmail.com")
                .birthDate(LocalDate.parse("2004-03-08"))
                .hasExtraTime(false)
                .build();

        ExamEntity exam1 = ExamEntity
                .builder()
                .name("exam1")
                .startDate(LocalDateTime.of(2024, Month.APRIL, 01, 10, 00))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 01, 12, 00))
                .weight(3)
                .build();

        ExamEntity exam2 = ExamEntity
                .builder()
                .name("exam2")
                .startDate(LocalDateTime.of(2024, Month.APRIL, 02, 9, 00))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 02, 11, 00))
                .weight(6)
                .build();

        CandidateEvaluationGridEntity eval1 = CandidateEvaluationGridEntity
                .builder()
                .grade(12)
                .submissionDate(LocalDateTime.now())
                .candidateEntity(cand)
                .examEntity(exam1)
                .build();

        CandidateEvaluationGridEntity eval2 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .submissionDate(LocalDateTime.now())
                .candidateEntity(cand)
                .examEntity(exam2)
                .build();


        cand.setCandidateEvaluationGridEntities(Set.of(eval1, eval2));
        exam1.setCandidateEvaluationGridEntities(Set.of(eval1));
        exam2.setCandidateEvaluationGridEntities(Set.of(eval2));

        when(candidateComponent.getCandidatById(anyLong())).thenReturn(cand);

        Double responseExpected = 14.0;

        //when
        Double candidateAverage = candidateService.getCandidateAverage(anyLong());

        //then
        assertThat(candidateAverage).isEqualTo(candidateAverage);
    }

    @Test
    void getCandidateNotFoundAverage() throws CandidateNotFoundException {
        //given - when
        when(candidateComponent.getCandidatById(anyLong())).thenThrow(CandidateNotFoundException.class);

        //then
        assertThrows(CandidateNotFoundRestException.class, ()->candidateService.getCandidateAverage(anyLong()));
    }


}
