package fr.uga.l3miage.spring.tp3.exceptions.handlers;

import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.TestCenterAddCandidateErrorResponse;
import fr.uga.l3miage.spring.tp3.exceptions.rest.TestCenterAddCandidatesRestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class TestCenterAddCandidatesHandler {

    @ExceptionHandler(TestCenterAddCandidatesRestException.class)
    public ResponseEntity<TestCenterAddCandidateErrorResponse> handle(HttpServletRequest httpServletRequest, Exception exception){
        TestCenterAddCandidatesRestException testCenterAddCandidatesRestException = (TestCenterAddCandidatesRestException) exception;
        TestCenterAddCandidateErrorResponse response = TestCenterAddCandidateErrorResponse
                .builder()
                .candidateId(testCenterAddCandidatesRestException.getCandidateId())
                .testCenterId(testCenterAddCandidatesRestException.getTestCenterId())
                .errorMessage(testCenterAddCandidatesRestException.getMessage())
                .uri(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(400).body(response);
    }
}
