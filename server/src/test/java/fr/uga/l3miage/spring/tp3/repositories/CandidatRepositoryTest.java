package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidatRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateEvaluationGridRepository gridRepository;

    @Test
    void testFindAllByTestCenterEntityCode() {
        //given
        TestCenterEntity testCenter = TestCenterEntity
                .builder()
                .code(TestCenterCode.GRE)
                .city("Grenobe")
                .university("UGA")
                .build();

        CandidateEntity cand1 = CandidateEntity
                .builder()
                .email("cand1@gmail.com")
                .firstname("John")
                .lastname("Doe")
                .birthDate(LocalDate.parse("2007-01-08"))
                .phoneNumber("07456798")
                .testCenterEntity(testCenter)
                .build();

        CandidateEntity cand2 = CandidateEntity
                .builder()
                .email("cand2@gmail.com")
                .firstname("Calvin")
                .lastname("Klein")
                .birthDate(LocalDate.parse("2005-05-08"))
                .phoneNumber("06435676")
                .testCenterEntity(testCenter)
                .build();

        Set<CandidateEntity> candidates = new HashSet<>();
        candidates.add(cand1);
        candidates.add(cand2);

        testCenter.setCandidateEntities(candidates);

        testCenterRepository.save(testCenter);


        candidateRepository.save(cand1);
        candidateRepository.save(cand2);

        //when
        Set<CandidateEntity> candidateEntities = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.GRE);

        //then
        assertThat(candidateEntities).hasSize(2);
        assertThat(candidateEntities.stream().findFirst().get().getBirthDate()).isEqualTo(LocalDate.parse("2007-01-08"));
    }


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

    @Test
    void testFindAllByHasExtraTimeFalseAndBirthDateBefore() {
        CandidateEntity cand1 = CandidateEntity
                .builder()
                .email("cand1@gmail.com")
                .firstname("John")
                .lastname("Doe")
                .hasExtraTime(true)
                .birthDate(LocalDate.parse("2007-01-08"))
                .phoneNumber("07456798")
                .build();

        CandidateEntity cand2 = CandidateEntity
                .builder()
                .email("cand2@gmail.com")
                .firstname("Calvin")
                .lastname("Klein")
                .hasExtraTime(false)
                .birthDate(LocalDate.parse("2005-05-08"))
                .phoneNumber("06435676")
                .build();

        candidateRepository.save(cand1);
        candidateRepository.save(cand2);

        //when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("2006-01-01"));

        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
    }
}
