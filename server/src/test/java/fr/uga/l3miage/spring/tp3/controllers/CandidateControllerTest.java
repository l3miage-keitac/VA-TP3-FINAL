package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CandidateRepository candidateRepository;

    @SpyBean
    private CandidateComponent candidateComponent;


    @AfterEach
    public void clear() {
        candidateRepository.deleteAll();
    }

    @Test
    void getNotFoundCandidateAverage() {
        //given
        final HttpHeaders headers = new HttpHeaders();

        final Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("candidateId", "10");

        CandidatNotFoundResponse candidatNotFoundResponseExpected = CandidatNotFoundResponse
                .builder()
                .candidateId(10L)
                .uri("/api/candidates/10/average")
                .errorMessage("Le candidat [10] n'a pas été trouvé")
                .build();

        //when
        ResponseEntity<CandidatNotFoundResponse> response = testRestTemplate
                .exchange(
                        "/api/candidates/{candidateId}/average",
                        HttpMethod.GET,
                        new HttpEntity<>(null, headers),
                        CandidatNotFoundResponse.class,
                        urlParams
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).usingRecursiveComparison()
                .isEqualTo(candidatNotFoundResponseExpected);
    }

    @Test
    void canGetCandidateAverage() throws CandidateNotFoundException {
        //given
        CandidateEntity cand = CandidateEntity
                .builder()
                .id(10L)
                .firstname("John")
                .lastname("Doe")
                .phoneNumber("07560987")
                .email("john.doe@gmail.com")
                .birthDate(LocalDate.parse("2004-03-08"))
                .hasExtraTime(false)
                .build();

        ExamEntity exam = ExamEntity
                .builder()
                .name("exam")
                .startDate(LocalDateTime.of(2024, Month.APRIL, 01, 10, 00))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 01, 12, 00))
                .weight(3)
                .build();

        CandidateEvaluationGridEntity eval = CandidateEvaluationGridEntity
                .builder()
                .grade(12)
                .submissionDate(LocalDateTime.now())
                .candidateEntity(cand)
                .examEntity(exam)
                .build();

        cand.setCandidateEvaluationGridEntities(Set.of(eval));
        exam.setCandidateEvaluationGridEntities(Set.of(eval));

        candidateRepository.save(cand);

        final HttpHeaders headers = new HttpHeaders();

        final Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("candidateId", "10");

        Double responseExpected = 12.0;

        //when
        ResponseEntity<Double> response = testRestTemplate
                .exchange(
                        "/api/candidates/{candidateId}/average",
                        HttpMethod.GET,
                        new HttpEntity<>(null, headers),
                        Double.class,
                        urlParams
                );

        //then
        assertThat(response).isEqualTo(responseExpected);
    }

}
