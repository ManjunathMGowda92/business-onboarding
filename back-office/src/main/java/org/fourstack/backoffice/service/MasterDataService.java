package org.fourstack.backoffice.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.mapper.ResponseMapper;
import org.fourstack.backoffice.model.AiResponse;
import org.fourstack.backoffice.repository.AiEntityRepository;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class MasterDataService {
    private final AiEntityRepository aiRepository;
    private final ResponseMapper responseMapper;

    public List<AiResponse> retrieveAiEntities() {
        List<AgentInstitutionEntity> aiEntities = aiRepository.findAll();
        return aiEntities.stream()
                .map(responseMapper::mapAiEntityToResponse)
                .toList();
    }
}
