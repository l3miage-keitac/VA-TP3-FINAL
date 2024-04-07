package fr.uga.l3miage.spring.tp3.errors;

import fr.uga.l3miage.spring.tp3.responses.enums.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Data
public class UpdateSessionStatusErrorResponse {
    @Schema(description = "end point call", example = "/api/drone/")
    private final String uri;
    @Schema(description = "error message", example = "La session n°1 n'existe pas")
    private final String errorMessage;
    @Schema
    private final SessionStatus status;
}
