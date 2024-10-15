package org.fourstack.backoffice.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.entity.AiOuMappingEntity;
import org.fourstack.backoffice.entity.OperationUnitEntity;
import org.fourstack.backoffice.enums.ErrorScenarioCode;
import org.fourstack.backoffice.mapper.EntityMapper;
import org.fourstack.backoffice.mapper.ResponseMapper;
import org.fourstack.backoffice.model.AiOuEncryptionDetailsRequest;
import org.fourstack.backoffice.model.AiOuMappingRequest;
import org.fourstack.backoffice.model.AiOuMappingResponse;
import org.fourstack.backoffice.model.AiRequest;
import org.fourstack.backoffice.model.AiResponse;
import org.fourstack.backoffice.model.BackOfficeListResponse;
import org.fourstack.backoffice.model.BackOfficeResponse;
import org.fourstack.backoffice.model.OuRequest;
import org.fourstack.backoffice.model.OuResponse;
import org.fourstack.backoffice.model.UpdateAiRequest;
import org.fourstack.backoffice.repository.AiEntityRepository;
import org.fourstack.backoffice.repository.AiOuMappingRepository;
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
    private final AiOuMappingRepository aiOuRepository;
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

    public ResponseEntity<BackOfficeResponse> updateEncryptionDetails(AiOuEncryptionDetailsRequest encryptionDetails) {
        String entityKey = KeyGenerationUtil.generateAiOuEntityKey(encryptionDetails.getAiId(), encryptionDetails.getOuId());
        Optional<AiOuMappingEntity> optionalEntity = aiOuRepository.findById(entityKey);
        if (optionalEntity.isPresent()) {
            AiOuMappingEntity entity = optionalEntity.get();
            entityMapper.updateAiOuEntity(entity, encryptionDetails);
            aiOuRepository.save(entity);
            return generateResponse(responseMapper.constructResponse(entity), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_0002,
                    "aiId"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<BackOfficeListResponse> retrieveOuEntities() {
        List<OperationUnitEntity> entities = ouRepository.findAll();
        if (BackOfficeUtil.isCollectionNotNullOrEmpty(entities)) {
            List<OuResponse> ouResponseList = entities.stream()
                    .map(responseMapper::mapOuEntityToResponse)
                    .toList();
            return generateResponse(responseMapper.constructListResponse(ouResponseList), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureListResponse(ErrorScenarioCode.BO_OU_0001,
                    null), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<BackOfficeResponse> retrieveOuEntity(String ouId) {
        String entityKey = KeyGenerationUtil.generateOuEntityKey(ouId);
        Optional<OperationUnitEntity> optionalEntity = ouRepository.findById(entityKey);
        if (optionalEntity.isPresent()) {
            OuResponse response = responseMapper.mapOuEntityToResponse(optionalEntity.get());
            return generateResponse(responseMapper.constructResponse(response), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_OU_0002,
                    "ouId"), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<BackOfficeResponse> createOuEntity(OuRequest request) {
        OperationUnitEntity entity = entityMapper.convertToOuEntity(request);
        String entityKey = KeyGenerationUtil.generateOuEntityKey(entity.getId());
        entity.setKey(entityKey);
        OperationUnitEntity savedEntity = ouRepository.save(entity);
        OuResponse response = responseMapper.mapOuEntityToResponse(savedEntity);
        return generateResponse(responseMapper.constructResponse(response), HttpStatus.CREATED);
    }

    public ResponseEntity<BackOfficeListResponse> retrieveAiOuEntities() {
        List<AiOuMappingEntity> entities = aiOuRepository.findAll();
        return generateAiOuEntityResponse(entities, ErrorScenarioCode.BO_AI_OU_001, null);
    }

    public ResponseEntity<BackOfficeListResponse> retrieveAiOuEntities(String aiId) {
        List<AiOuMappingEntity> entities = aiOuRepository.findAllByAiId(aiId);
        return generateAiOuEntityResponse(entities, ErrorScenarioCode.BO_AI_OU_003, "aiId");
    }

    private ResponseEntity<BackOfficeListResponse> generateAiOuEntityResponse(List<AiOuMappingEntity> entities,
                                                                              ErrorScenarioCode scenarioCode,
                                                                              String fieldName) {
        if (BackOfficeUtil.isCollectionNotNullOrEmpty(entities)) {
            List<AiOuMappingResponse> responses = entities.stream()
                    .map(responseMapper::mapToAiOuResponse)
                    .toList();
            return generateResponse(responseMapper.constructListResponse(responses), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureListResponse(scenarioCode,
                    fieldName), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<BackOfficeResponse> retrieveAiOuEntity(String aiId, String ouId) {
        String entityKey = KeyGenerationUtil.generateAiOuEntityKey(aiId, ouId);
        Optional<AiOuMappingEntity> optionalEntity = aiOuRepository.findById(entityKey);
        if (optionalEntity.isPresent()) {
            AiOuMappingResponse response = responseMapper.mapToAiOuResponse(optionalEntity.get());
            return generateResponse(responseMapper.constructResponse(response), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_OU_002,
                    null), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<BackOfficeResponse> generateResponse(BackOfficeResponse response, HttpStatus status) {
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<BackOfficeListResponse> generateResponse(BackOfficeListResponse response, HttpStatus status) {
        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<BackOfficeResponse> createAiOuEntity(AiOuMappingRequest request) {
        AiOuMappingEntity entity = entityMapper.convertToAiOuMapEntity(request);
        String entityKey = KeyGenerationUtil.generateAiOuEntityKey(request.getAiId(), request.getOuId());
        entity.setKey(entityKey);
        AiOuMappingEntity savedObj = aiOuRepository.save(entity);
        AiOuMappingResponse response = responseMapper.mapToAiOuResponse(savedObj);
        return generateResponse(responseMapper.constructResponse(response), HttpStatus.CREATED);
    }
}
