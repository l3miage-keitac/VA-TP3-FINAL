package fr.uga.l3miage.spring.tp3.exceptions.handlers;

import fr.uga.l3miage.spring.tp3.errors.UpdateSessionStatusErrorResponse;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionNotFoundRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionRestHandler {
    @ExceptionHandler(SessionNotFoundRestException.class)
    public ResponseEntity<UpdateSessionStatusErrorResponse> handle(HttpServletRequest httpServletRequest, Exception e){
        SessionNotFoundRestException exception = (SessionNotFoundRestException) e;
        final UpdateSessionStatusErrorResponse response = UpdateSessionStatusErrorResponse
                .builder()
                .uri(httpServletRequest.getRequestURI())
                .errorMessage(exception.getMessage())
// Je n'arrive pas à recuperer le status, car depuis le service, La session retournée depuis de GetSessionById ne peut
//pas être utilisé dans le catch :(
             //   .status(exception.getSessionId()) Je n'arrive pas à r
                .build();
        log.warn(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
