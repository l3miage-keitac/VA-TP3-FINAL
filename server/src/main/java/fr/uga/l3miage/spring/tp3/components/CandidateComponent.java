package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CandidateComponent {
    private final CandidateEvaluationGridRepository candidateEvaluationGridRepository;
    private final CandidateRepository candidateRepository;

    public Set<CandidateEntity> getAllEliminatedCandidate(){
        return candidateEvaluationGridRepository.findAllByGradeIsLessThanEqual(5)
                .stream()
                .map(CandidateEvaluationGridEntity::getCandidateEntity)
                .collect(Collectors.toSet());
    }


    public CandidateEntity getCandidatById(Long id) throws CandidateNotFoundException {
       return candidateRepository.findById(id).orElseThrow(()-> new CandidateNotFoundException(String.format("Le candidat [%s] n'a pas été trouvé",id),id));
    }

    public Set<CandidateEntity> getAllById(Set<Long> candidateIds) throws CandidateNotFoundException {

        List<CandidateEntity> candidateEntities = candidateRepository.findAllById(candidateIds);

        if (candidateIds.size() != candidateEntities.size())
            throw new CandidateNotFoundException("un candidat n'a pas été trouvé", null);
        return new HashSet<>();
    }

}
