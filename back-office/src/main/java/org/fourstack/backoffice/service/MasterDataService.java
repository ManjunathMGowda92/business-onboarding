package org.fourstack.backoffice.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.entity.OperationUnitEntity;
import org.fourstack.backoffice.enums.ErrorScenarioCode;
import org.fourstack.backoffice.mapper.EntityMapper;
import org.fourstack.backoffice.mapper.ResponseMapper;
import org.fourstack.backoffice.model.AiRequest;
import org.fourstack.backoffice.model.AiResponse;
import org.fourstack.backoffice.model.BackOfficeListResponse;
import org.fourstack.backoffice.model.BackOfficeResponse;
import org.fourstack.backoffice.model.EncryptionDetails;
import org.fourstack.backoffice.model.OuResponse;
import org.fourstack.backoffice.model.UpdateAiRequest;
import org.fourstack.backoffice.repository.AiEntityRepository;
import org.fourstack.backoffice.repository.OuEntityRepository;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.fourstack.backoffice.util.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class MasterDataService {
    private final AiEntityRepository aiRepository;
    private final OuEntityRepository ouRepository;
    private final ResponseMapper responseMapper;
    private final EntityMapper entityMapper;

    public ResponseEntity<BackOfficeListResponse> retrieveAiEntities() {
        List<AgentInstitutionEntity> aiEntities = aiRepository.findAll();
        if (BackOfficeUtil.isCollectionNotNullOrEmpty(aiEntities)) {
            List<AiResponse> aiResponses = aiEntities.stream()
                    .map(responseMapper::mapAiEntityToResponse)
                    .toList();
            return generateResponse(responseMapper.constructListResponse(aiResponses), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureListResponse(ErrorScenarioCode.BO_AI_0001,
                    null), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<BackOfficeResponse> retrieveAiEntity(String aiId) {
        String entityKey = KeyGenerationUtil.generateAiEntityKey(aiId);
        Optional<AgentInstitutionEntity> aiEntity = aiRepository.findById(entityKey);
        return aiEntity.map(entity -> generateResponse(responseMapper.constructResponse(entity), HttpStatus.OK))
                .orElseGet(() -> generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_0002,
                        "aiId"), HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<BackOfficeResponse> createAiEntity(AiRequest request) {
        AgentInstitutionEntity aiEntity = entityMapper.convertToAiEntity(request);
        String entityKey = KeyGenerationUtil.generateAiEntityKey(aiEntity.getId());
        aiEntity.setKey(entityKey);
        AgentInstitutionEntity savedObject = aiRepository.save(aiEntity);
        return generateResponse(responseMapper.constructResponse(savedObject), HttpStatus.CREATED);
    }

    public ResponseEntity<BackOfficeResponse> updateAiEntity(String aiId, UpdateAiRequest request) {
        String entityKey = KeyGenerationUtil.generateAiEntityKey(aiId);
        Optional<AgentInstitutionEntity> optionalEntity = aiRepository.findById(entityKey);
        if (optionalEntity.isPresent()) {
            AgentInstitutionEntity entity = optionalEntity.get();
            entityMapper.updateAiEntity(entity, request);
            aiRepository.save(entity);
            return generateResponse(responseMapper.constructResponse(entity), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_0002,
                    "aiId"), HttpStatus.NOT_FOUND);
        }
    }

    public BackOfficeResponse updateEncryptionDetails(String aiId, EncryptionDetails encryptionDetails) {
        String entityKey = KeyGenerationUtil.generateAiEntityKey(aiId);
        Optional<AgentInstitutionEntity> aiEntity = aiRepository.findById(entityKey);
        if (aiEntity.isPresent()) {
            AgentInstitutionEntity entity = aiEntity.get();
            entityMapper.updateAiEntity(entity, encryptionDetails);
            aiRepository.save(entity);
            return responseMapper.constructResponse(entity);
        } else {
            return responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_0002, "aiId");
        }
    }

    public BackOfficeListResponse retrieveOuEntities() {
        List<OperationUnitEntity> entities = ouRepository.findAll();
        if (BackOfficeUtil.isCollectionNotNullOrEmpty(entities)) {
            List<OuResponse> ouResponseList = entities.stream()
                    .map(responseMapper::mapOuEntityToResponse)
                    .toList();
            return responseMapper.constructListResponse(ouResponseList);
        } else {
            return responseMapper.constructFailureListResponse(ErrorScenarioCode.BO_OU_0001, null);
        }
    }

    private ResponseEntity<BackOfficeResponse> generateResponse(BackOfficeResponse response, HttpStatus status) {
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<BackOfficeListResponse> generateResponse(BackOfficeListResponse response, HttpStatus status) {
        return ResponseEntity.status(status).body(response);
    }
}
