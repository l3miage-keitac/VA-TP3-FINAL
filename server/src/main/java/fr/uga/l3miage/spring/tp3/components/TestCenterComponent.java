package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestCenterComponent {
    final private TestCenterRepository testCenterRepository;

    public TestCenterEntity getById(Long id) throws TestCenterNotFoundException {
        return testCenterRepository.findById(id).orElseThrow(() -> new TestCenterNotFoundException("Le centre d'examen n'a pas été trouvé", id));
    }

    public TestCenterEntity addSetOfCandidate(TestCenterEntity entity) {
        return testCenterRepository.save(entity);
    }
}
