package fr.uga.l3miage.spring.tp3.endpoints;

import fr.uga.l3miage.spring.tp3.exceptions.TestCenterAddCandidateErrorResponse;
import fr.uga.l3miage.spring.tp3.request.TestCenterAddCandidatesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Gestion des centres d'examen")
@RestController
@RequestMapping("/api/test-centers")
public interface TestCenterEndpoints {

    @Operation(description = "Ajouter à un centre de test une collection d'étudiants")
    @ApiResponse(responseCode = "202",description = "La session à bien été créer")
    @ApiResponse(responseCode = "404" ,description = "Le centre de test ou l'étudiant n'est pas trouvé", content = @Content(schema = @Schema(implementation = String.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = TestCenterAddCandidateErrorResponse.class),mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping("/{testCenterId}/candidates")
    Boolean addCandidates(@PathVariable Long testCenterId  , @RequestBody TestCenterAddCandidatesRequest data);
}
