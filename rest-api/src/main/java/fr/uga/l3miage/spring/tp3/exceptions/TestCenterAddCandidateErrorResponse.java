package fr.uga.l3miage.spring.tp3.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestCenterAddCandidateErrorResponse {
    @Schema(description = "end point call", example = "/test-centers/{testCenterId}/")
    private String uri;
    @Schema(description = "error message", example = "/api/drone/")
    private String errorMessage;
    @Schema(description = "candidate id")
    private Long candidateId;
    @Schema(description = "test center id")
    private Long testCenterId;
}
