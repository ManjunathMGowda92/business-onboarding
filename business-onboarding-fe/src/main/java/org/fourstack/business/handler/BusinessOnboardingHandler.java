package org.fourstack.business.handler;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.enums.EventWebhookType;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CheckBusinessRequest;
import org.fourstack.business.model.SearchBusinessRequest;
import org.fourstack.business.service.BusinessService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/onboarding/business")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessOnboardingHandler {
    private final BusinessService businessService;

    @PostMapping(value = "/reqRegisterEntity", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Acknowledgement> createNewBusiness(@RequestBody BusinessRegisterRequest businessRequest) {
        return businessService.processRequest(businessRequest,
                EventWebhookType.REQ_CREATE_BUSINESS.getEndPoint());
    }

    @PostMapping(value = "/reqRegisterId", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Acknowledgement> createB2BId(@RequestBody B2BIdRegisterRequest businessRequest) {
        return businessService.processRequest(businessRequest,
                EventWebhookType.REQ_ADD_B2B.getEndPoint());
    }

    @PostMapping(value = "/reqCheckEntity", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Acknowledgement> checkBusinessExist(@RequestBody CheckBusinessRequest businessRequest) {
        return businessService.processRequest(businessRequest,
                EventWebhookType.REQ_CHECK_BUSINESS.getEndPoint());
    }

    @PostMapping(value = "/reqSearchEntity", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Acknowledgement> searchBusiness(@RequestBody SearchBusinessRequest businessRequest) {
        return businessService.processRequest(businessRequest,
                EventWebhookType.REQ_SEARCH_BUSINESS.getEndPoint());
    }
}
