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
import org.fourstack.backoffice.model.AiUpdateRequest;
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
    private final KafkaPublisherService publisherService;

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
        Optional<AgentInstitutionEntity> aiEntity = retrieveAgentInstitution(aiId);
        return aiEntity.map(entity -> generateResponse(responseMapper.constructResponse(entity), HttpStatus.OK))
                .orElseGet(() -> generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_0002,
                        "aiId"), HttpStatus.NOT_FOUND));
    }

    private Optional<AgentInstitutionEntity> retrieveAgentInstitution(String aiId) {
        String entityKey = KeyGenerationUtil.generateAiEntityKey(aiId);
        return aiRepository.findById(entityKey);
    }

    public ResponseEntity<BackOfficeResponse> createAiEntity(AiRequest request) {
        Optional<AgentInstitutionEntity> optionalAiEntity = retrieveAgentInstitution(request.getAgentInstitutionId());
        if (optionalAiEntity.isPresent()) {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_0003,
                    "agentInstitutionId"), HttpStatus.BAD_REQUEST);
        }
        AgentInstitutionEntity aiEntity = entityMapper.convertToAiEntity(request);
        String entityKey = KeyGenerationUtil.generateAiEntityKey(aiEntity.getId());
        aiEntity.setKey(entityKey);
        AgentInstitutionEntity savedObject = aiRepository.save(aiEntity);
        publisherService.publishAiDetails(savedObject);
        return generateResponse(responseMapper.constructResponse(savedObject), HttpStatus.CREATED);
    }

    public ResponseEntity<BackOfficeResponse> updateAiEntity(String aiId, AiUpdateRequest request) {
        Optional<AgentInstitutionEntity> optionalEntity = retrieveAgentInstitution(aiId);
        if (optionalEntity.isPresent()) {
            AgentInstitutionEntity entity = optionalEntity.get();
            entityMapper.updateAiEntity(entity, request);
            AgentInstitutionEntity savedObject = aiRepository.save(entity);
            publisherService.publishAiDetails(savedObject);
            return generateResponse(responseMapper.constructResponse(entity), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_0002,
                    "aiId"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<BackOfficeResponse> updateEncryptionDetails(AiOuEncryptionDetailsRequest encryptionDetails) {
        Optional<AiOuMappingEntity> optionalEntity = retrieveAiOuMapEntity(encryptionDetails.getAiId(), encryptionDetails.getOuId());
        if (optionalEntity.isPresent()) {
            AiOuMappingEntity entity = optionalEntity.get();
            entityMapper.updateAiOuEntity(entity, encryptionDetails);
            AiOuMappingEntity savedObject = aiOuRepository.save(entity);
            publisherService.publishAiOuDetails(savedObject);
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
        Optional<OperationUnitEntity> optionalEntity = retrieveOperationUnit(ouId);
        if (optionalEntity.isPresent()) {
            OuResponse response = responseMapper.mapOuEntityToResponse(optionalEntity.get());
            return generateResponse(responseMapper.constructResponse(response), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_OU_0002,
                    "ouId"), HttpStatus.NOT_FOUND);
        }

    }

    private Optional<OperationUnitEntity> retrieveOperationUnit(String ouId) {
        String entityKey = KeyGenerationUtil.generateOuEntityKey(ouId);
        return ouRepository.findById(entityKey);
    }

    public ResponseEntity<BackOfficeResponse> createOuEntity(OuRequest request) {
        Optional<OperationUnitEntity> operationUnitEntity = retrieveOperationUnit(request.getOuId());
        if (operationUnitEntity.isPresent()) {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_OU_0003,
                    "ouId"), HttpStatus.BAD_REQUEST);
        }
        OperationUnitEntity entity = entityMapper.convertToOuEntity(request);
        String entityKey = KeyGenerationUtil.generateOuEntityKey(entity.getId());
        entity.setKey(entityKey);
        OperationUnitEntity savedEntity = ouRepository.save(entity);
        publisherService.publishOuDetails(savedEntity);
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
        Optional<AiOuMappingEntity> optionalEntity = retrieveAiOuMapEntity(aiId, ouId);
        if (optionalEntity.isPresent()) {
            AiOuMappingResponse response = responseMapper.mapToAiOuResponse(optionalEntity.get());
            return generateResponse(responseMapper.constructResponse(response), HttpStatus.OK);
        } else {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_OU_002,
                    null), HttpStatus.NOT_FOUND);
        }
    }

    private Optional<AiOuMappingEntity> retrieveAiOuMapEntity(String aiId, String ouId) {
        String entityKey = KeyGenerationUtil.generateAiOuEntityKey(aiId, ouId);
        return aiOuRepository.findById(entityKey);
    }

    private ResponseEntity<BackOfficeResponse> generateResponse(BackOfficeResponse response, HttpStatus status) {
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<BackOfficeListResponse> generateResponse(BackOfficeListResponse response, HttpStatus status) {
        return ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<BackOfficeResponse> createAiOuEntity(AiOuMappingRequest request) {
        Optional<AiOuMappingEntity> aiOuMappingEntity = retrieveAiOuMapEntity(request.getAiId(), request.getOuId());
        if (aiOuMappingEntity.isPresent()) {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_OU_004,
                    null), HttpStatus.BAD_REQUEST);
        }

        Optional<AgentInstitutionEntity> agentInstitutionEntity = retrieveAgentInstitution(request.getAiId());
        if (agentInstitutionEntity.isEmpty()) {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_OU_005,
                    "aiId"), HttpStatus.BAD_REQUEST);
        }

        Optional<OperationUnitEntity> operationUnitEntity = retrieveOperationUnit(request.getOuId());
        if (operationUnitEntity.isEmpty()) {
            return generateResponse(responseMapper.constructFailureResponse(ErrorScenarioCode.BO_AI_OU_006,
                    "ouId"), HttpStatus.BAD_REQUEST);
        }
        AiOuMappingEntity entity = entityMapper.convertToAiOuMapEntity(request);
        String entityKey = KeyGenerationUtil.generateAiOuEntityKey(request.getAiId(), request.getOuId());
        entity.setKey(entityKey);
        AiOuMappingEntity savedObj = aiOuRepository.save(entity);
        publisherService.publishAiOuDetails(savedObj);
        AiOuMappingResponse response = responseMapper.mapToAiOuResponse(savedObj);
        return generateResponse(responseMapper.constructResponse(response), HttpStatus.CREATED);
    }
}
