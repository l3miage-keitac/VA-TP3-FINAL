package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

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

}
