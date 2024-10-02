package org.fourstack.backoffice.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.mapper.EntityMapper;
import org.fourstack.backoffice.mapper.ResponseMapper;
import org.fourstack.backoffice.model.AiRequest;
import org.fourstack.backoffice.model.AiResponse;
import org.fourstack.backoffice.model.BackOfficeListResponse;
import org.fourstack.backoffice.model.BackOfficeResponse;
import org.fourstack.backoffice.repository.AiEntityRepository;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.fourstack.backoffice.util.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class MasterDataService {
    private final AiEntityRepository aiRepository;
    private final ResponseMapper responseMapper;
    private final EntityMapper entityMapper;

    public BackOfficeListResponse retrieveAiEntities() {
        List<AgentInstitutionEntity> aiEntities = aiRepository.findAll();
        if (BackOfficeUtil.isCollectionNotNullOrEmpty(aiEntities)) {
            List<AiResponse> aiResponses = aiEntities.stream()
                    .map(responseMapper::mapAiEntityToResponse)
                    .toList();
            return responseMapper.constructListResponse(aiResponses);

        } else {
            return responseMapper.constructFailureListResponse("ERR002", "No Ai Entities Exists", null);
        }
    }

    public BackOfficeResponse retrieveAiEntity(String aiId) {
        String entityKey = KeyGenerationUtil.generateAiEntityKey(aiId);
        Optional<AgentInstitutionEntity> aiEntity = aiRepository.findById(entityKey);
        if (aiEntity.isPresent()) {
            return responseMapper.constructResponse(aiEntity.get());
        } else {
            return responseMapper.constructFailureResponse("ERR001", "No Ai Entity Exist for Id", "aiId");
        }
    }

    public AiResponse createAiEntity(AiRequest request) {
        AgentInstitutionEntity aiEntity = entityMapper.convertToAiEntity(request);
        String entityKey = KeyGenerationUtil.generateAiEntityKey(aiEntity.getId());
        aiEntity.setKey(entityKey);
        AgentInstitutionEntity savedObject = aiRepository.save(aiEntity);
        return responseMapper.mapAiEntityToResponse(savedObject);
    }
}
