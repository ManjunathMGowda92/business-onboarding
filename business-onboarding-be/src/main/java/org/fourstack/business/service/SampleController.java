package org.fourstack.business.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.service.MasterDataService;
import org.fourstack.business.entity.AiEntity;
import org.fourstack.business.entity.AiOuMapEntity;
import org.fourstack.business.entity.OuEntity;
import org.fourstack.business.exception.ValidationException;
import org.fourstack.business.model.AiDetails;
import org.fourstack.business.model.AiOuMappingDetails;
import org.fourstack.business.model.OuDetails;
import org.fourstack.business.validator.BusinessValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/master")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class SampleController {

    private final MasterDataService masterDataService;
    private final BusinessValidator businessValidator;

    @GetMapping("/aiEntity/{id}")
    public AiEntity retrieveAiEntity(@PathVariable String id) {
        Optional<AiEntity> optionalAiEntity = masterDataService.retrieveAiEntity(id);
        return optionalAiEntity.orElseThrow(() -> new RuntimeException("No data found"));
    }

    @PostMapping("/aiEntity")
    public AiEntity saveAiEntity(@RequestBody AiDetails aiEntity) {
        return masterDataService.saveEntity(aiEntity);
    }

    @GetMapping("/ouEntity/{id}")
    public OuEntity retrieveOuEntity(@PathVariable String id) {
        Optional<OuEntity> optionalOuEntity = masterDataService.retrieveOuEntity(id);
        return optionalOuEntity.orElseThrow(() -> new RuntimeException("No data found"));
    }

    @PostMapping("/ouEntity")
    public OuEntity saveOuEntity(@RequestBody OuDetails ouDetails) {
        return masterDataService.saveEntity(ouDetails);
    }

    @GetMapping("/aiOuEntity/{aiId}/ou/{ouId}")
    public AiOuMapEntity retrieveAiOuEntity(@PathVariable String aiId, @PathVariable String ouId) {
        Optional<AiOuMapEntity> optionalAiOuMapEntity = masterDataService.retrieveAiOuMapEntity(aiId, ouId);
        return optionalAiOuMapEntity.orElseThrow(() -> new RuntimeException("No data found"));
    }

    @PostMapping("/aiOuEntity")
    public AiOuMapEntity saveAiOuEntity(@RequestBody AiOuMappingDetails details) {
        try {
            businessValidator.validateAiOuMappings(details);
        } catch (ValidationException exception) {
            return new AiOuMapEntity();
        }
        return masterDataService.saveEntity(details);
    }
}
