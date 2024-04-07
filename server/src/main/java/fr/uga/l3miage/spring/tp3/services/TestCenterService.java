package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.TestCenterComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.TestCenterAddCandidatesRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestCenterService {
    final private TestCenterComponent testCenterComponent;
    final private CandidateComponent candidateComponent;

    public Boolean addSetOfCandidates(Long testCenterId, Set<Long> candidateIds) {
        try {
            Set<CandidateEntity> candidateEntities = candidateComponent.getAllById(candidateIds);

            LocalDate date = LocalDate.now().minusYears(18);
            Set<CandidateEntity> candidateEntitiesSelected = (Set<CandidateEntity>) candidateEntities.stream()
                    .filter(candidateEntity -> candidateEntity.getBirthDate().compareTo(date) <0);

            TestCenterEntity testCenter = testCenterComponent.getById(testCenterId);

            testCenter.setCandidateEntities(candidateEntities);
            testCenterComponent.addSetOfCandidate(testCenter);

            return true;
        } catch (RuntimeException | CandidateNotFoundException | TestCenterNotFoundException e) {
            throw new TestCenterAddCandidatesRestException(
                    e.getMessage(),
                    ((CandidateNotFoundException) e).getCandidateId(),
                    ((TestCenterNotFoundException) e).getTestCenterId()
            );
        }
    }

}
