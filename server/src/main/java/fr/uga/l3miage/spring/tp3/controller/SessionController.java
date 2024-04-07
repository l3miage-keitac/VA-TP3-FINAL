package fr.uga.l3miage.spring.tp3.controller;

import fr.uga.l3miage.spring.tp3.endpoints.SessionEndpoints;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionNotFoundException;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import fr.uga.l3miage.spring.tp3.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SessionController implements SessionEndpoints {
    private final SessionService sessionService;

    @Override
    public SessionResponse createSession(SessionCreationRequest request) {
        return sessionService.createSession(request);
    }
    @Override
    public SessionResponse updateSessionStatus(Long idSession){
        try{
            return sessionService.updateSessionStatus(idSession);
        }catch (SessionNotFoundRestException e){
            if(e.getMessage().contains("L'étape précédente n'était pas EVAL_STARTED")){
              //  return new SessionNotFoundException(("Une erreur s'est produite lors de la mise à jour"), idSession);
                return null;
            }
            return null;
        }
    }
}
