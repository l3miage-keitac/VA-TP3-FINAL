package fr.uga.l3miage.spring.tp3.exceptions.rest;

import lombok.Getter;

@Getter
public class TestCenterAddCandidatesRestException extends RuntimeException{
    private Long candidateId;
    private Long testCenterId;
    public TestCenterAddCandidatesRestException(String message, Long candidateId, Long testCenterId) {
        super(message);
        this.candidateId = candidateId;
        this.testCenterId = testCenterId;
    }
}
