package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {
    @Autowired
    private SessionComponent sessionComponent;
    @MockBean
    private EcosSessionRepository sessionRepository;
    @MockBean
    private EcosSessionProgrammationRepository programmationRepository;
    @MockBean
    private EcosSessionProgrammationStepRepository stepRepository;

    @Test
    void createSession(){
        //Given
        ExamEntity examEntity = ExamEntity
                .builder()
                .id(123L)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .name("exam")
                .weight(12)
                .skillEntities(Set.of())
                .build();

        EcosSessionProgrammationEntity programmationEntity = EcosSessionProgrammationEntity
                .builder()
                .id(12L)
                .label("test1")
                .build();
        EcosSessionProgrammationStepEntity stepEntity = EcosSessionProgrammationStepEntity
                .builder()
                .id(1L)
                .code("code")
                .dateTime(LocalDateTime.now())
                .ecosSessionProgrammationEntity(programmationEntity)
                .build();

        programmationEntity.setEcosSessionProgrammationStepEntities(Set.of(stepEntity));

        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .id(123L)
                .name("test")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .status(SessionStatus.CREATED)
                .examEntities(Set.of(examEntity))
                .ecosSessionProgrammationEntity(programmationEntity)
                .build();
        //When
        when(stepRepository.saveAll(any())).thenReturn(List.of());
        when(programmationRepository.save(any())).thenReturn(new EcosSessionProgrammationEntity());
        when(sessionRepository.save(any())).thenReturn(ecosSessionEntity);

        EcosSessionEntity resultat = sessionComponent.createSession(ecosSessionEntity);
        //Then
        assertEquals(resultat, ecosSessionEntity);
        verify(stepRepository, times(1)).saveAll(any());
        verify(programmationRepository, times(1)).save(any());
        verify(sessionRepository, times(1)).save(any());
    }

    @Test
    void getSessionById() throws SessionNotFoundException{
        //Given
        ExamEntity examEntity = ExamEntity
                .builder()
                .id(123L)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .name("exam")
                .weight(12)
                .skillEntities(Set.of())
                .build();

        EcosSessionProgrammationEntity programmationEntity = EcosSessionProgrammationEntity
                .builder()
                .id(12L)
                .label("test1")
                .build();
        EcosSessionProgrammationStepEntity stepEntity = EcosSessionProgrammationStepEntity
                .builder()
                .id(1L)
                .code("code")
                .dateTime(LocalDateTime.now())
                .ecosSessionProgrammationEntity(programmationEntity)
                .build();

        programmationEntity.setEcosSessionProgrammationStepEntities(Set.of(stepEntity));

        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .id(123L)
                .name("test")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .status(SessionStatus.CREATED)
                .examEntities(Set.of(examEntity))
                .ecosSessionProgrammationEntity(programmationEntity)
                .build();
        //When
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(ecosSessionEntity));
        assertDoesNotThrow(() -> sessionComponent.getSessionById(123L));
    }
    @Test
    void getSessionByIdNotFound(){
        //When
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        //Then
        assertThrows(SessionNotFoundException.class, () -> sessionComponent.getSessionById(123L));
    }
}
