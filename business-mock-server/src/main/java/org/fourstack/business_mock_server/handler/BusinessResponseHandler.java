package org.fourstack.business_mock_server.handler;

import lombok.RequiredArgsConstructor;
import org.fourstack.business_mock_server.model.Acknowledgement;
import org.fourstack.business_mock_server.model.B2BIdRegisterResponse;
import org.fourstack.business_mock_server.model.BusinessRegisterResponse;
import org.fourstack.business_mock_server.model.CheckBusinessResponse;
import org.fourstack.business_mock_server.model.SearchBusinessResponse;
import org.fourstack.business_mock_server.service.BusinessResponseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/onboarding/business")
@RequiredArgsConstructor
public class BusinessResponseHandler {
    private final BusinessResponseService responseService;

    @PostMapping("respRegisterEntity")
    public Acknowledgement acceptBusinessResponse(@RequestBody BusinessRegisterResponse response) {
        return responseService.processResponseData(response, "/api/v1/onboarding/business/respRegisterEntity");
    }

    @PostMapping("respRegisterId")
    public Acknowledgement acceptB2BResponse(@RequestBody B2BIdRegisterResponse response) {
        return responseService.processResponseData(response, "/v1/onboarding/business/respRegisterId");
    }

    @PostMapping("respCheckEntity")
    public Acknowledgement acceptCheckBusinessResponse(@RequestBody CheckBusinessResponse response) {
        return responseService.processResponseData(response, "/v1/onboarding/business/respCheckEntity");
    }

    @PostMapping("respSearchEntity")
    public Acknowledgement acceptSearchResponse(@RequestBody SearchBusinessResponse response) {
        return responseService.processResponseData(response, "v1/onboarding/business/respSearchEntity");
    }
}
