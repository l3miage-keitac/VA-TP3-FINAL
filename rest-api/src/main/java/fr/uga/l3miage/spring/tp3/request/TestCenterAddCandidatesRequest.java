package fr.uga.l3miage.spring.tp3.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class TestCenterAddCandidatesRequest {
    @Schema(description = "liste des ids des candidats")
    Set<Long> candidatesIds;
}
