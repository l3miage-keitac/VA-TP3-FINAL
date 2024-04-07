package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionComponent sessionComponent;

    @MockBean
    private ExamComponent examComponent;

    @SpyBean
    private SessionMapper sessionMapper;

    @Test
    void createSession() throws ExamNotFoundException {
        //when
        SessionProgrammationStepCreationRequest step = SessionProgrammationStepCreationRequest
                .builder()
                .code("session-step1")
                .description("session step description")
                .dateTime(LocalDateTime.of(2024, Month.APRIL, 2, 10, 0))
                .build();

        SessionProgrammationCreationRequest sessionProg = SessionProgrammationCreationRequest
                .builder()
                .label("session-prog1")
                .steps(Set.of(step))
                .build();

        SessionCreationRequest session = SessionCreationRequest
                .builder()
                .name("session")
                .startDate(LocalDateTime.of(2024, Month.APRIL, 2, 10, 0))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 5, 15, 0))
                .ecosSessionProgrammation(sessionProg)
                .examsId(Set.of())
                .build();

        ExamEntity exam = ExamEntity
                .builder()
                .name("exam")
                .startDate(LocalDateTime.of(2024, Month.APRIL, 01, 10, 00))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 01, 12, 00))
                .weight(3)
                .build();

        EcosSessionEntity sessionEntity = sessionMapper.toEntity(session);

        when(examComponent.getAllById(any())).thenReturn(Set.of(exam));
        when(sessionComponent.createSession(any())).thenReturn(sessionEntity);
        SessionResponse responseExpected = sessionMapper.toResponse(sessionEntity);

        //when
        SessionResponse response = sessionService.createSession(session);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
    }

    @Test
    void createSessionExamNotFound() throws ExamNotFoundException {
        //given - when
        when(examComponent.getAllById(Set.of())).thenThrow(ExamNotFoundException.class);

        //then
        assertThrows(CreationSessionRestException.class, ()->sessionService.createSession(any()));
    }
    @Test
    void updateSessionStatus() throws SessionNotFoundException, ExamNotFoundException {
        //Given
        SessionProgrammationStepCreationRequest step = SessionProgrammationStepCreationRequest
                .builder()
                .code("session-step1")
                .description("session step description")
                .dateTime(LocalDateTime.of(2024, Month.APRIL, 2, 10, 0))
                .build();

        SessionProgrammationCreationRequest sessionProg = SessionProgrammationCreationRequest
                .builder()
                .label("session-prog1")
                .steps(Set.of(step))
                .build();

        SessionCreationRequest session = SessionCreationRequest
                .builder()
                .name("session")
                .startDate(LocalDateTime.of(2024, Month.APRIL, 2, 10, 0))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 5, 15, 0))
                .ecosSessionProgrammation(sessionProg)
                .examsId(Set.of())
                .build();

        ExamEntity exam = ExamEntity
                .builder()
                .name("exam")
                .startDate(LocalDateTime.of(2024, Month.APRIL, 01, 10, 00))
                .endDate(LocalDateTime.of(2024, Month.APRIL, 01, 12, 00))
                .weight(3)
                .build();

        EcosSessionEntity sessionEntity = sessionMapper.toEntity(session);

        when(examComponent.getAllById(any())).thenReturn(Set.of(exam));
        when(sessionComponent.createSession(any())).thenReturn(sessionEntity);
        SessionResponse responseExpected = sessionMapper.toResponse(sessionEntity);
        //when
        SessionResponse response = sessionService.updateSessionStatus(123L);

        assertThat(response.getStatus()).isEqualTo(SessionStatus.EVAL_ENDED);


    }
}
