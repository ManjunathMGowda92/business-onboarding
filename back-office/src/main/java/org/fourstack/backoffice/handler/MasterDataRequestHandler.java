package org.fourstack.backoffice.handler;

import lombok.RequiredArgsConstructor;
import org.fourstack.backoffice.model.AiOuMappingRequest;
import org.fourstack.backoffice.model.AiOuMappingResponse;
import org.fourstack.backoffice.model.AiRequest;
import org.fourstack.backoffice.model.AiResponse;
import org.fourstack.backoffice.model.BackOfficeListResponse;
import org.fourstack.backoffice.model.BackOfficeResponse;
import org.fourstack.backoffice.model.EncryptionDetails;
import org.fourstack.backoffice.model.OuRequest;
import org.fourstack.backoffice.model.OuResponse;
import org.fourstack.backoffice.service.MasterDataService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/masterData")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class MasterDataRequestHandler {
    private final MasterDataService masterDataService;

    @GetMapping("/aiEntity/{id}")
    public BackOfficeResponse retrieveAiEntity(@PathVariable String id) {
        return masterDataService.retrieveAiEntity(id);
    }

    @GetMapping("/aiEntity")
    public BackOfficeListResponse retrieveAiEntities() {
        return masterDataService.retrieveAiEntities();
    }

    @PostMapping("/aiEntity")
    public AiResponse saveAiEntity(@RequestBody AiRequest request) {
        return masterDataService.createAiEntity(request);
    }

    @PutMapping("/aiEntity/{id}/encryptionDetails")
    public AiResponse updateEncryptionDetails(@PathVariable String id, @RequestBody EncryptionDetails encryptionDetails) {
        return new AiResponse();
    }

    @GetMapping("/ouEntity/{id}")
    public OuResponse retrieveOuEntity(@PathVariable String id) {
        return new OuResponse();
    }

    @GetMapping("/ouEntity")
    public List<OuResponse> retrieveOuEntities() {
        return Collections.emptyList();
    }

    @PostMapping("/ouEntity")
    public OuResponse saveOuEntity(@RequestBody OuRequest request) {
        return new OuResponse();
    }

    @GetMapping("/aiOuEntity")
    public List<AiOuMappingResponse> retrieveAiOuEntities() {
        return Collections.emptyList();
    }

    @GetMapping("/aiOuEntity/{aiId}/ou/{ouId}")
    public AiOuMappingResponse retrieveAiOuEntity(@PathVariable String aiId, @PathVariable String ouId) {
        return new AiOuMappingResponse();
    }

    @GetMapping("/aiOuEntity/{aiId}")
    public List<AiOuMappingResponse> retrieveAiOuEntities(@PathVariable String aiId) {
        return Collections.emptyList();
    }

    @PostMapping("/aiOuEntity")
    public AiOuMappingResponse saveAiOuMapEntity(@RequestBody AiOuMappingRequest request) {
        return new AiOuMappingResponse();
    }
}
