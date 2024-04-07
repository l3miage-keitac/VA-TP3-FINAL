package fr.uga.l3miage.spring.tp3.controllers;


import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EcosSessionRepository sessionRepository;

    @SpyBean
    private ExamComponent examComponent;

    @SpyBean
    private SessionComponent sessionComponent;

    @AfterEach
    public void clear() {
        sessionRepository.deleteAll();
    }

    @Test
    void canCreateEcoSessionWithoutExams() {
        //when
        final SessionProgrammationStepCreationRequest step = SessionProgrammationStepCreationRequest
                .builder()
                .code("session-step1")
                .description("session step description")
                .dateTime(LocalDateTime.of(2024, Month.APRIL, 2, 10, 0))
                .build();

        final SessionProgrammationCreationRequest sessionProg = SessionProgrammationCreationRequest
                .builder()
                .label("session-prog1")
                .steps(Set.of(step))
                .build();

        final SessionCreationRequest request = SessionCreationRequest
                .builder()
                .name("session")
                .startDate(LocalDateTime.of(2024, Month.APRIL, 2, 10, 0))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 5, 15, 0))
                .ecosSessionProgrammation(sessionProg)
                .examsId(Set.of())
                .build();

        final HttpHeaders headers = new HttpHeaders();

        // when
        ResponseEntity<SessionResponse> response = testRestTemplate
                .exchange(
                        "/api/sessions/create",
                        HttpMethod.POST,
                        new HttpEntity<>(request, headers),
                        SessionResponse.class
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sessionRepository.count()).isEqualTo(1);
    }
}
