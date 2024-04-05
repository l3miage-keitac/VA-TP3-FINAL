package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidatRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private CandidateEvaluationGridRepository gridRepository;
    @Test
    void findAllByCandidateEvaluationGridEntitiesGradeLessThan(){
        CandidateEvaluationGridEntity candidateEvaluationGrid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(12.50)
                .submissionDate(LocalDateTime.now())
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGrid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(9.50)
                .submissionDate(LocalDateTime.now())
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .birthDate(LocalDate.now())
                .hasExtraTime(false)
                .email("test1@test")
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGrid1))
                .build();
        candidateEvaluationGrid1.setCandidateEntity(candidateEntity1);

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .birthDate(LocalDate.now())
                .hasExtraTime(false)
                .email("test2@test")
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGrid2))
                .build();

        candidateEvaluationGrid2.setCandidateEntity(candidateEntity2);


        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);
        gridRepository.save(candidateEvaluationGrid1);
        gridRepository.save(candidateEvaluationGrid2);

        //when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(10.00);

        //then
        assertThat(candidateEntitiesResponses).hasSize(1);

    }
}
