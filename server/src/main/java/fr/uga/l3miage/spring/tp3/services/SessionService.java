package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionMapper sessionMapper;
    private final ExamComponent examComponent;
    private final SessionComponent sessionComponent;

    public SessionResponse createSession(SessionCreationRequest sessionCreationRequest){
        try {
            EcosSessionEntity ecosSessionEntity = sessionMapper.toEntity(sessionCreationRequest);
            EcosSessionProgrammationEntity programmation = sessionMapper.toEntity(sessionCreationRequest.getEcosSessionProgrammation());
            Set<EcosSessionProgrammationStepEntity> stepEntities = sessionCreationRequest.getEcosSessionProgrammation()
                    .getSteps()
                    .stream()
                    .map(sessionMapper::toEntity)
                    .collect(Collectors.toSet());

            Set<ExamEntity> exams = examComponent.getAllById(sessionCreationRequest.getExamsId());

            ecosSessionEntity.setExamEntities(exams);
            programmation.setEcosSessionProgrammationStepEntities(stepEntities);
            ecosSessionEntity.setEcosSessionProgrammationEntity(programmation);

            ecosSessionEntity.setStatus(SessionStatus.CREATED);

            return sessionMapper.toResponse(sessionComponent.createSession(ecosSessionEntity));
        }catch (RuntimeException | ExamNotFoundException e){
            throw new CreationSessionRestException(e.getMessage());
        }
    }
    // On supposera ici que la session dont l'identifiant est passé en paramètre a le status EVAL_STARTED
    // Le passage de l'état EVAL_STARTED à EVAL_ENDED n'est possible que si la première étape est correcte
    public SessionResponse updateSessionStatus(Long idSession) {
        try {
            EcosSessionEntity ecosSessionEntity =sessionComponent.getSessionById(idSession);
            LocalDateTime latestDateTime = ecosSessionEntity
                    .getEcosSessionProgrammationEntity()
                    .getEcosSessionProgrammationStepEntities()
                    .stream()
                    .max(Comparator.comparing(EcosSessionProgrammationStepEntity::getDateTime))
                    .orElse(null).getDateTime();
            if((latestDateTime != null)){
                if(ecosSessionEntity.getStatus().equals(SessionStatus.EVAL_STARTED)) {
                    ecosSessionEntity.setStatus(SessionStatus.EVAL_ENDED);
                }
                else {
                    throw new SessionNotFoundRestException(("L'étape précédente n'était pas EVAL_STARTED pour la session " + idSession), idSession);
                }
            }
            return sessionMapper.toResponse(sessionComponent.getEcosSessionRepository().save(ecosSessionEntity));
        }catch (SessionNotFoundException e){
            throw new SessionNotFoundRestException(e.getMessage(), e.getSessionId());
        }
    }
}
